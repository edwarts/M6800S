package com.m6800.ui;

public class KeyPad {

    
    public static boolean[] keypad = new boolean[16];
    
    
    public boolean isKeyPressed(int i){
            
            return keypad[i];
            
    }
    
    
    
    public int isAnyKeyPressed(){
            
            for(int i=0;i<16;i++){
                    
                    if(keypad[i] == true){
                            return i;
                    }
                    
            }
            
            return -1;
            
    }
    
    
    public void press(char i){
            
            if(i == '1'){
                    keypad[0] = true;
            }
            else if(i == '2'){
                    keypad[1] = true;
            }
            else if(i == '3'){
                    keypad[2] = true;
            }
            else if(i == '4'){
                    keypad[3] = true;
            }
            if(i == 'q'){
                    keypad[4] = true;
            }
            else if(i == 'w'){
                    keypad[5] = true;
            }
            else if(i == 'e'){
                    keypad[6] = true;
            }
            else if(i == 'r'){
                    keypad[7] = true;
            }
            else if(i == 'a'){
                    keypad[8] = true;
            }
            else if(i == 's'){
                    keypad[9] = true;
            }
            else if(i == 'd'){
                    keypad[10] = true;
            }
            else if(i == 'f'){
                    keypad[11] = true;
            }
            else if(i == 'z'){
                    keypad[12] = true;
            }
            else if(i == 'x'){
                    keypad[13] = true;
            }
            else if(i == 'c'){
                    keypad[14] = true;
            }
            else if(i == 'v'){
                    keypad[15] = true;
            }
            
    }
    
    public void release(char i){
            
            if(i == '1'){
                    keypad[0] = false;
            }
            else if(i == '2'){
                    keypad[1] = false;
            }
            else if(i == '3'){
                    keypad[2] = false;
            }
            else if(i == '4'){
                    keypad[3] = false;
            }
            if(i == 'q'){
                    keypad[4] = false;
            }
            else if(i == 'w'){
                    keypad[5] = false;
            }
            else if(i == 'e'){
                    keypad[6] = false;
            }
            else if(i == 'r'){
                    keypad[7] = false;
            }
            else if(i == 'a'){
                    keypad[8] = false;
            }
            else if(i == 's'){
                    keypad[9] = false;
            }
            else if(i == 'd'){
                    keypad[10] = false;
            }
            else if(i == 'f'){
                    keypad[11] = false;
            }
            else if(i == 'z'){
                    keypad[12] = false;
            }
            else if(i == 'x'){
                    keypad[13] = false;
            }
            else if(i == 'c'){
                    keypad[14] = false;
            }
            else if(i == 'v'){
                    keypad[15] = false;
            }
            
            
    }
    
}