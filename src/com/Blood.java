package com;

final class Blood {

   private static final Texture blood = Texture.createTexture("/blood.png");
   private GameObject obj;
   private int frame = Integer.MAX_VALUE;
   private Sprite sprite = new Sprite(5);


   public Blood(GameObject obj) {
      this.obj = obj;
      this.sprite.setTextures(new Texture[]{blood});
      this.sprite.setScale(5);
   }

   public final void reset() {
      this.sprite.setScale(5);
      this.frame = Integer.MAX_VALUE;
   }

   public final void destroy() {
      this.obj = null;
      this.sprite.destroy();
      this.sprite = null;
   }

   public final void bleed() {
      this.frame = 0;
      this.sprite.mirX = !this.sprite.mirX;
      this.sprite.mirY = !this.sprite.mirX;
   }

   public final void render(Graphics3D g3d, int sz) {
      Matrix var3 = this.obj.getCharacter().getTransform();
      this.sprite.getPosition().set(var3.m03, var3.m13 + this.obj.getCharacter().getHeight(), var3.m23);
      ++this.frame;
      this.sprite.setScale(5 * this.frame);
      this.sprite.setOffset(0, -this.sprite.getHeight() / 2 - this.frame * 40);
      this.sprite.project(g3d.getInvCamera(), g3d);
      g3d.addRenderObject((RenderObject)this.sprite);
      this.sprite.sz += sz;
   }

   public final boolean isBleeding() {
      return this.frame < 7;
   }

}
