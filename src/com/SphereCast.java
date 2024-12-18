package com;

public final class SphereCast {

   private static final Vector3D temp = new Vector3D();
   private static final Vector3D nor = new Vector3D();
   private static final Vector3D v1 = new Vector3D();
   private static final Vector3D v2 = new Vector3D();
   private static final Vector3D v3 = new Vector3D();
   private static final Vector3D v4 = new Vector3D();


   public static boolean sphereCast(Mesh mesh, Vector3D pos, int rad) {
      RenderObject[] var10000 = mesh.getPoligons();
      rad = rad;
      pos = pos;
      RenderObject[] var15 = var10000;
      boolean var3 = false;
      int var4 = Integer.MAX_VALUE;

      for(int var9 = 0; var9 < var15.length; ++var9) {
         Vertex var5;
         Vertex var6;
         Vertex var7;
         RenderObject var8;
         int var13;
         int var14;
         Vector3D var17;
         int var16;
         int var19;
         Vector3D var20;
         Vector3D var22;
         Vector3D var26;
         if((var8 = var15[var9]) instanceof Polygon3V) {
            Polygon3V var10;
            var5 = (var10 = (Polygon3V)var8).a;
            var6 = var10.b;
            var7 = var10.c;
            if(max(var5.x, var6.x, var7.x) < pos.x - rad || min(var5.x, var6.x, var7.x) > pos.x + rad || max(var5.z, var6.z, var7.z) < pos.z - rad || min(var5.z, var6.z, var7.z) > pos.z + rad || max(var5.y, var6.y, var7.y) < pos.y - rad || min(var5.y, var6.y, var7.y) > pos.y + rad) {
               continue;
            }

            label71: {
               v1.set(var5.x, var5.y, var5.z);
               v2.set(var6.x, var6.y, var6.z);
               v3.set(var7.x, var7.y, var7.z);
               nor.set(var10.nx, var10.ny, var10.nz);
               var26 = nor;
               var22 = v3;
               var20 = v2;
               var17 = v1;
               temp.set(pos.x - var17.x, pos.y - var17.y, pos.z - var17.z);
               int var12;
               if((var12 = temp.dot(var26) >> 12) <= rad) {
                  temp.set(pos.x - (var26.x * var12 >> 12), pos.y - (var26.y * var12 >> 12), pos.z - (var26.z * var12 >> 12));
                  if(MathUtils.isPointOnPolygon(var17, var20, var22, var26, temp)) {
                     var13 = var12;
                     if(var12 < 0) {
                        var13 = -var12;
                     }

                     var16 = rad - var13;
                     break label71;
                  }

                  var13 = MathUtils.distanceToLine(pos, var17, var20);
                  var14 = MathUtils.distanceToLine(pos, var20, var22);
                  var4 = MathUtils.distanceToLine(pos, var22, var17);
                  var19 = var13;
                  if(var14 < var13) {
                     var19 = var14;
                  }

                  if(var4 < var19) {
                     var19 = var4;
                  }

                  if(var19 <= rad * rad) {
                     var16 = rad - (int)(1.0F / MathUtils.invSqrt((float)var19));
                     break label71;
                  }
               }

               var16 = Integer.MAX_VALUE;
            }

            var4 = var16;
         }

         int var24;
         if(var8 instanceof Polygon4V) {
            Polygon4V var27;
            var5 = (var27 = (Polygon4V)var8).a;
            var6 = var27.b;
            var7 = var27.c;
            Vertex var23 = var27.d;
            if(max(var5.x, var6.x, var7.x, var23.x) < pos.x - rad || min(var5.x, var6.x, var7.x, var23.x) > pos.x + rad || max(var5.z, var6.z, var7.z, var23.z) < pos.z - rad || min(var5.z, var6.z, var7.z, var23.z) > pos.z + rad || max(var5.y, var6.y, var7.y, var23.y) < pos.y - rad || min(var5.y, var6.y, var7.y, var23.y) > pos.y + rad) {
               continue;
            }

            label85: {
               v1.set(var5.x, var5.y, var5.z);
               v2.set(var6.x, var6.y, var6.z);
               v3.set(var7.x, var7.y, var7.z);
               v4.set(var23.x, var23.y, var23.z);
               nor.set(var27.nx, var27.ny, var27.nz);
               Vector3D var18 = nor;
               var26 = v4;
               var22 = v3;
               var20 = v2;
               var17 = v1;
               temp.set(pos.x - var17.x, pos.y - var17.y, pos.z - var17.z);
               if((var13 = temp.dot(var18) >> 12) <= rad) {
                  temp.set(pos.x - (var18.x * var13 >> 12), pos.y - (var18.y * var13 >> 12), pos.z - (var18.z * var13 >> 12));
                  if(MathUtils.isPointOnPolygon(var17, var20, var22, var26, var18, temp)) {
                     var14 = var13;
                     if(var13 < 0) {
                        var14 = -var13;
                     }

                     var16 = rad - var14;
                     break label85;
                  }

                  var14 = MathUtils.distanceToLine(pos, var17, var20);
                  var4 = MathUtils.distanceToLine(pos, var20, var22);
                  var19 = MathUtils.distanceToLine(pos, var22, var26);
                  int var21 = MathUtils.distanceToLine(pos, var26, var17);
                  var24 = var14;
                  if(var4 < var14) {
                     var24 = var4;
                  }

                  if(var19 < var24) {
                     var24 = var19;
                  }

                  if(var21 < var24) {
                     var24 = var21;
                  }

                  if(var24 <= rad * rad) {
                     var16 = rad - (int)(1.0F / MathUtils.invSqrt((float)var24));
                     break label85;
                  }
               }

               var16 = Integer.MAX_VALUE;
            }

            var4 = var16;
         }

         if(var4 > 0 && var4 != Integer.MAX_VALUE) {
            int var10001 = -nor.x * var4 >> 12;
            int var10002 = -nor.y * var4 >> 12;
            int var25 = -nor.z * var4 >> 12;
            var24 = var10002;
            var19 = var10001;
            pos.x += var19;
            pos.y += var24;
            pos.z += var25;
            var3 = true;
         }
      }

      return var3;
   }

   private static int min(int x1, int x2, int x3) {
      return min(min(x1, x2), x3);
   }

   private static int max(int x1, int x2, int x3) {
      return max(max(x1, x2), x3);
   }

   private static int min(int x1, int x2, int x3, int x4) {
      return min(min(x1, x2), min(x3, x4));
   }

   private static int max(int x1, int x2, int x3, int x4) {
      return max(max(x1, x2), max(x3, x4));
   }

   private static int min(int x1, int x2) {
      return x1 < x2?x1:x2;
   }

   private static int max(int x1, int x2) {
      return x1 > x2?x1:x2;
   }

}
