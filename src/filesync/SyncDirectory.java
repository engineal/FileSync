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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Stores the last state of a directory and all files inside, so that it is
 * known when comparing directories
 *
 * @author Aaron Lucia
 * @version Aug 14, 2015
 */
public class SyncDirectory extends SyncFile implements Collection<SyncFile> {

    private final List<SyncFile> files;

    /**
     * Create a new sync directory from an existing file
     *
     * @param file the file to create the directory from
     */
    public SyncDirectory(File file) {
        this(file, false);
    }

    /**
     * Create a new sync directory from an existing file, where the file has
     * been added
     *
     * @param file the file to create the directory from
     * @param added if the file was just added
     */
    public SyncDirectory(File file, boolean added) {
        super(file, added);
        files = new ArrayList<>();
    }

    /**
     * Create a new sync directory from known attributes
     *
     * @param name
     * @param size
     * @param lastModified
     */
    public SyncDirectory(String name, long size, long lastModified) {
        this(name, size, lastModified, false);
    }

    /**
     * Create a new sync directory from known attributes, where the file has
     * been added
     *
     * @param name
     * @param size
     * @param lastModified
     * @param added if the file was just added
     */
    public SyncDirectory(String name, long size, long lastModified, boolean added) {
        super(name, size, lastModified, added);
        files = new ArrayList<>();
    }

    /**
     * Get the number of files and directories in this directory, including sub
     * directories
     *
     * @return file count
     */
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

    @Override
    public int size() {
        return files.size();
    }

    @Override
    public boolean isEmpty() {
        return files.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        if (o instanceof String) {
            return files.stream().anyMatch((temp) -> (temp.getName().equals(o)));
        } else if (o instanceof SyncFile) {
            return files.contains(o);
        }
        return false;
    }

    @Override
    public Iterator<SyncFile> iterator() {
        return files.iterator();
    }

    @Override
    public Object[] toArray() {
        return files.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return files.toArray(a);
    }

    @Override
    public boolean add(SyncFile e) {
        return files.add(e);
    }

    @Override
    public boolean remove(Object o) {
        return files.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return files.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends SyncFile> c) {
        return files.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return files.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return files.retainAll(c);
    }

    @Override
    public void clear() {
        files.clear();
    }
}
