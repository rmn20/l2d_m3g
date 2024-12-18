package com;

import javax.microedition.lcdui.Graphics;

public class Selectable extends GUIScreen {

	private ItemList list;

	public Selectable() {
		this.setFullScreenMode(true);
	}

	public void destroy() {
		super.destroy();
		this.list = null;
	}

	protected final void set(Font font, String[] items, String leftSoft, String rightSoft) {
		this.list = new ItemList(items);
		this.list.setFont(font);
		this.setFont(font);
		this.setSoftKeysNames(leftSoft, rightSoft);
	}

	protected void paint(Graphics g) {
		this.list.draw(g, 0, 0, this.getWidth(), this.getHeight());
		this.drawSoftKeys(g);
	}

	protected final void onKey2() {
		this.list.scrollUp();
		this.repaint();
	}

	protected final void onKey8() {
		this.list.scrollDown();
		this.repaint();
	}

	// Номер выбранного пункта меню
	public final int itemIndex() {
		return this.list.getIndex();
	}

	public final String[] getItems() {
		return this.list.getItems();
	}
}
