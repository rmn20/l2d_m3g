package com;

public final class MathUtils {

   public static float invSqrt(float val) {
      float var1 = 0.5F * val;
      int var2 = Float.floatToIntBits(val);
      return (val = Float.intBitsToFloat(var2 = 1597463007 - (var2 >> 1))) * (1.5F - var1 * val * val);
   }

   public static Vector3D createNormal(int ax, int ay, int az, int bx, int by, int bz, int cx, int cy, int cz) {
      double var9 = (double)((long)(ay - by) * (long)(az - cz) - (long)(az - bz) * (long)(ay - cy));
      double var11 = (double)((long)(az - bz) * (long)(ax - cx) - (long)(ax - bx) * (long)(az - cz));
      double var13 = (double)((long)(ax - bx) * (long)(ay - cy) - (long)(ay - by) * (long)(ax - cx));
      double var15 = Math.sqrt(var9 * var9 + var11 * var11 + var13 * var13) / 4096.0D;
      return new Vector3D((int)(var9 / var15), (int)(var11 / var15), (int)(var13 / var15));
   }

   public static int distanceToLine(Vector3D point, Vector3D a, Vector3D b) {
      int var3 = b.x - a.x;
      int var4 = b.y - a.y;
      int var13 = b.z - a.z;
      int var5 = point.x - a.x;
      int var6 = point.y - a.y;
      int var7 = point.z - a.z;
      long var9 = (long)var3 * (long)var3 + (long)(var4 * var4) + (long)(var13 * var13);
      long var11 = 0L;
      if(var9 != 0L) {
         var11 = ((long)var5 * (long)var3 + (long)(var6 * var4) + (long)(var7 * var13) << 14) / var9;
      }

      if(var11 < 0L) {
         var11 = 0L;
      }

      if(var11 > 16384L) {
         var11 = 16384L;
      }

      var3 = a.x + (int)((long)var3 * var11 >> 14);
      var4 = a.y + (int)((long)var4 * var11 >> 14);
      var13 = a.z + (int)((long)var13 * var11 >> 14);
      var3 -= point.x;
      var4 -= point.y;
      var13 -= point.z;
      return var3 * var3 + var4 * var4 + var13 * var13;
   }

   public static int distanceToRay(Vector3D point, Vector3D a, Vector3D dir) {
      int var3 = dir.x;
      int var4 = dir.y;
      int var13 = dir.z;
      int var5 = point.x - a.x;
      int var6 = point.y - a.y;
      int var7 = point.z - a.z;
      long var9 = (long)(var3 * var3 + var4 * var4 + var13 * var13);
      long var11 = 0L;
      if(var9 != 0L) {
         var11 = ((long)var5 * (long)var3 + (long)(var6 * var4) + (long)(var7 * var13) << 14) / var9;
      }

      if(var11 < 0L) {
         var11 = 0L;
      }

      if(var11 > 16384L) {
         var11 = 16384L;
      }

      var3 = a.x + (int)((long)var3 * var11 >> 14);
      var4 = a.y + (int)((long)var4 * var11 >> 14);
      var13 = a.z + (int)((long)var13 * var11 >> 14);
      var3 -= point.x;
      var4 -= point.y;
      var13 -= point.z;
      return var3 * var3 + var4 * var4 + var13 * var13;
   }

   public static boolean isPointOnPolygon(Vector3D a, Vector3D b, Vector3D c, Vector3D normal, Vector3D point) {
      int var5 = normal.x > 0?normal.x:-normal.x;
      int var6 = normal.y > 0?normal.y:-normal.y;
      int var7 = normal.z > 0?normal.z:-normal.z;
      return var5 >= var6 && var5 >= var7?(normal.x >= 0?isPointOnPolygon(point.z, point.y, a.z, a.y, b.z, b.y, c.z, c.y):isPointOnPolygon(point.z, point.y, c.z, c.y, b.z, b.y, a.z, a.y)):(var6 >= var5 && var6 >= var7?(normal.y >= 0?isPointOnPolygon(point.x, point.z, a.x, a.z, b.x, b.z, c.x, c.z):isPointOnPolygon(point.x, point.z, c.x, c.z, b.x, b.z, a.x, a.z)):(var7 >= var5 && var7 >= var6?(normal.z <= 0?isPointOnPolygon(point.x, point.y, a.x, a.y, b.x, b.y, c.x, c.y):isPointOnPolygon(point.x, point.y, c.x, c.y, b.x, b.y, a.x, a.y)):true));
   }

   public static boolean isPointOnPolygon(int px, int py, int x1, int y1, int x2, int y2, int x3, int y3) {
      return (x2 - x1) * (py - y1) - (px - x1) * (y2 - y1) > 0?false:((x3 - x2) * (py - y2) - (px - x2) * (y3 - y2) > 0?false:(x1 - x3) * (py - y3) - (px - x3) * (y1 - y3) <= 0);
   }

   public static boolean isPointOnPolygon(Vector3D a, Vector3D b, Vector3D c, Vector3D d, Vector3D normal, Vector3D point) {
      int var6 = normal.x > 0?normal.x:-normal.x;
      int var7 = normal.y > 0?normal.y:-normal.y;
      int var8 = normal.z > 0?normal.z:-normal.z;
      return var6 >= var7 && var6 >= var8?(normal.x >= 0?isPointOnPolygon(point.z, point.y, a.z, a.y, b.z, b.y, c.z, c.y, d.z, d.y):isPointOnPolygon(point.z, point.y, d.z, d.y, c.z, c.y, b.z, b.y, a.z, a.y)):(var7 >= var6 && var7 >= var8?(normal.y >= 0?isPointOnPolygon(point.x, point.z, a.x, a.z, b.x, b.z, c.x, c.z, d.x, d.z):isPointOnPolygon(point.x, point.z, d.x, d.z, c.x, c.z, b.x, b.z, a.x, a.z)):(var8 >= var6 && var8 >= var7?(normal.z <= 0?isPointOnPolygon(point.x, point.y, a.x, a.y, b.x, b.y, c.x, c.y, d.x, d.y):isPointOnPolygon(point.x, point.y, d.x, d.y, c.x, c.y, b.x, b.y, a.x, a.y)):true));
   }

   public static boolean isPointOnPolygon(int px, int py, int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
      return (x2 - x1) * (py - y1) - (px - x1) * (y2 - y1) > 0?false:((x3 - x2) * (py - y2) - (px - x2) * (y3 - y2) > 0?false:((x4 - x3) * (py - y3) - (px - x3) * (y4 - y3) > 0?false:(x1 - x4) * (py - y4) - (px - x4) * (y1 - y4) <= 0));
   }

   public static boolean isPointOnPolygon(int px, int pz, Vertex a, Vertex b, Vertex c, int norY) {
      return norY > 0?isPointOnPolygon(px, pz, a.x, a.z, b.x, b.z, c.x, c.z):(norY < 0?isPointOnPolygon(px, pz, c.x, c.z, b.x, b.z, a.x, a.z):false);
   }

   public static boolean isPointOnPolygon(int px, int pz, Vertex a, Vertex b, Vertex c, Vertex d, int norY) {
      return norY > 0?isPointOnPolygon(px, pz, a.x, a.z, b.x, b.z, c.x, c.z, d.x, d.z):(norY < 0?isPointOnPolygon(px, pz, d.x, d.z, c.x, c.z, b.x, b.z, a.x, a.z):false);
   }

   static {
      new Vector3D();
   }
}
