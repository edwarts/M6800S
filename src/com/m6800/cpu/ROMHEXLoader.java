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
	public ROMHEXLoader(String file) throws IOException, HEXloaderException {
		fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		int nbLines = 0;
		String str;
		while((str = fileReader.readLine()) != null) {              // pour chaque ligne du fichier
			str = str.substring(1);                                 // enleve le ':' au debut de la ligne
			int len = Integer.parseInt(str.substring(0, 2), 16);    // taille de la zone de donnée (la banque)
			int add = Integer.parseInt(str.substring(2, 6), 16);    // adresse de la banque
			String data = str.substring(8, 8+2*len);			    // recupere les données
			int crc = Integer.parseInt(str.substring(8+2*len), 16); // recupere le CRC
			
			// Calcul du CRC
			int dcrc = 0;
			for(int i = 0; i < (str.length()-2)/2; i++) // addition succesive 
				dcrc += (byte)Integer.parseInt(str.substring(i*2, i*2+2), 16);
			
			if((((~dcrc)+1)&0xFF) != crc) { // complement a 2 
				fileReader.close();
				throw new HEXloaderException(file, nbLines);
			}
			System.out.println(data);
			/*
			for(int i = 0; i < len; i++) // load les données
				//CORE_CPU8051.ROMemory[add+i] = Integer.parseInt(data.substring(i*2, i*2+2), 16); comment out of the cpu loader for testing.
				*/
			
			nbLines++;
		}
		fileReader.close();
	}

}
