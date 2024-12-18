package com;

public final class Matrix {

   private static final short[] sin = new short[360];
   private static final short[] cos = new short[360];
   public int m00 = 16384;
   public int m01 = 0;
   public int m02 = 0;
   public int m03 = 0;
   
   public int m10 = 0;
   public int m11 = 16384;
   public int m12 = 0;
   public int m13 = 0;
   
   public int m20 = 0;
   public int m21 = 0;
   public int m22 = 16384;
   public int m23 = 0;


   public final void setIdentity() {
      this.m00 = 16384;
      this.m01 = 0;
      this.m02 = 0;
      this.m03 = 0;
      
      this.m10 = 0;
      this.m11 = 16384;
      this.m12 = 0;
      this.m13 = 0;
      
      this.m20 = 0;
      this.m21 = 0;
      this.m22 = 16384;
      this.m23 = 0;
   }

   public final void set(Matrix m) {
      this.m00 = m.m00;
      this.m01 = m.m01;
      this.m02 = m.m02;
      this.m03 = m.m03;
      
      this.m10 = m.m10;
      this.m11 = m.m11;
      this.m12 = m.m12;
      this.m13 = m.m13;
      
      this.m20 = m.m20;
      this.m21 = m.m21;
      this.m22 = m.m22;
      this.m23 = m.m23;
   }

   public final void invert() {
      long var1 = ((long)this.m00 * (long)this.m03 >> 14) + ((long)this.m10 * (long)this.m13 >> 14) + ((long)this.m20 * (long)this.m23 >> 14);
      long var3 = ((long)this.m01 * (long)this.m03 >> 14) + ((long)this.m11 * (long)this.m13 >> 14) + ((long)this.m21 * (long)this.m23 >> 14);
      long var5 = ((long)this.m02 * (long)this.m03 >> 14) + ((long)this.m12 * (long)this.m13 >> 14) + ((long)this.m22 * (long)this.m23 >> 14);
      this.m03 = (int)(-var1);
      this.m13 = (int)(-var3);
      this.m23 = (int)(-var5);
      int var7 = this.m01;
      this.m01 = this.m10;
      this.m10 = var7;
      var7 = this.m02;
      this.m02 = this.m20;
      this.m20 = var7;
      var7 = this.m12;
      this.m12 = this.m21;
      this.m21 = var7;
   }

   private static int fixDegree(int degree) {
      while(degree < 0) {
         degree += 360;
      }

      while(degree >= 360) {
         degree -= 360;
      }

      return degree;
   }

   public final void setRotX(int degree) {
      degree = fixDegree(degree);
      this.m00 = 16384;
      this.m01 = 0;
      this.m02 = 0;
      this.m03 = 0;
      this.m10 = 0;
      this.m11 = cos[degree];
      this.m12 = -sin[degree];
      this.m13 = 0;
      this.m20 = 0;
      this.m21 = sin[degree];
      this.m22 = cos[degree];
      this.m23 = 0;
   }

   public final void mul(Matrix m) {
      this.mul(this, m);
   }

