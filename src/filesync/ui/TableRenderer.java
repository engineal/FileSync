/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesync.ui;

import java.awt.Component;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Aaron Lucia
 * @version Dec 18, 2014
 */
public class TableRenderer implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value instanceof File) {
            JPanel panel = new JPanel();
            JLabel label = new JLabel(((File) value).getName());
            panel.add(label);
            JButton button = new JButton("...");
            panel.add(button);
            return panel;
        }
        return null;
    }
}
