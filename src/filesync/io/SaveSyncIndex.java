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
package filesync.io;

import filesync.io.object.ObjectIO;
import filesync.io.json.JSONIO;
import filesync.SyncIndex;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Aaron Lucia
 * @version Jul 7, 2015
 */
public class SaveSyncIndex {

    /**
     *
     * @param file
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static SyncIndex load(File file) throws IOException, ClassNotFoundException {
        String extension = file.getName().substring(file.getName().lastIndexOf('.'));
        switch (extension) {
            case ".ser":
                return new ObjectIO(file).load();
            case ".json":
                return new JSONIO(file).load();
            default:
                throw new IOException("File extension not supported: " + file.getName());
        }
    }

    /**
     *
     * @param file
     * @param index
     * @throws IOException
     */
    public static void save(File file, SyncIndex index) throws IOException {
        String extension = file.getName().substring(file.getName().lastIndexOf('.'));
        switch (extension) {
            case ".ser":
                new ObjectIO(file).save(index);
                break;
            case ".json":
                new JSONIO(file).save(index);
                break;
            default:
                throw new IOException("File extension not supported: " + file.getName());
        }
    }
}
