package com.m6800.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.SwingConstants;
import java.awt.GridLayout;
import javax.swing.SpringLayout;

public class KeyBoard {

	private JFrame frmKeyboard;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					KeyBoard window = new KeyBoard();
					window.frmKeyboard.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public KeyBoard() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmKeyboard = new JFrame();
		frmKeyboard.setTitle("KeyBoard");
		frmKeyboard.setBounds(100, 100, 270, 352);
		frmKeyboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frmKeyboard.getContentPane().setLayout(springLayout);
		
		JButton bt_num1 = new JButton("1");
		springLayout.putConstraint(SpringLayout.WEST, bt_num1, 0, SpringLayout.WEST, frmKeyboard.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, bt_num1, -204, SpringLayout.SOUTH, frmKeyboard.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, bt_num1, 77, SpringLayout.WEST, frmKeyboard.getContentPane());
		frmKeyboard.getContentPane().add(bt_num1);
		
		JButton bt_num2 = new JButton("2");
		springLayout.putConstraint(SpringLayout.NORTH, bt_num2, 0, SpringLayout.NORTH, bt_num1);
		springLayout.putConstraint(SpringLayout.WEST, bt_num2, 6, SpringLayout.EAST, bt_num1);
		springLayout.putConstraint(SpringLayout.EAST, bt_num2, 83, SpringLayout.EAST, bt_num1);
		frmKeyboard.getContentPane().add(bt_num2);
		
		JButton bt_num4 = new JButton("4");
		springLayout.putConstraint(SpringLayout.NORTH, bt_num4, 25, SpringLayout.SOUTH, bt_num1);
		springLayout.putConstraint(SpringLayout.WEST, bt_num4, 0, SpringLayout.WEST, frmKeyboard.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, bt_num4, 0, SpringLayout.EAST, bt_num1);
		frmKeyboard.getContentPane().add(bt_num4);
		
		JButton bt_num5 = new JButton("5");
		springLayout.putConstraint(SpringLayout.NORTH, bt_num5, 0, SpringLayout.NORTH, bt_num4);
		springLayout.putConstraint(SpringLayout.WEST, bt_num5, 0, SpringLayout.WEST, bt_num2);
		springLayout.putConstraint(SpringLayout.EAST, bt_num5, -94, SpringLayout.EAST, frmKeyboard.getContentPane());
		frmKeyboard.getContentPane().add(bt_num5);
		
		JButton bt_num7 = new JButton("7");
		springLayout.putConstraint(SpringLayout.WEST, bt_num7, 0, SpringLayout.WEST, bt_num1);
		springLayout.putConstraint(SpringLayout.EAST, bt_num7, 0, SpringLayout.EAST, bt_num1);
		frmKeyboard.getContentPane().add(bt_num7);
		
		JButton bt_num8 = new JButton("8");
		springLayout.putConstraint(SpringLayout.WEST, bt_num8, 6, SpringLayout.EAST, bt_num7);
		springLayout.putConstraint(SpringLayout.NORTH, bt_num7, 0, SpringLayout.NORTH, bt_num8);
		springLayout.putConstraint(SpringLayout.NORTH, bt_num8, 26, SpringLayout.SOUTH, bt_num5);
		frmKeyboard.getContentPane().add(bt_num8);
		
		JButton bt_numA = new JButton("A");
		springLayout.putConstraint(SpringLayout.NORTH, bt_numA, 10, SpringLayout.NORTH, frmKeyboard.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, bt_numA, 0, SpringLayout.WEST, bt_num1);
		springLayout.putConstraint(SpringLayout.EAST, bt_numA, 0, SpringLayout.EAST, bt_num1);
		frmKeyboard.getContentPane().add(bt_numA);
		
		JButton bt_numB = new JButton("B");
		springLayout.putConstraint(SpringLayout.NORTH, bt_numB, 0, SpringLayout.NORTH, bt_numA);
		springLayout.putConstraint(SpringLayout.WEST, bt_numB, 83, SpringLayout.WEST, frmKeyboard.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, bt_numB, 0, SpringLayout.EAST, bt_num2);
		frmKeyboard.getContentPane().add(bt_numB);
		
		JButton bt_num3 = new JButton("3");
		springLayout.putConstraint(SpringLayout.NORTH, bt_num3, 0, SpringLayout.NORTH, bt_num1);
		springLayout.putConstraint(SpringLayout.WEST, bt_num3, 6, SpringLayout.EAST, bt_num2);
		frmKeyboard.getContentPane().add(bt_num3);
		
