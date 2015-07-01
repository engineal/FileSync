/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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