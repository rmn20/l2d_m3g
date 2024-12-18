package com;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

public abstract class GUIScreen extends Canvas {

	private final Keyboard keys = new Keyboard(this);
	private Font font;
	private String leftSoft, rightSoft;
	
	private int prevPressX = 0;
	private int prevPressY = 0;

	public GUIScreen() {
		setFullScreenMode(true);
	}

	protected final void set(Font font, String leftSoft, String rightSoft) {
		this.font = font;
		setSoftKeysNames(leftSoft, rightSoft);
	}

	protected final void setSoftKeysNames(String leftSoft, String rightSoft) {
		this.leftSoft = leftSoft;
		this.rightSoft = rightSoft;
	}

	protected final void setLeftSoft(String name) {
		leftSoft = name;
	}

	public void destroy() {
		leftSoft = rightSoft = null;
		font = null;
	}

	protected final void setFont(Font font) {
		this.font = font;
	}

	protected final Font getFont() {
		return font;
	}

	protected final void drawSoftKeys(Graphics g) {
		int w = getWidth();
		int h = getHeight();
		
		if(font == null) {
			System.out.println("GUIScreen: ERROR: font == null");
		} else {
			font.setStyle(0);
			
			if(leftSoft != null) font.drawString(g, leftSoft, 2, h, 36);
			if(rightSoft != null) font.drawString(g, rightSoft, w - 2, h, 40);
		}
	}

	protected final void keyPressed(int key) {
		if(key == keys.KEY7) onLeftSoftKey();
		if(key == keys.KEY9) onRightSoftKey();

		if(key == Canvas.FIRE || key == keys.FIRE) onKey5();
		if(key == Canvas.LEFT || key == keys.LEFT) onKey4();

		if(key == Canvas.RIGHT || key == keys.RIGHT) onKey6();
		if(key == Canvas.UP || key == keys.UP) onKey2();

		if(key == Canvas.DOWN || key == keys.DOWN) onKey8();
	}

	protected void onLeftSoftKey() {}
	protected void onRightSoftKey() {}
	protected void onKey5() {}
	protected void onKey4() {}
	protected void onKey6() {}
	protected void onKey2() {}
	protected void onKey8() {}

	protected void pointerPressed(int x, int y) {
		prevPressX = x;
		prevPressY = y;
		
		if(isLeftSoft(x, y, getWidth(), getHeight())) keyPressed(keys.KEY7);
		if(isRightSoft(x, y, getWidth(), getHeight())) keyPressed(keys.KEY9);
	}

	protected void pointerDragged(int x, int y) {
		int w = getWidth();
		int h = getHeight();
		
		if(Math.abs(prevPressX - x) > w / 7) {
			if(prevPressX < x) onKey6();
			if(prevPressX > x) onKey4();

			prevPressX = x;
		}

		if(Math.abs(prevPressY - y) > h / 7) {
			if(prevPressY > y) onKey2();
			if(prevPressY < y) onKey8();

			prevPressY = y;
		}
	}

	public static boolean isLeftSoft(int x, int y, int w, int h) {
		return inArea(x, y, 0, h * 9 / 10, w / 2 - w / 6, h);
	}

	public static boolean isRightSoft(int x, int y, int w, int h) {
		return inArea(x, y, w / 2 + w / 6, h * 9 / 10, w, h);
	}

	private static boolean inArea(int x, int y, int x1, int y1, int x2, int y2) {
		return x >= x1 && x <= x2 && y >= y1 && y <= y2;
	}
}
