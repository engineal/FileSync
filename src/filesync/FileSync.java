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

import filesync.distribution.Install;
import filesync.distribution.Update;
import filesync.distribution.Version;
import filesync.io.SaveSyncIndex;
import filesync.ui.Console;
import filesync.ui.FileSyncSystemTray;
import filesync.ui.SettingsUI;
import java.awt.AWTException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Main class of File Sync, contains main method and logic to represent a single
 * instance of the application
 *
 * @author Aaron Lucia
 * @version Dec 16, 2014
 */
public class FileSync {

    public static final Version VERSION = Version.parseVersion("v0.1.0-DEV");
    private static final Logger log = Logger.getLogger(FileSync.class.getName());
    private static FileSync instance;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            log.log(Level.SEVERE, ex.getMessage(), ex);
        }

        if (instance == null) {
            try {
                Install install = Install.getInstall(args);
                instance = new FileSync(install.getPreferences());
            } catch (Exception ex) {
                log.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }

        instance.processArgs(args);
    }

    private final Preferences preferences;
    private final List<SyncIndex> syncIndexes;

    private FileSync(Preferences preferences) {
        this.preferences = preferences;

        try {
            FileHandler fh = new FileHandler(preferences.getLogLocation().getCanonicalPath());
            fh.setFormatter(new LogFormatter());
            fh.setLevel(Level.ALL);
            log.addHandler(fh);

            ConsoleHandler ch = new ConsoleHandler();
            ch.setFormatter(new LogFormatter());
            ch.setLevel(Level.ALL);
            log.addHandler(ch);

            log.setLevel(Level.ALL);
        } catch (IOException | SecurityException ex) {
            log.log(Level.SEVERE, ex.getMessage(), ex);
        }

        syncIndexes = new ArrayList<>();
        File dataDir = preferences.getIndexesLocation();
        for (File file : dataDir.listFiles()) {
            try {
                SyncIndex index = SaveSyncIndex.load(file);
                syncIndexes.add(index);
                log.log(Level.SEVERE, "Loaded {0}", index);
            } catch (IOException | ClassNotFoundException ex) {
                log.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

    private void processArgs(String[] args) {
        Console console = new Console(args);

        if (console.isTray()) {
            java.awt.EventQueue.invokeLater(() -> {
                try {
                    FileSyncSystemTray tray = new FileSyncSystemTray(syncIndexes);
                    tray.setVisible(true);
                } catch (AWTException ex) {
                    log.log(Level.SEVERE, ex.getMessage(), ex);
                }
            });
        }

        if (console.isSettings()) {
            java.awt.EventQueue.invokeLater(() -> {
                SettingsUI settings = new SettingsUI(syncIndexes);
                settings.setVisible(true);
            });
        }
    }
}
