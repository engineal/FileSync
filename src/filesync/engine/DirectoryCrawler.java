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
import filesync.SyncDirectory;
import filesync.SyncFile;
import filesync.SyncIndex;
import filesync.SyncStats;
import static filesync.engine.FileCompare.SyncAction.Removed;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aaron Lucia
 * @version Aug 11, 2015
 */
public class DirectoryCrawler implements Runnable {

    private static final Logger log = Logger.getLogger(FileSync.class.getName());

    private static boolean containsFile(SyncDirectory directory, File file) {
        for (SyncFile syncFile : directory) {
            if (syncFile.getName().equals(file.getName())) {
                return true;
            }
        }
        return false;
    }

    private final SyncIndex index;
    private final Thread thread;
    private CrawlState state;
    private SyncStats stats;
    private final List<SyncListener> _syncListeners;

    public DirectoryCrawler(SyncIndex index, List<SyncListener> _syncListeners) {
        this.index = index;
        thread = new Thread(this, index.getName() + "Crawler");
        state = CrawlState.Stopped;
        this._syncListeners = _syncListeners;
    }

    @Override
    public void run() {
        try {
            stats = new SyncStats(index.fileCount());
            compareDirs(index, index.getDirectories());
        } catch (Exception ex) {
            log.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    protected synchronized void compareDirs(SyncDirectory directory, List<Path> directories) throws Exception {
        // Add all new files
        for (Path dir : directories) {
            for (File file : dir.toFile().listFiles()) {
                if (!containsFile(directory, file)) {
                    if (file.isDirectory()) {
                        directory.add(new SyncDirectory(file));
                    } else {
                        directory.add(new SyncFile(file));
                    }
                }
            }
        }

        fireSyncStatusEvent(true, false, stats);

        // Process all known files
        for (SyncFile syncFile : directory) {
            if (checkStatus()) {
                return;
            }

            // Recursivly process directories
            if (syncFile instanceof SyncDirectory) {
                List<Path> newDirectories = new ArrayList<>();
                for (Path dir : directories) {
                    newDirectories.add(new File(dir.toFile(), syncFile.getName()).toPath());
                }
                compareDirs((SyncDirectory) syncFile, newDirectories);
            }

            // Compare files
            File[] files = new File[directories.size()];
            for (int i = 0; i < directories.size(); i++) {
                files[i] = new File(directories.get(i).toFile(), syncFile.getName());
            }

            FileCompare comparer = new FileCompare(syncFile, files);
            if (comparer.resolveConflict() == Removed) {
                directory.remove(syncFile);
            }

            fireSyncStatusEvent(true, false, stats);
        }

        state = CrawlState.Stopped;
        fireSyncStatusEvent(false, true, stats);
    }

    private synchronized boolean checkStatus() throws Exception {
        while (state == CrawlState.Paused) {
            wait();
        }

        switch (state) {
            case Running:
                return false;
            case Stopped:
                return true;
            default:
                throw new Exception("Invalid crawl state!");
        }
    }

    public CrawlState getState() {
        return state;
    }

    public void startCrawl() {
        if (state == CrawlState.Stopped) {
            state = CrawlState.Running;
            thread.start();
        }
    }

    public void pauseCrawl() {
        synchronized (thread) {
            switch (state) {
                case Running:
                    state = CrawlState.Paused;
                    break;
                case Paused:
                    state = CrawlState.Running;
                    thread.notify();
                    break;
            }
        }
    }

    public void stopCrawl() {
        synchronized (thread) {
            state = CrawlState.Stopped;
            thread.notify();
        }
    }

    /**
     *
     * @param status
     */
    private synchronized void fireSyncStatusEvent(boolean status, boolean done, SyncStats stats) {
        SyncEvent event = new SyncEvent(this, status, done, stats, index);
        for (SyncListener listener : _syncListeners) {
            listener.statusUpdated(event);
        }
    }

    public enum CrawlState {

        Running, Paused, Stopped
    }
}
