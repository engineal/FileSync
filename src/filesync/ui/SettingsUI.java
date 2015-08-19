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
import filesync.Preferences;
import filesync.SyncIndex;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Aaron Lucia
 */
public class SettingsUI extends JFrame implements ActionListener {

    private final Preferences preferences;

    /**
     * Creates new form FileSyncUI
     *
     * @param preferences
     */
    public SettingsUI(Preferences preferences) {
        initComponents();
        indexesListPanel.setLayout(new BoxLayout(indexesListPanel, javax.swing.BoxLayout.Y_AXIS));

        this.preferences = preferences;
        preferences.addPropertyChangeListener((PropertyChangeEvent e) -> {
            switch (e.getPropertyName()) {
                case "indexesLocation":
                    dataTextField.setText(preferences.getIndexesLocation().toString());
                    break;
                case "prereleases":
                    prereleaseCheckBox.setSelected(preferences.isPrereleases());
                    break;
            }
        });

        preferences.getIndexes().addPropertyChangeListener((PropertyChangeEvent e) -> {
            updateIndexes();
        });

        updateIndexes();
        versionLabel.setText(FileSync.VERSION.toString());
        dataTextField.setText(preferences.getIndexesLocation().toString());
        prereleaseCheckBox.setSelected(preferences.isPrereleases());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPane = new javax.swing.JTabbedPane();
        indexesPanel = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();
        syncButton = new javax.swing.JButton();
        indexesScrollPane = new javax.swing.JScrollPane();
        indexesListPanel = new javax.swing.JPanel();
        aboutPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        versionLabel = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        dataTextField = new javax.swing.JTextField();
        dataButton = new javax.swing.JButton();
        prereleaseCheckBox = new javax.swing.JCheckBox();
        exitButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("FileSync");

        addButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/filesync/ui/images/plus-2x.png"))); // NOI18N
        addButton.setToolTipText("Add");
        addButton.setBorder(null);
        addButton.setContentAreaFilled(false);
        addButton.addActionListener(this);

        syncButton.setText("Sync All");
        syncButton.addActionListener(this);

        indexesListPanel.setBackground(new java.awt.Color(255, 255, 255));
        indexesScrollPane.setViewportView(indexesListPanel);

        javax.swing.GroupLayout indexesPanelLayout = new javax.swing.GroupLayout(indexesPanel);
        indexesPanel.setLayout(indexesPanelLayout);
        indexesPanelLayout.setHorizontalGroup(
            indexesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(indexesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(indexesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(indexesScrollPane)
                    .addGroup(indexesPanelLayout.createSequentialGroup()
                        .addComponent(addButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 389, Short.MAX_VALUE)
                        .addComponent(syncButton)))
                .addContainerGap())
        );
        indexesPanelLayout.setVerticalGroup(
            indexesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, indexesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(indexesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addButton)
                    .addComponent(syncButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(indexesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPane.addTab("Indexes", indexesPanel);

        jLabel1.setText("FileSync by Aaron Lucia");

        jLabel2.setLabelFor(versionLabel);
        jLabel2.setText("Version:");

        versionLabel.setText("version");

        jLabel3.setText("© Aaron Lucia 2015");

        jLabel4.setLabelFor(dataTextField);
        jLabel4.setText("Data Location:");

        dataTextField.setEditable(false);

        dataButton.setText("Browse...");
        dataButton.setToolTipText("Select Data Location");
        dataButton.addActionListener(this);

        prereleaseCheckBox.setText("Enable Prereleases");
        prereleaseCheckBox.addActionListener(this);

        javax.swing.GroupLayout aboutPanelLayout = new javax.swing.GroupLayout(aboutPanel);
        aboutPanel.setLayout(aboutPanelLayout);
        aboutPanelLayout.setHorizontalGroup(
            aboutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(aboutPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(aboutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(aboutPanelLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dataTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dataButton))
                    .addGroup(aboutPanelLayout.createSequentialGroup()
                        .addGroup(aboutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(aboutPanelLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(versionLabel))
                            .addComponent(jLabel1)
                            .addComponent(prereleaseCheckBox))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        aboutPanelLayout.setVerticalGroup(
            aboutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(aboutPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(aboutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(versionLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(aboutPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(dataTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dataButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(prereleaseCheckBox)
                .addContainerGap(313, Short.MAX_VALUE))
        );

        tabbedPane.addTab("About", aboutPanel);

        exitButton.setText("Exit");
        exitButton.addActionListener(this);

        closeButton.setText("Close");
        closeButton.addActionListener(this);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(exitButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(closeButton)
                .addContainerGap())
            .addComponent(tabbedPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(tabbedPane)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(closeButton)
                    .addComponent(exitButton))
                .addContainerGap())
        );

        pack();
    }

    // Code for dispatching events from components to event handlers.

    public void actionPerformed(java.awt.event.ActionEvent evt) {
        if (evt.getSource() == addButton) {
            SettingsUI.this.addButtonActionPerformed(evt);
        }
        else if (evt.getSource() == syncButton) {
            SettingsUI.this.syncButtonActionPerformed(evt);
        }
        else if (evt.getSource() == exitButton) {
            SettingsUI.this.exitButtonActionPerformed(evt);
        }
        else if (evt.getSource() == closeButton) {
            SettingsUI.this.closeButtonActionPerformed(evt);
        }
        else if (evt.getSource() == dataButton) {
            SettingsUI.this.dataButtonActionPerformed(evt);
        }
        else if (evt.getSource() == prereleaseCheckBox) {
            SettingsUI.this.prereleaseCheckBoxActionPerformed(evt);
        }
    }// </editor-fold>//GEN-END:initComponents

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }//GEN-LAST:event_closeButtonActionPerformed

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        SyncIndex index = new SyncIndex("Name");
        preferences.getIndexes().add(index);

        IndexUI indexUI = new IndexUI(index);
        indexUI.setVisible(true);
    }//GEN-LAST:event_addButtonActionPerformed

    private void syncButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_syncButtonActionPerformed
        for (SyncIndex index : preferences.getIndexes()) {
            index.getSyncEngine().startCrawl();
        }
    }//GEN-LAST:event_syncButtonActionPerformed

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitButtonActionPerformed

    private void dataButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dataButtonActionPerformed
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        chooser.setDialogTitle("Select location for data");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            preferences.setIndexesLocation(chooser.getSelectedFile());
        }
    }//GEN-LAST:event_dataButtonActionPerformed

    private void prereleaseCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prereleaseCheckBoxActionPerformed
        preferences.setPrereleases(prereleaseCheckBox.isSelected());
    }//GEN-LAST:event_prereleaseCheckBoxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel aboutPanel;
    private javax.swing.JButton addButton;
    private javax.swing.JButton closeButton;
    private javax.swing.JButton dataButton;
    private javax.swing.JTextField dataTextField;
    private javax.swing.JButton exitButton;
    private javax.swing.JPanel indexesListPanel;
    private javax.swing.JPanel indexesPanel;
    private javax.swing.JScrollPane indexesScrollPane;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JCheckBox prereleaseCheckBox;
    private javax.swing.JButton syncButton;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JLabel versionLabel;
    // End of variables declaration//GEN-END:variables

    private void updateIndexes() {
        indexesListPanel.removeAll();

        for (SyncIndex index : preferences.getIndexes()) {
            IndexPanel panel = new IndexPanel(index);
            panel.addActionListener((ActionEvent e) -> {
                if (e.getActionCommand().equals("delete")
                        && JOptionPane.showConfirmDialog(panel, "Are you sure you want to delete " + index.getName() + "?",
                                "Are you sure?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    preferences.getIndexes().remove(index);
                }
            });
            indexesListPanel.add(panel);
        }

        indexesListPanel.repaint();
    }
}
