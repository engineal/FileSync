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
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Aaron Lucia
 * @version Aug 17, 2015
 */
public class Preferences {

    private File indexesLocation;
    private final IndexList indexes;
    private boolean prereleases;

    private final transient List<PropertyChangeListener> _propertyChangeListeners;

    public Preferences(File installLocation) {
        this(new File(installLocation, "data"),
                new IndexList(),
                false);
    }

    public Preferences(File indexesLocation, IndexList indexes, boolean prereleases) {
        this.indexesLocation = indexesLocation;
        this.indexes = indexes;
        this.prereleases = prereleases;

        _propertyChangeListeners = new ArrayList<>();
    }

    public File getIndexesLocation() {
        return indexesLocation;
    }

    public void setIndexesLocation(File indexesLocation) {
        File oldValue = getIndexesLocation();
        this.indexesLocation = indexesLocation;
        propertyChanged("indexesLocation", oldValue, indexesLocation);
    }
    
    public IndexList getIndexes() {
        return indexes;
    }

    public boolean isPrereleases() {
        return prereleases;
    }

    public void setPrereleases(boolean prereleases) {
        boolean oldValue = isPrereleases();
        this.prereleases = prereleases;
        propertyChanged("prereleases", oldValue, prereleases);
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
