package com;

public final class Vector3D {

   public int x;
   public int y;
   public int z;


   public Vector3D() {}

   public Vector3D(int x, int y, int z) {
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public final void set(int x, int y, int z) {
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public final void set(Vector3D v) {
      this.x = v.x;
      this.y = v.y;
      this.z = v.z;
   }

   public final int lengthSquared() {
      return this.x * this.x + this.y * this.y + this.z * this.z;
   }

   public final void setLength(int len) {
      int var2;
      if((var2 = this.x * this.x + this.y * this.y + this.z * this.z) != len * len) {
         float var3 = MathUtils.invSqrt((float)var2) * (float)len;
         this.x = (int)((float)this.x * var3);
         this.y = (int)((float)this.y * var3);
         this.z = (int)((float)this.z * var3);
      }
   }

   public final int dot(Vector3D v) {
      return this.x * v.x + this.y * v.y + this.z * v.z;
   }

   public final String toString() {
      return this.x + " " + this.y + " " + this.z;
   }
}
