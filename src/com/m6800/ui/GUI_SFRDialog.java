package com.m6800.ui;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import com.m6800.cpu.CPU;

/**
 * Projet de langage JAVA IFITEP 3 2005 - Simulateur 8051 
 * <p> 
 * GUI_SFRDialog
 *  
 * @author Matthieu SIMON
 * @version 1.0 du 28/06/05
 */
public class GUI_SFRDialog extends JDialog {
	
	private JTable sfrTable;
	private SFRTableModel sfrTableModel;
/*	private final static int[] sfrResolveTable = {
			CORE_CPU8051.P0,
			CORE_CPU8051.SP,
			CORE_CPU8051.DPL,
			CORE_CPU8051.DPH,
			CORE_CPU8051.PCON,
			CORE_CPU8051.TCON,
			CORE_CPU8051.TMOD,
			CORE_CPU8051.TL0,
			CORE_CPU8051.TL1,
			CORE_CPU8051.TH0,
			CORE_CPU8051.TH1,
			CORE_CPU8051.P1,
			CORE_CPU8051.SCON,
			CORE_CPU8051.SBUF,
			CORE_CPU8051.P2,
			CORE_CPU8051.IE,
			CORE_CPU8051.P3,
			CORE_CPU8051.IP,
			CORE_CPU8051.PSW,
			CORE_CPU8051.ACC,
			CORE_CPU8051.B
	};*/
	private final static int[] sfrResolveTable = 
		{
		CPU.getInstance().IX,
		CPU.getInstance().PC,
		CPU.getInstance().SP,
		CPU.getInstance().ACCA,
		CPU.getInstance().ACCB,
		CPU.getInstance().CC,
		CPU.getInstance().irq
		};
	public GUI_SFRDialog(JFrame frame) {
		super(frame);
		setTitle("SFR");
		sfrTable = new JTable((sfrTableModel = new SFRTableModel()));
		sfrTable.setPreferredScrollableViewportSize(new Dimension(7, 7));
		sfrTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		sfrTable.setDragEnabled(false);
		sfrTable.setEnabled(false);
		JScrollPane scrollPane = new JScrollPane(sfrTable);
		getContentPane().add(scrollPane);	
        sfrTable.getModel().addTableModelListener(new TableModelListener() {
			public void tableChanged(TableModelEvent e) {		
		        int row = e.getFirstRow();
		        int column = e.getColumn();
	        	TableModel model = (TableModel)e.getSource();
	        	String data = (String)model.getValueAt(row, column);
	        	if(data.length() > 2) { // bytes allowed only
	        		data = data.substring(0, 2);
	        		sfrTable.setValueAt(data, row, column);	        		
	        	}	        	
	        	int tmp;
	        	try {
	        		tmp = Integer.parseInt(data, 16);
	        	}
	        	catch(NumberFormatException ex) {
	        		sfrTable.setValueAt(
	        				Integer.toHexString(sfrResolveTable[row]&0xFF).toUpperCase(),
							row, column);
	        		return;
	        	}
	        	//CORE_CPU8051.internalDataMem[sfrResolveTable[row]] = tmp&0xFF;
		    }
		});
	}
	
	public void setDefaultLocation() {
		setSize(210, 385);	
		setLocation(530+250, 30);
		setVisible(true);
	}
	
	public void readSFRTableFromCPU() {

		for(int i = 0; i < sfrResolveTable.length; i++)
			sfrTable.setValueAt(
					Integer.toHexString(CPU.getInstance().mem[i]&0xFF).toUpperCase(),
					i, 1);
		sfrTable.setEnabled(true);
	}
	
	private class SFRTableModel extends AbstractTableModel {
		private String[] columnNames = { "Register", "Value" };
		/*
		 * CPU.getInstance().IX,
		CPU.getInstance().PC,
		CPU.getInstance().SP,
		CPU.getInstance().ACCA,
		CPU.getInstance().ACCB,
		CPU.getInstance().CC,
		CPU.getInstance().irq
		 * 
		 * 
		 * 
		 * 
		 * */
		private Object[][] rowData = {
				{ "IX"   , null },
				{ "PC"   , null },
				{ "SP"  , null },
				{ "ACCA"  , null },
				{ "ACCB" , null },
				{ "CC" , null },
				{ "IRQ" , null }
		};
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
