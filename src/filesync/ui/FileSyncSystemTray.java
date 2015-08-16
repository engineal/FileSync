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

import filesync.SyncIndex;
import java.awt.AWTException;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *
 * @author Aaron Lucia
 * @version Dec 16, 2014
 */
public class FileSyncSystemTray {

    private final List<SyncIndex> syncIndexes;

    private final TrayIcon trayIcon;
    private final SystemTray tray;

    private final JPopupMenu popup;
    private final JMenuItem syncItem;
    private final JMenuItem pauseItem;

    public FileSyncSystemTray(List<SyncIndex> syncIndexes) {
        this.syncIndexes = syncIndexes;

        ImageIcon icon = new ImageIcon(getClass().getResource("/filesync/ui/images/loop-circular-2x.png"));
        trayIcon = new TrayIcon(icon.getImage());
        trayIcon.setImageAutoSize(true);
        tray = SystemTray.getSystemTray();

        popup = new JPopupMenu();

        JMenuItem settingsItem = new JMenuItem("Settings");
        settingsItem.addActionListener((ActionEvent e) -> {
            showSettings();
        });
        popup.add(settingsItem);

        popup.addSeparator();

        syncItem = new JMenuItem("Sync");
        syncItem.addActionListener((ActionEvent e) -> {
            sync();
        });
        popup.add(syncItem);

        pauseItem = new JMenuItem("Pause Schedule");
        pauseItem.addActionListener((ActionEvent e) -> {

        });
        popup.add(pauseItem);

        popup.addSeparator();

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });
        popup.add(exitItem);

        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    popup.setLocation(e.getX(), e.getY());
                    popup.setInvoker(popup);
                    popup.setVisible(true);
                } else if (e.getClickCount() >= 2) {
                    showSettings();
                }
            }
        });
    }

    public void setVisible(boolean visible) throws AWTException {
        if (visible) {
            tray.add(trayIcon);
        } else {
            tray.remove(trayIcon);
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

    private void showSettings() {
        java.awt.EventQueue.invokeLater(() -> {
            SettingsUI settings = new SettingsUI(syncIndexes);
            settings.setVisible(true);
        });
    }

    private void sync() {
        for (SyncIndex index : syncIndexes) {
            index.getSyncEngine().startCrawl();
        }
    }
}
