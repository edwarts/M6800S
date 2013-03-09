package com.m6800.cpu;

import java.io.IOException;

public class testHexloader {

	/**
	 * @param args
	 * @throws HEXloaderException 
	 * @throws IOException 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//try {
			//HEXloader che=new HEXloader("C:\\Dream6800Rom.hex");
			//byte k=(byte) 0x8D;
			//System.out.println(Integer.toHexString((int)k));
			//byte y=(byte) (k>>4);
			//System.out.println(y);
			/*byte s=(byte) 0x8c;
			byte s2=(byte)0xd3;
			short k=s;
			//k=(short) (0x00ff&k);
			k=(short) (k<<8);

			k=(short)(k+(s2&0x00ff));
			System.out.println(Integer.toHexString(k));
			//short firstOP=Short.decode("0xCl").shortValue();
			
			try {
				ROMHEXLoader romhex=new ROMHEXLoader("chipos.txt");
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
		//11101101
		/*  double tmpa=1*Math.pow(2d, 7d)+1*Math.pow(2d, 6d)+1*Math.pow(2d, 5d)+0*Math.pow(2d, 4d)+1*Math.pow(2d, 3d)+1*Math.pow(2d, 2d)+0*Math.pow(2d, 1d)+1*1;
		    System.out.println((int)tmpa);
		    int kk=((int)tmpa);
		    System.out.println(Byte.MAX_VALUE);
		    System.out.println(Integer.MAX_VALUE);
		    System.out.println();
			System.out.println(Byte.valueOf((byte)tmpa));*/
			  int[] conData = new int[2];
			  int SP=0x1D6A;
			  //transfer one into two
			  conData[0] =  SP&0x00ff;//low 8
			  System.out.println(Integer.toHexString(conData[0]));
			  conData[1] = (int) (SP &0xff00);//high 8
			//System.out.println(getbit(s,8));
			  
			  //System.out.println(Integer.toHexString(conData[1]>>8));
			  //combine two together.
			  String tmp=Integer.toHexString(conData[1]>>8)+Integer.toHexString(conData[0]);
			  int k=Integer.decode("0x"+tmp);
			  //set the highest bit
			  byte bitnum=(byte) (k>>1&0x01);
			  //last bit]
			  System.out.println("Last bit:"+(0x8d&01));
			  System.out.println("Hex:"+Integer.toBinaryString(0x8d));
              System.out.println(Integer.toBinaryString(0x8d).length());
			  System.out.println("Last bit:"+(k&0x01));
			  System.out.println("bit num:"+bitnum);
			  //test the bitcount
			  //System.out.println("Bit count:"+Integer.bitCount(k));
			  //System.out.println(Integer.highestOneBit(k));
			  String kb=Integer.toBinaryString(k);
			  System.out.println(kb.length());
			  System.out.println(k);
			  System.out.println(Integer.toHexString(Integer.decode("0x"+tmp)));
			  System.out.println(kb);
		/*} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HEXloaderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		

	}
	 public static byte getbit(byte num, int bitnum)
	  {
	
	   /* if((num>>(8-bitnum)&0x01) != 0)
	    {
	    	return 1;
	    }
	    else
	    {
	    	return 0;
	    }*/
		return (byte)(num>>(8-bitnum)&0x01);
	  } 

}
