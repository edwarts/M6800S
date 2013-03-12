package com.m6800.test;

import java.util.Random;
import java.io.IOException;
import java.io.RandomAccessFile;



public class CPU implements Runnable{
        
        
public Renderer renderer;

                
        
        //NOTE: char data type is 16 bits in java. If i dont do extra 0s in the start it will sign extend the MSB               
        public char[] font_set = {
                
                0x00f0, 0x0090, 0x0090, 0x0090, 0x00f0,                         //0
                0x0020, 0x0060, 0x0020, 0x0020, 0x0070,                         //1
                0x00f0, 0x0010, 0x00f0, 0x0080, 0x00f0,                         //2
                0x00f0, 0x0010, 0x00f0, 0x0010, 0x00f0,                         //3
                0x0090, 0x0090, 0x00f0, 0x0010, 0x0010,                         //4
                0x00f0, 0x0080, 0x00f0, 0x0010, 0x00f0,                         //5
                0x00f0, 0x0080, 0x00f0, 0x0090, 0x00f0,                         //6
                0x00f0, 0x0010, 0x0020, 0x0040, 0x0040,                         //7
                0x00f0, 0x0090, 0x00f0, 0x0090, 0x00f0,                         //8
                0x00f0, 0x0090, 0x00f0, 0x0010, 0x00f0,                         //9
                0x00f0, 0x0090, 0x00f0, 0x0090, 0x0090,                         //A
                0x00e0, 0x0090, 0x00e0, 0x0090, 0x00e0,                         //B
                0x00f0, 0x0080, 0x0080, 0x0080, 0x00f0,                         //C
                0x00e0, 0x0090, 0x0090, 0x0090, 0x00e0,                         //D
                0x00f0, 0x0080, 0x00f0, 0x0080, 0x00f0,                         //E
                0x00f0, 0x0080, 0x00f0, 0x0080, 0x0080                          //F
                
        };
        
        
        public boolean running;
        
        public char[] memory = new char[4096];  
        public char[] registers = new char[16];
        public short[] stack = new short[16];
        
        public short index;
        
        public short pc;
        public short sp;
        
        public char delay;
        public char sound;
        
        
        public CPU(Renderer r){
                
                this.renderer= r;       
                
        }
        
        
        public void initialize(){
                
                pc = 0x0200;
                sp = 0;
                index = 0;
                
                loadFont();
                
        }
        
        
        public void printState(){
                
                System.out.print("R0="+Integer.toHexString(registers[0])+ " ");
                System.out.print("R1="+Integer.toHexString(registers[1])+ " ");
                System.out.print("R2="+Integer.toHexString(registers[2])+ " ");
                System.out.print("R3="+Integer.toHexString(registers[3])+ " ");
                System.out.print("R4="+Integer.toHexString(registers[4])+ " ");
                System.out.print("R5="+Integer.toHexString(registers[5])+ " ");
                System.out.print("R6="+Integer.toHexString(registers[6])+ " ");
                System.out.print("R7="+Integer.toHexString(registers[7])+ " ");
                System.out.print("R8="+Integer.toHexString(registers[8])+ " ");
                System.out.print("R9="+Integer.toHexString(registers[9])+ " ");
                System.out.print("R10="+Integer.toHexString(registers[10])+ " ");
                System.out.print("R11="+Integer.toHexString(registers[11])+ " ");
                System.out.print("R12="+Integer.toHexString(registers[12])+ " ");
                System.out.print("R13="+Integer.toHexString(registers[13])+ " ");
                System.out.print("R14="+Integer.toHexString(registers[14])+ " ");
                System.out.print("R15="+Integer.toHexString(registers[15])+ " ");
                System.out.println();
                System.out.println("Index is "+Integer.toHexString(index)+ " ");
                System.out.println();
                System.out.println();
                
                
                
        }
        


        
        
        public void loadROM(String file){
                
                
                
                try{
        
                        RandomAccessFile in = new RandomAccessFile(file, "r");
                                
                        byte[] bytes = new byte[(int)in.length()];
                
                        in.read(bytes, 0, (int)in.length());
                        
                        
                        for(int i=0;i<bytes.length;i++){
                                memory[0x200+i] = (char)bytes[i];
                        }
                        
                                                
                        
                }
                catch(IOException i){
                        System.out.println("Cannot open file");
                }
                
        
        }
        
        
        
////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////ALL THE INSTRUCTIONS FOR THIS CPU///////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
        
        public void CLS(){
                
                renderer.clearScreen();
                
        }
        
        
        public void RET(){
                
                pc = stack[sp];
                sp--;
                
        }
        
        public void JP_addr(){
                
                pc = (short)(((memory[pc-2]&0x0f)<<8)|(memory[pc-1]&0xff));
                
        }
        
        
        public void CALL_addr(){
                
                sp++;
                stack[sp] = pc;
                pc = (short)(((memory[pc-2]&0x0f)<<8)|(memory[pc-1]&0xff));
                
                
        }
        
