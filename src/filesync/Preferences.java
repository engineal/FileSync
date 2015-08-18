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

import java.io.File;

/**
 *
 * @author Aaron Lucia
 * @version Aug 17, 2015
 */
public class Preferences {

    private File indexesLocation;
    private File logLocation;
    private File updateLocation;
    
    public Preferences(File installLocation) {
        indexesLocation = new File(installLocation, "data");
        logLocation = new File(new File(installLocation, "logs"), "log.log");
        updateLocation = new File(installLocation, "updates");
    }

    public File getIndexesLocation() {
        return indexesLocation;
    }

    public void setIndexesLocation(File indexesLocation) {
        this.indexesLocation = indexesLocation;
    }

    public File getLogLocation() {
        return logLocation;
    }

    public void setLogLocation(File logLocation) {
        this.logLocation = logLocation;
    }

    public File getUpdateLocation() {
        return updateLocation;
    }

    public void setUpdateLocation(File updateLocation) {
        this.updateLocation = updateLocation;
    }
}
