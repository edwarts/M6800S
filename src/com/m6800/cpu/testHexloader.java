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
			byte s=(byte) 0x8c;
			byte s2=(byte)0xd3;
			short k=s;
			//k=(short) (0x00ff&k);
			k=(short) (k<<8);

			k=(short)(k+(s2&0x00ff));
			System.out.println(Integer.toHexString(k));
			//System.out.println(getbit(s,8));
			
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
