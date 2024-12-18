package com;

public abstract class RoomObject {

   private int oldX;
   private int oldZ;
   private int part;


   public abstract void render(Renderer g3d);

   public final int getPart() {
      return this.part;
   }

   public void setPart(int part) {
      this.part = part;
   }

   public abstract int getPosX();

   public abstract int getPosZ();

   protected boolean isNeedRecomputePart() {
      int var1 = this.getPosX();
      int var2 = this.getPosZ();
      if(this.oldX == var1 && this.oldZ == var2 && this.part != -1) {
         return false;
      } else {
         this.oldX = var1;
         this.oldZ = var2;
         return true;
      }
   }
}
