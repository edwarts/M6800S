package com.m6800.cpu;
import java.io.*;

/**
 * Projet de langage JAVA IFITEP 3 2005 - Simulateur 8051 
 * <p> 
 * Exception de type erreur de CRC dans le fichier source binaire.
 *  
 * @author Matthieu SIMON
 * @version 1.0 du 17/06/05
 */
class HEXloaderException extends Exception {
	private String file;
	private int nb;
	/**
	 * Crée une exception.
	 * @param file Le nombre du fichier binaire.
	 * @param nb Le numéro de la ligne o?il y a eu l'erreur.
	 */
	public HEXloaderException(String file, int nb) {
		this.file = file;
		this.nb = nb;
	}
	public String toString() {
		return "Invalid CRC at " + file + " # line " + (nb+1);
	}
}

/**
 * Projet de langage JAVA IFITEP 3 2005 - Simulateur 8051 
 * <p> 
 * Chargeur de code hexadecimal nécessaire pour l'instruction MOVC.
 * Il charge en mémoire ROM le code binaire extrait d'un fichier source au format
 * Intel IHX 16 bits, en verifiant le CRC de chaque banque.
 * 
 * (Cf. http://shop-pdp.kent.edu/ashtml/asls01.htm#IntelIHX pour plus d'informations
 * sur le format Intel IHX 16 bits.)
 *  
 * @author Matthieu SIMON
 * @version 1.0 du 17/06/05
 */
public class HEXloader {
	private BufferedReader fileReader = null;
	/**
	 * Charge le fichier source binaire dans la ROM du microcontroleur (CORE_CPU8051.ROMemory).
	 * @param file Le fichier source hexadecimal.
	 * @throws IOException
	 * @throws HEXloaderException
	 */
	public HEXloader(String file) throws IOException, HEXloaderException {
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