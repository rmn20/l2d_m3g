package com;

import home.Main;
import javax.microedition.lcdui.Graphics;

public final class LevelSelection extends Selectable {

   private Main main;
   private Menu menu;
   private Object hudInfo;


   public LevelSelection(Main main, Menu menu) {
      this.main = main;
      this.menu = menu;
      IniFile var6;
      String var3 = (var6 = main.getGameText$6783a6a7()).getString("LEVEL");
      String[] var4 = new String[main.getAvailableLevelCount()];

      for(int var5 = 0; var5 < var4.length; ++var5) {
         var4[var5] = var3 + " " + (var5 + 1);
      }

      this.set(main.getFont(), var4, var6.getString("SELECT"), var6.getString("BACK"));
   }

   public LevelSelection(Main main, Menu menu, Object hudInfo) {
      this(main, menu);
      this.hudInfo = hudInfo;
   }

   protected final void paint(Graphics g) {
      this.menu.drawBackground(g);
      super.paint(g);
   }

   protected final void onRightSoftKey() {
      this.main.setCurrent(this.menu);
   }

   protected final void onLeftSoftKey() {
      this.onKey5();
   }

   protected final void onKey5() {
      try {
         int var1;
         String var2;
         if(this.itemIndex() != 0) {
            var1 = this.itemIndex() + 1;
            var2 = "/level" + var1 + ".txt";
            this.menu.destroy();
            this.destroy();
            System.gc();
            Thread.sleep(5L);
            GameScreen var5 = new GameScreen(this.main, var2, var1, this.hudInfo);
			var5.start();
            this.main.setCurrent(var5);
			var5.repaint();
            return;
         }

         var1 = this.itemIndex() + 1;
         var2 = "/level" + var1 + ".txt";
         GameHelp var4 = new GameHelp(this.main, this.menu, var2, var1, this.hudInfo);
         this.main.setCurrent(var4);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }
}
