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

import com.google.gson.GsonBuilder;
import filesync.FileSync;
import static filesync.FileSync.VERSION;
import filesync.LogFormatter;
import filesync.Preferences;
import filesync.io.json.PreferencesSerializer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Aaron Lucia
 * @version Aug 17, 2015
 */
public abstract class Install {

    private static final Logger log = Logger.getLogger(FileSync.class.getName());

    public static Install getInstall() throws IOException {
        return getInstall(false);
    }

    public static Install getInstall(boolean inPlace) throws IOException {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            return new WindowsInstall(inPlace);
        } else if (os.contains("mac")) {
            throw new UnsupportedOperationException("Mac to be supported");
        } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            return new UnixInstall(inPlace);
        } else if (os.contains("sunos")) {
            throw new UnsupportedOperationException("Solaris not supported");
        } else {
            throw new UnsupportedOperationException("OS not supported");
        }
    }

    private static Preferences readPreferences(File installLocation, File preferencesLocation) throws IOException {
        Preferences pref;
        if (preferencesLocation.exists()) {
            log.log(Level.FINE, "Reading {0}", preferencesLocation);
            try (FileReader reader = new FileReader(preferencesLocation)) {
                GsonBuilder gson = new GsonBuilder();
                gson.registerTypeAdapter(Preferences.class, new PreferencesSerializer());
                pref = gson.create().fromJson(reader, Preferences.class);
            }
        } else {
            pref = new Preferences(installLocation);
            writePreferences(pref, preferencesLocation);
        }

        return pref;
    }

    private static void writePreferences(Preferences preferences, File preferencesLocation) throws IOException {
        log.log(Level.FINE, "Writing {0}", preferencesLocation);
        try (FileWriter writer = new FileWriter(preferencesLocation)) {
            GsonBuilder gson = new GsonBuilder();
            gson.registerTypeAdapter(Preferences.class, new PreferencesSerializer());
            writer.write(gson.create().toJson(preferences));
        }
    }

    protected final File INSTALL_LOCATION;
    private final File LOG_LOCATION;
    private final File LIB_LOCATION;
    private final File PREFERENCES_LOCATION;
    protected final File JAR_LOCATION;
    protected final File UPDATE_LOCATION;

    private final Preferences preferences;

    public Install(File installLocation, boolean inPlace) throws IOException {
        this.INSTALL_LOCATION = installLocation;
        this.LOG_LOCATION = new File(installLocation, "logs");
        this.LIB_LOCATION = new File(installLocation, "lib");
        this.PREFERENCES_LOCATION = new File(installLocation, "preferences.json");
        this.JAR_LOCATION = new File(installLocation, "FileSync.jar");
        this.UPDATE_LOCATION = new File(installLocation, "updates");

        if (!INSTALL_LOCATION.exists()) {
            log.log(Level.FINE, "Creating {0}", INSTALL_LOCATION);
            INSTALL_LOCATION.mkdirs();
        }

        verifyLogging();
        verifyLib();
        verifyJar(inPlace);
        this.preferences = readPreferences(INSTALL_LOCATION, PREFERENCES_LOCATION);
        verifyUpdates();
    }

    public Preferences getPreferences() {
        return preferences;
    }

    public void writePreferences() throws IOException {
        writePreferences(preferences, PREFERENCES_LOCATION);
    }

    private void verifyLogging() throws IOException {
        if (!LOG_LOCATION.exists()) {
            log.log(Level.FINE, "Creating {0}", LOG_LOCATION);
            LOG_LOCATION.mkdirs();
        }

        FileHandler fh = new FileHandler(new File(LOG_LOCATION, "log.log").toString(), true);
        fh.setFormatter(new LogFormatter());
        fh.setLevel(Level.ALL);
        log.addHandler(fh);
    }

    private void verifyLib() throws IOException {
        if (!LIB_LOCATION.exists()) {
            log.log(Level.FINE, "Creating {0}", LIB_LOCATION);
            LIB_LOCATION.mkdirs();
        }

        File gsonLocation = new File(LIB_LOCATION, "gson-2.3.1.jar");
        if (!gsonLocation.exists()) {
            log.log(Level.FINE, "Downloading {0}", gsonLocation);
            URL website = new URL("http://repo1.maven.org/maven2/com/google/code/gson/gson/2.3.1/gson-2.3.1.jar");
            try (ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                    FileOutputStream fos = new FileOutputStream(gsonLocation)) {
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            }
        }
    }

    private void verifyJar(boolean inPlace) throws IOException {
        if (!UPDATE_LOCATION.exists()) {
            log.log(Level.FINE, "Creating {0}", UPDATE_LOCATION);
            UPDATE_LOCATION.mkdirs();
        }

        File currentPath = new File(FileSync.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        if (!currentPath.exists() || !currentPath.getName().endsWith(".jar")) {
            Update u = new Update(INSTALL_LOCATION, false);
            log.log(Level.FINE, "Downloading {0}", JAR_LOCATION);
            currentPath = u.update();
        }

        if (!inPlace && !JAR_LOCATION.equals(currentPath)) {
            triggerInstall(currentPath);
        }
    }

    private void verifyUpdates() throws IOException {
        Update u = new Update(UPDATE_LOCATION, preferences.isPrereleases());
        if (u.isUpdate(VERSION)
                && JOptionPane.showConfirmDialog(null, "There is an update availible. Do you want to update?",
                        "FileSync Update", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            log.log(Level.FINE, "Updating {0}", JAR_LOCATION);
            triggerInstall(u.update());
        }
    }

    private void triggerInstall(File currentPath) throws IOException {
        if (currentPath != null) {
            log.log(Level.FINE, "Restarting!");
            Runtime.getRuntime().exec(writeInstallScript(currentPath));
            System.exit(0);
        }
    }

    protected abstract String writeInstallScript(File currentPath) throws IOException;
}
