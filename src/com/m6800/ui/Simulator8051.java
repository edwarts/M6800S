package com.m6800.ui;
import javax.swing.*;

import com.m6800.cpu.CPU;

import java.io.*;
import java.awt.event.*;
import java.util.*;
import java.sql.*;

/**
 * Projet de langage JAVA IFITEP 3 2005 - Simulateur 8051 
 * <p> 
 * Classe principale de l'application.
 *  
 * @author Matthieu SIMON
 * @version 0.2 du 29/06/05
 */
public class Simulator8051 extends JFrame implements Runnable {
	public final static double VERSION     = 0.2;	
	public final static String FRAME_TITLE = "8051 Simulator v" + VERSION;
	public final static String SPLASH_PIC  = "docs/microprocessor.png";
	public final static String ICON        = "docs/icon.gif";
	public final static String HELP_URL    = "docs/help.html";
	
	private CPU     cpu;
	private CORE_AsmParser   parser;
	private ArrayList   sourceList;
 	
	private JMenuItem   openBut;
	private JMenuItem   reloadBut;
	private JMenuItem   quitBut;
	private JMenuItem   resetBut;
	private JMenuItem   stepBut;
	private JMenuItem   runBut;
	private JMenuItem   breakBut;
	private JMenuItem   defWinBut;
	private JMenuItem   sourceWinBut;
	private JMenuItem   sfrWinBut;
	private JMenuItem   intMemWinBut;
	private JMenuItem   ioWinBut;
	private JMenuItem   aboutBut;
	private JMenuItem   seg7But;
	private JMenuItem   uartBut;
	private JMenuItem   setBrkBut;
	private JMenuItem   helpBut;
	
	private JTextArea   consoleText;
	private JScrollPane scrollPane;
	
	private GUI_SFRDialog        sfrDlg;
	private GUI_SourceCodeDialog sourceCodeDlg;
	private GUI_DataMemoryDialog dataMemDlg;
	//private GUI_IOPorts          ioPortsDlg;
	///private GUI_UART             uartDlg;
	private AbstractCollection   seg7DlgList;
	
	private boolean           threadRun;
	private javax.swing.Timer timer;
	private boolean           needReset;
	private String            asmFile;
		
	/**
	 * Crée l'objet.
	 */
	public Simulator8051() {		
		super(FRAME_TITLE);
		sourceCodeDlg = new GUI_SourceCodeDialog(this);
		sfrDlg        = new GUI_SFRDialog(this);
		dataMemDlg    = new GUI_DataMemoryDialog(this);
		//ioPortsDlg    = new GUI_IOPorts(this);
		//uartDlg       = new GUI_UART();
		seg7DlgList   = new ArrayList(0);
		createMasterFrame();
		updateConsole("8051 Simulator started.");
	}	
	
	/**
	 * Met ?jour l'interface graphique
	 */
	private void updateGUI() {
		sfrDlg.readSFRTableFromCPU();
		sourceCodeDlg.updateRowSelection(cpu.getPC());
		sourceCodeDlg.getCyclesLabel().setText("Cycles count : " + cpu.getCyclesCount());
		dataMemDlg.fillDataMemory();
		//ioPortsDlg.fillIOPorts();		
		Iterator iter = seg7DlgList.iterator();
		while(iter.hasNext())
			((GUI_7SegDisplay)iter.next()).drawSeg();
	}
	
	/**
	 * Charge un fichier source dans la mémoire du similateur.
	 * @param file Le chemin du fichier source.
	 */
	private void openAsmFile(String file) {
		if(file.equals("")) {
			JFileChooser fileChooser = new JFileChooser();
			if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
				asmFile = file = fileChooser.getSelectedFile().getPath();
				updateConsole(file + " opened.");
			}
		}
		
