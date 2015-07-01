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

import java.util.EventObject;

/**
 *
 * @author Aaron Lucia
 * @version Dec 16, 2014
 */
public class StatusEvent extends EventObject {

    private final boolean syncing;
    private final double percent;

    public StatusEvent(Object source, boolean syncing, boolean paused) {
        super(source);
        this.syncing = syncing;
        this.percent = 0;
    }
    
    public StatusEvent(Object source, boolean syncing, double percent) {
        super(source);
        this.syncing = syncing;
        this.percent = percent;
    }

    public boolean isSyncing() {
        return syncing;
    }

    public double getPercent() {
        return percent;
    }
}
