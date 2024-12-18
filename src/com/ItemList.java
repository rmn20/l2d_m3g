package com;

import javax.microedition.lcdui.Graphics;


public final class ItemList {

   private Font font;
   private String[] items; // Пункты меню
   private int index = 0; // Номер выбранного пункта меню


   public ItemList(String[] items, Font font) {
      this.items = items;
      this.font = font;
   }

   public ItemList(String[] items) {
      this.items = items;
   }

   public final void setFont(Font font) {
      this.font = font;
   }

   public final void draw(Graphics g, int x1, int y1, int x2, int y2) {
      x1 = this.font.getHeight() + 3;
      y1 = y2 / x1;
      boolean var6 = this.items.length <= y1;
      int var7 = 0;
      if(var6) {
         var7 = 0 + (y2 / 2 - this.items.length * x1 / 2);
      }

      int var8 = 0;
      if(!var6) {
         if((var8 = this.index - y1 / 2) < 0) {
            var8 = 0;
         }

         if(var8 >= this.items.length - y1) {
            var8 = this.items.length - y1;
         }
      }

      for(; var8 < this.items.length; ++var8) {
         if(var7 >= 0) {
            if(var7 + x1 > y2 + 0) {
               break;
            }

            String var11 = this.items[var8];
            if(var8 == this.index) {
               int var12 = x2 / 2 - this.font.stringWidth(var11) / 2;
               int var9 = this.font.stringWidth(var11);
               int var10 = this.font.getHeight();
               g.setColor(16711680);
               g.drawRect(var12 - 1, var7 - 1, var9 + 1, var10 + 1); // Красная рамка вокруг выбранного пункта меню
            }

            this.font.drawString(g, var11, x2 / 2 - this.font.stringWidth(var11) / 2, var7, 0);
            var7 += x1;
         }
      }

   }

   public final void scrollDown() {
      ++this.index;
      this.index %= this.items.length;
   }

   public final void scrollUp() {
      --this.index;
      if(this.index < 0) {
         this.index = this.items.length - 1;
      }

   }

   public final int getIndex() {
      return this.index;
   }

   public final String getCurrentItem() {
      return this.items[this.index];
   }

   public final String[] getItems() {
      return this.items;
   }
}
