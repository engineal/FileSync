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
import filesync.SyncIndex;
import filesync.engine.DirectoryCrawler.CrawlState;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aaron Lucia
 * @version Dec 16, 2014
 */
public class SyncEngine {

    private static final Logger log = Logger.getLogger(FileSync.class.getName());

    private final SyncIndex index;
    private final DirectoryCrawler crawl;
    private final List<SyncListener> _syncListeners;

    public SyncEngine(SyncIndex index) {
        this.index = index;
        _syncListeners = new ArrayList<>();
        crawl = new DirectoryCrawler(index, _syncListeners);

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

    public CrawlState getState() {
        return crawl.getState();
    }

    public void startCrawl() {
        crawl.startCrawl();
    }

    public void pauseCrawl() {
        crawl.pauseCrawl();
    }

    public void stopCrawl() {
        crawl.stopCrawl();
    }

    /**
     * Add a listener
     *
     * @param listener the listener to add
     */
    public synchronized void addStatusListener(SyncListener listener) {
        if (!_syncListeners.contains(listener)) {
            _syncListeners.add(listener);
        }
    }

    /**
     * Remove a listener
     *
     * @param listener the listener to remove
     */
    public synchronized void removeStatusListener(SyncListener listener) {
        _syncListeners.remove(listener);
    }
}
