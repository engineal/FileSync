/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesync;

import java.io.Serializable;

/**
 *
 * @author Aaron Lucia
 * @version Dec 17, 2014
 */
public class SyncFile implements Serializable {

    private final String name;
    private int size;

    public SyncFile(String name) {
        this.name = name;
    }

    /**
     * Get the value of size
     *
     * @return the value of size
     */
    public int getSize() {
        return size;
    }


    /**
     * Set the value of size
     *
     * @param size new value of size
     */
    public void setSize(int size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
