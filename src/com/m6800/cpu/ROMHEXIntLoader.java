package com.m6800.cpu;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;




public class ROMHEXIntLoader {
	private BufferedReader fileReader = null;
	/**
	 * Charge le fichier source binaire dans la ROM du microcontroleur (CORE_CPU8051.ROMemory).
	 * @param file Le fichier source hexadecimal.
	 * @throws IOException
	 * @throws HEXloaderException
	 */
	public int[] rom=new int[0xc3fe];
	public ROMHEXIntLoader(String file) throws IOException{
		fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		int nbLines = 0;
		
		//short b=Short.decode("0x8D");
		String str;
		int k=0xC000;
		while((str = fileReader.readLine()) != null) { 
			int addresIndex=str.indexOf("#");
			
			str=str.substring(addresIndex+1, str.length());
			System.out.println(str);
			int first=str.indexOf(" ");
			while(first!=-1)
			{
				if(!str.substring(0,first).equals(""))
				{
				int firstOP=Integer.decode("0x"+str.substring(0,first)).shortValue();
				str=str.substring(first+1);
				//System.out.println(firstOP);
				rom[k]=firstOP;
				k++;
				first=str.indexOf(" ");
				}
				else
				{
					int firstOP=Integer.decode("0x"+str).shortValue();
					str=str.substring(first);
					//System.out.println(firstOP);
					rom[k]=firstOP;
					k++;
					first=str.indexOf(" ");
				}
				
			}
			
			//get opt
			
		}
		fileReader.close();
	/*	for(Short oneOp:rom)
		{
			System.out.println(Integer.toHexString(oneOp));
		}*/
		
	}
	public int[] getRom() {
		return rom;
	}
	public void setRom(int[] rom) {
		this.rom = rom;
	}

}
