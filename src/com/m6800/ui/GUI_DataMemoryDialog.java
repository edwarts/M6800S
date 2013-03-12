package com.m6800.ui;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import com.m6800.cpu.CPU;

/**
 * Projet de langage JAVA IFITEP 3 2005 - Simulateur 8051 
 * <p> 
 * GUI_DataMemoryDialog
 *  
 * @author Matthieu SIMON
 * @version 1.0 du 28/06/05
 */
public class GUI_DataMemoryDialog extends JDialog {
	private JTable internalMemTable;
	private JTable externalMemTable;
	
	public GUI_DataMemoryDialog(JFrame frame) {
		super(frame);
		setTitle("Data Memory");
		getContentPane().setLayout(new GridLayout(2,0));
		internalMemTable = createMemTable(internalMemTable,CPU.getInstance().mem);
		externalMemTable = createMemTable(externalMemTable,CPU.getInstance().mem);
	}
	
	private JTable createMemTable(JTable table, byte[] dataMem) {
		DataMemTableModel tableModel;
		table = new JTable((tableModel = new DataMemTableModel(dataMem.length/8)));
		table.setPreferredScrollableViewportSize(new Dimension(1000, 1000));
		JScrollPane scrollPane = new JScrollPane(table);
		getContentPane().add(scrollPane);			
		table.getColumnModel().getColumn(0).setMaxWidth(50);
		table.getColumnModel().getColumn(0).setResizable(false);
		for(int i = 0; i < 8; i++)
			table.getColumnModel().getColumn(i+1).setPreferredWidth(30);
		for(int i = 0; i < dataMem.length/8; i++)
			table.setValueAt(Integer.toHexString(i*8).toUpperCase(), i, 0);
		table.setEnabled(false);
		table.getModel().addTableModelListener(new DataTableModelListener(table, dataMem));
		return table;
	}
	
	public void setDefaultLocation() {
		setSize(250, 160+560);	
		setLocation(520, 30);
		setVisible(true);
	}
	
	public void fillDataMemory() {
		for(int i = 0; i < 128; i++)
			for(int j = 0; j < 8; j++)				
				internalMemTable.setValueAt(Integer.toHexString(CPU.getInstance().mem[8*i+j]&0xFF).toUpperCase(), i, j+1);
		internalMemTable.setEnabled(true);
		
		for(int i = 0xC000; i < 0xC400; i++)
			for(int j = 0; j < 8; j++)
				externalMemTable.setValueAt(Integer.toHexString(CPU.getInstance().mem[8*i+j]&0xFF).toUpperCase(), i, j+1);
		externalMemTable.setEnabled(true);
	}
	
	private class DataTableModelListener implements TableModelListener {
		private byte[] dataMem;
		private JTable table;
		public DataTableModelListener(JTable table, byte[] dataMem) {
			this.dataMem = dataMem;
		}
		public void tableChanged(TableModelEvent e) {		
	        int row = e.getFirstRow();
	        int column = e.getColumn();
        	TableModel model = (TableModel)e.getSource();
        	String data = (String)model.getValueAt(row, column);
        	if(data.length() > 2) { // bytes allowed only
        		data = data.substring(0, 2);
        		model.setValueAt(data, row, column);	        		
        	}	        	
        	int tmp;
        	try {
        		tmp = Integer.parseInt(data, 16);
        	}
        	catch(NumberFormatException ex) {
        		model.setValueAt(
        				Integer.toHexString(dataMem[8*row + (column-1)]&0xFF).toUpperCase(),
						row, column);
        		return;
        	}
        	dataMem[8*row + (column-1)] = (byte) (tmp&0xFF);
	    }
	}
	private class DataMemTableModel extends AbstractTableModel {
		private String[] columnNames = { "Addr", "+0", "+1", "+2", "+3", "+4", "+5", "+6", "+7"};
		private Object[][] rowData;
		public DataMemTableModel(int rowNum) {
			super();
			rowData = new Object[rowNum][columnNames.length];
		}
		public String getColumnName(int col) {
	        return columnNames[col].toString();
	    }
	    public int getRowCount() {
	    	return rowData.length;
	    }
	    public int getColumnCount() {
	    	return columnNames.length;
	    }
	    public Object getValueAt(int row, int col) {
	        return rowData[row][col];
	    }
	    public boolean isCellEditable(int row, int col) {
	    	if(col == 0) return false; 
	    	return true;
	    }
	    public void setValueAt(Object value, int row, int col) {
	        rowData[row][col] = value;
	        fireTableCellUpdated(row, col);
	    }
	}
}
