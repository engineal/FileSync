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
 * Holds the stats during a directory crawl
 *
 * @author Aaron Lucia
 * @version Jul 2, 2015
 */
public class SyncStats {

    private int fileCount;
    private int filesAdded;
    private int filesUnmodified;
    private int filesModified;
    private int filesRemoved;
    private int processedCount;

    /**
     * Create a new empty stat set
     */
    public SyncStats() {
        this(0);
    }

    /**
     * Create a new empty stat set with specified file count
     *
     * @param fileCount
     */
    public SyncStats(int fileCount) {
        this.fileCount = fileCount;
        filesAdded = 0;
        filesUnmodified = 0;
        filesModified = 0;
        filesRemoved = 0;
        processedCount = 0;
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
     * Increase the file count by specified amount
     *
     * @param increment
     */
    public void increaseFileCountBy(int increment) {
        this.fileCount += increment;
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
     * Increase the count of files added
     */
    public void increaseFilesAdded() {
        filesAdded++;
    }

    /**
     * Get the count of the files unmodified
     *
     * @return
     */
    public int getFilesUnmodified() {
        return filesUnmodified;
    }

    /**
     * Increase the count of files unmodified
     */
    public void increaseFilesUnmodified() {
        filesUnmodified++;
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
     * Increase the count of files modified
     */
    public void increaseFilesModified() {
        filesModified++;
    }

    /**
     * Get the count of the files removed
     *
     * @return
     */
    public int getFilesRemoved() {
        return filesRemoved;
    }

    /**
     * Increase the count of files removed
     */
    public void increaseFilesRemoved() {
        filesRemoved++;
    }

    /**
     * Get the count of the files processed
     *
     * @return
     */
    public int getProcessedCount() {
        return processedCount;
    }

    /**
     * Increase the count of files processed
     */
    public void increaseProcessedCount() {
        processedCount++;
    }

    @Override
    public String toString() {
        return "Total files: " + fileCount
                + "\nTotal files added: " + filesAdded
                + "\nTotal files unmodified: " + filesUnmodified
                + "\nTotal files modified: " + filesModified
                + "\nTotal files removed: " + filesRemoved;
    }
}
