package com;

import home.Main;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

final class Shop extends GUIScreen {

   private final String[] files = new String[]{"/icon_pistol.png", "/icon_pistol2.png", "/icon_shotgun.png", "/icon_m16.png", "/icon_uzi.png", "/icon_medicine_chest.png"};
   private final int[] prices;
   private Main main;
   private GameScreen gameScreen;
   private Player player;
   private int index = 0; // Текущий номер товара
   private Image iconItem;
   private Image iconPatron;


   public Shop(Main main, GameScreen gameScreen, Player player) {
      this.main = main;
      this.gameScreen = gameScreen;
      this.player = player;
      Stringer var5 = main.getGameText$6783a6a7();
      this.set(main.getFont(), var5.getString("BUY"), var5.getString("BACK"));
      Stringer var4 = Stringer.createFromResource("/setting.txt");
      this.prices = new int[]{var4.getInt("PRICE_PISTOL"), var4.getInt("PRICE_TWO_PISTOL"), var4.getInt("PRICE_SHOTGUN"), var4.getInt("PRICE_M16"), var4.getInt("PRICE_UZI"), var4.getInt("PRICE_MEDICINE_CHEST")};
      this.iconPatron = this.createImage("/icon_patron.png");
      this.reset();
   }

   public final void destroy() {
      super.destroy();
      this.iconItem = this.iconPatron = null;
      this.player = null;
   }

   private void reset() {
      this.iconItem = null;
      this.iconItem = this.createImage(this.files[this.index]);
   }

   private Image createImage(String file) {
      try {
         return Arsenal.resize(Image.createImage(file), (float)this.getWidth() / 240.0F, (float)this.getHeight() / 320.0F);
      } catch (Exception var2) {
         System.out.println("ERROR create image " + file);
         return null;
      }
   }

   // цена текущего товара
   private int price() {
      return this.index == 5?this.prices[this.index]:(this.isNotPurchased()?this.prices[this.index]:(this.isPurchased()?this.prices[this.index] / 3:32767));
   }

   // true, если аптечкку можно купить (есть деньги и hp<100)
   private boolean isAvailableAidKit() {
      return this.index == 5 && this.player.getHp() == 100?false:this.player.getMoney() >= this.price();
   }

   // true, если оружие еще не куплено
   private boolean isNotPurchased() {
      Weapon[] var1 = this.player.getArsenal().getWeapons();
      return this.index >= 0 && this.index < 5 && var1[this.index] == null;
   }

   // true, если оружие уже куплено
   private boolean isPurchased() {
      Weapon[] var1 = this.player.getArsenal().getWeapons();
      return this.index >= 0 && this.index < 5 && var1[this.index] != null;
   }

   protected final void paint(Graphics g) {
      int var2 = this.getWidth();
      int var3 = this.getHeight();
      g.setColor(0);
      g.fillRect(0, 0, var2, var3);
      int var4 = var3 / 8;
      int var5 = (var5 = var3 - (var4 << 1)) - var5 % 15;
      if(this.isAvailableAidKit()) {
         Stringer var6 = this.main.getGameText$6783a6a7();
         this.setLeftSoft(var6.getString("BUY"));
         drawGrid(g, 0, var4, var2, var5, 15, 0, 255, 0);
      } else {
         this.setLeftSoft((String)null);
         drawGrid(g, 0, var4, var2, var5, 15, 255, 0, 0);
      }

      Font var8 = this.getFont();
      Stringer var7 = this.main.getGameText$6783a6a7();
      var8.drawString(g, var7.getString("MONEY") + ":" + this.player.getMoney(), var2 - 2, 2, 24);
      g.drawImage(this.iconItem, var2 / 2, var3 / 2, 3);
      if(this.isPurchased()) {
         g.drawImage(this.iconPatron, var2 / 2 - this.iconItem.getWidth() / 2, var3 / 2 + this.iconItem.getHeight() / 2, 36);
      }

      var8.drawString(g, var7.getString("CENA") + ":" + this.price(), this.getWidth() / 2, this.getHeight() / 2 + this.iconItem.getHeight() / 2 + 2, 17);
      this.drawSoftKeys(g);
      var4 = var2 / 15;
      var5 = var3 / 15;
      this.drawArrow(g, 4, var3 / 2, var4 + 4, var3 / 2 - var5, var4 + 4, var3 / 2 + var5);
      this.drawArrow(g, var2 - 4, var3 / 2, var2 - 4 - var4, var3 / 2 - var5, var2 - 4 - var4, var3 / 2 + var5);
   }

