package com.m6800.ui;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

/**
 * Projet de langage JAVA IFITEP 3 2005 - Simulateur 8051 
 * <p> 
 * GUI_SourceCodeDialog
 *  
 * @author Matthieu SIMON
 * @version 1.0 du 28/06/05
 */
public class GUI_SourceCodeDialog extends JDialog {

	private JTable sourceCodeTable;
	private SourceCodeTableModel sourceCodeTableModel;
	private JScrollPane sourceCodeScrollPane;
	private JLabel cyclesLabel;
	private JPopupMenu popup;
	private JMenuItem brkBut;
	
	public JTable getSourceCodeTable() {
		return sourceCodeTable;
	}
	
	public GUI_SourceCodeDialog(JFrame frame) {
		super(frame);
		setTitle("Source Code");
		createSourceCodeTable(0);	
	}
	
	class PopupListener extends MouseAdapter {
	    public void mousePressed(MouseEvent e) {
	        maybeShowPopup(e);
	    }

	    public void mouseReleased(MouseEvent e) {
	        maybeShowPopup(e);
	    }

	    private void maybeShowPopup(MouseEvent e) {
	        if (e.isPopupTrigger()) {
	            popup.show(e.getComponent(),
	                       e.getX(), e.getY());
	        }
	    }
	}
	
	public void setDefaultLocation() {
		setSize(500, 560);
		setLocation(10, 190);
		setVisible(true);
	}
	
	private void createSourceCodeTable(int nb) {
		cyclesLabel = new JLabel("Cycles count : 0");
		getContentPane().add(cyclesLabel, BorderLayout.SOUTH);
		sourceCodeTable = new JTable(sourceCodeTableModel = new SourceCodeTableModel(nb));
		sourceCodeTable.setPreferredScrollableViewportSize(new Dimension(10, 10));
		sourceCodeScrollPane = new JScrollPane(sourceCodeTable);
		getContentPane().add(sourceCodeScrollPane);
		sourceCodeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		validate();
		sourceCodeTable.getColumnModel().getColumn(0).setMaxWidth(50);
		sourceCodeTable.getColumnModel().getColumn(0).setResizable(false);
		sourceCodeTable.getColumnModel().getColumn(2).setMaxWidth(25);
		sourceCodeTable.getColumnModel().getColumn(2).setResizable(false);
		sourceCodeTable.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_B) {
					//setBreakpoint();
				}
			}
		});
		
		popup = new JPopupMenu();
		brkBut = new JMenuItem("Set Breakpoint");
		brkBut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, 0));
		brkBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//setBreakpoint();
			}
		});
	    popup.add(brkBut);
	    
	    MouseListener popupListener = new PopupListener();
	    sourceCodeTable.addMouseListener(popupListener);
	}
	
/*	public void setBreakpoint() {
		String s = null;
		try {
			s = (String)sourceCodeTable.getValueAt(sourceCodeTable.getSelectedRow(), 0);
		}
		catch(Exception ex) {
		}
		if(s != null) {
			sourceCodeTable.setValueAt(
					(sourceCodeTable.getValueAt(sourceCodeTable.getSelectedRow(), 2) == null)?"B":null,
					sourceCodeTable.getSelectedRow(), 2                                                );
			CORE_AsmLine line = CORE_CPU8051.sourceCode[Integer.parseInt(((String)s), 16)];
			if(line != null)
				line.setBreakPoint(!line.getBreakPoint());
		}	*/
	}
	
	public void updateRowSelection(int pc) {
		JScrollBar bar;
		for(int i = 0; i < sourceCodeTable.getRowCount(); i++)
			if((String)sourceCodeTable.getValueAt(i, 0) != null &&
				Integer.parseInt(((String)sourceCodeTable.getValueAt(i, 0)), 16) == pc) {
				sourceCodeTable.setRowSelectionInterval(i, i);				
				bar = sourceCodeScrollPane.getVerticalScrollBar();
				bar.setValue(((bar.getMaximum()-bar.getMinimum())/sourceCodeTable.getRowCount())*(i-1));
				sourceCodeTable.updateUI();
				return;
			}
	}
	
	private void fillSourceCodeTable(String file, ArrayList sourceList) throws IOException {
		BufferedReader fileReader;	
		fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		int i = 0;
		while((file = fileReader.readLine()) != null) {
			sourceCodeTableModel.setValueAt(CORE_AsmParser.reduceString(file, "\t"), i++, 1);
		}
		fileReader.close();
		
		for(i = 0; i < sourceList.size(); i++)
			if(((CORE_AsmLine)sourceList.get(i)).getType() == CORE_AsmLine.OPCODE)
				sourceCodeTableModel.setValueAt(
						Integer.toHexString(((CORE_AsmLine)sourceList.get(i)).getAddress()).toUpperCase(),
						((CORE_AsmLine)sourceList.get(i)).getFileLine(), 0);
	}
	
	public void updateSourceCodeTable(String file, ArrayList sourceList) throws IOException {
		BufferedReader fileReader;		
		int nb = 0;
		getContentPane().removeAll();
		fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		while(fileReader.readLine() != null) nb++;
		fileReader.close();
		createSourceCodeTable(nb);	
		fillSourceCodeTable(file, sourceList);
	}
	
	public JLabel getCyclesLabel() {
		return cyclesLabel;
	}
	
	private class SourceCodeTableModel extends AbstractTableModel {
		private int rowcount = 0;
		private String[] columnNames = { "PC", "Code", "BP" };
		private Object[][] rowData;
		public SourceCodeTableModel(int rowcount) {
			super();
			this.rowcount = rowcount;	
			rowData = new Object[rowcount][columnNames.length];
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
	    	return false;
	    }
	    public boolean isCellSelectable(int row, int col) {
	    	return true;
	    }
	    public void setValueAt(Object value, int row, int col) {
	        rowData[row][col] = value;
	        fireTableCellUpdated(row, col);
	    }
	}
}