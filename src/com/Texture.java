package com;

import javax.microedition.lcdui.Image;
import javax.microedition.m3g.Image2D;
import javax.microedition.m3g.Texture2D;

public class Texture {

	int w;
	int h;
	Texture2D tex;

	Texture() {
	}

	private Texture(Image img) {
		Image2D img2d = new Image2D(Image2D.RGBA, img);
		
		tex = new Texture2D(img2d);
		tex.setBlending(Texture2D.FUNC_MODULATE);
		tex.setWrapping(Texture2D.WRAP_CLAMP, Texture2D.WRAP_CLAMP);
		tex.setFiltering(Texture2D.FILTER_BASE_LEVEL, Texture2D.FILTER_NEAREST);
		
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