        public void SE_Vx_byte(){
                
                if(registers[memory[pc-2]&0x0f] == (memory[pc-1]&0xff)){
                        pc+=2;
                }
                        
        }
        
        
        public void SNE_Vx_byte(){
                
                if(registers[memory[pc-2]&0x0f] != (memory[pc-1]&0xff)){
                        pc+=2;
                }
                                
        }
        
        
        public void SE_Vx_Vy(){
                                                
                if(registers[memory[pc-2]&0x0f] == registers[(memory[pc-1]>>4)&0x0f]){
                        pc+=2;
                }
                
        }
        
        
        public void LD_Vx_byte(){
                
                registers[memory[pc-2]&0x0f] = (char)(memory[pc-1]&0xff);
                
        }
        
        
        public void ADD_Vx_byte(){
                
                registers[memory[pc-2]&0x0f] = (char) (((memory[pc-1]&0xff) + registers[memory[pc-2]&0x0f])&0xff);              
                
        }
        
        
        public void LD_Vx_Vy(){
                
                registers[memory[pc-2]&0x0f] = registers[(memory[pc-1]>>4)&0x0f];
                
        }
        
        
        public void OR_Vx_Vy(){
                
                registers[memory[pc-2]&0x0f] = (char) (registers[memory[pc-2]&0x0f] | registers[(memory[pc-1]>>4)&0x0f]); 
                
        }
        
        
        public void AND_Vx_Vy(){
                
                registers[memory[pc-2]&0x0f] = (char) (registers[memory[pc-2]&0x0f] & registers[(memory[pc-1]>>4)&0x0f]); 

        }
        

        public void XOR_Vx_Vy(){
                
                registers[memory[pc-2]&0x0f] = (char) ((registers[memory[pc-2]&0x0f] ^ registers[(memory[pc-1]>>4)&0x0f])&0xff); 

        }

        
        public void ADD_Vx_Vy(){
                
                int result =  (registers[memory[pc-2]&0x0f] + registers[(memory[pc-1]>>4)&0x0f]);
                
                if(result > 255){
                        
                        registers[15] = 1; 
                        registers[memory[pc-2]&0x0f] = (char)(result&0xff);
                        
                }else
                {
                        registers[15] = 0; 
                        registers[memory[pc-2]&0x0f] = (char)(result&0xff); 
                }
                
        }
        
        
        public void SUB_Vx_Vy(){
                
                int result =  (registers[memory[pc-2]&0x0f] - registers[(memory[pc-1]>>4)&0x0f]);
                
                if(registers[memory[pc-2]&0x0f] > registers[(memory[pc-1]>>4)&0x0f]){
                        
                        registers[15] = 1; 
                        registers[memory[pc-2]&0x0f] = (char)(result&0xff);
                        
                }else
                {
                        registers[15] = 0; 
                        registers[memory[pc-2]&0x0f] = (char)(result&0xff); 
                }
                
        }
        
        
                
        public void SHR_Vx(){
                
                short temp = (short)registers[memory[pc-2]&0x0f];
                
                if((temp&0x001) == 1)
                        registers[15] = 1;
                else
                        registers[15] = 0;
                        
                
                registers[memory[pc-2]&0x0f] = (char)((registers[memory[pc-2]&0x0f] >> 1)&0xff);
                
        }
        
        
        public void SUBN_Vx_Vy(){
                
                int result =  (registers[(memory[pc-1]>>4)&0x0f] - registers[memory[pc-2]&0x0f]);
                
                if(registers[(memory[pc-1]>>4)&0x0f] > registers[memory[pc-2]&0x0f]){
                        
                        registers[15] = 1; 
                        registers[memory[pc-2]&0x0f] = (char)(result&0xff);
                        
                }else
                {
                        registers[15] = 0; 
                        registers[memory[pc-2]&0x0f] = (char)(result&0xff); 
                }
                
        }
        
        
        public void SHL_Vx(){
                
                short temp = (short)registers[memory[pc-2]&0x0f];
                
                if(((temp>>7)&0x1) == 1)
                        registers[15] = 1;
                else
                        registers[15] = 0;
                        
                
                registers[memory[pc-2]&0x0f] = (char)((registers[memory[pc-2]&0x0f] << 1)&0xff);
                
        }
        
        
        
