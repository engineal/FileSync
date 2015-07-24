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
 *
 * @author Aaron Lucia
 * @version Jul 23, 2015
 */
public enum SyncInterval {

    MINUTE("Minute", 60),
    HOUR("Hour", 60 * 60),
    DAY("Day", 60 * 60 * 24),
    WEEK("Day", 60 * 60 * 24 * 7),
    MONTH("Month", 60 * 60 * 24 * 30),
    YEAR("Year", 60 * 60 * 24 * 30 * 365);
    
    private final String displayText;
    private final int delayM;
    
    private SyncInterval(String displayText, int delayM) {
        this.displayText = displayText;
        this.delayM = delayM;
    }

    public String getDisplayText() {
        return displayText;
    }

    public int getDelayM() {
        return delayM;
    }
}
