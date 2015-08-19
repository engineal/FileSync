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

import filesync.FileSync;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Aaron Lucia
 * @version Aug 17, 2015
 */
public class WindowsInstall extends Install {

    private static final Logger log = Logger.getLogger(FileSync.class.getName());

    private final File STARTSCRIPT_LOCATION = new File(INSTALL_LOCATION, "start.bat");

    public WindowsInstall(boolean inPlace) throws IOException {
        super(new File(System.getenv("APPDATA"), "FileSync"), inPlace);

        verifyStartScript();
        verifyStartLink();
    }

    @Override
    protected String writeInstallScript(File currentPath) throws IOException {
        File updateLocation = new File(UPDATE_LOCATION, "install.bat");
        log.log(Level.FINE, "Creating {0}", updateLocation);
        try (PrintWriter out = new PrintWriter(updateLocation)) {
            out.println("sleep 1");
            out.println("move " + currentPath + " " + INSTALL_LOCATION);
            out.print("start javaw -jar " + JAR_LOCATION);
            out.println();
            out.println("(goto) 2>nul & del \"%~f0\"");
        }
        return "cmd /c start /B " + updateLocation.getCanonicalPath();
    }

    private void verifyStartScript() throws IOException {
        if (!STARTSCRIPT_LOCATION.exists()) {
            log.log(Level.FINE, "Creating {0}", STARTSCRIPT_LOCATION);
            try (PrintWriter out = new PrintWriter(STARTSCRIPT_LOCATION)) {
                out.print("start javaw -jar " + JAR_LOCATION + " ");
            }
        }
    }

    private void verifyStartLink() throws IOException {
        if (false) {
            log.log(Level.FINE, "Creating start link");
            Runtime.getRuntime().exec("cmd /c mklink \"" + "\" \"" + "\"");
        }
    }
}
