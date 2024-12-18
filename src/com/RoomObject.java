package com;

public abstract class RoomObject {

   private int oldX;
   private int oldZ;
   private int part;


   public abstract void render(Graphics3D g3d, int x1, int y1, int x2, int y2);

   public final int getPart() {
      return this.part;
   }

   public final void setPart(int part) {
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
