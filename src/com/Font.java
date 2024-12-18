package com;

import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class Font {

	private Image img;
	
	private int linesInImg; //Font styles in img
	private int activeLine = 0; //Active font style
	
	private int spaceWidth;
	
	private char[] chars;
	private int[] charsX;

	public Font(String file) {
		try {
			IniFile ini = IniFile.createFromResource(file);
			
			img = Image.createImage(ini.getString("IMG"));
			linesInImg = ini.getInt("LINES");
			spaceWidth = ini.getInt("SPACE");
			chars = ini.getString("CHARS").toCharArray();
			charsX = MeshData.cutOnInts(ini.getString("COORDS"), ',');
		} catch(IOException ex) {
			ex.printStackTrace();
		}
	}

	public final void setStyle(int line) {
		if(linesInImg >= 0) {
			activeLine = line;
		}
	}

	private int indexOf(char ch) {
		for(int i = 0; i < chars.length; i++) {
			if(chars[i] == ch) return i;
		}

		return -1;
	}

	public final void drawString(Graphics g, String str, int x, int y, int anchor) {
		int renderX = x;
		int renderY = y;
		
		if((anchor & Graphics.RIGHT) != 0) renderX = x - stringWidth(str);
		if((anchor & Graphics.BOTTOM) != 0) renderY = y - getHeight();
		if((anchor & Graphics.HCENTER) != 0) renderX -= stringWidth(str) >> 1;
		if((anchor & Graphics.VCENTER) != 0) renderY -= getHeight() >> 1;
		if((anchor & Graphics.BASELINE) != 0) renderY -= getHeight() + 1;

		int imgLineH = img.getHeight() / (linesInImg + 1);
		int imgLineY = activeLine * imgLineH;
		
		int strLen = str.length();

		for(int i = 0; i < strLen; i++) {
			char ch = str.charAt(i);
			
			if(ch == ' ') {
				renderX += spaceWidth;
			} else {
				int charId = indexOf(ch);
				
				if(charId == -1) {
					g.setColor(-1);
					g.drawRect(renderX, renderY, spaceWidth, imgLineH);
					renderX += spaceWidth;
				} else {
					int imgCharX = charsX[charId];
					int imgCharW = charsX[charId + 1] - imgCharX;
					
					g.drawRegion(img, imgCharX, imgLineY, imgCharW, imgLineH, 0, renderX, renderY, 0);
					
					renderX += imgCharW;
				}
			}
		}

	}

	public final int charWidth(char ch) {
		if(ch == ' ') {
			return spaceWidth;
		} else {
			int charId = indexOf(ch);
			
			if(charId == -1) {
				return spaceWidth;
			} else {
				return charsX[charId + 1] - charsX[charId];
			}
		}
	}

	public final int getHeight() {
		return img.getHeight() / (linesInImg + 1);
	}

	public final int stringWidth(String str) {
		int strLen = str.length();
		int w = 0;

		for(int i = 0; i < strLen; i++) {
			w += charWidth(str.charAt(i));
		}

		return w;
	}
}
