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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Aaron Lucia
 * @version Dec 16, 2014
 */
public class FileQueue {

    private final ArrayList<File> queue;

    public FileQueue(File[] files) {
        queue = new ArrayList<>(Arrays.asList(files));
    }
    
    public File remove() {
        if (!isEmpty()) {
            return queue.remove(0);
        }
        return null;
    }

    public File remove(String name) {
        for (File file : queue) {
            if (file.getName().equals(name)) {
                queue.remove(file);
                return file;
            }
        }
        return null;
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int size() {
        return queue.size();
    }
}
