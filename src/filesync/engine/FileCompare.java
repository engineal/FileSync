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

    private final SyncFile syncFile;
    private final File[] files;

    public FileCompare(SyncFile syncFile, File[] files) {
        this.syncFile = syncFile;
        this.files = files;
    }

    public SyncAction getAction() {
        if (syncFile.isAdded()) {
            return SyncAction.Added;
        }
        for (File file : files) {
            if (!file.exists()) {
                return SyncAction.Removed;
            }
        }
        for (File file : files) {
            if (file.lastModified() != syncFile.getLastModified()
                    || file.length() != syncFile.getSize()) {
                return SyncAction.Modified;
            }
        }
        return SyncAction.Unchanged;
    }

    public SyncAction resolveConflict() throws IOException {
        SyncAction action = getAction();
        switch (action) {
            case Added:
                modifyFiles();
                syncFile.clearAdded();
                break;
            case Modified:
                modifyFiles();
                break;
            case Removed:
                removeFiles();
                break;
        }

        return action;
    }

    private void modifyFiles() throws IOException {
        File newFile = compareFiles();
        for (File file : files) {
            if (file != newFile) {
                if (newFile.isDirectory()) {
                    if (!file.exists()) {
                        log.log(Level.FINE, "Creating {0}", new Object[]{file});
                        file.mkdirs();
                    }
                } else {
                    if (!file.getParentFile().exists()) {
                        log.log(Level.FINE, "Creating {0}", new Object[]{file.getParentFile()});
                        file.mkdirs();
                    }
                    log.log(Level.FINE, "Copying {0} to {1}", new Object[]{newFile, file});
                    Files.copy(newFile.toPath(), file.toPath(), REPLACE_EXISTING, COPY_ATTRIBUTES);
                }
            }
        }
        syncFile.setSize(newFile.length());
        syncFile.setLastModified(newFile.lastModified());
    }

    protected File compareFiles() {
        File latest = files[0];
        for (File file : files) {
            if (file.lastModified() > latest.lastModified()) {
                latest = file;
            }
        }
        return latest;
    }

    private void removeFiles() {
        for (File file : files) {
            log.log(Level.FINE, "Removing {0}", file);
            file.delete();
        }
    }

    public enum SyncAction {

        Added, Modified, Removed, Unchanged
    }
}