   private void drawArrow(Graphics g, int x1, int y1, int x2, int y2, int x3, int y3) {
      int var8 = Math.min(y1, Math.min(y2, y3));
      int var9;
      int var10 = ((var9 = Math.max(y1, Math.max(y2, y3))) - var8) / 3;

      for(int var11 = 0; var11 < var10; ++var11) {
         int var12;
         if(this.isAvailableAidKit()) {
            var12 = Math.min(255, var11 * 255 / var10);
            g.setColor(0, var12, 0);
         } else {
            var12 = Math.min(255, var11 * 255 / var10);
            g.setColor(var12, 0, 0);
         }

         g.setClip(0, var8 + (var9 - var8) * var11 / var10, this.getWidth(), var9);
         g.fillTriangle(x1, y1, x2, y2, x3, y3);
      }

      g.setClip(0, 0, this.getWidth(), this.getHeight());
   }

   private static void drawGrid(Graphics g, int var1, int var2, int var3, int var4, int var5, int red, int green, int blue) {
      var1 = var3 / 15;
      var5 = var4 / 15;
      blue = (var3 + 0) / 2;
      int var9 = (var2 + var4) / 2;
      int var10 = (Math.abs(blue) + Math.abs(var9 - var2)) / 2;

      for(int var11 = var2; var11 < var2 + var4; var11 += var5) {
         for(int var12 = 0; var12 < var3 + 0; var12 += var1) {
            int var13;
            if((var13 = (Math.abs(blue - var12) + Math.abs(var9 - var11)) / 2) < 0) {
               var13 = 0;
            }

            if(var13 > var10) {
               var13 = var10;
            }

            var13 = var10 - var13;
            int var14 = red * var13 / var10;
            int var15 = green * var13 / var10;
            var13 = var13 * 0 / var10;
            g.setColor(var14, var15, var13);
            g.drawRect(var12, var11, var1, var5);
         }
      }

   }

   protected final void onRightSoftKey() {
      try {
         this.destroy();
         System.gc();
         Thread.sleep(50L);
      } catch (Exception var2) {
         var2.printStackTrace();
      }

      this.gameScreen.start();
      this.main.setCurrent(this.gameScreen);
      this.gameScreen = null;
   }

   protected final void onLeftSoftKey() {
      if(this.isAvailableAidKit()) {
         Weapon[] var1;
         if(this.isNotPurchased()) {
            var1 = this.player.getArsenal().getWeapons();
            this.player.pay(this.price());
            var1[this.index] = Stringer.createWeapon(this.index);
            var1[this.index].setAmmo(100);
            this.repaint();
         } else if(this.isPurchased()) {
            var1 = this.player.getArsenal().getWeapons();
            this.player.pay(this.price());
            var1[this.index].addAmmo(100);
            this.repaint();
         } else {
            if(this.index == 5) {
               this.player.pay(this.price());
               this.player.setHp(100);
               this.repaint();
            }

         }
      }
   }

   protected final void onKey6() {
      ++this.index;
      this.index %= this.files.length;
      this.reset();
      this.repaint();
   }

   protected final void onKey4() {
      --this.index;
      if(this.index < 0) {
         this.index += this.files.length;
      }

      this.reset();
      this.repaint();
   }
}
