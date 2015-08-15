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
package filesync.io.object;

import filesync.SyncIndex;
import filesync.io.IndexIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Handles index IO through Java's default Object serialization
 *
 * @author Aaron Lucia
 * @version Jul 23, 2015
 */
public class ObjectIO implements IndexIO {

    private final File file;

    /**
     * Create a new Object file interface
     *
     * @param file
     */
    public ObjectIO(File file) {
        this.file = file;
    }

    @Override
    public SyncIndex load() throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (SyncIndex) in.readObject();
        }
    }

    @Override
    public void save(SyncIndex index) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(index);
        }
    }
}
