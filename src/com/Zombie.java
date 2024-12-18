package com;

import java.util.Vector;

public final class Zombie extends Bot {

   private static final int max_hp = Stringer.createFromResource("/setting.txt").getInt("LIFE_ZOMBIE");
   private static int damage_value = 1;
   private static short[][] animData;
   private static Asset meshData;
   private static final int model_height; // высота по y
   private int state = -1;
   private Morphing animation;
   private Player enemy = null;
   private Vector3D dir = new Vector3D();


   public Zombie(Vector3D pos) {
      this.animation = new Morphing(animData, meshData.copy());
      this.set(pos);
   }

   public final void set(Vector3D pos) {
      super.set(pos);
      this.setHp(max_hp);
      this.setCharacterSize(model_height);
   }

   public final void destroy() {
      super.destroy();
      this.enemy = null;
      this.animation.getMesh().destroy();
      this.animation.destroy();
      this.animation = null;
   }

   public final void render(Graphics3D g3d, int x1, int y1, int x2, int y2) {
      Matrix var6 = this.getCharacter().getTransform();
      var6 = g3d.computeFinalMatrix(var6);
      if(this.state == 1) {
         this.animation.setFrame(this.getFrame() * 140);
      }

      if(this.state == 2) {
         this.animation.setFrame(this.getFrame() * 140 * 5);
      }

      g3d.transformAndProjectVertices(this.animation.getMesh(), var6);
      g3d.addMesh(this.animation.getMesh(), x1, y1, x2, y2);
      increaseMeshSz(this.animation.getMesh(), 1000);
      this.renderBlood(g3d, 1500);
   }

   protected final void action(Scene scene) {
      if(this.getFrame() % 8 == 0) {
         House var7;
         Vector var2 = (var7 = scene.getHouse()).getObjects();
         if(this.enemy != null && this.enemy.isDead()) {
            this.enemy = null;
         }

         if(this.enemy == null) {
            this.enemy = find(var2);
         }

         if(this.enemy != null) {
            Vector3D var4 = this.dir;
            Player var3 = this.enemy;
            boolean var10000;
            if(this.notCollided(var7, var3)) {
               Matrix var8 = var3.getCharacter().getTransform();
               var4.set(var8.m03, var8.m13, var8.m23);
               var10000 = true;
            } else {
               Portal var9;
               if((var9 = commonPortal(var7, this.getPart(), var3.getPart())) != null) {
                  computeCentre(var9, var4);
               }

               var10000 = false;
            }

            boolean var10 = var10000;
            this.lookAt(this.dir.x, this.dir.z);
            long var5 = this.getCharacter().distance(this.enemy.getCharacter());
            if(var10 && (float)var5 <= (float)sqr(this.getCharacter().getRadius() + this.enemy.getCharacter().getRadius()) * 1.2F) {
               this.state = 2;
            } else {
               if(this.getCharacter().isCollision()) {
                  this.getCharacter().jump(140, 1.2F);
               }

               this.state = 1;
            }
         } else {
            this.state = -1;
         }
      }

      if(this.state == 1) {
         this.moveZ(140);
      }

      if(this.state == 2 && this.getFrame() % 4 == 0) {
         this.enemy.damage(this, damage_value);
      }

   }

   protected final void drop(Scene scene) {
      super.drop(scene);
      this.state = -1;
   }

   static {
      Texture texture;
      (texture = Texture.createTexture("/zombie.png")).setPerspectiveCorrection(false);
      Mesh[] meshes;
      Mesh mesh;
      (mesh = (meshes = Room.loadMeshes("/zombie.3d", 4.5F, 4.5F, 4.5F))[0]).setTexture(texture);
      model_height = mesh.maxY() - mesh.minY();
      meshData = new Asset(mesh);
      animData = Morphing.create(meshes);
   }
}
