/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesync;

import filesync.ui.Console;
import filesync.ui.FileSyncSystemTray;
import filesync.ui.FileSyncUI;
import java.awt.AWTException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
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

        ArrayList<File> directories = new ArrayList<>();
        directories.add(new File("Dir1"));
        directories.add(new File("Dir2"));
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
    private SyncEngine syncEngine;
    private FileSyncSystemTray tray;

    public FileSync() {
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
