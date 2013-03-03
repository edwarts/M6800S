package com.m6800.cpu;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

public class ROMHEXLoader {
	private BufferedReader fileReader = null;
	/**
	 * Charge le fichier source binaire dans la ROM du microcontroleur (CORE_CPU8051.ROMemory).
	 * @param file Le fichier source hexadecimal.
	 * @throws IOException
	 * @throws HEXloaderException
	 */
	Hashtable(String,short)
	public ROMHEXLoader(String file) throws IOException, HEXloaderException {
		fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		int nbLines = 0;
		String str;
		while((str = fileReader.readLine()) != null) { 
			int addresIndex=str.indexOf("*");
			str=str.substring(addresIndex, str.length()-addresIndex);
			//get opt
			
		}
		fileReader.close();
	}
	public Hashtable<String,>

}
