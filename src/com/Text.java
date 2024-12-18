package com;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public final class Text {

   private Vector lines = new Vector(); // Строки текста, для вывода на экран
   private Font font;
   private int wSpace; // (размер бокового отступа области вывода текста от края экрана) = (ширина экрана)/15
   private int hSpace; // (размер верхнего отступа области вывода текста от края экрана) = (высота экрана)/13
   private int width; // (ширина области вывода текста) = ((ширина экрана) - ((ширина экрана)/15)*2)
   private int height; // (высота области вывода текста) = ((высота экрана) - ((высота экрана)/15)*2)
   private boolean centreAlign = false; // выравнивание по центру
   private int y; // y облати вывода текста


   public Text(String str, int wSpace, int hSpace, int width, int height, Font font) {
      this.font = font;
      this.wSpace = wSpace;
      this.hSpace = hSpace;
      this.width = width;
      this.height = height;
      str = deleteCR(str);
      this.fillLines(str, this.lines);
      this.y = hSpace;
   }

   // Высота строки в пикселях
   private int lineHieght() {
      return this.font.getHeight() + 3;
   }

   // Возвращает строку str с удаленными из нее символами возрата каретки 
   private static String deleteCR(String str) {
      StringBuffer var2 = new StringBuffer(str);
      int var1 = 0;

      while(var1 < var2.length()) {
         if(var2.charAt(var1) == 13) { // 13 - символ CR (возрат каретки)
            var2.deleteCharAt(var1);
         } else {
            ++var1;
         }
      }

      return var2.toString();
   }

   private void fillLines(String str, Vector lines) {
      int var3 = 0;
      int var4 = 0;
      int var5 = -1;
      int var6 = 0;

      while(var6 < str.length()) {
         char var7;
         if((var7 = str.charAt(var6)) == 32) { // 32 - пробел
            var5 = var6;
         }

         if(var7 != 10 && var3 + this.font.charWidth(var7) <= this.width) { // 10 - символ LF (перевод строки) 
            var3 += this.font.charWidth(var7);
            ++var6;
         } else {
            int var8 = var4;
            if(var7 == 10) {
               var8 = var6++;
            } else if(var3 + this.font.charWidth(var7) > this.width) {
               if(var5 != -1) {
                  var8 = var5;
                  var6 = var5;
               } else {
                  var8 = var6;
               }
            }

            String var9 = str.substring(var4, var8);
            lines.addElement(var9);
            var3 = 0;
            var4 = var6;
         }
      }

      if(var4 < str.length()) {
         lines.addElement(str.substring(var4, str.length()));
      }

   }

   public final void draw(Graphics g) {
      int var2 = this.lineHieght();
      int var3 = this.y;

      for(int var4 = 0; var4 < this.lines.size(); ++var4) {
         if(var3 >= this.hSpace) {
            if(var3 + var2 > this.height + this.hSpace) {
               break;
            }

            String var5 = (String)this.lines.elementAt(var4);
            int var6 = this.centreAlign?this.width - this.font.stringWidth(var5) >> 1:0;
            this.font.drawString(g, var5, var6 + this.wSpace, var3, 0);
         }

         var3 += var2;
      }

   }

   public final void move(int dy) {
      dy += this.y;
      int var2;
      if((var2 = this.lineHieght() * this.lines.size()) > this.height) {
         if(dy <= this.hSpace && dy + var2 >= this.hSpace + this.height - this.lineHieght()) {
            this.y = dy;
            return;
         }
      } else if(dy >= this.hSpace && dy + var2 < this.height + this.hSpace) {
         this.y = dy;
      }

   }

   public final void setCentreAlign(boolean flag) {
      this.centreAlign = true;
   }
}
