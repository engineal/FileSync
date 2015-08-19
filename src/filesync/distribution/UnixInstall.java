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
package filesync.distribution;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author Aaron Lucia
 * @version Aug 17, 2015
 */
public class UnixInstall extends Install {

    public UnixInstall(boolean inPlace) throws IOException {
        super(new File("/usr/local", "FileSync"), inPlace);
    }

    @Override
    protected String writeInstallScript(File currentPath) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
