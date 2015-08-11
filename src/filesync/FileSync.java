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

import filesync.engine.SyncListener;
import filesync.engine.SyncEvent;
import filesync.io.SaveSyncIndex;
import filesync.engine.SyncEngine;
import filesync.ui.Console;
import filesync.ui.FileSyncSystemTray;
import filesync.ui.SettingsUI;
import filesync.ui.UIEvent;
import filesync.ui.UIListener;
import java.awt.AWTException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Main class of File Sync, contains main method and logic to represent a single
 * instance of the application
 *
 * @author Aaron Lucia
 * @version Dec 16, 2014
 */
public class FileSync implements SyncListener, UIListener {

    public static final String VERSION = "1.0.DEV";
    private static final Logger log = Logger.getLogger(FileSync.class.getName());
    private static FileSync instance;

    public static FileSync getInstance() {
        if (instance == null) {
            instance = new FileSync();
        }
        return instance;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            log.log(Level.SEVERE, ex.getMessage(), ex);
        }

        FileSync sync = getInstance();
        sync.processArgs(args);
    }

    private List<SyncIndex> syncIndexes;

    private FileSync() {
        try {
            FileHandler fh = new FileHandler("log.log");
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
        File dataDir = new File("Data");
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
                    FileSyncSystemTray tray = new FileSyncSystemTray();
                    tray.addUIListener(this);
                    tray.setVisible(true);
                } catch (AWTException ex) {
                    log.log(Level.SEVERE, ex.getMessage(), ex);
                }
            });
        }

        if (console.isSettings()) {
            java.awt.EventQueue.invokeLater(() -> {
                SettingsUI settings = new SettingsUI(syncIndexes);
                settings.addUIListener(this);
                settings.setVisible(true);
            });
        }
    }

    @Override
    public void statusUpdated(SyncEvent event) {
        if (!event.isSyncing()) {
            try {
                SaveSyncIndex.save(new File("Data\\" + event.getIndex().getName() + ".json"), event.getIndex());
                log.log(Level.SEVERE, "Saved {0}", event.getIndex());
            } catch (IOException ex) {
                log.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

    @Override
    public void actionPerformed(UIEvent event) {
        switch (event.getAction()) {
            case Settings:
                SettingsUI settings = new SettingsUI(syncIndexes);
                settings.addUIListener(this);
                settings.setVisible(true);
                break;
            case Sync:
                sync();
                break;
            case Pause:
                break;
        }
    }

    public synchronized void sync() {
        for (SyncIndex index : syncIndexes) {
            index.getSyncEngine().startCrawl();
        }
    }
}
