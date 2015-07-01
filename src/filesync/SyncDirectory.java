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
import java.util.ArrayList;

/**
 *
 * @author Aaron Lucia
 * @version Dec 17, 2014
 */
public class SyncDirectory extends SyncFile implements Serializable {

    private final ArrayList<SyncFile> files;

    public SyncDirectory(String name) {
        super(name);
        files = new ArrayList<>();
    }

    public ArrayList<SyncFile> getFiles() {
        return files;
    }
    
    public int fileCount() {
        int count = 0;
        for (SyncFile file : files) {
            if (file instanceof SyncDirectory) {
                count += ((SyncDirectory) file).fileCount();
            }
            count++;
        }
        return count;
    }
    
    public boolean contains(String file) {
        if (file != null) {
            return files.stream().anyMatch((temp) -> (temp.getName().equals(file)));
        }
        return false;
    }
}