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
 *
 * @author Aaron Lucia
 * @version Dec 17, 2014
 */
public class SyncDirectory extends SyncFile implements Collection<SyncFile> {

    private final List<SyncFile> files;

    public SyncDirectory(File file) {
        super(file);
        files = new ArrayList<>();
    }
    
    public SyncDirectory(String name, long size, long lastModified) {
        super(name, size, lastModified);
        files = new ArrayList<>();
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
