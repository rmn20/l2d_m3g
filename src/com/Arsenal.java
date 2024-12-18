package com;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Arsenal {

	private Weapon[] weapons; // Всё купленное оружие
	private int current;  // Номер выбранного оружия

	public static Weapon createWeapon(int index) {
		return index == 0 ? new Weapon("/pistol.png", "/fire.png", 1.0F, 1.0F, 20, 4, 2, false, 12, 10)
				: (index == 1 ? new Weapon("/pistol.png", "/fire.png", 1.0F, 1.0F, 40, 4, 2, true, 24, 15)
				: (index == 2 ? new Weapon("/shotgun.png", "/fire.png", 0.755F, 1.0F, 90, 5, 2, false, 6, 15)
				: (index == 3 ? new Weapon("/m16.png", "/fire.png", 0.82F, 0.9F, 35, 2, 2, false, 30, 10)
				: (index == 4 ? new Weapon("/uzi.png", "/fire.png", 0.55F, 0.9F, 20, 1, 1, false, 30, 10) : null))));
	}

	public Arsenal(int scrW, int scrH) {
		current = 0;
		weapons = new Weapon[5];
		weapons[0] = createWeapon(0);

		for(int i = 0; i < weapons.length; i++) {
			if(weapons[i] == null) continue;

			weapons[i].reset();
			weapons[i].setAmmo(weapons[i].isTwoHands() ? 400 : 200);
		}

		current = 0;
		weapons[0].createSprite(scrW, scrH);
	}

	public final void destroy() {
		for(int i = 0; i < weapons.length; i++) {
			if(weapons[i] != null) {
				weapons[i].reset();
				weapons[i] = null;
			}
		}

		weapons = null;
	}

	public final Weapon currentWeapon() {
		return weapons[current];
	}

	public final Weapon[] getWeapons() {
		return weapons;
	}

	// Смена оружия
	public final void nextWeapon(int scrW, int scrH) {
		while(true) {
			if(currentWeapon() != null) {
				currentWeapon().reset();
			}

			current++;
			current %= weapons.length;
			
			if(currentWeapon() != null) {
				currentWeapon().createSprite(scrW, scrH);
				break;
			}
		}
	}

	// ? Прорисовка оружия и полоски перезарядки
	public final void drawWeapon(Graphics g, int y, int w, int h) {
		Weapon weapon = currentWeapon();
		weapon.draw(g, 0, y, w, h);

		if(weapon.isReloading()) {
			int barW = w / 2;
			int barH = Math.max(h / 50, 6);
			
			int barX = (w - barW) / 2;
			int barY = h - barH - 2 + y;
			
			int percentage = weapon.reloadingPercentage();
			
			g.setColor(0xffffff);
			g.drawRect(barX, barY, barW, barH);
			g.fillRect(barX, barY, barW * percentage / 100, barH);
		}
	}

	// ?
	public static Image resize(Image img, float scaleW, float scaleH) {
		return resize(img, (int) (img.getWidth() * scaleW), (int) (img.getHeight() * scaleH));
	}

	// ?
	public static Image resize(Image img, int new_width, int new_height) {
		if(img.getWidth() == new_width && img.getHeight() == new_height) {
			return img;
		} else {
			int[] var3 = new int[img.getWidth() * img.getHeight()];
			img.getRGB(var3, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());
			int[] var4 = new int[new_width * new_height];
			int[] var10000 = var3;
			int var10002 = img.getWidth();
			int var10003 = img.getHeight();
			boolean var21 = true;
			int var8 = new_height;
			int var7 = new_width;
			int var6 = var10003;
			int var5 = var10002;
			var3 = var4;
			int[] var22 = var10000;

			try {
				for(int var9 = 0; var9 < var7; ++var9) {
					for(int var10 = 0; var10 < var8; ++var10) {
						int var11 = (var9 << 16) / var7 * var5;
						int var12 = (var10 << 16) / var8 * var6;
						int var13 = var11 >>> 16;
						int var14 = var12 >>> 16;
						var11 &= 0xffff;
						var12 &= 0xffff;
						int var17;
						int var16;
						int var18;
						int var15 = var16 = var17 = var18 = var13 + var14 * var5;
						if(var13 < var5 - 1) {
							++var16;
							++var18;
						}

						if(var14 < var6 - 1) {
							var17 += var5;
							var18 += var5;
						}

						var13 = ((var22[var15] >>> 24) * (0xffff - var11) + (var22[var16] >>> 24) * var11 >>> 16) * (0xffff - var12) + ((var22[var17] >>> 24) * (0xffff - var11) + (var22[var18] >>> 24) * var11 >>> 16) * var12 >>> 16;
						var14 = ((var22[var15] >> 16 & 255) * (0xffff - var11) + (var22[var16] >> 16 & 255) * var11 >>> 16) * (0xffff - var12) + ((var22[var17] >> 16 & 255) * (0xffff - var11) + (var22[var18] >> 16 & 255) * var11 >>> 16) * var12 >>> 16;
						int var19 = ((var22[var15] >> 8 & 255) * (0xffff - var11) + (var22[var16] >> 8 & 255) * var11 >>> 16) * (0xffff - var12) + ((var22[var17] >> 8 & 255) * (0xffff - var11) + (var22[var18] >> 8 & 255) * var11 >>> 16) * var12 >>> 16;
						var11 = ((var22[var15] & 255) * (0xffff - var11) + (var22[var16] & 255) * var11 >>> 16) * (0xffff - var12) + ((var22[var17] & 255) * (0xffff - var11) + (var22[var18] & 255) * var11 >>> 16) * var12 >>> 16;
						var3[var9 + var10 * var7] = var13 << 24 | var14 << 16 | var19 << 8 | var11;
					}
				}
			} catch(ArrayIndexOutOfBoundsException var20) {
				var20.printStackTrace();
			}

			return Image.createRGBImage(var4, new_width, new_height, true);
		}
	}
}
