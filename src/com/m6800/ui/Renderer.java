package com.m6800.ui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;

public class Renderer extends JFrame implements KeyListener{

        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        private final int pixel_width = 5;
        private final int pixel_height = 5;
        
        private final int pixel_x_offset = 8;
        private final int pixel_y_offset = 30;  
        
        private boolean[][] frame_buffer = new boolean[64][32];
        
        public KeyPad keypad = new KeyPad();
        
        
        
        /* here is the mapping of keys
         * 
         * 
         * 1 in real keypad mapped to 1 in our keyboard
         * 2 in real keypad mapped to 2 in our keyboard
         * 3 in real keypad mapped to 3 in our keyboard
         * C in real keypad mapped to 4 in our keyboard
         * 4 in real keypad mapped to Q in our keyboard
         * 5 in real keypad mapped to W in our keyboard
         * 6 in real keypad mapped to E in our keyboard
         * D in real keypad mapped to R in our keyboard
         * 7 in real keypad mapped to A in our keyboard
         * 8 in real keypad mapped to S in our keyboard
         * 9 in real keypad mapped to D in our keyboard
         * E in real keypad mapped to F in our keyboard
         * A in real keypad mapped to Z in our keyboard
         * 0 in real keypad mapped to X in our keyboard
         * B in real keypad mapped to C in our keyboard
         * F in real keypad mapped to V in our keyboard
         *      
         * 
         * 
         */
        
        
        public Renderer(){
                
                super();
                
                this.clearScreen();
                repaint();
                this.addKeyListener(this);

                                
        }
        
        
        
        
        /*
         * return boolean: true if it erased existing pixel, false if no existing pixel was erased
         */
        public boolean render(int x, int y, char[] array){
                
                                                
                boolean erased = false;
                
                for(int i=0;i<array.length;i++){
                        
                        
                        boolean[] temp = new boolean[8];
                        
                        
                        for(int j=0;j<8;j++){
                                
                                
                                int lsb = ((array[i]>>j)&0x01);
                                
                                if(lsb == 1)
                                        temp[7-j] = true;
                                else
                                        temp[7-j] = false;
                                                                
                                
                        }
                        
                        
                        for(int k=0;k<temp.length;k++){
                                
                                
                                if((x+k) < 64 && (y+i) < 32){
                                boolean before = frame_buffer[x+k][y+i];
                                
                                frame_buffer[x+k][y+i] ^= temp[k];
                                
                                boolean after = frame_buffer[x+k][y+i];
                                
                                if(before == true && after == false){
                                        erased = true;
                                }
                                
                                }
                                
                        }
                        
                }
                
                return erased;
                
                
        }
        
        
        public void setPixel(Graphics g, int x, int y, boolean color){
                
            
                if(color)
                        g.setColor(Color.BLACK);
                else
                        g.setColor(Color.WHITE);
                        
                        
            g.fillRect(pixel_width*x+pixel_x_offset, pixel_height*y+pixel_y_offset, pixel_width, pixel_height);
            
                
            
        }
        
         public void paint(Graphics g){
                
                 
                         
                 for(int i=0;i<64;i++){
                         for(int j=0;j<32;j++){
                        
                                 if(frame_buffer[i][j] == true)
                                         setPixel(g, i, j, true);
                                 else
                                         setPixel(g, i, j, false);
                                 
                         }
                         }
                         
         }

         
         
         public void printFrameBuffer(){
                 
                 for(int j=0;j<32;j++){
                         for(int i=0;i<64;i++){
                        
                
                                 if(frame_buffer[i][j] == true)
                                         System.out.print("1 ");
                                 else
                                         System.out.print("0 ");
                                 
                         }
                         
                         System.out.println();
                         }
                 
                
                 
         }
         
         
                public void clearScreen(){
                        
                         for(int i=0;i<64;i++){
                                 for(int j=0;j<32;j++){         
                
                                         frame_buffer[i][j] = false;
                                                 
                                         
                                 }
                                 }      
                }
                


        @Override
        public void keyPressed(KeyEvent e) {
                
                char i = e.getKeyChar();
                keypad.press(i);
                        
        }



        @Override
        public void keyReleased(KeyEvent e) {                   

                char i = e.getKeyChar();
                keypad.release(i);
                                
        }



        @Override
        public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
        }

         
         
        
}