		if(!file.equals("")) {		
			parser = new CORE_AsmParser();
			try {
				parser.openAsmFile(file);
			}
			catch(IOException e) {
				updateConsole("** Error reading the source file. **");
				return;
			}	
			sourceList = new ArrayList(0);
			CORE_AsmLine asmline;
			try {
				while((asmline = parser.getAsmLine()) != null) {			
					sourceList.ensureCapacity(sourceList.size()+1);
					sourceList.add(asmline);			
				}		
				parser.closeParser();
			}
			catch(IOException e) {
				updateConsole("** Error reading the source file. **");
				return;
			}
			try {
				cpu = new CORE_CPU8051(sourceList);
			}
			catch(CPU8051Exception ex) {
				updateConsole(ex.toString());
				return;
			}			
			try {
				sourceCodeDlg.updateSourceCodeTable(file, sourceList);
			}
			catch(IOException e) {
				cpu = null;
				updateConsole("** Error reading the source file. **");
				return;
			}		
			try {
				cpu.setOpcodeMOVCEnabled(true);
				new CORE_HEXloader(file+".hex");
			}
			catch(IOException e){
				updateConsole("** Error reading the binary file : " + file + ".hex **");
				updateConsole("** MOVC instruction is not supported. **");
				cpu.setOpcodeMOVCEnabled(false);
			}
			catch(HEXloaderException e) {
				cpu = null;
				updateConsole(e.toString());
				return;
			}
			setTitle(FRAME_TITLE + " -- " + file);			
			needReset = false;
			updateGUI();
		}
	}
	
	/**********************************************************************
	 *                  Master Frame
	 **********************************************************************/

	private JMenuItem addItem(JMenu menu, String title, int key, int mask, MasterButtonsListener listener) {
		JMenuItem item = new JMenuItem(title);
		item.setAccelerator(KeyStroke.getKeyStroke(key, mask));
		item.addActionListener(listener);
		menu.add(item);		
		return item;
	}
	
	private void createMasterFrame() {
		JMenu menu;
		MasterButtonsListener masterButtonsListener = new MasterButtonsListener();
		JMenuBar menuBar = new JMenuBar();
		
		setIconImage((new ImageIcon(ICON)).getImage());	
		setJMenuBar(menuBar);
				
		menu = new JMenu("File");
		menuBar.add(menu);
		openBut   = addItem(menu, "Open",   KeyEvent.VK_O, ActionEvent.CTRL_MASK, masterButtonsListener);
		reloadBut = addItem(menu, "Reload", KeyEvent.VK_R, ActionEvent.CTRL_MASK, masterButtonsListener);
		menu.addSeparator();		
		quitBut   = addItem(menu, "Quit", KeyEvent.VK_Q, ActionEvent.CTRL_MASK, masterButtonsListener);
		
		menu = new JMenu("Debug");
		menuBar.add(menu);		
		resetBut = addItem(menu, "Reset", KeyEvent.VK_F4, 0, masterButtonsListener);
		runBut   = addItem(menu, "Run",   KeyEvent.VK_F5, 0, masterButtonsListener);
		breakBut = addItem(menu, "Break", KeyEvent.VK_F6, 0, masterButtonsListener);
		stepBut  = addItem(menu, "Step",  KeyEvent.VK_F7, 0, masterButtonsListener);
		menu.addSeparator();
		setBrkBut  = addItem(menu, "Set Breakpoint",  KeyEvent.VK_B, 0, masterButtonsListener);
		
		menu = new JMenu("Component");
		menuBar.add(menu);		
		seg7But = addItem(menu, "7 Seg Display", KeyEvent.VK_Q, 0, masterButtonsListener);
		uartBut = addItem(menu, "UART Terminal", KeyEvent.VK_U, 0, masterButtonsListener);
		
		menu = new JMenu("Window");
		menuBar.add(menu);	
		defWinBut    = addItem(menu, "Default", KeyEvent.VK_D, ActionEvent.CTRL_MASK, masterButtonsListener);
		menu.addSeparator();
		sourceWinBut = addItem(menu, "Source Code", KeyEvent.VK_A, 0, masterButtonsListener);
		sfrWinBut    = addItem(menu, "SFR", KeyEvent.VK_Z, 0, masterButtonsListener);
		intMemWinBut = addItem(menu, "Data Memory", KeyEvent.VK_E, 0, masterButtonsListener);
		ioWinBut     = addItem(menu, "I/O Ports", KeyEvent.VK_R, 0, masterButtonsListener);
		
		menu = new JMenu("Help");
		menuBar.add(menu);	
		helpBut = addItem(menu, "Help", KeyEvent.VK_F1, 0, masterButtonsListener);
		aboutBut = addItem(menu, "About", KeyEvent.VK_A, ActionEvent.CTRL_MASK, masterButtonsListener);
		
		consoleText = new JTextArea();
		consoleText.setEditable(false);
		scrollPane = new JScrollPane(consoleText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
												  JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		getContentPane().add(scrollPane);	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		setDefaultLocation();
		timer = new javax.swing.Timer(250, taskPerformer);
	}
	
	/**
	 * Positionne toutes les fenetres ?leur emplacement par défaut
	 */
	public void setDefaultLocation() {
		sourceCodeDlg.setDefaultLocation();
		sfrDlg.setDefaultLocation();
		dataMemDlg.setDefaultLocation();
		//ioPortsDlg.setDefaultLocation();
		setSize(500, 150);	
		setLocation(10, 30);
		setVisible(true);
	}
	
	/**
	 * Met ?jour la console.
	 * @param str La chaîne de caractère ?ajouter dans la console.
	 */
	public void updateConsole(String str) {
		consoleText.append(new Time(System.currentTimeMillis()) + " : " + str + "\n");
		consoleText.setCaretPosition(consoleText.getDocument().getLength());
		scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
	}
	
	private class MasterButtonsListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JMenuItem item = (JMenuItem)e.getSource();			
			if(item.equals(quitBut))
				Runtime.getRuntime().exit(0);
			if(item.equals(helpBut))
				new GUI_HelpDialog(HELP_URL);
			else if(item.equals(aboutBut))
				new GUI_About(SPLASH_PIC);		
			else if(item.equals(defWinBut))
				setDefaultLocation();
			else if(item.equals(sourceWinBut))
				sourceCodeDlg.setVisible(!sourceCodeDlg.isVisible());
			else if(item.equals(sfrWinBut))
				sfrDlg.setVisible(!sfrDlg.isVisible());
			else if(item.equals(intMemWinBut))
				dataMemDlg.setVisible(!dataMemDlg.isVisible());
			else if(item.equals(ioWinBut))
				ioPortsDlg.setVisible(!ioPortsDlg.isVisible());
			else if(cpu == null || item.equals(openBut))
				openAsmFile("");
			else if(item.equals(reloadBut)) {
				updateConsole("Reload");
				openAsmFile(asmFile);
			}
			else if(item.equals(resetBut)) {
				updateConsole("Reset");
				threadRun = false;
				needReset = false;
				timer.stop();
				cpu.initCPU();
				updateGUI();				
			}
			else if(item.equals(stepBut) && !needReset && !threadRun) { 
				updateConsole("Step");
				scheduleCPU();
				updateGUI();
			}
			else if(item.equals(runBut) && !needReset && !threadRun) {
					updateConsole("Running ...");
					threadRun = true;
					runThread();
					timer.start();
			}
			else if(item.equals(breakBut))
				BreakRun();
			else if(item.equals(setBrkBut))
				sourceCodeDlg.setBreakpoint();
			else if(item.equals(seg7But)) {
				synchronized(seg7DlgList) {
					Iterator iter = seg7DlgList.iterator();
					GUI_7SegDisplay dlg;
					while(iter.hasNext()) {
						if(!(dlg = ((GUI_7SegDisplay)iter.next())).isShowing()) {
							seg7DlgList.remove(dlg);
							iter = seg7DlgList.iterator();
						}
					}		
				}
				if(seg7DlgList.size() < 4)
					seg7DlgList.add(new GUI_7SegDisplay());
			}	
			else if(item.equals(uartBut)) 
				uartDlg.setVisible(!uartDlg.isVisible());
		}
	}
	
	private void BreakRun() {
		updateConsole("Break");
		threadRun = false;
		timer.stop();
		updateGUI();
	}
	
	/**
	 * Appel de l'ordonanceur du coeur.
	 */
	private void scheduleCPU() {
		try {			
			cpu.scheduleCPU();		
		}
		catch(CPU8051Exception e) {
			threadRun = false;
			needReset = true;
			timer.stop();
			cpu.setPC(cpu.getPC()-1);
			updateGUI();
			updateConsole(e.toString());
		}
		uartDlg.checkChange(cpu);
	}
	
	private void runThread() {
		new Thread(this).start();
	}
	
	/**
	 * Le thread utilis?pour le mode RUN
	 */
	public void run() {
		while(threadRun) {
			scheduleCPU();			
			if(CORE_CPU8051.sourceCode[cpu.getPC()] != null && CORE_CPU8051.sourceCode[cpu.getPC()].getBreakPoint())
				BreakRun();
			
			synchronized(seg7DlgList) {
				Iterator iter = seg7DlgList.iterator();
				while(iter.hasNext())
					((GUI_7SegDisplay)iter.next()).drawSeg();
			}
		}	
	}
	
	ActionListener taskPerformer = new ActionListener() {
		public void actionPerformed(ActionEvent evt) {
			sourceCodeDlg.getCyclesLabel().setText("Cycles count : " + cpu.getCyclesCount());
		}
	};
	
	/**********************************************************************
	 *                  Main
	 **********************************************************************/
	public static void main(String[] args) throws Exception  {
		new Thread(new Runnable() {
			public void run() {
				new GUI_SplashScreen(SPLASH_PIC, 2000);
			}
		}).start();
		Thread.sleep(1000);
		new Simulator8051();
	}
}