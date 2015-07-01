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
