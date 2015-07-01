/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesync;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Aaron Lucia
 * @version Dec 17, 2014
 */
public class SyncIndex extends SyncDirectory implements Serializable {

    public static final long serialVersionUID = 1;
    private final ArrayList<File> directories;

    public SyncIndex(String name, ArrayList<File> directories) {
        super(name);
        this.directories = directories;
    }

    public ArrayList<File> getDirectories() {
        return directories;
    }
}
