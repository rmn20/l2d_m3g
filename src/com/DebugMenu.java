package com;

import home.Main;
import javax.microedition.lcdui.Graphics;
import javax.microedition.media.Manager;

final class DebugMenu extends GUIScreen {

	private int y0; // y точки из которой перетскивается текст
	private Main main;
	private Menu menu;
	private Text text;

	public DebugMenu(Main main, Menu menu) {
		this.main = main;
		this.menu = menu;
		this.setFont(main.getFont());
		this.setSoftKeysNames((String) null, main.getGameText$6783a6a7().getString("BACK"));
		int var7 = this.getWidth() / 15;
		int var3 = this.getHeight() / 13;
		int var4 = this.getWidth() - var7 * 2;
		int var5 = this.getHeight() - var3 * 2;
		
		String text = "";
		String supportedTypes[] = Manager.getSupportedContentTypes(null);
		
		for(int i = 0; i < supportedTypes.length; i++) {
			if(supportedTypes[i].startsWith("audio")) {
				text += supportedTypes[i].toUpperCase() + "\n";
			}
		}

		this.text = new Text(text, var7, var3, var4, var5, main.getFont());
		this.text.setCentreAlign(true);
	}

	protected final void paint(Graphics g) {
		this.menu.drawBackground(g);
		this.text.draw(g);
		this.drawSoftKeys(g);
	}

	protected final void onRightSoftKey() {
		this.main.setCurrent(this.menu);
	}

	protected final void onKey2() {
		this.text.move(this.main.getFont().getHeight());
		this.repaint();
	}

	protected final void onKey8() {
		this.text.move(-this.main.getFont().getHeight());
		this.repaint();
	}

	protected final void pointerPressed(int x, int y) {
		super.pointerPressed(x, y);
		this.y0 = y;
	}

	protected final void pointerDragged(int x, int y) {
		x = y - this.y0;
		this.y0 = y;
		this.serviceRepaints();
		this.text.move(x);
		this.repaint();
	}
}
