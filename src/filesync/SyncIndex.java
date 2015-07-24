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
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Aaron Lucia
 * @version Dec 17, 2014
 */
public class SyncIndex extends SyncDirectory {

    public static final long serialVersionUID = 1;
    private final List<Path> directories;
    private SyncSchedule schedule;
    private SyncStats stats;
    private transient SyncEngine engine;

    public SyncIndex(String name) {
        super(name);
        this.directories = new ArrayList<>();
        schedule = new SyncSchedule();
        stats = new SyncStats();
        engine = new SyncEngine(this);
    }
    
    public SyncIndex(String name, List<Path> directories) {
        super(name);
        this.directories = directories;
    }

    public List<Path> getDirectories() {
        return directories;
    }
    
    public SyncSchedule getSchedule() {
        return schedule;
    }
    
    public SyncSchedule getStats() {
        return schedule;
    }
    
    public SyncEngine getSyncEngine() {
        return engine;
    }
}
