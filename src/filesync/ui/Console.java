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
package filesync.ui;

/**
 *
 * @author Aaron Lucia
 * @version Dec 18, 2014
 */
public class Console {

    private boolean tray;
    private boolean settings;
    private boolean sync;

    public Console(String[] args) {
        tray = args.length == 0;
        settings = false;
        sync = false;

        for (String arg : args) {
            switch (arg) {
                case "tray":
                    tray = true;
                    break;
                case "settings":
                    tray = true;
                    settings = true;
                    break;
                case "sync":
                    sync = true;
                    break;
            }
        }
    }

    public boolean isTray() {
        return tray;
    }
    
    public boolean isSettings() {
        return settings;
    }
    
    public boolean isSync() {
        return settings;
    }
}
