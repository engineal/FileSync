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
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
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
 * @version Aug 11, 2015
 */
public class DirectoryCrawler implements Runnable {

    private static final Logger log = Logger.getLogger(FileSync.class.getName());

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

    private synchronized void compareDirs(SyncDirectory directory, List<Path> directories) throws Exception {
        Queue<SyncFile> syncFiles = new LinkedList<>(directory);
        List<File>[] actualFiles = new List[directories.size()];

        for (int i = 0; i < directories.size(); i++) {
            List<File> fileList = new ArrayList<>();
            fileList.addAll(Arrays.asList(directories.get(i).toFile().listFiles()));
            actualFiles[i] = fileList;
        }

        // Process all known files
        while (!syncFiles.isEmpty()) {
            if (checkStatus()) {
                return;
            }

            fireSyncStatusEvent(true, false, stats);

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
            }
        }

        // Process all new files
        for (List<File> actualFile : actualFiles) {
            if (checkStatus()) {
                return;
            }

            List<File> fileList = actualFile;
            for (File file : fileList) {
                FileCompare comparer = new FileCompare(file);
                comparer.resolveConflict();
            }
        }

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
            state = CrawlState.Running;
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
