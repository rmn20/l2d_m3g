package com;

import javax.microedition.lcdui.Image;
import javax.microedition.m3g.Image2D;

public class Texture {

	int w;
	int h;
	Image2D img;

	Texture() {
	}

	private Texture(Image imgPath) {
		this.img = new Image2D(Image2D.RGBA, imgPath);
		
		this.w = img.getWidth();
		this.h = img.getHeight();
	}

	public void destroy() {
	}

	public static Texture createTexture(String file) {
		try {
			return new Texture(Image.createImage(file));
		} catch(Exception ex) {
			System.err.println("ERROR in createTexture " + file + ": " + ex);
			ex.printStackTrace();
			return null;
		}
	}
}
