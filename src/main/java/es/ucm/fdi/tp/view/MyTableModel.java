package es.ucm.fdi.tp.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

class MyTableModel extends AbstractTableModel {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	private String[] colNames;
	List<String> names;

	MyTableModel() {
		this.colNames = new String[] { "#Player", "Color", "State" };
		names = new ArrayList<>();
		names.add("");
		names.add("");
	}

	@Override
	public String getColumnName(int col) {
		return colNames[col];
	}

	@Override
	public int getColumnCount() {
		return colNames.length;
	}

	@Override
	public int getRowCount() {
		return names != null ? names.size() : 0;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			return rowIndex;
		} else {
			return names.get(rowIndex);
		}
	}

	public void addName(String name) {
		names.add(name);
		refresh();
	}

	public void refresh() {
		fireTableDataChanged();
	}

};