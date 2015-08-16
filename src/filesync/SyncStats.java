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

import filesync.engine.FileCompare.SyncAction;

/**
 * Holds the stats during a directory crawl
 *
 * @author Aaron Lucia
 * @version Jul 2, 2015
 */
public class SyncStats {

    private int fileCount;
    private int filesAdded;
    private int filesUnchanged;
    private int filesModified;
    private int filesRemoved;

    /**
     * Create a new empty stat set with specified file count
     *
     * @param fileCount
     */
    public SyncStats(int fileCount) {
        this.fileCount = fileCount;
        filesAdded = 0;
        filesUnchanged = 0;
        filesModified = 0;
        filesRemoved = 0;
    }

    /**
     * Get the file count
     *
     * @return
     */
    public int getFileCount() {
        return fileCount;
    }

    /**
     * Set the file count to the specified amount
     *
     * @param fileCount
     */
    public void setFileCount(int fileCount) {
        this.fileCount = fileCount;
    }

    /**
     * Get the count of the files added
     *
     * @return
     */
    public int getFilesAdded() {
        return filesAdded;
    }

    /**
     * Get the count of the files unchanged
     *
     * @return
     */
    public int getFilesUnchanged() {
        return filesUnchanged;
    }

    /**
     * Get the count of the files modified
     *
     * @return
     */
    public int getFilesModified() {
        return filesModified;
    }

    /**
     * Get the count of the files removed
     *
     * @return
     */
    public int getFilesRemoved() {
        return filesRemoved;
    }

    public double getPercent() {
        if (fileCount > 0) {
            int processed = filesAdded + filesModified + filesRemoved + filesUnchanged;
            return processed / fileCount;
        } else {
            return 1.0;
        }
    }
    
    public void fileProcessed(SyncAction action) {
        switch (action) {
            case Added:
                filesAdded++;
                break;
            case Modified:
                filesModified++;
                break;
            case Removed:
                filesRemoved++;
                break;
            case Unchanged:
                filesUnchanged++;
                break;
        }
    }

    @Override
    public String toString() {
        return "Total files: " + fileCount
                + "\nTotal files added: " + filesAdded
                + "\nTotal files unchanged: " + filesUnchanged
                + "\nTotal files modified: " + filesModified
                + "\nTotal files removed: " + filesRemoved;
    }
}