   public final void mul(Matrix m2, Matrix m1) {
      long var10000 = (long)(m2.m00 * m1.m00 + m2.m01 * m1.m10 + m2.m02 * m1.m20);
      long var10001 = (long)m2.m03;
      long var3 = var10000 + 0L >> 14;
      var10000 = (long)(m2.m00 * m1.m01 + m2.m01 * m1.m11 + m2.m02 * m1.m21);
      var10001 = (long)m2.m03;
      long var5 = var10000 + 0L >> 14;
      var10000 = (long)(m2.m00 * m1.m02 + m2.m01 * m1.m12 + m2.m02 * m1.m22);
      var10001 = (long)m2.m03;
      long var7 = var10000 + 0L >> 14;
      long var9 = (long)m2.m00 * (long)m1.m03 + (long)m2.m01 * (long)m1.m13 + (long)m2.m02 * (long)m1.m23 + ((long)m2.m03 << 14) >> 14;
      var10000 = (long)(m2.m10 * m1.m00 + m2.m11 * m1.m10 + m2.m12 * m1.m20);
      var10001 = (long)m2.m13;
      long var11 = var10000 + 0L >> 14;
      var10000 = (long)(m2.m10 * m1.m01 + m2.m11 * m1.m11 + m2.m12 * m1.m21);
      var10001 = (long)m2.m13;
      long var13 = var10000 + 0L >> 14;
      var10000 = (long)(m2.m10 * m1.m02 + m2.m11 * m1.m12 + m2.m12 * m1.m22);
      var10001 = (long)m2.m13;
      long var15 = var10000 + 0L >> 14;
      long var17 = (long)m2.m10 * (long)m1.m03 + (long)m2.m11 * (long)m1.m13 + (long)m2.m12 * (long)m1.m23 + ((long)m2.m13 << 14) >> 14;
      var10000 = (long)(m2.m20 * m1.m00 + m2.m21 * m1.m10 + m2.m22 * m1.m20);
      var10001 = (long)m2.m23;
      long var19 = var10000 + 0L >> 14;
      var10000 = (long)(m2.m20 * m1.m01 + m2.m21 * m1.m11 + m2.m22 * m1.m21);
      var10001 = (long)m2.m23;
      long var21 = var10000 + 0L >> 14;
      var10000 = (long)(m2.m20 * m1.m02 + m2.m21 * m1.m12 + m2.m22 * m1.m22);
      var10001 = (long)m2.m23;
      long var23 = var10000 + 0L >> 14;
      long var25 = (long)m2.m20 * (long)m1.m03 + (long)m2.m21 * (long)m1.m13 + (long)m2.m22 * (long)m1.m23 + ((long)m2.m23 << 14) >> 14;
      this.m00 = (int)var3;
      this.m01 = (int)var5;
      this.m02 = (int)var7;
      this.m03 = (int)var9;
      this.m10 = (int)var11;
      this.m11 = (int)var13;
      this.m12 = (int)var15;
      this.m13 = (int)var17;
      this.m20 = (int)var19;
      this.m21 = (int)var21;
      this.m22 = (int)var23;
      this.m23 = (int)var25;
   }

   public final void rotY(int degree) {
      degree = fixDegree(degree);
      short var2 = cos[degree];
      short var8 = sin[degree];
      int var3 = this.m00 * var2 + this.m20 * var8 >> 14;
      int var4 = this.m01 * var2 + this.m21 * var8 >> 14;
      int var5 = this.m02 * var2 + this.m22 * var8 >> 14;
      
      int var6 = this.m20 * var2 - this.m00 * var8 >> 14;
      int var7 = this.m21 * var2 - this.m01 * var8 >> 14;
      degree = this.m22 * var2 - this.m02 * var8 >> 14;
      
      this.m00 = var3;
      this.m01 = var4;
      this.m02 = var5;
      this.m20 = var6;
      this.m21 = var7;
      this.m22 = degree;
   }

   public final void setPosition(int x, int y, int z) {
      this.m03 = x;
      this.m13 = y;
      this.m23 = z;
   }

   public final void setSide(int x, int y, int z) {
      this.m00 = x;
      this.m10 = y;
      this.m20 = z;
   }

   public final void setUp(int x, int y, int z) {
      this.m01 = x;
      this.m11 = y;
      this.m21 = z;
   }

   public final void setDir(int x, int y, int z) {
      this.m02 = x;
      this.m12 = y;
      this.m22 = z;
   }

   static {
      for(int var0 = 0; var0 < 360; ++var0) {
         sin[var0] = (short)((int)(Math.sin(Math.toRadians((double)var0)) * 16384.0D));
         cos[var0] = (short)((int)(Math.cos(Math.toRadians((double)var0)) * 16384.0D));
      }

   }
}
