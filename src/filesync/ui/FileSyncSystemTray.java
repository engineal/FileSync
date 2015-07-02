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
package filesync.ui;

import filesync.FileSync;
import filesync.UIAction;
import static filesync.UIAction.Pause;
import static filesync.UIAction.Settings;
import static filesync.UIAction.Sync;
import filesync.UIEvent;
import filesync.UIListener;
import java.awt.AWTException;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *
 * @author Aaron Lucia
 * @version Dec 16, 2014
 */
public class FileSyncSystemTray implements ActionListener {

    private final List<UIListener> _uiListeners;

    private final JPopupMenu popup;
    private final JMenuItem settingsItem;
    private final JMenuItem syncItem;
    private final JMenuItem pauseItem;
    private final JMenuItem exitItem;

    public FileSyncSystemTray() throws AWTException {
        _uiListeners = new ArrayList<>();
        ImageIcon icon = new ImageIcon(FileSync.class.getResource("images/bulb.gif"), "FileSync");
        TrayIcon trayIcon = new TrayIcon(icon.getImage());
        SystemTray tray = SystemTray.getSystemTray();

        popup = new JPopupMenu();
        settingsItem = new JMenuItem("Settings");
        settingsItem.addActionListener(this);
        syncItem = new JMenuItem("Sync");
        syncItem.addActionListener(this);
        pauseItem = new JMenuItem("Pause Schedule");
        pauseItem.addActionListener(this);
        exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(this);

        popup.add(settingsItem);
        popup.addSeparator();
        popup.add(syncItem);
        popup.add(pauseItem);
        popup.addSeparator();
        popup.add(exitItem);

        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popup.setLocation(e.getX(), e.getY());
                    popup.setInvoker(popup);
                    popup.setVisible(true);
                } else if (e.getClickCount() >= 2) {
                    fireUIEvent(Settings);
                }
            }
        });

        tray.add(trayIcon);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(settingsItem)) {
            fireUIEvent(Settings);
        } else if (e.getSource().equals(syncItem)) {
            fireUIEvent(Sync);
        } else if (e.getSource().equals(pauseItem)) {
            fireUIEvent(Pause);
        } else if (e.getSource().equals(exitItem)) {
            System.exit(0);
        }
    }

    public void updateSyncStatus(boolean syncing) {
        if (syncing) {
            syncItem.setText("Pause Sync");
        } else {
            syncItem.setText("Sync");
        }
        popup.pack();
    }

    public void updatePauseStatus(boolean paused) {
        if (paused) {
            pauseItem.setText("Restart Schedule");
        } else {
            pauseItem.setText("Pause Schedule");
        }
        popup.pack();
    }

    /**
     *
     * @param listener
     */
    public synchronized void addUIListener(UIListener listener) {
        if (!_uiListeners.contains(listener)) {
            _uiListeners.add(listener);
        }
    }

    /**
     *
     * @param listener
     */
    public synchronized void removeUIListener(UIListener listener) {
        _uiListeners.remove(listener);
    }

    /**
     *
     * @param status
     */
    private synchronized void fireUIEvent(UIAction action) {
        UIEvent event = new UIEvent(this, action);
        for (UIListener listener : _uiListeners) {
            listener.actionPerformed(event);
        }
    }
}
