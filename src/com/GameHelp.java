package com;

import home.Main;
import javax.microedition.lcdui.Graphics;

final class GameHelp extends GUIScreen {

	private int y0; // y точки из которой перетаскивается текст
	private Main main;
	private Menu menu;
	private Text text;
	private String levelFile;
	private int levelNumber;
	private Object hudInfo;

	public GameHelp(Main main, Menu menu, String levelFile, int levelNumber, Object hudInfo) {
		this.main = main;
		this.menu = menu;
		this.levelFile = levelFile;
		this.levelNumber = levelNumber;
		this.hudInfo = hudInfo;
		this.setFont(main.getFont());
		this.setLeftSoft(main.getGameText$6783a6a7().getString("START_GAME"));
		int var7 = this.getWidth() / 15;
		int var8 = this.getHeight() / 13;
		levelNumber = this.getWidth() - var7 * 2;
		int var9 = this.getHeight() - var8 * 2;
		String var6 = main.getGameText$6783a6a7().getString("GAME_HELP_TEXT").replace('*', '\n');
		this.text = new Text(var6, var7, var8, levelNumber, var9, main.getFont());
		this.text.setCentreAlign(true);
	}

	protected final void paint(Graphics g) {
		this.menu.drawBackground(g);
		this.text.draw(g);
		this.drawSoftKeys(g);
	}

	protected final void onLeftSoftKey() {
		this.onKey5();
	}

	protected final void onKey5() {
		try {
			this.menu.destroy();
			this.destroy();
			System.gc();
			Thread.sleep(5L);
			GameScreen var1;
			(var1 = new GameScreen(this.main, this.levelFile, this.levelNumber, this.hudInfo)).start();
			this.main.setCurrent(var1);
		} catch(Exception var2) {
			var2.printStackTrace();
		}
	}

	protected final void onKey2() {
		this.text.move(this.main.getFont().height());
		this.repaint();
	}

	protected final void onKey8() {
		this.text.move(-this.main.getFont().height());
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
