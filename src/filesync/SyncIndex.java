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

import filesync.engine.SyncEngine;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * The top directory of a sync tree, stores the paths to all sync locations
 *
 * @author Aaron Lucia
 * @version Dec 17, 2014
 */
public class SyncIndex extends SyncDirectory {

    private final List<File> directories;
    private SyncSchedule schedule;
    
    private final transient SyncEngine engine;
    private final transient List<PropertyChangeListener> _propertyChangeListeners;

    /**
     * Create a new sync index with specified name
     *
     * @param name
     */
    public SyncIndex(String name) {
        this(name, new ArrayList<>());
    }

    /**
     * Create a new sync index with specified name and list of directories
     *
     * @param name
     * @param directories
     */
    public SyncIndex(String name, List<File> directories) {
        super(name, 0, 0);
        this.directories = directories;
        schedule = new SyncSchedule();
        engine = new SyncEngine(this);
        _propertyChangeListeners = new ArrayList<>();
    }
    
    /**
     * Set the name of the file
     *
     * @param name
     */
    @Override
    public void setName(String name) {
        String oldValue = getName();
        super.setName(name);
        propertyChanged("name", oldValue, name);
    }

    /**
     * Get the list of directories
     *
     * @return
     */
    public List<File> getDirectories() {
        return directories;
    }

    /**
     * Get the sync schedule
     *
     * @return
     */
    public SyncSchedule getSchedule() {
        return schedule;
    }

    /**
     * Set the sync schedule
     *
     * @param schedule
     */
    public void setSchedule(SyncSchedule schedule) {
        this.schedule = schedule;
    }

    /**
     * Get the sync engine
     *
     * @return
     */
    public SyncEngine getSyncEngine() {
        return engine;
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
