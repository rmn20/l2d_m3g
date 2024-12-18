package com;

public final class RayCast {

   private static final Vector3D temp = new Vector3D();
   private static final Vector3D colPoint = new Vector3D();
   private static final Vector3D normal = new Vector3D();
   private static final Vector3D v1 = new Vector3D();
   private static final Vector3D v2 = new Vector3D();
   private static final Vector3D v3 = new Vector3D();
   private static final Vector3D v4 = new Vector3D();


   public static void rayCast(Mesh mesh, Ray ray) {
      RenderObject[] var23 = mesh.getPoligons();
      Vector3D var2 = ray.start;
      Vector3D var3 = ray.dir;
      int var4 = var2.x;
      int var5 = var2.y;
      int var6 = var2.z;
      int var7 = var4 + var3.x;
      int var8 = var5 + var3.y;
      int var9 = var6 + var3.z;
      int var10 = Math.min(var4, var7);
      int var11 = Math.min(var5, var8);
      int var12 = Math.min(var6, var9);
      var4 = Math.max(var4, var7);
      var5 = Math.max(var5, var8);
      var6 = Math.max(var6, var9);
      var7 = Integer.MAX_VALUE;
      Object var25 = null;
      var9 = Integer.MAX_VALUE;

      int var13;
      Vector3D var30;
      for(var13 = 0; var13 < var23.length; ++var13) {
         RenderObject var14;
         Vertex var17;
         Vertex var16;
         Vertex var18;
         Vector3D var24;
         Vector3D var27;
         Vector3D var26;
         int var10000;
         if((var14 = var23[var13]) instanceof Polygon3V) {
            Polygon3V var15;
            var16 = (var15 = (Polygon3V)var14).a;
            var17 = var15.b;
            var18 = var15.c;
            if(vertexWithMaxX(vertexWithMaxX(var16, var17), var18).x < var10 || vertexWithMinX(vertexWithMinX(var16, var17), var18).x > var4 || vertexWithMaxZ(vertexWithMaxZ(var16, var17), var18).z < var12 || vertexWithMinZ(vertexWithMinZ(var16, var17), var18).z > var6 || vertexWithMaxY(vertexWithMaxY(var16, var17), var18).y < var11 || vertexWithMinY(vertexWithMinY(var16, var17), var18).y > var5) {
               continue;
            }

            label55: {
               normal.set(var15.nx, var15.ny, var15.nz);
               v1.set(var16.x, var16.y, var16.z);
               v2.set(var17.x, var17.y, var17.z);
               v3.set(var18.x, var18.y, var18.z);
               Vector3D var20 = temp;
               var30 = normal;
               var27 = v3;
               var26 = v2;
               var24 = v1;
               var20.set(var2.x - var24.x, var2.y - var24.y, var2.z - var24.z);
               int var21;
               if((var21 = var3.dot(var30) >> 12) > 0) {
                  if((var21 = -var20.dot(var30) / var21) < 0 || var21 > 4096) {
                     var10000 = Integer.MAX_VALUE;
                     break label55;
                  }

                  var20.set(var2.x + (var3.x * var21 >> 12), var2.y + (var3.y * var21 >> 12), var2.z + (var3.z * var21 >> 12));
                  if(MathUtils.isPointOnPolygon(var24, var26, var27, var30, var20)) {
                     var10000 = var21;
                     break label55;
                  }
               }

               var10000 = Integer.MAX_VALUE;
            }

            var7 = var10000;
         }

         if(var14 instanceof Polygon4V) {
            Polygon4V var29;
            var16 = (var29 = (Polygon4V)var14).a;
            var17 = var29.b;
            var18 = var29.c;
            Vertex var19 = var29.d;
            if(vertexWithMaxX(vertexWithMaxX(var16, var17), vertexWithMaxX(var18, var19)).x < var10 || vertexWithMinX(vertexWithMinX(var16, var17), vertexWithMinX(var18, var19)).x > var4 || vertexWithMaxZ(vertexWithMaxZ(var16, var17), vertexWithMaxZ(var18, var19)).z < var12 || vertexWithMinZ(vertexWithMinZ(var16, var17), vertexWithMinZ(var18, var19)).z > var6 || vertexWithMaxY(vertexWithMaxY(var16, var17), vertexWithMaxY(var18, var19)).y < var11 || vertexWithMinY(vertexWithMinY(var16, var17), vertexWithMinY(var18, var19)).y > var5) {
               continue;
            }

            label66: {
               normal.set(var29.nx, var29.ny, var29.nz);
               v1.set(var16.x, var16.y, var16.z);
               v2.set(var17.x, var17.y, var17.z);
               v3.set(var18.x, var18.y, var18.z);
               v4.set(var19.x, var19.y, var19.z);
               Vector3D var32 = temp;
               Vector3D var31 = normal;
               var30 = v4;
               var27 = v3;
               var26 = v2;
               var24 = v1;
               var32.set(var2.x - var24.x, var2.y - var24.y, var2.z - var24.z);
               int var22;
               if((var22 = var3.dot(var31) >> 12) > 0) {
                  if((var22 = -var32.dot(var31) / var22) < 0 || var22 > 4096) {
                     var10000 = Integer.MAX_VALUE;
                     break label66;
                  }

                  var32.set(var2.x + (var3.x * var22 >> 12), var2.y + (var3.y * var22 >> 12), var2.z + (var3.z * var22 >> 12));
                  if(MathUtils.isPointOnPolygon(var24, var26, var27, var30, var31, var32)) {
                     var10000 = var22;
                     break label66;
                  }
               }

               var10000 = Integer.MAX_VALUE;
            }

            var7 = var10000;
         }

         if(var7 < var9) {
            var9 = var7;
            colPoint.set(temp);
         }
      }

      if(var9 < Integer.MAX_VALUE && (var13 = (int)(1.0F / MathUtils.invSqrt((float)(var3.x * var3.x + var3.y * var3.y + var3.z * var3.z))) * var9 >> 12) < ray.distance) {
         var30 = colPoint;
         boolean var28 = true;
         ray.collision = true;
         ray.distance = var13;
         ray.collisionPoint.set(var30);
      }

   }

   private static Vertex vertexWithMaxX(Vertex v1, Vertex v2) {
      return v1.x > v2.x?v1:v2;
   }

   private static Vertex vertexWithMinX(Vertex v1, Vertex v2) {
      return v1.x < v2.x?v1:v2;
   }

   private static Vertex vertexWithMaxY(Vertex v1, Vertex v2) {
      return v1.y > v2.y?v1:v2;
   }

   private static Vertex vertexWithMinY(Vertex v1, Vertex v2) {
      return v1.y < v2.y?v1:v2;
   }

   private static Vertex vertexWithMaxZ(Vertex v1, Vertex v2) {
      return v1.z > v2.z?v1:v2;
   }

   private static Vertex vertexWithMinZ(Vertex v1, Vertex v2) {
      return v1.z < v2.z?v1:v2;
   }

}
