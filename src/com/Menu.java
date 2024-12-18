package com;

import home.Main;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class Menu extends Selectable {

   private Main main;
   private Image background;


   public Menu(Main main) {
      try {
         this.main = main;
         Stringer var2 = main.getGameText$6783a6a7();
         String[] var3 = new String[]{var2.getString("START_GAME"), var2.getString("HELP"), var2.getString("OPTIONS"), var2.getString("EXIT")};
         this.set(main.getFont(), var3, var2.getString("SELECT"), (String)null);
         this.background = Arsenal.resize(Image.createImage("/background.png"), this.getWidth(), this.getHeight());
      } catch (Exception var4) {
         var4.printStackTrace();
      }
   }

   public final void destroy() {
      super.destroy();
      this.background = null;
   }

   protected final void paint(Graphics g) {
      this.drawBackground(g);
      super.paint(g);
   }

   public final void drawBackground(Graphics g) {
      g.drawImage(this.background, 0, 0, 0);
   }

   protected final void onLeftSoftKey() {
      this.onKey5();
   }

   protected final void onKey5() {
      int var1;
      if((var1 = this.itemIndex()) == 0) {
         this.main.setCurrent(new LevelSelection(this.main, this));
      } else if(var1 == 1) {
         this.main.setCurrent(new Help(this.main, this));
      } else if(var1 == 2) {
         this.main.setCurrent(new Setting(this.main, this));
      } else if(var1 == 3) {
         this.main.notifyDestroyed();
      } else {
         this.repaint();
      }
   }
}
