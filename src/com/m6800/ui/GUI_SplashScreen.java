package com.m6800.ui;
import javax.swing.JWindow; 
import java.awt.*; 
import java.awt.image.*; 
import javax.imageio.*; 
import java.net.*; 
import java.io.*; 
import java.util.*;

/**
 * Projet de langage JAVA IFITEP 3 2005 - Simulateur 8051 
 * <p> 
 * GUI_SplashScreen
 *  
 * @author Matthieu SIMON
 * @version 1.0 du 28/06/05
 */
public class GUI_SplashScreen extends JWindow{
	private BufferedImage image; 
	public GUI_SplashScreen(String file, long time){
		super();
		
		URL url = getClass().getResource(file);
		try{
			image = ImageIO.read(url);
			setSize(new Dimension(image.getWidth(), image.getHeight()));
			setLocationRelativeTo(null);
			setVisible(true);
		}catch(IOException ioe){
			System.out.println(ioe.getMessage());
		}
		if(time>0){
			TimerTask dispose = new TimerTask(){
				public void run(){dispose();}	
			};
			Timer timer = new Timer();
			timer.schedule(dispose, time);
			try{
				Thread.sleep(time);
			}catch(Exception e){e.printStackTrace();}
		}
	}
	public GUI_SplashScreen(String file){
		this(file,0);	
	}
	public void paint(Graphics g){
		if(image.getColorModel().hasAlpha()){
			try{
				Robot robot = new Robot();
				BufferedImage fond = robot.createScreenCapture(getBounds());
				MediaTracker tracker = new MediaTracker(this);
				tracker.addImage(fond,0);
				tracker.waitForAll();
				g.drawImage(fond, 0,0,null);
			}catch(Exception e){e.printStackTrace();}
		}
		g.drawImage(image,0,0,null);
	}
}