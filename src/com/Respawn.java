package com;

public class Respawn {

   final Vector3D point;
   final int part;
final byte mode=-128;
final byte cmode=-128;
final boolean respa=false;


   public Respawn(Vector3D point, House house) {
      this.point = point;
      this.part = house.computePart(-1, point.x, point.z);
      if(this.part == -1) {
         System.out.println("ERROR: неправильная точка старта " + point.x + " " + point.y + " " + point.z);
      }

   }

   //public u() {}

   public static Scene createScene(int width, int height, String file) {
      Stringer var10;
      float var3 = (var10 = Stringer.createFromResource(file)).getFloat("WORLD_SCALE");
      Mesh[] var12 = Room.loadMeshes(var10.getString("WORLD_MODEL"), var3, var3, var3);
      Texture var4 = Texture.createTexture(var10.getString("WORLD_TEXTURE"));
      String var5 = var10.getString("SKYBOX_MODEL");
      String var6 = var10.getString("SKYBOX_TEXTURE");
      Skybox var7 = null;
      if(var5 != null && var6 != null) {
         var7 = new Skybox(var10.getString("SKYBOX_MODEL"), var10.getString("SKYBOX_TEXTURE"));
      }

      var4.setPerspectiveCorrection(true);
      House var13;
      (var13 = HouseCreator.create(var12)).setTexture(var4);
      Respawn var14 = readPoints(var10.getString("START"), var13)[0];
      Respawn var15 = readPoints(var10.getString("FINISH"), var13)[0];
      Respawn[] var16 =readPoints(var10.getString("ENEMIES"), var13);

      int var8 = var10.getInt("ENEMY_COUNT");
      int var11 = var10.getInt("FREQUENCY");
      Scene var9;
      (var9 = new Scene(width, height, var13, var14, var15, var16, var8, var11)).getHouse().setSkybox(var7);
      return var9;
   }

   private static Respawn[] readPoints(String file, House house) {
      if(file == null) {
         return null;
      } else {
         file = file;
         StringBuffer var2 = new StringBuffer();

         int var3;
         for(var3 = 0; var3 < file.length(); ++var3) {
            char var4;
            if((var4 = file.charAt(var3)) != 32) {
               var2.append(var4);
            }
         }

         String[] var5;
         Respawn[] var6 = new Respawn[(var5 = Mesh.cutOnStrings(var2.toString(), ';')).length];

         for(var3 = 0; var3 < var6.length; ++var3) {
            int[] var7 = Mesh.cutOnInts(var5[var3], ',');
            Vector3D var8 = new Vector3D(var7[0], var7[1], var7[2]);
            var6[var3] = new Respawn(var8, house);
if(var7.length>2)
{
var6[var3].mode=-127+var7[3];
if(var7.length==4) 
{
if(var7[4]==1) var6[var3].respa=true;
}
}
         }

         return var6;
      }
   }

}
