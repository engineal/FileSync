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

import java.io.Serializable;

/**
 *
 * @author Aaron Lucia
 * @version Dec 17, 2014
 */
public class SyncFile implements Serializable {

    private final String name;
    private long size;
    private long lastModified;

    public SyncFile(String name) {
        this.name = name;
    }
    
    public SyncFile(String name, long size, long lastModified) {
        this.name = name;
        this.size = size;
        this.lastModified = lastModified;
    }

    /**
     * Get the name of the file
     *
     * @return the value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the size of the file
     *
     * @return the value of size
     */
    public long getSize() {
        return size;
    }

    /**
     * Set the size of the file
     *
     * @param size new value of size
     */
    public void setSize(long size) {
        this.size = size;
    }

    /**
     * Get the last modified time of the file
     *
     * @return the value of lastModified
     */
    public long getLastModified() {
        return lastModified;
    }

    /**
     * Set the last modified time of the file
     *
     * @param lastModified new value of lastModified
     */
    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
