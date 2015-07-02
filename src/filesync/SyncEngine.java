/*
 * Copyright (C) 2015 Aaron Lucia
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package filesync;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aaron Lucia
 * @version Dec 16, 2014
 */
public class SyncEngine implements Runnable {

    private static final Logger log = Logger.getLogger(FileSync.class.getName());

    private static void copyFile(File file, File directory) throws IOException {
        Path source = Paths.get(file.getAbsolutePath());
        Path destination = Paths.get(directory.getAbsolutePath() + "\\" + file.getName());
        log.log(Level.FINE, "Copying {0} to {1}", new Object[]{source, destination});
        Files.copy(source, destination, REPLACE_EXISTING, COPY_ATTRIBUTES);
    }

    private static void removeFile(File file) throws IOException {
        Path source = Paths.get(file.getAbsolutePath());
        log.log(Level.FINE, "Removing {0}", source);
        Files.delete(source);
    }

    private final SyncIndex index;
    private boolean paused;
    private int fileCount;
    private int filesAdded;
    private int filesUnmodified;
    private int filesModified;
    private int filesRemoved;
    private int processedCount;
    private final Thread thread;
    private final ArrayList<StatusListener> _statusListeners;

    public SyncEngine(SyncIndex index) {
        this.index = index;
        paused = false;
        thread = new Thread(this, "FileSync Engine");
        _statusListeners = new ArrayList<>();
    }

    private synchronized void compareDirs(SyncIndex index) throws IOException, InterruptedException {
        fileCount += index.fileCount();
        filesAdded = 0;
        filesUnmodified = 0;
        filesModified = 0;
        filesRemoved = 0;
        processedCount = 0;
        compareDirs(index, index.getDirectories());
        log.log(Level.FINE, "Total files: {0}\nTotal files added: {1}\nTotal files unmodified: {2}\nTotal files modified: {3}\nTotal files removed: {4}", new Object[]{fileCount, filesAdded, filesUnmodified, filesModified, filesRemoved});
        fireSyncStatusEvent(false, 0);
    }

    private synchronized void compareDirs(SyncDirectory index, File dir1, File dir2) throws IOException, InterruptedException {
        Queue<SyncFile> files = new LinkedList<>(index);
        FileQueue dir1Files = new FileQueue(dir1.listFiles());
        FileQueue dir2Files = new FileQueue(dir2.listFiles());

        while (!files.isEmpty()) {
            fireSyncStatusEvent(true, (double) processedCount / fileCount);
            if (paused) {
                wait();
            }
            SyncFile file = files.remove();
            File file1 = dir1Files.remove(file.getName());
            File file2 = dir2Files.remove(file.getName());

            if (file1 == null || file2 == null) {
                if (file1 != null) {
                    removeFile(file1);
                }
                if (file2 != null) {
                    removeFile(file2);
                }
                index.remove(file);
                processedCount++;
                filesRemoved++;
                log.log(Level.FINE, "{0} removed from database", file);
            } else if (file1.lastModified() != file2.lastModified() || file1.length() != file2.length()) {
                if (file1.lastModified() > file2.lastModified()) {
                    copyFile(file1, dir2);
                } else {
                    copyFile(file2, dir1);
                }
                processedCount++;
                filesModified++;
                log.log(Level.FINE, "{0} modified", file);
            } else {
                processedCount++;
                filesUnmodified++;
                log.log(Level.FINE, "{0} unmodified", file);
            }

            if (file1 != null && file2 != null && file instanceof SyncDirectory) {
                compareDirs((SyncDirectory) file, file1, file2);
            }
        }

        fileCount += dir1Files.size();
        while (!dir1Files.isEmpty()) {
            fireSyncStatusEvent(true, (double) processedCount / fileCount);
            if (paused) {
                wait();
            }
            File file1 = dir1Files.remove();
            File file2 = dir2Files.remove(file1.getName());

            if (file2 != null) {
                if (file1.lastModified() != file2.lastModified() || file1.length() != file2.length()) {
                    if (file1.lastModified() > file2.lastModified()) {
                        copyFile(file1, dir2);
                    } else {
                        copyFile(file2, dir1);
                    }
                }
            } else {
                copyFile(file1, dir2);
            }

            if (file1.isDirectory()) {
                SyncDirectory file = new SyncDirectory(file1.getName());
                compareDirs((SyncDirectory) file, file1, file2);
                index.add(file);
                processedCount++;
                filesAdded++;
                log.log(Level.FINE, "{0} added to database", file);
            } else {
                SyncFile file = new SyncFile(file1.getName());
                index.add(file);
                processedCount++;
                filesAdded++;
                log.log(Level.FINE, "{0} added to database", file);
            }
        }

        fileCount += dir2Files.size();
        while (!dir2Files.isEmpty()) {
            fireSyncStatusEvent(true, (double) processedCount / fileCount);
            if (paused) {
                wait();
            }
            File file2 = dir2Files.remove();

            copyFile(file2, dir1);

            if (file2.isDirectory()) {
                SyncDirectory file = new SyncDirectory(file2.getName());
                compareDirs((SyncDirectory) file, file2, file2);
                index.add(file);
                processedCount++;
                log.log(Level.FINE, "{0} added to database", file);
            } else {
                SyncFile file = new SyncFile(file2.getName());
                index.add(file);
                processedCount++;
                log.log(Level.FINE, "{0} added to database", file);
            }
        }
    }

    @Override
    public void run() {
        try {
            compareDirs(index);
        } catch (IOException | InterruptedException ex) {
            log.log(Level.SEVERE, ex.toString(), ex);
        }
    }

    public synchronized boolean start() {
        if (!thread.isAlive()) {
            thread.start();
            return true;
        }
        return false;
    }

    public synchronized void pause() {
        if (thread.isAlive()) {
            if (!paused) {
                paused = true;
            } else {
                paused = false;
                this.notify();
            }
        }
    }

    public synchronized boolean isPaused() {
        return paused;
    }

    public SyncIndex getIndex() {
        return index;
    }

    /**
     *
     * @param listener
     */
    public synchronized void addStatusListener(StatusListener listener) {
        if (!_statusListeners.contains(listener)) {
            _statusListeners.add(listener);
        }
    }

    /**
     *
     * @param listener
     */
    public synchronized void removeStatusListener(StatusListener listener) {
        _statusListeners.remove(listener);
    }

    /**
     *
     * @param status
     */
    private synchronized void fireSyncStatusEvent(boolean status, double percent) {
        StatusEvent event = new StatusEvent(this, status, percent);
        for (StatusListener listener : _statusListeners) {
            listener.statusUpdated(event);
        }
    }
}
