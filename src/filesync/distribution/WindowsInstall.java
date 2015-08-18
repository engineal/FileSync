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
import java.io.PrintWriter;

/**
 *
 * @author Aaron Lucia
 * @version Aug 17, 2015
 */
public class WindowsInstall extends Install {

    public WindowsInstall(String[] args) throws IOException {
        super(args);
    }

    @Override
    protected File getInstallLocation() {
        return new File(System.getenv("APPDATA"), "FileSync");
    }

    @Override
    public File writeInstallScript(File currentPath, String[] args) throws IOException {
        File updateLocation = new File(getPreferences().getUpdateLocation(), "install.bat");
        try (PrintWriter out = new PrintWriter(updateLocation)) {
            out.println("move " + currentPath + " " + getInstallLocation());
            out.print("start /B java -jar " + getJARLocation() + " ");
            for (String arg : args) {
                out.print("\"" + arg + "\" ");
            }
            out.println();
            out.println("(goto) 2>nul & del \"%~f0\"");
        }
        return updateLocation;
    }
}
