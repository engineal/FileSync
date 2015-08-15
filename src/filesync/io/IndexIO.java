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
package filesync.io;

import filesync.SyncIndex;
import java.io.File;
import java.io.IOException;

/**
 * The interface that defines basic calls to load and save a index
 *
 * @author Aaron Lucia
 * @version Jul 23, 2015
 */
public interface IndexIO {

    /**
     * Loads the sync index from a file
     *
     * @return the loaded sync index
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public abstract SyncIndex load() throws IOException, ClassNotFoundException;

    /**
     * Saves the sync index to a file
     *
     * @param index the index to save
     * @throws IOException
     */
    public abstract void save(SyncIndex index) throws IOException;
}
