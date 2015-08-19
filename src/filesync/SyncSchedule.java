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

/**
 * Holds the schedule to sync
 *
 * @author Aaron Lucia
 * @version Jul 23, 2015
 */
public class SyncSchedule {

    private boolean enabled;
    private SyncInterval interval;
    private int repeat;

    /**
     * Create the default schedule
     */
    public SyncSchedule() {
        enabled = false;
        interval = SyncInterval.HOUR;
        repeat = 1;
    }

    /**
     * Check if the schedule is enabled
     *
     * @return
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Set if the schedule is enabled
     *
     * @param enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public SyncInterval getInterval() {
        return interval;
    }

    public void setInterval(SyncInterval interval) {
        this.interval = interval;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public int getDelayS() {
        return interval.getDelayS() * repeat;
    }
}
