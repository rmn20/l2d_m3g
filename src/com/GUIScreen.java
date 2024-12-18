package com;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

public abstract class GUIScreen extends Canvas {

	private String leftSoft; // название (подпись) левой софт-клавиши
	private String rightSoft; // название правой софт-клавиши
	private Font font;
	private final HouseCreator keys = new HouseCreator(this);
	private int x = 0; // x точки нажатия на экран
	private int y = 0; // y точки нажатия на экран

	public GUIScreen() {
		this.setFullScreenMode(true);
	}

	public void destroy() {
		this.leftSoft = this.rightSoft = null;
		this.font = null;
	}

	protected final void set(Font font, String leftSoft, String rightSoft) {
		this.font = font;
		this.setSoftKeysNames(leftSoft, rightSoft);
	}

	protected final void setFont(Font font) {
		this.font = font;
	}

	protected final Font getFont() {
		return this.font;
	}

	// задает названия левой и правой софт клавиш
	protected final void setSoftKeysNames(String leftSoft, String rightSoft) {
		this.leftSoft = leftSoft;
		this.rightSoft = rightSoft;
	}

	protected final void setLeftSoft(String name) {
		this.leftSoft = name;
	}

	protected final void drawSoftKeys(Graphics g) {
		int var2 = this.getWidth();
		int var3 = this.getHeight();
		if(this.font == null) {
			System.out.println("GUIScreen: ERROR: font == null");
		} else {
			this.font.setY(0);
			if(this.leftSoft != null) {
				this.font.drawString(g, this.leftSoft, 2, var3, 36);
			}

			if(this.rightSoft != null) {
				this.font.drawString(g, this.rightSoft, var2 - 2, var3, 40);
			}

		}
	}

	protected final void keyPressed(int key) {
		if(key == this.keys.code7) {
			this.onLeftSoftKey();
		}

		if(key == this.keys.code9) {
			this.onRightSoftKey();
		}

		if(key == 53 || key == this.keys.codeCentre) {
			this.onKey5();
		}

		if(key == 52 || key == this.keys.codeLeft) {
			this.onKey4();
		}

		if(key == 54 || key == this.keys.codeRight) {
			this.onKey6();
		}

		if(key == 50 || key == this.keys.codeUp) {
			this.onKey2();
		}

		if(key == 56 || key == this.keys.codeDown) {
			this.onKey8();
		}

	}

	// Действие при нажатии левой софт-клавиши
	protected void onLeftSoftKey() {
	}

	// Действие при нажатии правой софт-клавиши
	protected void onRightSoftKey() {
	}

	// Действие при нажатии 5
	protected void onKey5() {
	}

	// при нажатии 4
	protected void onKey4() {
	}

	// при нажатии 6
	protected void onKey6() {
	}

	// при нажатии 2
	protected void onKey2() {
	}

	// при нажатии 8
	protected void onKey8() {
	}

	protected void pointerPressed(int x, int y) {
		this.x = x;
		this.y = y;
		if(isLeftSoft(x, y, this.getWidth(), this.getHeight())) {
			this.keyPressed(this.keys.code7);
		}

		if(isRightSoft(x, y, this.getWidth(), this.getHeight())) {
			this.keyPressed(this.keys.code9);
		}

	}

	protected void pointerDragged(int x, int y) {
		int var3 = this.getWidth();
		int var4 = this.getHeight();
		if(Math.abs(this.x - x) > var3 / 7) {
			if(this.x < x) {
				this.onKey6();
			}

			if(this.x > x) {
				this.onKey4();
			}

			this.x = x;
		}

		if(Math.abs(this.y - y) > var4 / 7) {
			if(this.y > y) {
				this.onKey2();
			}

			if(this.y < y) {
				this.onKey8();
			}

			this.y = y;
		}

	}

	public static boolean isLeftSoft(int x, int y, int w, int h) {
		int var4 = h / 10;
		return inArea(x, y, 0, h - var4, w / 2 - w / 6, h);
	}

	public static boolean isRightSoft(int x, int y, int w, int h) {
		int var4 = h / 10;
		return inArea(x, y, w / 2 + w / 6, h - var4, w, h);
	}

	private static boolean inArea(int x, int y, int x1, int y1, int x2, int y2) {
		return x >= x1 && x <= x2 && y >= y1 && y <= y2;
	}
}
