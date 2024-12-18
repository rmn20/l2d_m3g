package com;

import javax.microedition.lcdui.Graphics;

public final class Graphics3D {

   private static final int[] sort = new int[]{1, 4, 10, 23, 57, 145, 356, 911, 1968, 4711, 11969, 27901, 84801};
   int[] display;
   int width;
   int height;
   private Matrix camera = new Matrix();
   private Matrix invCamera = new Matrix();
   private Matrix finalCamera = new Matrix();
   int distX = 150;
   int distY = 150;
   int centreX;
   int centreY;
   private TMPElement[] buffer = new TMPElement[700];
   private int size = 0;


   public Graphics3D(int width, int height) {
      this.width = width;
      this.height = height;
      this.display = new int[width * height];

      for(int var3 = 0; var3 < this.buffer.length; ++var3) {
         this.buffer[var3] = new TMPElement();
      }

      this.centreX = width / 2;
      this.centreY = height / 2;
      boolean var4 = true;
      this.distX = this.centreX * 6144 >> 12;
      this.distY = this.centreY * 6144 >> 12;
   }

   public final void destroy() {
      this.camera = this.invCamera = this.finalCamera = null;

      for(int var1 = 0; var1 < this.buffer.length; ++var1) {
         this.buffer[var1] = null;
      }

      this.buffer = null;
   }

   public final TMPElement[] getBuffer() {
      return this.buffer;
   }

   public final int getSize() {
      return this.size;
   }

   public final int getWidth() {
      return this.width;
   }

   public final int getHeight() {
      return this.height;
   }

   public final int[] getDisplay() {
      return this.display;
   }

   public final void setCamera(Matrix matrix) {
      this.invCamera.set(matrix);
      this.camera.set(matrix);
      this.invCamera.invert();
   }

   public final Matrix getInvCamera() {
      return this.invCamera;
   }

   public final Matrix getCamera() {
      return this.camera;
   }

   public final Matrix computeFinalMatrix(Matrix matrix) {
      this.finalCamera.mul(this.invCamera, matrix);
      return this.finalCamera;
   }

   public final void addRenderObject(RenderObject obj) {
      if(obj.isVisible(0, 0, this.width, this.height) && this.size < this.buffer.length) {
         this.buffer[this.size].obj = obj;
         ++this.size;
      }

   }

   public final void addMesh(Mesh mesh, int x1, int y1, int x2, int y2) {
      Texture var6 = mesh.getTexture();
      RenderObject[] var13;
      int var7 = (var13 = mesh.getPoligons()).length;
      TMPElement[] var8 = this.buffer;
      int var9 = this.buffer.length;

      for(int var11 = 0; var11 < var7; ++var11) {
         if(this.size >= var9) {
            return;
         }

         RenderObject var10;
         if((var10 = var13[var11]).isVisible(x1, y1, x2, y2)) {
            TMPElement var12;
            (var12 = var8[this.size]).obj = var10;
            var12.tex = var6;
            ++this.size;
         }
      }

   }

   public final void render() {
      TMPElement[] var1 = this.buffer;
      int var3 = this.size;
      TMPElement[] var2 = this.buffer;

      int var8;
      for(var8 = 0; sort[var8] < var3; ++var8) {
         ;
      }

      while(true) {
         --var8;
         if(var8 < 0) {
            int var10 = this.size - 1;

            for(this.size = 0; var10 >= 0; --var10) {
               TMPElement var11;
               (var11 = var1[var10]).obj.render(this, var11.tex);
            }

            return;
         }

         int var7;
         for(int var5 = var7 = sort[var8]; var5 < var3; ++var5) {
            int var6 = var5;

            TMPElement var4;
            for(int var9 = (var4 = var2[var5]).obj.sz; var6 >= var7 && var2[var6 - var7].obj.sz < var9; var6 -= var7) {
               var2[var6] = var2[var6 - var7];
            }

            var2[var6] = var4;
         }
      }
   }

   public final void flush(Graphics g, int x, int y) {
      g.drawRGB(this.display, 0, this.width, x, y, this.width, this.height, false);
   }

   public final void transformAndProjectVertices(Mesh vertices, Matrix matrix) {
      Vertex[] var26 = vertices.getVertices();
      int var4 = this.centreX;
      int var5 = this.centreY;
      int var6 = this.distX;
      int var25 = this.distY;
      int var7 = matrix.m00 >> 2;
      int var8 = matrix.m01 >> 2;
      int var9 = matrix.m02 >> 2;
      int var10 = matrix.m03;
      int var11 = matrix.m10 >> 2;
      int var12 = matrix.m11 >> 2;
      int var13 = matrix.m12 >> 2;
      int var14 = matrix.m13;
      int var15 = matrix.m20 >> 2;
      int var16 = matrix.m21 >> 2;
      int var17 = matrix.m22 >> 2;
      int var27 = matrix.m23;

      for(int var24 = var26.length - 1; var24 >= 0; --var24) {
         Vertex var18;
         int var19 = (var18 = var26[var24]).x;
         int var20 = var18.y;
         int var21 = var18.z;
         int var22 = (var19 * var7 >> 12) + (var20 * var8 >> 12) + (var21 * var9 >> 12) + var10;
         int var23 = (var19 * var11 >> 12) + (var20 * var12 >> 12) + (var21 * var13 >> 12) + var14;
         if((var19 = (var19 * var15 >> 12) + (var20 * var16 >> 12) + (var21 * var17 >> 12) + var27) <= 0) {
            var22 = var22 * var6 / (-var19 + var6) + var4;
            var23 = -var23 * var25 / (-var19 + var25) + var5;
         } else {
            var22 += var4;
            var23 = -var23 + var5;
         }

         var18.sx = var22;
         var18.sy = var23;
         var18.rz = var19;
      }

   }

}
