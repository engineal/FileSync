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
package filesync.engine;

import filesync.FileSync;
import filesync.StatusEvent;
import filesync.StatusListener;
import filesync.SyncDirectory;
import filesync.SyncFile;
import filesync.SyncIndex;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
    private SyncStats stats;
    private boolean paused;
    private final Thread thread;
    private final List<StatusListener> _statusListeners;

    public SyncEngine(SyncIndex index) {
        this.index = index;
        paused = false;
        thread = new Thread(this, "FileSync Engine");
        _statusListeners = new ArrayList<>();

        try {
            WatchService watcher = FileSystems.getDefault().newWatchService();

            for (Path directory : index.getDirectories()) {
                WatchKey key = directory.register(watcher,
                        ENTRY_CREATE,
                        ENTRY_DELETE,
                        ENTRY_MODIFY);
            }
        } catch (IOException ex) {
            log.log(Level.SEVERE, null, ex);
        }
    }

    private synchronized void compareDirs(SyncDirectory index, List directories) throws IOException, InterruptedException {
        Queue<SyncFile> files = new LinkedList<>(index);
        Queue<File> dir1Files = new LinkedList<File>(dir1.listFiles());
        Queue<File> dir2Files = new LinkedList<File>(dir2.listFiles());

        while (!files.isEmpty()) {
            fireSyncStatusEvent(true, stats);
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
                stats.increaseFilesRemoved();
                log.log(Level.FINE, "{0} removed from database", file);
            } else if (file1.lastModified() != file2.lastModified() || file1.length() != file2.length()) {
                if (file1.lastModified() > file2.lastModified()) {
                    copyFile(file1, dir2);
                } else {
                    copyFile(file2, dir1);
                }
                stats.increaseFilesModified();
                log.log(Level.FINE, "{0} modified", file);
            } else {
                stats.increaseFilesUnmodified();
                log.log(Level.FINE, "{0} unmodified", file);
            }
            stats.increaseProcessedCount();

            if (file1 != null && file2 != null && file instanceof SyncDirectory) {
                compareDirs((SyncDirectory) file, file1, file2);
            }
        }

        stats.increaseFileCount(dir1Files.size());
        while (!dir1Files.isEmpty()) {
            fireSyncStatusEvent(true, stats);
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

            SyncFile file;
            if (file1.isDirectory()) {
                file = new SyncDirectory(file1.getName());
                compareDirs((SyncDirectory) file, file1, file2);
            } else {
                file = new SyncFile(file1.getName());
            }
            index.add(file);
            stats.increaseProcessedCount();
            stats.increaseFilesAdded();
            log.log(Level.FINE, "{0} added to database", file);
        }

        stats.increaseFileCount(dir2Files.size());
        while (!dir2Files.isEmpty()) {
            fireSyncStatusEvent(true, stats);
            if (paused) {
                wait();
            }
            File file2 = dir2Files.remove();

            copyFile(file2, dir1);

            SyncFile file;
            if (file2.isDirectory()) {
                file = new SyncDirectory(file2.getName());
                compareDirs((SyncDirectory) file, file2, file2);
            } else {
                file = new SyncFile(file2.getName());
            }
            index.add(file);
            stats.increaseProcessedCount();
            stats.increaseFilesAdded();
            log.log(Level.FINE, "{0} added to database", file);
        }
    }

    @Override
    public void run() {
        try {
            stats = new SyncStats(index.fileCount());
            compareDirs(index, index.getDirectories());
            log.log(Level.FINE, stats.toString());
            fireSyncStatusEvent(false, stats);
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
    private synchronized void fireSyncStatusEvent(boolean status, SyncStats stats) {
        StatusEvent event = new StatusEvent(this, status, stats);
        for (StatusListener listener : _statusListeners) {
            listener.statusUpdated(event);
        }
    }
}
