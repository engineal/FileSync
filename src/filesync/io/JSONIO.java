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

import com.google.gson.Gson;
import filesync.SyncIndex;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Aaron Lucia
 * @version Jul 23, 2015
 */
public class JSONIO implements IndexIO {

    private final File file;
    
    public JSONIO(File file) {
        this.file = file;
    }

    @Override
    public SyncIndex load() throws IOException, ClassNotFoundException {
        try (FileReader reader = new FileReader(file)) {
            Gson json = new Gson();
            return json.fromJson(reader, SyncIndex.class);
        }
    }

    @Override
    public void save(SyncIndex index) throws IOException {
        try (FileWriter reader = new FileWriter(file)) {
            Gson json = new Gson();
            reader.write(json.toJson(index));
        }
    }
}
