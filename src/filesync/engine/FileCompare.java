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
package filesync.engine;

import filesync.FileSync;
import filesync.SyncFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aaron Lucia
 * @version Jul 6, 2015
 */
public class FileCompare {

    private static final Logger log = Logger.getLogger(FileSync.class.getName());

    private static void copyFile(File file, File directory) throws IOException {
        Path source = Paths.get(file.getAbsolutePath());
        Path destination = Paths.get(directory.getAbsolutePath() + "\\" + file.getName());
        log.log(Level.FINE, "Copying {0} to {1}", new Object[]{source, destination});
        Files.copy(source, destination, REPLACE_EXISTING, COPY_ATTRIBUTES);
    }

    private final SyncFile syncFile;
    private final File[] files;

    public FileCompare(File file) {
        this.syncFile = null;
        this.files = new File[1];
        files[0] = file;
    }

    public FileCompare(SyncFile syncFile, File[] files) {
        this.syncFile = syncFile;
        this.files = files;
    }

    public SyncFile getSyncFile() {
        if (syncFile != null) {
            return syncFile;
        } else {
            return new SyncFile(files[0].getName(), files[0].length(), files[0].lastModified());
        }
    }

    public File[] getFiles() {
        return files;
    }

    public SyncAction getAction() {
        if (syncFile == null) {
            return SyncAction.Added;
        }
        for (File file : files) {
            if (file == null) {
                return SyncAction.Removed;
            } else if (file.lastModified() != syncFile.getLastModified()) {
                return SyncAction.Modified;
            } else if (file.length() != syncFile.getSize()) {
                return SyncAction.Modified;
            }
        }
        return SyncAction.Unchanged;
    }

    public SyncAction resolveConflict() throws IOException {
        SyncAction action = getAction();
        switch (action) {
            case Added:
                addFiles();
                break;
            case Modified:
                break;
            case Removed:
                removeFiles();
                break;
        }

        return action;
    }
    
    private void addFiles() {
        
    }

    private void removeFiles() throws IOException {
        for (File file : files) {
            Path source = Paths.get(file.getAbsolutePath());
            log.log(Level.FINE, "Removing {0}", source);
            Files.deleteIfExists(source);
        }
    }
}
