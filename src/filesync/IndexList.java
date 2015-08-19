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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Aaron Lucia
 * @version Aug 18, 2015
 */
public class IndexList implements Collection<SyncIndex> {
    
    private final transient ArrayList<SyncIndex> indexes;
    
    private final transient List<PropertyChangeListener> _propertyChangeListeners;

    public IndexList() {
        indexes = new ArrayList<>();
        _propertyChangeListeners = new ArrayList<>();
    }

    @Override
    public int size() {
        return indexes.size();
    }

    @Override
    public boolean isEmpty() {
        return indexes.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return indexes.contains(o);
    }

    @Override
    public Iterator<SyncIndex> iterator() {
        return indexes.iterator();
    }

    @Override
    public Object[] toArray() {
        return indexes.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return indexes.toArray(a);
    }

    @Override
    public boolean add(SyncIndex e) {
        Object[] oldValue = indexes.toArray();
        boolean result = indexes.add(e);
        propertyChanged("indexes", oldValue, indexes.toArray());
        return result;
    }

    @Override
    public boolean remove(Object o) {
        Object[] oldValue = indexes.toArray();
        boolean result = indexes.remove(o);
        propertyChanged("indexes", oldValue, indexes.toArray());
        return result;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return indexes.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends SyncIndex> c) {
        Object[] oldValue = indexes.toArray();
        boolean result = indexes.addAll(c);
        propertyChanged("indexes", oldValue, indexes.toArray());
        return result;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        Object[] oldValue = indexes.toArray();
        boolean result = indexes.removeAll(c);
        propertyChanged("indexes", oldValue, indexes.toArray());
        return result;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        Object[] oldValue = indexes.toArray();
        boolean result = indexes.retainAll(c);
        propertyChanged("indexes", oldValue, indexes.toArray());
        return result;
    }

    @Override
    public void clear() {
        Object[] oldValue = indexes.toArray();
        indexes.clear();
        propertyChanged("indexes", oldValue, indexes.toArray());
    }

    /**
     * Add a listener
     *
     * @param listener the listener to add
     */
    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        if (!_propertyChangeListeners.contains(listener)) {
            _propertyChangeListeners.add(listener);
        }
    }

    /**
     * Remove a listener
     *
     * @param listener the listener to remove
     */
    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        _propertyChangeListeners.remove(listener);
    }

    private synchronized void propertyChanged(String propertyName, Object oldValue, Object newValue) {
        PropertyChangeEvent event = new PropertyChangeEvent(this, propertyName, oldValue, newValue);
        for (PropertyChangeListener listener : _propertyChangeListeners) {
            listener.propertyChange(event);
        }
    }
}
