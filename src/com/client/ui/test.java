package com.client.ui;

import java.awt.Font;
import java.awt.GraphicsEnvironment;

public class test {

	public static void main(String args[]){
		
		GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
		Font[] fonts = e.getAllFonts();
		
		for(Font f : fonts){
			System.out.println(f.getFontName());
			
			
		}
			
			
			
	}
	
	
}
