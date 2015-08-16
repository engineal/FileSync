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
import static filesync.engine.FileCompare.SyncAction;
import filesync.io.SaveSyncIndex;
import java.io.File;
import java.io.IOException;
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

    private Thread thread;
    private final SyncIndex index;
    private CrawlState state;
    private SyncStats stats;
    private final List<SyncListener> _syncListeners;

    public DirectoryCrawler(SyncIndex index, List<SyncListener> _syncListeners) {
        this.index = index;
        state = CrawlState.Stopped;
        stats = new SyncStats(index.fileCount());
        this._syncListeners = _syncListeners;
    }

    @Override
    public void run() {
        System.out.println("Starting crawl");
        state = CrawlState.Running;
        stats = new SyncStats(index.fileCount());

        try {
            compareDirs(index, index.getDirectories());
        } catch (Exception ex) {
            log.log(Level.SEVERE, ex.getMessage(), ex);
        }

        state = CrawlState.Stopped;
        fireSyncEvent();
        System.out.println("Crawl finished");

        try {
            SaveSyncIndex.save(new File("Data", index.getName() + ".json"), index);
            log.log(Level.FINE, "Saved {0}", index);
        } catch (IOException ex) {
            log.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    protected synchronized void compareDirs(SyncDirectory directory, List<File> directories) throws Exception {
        // Add all new files
        for (File dir : directories) {
            File[] fileList = dir.listFiles();
            if (fileList != null) {
                for (File file : fileList) {
                    if (!containsFile(directory, file)) {
                        if (file.isDirectory()) {
                            directory.add(new SyncDirectory(file, true));
                        } else {
                            directory.add(new SyncFile(file, true));
                        }
                    }
                }
            }
        }

        List<SyncFile> removedFiles = new ArrayList<>();

        stats.setFileCount(index.fileCount());
        fireSyncEvent();

        // Process all known files
        for (SyncFile syncFile : directory) {
            if (checkStatus()) {
                return;
            }

            // Recursivly process directories
            if (syncFile instanceof SyncDirectory) {
                List<File> newDirectories = new ArrayList<>();
                for (File dir : directories) {
                    newDirectories.add(new File(dir, syncFile.getName()));
                }
                compareDirs((SyncDirectory) syncFile, newDirectories);
            }

            // Compare files
            File[] files = new File[directories.size()];
            for (int i = 0; i < directories.size(); i++) {
                files[i] = new File(directories.get(i), syncFile.getName());
            }

            FileCompare comparer = new FileCompare(syncFile, files);
            SyncAction action = comparer.resolveConflict();
            stats.fileProcessed(action);
            if (action == SyncAction.Removed) {
                removedFiles.add(syncFile);
            }

            fireSyncEvent();
        }

        for (SyncFile syncFile : removedFiles) {
            directory.remove(syncFile);
        }
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
            thread = new Thread(this, index.getName() + "Crawler");
            thread.start();
        }
        fireSyncEvent();
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
        fireSyncEvent();
    }

    public void stopCrawl() {
        synchronized (thread) {
            state = CrawlState.Stopped;
            thread.notify();
        }
        fireSyncEvent();
    }

    private synchronized void fireSyncEvent() {
        SyncEvent event = new SyncEvent(this, state, stats, index);
        for (SyncListener listener : _syncListeners) {
            listener.statusUpdated(event);
        }
    }

    public enum CrawlState {

        Running, Paused, Stopped
    }
}
