package com;

public class Skybox {

   private boolean a_boolean_fld;
   private Matrix matrix; // ?
   private Texture texture;
   private Mesh mesh;
   private int frame;
   private int x1; // ?
   private int y1; // ?
   private int x2; // ?
   private int y2; // ?
   private boolean b_boolean_fld;


   public Skybox() {}

   public Skybox(String modelPath, String texturePath) {
      this.a_boolean_fld = false;
      this.matrix = new Matrix();
      this.frame = 0;
      this.b_boolean_fld = false;
      this.texture = Texture.createTexture(texturePath);
      this.texture.setPerspectiveCorrection(false);
      this.mesh = Room.loadMesh(modelPath, 1.0F, 1.0F, 1.0F);
      this.mesh.setTexture(this.texture);
   }

   public void destroy() {
      this.matrix = null;
      this.texture.destroy();
      this.texture = null;
      this.mesh.destroy();
      this.mesh = null;
   }

   public void b_void_sub() {
      this.b_boolean_fld = true;
   }

   public void a_void_sub(int x1, int y1, int x2, int y2) {
      if(this.b_boolean_fld) {
         this.b_boolean_fld = false;
         this.x1 = x1;
         this.y1 = y1;
         this.x2 = x2;
         this.y2 = y2;
      } else {
         if(x1 < this.x1) {
            this.x1 = x1;
         }

         if(y1 < this.y1) {
            this.y1 = y1;
         }

         if(x2 > this.x2) {
            this.x2 = x2;
         }

         if(y2 > this.y2) {
            this.y2 = y2;
         }

      }
   }

   public void render(Graphics3D g3d) {
      this.matrix.set(g3d.getInvCamera());
      this.matrix.setPosition(0, 0, 0);
      if(this.a_boolean_fld) {
         int[] var3 = this.texture.getPixels();
         Skybox var2 = this;
         int var4 = var3.length / 4;

         for(int var5 = 0; var5 < var4; ++var5) {
            int var8 = var2.frame + 1;
            int var7 = var2.frame;
            int var9 = var3[var7];
            var3[var7] = var3[var8];
            var3[var8] = var9;
            ++var2.frame;
            var2.frame %= var3.length - 1;
         }
      }

      g3d.transformAndProjectVertices(this.mesh, this.matrix);
      g3d.addMesh(this.mesh, this.x1, this.y1, this.x2, this.y2);
   }

   public void a_void_sub(boolean var1) {
      this.a_boolean_fld = true;
   }

   public boolean a_boolean_sub() {
      return !this.b_boolean_fld;
   }
}
