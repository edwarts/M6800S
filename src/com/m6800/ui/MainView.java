package com.m6800.ui;

import java.awt.*;
import javax.swing.*;


public class MainView extends JFrame{
        
        
            
        /**
         * 
         */
        private static final long serialVersionUID = 1L;



        public MainView(){
                
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("CHIP 8 Emulator");
        setLocationRelativeTo(null);  
        pack();
        }
        


    //========================================================== main
    public static void main(String[] args) {
        MainView window = new MainView();
        
        
        window.setSize(250, 120);
        window.setPreferredSize(new Dimension(100,100));
        window.requestFocus();
        
       
                         
                 
        Emulator t = new Emulator();
        
        window.getContentPane().add(t, BorderLayout.EAST);
        
       
        
        window.setVisible(true);
    }


}