        public void SNE_Vx_Vy(){
                
                if(registers[memory[pc-2]&0x0f] != registers[(memory[pc-1]>>4)&0x0f]){
                        pc+=2;
                }
                
        }
        
        
        public void LD_I_addr(){

                index = (short)(((memory[pc-2]&0x0f)<<8)|(memory[pc-1]&0xff));
                
        }
        
        
        public void JP_V0_addr(){
                
                pc = (short)(((((memory[pc-2]&0x0f)<<8)|(memory[pc-1]&0xff)) + registers[0])&0xffff);
                
        }
        
        
        public void RND_Vx_byte(){
        
                Random rn = new Random();
                
                int maximum = 255;
                int minimum = 0;
                
                int n = maximum - minimum + 1;
                int i = rn.nextInt() % n;
                int randomNum =  minimum + i;
                
                registers[memory[pc-2]&0x0f] = (char) ((randomNum & (memory[pc-1]&0x0f))&0xff); 
                
        }
        
        
        public void DRW_Vx_Vy_nibble(){
                
                char[] temp = new char[memory[pc-1]&0x0f];
                
                int temp_index = index;
                
                
                for(int i=0;i<temp.length;i++){
                        
                        temp[i] = memory[temp_index];
                        temp_index++;
                                
                }
                
                
                
                if(renderer.render(((registers[memory[pc-2]&0x0f]&0xff)%64), ((registers[(memory[pc-1]>>4)&0x0f]&0xff)%32), temp)){
                        registers[15] = 1; 
                }
                else
                {
                        registers[15] = 0; 
                }
        }
        
        
        public void SKP_Vx(){
                
                char key = registers[memory[pc-2]&0x0f];
                
                if(renderer.keypad.isKeyPressed(convertKeyToKeyIndex(key))){
                        pc+=2;
                }
                
        }
        
        public void SKNP_Vx(){

                char key = registers[memory[pc-2]&0x0f];
                
                if(!renderer.keypad.isKeyPressed(convertKeyToKeyIndex(key))){
                        pc+=2;
                }
                
                
        }
        
        
        public void LD_Vx_DT(){
                
                registers[memory[pc-2]&0x0f] = (char)(delay&0xff);
                
        }
        
        public void LD_Vx_K(){
                
                
                int key = 0;
                
                
                while(true){
                        
                        if((key = renderer.keypad.isAnyKeyPressed()) != -1){
                                                                
                                break;
                        }
                        
                }
                
                
                registers[memory[pc-2]&0x0f] = convertKeyIndexToKey(key);
                
        }
        
        
        
        
        
        public void LD_DT_Vx(){
                
                
                delay = registers[memory[pc-2]&0x0f];
                
        }
        
        
        public  void LD_ST_Vx(){
                
                sound = registers[memory[pc-2]&0x0f];
                                
        }
        
        
        public  void ADD_I_Vx(){
                
                index = (short) (index + registers[memory[pc-2]&0x0f]);
                
        }
        
        
        public  void LD_F_Vx(){
                
                index = (short) (registers[memory[pc-2]&0x0f] * 5);

        }
        
        
        public  void LD_B_Vx(){
                
                char value = registers[memory[pc-2]&0x0f];
                
                char ones = (char)(value % 10);
                
                char valueb = (char)(value/10);
                
                char tens = (char)(valueb % 10);
                
                char valuec = (char)(valueb/10);
                
                char hundreds = (char) (valuec % 10);
                
                memory[index] = (char)(hundreds&0xff);
                memory[index+1] = (char)(tens&0xff);
                memory[index+2] = (char)(ones&0xff);
                
                
        }

        
        public  void LD_i_Vx(){
                
                int rx = memory[pc-2]&0x0f;
                
                
                for(int i=0;i<=rx;i++){
                        memory[index + i] = registers[i];
                }
                
        }
        
        
        
