package com;

import javax.microedition.m3g.Image2D;
import javax.microedition.m3g.Loader;

public class Texture {

	Image2D img;
	int w, h;

	private Texture(Image2D img) {
		this.img = img;
		
		this.w = img.getWidth();
		this.h = img.getHeight();
	}

	public void destroy() {}

	public static Texture createTexture(String file) {
		try {
			if(file.toLowerCase().endsWith(".bmp")) {
				return new Texture(BMPLoader.loadBMP(file));
			} else {
				return new Texture((Image2D) Loader.load(file)[0]);
			}
		} catch(Exception ex) {
			System.err.println("ERROR in createTexture " + file + ": " + ex);
			ex.printStackTrace();
			return null;
		}
	}
}
