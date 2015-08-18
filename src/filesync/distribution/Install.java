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

import com.google.gson.Gson;
import filesync.FileSync;
import static filesync.FileSync.VERSION;
import filesync.Preferences;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Aaron Lucia
 * @version Aug 17, 2015
 */
public abstract class Install {

    public static Install getInstall(String[] args) throws IOException {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return new WindowsInstall(args);
        } else if (os.contains("mac")) {
            throw new UnsupportedOperationException("Mac to be supported");
        } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            return new UnixInstall(args);
        } else if (os.contains("sunos")) {
            throw new UnsupportedOperationException("Solaris not supported");
        } else {
            throw new UnsupportedOperationException("OS not supported");
        }
    }

    String[] args;

    public Install(String[] args) throws IOException {
        this.args = args;
        verifyInstall();
    }

    protected abstract File getInstallLocation();

    protected File getJARLocation() {
        return new File(getInstallLocation(), "FileSync.jar");
    }

    private File getPreferencesLocation() {
        return new File(getInstallLocation(), "preferences.json");
    }

    public abstract File writeInstallScript(File currentPath, String[] args) throws IOException;

    public Preferences getPreferences() throws IOException {
        try (FileReader reader = new FileReader(getPreferencesLocation())) {
            Gson gson = new Gson();
            return gson.fromJson(reader, Preferences.class);
        }
    }

    public void setPreferences(Preferences preferences) throws IOException {
        try (FileWriter reader = new FileWriter(getPreferencesLocation())) {
            Gson gson = new Gson();
            reader.write(gson.toJson(preferences));
        }
    }

    private void verifyInstall() throws IOException {
        if (!getInstallLocation().exists()) {
            getInstallLocation().mkdirs();
        }

        File currentPath = new File(FileSync.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        if (!getJARLocation().equals(currentPath)) {
            triggerInstall(currentPath);
        }

        if (!getPreferencesLocation().exists()) {
            setPreferences(new Preferences(getInstallLocation()));
        }

        Preferences preferences = getPreferences();

        if (!preferences.getIndexesLocation().exists()) {
            preferences.getIndexesLocation().mkdirs();
        }

        if (!preferences.getLogLocation().exists()) {
            preferences.getLogLocation().getParentFile().mkdirs();
        }

        if (!preferences.getUpdateLocation().exists()) {
            preferences.getUpdateLocation().mkdirs();
        }

        Update u = new Update(preferences.getUpdateLocation());
        if (u.isUpdate(VERSION)
                && JOptionPane.showConfirmDialog(null, "There is an update availible. Do you want to update?",
                        "FileSync Update", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            triggerInstall(u.update());
        }
    }

    private void triggerInstall(File currentPath) throws IOException {
        if (!currentPath.exists() || !currentPath.getName().endsWith(".jar")) {
            Update u = new Update(getPreferences().getUpdateLocation());
            currentPath = u.update();
        }
        
        File installFile = writeInstallScript(currentPath, args);
        Runtime.getRuntime().exec("cmd /c start /B " + installFile.getCanonicalPath());
        System.exit(0);
    }
}
