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

import filesync.SyncIndex;
import filesync.SyncStats;
import java.util.EventObject;

/**
 *
 * @author Aaron Lucia
 * @version Dec 16, 2014
 */
public class SyncEvent extends EventObject {

    private final boolean syncing;
    private final boolean done;
    private final SyncStats stats;
    private final SyncIndex index;

    public SyncEvent(Object source, boolean syncing, boolean done, SyncStats stats, SyncIndex index) {
        super(source);
        this.syncing = syncing;
        this.done = done;
        this.stats = stats;
        this.index = index;
    }

    public boolean isSyncing() {
        return syncing;
    }

    public boolean isDone() {
        return done;
    }

    public SyncStats getStats() {
        return stats;
    }

    public SyncIndex getIndex() {
        return index;
    }

    public double getPercent() {
        return stats.getProcessedCount() / stats.getFileCount();
    }
}