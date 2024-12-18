package com;

import java.util.Vector;

public abstract class Bot extends GameObject {

   private static final Ray ray = new Ray();
   private static final Vector3D tmpVec = new Vector3D(); // ?
   private static final Vector3D side = new Vector3D();
   private static final Vector3D up = new Vector3D();
   private Blood blood = new Blood(this);


   public void set(Vector3D pos) {
      this.getCharacter().reset();
      this.getCharacter().getTransform().setPosition(pos.x, pos.y, pos.z);
      this.blood.reset();
   }

   public void destroy() {
      this.blood.destroy();
      this.blood = null;
   }

   protected final boolean isNeedRecomputePart() {
      return this.getFrame() % 3 != 0?false:super.isNeedRecomputePart();
   }

   protected final void renderBlood(Graphics3D g3d, int sz) {
      if(this.blood.isBleeding()) {
         this.blood.render(g3d, 2250);
      }

   }

   public final boolean damage(GameObject obj, int dmg) {
      if(obj != null && this.getCharacter().isOnFloor()) {
         Matrix var3 = this.getCharacter().getTransform();
         Matrix var4 = obj.getCharacter().getTransform();
         tmpVec.set(var3.m03 - var4.m03, var3.m03 - var4.m03, var3.m03 - var4.m03);
         tmpVec.setLength(dmg * this.getCharacter().getRadius() / 200);
         Vector3D var10000 = this.getCharacter().getSpeed();
         Vector3D var6 = tmpVec;
         Vector3D var5 = var10000;
         var10000.x += var6.x;
         var5.y += var6.y;
         var5.z += var6.z;
      }

      this.blood.bleed();
      return super.damage(obj, dmg);
   }

   // Поведение (движение и поиск врага или падение при 0 hp)
   public final void update(Scene scene) {
      if(!this.isDead()) {
         this.action(scene);
      } else {
         this.drop(scene);
      }

      super.update(scene);
   }

   protected abstract void action(Scene scene);

   // Падение при 0 hp
   protected void drop(Scene scene) {
      Matrix var2;
      if((var2 = this.getCharacter().getTransform()).m11 > 0) {
         matrix.setRotX(-8);
         var2.mul(matrix);
      }

   }

   protected static void increaseMeshSz(Mesh mesh, int val) {
      RenderObject[] var2 = mesh.getPoligons();

      for(val = 0; val < var2.length; ++val) {
         var2[val].sz += 1000;
      }

   }

   protected final boolean notCollided(House house, GameObject obj) {
      Matrix var7 = obj.getCharacter().getTransform();
      int var5 = var7.m23;
      int var4 = var7.m13;
      int var3 = var7.m03;
      Matrix var6 = this.getCharacter().getTransform();
      ray.reset();
      ray.getStart().set(var6.m03, var6.m13 + this.getCharacter().getHeight(), var6.m23);
      ray.getDir().set(var3 - var6.m03, var4 - var6.m13, var5 - var6.m23);
      house.rayCast(this.getPart(), ray);
      return !ray.isCollision();
   }

   // Возвращает портал, соединяющий комнаты по номерами part1 и part2. Если общего портала нет, возращает null
   protected static Portal commonPortal(House house, int part1, int part2) {
      Portal[] var4 = house.getRooms()[part1].getPortals(); // все порталы комнаты под номером part1

      for(part1 = 0; part1 < var4.length; ++part1) {
         Portal var3;
         if((var3 = var4[part1]).getRoom().getId() == part2) {
            return var3;
         }
      }

      return null;
   }

   // из MeshImage: private static Vertex computeCentre(Vertex[] vertices) {
   protected static void computeCentre(Portal portal, Vector3D vec) {
      Vertex[] var7 = portal.getVertices();
      int var2 = 0;
      int var3 = 0;
      int var4 = 0;

      for(int var5 = 0; var5 < var7.length; ++var5) {
         Vertex var6 = var7[var5];
         var2 += var6.x;
         var3 += var6.y;
         var4 += var6.z;
      }

      var2 /= var7.length;
      var3 /= var7.length;
      var4 /= var7.length;
      vec.set(var2, var3, var4);
   }

   protected static Player find(Vector objs) {
      for(int var1 = 0; var1 < objs.size(); ++var1) {
         GameObject var2;
         if(!(var2 = (GameObject)objs.elementAt(var1)).isDead() && var2 instanceof Player) {
            return (Player)var2;
         }
      }

      return null;
   }

   // ?
   protected final void lookAt(int posX, int posZ) {
      Matrix var3;
      Matrix var10000 = var3 = this.getCharacter().getTransform();
      int var10001 = posX - var3.m03;
      int var10 = posZ - var3.m23;
      posZ = var10001;
      Matrix var8 = var10000;
      tmpVec.set(posZ, 0, var10);
      tmpVec.setLength(16384);
      int var7 = var8.m22;
      int var6 = var8.m12;
      int var5 = var8.m02;
      int var4 = tmpVec.z;
      var10 = tmpVec.y;
      var10001 = tmpVec.x;
      if(Math.abs(tmpVec.x - var5) >= 20 || Math.abs(var10 - var6) >= 20 || Math.abs(var4 - var7) >= 20) {
         up.set(0, 16384, 0);
         boolean var13 = true;
         Vector3D var12 = tmpVec;
         Vector3D var11 = up;
         Vector3D var9 = side;
         side.x = var11.y * var12.z - var12.y * var11.z >> 14;
         var9.y = var11.z * var12.x - var12.z * var11.x >> 14;
         var9.z = var11.x * var12.y - var12.x * var11.y >> 14;
         side.setLength(16384);
         if(tmpVec.lengthSquared() != 0 && side.lengthSquared() != 0) {
            var8.setDir(tmpVec.x, tmpVec.y, tmpVec.z);
            var8.setSide(side.x, side.y, side.z);
            var8.setUp(up.x, up.y, up.z);
         }
      }

   }

   protected static long sqr(int val) {
      return (long)val * (long)val;
   }

}
