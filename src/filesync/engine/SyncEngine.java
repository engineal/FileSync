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

import filesync.SyncStats;
import filesync.FileSync;
import filesync.StatusEvent;
import filesync.StatusListener;
import filesync.SyncDirectory;
import filesync.SyncFile;
import filesync.SyncIndex;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.Arrays;
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
public class SyncEngine extends Thread {

    private static final Logger log = Logger.getLogger(FileSync.class.getName());

    private final SyncIndex index;
    private boolean running;
    private boolean stop;
    private SyncStats stats;
    private final List<StatusListener> _statusListeners;

    public SyncEngine(SyncIndex index) {
        super("FileSync Engine");
        this.index = index;
        this.running = false;
        this.stop = true;
        
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
            log.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    private synchronized void compareDirs(SyncDirectory directory, List<Path> directories) throws IOException, InterruptedException {
        Queue<SyncFile> syncFiles = new LinkedList<>(directory);
        List<File>[] actualFiles = new List[directories.size()];

        for (int i = 0; i < directories.size(); i++) {
            List<File> fileList = new ArrayList<>();
            fileList.addAll(Arrays.asList(directories.get(i).toFile().listFiles()));
            actualFiles[i] = fileList;
        }

        // Process all known files
        while (!syncFiles.isEmpty()) {
            if (stop) {
                return;
            }

            while (!running) {
                wait();
            }

            fireSyncStatusEvent(true, stats);

            SyncFile syncFile = syncFiles.remove();
            File[] files = new File[directories.size()];

            for (int i = 0; i < actualFiles.length; i++) {
                List<File> fileList = actualFiles[i];
                for (File file : fileList) {
                    if (file.getName().equals(syncFile.getName())) {
                        fileList.remove(file);
                        files[i] = file;
                        break;
                    }
                }
            }

            FileCompare comparer = new FileCompare(syncFile, files);
            if (comparer.resolveConflict() == SyncAction.Removed) {
                directory.remove(syncFile);
                log.log(Level.FINE, "{0} removed from database", syncFile);
            }
        }

        // Process all new files
        for (int i = 0; i < actualFiles.length; i++) {
            if (stop) {
                return;
            }

            while (!running) {
                wait();
            }

            List<File> fileList = actualFiles[i];
            for (File file : fileList) {
                FileCompare comparer = new FileCompare(file);
                comparer.resolveConflict();
            }
        }
    }

    @Override
    public void run() {
        try {
            running = true;
            stop = false;
            stats = new SyncStats(index.fileCount());
            compareDirs(index, index.getDirectories());
            log.log(Level.FINE, stats.toString());
            fireSyncStatusEvent(false, stats);
        } catch (IOException | InterruptedException ex) {
            log.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    public SyncIndex getIndex() {
        return index;
    }

    public boolean isRunning() {
        return running;
    }

    public void pauseSync() {
        running = false;
    }

    public synchronized void resumeSync() {
        running = true;
        notify();
    }

    public void stopSync() {
        stop = true;
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
        StatusEvent event = new StatusEvent(this, status, stats, index);
        for (StatusListener listener : _statusListeners) {
            listener.statusUpdated(event);
        }
    }
}
