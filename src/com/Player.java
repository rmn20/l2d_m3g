package com;

public final class Player extends GameObject {

   private int money = 0;
   private int frags = 0;
   private Arsenal arsenal;
   private boolean damage = false;


   public Player(int width_g3d, int height_g3d, Vector3D pos, Object hudInfo) {
      this.set(width_g3d, height_g3d, pos, hudInfo);
   }

   public final void set(int width_g3d, int height_g3d, Vector3D pos, Object hudInfo) {
      this.getCharacter().reset();
      this.getCharacter().getPosition().set(pos.x, pos.y, pos.z);
      this.setHp(100);
      this.money = 0;
      this.frags = 0;
      this.setCharacterSize(2005);
      if(this.arsenal != null) {
         this.arsenal.destroy();
         this.arsenal = null;
      }

      this.arsenal = new Arsenal(width_g3d, height_g3d);
      if(hudInfo != null) {
         this.money = ((HUDInfo)hudInfo).money;
         int[] var8 = ((HUDInfo)hudInfo).ammo;
         Weapon[] var5 = this.arsenal.getWeapons();

         for(int var6 = 0; var6 < var5.length; ++var6) {
            if(var5[var6] != null) {
               var5[var6].reset();
            }

            if(var8[var6] == -1) {
               var5[var6] = null;
            } else {
               var5[var6] = Arsenal.createWeapon(var6);
               var5[var6].setAmmo(var8[var6]);
            }
         }

         this.arsenal.currentWeapon().createSprite(width_g3d, height_g3d);
      }

   }

   public final void destroy() {
      this.arsenal.destroy();
      this.arsenal = null;
   }

   public final void render(Renderer g3d) {
      Weapon.renderSplinter(g3d);
   }

   public final void update(Scene scene) {
      super.update(scene);
      GameObject var2;
      if((var2 = this.arsenal.currentWeapon().update(scene.getHouse(), this)) instanceof Zombie) {
         this.money += 10;
      }

      if(var2 instanceof BigZombie) {
         this.money += 30;
      }

      if(var2 != null) {
         ++this.frags;
      }

   }

   public final boolean damage(GameObject obj, int dmg) {
      this.damage = true;
      return super.damage(obj, dmg);
	   //return false;
   }

   public final boolean isDamaged() {
      boolean var1 = this.damage;
      this.damage = false;
      return var1;
   }

   public final int getMoney() {
      return this.money;
   }

   public final int getFrags() {
      return this.frags;
   }

   public final void pay(int price) {
      this.money -= price;
   }

   public final Arsenal getArsenal() {
      return this.arsenal;
   }

   public final boolean isTimeToRenew() {
      return this.isDead() && this.getFrame() > 45;
   }

   public final void fire() {
      this.arsenal.currentWeapon().fire();
   }

   public final void jump() {
      this.jump(150, 1.2F);
   }

   public final void rotLeft() {
      this.rotY(5);
   }

   public final void rotRight() {
      this.rotY(-5);
   }

   public final void rotX(int angle) {
      this.getCharacter().rotX(angle);
   }

   public final void moveForward() {
      this.moveZ(-150);
      this.arsenal.currentWeapon().enableShake();
   }

   public final void moveBackward() {
      this.moveZ(150);
      this.arsenal.currentWeapon().enableShake();
   }

   public final void moveLeft() {
      this.getCharacter().moveX(-150);
      this.arsenal.currentWeapon().enableShake();
   }

   public final void moveRight() {
      this.getCharacter().moveX(150);
      this.arsenal.currentWeapon().enableShake();
   }

   public final Object getHUDInfo() {
      Weapon[] var1;
      int[] var2 = new int[(var1 = this.arsenal.getWeapons()).length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         if(var1[var3] != null) {
            var2[var3] = var1[var3].getAmmo() + var1[var3].getRounds();
         } else {
            var2[var3] = -1;
         }
      }

      return new HUDInfo(this.money, var2);
   }
}
