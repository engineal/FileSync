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
import java.io.Serializable;

/**
 * Stores the last state of a file, so that it is known when comparing
 * directories
 *
 * @author Aaron Lucia
 * @version Aug 14, 2015
 */
public class SyncFile implements Serializable {

    private final String name;
    private long size;
    private long lastModified;
    private boolean added;

    /**
     * Create a new sync file from an existing file
     *
     * @param file the file to create the directory from
     */
    public SyncFile(File file) {
        this(file, false);
    }

    /**
     * Create a new sync file from an existing file, where the file has been
     * added
     *
     * @param file the file to create the directory from
     * @param added if the file was just added
     */
    public SyncFile(File file, boolean added) {
        this(file.getName(), file.length(), file.lastModified(), added);
    }

    /**
     * Create a new sync file from known attributes
     *
     * @param name
     * @param size
     * @param lastModified
     */
    public SyncFile(String name, long size, long lastModified) {
        this(name, size, lastModified, false);
    }

    /**
     * Create a new sync file from known attributes, where the file has been
     * added
     *
     * @param name
     * @param size
     * @param lastModified
     * @param added if the file was just added
     */
    public SyncFile(String name, long size, long lastModified, boolean added) {
        this.name = name;
        this.size = size;
        this.lastModified = lastModified;
        this.added = added;
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

    /**
     * Get if the file was just added
     *
     * @return if the file was added
     */
    public boolean isAdded() {
        return added;
    }

    /**
     * Clear that the file was just added
     */
    public void clearAdded() {
        added = false;
    }

    @Override
    public String toString() {
        return name;
    }
}
