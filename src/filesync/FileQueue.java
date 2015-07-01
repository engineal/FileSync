/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
