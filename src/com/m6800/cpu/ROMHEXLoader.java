package com.m6800.cpu;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class ROMHEXLoader {
	private BufferedReader fileReader = null;
	/**
	 * Charge le fichier source binaire dans la ROM du microcontroleur (CORE_CPU8051.ROMemory).
	 * @param file Le fichier source hexadecimal.
	 * @throws IOException
	 * @throws HEXloaderException
	 */
	short[] rom=new short[0xc3fe];
	public ROMHEXLoader(String file) throws IOException, HEXloaderException {
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
				short firstOP=Short.decode("0x"+str.substring(0,first)).shortValue();
				str=str.substring(first+1);
				System.out.println(firstOP);
				rom[k]=firstOP;
				k++;
				first=str.indexOf(" ");
				}
				else
				{
					short firstOP=Short.decode("0x"+str).shortValue();
					str=str.substring(first);
					System.out.println(firstOP);
					rom[k]=firstOP;
					k++;
				}
				
			}
			
			//get opt
			
		}
		fileReader.close();
	}

}
