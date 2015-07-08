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

import com.google.gson.Gson;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author Aaron Lucia
 * @version Jul 7, 2015
 */
public class SaveSyncIndex {

    public static SyncIndex load(File file) throws IOException, ClassNotFoundException {
        String extension = file.getName().substring(file.getName().lastIndexOf('.'));
        switch (extension) {
            case ".ser":
                return loadObject(file);
            case ".json":
                return loadJSON(file);
            default:
                throw new IOException("File extension not supported: " + file.getName());
        }
    }

    private static SyncIndex loadObject(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (SyncIndex) in.readObject();
        }
    }

    private static SyncIndex loadJSON(File file) throws IOException {
        try (FileReader reader = new FileReader(file)) {
            Gson json = new Gson();
            return json.fromJson(reader, SyncIndex.class);
        }
    }

    public static void save(File file, SyncIndex index) throws IOException {
        String extension = file.getName().substring(file.getName().lastIndexOf('.'));
        switch (extension) {
            case ".ser":
                saveObject(file, index);
                break;
            case ".json":
                saveJSON(file, index);
                break;
            default:
                throw new IOException("File extension not supported: " + file.getName());
        }
    }

    private static void saveObject(File file, SyncIndex index) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(index);
        }
    }

    private static void saveJSON(File file, SyncIndex index) throws IOException {
        try (FileWriter reader = new FileWriter(file)) {
            Gson json = new Gson();
            reader.write(json.toJson(index));
        }
    }
}