        /*INTERESTING BUG FIXED:*/
        public  void LD_Vx_i(){
                
                int rx = memory[pc-2]&0x0f;
                
                for(int i=0;i<=rx;i++){
                        //registers[i] = (char)(memory[index + i]);                             //before  HERE IT SIGN EXTENDED THE VALUE WHICH GAVE SOME PROBLEMS IN GRAPHICS
                        registers[i] = (char)(memory[index + i]&0xff);                          //after
                }
                
        }
        
        
////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////END OF INSTRUCTIONS FOR THIS CPU////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////

        
        
        
        
        
        public  void fetchDecodeExecute() throws InterruptedException{
                
                //fetch
                char first = memory[pc];
                char second = memory[pc+1];
                
                
                char opcode = (char)((first >> 4)&0x0f);
                
                        
                pc += 2;
                
                
                //decode and execute
                
                switch(opcode){
                
                case 0x00:
                        if((char)(second&0x0f) == 0x00){
                                CLS();

                        }
                        else if((char)(second&0x0f) == 0x0E){
                                RET();

                        }
                break;
                
                case 0x01:
                        JP_addr();

                break;
                
                case 0x02:
                        CALL_addr();

                break;
                
                case 0x03:              
                        SE_Vx_byte();

                break;
                
                case 0x04:
                        SNE_Vx_byte();

                break;
                
                case 0x05:
                        SE_Vx_Vy();

                break;
                
                case 0x06:
                        LD_Vx_byte();

                break;
                
                case 0x07:
                        ADD_Vx_byte();

                break;
                
                case 0x08:
                        if((char)(second&0x0f) == 0x00){
                                LD_Vx_Vy();

                        }
                        else if((char)(second&0x0f) == 0x01){
                                OR_Vx_Vy();

                        }
                        else if((char)(second&0x0f) == 0x02){
                                AND_Vx_Vy();

                        }
                        else if((char)(second&0x0f) == 0x03){
                                XOR_Vx_Vy();

                        }
                        else if((char)(second&0x0f) == 0x04){
                                ADD_Vx_Vy();

                        }
                        else if((char)(second&0x0f) == 0x05){
                                SUB_Vx_Vy();

                        }
                        else if((char)(second&0x0f) == 0x06){
                                SHR_Vx();

                        }
                        else if((char)(second&0x0f) == 0x07){
                                SUBN_Vx_Vy();

                        }
                        else if((char)(second&0x0f) == 0x0E){
                                SHL_Vx();

                        }
                break;
                
                case 0x09:
                        SNE_Vx_Vy();                    

                break;
                
                case 0x0A:
                        LD_I_addr();

                break;
                
                case 0x0B:
                        JP_V0_addr();

                break;

                case 0x0C:
                        RND_Vx_byte();

                break;

                case 0x0D:
                        DRW_Vx_Vy_nibble();

                break;

                case 0x0E:
                        if((char)(second&0x0f) == 0x0E){
                                SKP_Vx();

                        }
                        else if((char)(second&0x0f) == 0x01){
                                SKNP_Vx();

                        }
                break;

                case 0x0F:
                        if((char)(second&0x0f) == 0x07){
                                LD_Vx_DT();

                        }
                        else if((char)(second&0x0f) == 0x0A){
                                LD_Vx_K();

                        }
                        else if(second == 0x15){
                                LD_DT_Vx();

                        }
                        else if(second == 0x18){
                                LD_ST_Vx();

                        }
                        else if(second == 0x1E){
                                ADD_I_Vx();

                        }
                        else if(second == 0x29){
                                LD_F_Vx();

                        }
                        else if(second == 0x33){
                                LD_B_Vx();

                        }
                        else if(second == 0x55){
                                LD_i_Vx();

                        }
                        else if(second == 0x65){
                                LD_Vx_i();

                        }                       
                break;

                
                }
                
                
                
                if(delay > 0){
                        delay--;
                }
                
                
                renderer.repaint();
                Thread.sleep(1);
                
                
        }
        
        
        
        
        //ENTRY POINT OF THIS THREAD
        public void run(){
                
                running = true;
                
            while(running)
            {
              
              
                try {
                                fetchDecodeExecute();
                        } catch (InterruptedException e) {
                                e.printStackTrace();
                        }
                
           
            }

                
            
            
        }
        
                
         
        //STOPS THE THREAD
         public void stop(){
                 running = false;
         }
         
        
         public  void loadFont(){
                        
                        for(int i=0;i<80;i++){
                                memory[i] = font_set[i];
                        }
                                        
                        
                }
                
         
         
         public  int convertKeyToKeyIndex(int key){
                 
                        switch(key){
                        
                        case 1:
                                return 0;
                                                
                        case 2:
                                return 1;

                        case 3:
                                return 2;

                        case 12:
                                return 3;

                        case 4:
                                return 4;
                        
                        case 5:
                                return 5;
                                
                        
                        case 6:
                                return 6;
                        
                        case 13:
                                return 7;

                        case 7:
                                return 8;

                        case 8:
                                return 9;

                        case 9:
                                return 10;

                        case 14:
                                return 11;

                        case 10:
                                return 12;

                        case 0:
                                return 13;

                        case 11:
                                return 14;

                        case 15:
                                return 15;
                        
                        
                        }
                        
                        
                        return -1;
                 
         }
         
         
         public  char convertKeyIndexToKey(int index){
                        
                        
                        switch(index){
                        
                        case 0:
                                return 0x0001;
                                                
                        case 1:
                                return 0x0002;

                        case 2:
                                return 0x0003;

                        case 3:
                                return 0x000c;

                        case 4:
                                return 0x0004;
                        
                        case 5:
                                return 0x0005;
                                        
                        case 6:
                                return 0x0006;
                        
                        case 7:
                                return 0x000d;

                        case 8:
                                return 0x0007;

                        case 9:
                                return 0x0008;

                        case 10:
                                return 0x0009;

                        case 11:
                                return 0x000e;

                        case 12:
                                return 0x000a;

                        case 13:
                                return 0x0000;

                        case 14:
                                return 0x000b;

                        case 15:
                                return 0x000f;

                        
                        
                        }
                        
                        
                        return 100;
                }
                
        
        

}
