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
import filesync.engine.SyncEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *
 * @author Aaron Lucia
 */
public class IndexPanel extends JPanel implements ActionListener {

    private final SyncIndex index;
    private final List<ActionListener> _actionListeners;

    /**
     * Creates new form IndexPanel
     *
     * @param index
     */
    public IndexPanel(SyncIndex index) {
        initComponents();
        _actionListeners = new ArrayList<>();
        this.index = index;
        index.addPropertyChangeListener((PropertyChangeEvent e) -> {
            switch (e.getPropertyName()) {
                case "name":
                    nameLabel.setText(index.getName());
                    break;
            }
        });
        index.getSyncEngine().addSyncListener((SyncEvent e) -> {
            syncProgressBar.setValue((int) (e.getStats().getPercent() * 100));

            switch (e.getState()) {
                case Running:
                    syncButton.setIcon(new ImageIcon(getClass().getResource("/filesync/ui/images/media-pause-2x.png")));
                    stopButton.setVisible(true);
                    syncProgressBar.setVisible(true);
                    break;
                case Paused:
                    syncButton.setIcon(new ImageIcon(getClass().getResource("/filesync/ui/images/loop-circular-2x.png")));
                    stopButton.setVisible(true);
                    syncProgressBar.setVisible(true);
                    break;
                case Stopped:
                    syncButton.setIcon(new ImageIcon(getClass().getResource("/filesync/ui/images/loop-circular-2x.png")));
                    stopButton.setVisible(false);
                    syncProgressBar.setVisible(false);
                    break;
            }
        });

        nameLabel.setText(index.getName());
        stopButton.setVisible(false);
        syncProgressBar.setVisible(false);
        syncProgressBar.setValue(100);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nameLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        updatedLabel = new javax.swing.JLabel();
        syncButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        syncProgressBar = new javax.swing.JProgressBar();

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(32767, 50));
        setMinimumSize(new java.awt.Dimension(251, 50));

        nameLabel.setText("name");

        jLabel1.setLabelFor(updatedLabel);
        jLabel1.setText("Last Updated:");

        updatedLabel.setText("Never");

        syncButton.setBackground(new java.awt.Color(255, 255, 255));
        syncButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/filesync/ui/images/loop-circular-2x.png"))); // NOI18N
        syncButton.setToolTipText("Sync");
        syncButton.setBorder(null);
        syncButton.setContentAreaFilled(false);
        syncButton.addActionListener(this);

        stopButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/filesync/ui/images/media-stop-2x.png"))); // NOI18N
        stopButton.setToolTipText("Stop");
        stopButton.setBorder(null);
        stopButton.setContentAreaFilled(false);
        stopButton.addActionListener(this);

        editButton.setBackground(getBackground());
        editButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/filesync/ui/images/pencil-2x.png"))); // NOI18N
        editButton.setToolTipText("Edit");
        editButton.setBorder(null);
        editButton.setContentAreaFilled(false);
        editButton.addActionListener(this);

        removeButton.setBackground(getBackground());
        removeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/filesync/ui/images/x-2x.png"))); // NOI18N
        removeButton.setToolTipText("Remove");
        removeButton.setBorder(null);
        removeButton.setContentAreaFilled(false);
        removeButton.addActionListener(this);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 155, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(updatedLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(syncButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(stopButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(removeButton)
                .addContainerGap())
            .addComponent(syncProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(nameLabel)
                                .addComponent(jLabel1)
                                .addComponent(updatedLabel)
                                .addComponent(stopButton))
                            .addComponent(syncButton, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addComponent(editButton, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addComponent(removeButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(syncProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }

    // Code for dispatching events from components to event handlers.

    public void actionPerformed(java.awt.event.ActionEvent evt) {
        if (evt.getSource() == syncButton) {
            IndexPanel.this.syncButtonActionPerformed(evt);
        }
        else if (evt.getSource() == editButton) {
            IndexPanel.this.editButtonActionPerformed(evt);
        }
        else if (evt.getSource() == removeButton) {
            IndexPanel.this.removeButtonActionPerformed(evt);
        }
        else if (evt.getSource() == stopButton) {
            IndexPanel.this.stopButtonActionPerformed(evt);
        }
    }// </editor-fold>//GEN-END:initComponents

    private void syncButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_syncButtonActionPerformed
        switch (index.getSyncEngine().getState()) {
            case Running:
            case Paused:
                index.getSyncEngine().pauseCrawl();
                break;
            case Stopped:
                index.getSyncEngine().startCrawl();
                break;
        }
    }//GEN-LAST:event_syncButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        IndexUI indexUI = new IndexUI(index);
        indexUI.setVisible(true);
    }//GEN-LAST:event_editButtonActionPerformed

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "delete");
        for (ActionListener listener : _actionListeners) {
            listener.actionPerformed(event);
        }
    }//GEN-LAST:event_removeButtonActionPerformed

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
        index.getSyncEngine().stopCrawl();
    }//GEN-LAST:event_stopButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton editButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JButton removeButton;
    private javax.swing.JButton stopButton;
    private javax.swing.JButton syncButton;
    private javax.swing.JProgressBar syncProgressBar;
    private javax.swing.JLabel updatedLabel;
    // End of variables declaration//GEN-END:variables

    /**
     * Add a listener
     *
     * @param listener the listener to add
     */
    public synchronized void addActionListener(ActionListener listener) {
        if (!_actionListeners.contains(listener)) {
            _actionListeners.add(listener);
        }
    }

    /**
     * Remove a listener
     *
     * @param listener the listener to remove
     */
    public synchronized void removeActionListener(ActionListener listener) {
        _actionListeners.remove(listener);
    }
}
