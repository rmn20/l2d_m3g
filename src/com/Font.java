package com;

import java.io.IOException;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class Font {

   private int space; // Ширина пробела в пикселях
   private Image img;
   private int[] coords; // иксы левого верхнего угла каждого символа  
   private char[] chars; // Символы языка
   private int lines; // (кол-во строк с символами шрифта в img)-1
   private int y_src = 0; // у левого верхнего угла символа (нужна при использовании drawRegion)


   public Font(String file) {
      try {
         Stringer var4;
         String var2 = (var4 = Stringer.createFromResource(file)).getString("IMG");
         this.img = Image.createImage(var2);
         this.lines = var4.getInt("LINES");
         this.space = var4.getInt("SPACE");
         this.chars = var4.getString("CHARS").toCharArray();
         file = var4.getString("COORDS");
         this.coords = Mesh.cutOnInts(file, ',');
      } catch (IOException var3) {
         var3.printStackTrace();
      }
   }

   public final void setY(int val) {
      if(this.lines >= 0) {
         this.y_src = 0;
      }

   }

   private int indexOf(char ch) {
      for(int var2 = 0; var2 < this.chars.length; ++var2) {
         if(this.chars[var2] == ch) {
            return var2;
         }
      }

      return -1;
   }

   // Рисует строку на экране, аналогично методу из javax.microedition.lcdui.Graphics 
   public final void drawString(Graphics g, String str, int x, int y, int anchor) {
      int var10004 = str.length();
      int var6 = y;
      y = var10004;
      boolean var12 = false;
      int var8 = x;
      int var9 = var6;
      // 8=RIGHT
      if((anchor & 8) != 0) {
         var8 = x - this.widthOf(str);
      }

      // 32=BOTTOM
      if((anchor & 32) != 0) {
         var9 = var6 - this.height();
      }

      // 1=HCENTER
      if((anchor & 1) != 0) {
         var8 -= this.widthOf(str) >> 1;
      }

      // 2=VCENTER
      if((anchor & 2) != 0) {
         var9 -= this.height() >> 1;
      }

      // 64=BASELINE
      if((anchor & 64) != 0) {
         var9 -= this.height() + 1;
      }

      int var7 = var9;
      var6 = var8;
      anchor = y;
      byte var16 = 0;
      String var15 = str;
      Graphics var14 = g;
      Font var13 = this;
      var8 = this.img.getHeight() / (this.lines + 1);
      var9 = this.y_src * var8;

      for(y = var16; y < anchor; ++y) {
         char var10;
         if((var10 = var15.charAt(y)) == 32) {
            var6 += var13.space;
         } else {
            int var17;
            if((var17 = var13.indexOf(var10)) == -1) {
               var14.setColor(-1);
               var14.drawRect(var6, var7, var13.space, var8);
               var6 += var13.space;
            } else {
               int var11 = var13.coords[var17];
               var17 = var13.coords[var17 + 1] - var11;
               var14.drawRegion(var13.img, var11, var9, var17, var8, 0, var6, var7, 0);
               var6 += var17;
            }
         }
      }

   }

   // Ширина символа в пикселях
   public final int widthOf(char ch) {
      if(ch == 32) {
         return this.space;
      } else {
         int var3;
         if((var3 = this.indexOf(ch)) == -1) {
            return this.space;
         } else {
            int var2 = this.coords[var3];
            return this.coords[var3 + 1] - var2;
         }
      }
   }

   // Высота шрифта в пикселях
   public final int height() {
      return this.img.getHeight() / (this.lines + 1);
   }

   // Ширина строки в пикселях
   public final int widthOf(String str) {
      int var3 = str.length();
      boolean var6 = false;
      String var2 = str;
      Font var7 = this;
      int var4 = 0;

      for(int var5 = 0; var5 < var3; ++var5) {
         var4 += var7.widthOf(var2.charAt(var5));
      }

      return var4;
   }
}
