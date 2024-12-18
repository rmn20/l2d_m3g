package com;

import java.io.InputStream;
import java.util.Vector;

public class Mesh {

   private Texture texture;
   private Vertex[] vertices;
   private RenderObject[] polygons;


   public Mesh() {}

   // из ResourceLoader
   public static String getStringFromResource(String file) {
      InputStream var1 = null;

      try {
         var1 = (new Object()).getClass().getResourceAsStream(file);
         StringBuffer var5 = new StringBuffer();

         int var2;
         while((var2 = var1.read()) != -1) {
            if(var2 != 13) {
               var2 = var2 >= 192 && var2 <= 255?var2 + 848:var2;
               var5.append((char)var2);
            }
         }

         var1.close();
         return var5.toString();
      } catch (Exception var4) {
         System.out.println("ERROR in getStr: " + var4);
         if(var1 != null) {
            try {
               var1.close();
            } catch (Exception var3) {
               ;
            }
         }

         return null;
      }
   }

   /* Возращает вектор, содержащий фрагменты строки str,
   заключенные между друмя символами d или между первым символом в str 
   и первым символом d (для первого фрагмента)
   */
   private static Vector fragments(String str, char d) {
      Vector var2 = new Vector();
      int var5 = 0;

      while(var5 < str.length()) {
         while(var5 < str.length() && str.charAt(var5) == d) {
            ++var5;
         }

         int var3;
         for(var3 = var5; var5 < str.length() && str.charAt(var5) != d; ++var5) {
            ;
         }

         if(var3 < var5) {
            String var6 = str.substring(var3, var5);
            var2.addElement(var6);
         }
      }

      return var2;
   }

   public static String[] cutOnStrings(String str, char d) {
      Vector var2;
      String[] var3 = new String[(var2 = fragments(str, d)).size()];
      var2.copyInto(var3);
      return var3;
   }

   public static int[] cutOnInts(String str, char d) {
      Vector var3;
      int[] var4 = new int[(var3 = fragments(str, ',')).size()];

      for(int var2 = 0; var2 < var4.length; ++var2) {
         var4[var2] = Integer.parseInt((String)var3.elementAt(var2));
      }

      return var4;
   }

   public Mesh(Vertex[] vertices, RenderObject[] polygons) {
      this.vertices = vertices;
      this.polygons = polygons;
   }

   public void destroy() {
      this.texture = null;

      int var1;
      for(var1 = 0; var1 < this.polygons.length; ++var1) {
         this.polygons[var1] = null;
      }

      this.polygons = null;

      for(var1 = 0; var1 < this.vertices.length; ++var1) {
         this.vertices[var1] = null;
      }

      this.vertices = null;
   }

   public void setTexture(Texture texture) {
      this.texture = texture;
   }

   public int maxX() {
      int var1 = Integer.MIN_VALUE;

      for(int var2 = 0; var2 < this.vertices.length; ++var2) {
         if(this.vertices[var2].x > var1) {
            var1 = this.vertices[var2].x;
         }
      }

      return var1;
   }

   public int maxY() {
      int var1 = Integer.MIN_VALUE;

      for(int var2 = 0; var2 < this.vertices.length; ++var2) {
         if(this.vertices[var2].y > var1) {
            var1 = this.vertices[var2].y;
         }
      }

      return var1;
   }

   public int maxZ() {
      int var1 = Integer.MIN_VALUE;

      for(int var2 = 0; var2 < this.vertices.length; ++var2) {
         if(this.vertices[var2].z > var1) {
            var1 = this.vertices[var2].z;
         }
      }

      return var1;
   }

   public int minX() {
      int var1 = Integer.MAX_VALUE;

      for(int var2 = 0; var2 < this.vertices.length; ++var2) {
         if(this.vertices[var2].x < var1) {
            var1 = this.vertices[var2].x;
         }
      }

      return var1;
   }

   public int minY() {
      int var1 = Integer.MAX_VALUE;

      for(int var2 = 0; var2 < this.vertices.length; ++var2) {
         if(this.vertices[var2].y < var1) {
            var1 = this.vertices[var2].y;
         }
      }

      return var1;
   }

   public int minZ() {
      int var1 = Integer.MAX_VALUE;

      for(int var2 = 0; var2 < this.vertices.length; ++var2) {
         if(this.vertices[var2].z < var1) {
            var1 = this.vertices[var2].z;
         }
      }

      return var1;
   }

   public Texture getTexture() {
      return this.texture;
   }

   public Vertex[] getVertices() {
      return this.vertices;
   }

   public RenderObject[] getPoligons() {
      return this.polygons;
   }
}
