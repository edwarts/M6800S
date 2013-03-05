package com.m6800.dream6800;

import java.io.IOException;
import java.util.Stack;

import com.m6800.cpu.M6800CPU;
import com.m6800.cpu.ROMHEXLoader;
import com.m6800.ram.IRAM;
import com.m6800.ram.M6800RAM;

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
public class Dream6800 {
	public static IRAM ram;
	public static M6800CPU cpu;
    
	/**
	 * @param args
	 * @throws com.m6800.cpu.HEXloaderException 
	 */
	public static void main(String[] args) throws HEXloaderException, com.m6800.dream6800.HEXloaderException {
		
		//init Ram
		System.out.println("Initialize the ROM");
		IRAM m68001kIram=new M6800RAM();
		m68001kIram.init(1024, 0x0200);
		//init cpu
        ram=m68001kIram;		
		//load rom
		
		ROMHEXLoader romhexLoader;
		try {
			romhexLoader = new ROMHEXLoader("chipos.txt");
			// sub status
			System.out.println("Initialize the ROM");
			short[] rom=romhexLoader.getRom();
			cpu=new M6800CPU(ram,rom);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		short k=0x7C00;
		System.out.println(k);
		cpu.setPC((short) 49152);
		while(true)
		{
			
			cpu.runcycle();
		}
		

	}

}