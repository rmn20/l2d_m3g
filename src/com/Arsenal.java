package com;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Arsenal {

   private Weapon[] weapons; // Всё купленное оружие
   private int current;  // Номер выбранного оружия


   public Arsenal(int width_g3d, int height_g3d) {
      this.current = 0;
      this.weapons = new Weapon[5];
      this.weapons[0] = Stringer.createWeapon(0);

      for(int var3 = 0; var3 < this.weapons.length; ++var3) {
         if(this.weapons[var3] != null) {
            this.weapons[var3].reset();
            if(!this.weapons[var3].isTwoHands()) {
               this.weapons[var3].setAmmo(200);
            } else {
               this.weapons[var3].setAmmo(400);
            }
         }
      }

      this.current = 0;
      this.currentWeapon().createSprite(width_g3d, height_g3d);
   }

   public final void destroy() {
      for(int var1 = 0; var1 < this.weapons.length; ++var1) {
         if(this.weapons[var1] != null) {
            this.weapons[var1].reset();
            this.weapons[var1] = null;
         }
      }

      this.weapons = null;
   }

   public final Weapon currentWeapon() {
      return this.weapons[this.current];
   }

   public final Weapon[] getWeapons() {
      return this.weapons;
   }

    // Смена оружия
   public final void next(int width, int height) {
      while(true) {
         if(this.currentWeapon() != null) {
            this.currentWeapon().reset();
         }

         ++this.current;
         this.current %= this.weapons.length;
         if(this.currentWeapon() != null) {
            this.currentWeapon().createSprite(width, height);
            return;
         }

         //var1 = var1;
         //this = this;
      }
   }

   // ? Прорисовка оружия и полоски перезарядки
   public final void drawWeapon(Graphics g, int y, int width, int height) {
      Weapon var5;
      (var5 = this.currentWeapon()).draw(g, 0, y, width, height);
      if(var5.isReloading()) {
         width /= 2;
         int var6 = Math.max(height / 50, 6);
         int var10001 = width - width / 2;
         int var10002 = height - var6 - 2 + y;
         int var10004 = var6;
         var6 = var5.reloadingPercentage();
         int var7 = var10004;
         height = width;
         width = var10002;
         y = var10001;
         g.setColor(16777215);
         g.drawRect(y, width, height, var7);
         g.fillRect(y, width, height * var6 / 100, var7);
      }

   }

   public Arsenal() {}

   // ?
   public static Image resize(Image img, float scaleW, float scaleH) {
      return resize(img, (int)((float)img.getWidth() * scaleW), (int)((float)img.getHeight() * scaleH));
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
         } catch (ArrayIndexOutOfBoundsException var20) {
            var20.printStackTrace();
         }

         return Image.createRGBImage(var4, new_width, new_height, true);
      }
   }
}
