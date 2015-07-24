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
 * @version Jul 2, 2015
 */
public class SyncStats {

    private int fileCount;
    private int filesAdded;
    private int filesUnmodified;
    private int filesModified;
    private int filesRemoved;
    private int processedCount;

    public SyncStats() {
        fileCount = 0;
        filesAdded = 0;
        filesUnmodified = 0;
        filesModified = 0;
        filesRemoved = 0;
        processedCount = 0;
    }

    public SyncStats(int fileCount) {
        this.fileCount = fileCount;
        filesAdded = 0;
        filesUnmodified = 0;
        filesModified = 0;
        filesRemoved = 0;
        processedCount = 0;
    }

    public int getFileCount() {
        return fileCount;
    }

    public void increaseFileCount(int fileCount) {
        this.fileCount += fileCount;
    }

    public int getFilesAdded() {
        return filesAdded;
    }

    public void increaseFilesAdded() {
        filesAdded++;
    }

    public int getFilesUnmodified() {
        return filesUnmodified;
    }

    public void increaseFilesUnmodified() {
        filesUnmodified++;
    }

    public int getFilesModified() {
        return filesModified;
    }

    public void increaseFilesModified() {
        filesModified++;
    }

    public int getFilesRemoved() {
        return filesRemoved;
    }

    public void increaseFilesRemoved() {
        filesRemoved++;
    }

    public int getProcessedCount() {
        return processedCount;
    }

    public void increaseProcessedCount() {
        processedCount++;
    }

    @Override
    public String toString() {
        return "Total files: " + fileCount +
                "\nTotal files added: " + filesAdded +
                "\nTotal files unmodified: " + filesUnmodified +
                "\nTotal files modified: " + filesModified +
                "\nTotal files removed: " + filesRemoved;
    }
}
