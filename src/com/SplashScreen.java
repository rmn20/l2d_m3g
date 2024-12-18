package com;

import home.Main;
import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class SplashScreen extends GUIScreen {

	private Main main;
	private int action;
	private Image splash;
	private Image background;
	private ItemList list;

	public SplashScreen(Main var1) {
		this.main = var1;
		this.setFont(var1.getFont());
		this.onAction(1);
	}

	public final void destroy() {
		super.destroy();
		this.splash = this.background = null;
	}

	private void onAction(int action) {
		this.action = action;
		if(action == 1) {
			try {
				this.splash = Arsenal.resize(Image.createImage("/splash.png"), (float) this.getWidth() / 240.0F, (float) this.getHeight() / 320.0F);
			} catch(IOException var3) {
				var3.printStackTrace();
			}

			(new LanguageScreen(this)).start();
		} else {
			String[] var4;
			if(action == 2) {
				this.splash = null;

				try {
					this.background = Arsenal.resize(Image.createImage("/background.png"), this.getWidth(), this.getHeight());
				} catch(IOException var2) {
					var2.printStackTrace();
				}

				var4 = Mesh.cutOnStrings(Mesh.getStringFromResource("/languages.txt"), ',');
				this.list = new ItemList(var4, this.main.getFont());
				this.setSoftKeysNames(this.main.getGameText$6783a6a7().getString("SELECT"), (String) null);
			} else if(action == 3) {
				var4 = new String[]{this.main.getGameText$6783a6a7().getString("SOUND_ON"), this.main.getGameText$6783a6a7().getString("SOUND_OFF")};
				this.list = new ItemList(var4, this.main.getFont());
				this.setSoftKeysNames(this.main.getGameText$6783a6a7().getString("SELECT"), (String) null);
			}
		}

		this.repaint();
	}

	protected final void paint(Graphics g) {
		if(this.action == 1) {
			g.setColor(16777215);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			g.drawImage(this.splash, this.getWidth() / 2, this.getHeight() / 2, 3);
		} else if(this.action == 2) {
			g.drawImage(this.background, 0, 0, 0);
			this.list.draw(g, 0, 0, this.getWidth(), this.getHeight());
		} else if(this.action == 3) {
			g.drawImage(this.background, 0, 0, 0);
			this.list.draw(g, 0, 0, this.getWidth(), this.getHeight());
		}

		this.drawSoftKeys(g);
	}

	protected final void onKey2() {
		if(this.action == 2 || this.action == 3) {
			this.list.scrollUp();
			this.repaint();
		}

	}

	protected final void onKey8() {
		if(this.action == 2 || this.action == 3) {
			this.list.scrollDown();
			this.repaint();
		}

	}

	protected final void onKey5() {
		if(this.action == 2) {
			String var1 = "/" + this.list.getCurrentItem().toLowerCase() + ".txt";
			this.main.setLanguage(var1);
			this.setFont(this.main.getFont());
			this.onAction(3);
		} else {
			if(this.action == 3) {
				this.main.setSound(this.list.getIndex() == 0);
				this.destroy();
				this.main.setCurrent(new Menu(this.main));
			}

		}
	}

	protected final void onLeftSoftKey() {
		this.onKey5();
	}

	static void langSelection(SplashScreen s, int action) {
		s.onAction(2);
	}
}