		JButton bt_num6 = new JButton("6");
		springLayout.putConstraint(SpringLayout.EAST, bt_num3, 0, SpringLayout.EAST, bt_num6);
		springLayout.putConstraint(SpringLayout.NORTH, bt_num6, 0, SpringLayout.NORTH, bt_num4);
		springLayout.putConstraint(SpringLayout.WEST, bt_num6, 7, SpringLayout.EAST, bt_num5);
		springLayout.putConstraint(SpringLayout.EAST, bt_num6, -10, SpringLayout.EAST, frmKeyboard.getContentPane());
		frmKeyboard.getContentPane().add(bt_num6);
		
		JButton bt_num9 = new JButton("9");
		springLayout.putConstraint(SpringLayout.EAST, bt_num8, -6, SpringLayout.WEST, bt_num9);
		springLayout.putConstraint(SpringLayout.WEST, bt_num9, 166, SpringLayout.WEST, frmKeyboard.getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, bt_num9, 26, SpringLayout.SOUTH, bt_num6);
		springLayout.putConstraint(SpringLayout.EAST, bt_num9, -10, SpringLayout.EAST, frmKeyboard.getContentPane());
		frmKeyboard.getContentPane().add(bt_num9);
		
		JButton bt_numF = new JButton("F");
		springLayout.putConstraint(SpringLayout.WEST, bt_numF, 0, SpringLayout.WEST, bt_num6);
		springLayout.putConstraint(SpringLayout.EAST, bt_numF, 0, SpringLayout.EAST, bt_num3);
		frmKeyboard.getContentPane().add(bt_numF);
		
		JButton bt_numD = new JButton("D");
		springLayout.putConstraint(SpringLayout.NORTH, bt_numF, 0, SpringLayout.NORTH, bt_numD);
		springLayout.putConstraint(SpringLayout.NORTH, bt_numD, 14, SpringLayout.SOUTH, bt_numA);
		springLayout.putConstraint(SpringLayout.WEST, bt_numD, 0, SpringLayout.WEST, bt_num1);
		springLayout.putConstraint(SpringLayout.EAST, bt_numD, 0, SpringLayout.EAST, bt_num1);
		frmKeyboard.getContentPane().add(bt_numD);
		
		JButton bt_numE = new JButton("E");
		springLayout.putConstraint(SpringLayout.NORTH, bt_numE, 0, SpringLayout.NORTH, bt_numD);
		springLayout.putConstraint(SpringLayout.WEST, bt_numE, 83, SpringLayout.WEST, frmKeyboard.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, bt_numE, 0, SpringLayout.EAST, bt_num2);
		frmKeyboard.getContentPane().add(bt_numE);
		
		JButton bt_numC = new JButton("C");
		springLayout.putConstraint(SpringLayout.NORTH, bt_numC, 0, SpringLayout.NORTH, bt_numA);
		springLayout.putConstraint(SpringLayout.WEST, bt_numC, 166, SpringLayout.WEST, frmKeyboard.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, bt_numC, 0, SpringLayout.EAST, bt_num3);
		frmKeyboard.getContentPane().add(bt_numC);
		
		JButton bt_Fn = new JButton("FN");
		springLayout.putConstraint(SpringLayout.NORTH, bt_Fn, 30, SpringLayout.SOUTH, bt_num7);
		springLayout.putConstraint(SpringLayout.WEST, bt_Fn, 10, SpringLayout.WEST, frmKeyboard.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, bt_Fn, 73, SpringLayout.SOUTH, bt_num7);
		frmKeyboard.getContentPane().add(bt_Fn);
		
		JButton bt_reset = new JButton("Reset");
		springLayout.putConstraint(SpringLayout.NORTH, bt_reset, 0, SpringLayout.NORTH, bt_Fn);
		springLayout.putConstraint(SpringLayout.SOUTH, bt_reset, 0, SpringLayout.SOUTH, bt_Fn);
		springLayout.putConstraint(SpringLayout.EAST, bt_reset, -23, SpringLayout.EAST, frmKeyboard.getContentPane());
		frmKeyboard.getContentPane().add(bt_reset);
		
		JButton bt_num0 = new JButton("0");
		springLayout.putConstraint(SpringLayout.WEST, bt_reset, 8, SpringLayout.EAST, bt_num0);
		springLayout.putConstraint(SpringLayout.EAST, bt_Fn, -6, SpringLayout.WEST, bt_num0);
		springLayout.putConstraint(SpringLayout.NORTH, bt_num0, 30, SpringLayout.SOUTH, bt_num8);
		springLayout.putConstraint(SpringLayout.WEST, bt_num0, 0, SpringLayout.WEST, bt_num2);
		springLayout.putConstraint(SpringLayout.EAST, bt_num0, 0, SpringLayout.EAST, bt_num2);
		frmKeyboard.getContentPane().add(bt_num0);
	}
}
