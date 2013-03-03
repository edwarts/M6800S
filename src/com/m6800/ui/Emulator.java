package com.m6800.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;


        
        public class Emulator extends JPanel
           implements ActionListener {
                
                
                /**
                 * 
                 */
                private static final long serialVersionUID = 1L;

                CPU cpu;
                
                Thread thread;
           JButton go;
            
           JFileChooser chooser;
           String choosertitle;
            
           Renderer renderer;
           
           
          public Emulator() {
                  
                          
            go = new JButton("Open game ...");
            
            
            JLabel label = new JLabel("Programmed by Sanjay Giri", JLabel.CENTER);
            JLabel label2 = new JLabel("Copyright (c) 2012", JLabel.CENTER);
            
            label.setForeground(Color.gray);
            label.setFont(new Font("Courier New", Font.ITALIC, 12));
            label2.setForeground(Color.gray);
            label2.setFont(new Font("Courier New", Font.ITALIC, 12));
            
            
            
            
            add(label);
            add(label2);
            
            go.addActionListener(this);
            add(go);
           }
          
          
          public void runGame(String path){
                  
                  
                        
                        if(cpu!=null){
                                cpu.stop();
                                }
        

                  if(thread!=null){
                                if(thread.isAlive()){
                                        cpu.stop();
                                        
                                }
                        }
                  
                  
                  if(renderer != null){
                          renderer.clearScreen();
                          renderer.setVisible(false);
                          renderer.dispose();
                  }
                  
                    renderer = new Renderer();
                    
                renderer.setSize(335,200);
                    renderer.setPreferredSize(new Dimension(400,400));
            renderer.setVisible(true);

        
                  
                    cpu = new CPU(renderer);
                    
                    cpu.loadROM(path);
                    cpu.initialize();
                                    
                    thread = new Thread(cpu, "th1");
                    thread.start();
                    
                                    
                     
                  
          }
          
          
          
         
         
          public void actionPerformed(ActionEvent e) {
                  
                                 
            chooser = new JFileChooser(); 
           
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                   
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "chip8 files (*.ch8)", "ch8");
            
            chooser.setAcceptAllFileFilterUsed(false);
                
                
                chooser.addChoosableFileFilter(filter);
            
            chooser.setCurrentDirectory(new java.io.File("."));
            chooser.setDialogTitle(choosertitle);
            
            
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
                
                              
              runGame(chooser.getSelectedFile().toString());
              
              
              
              }
            else {
              System.out.println("No Selection ");
              }
             }
            
          public Dimension getPreferredSize(){
            return new Dimension(200, 200);
            }
             
          

        
        }