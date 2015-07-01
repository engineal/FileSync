/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filesync.ui;

import filesync.SyncIndex;
import java.io.File;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Aaron Lucia
 * @version Dec 18, 2014
 */
public class TableModel extends AbstractTableModel {

    private final String[] columns;
    private final ArrayList<SyncIndex> rows;

    public TableModel() {
        this.columns = new String[]{"Folder 1", "Folder 2"};
        rows = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return rows.get(rowIndex).getDirectories().get(rowIndex);
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        rows.get(rowIndex).getDirectories().get(rowIndex);
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return true;
    }

    @Override
    public String getColumnName(int col) {
        return columns[col];
    }

    @Override
    public Class getColumnClass(int col) {
        return File.class;
    }

    public void addRow(SyncIndex row) {
        rows.add(row);
        fireTableRowsInserted(rows.size(), rows.size());
    }
}
