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
import filesync.ui.Console;
import filesync.ui.FileSyncSystemTray;
import filesync.ui.FileSyncUI;
import filesync.ui.UIEvent;
import filesync.ui.UIListener;
import java.awt.AWTException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Aaron Lucia
 */
public class FileSync implements StatusListener, UIListener {

    public static final String VERSION = "0.0.3";
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
        FileSync sync = getInstance();
        sync.processArgs(args);
    }

    private static SyncIndex loadSyncIndex(String file) {
        if (new File(file).exists()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                log.log(Level.FINE, "Opening {0}", file);
                return (SyncIndex) in.readObject();
            } catch (IOException | ClassNotFoundException ex) {
                log.log(Level.SEVERE, ex.toString(), ex);
            }
        }
        log.log(Level.FINE, "Could not open {0}, creating default instead.", file);

        List<Path> directories = new ArrayList<>();
        directories.add(new File("Dir1").toPath());
        directories.add(new File("Dir2").toPath());
        return new SyncIndex("Sync", directories);
    }

    private static void saveSyncIndex(String file, SyncIndex index) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            log.log(Level.FINE, "Saving {0}", index);
            out.writeObject(index);
        } catch (IOException ex) {
            log.log(Level.SEVERE, ex.toString(), ex);
        }
    }


    private FileSyncUI settings;
    private FileSyncSystemTray tray;
    private SyncEngine syncEngine;

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
            log.log(Level.SEVERE, ex.toString(), ex);
        }
    }

    private void processArgs(String[] args) {
        Console console = new Console(args);
        console.addUIListener(this);
        if (console.isGui()) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                log.log(Level.SEVERE, ex.toString(), ex);
            }
            
            java.awt.EventQueue.invokeLater(() -> {
                try {
                    tray = new FileSyncSystemTray();
                    tray.addUIListener(this);
                    settings = new FileSyncUI();
                    settings.addUIListener(this);
                } catch (AWTException ex) {
                    log.log(Level.SEVERE, ex.toString(), ex);
                }
            });
        }
    }

    public void sync() {
        if (syncEngine == null) {
            syncEngine = new SyncEngine(loadSyncIndex("Index.ser"));
            syncEngine.addStatusListener(this);
            syncEngine.start();
        } else {
            syncEngine.pause();
        }
    }

    public void pauseSchedule() {
        settings.updatePauseStatus(true);
        settings.updatePauseStatus(false);
    }

    @Override
    public void statusUpdated(StatusEvent event) {
        if (!event.isSyncing()) {
            saveSyncIndex("Index.ser", syncEngine.getIndex());
            syncEngine = null;
        }
        if (settings != null) {
            settings.updateSyncStatus(event.isSyncing(), event.getPercent());
        }
    }

    @Override
    public void actionPerformed(UIEvent event) {
        switch (event.getAction()) {
            case Settings:
                settings.setVisible(true);
                break;
            case Sync:
                sync();
                break;
            case Pause:
                pauseSchedule();
                break;
        }
    }
}
