package com;

public final class Vertex {

   public int x;
   public int y;
   public int z;
   
   public int sx;
   public int sy;
   public int rz;


   public Vertex() {}

   public Vertex(int x, int y, int z) {
      this.set(x, y, z);
   }

   public final void set(int x, int y, int z) {
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public final void transform(Matrix matrix) {
      int var2 = matrix.m00 >> 2;
      int var3 = matrix.m01 >> 2;
      int var4 = matrix.m02 >> 2;
      int var5 = matrix.m03;
      int var6 = matrix.m10 >> 2;
      int var7 = matrix.m11 >> 2;
      int var8 = matrix.m12 >> 2;
      int var9 = matrix.m13;
      int var10 = matrix.m20 >> 2;
      int var11 = matrix.m21 >> 2;
      int var12 = matrix.m22 >> 2;
      int var13 = matrix.m23;
      this.sx = (this.x * var2 >> 12) + (this.y * var3 >> 12) + (this.z * var4 >> 12) + var5;
      this.sy = (this.x * var6 >> 12) + (this.y * var7 >> 12) + (this.z * var8 >> 12) + var9;
      this.rz = (this.x * var10 >> 12) + (this.y * var11 >> 12) + (this.z * var12 >> 12) + var13;
   }

   // из Graphics3D: public void project(Vertex vertex)
   public final void project(Graphics3D g3d) {
      if(this.rz <= 0) {
         this.sx = this.sx * g3d.distX / (-this.rz + g3d.distX) + g3d.centreX;
         this.sy = -this.sy * g3d.distY / (-this.rz + g3d.distY) + g3d.centreY;
      } else {
         this.sx += g3d.centreX;
         this.sy = -this.sy + g3d.centreY;
      }
   }
}
