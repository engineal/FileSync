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
 * The time intervals that can be used for the sync schedule
 *
 * @author Aaron Lucia
 * @version Jul 23, 2015
 */
public enum SyncInterval {

    /**
     * One minute, 60 seconds
     */
    MINUTE("Minute", 60),
    /**
     * One hour, 3600 seconds
     */
    HOUR("Hour", 3600),
    /**
     * One day, 86400 seconds
     */
    DAY("Day", 86400),
    /**
     * One week, 604800 seconds
     */
    WEEK("Day", 604800),
    /**
     * One month, 2592000 seconds
     */
    MONTH("Month", 2592000),
    /**
     * One year, 946080000 seconds
     */
    YEAR("Year", 946080000);

    private final String displayText;
    private final int delayS;

    private SyncInterval(String displayText, int delayS) {
        this.displayText = displayText;
        this.delayS = delayS;
    }

    /**
     * Get the text to display associated with this interval
     *
     * @return
     */
    public String getDisplayText() {
        return displayText;
    }

    /**
     * Get the seconds associated with this interval
     *
     * @return
     */
    public int getDelayS() {
        return delayS;
    }

    @Override
    public String toString() {
        return displayText;
    }
}
