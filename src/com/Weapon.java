package com;

import java.io.IOException;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class Weapon {

   private static final Splinter splinter = new Splinter();
   private static final Ray ray = new Ray();
   private final short damageValue; // Урон от выстрела
   private final short delay; // Задержка между выстрелами
   private final short shotTime; // Продолжительность выстрела
   private short frame = -1; // Кол-во кадров прорисованных во время выстрела и после. Если -1, нет выстрела.
   private String fileWeapon; // путь к картинке оружия
   private String fileFire; // путь к картинке вспышки выстрела
   private float kW; // коэф. смещения вспышки по горизонтали относительно правого нижнего угла спрайта оружия
   private float kH; // коэф. смещения вспышки по вертикали
   private Image imgWeapon;
   private Image imgFire;
   private short dx_fire = 1; // смещение вспышки по горизонтали относительно правого нижнего угла спрайта оружия
   private short dy_fire = 1; // смещение вспышки по вертикали
   private short dx_max = 0; // максимально возможное dx
   private short dy_max = 0; // максимально возможное dy
   private short dx = 0; // смещение спрайта оружия по горизонтали (от левого нижнего угла) в текущем кадре
   private short dy = 0; // смещение спрайта оружия по вертикали (от левого нижнего угла) в текущем кадре
   private short widthShift = 2; // смещение смешения оружия по горизонтали при ходьбе
   private short heightShift = 5; // смещение смешения оружия по вертикали при ходьбе
   private boolean twoHands = false; // Оружие в двух руках
   private boolean shake = false; // Тряска оружия
   private Magazine magazine;


   public Weapon(String fileWeapon, String fileFire, float kW, float kH, int damageValue, int delay, int shotTime, boolean twoHands, int capacity, int reloadTime) {
      this.fileWeapon = fileWeapon;
      this.fileFire = fileFire;
      this.kW = kW;
      this.kH = kH;
      this.damageValue = (short)damageValue;
      this.delay = (short)delay;
      this.shotTime = (short)shotTime;
      this.twoHands = twoHands;
      this.magazine = new Magazine(capacity, reloadTime);
   }

   public final void reset() {
      this.imgWeapon = this.imgFire = null;
   }

   // ?
   public final void createSprite(int width_g3d, int height_g3d) {
      Image[] var6 = this.createImages(this.fileWeapon, this.fileFire, width_g3d, height_g3d);
      Image var10001 = var6[0];
      Image var10002 = var6[1];
      float var5 = this.kH;
      float var4 = this.kW;
      Image var3 = var10002;
      Image var7 = var10001;
      this.imgWeapon = var7;
      this.imgFire = var3;
      this.dx_fire = (short)((int)((float)this.imgWeapon.getWidth() * var4));
      this.dy_fire = (short)((int)((float)this.imgWeapon.getHeight() * var5));
      this.dx_max = (short)(this.imgWeapon.getWidth() / 5);
      this.dy_max = (short)(this.imgWeapon.getHeight() / 5);
   }

   private Image[] createImages(String file1, String file2, int width, int height) {
      float var6 = (float)width / 240.0F;
      float var7 = (float)height / 320.0F;

      try {
         return new Image[]{createImage(file1, var6, var7), createImage(file2, var6, var7)};
      } catch (IOException var5) {
         var5.printStackTrace();
         return null;
      }
   }

   private static Image createImage(String file, float scaleW, float scaleH) throws IOException {
      Image var3;
      int var4 = (int)((float)(var3 = Image.createImage(file)).getWidth() * scaleW);
      int var5 = (int)((float)var3.getHeight() * scaleH);
      return var3.getWidth() == var4 && var3.getHeight() == var5?var3:Arsenal.resize(var3, var4, var5);
   }

   // true, если происходит выстрел
   private boolean isFire() {
      return this.frame >= 0;
   }

   public final void draw(Graphics g, int x, int y, int width, int height) { // x, y, width, height - координаты, ширина и высота области внутри черной рамки
      if(this.isFire()) {
         g.drawImage(this.imgFire, width - this.dx_fire + this.dx, height - this.dy_fire + this.dy + y, 3); // 3=2+1 = VCENTER+HCENTER
      }

      g.drawImage(this.imgWeapon, width + this.dx, height + this.dy + y, 40); // 40=32+8 = BOTTOM+RIGHT
      if(this.twoHands) {
         if(this.isFire()) {
            g.drawRegion(this.imgFire, 0, 0, this.imgFire.getWidth(), this.imgFire.getHeight(), 2, this.dx_fire - this.dx, height - this.dy_fire + (this.dy_max - this.dy) + y, 3);
         }

         g.drawRegion(this.imgWeapon, 0, 0, this.imgWeapon.getWidth(), this.imgWeapon.getHeight(), 2, 0 - this.dx, height + (this.dy_max - this.dy) + y, 36); // 36=32+4 = BOTTOM+LEFT
      }

   }

   public static void renderSplinter(Renderer g3d) {
      if(splinter.isShatters()) {
         splinter.render(g3d, 1500);
      }

   }

   public final void enableShake() {
      this.shake = true;
   }

   public final int getRounds() {
      return this.magazine.getRounds();
   }

   public final int getAmmo() {
      return this.magazine.getAmmo();
   }

   public final void setAmmo(int number) {
      this.magazine.setAmmo(number);
      this.magazine.recount();
   }

   // true, если нужно перезаряжаться (продолжать перезарядку)
   public final boolean isReloading() {
      return this.magazine.isReloading();
   }

   public final int reloadingPercentage() {
      return this.magazine.percentage();
   }

   // Анимация ходьбы. Нанисение урона врагу. Задание точки столкновения осколка выстрела (splinter).
   // Возвращает ссылку на врага, если он убит, иначе null
   public final GameObject update(House house, GameObject player) {
      this.magazine.update();
      boolean var3 = this.frame == 0;
      if(this.isFire()) {
         ++this.frame;
         if(this.frame > this.shotTime) {
            this.frame = (short)(-this.delay);
         }
      }

      if(this.frame < -1) {
         ++this.frame;
      }

      if(this.isFire()) {
         this.dx = (short)(this.dx + (Math.abs(this.widthShift) << 1));
         this.dy = (short)(this.dy + (Math.abs(this.heightShift) << 1));
      }

      if(this.shake) {
         this.dx += this.widthShift;
         this.dy += this.heightShift;
         this.shake = false;
      } else {
         this.dx = (short)(this.dx + -this.dx / 8);
         this.dy = (short)(this.dy + -this.dy / 8);
      }

      if(this.dy <= 0) {
         this.dy = 0;
         this.heightShift = (short)(-this.heightShift);
      }

      if(this.dy > this.dy_max) {
         this.dy = this.dy_max;
         this.heightShift = (short)(-this.heightShift);
      }

      if(this.dx <= 0) {
         this.dx = 0;
         this.widthShift = (short)(-this.widthShift);
      }

      if(this.dx >= this.dx_max) {
         this.dx = this.dx_max;
         this.widthShift = (short)(-this.widthShift);
      }

      if(!var3) {
         return null;
      } else {
         Vector3D playerPos = player.getCharacter().getPosition();
         Vector3D playerRot = player.getCharacter().getRotation();
		 
         Vector3D dirVector = new Vector3D();
		 dirVector.setFromRotation(playerRot.x, playerRot.y);
		 
         ray.reset();
         Vector3D var4 = ray.getStart();
         Vector3D var5 = ray.getDir();
         var4.set(playerPos.x, playerPos.y + player.getCharacter().getHeight(), playerPos.z);
         var5.set(dirVector.x, dirVector.y, dirVector.z);
         ray.reset();
         house.rayCast(player.getPart(), ray);
         if(ray.isCollision()) {
            var5.setLength(ray.getDistance());
         }

         Vector3D var6 = var5;
         var5 = var4;
         GameObject var31 = player;
         GameObject var32 = null;
         long var17 = 2147483647L;
         Vector var27 = house.getObjects();
         Vector3D var7 = new Vector3D();

         for(int var8 = 0; var8 < var27.size(); ++var8) {
            GameObject var9;
            if((var9 = (GameObject)var27.elementAt(var8)) != var31 && !var9.isDead()) {
               Character var10;
               Vector3D var11 = (var10 = var9.getCharacter()).getPosition();
               var7.set(var11.x, var11.y + var10.getHeight(), var11.z);
               int var34 = var10.getRadius();
               long var10000;
               if(MathUtils.distanceToRay(var7, var5, var6) < var34 * var34) {
                  Vector3D var35;
                  int var12 = (var35 = var7).x - var5.x;
                  int var13 = var35.y - var5.y;
                  int var36 = var35.z - var5.z;
                  var10000 = (long)var12 * (long)var12 + (long)var13 * (long)var13 + (long)var36 * (long)var36;
               } else {
                  var10000 = 2147483647L;
               }

               long var25 = var10000;
               if(var10000 < var17) {
                  var32 = var9;
                  var17 = var25;
               }
            }
         }

         var3 = var32 != null;
         boolean var33 = false;
         if(var3) {
            var33 = var32.damage(player, this.damageValue);
         }

         if(!var3 && ray.isCollision()) {
            Vector3D colPoint = ray.getCollisionPoint();
            splinter.set(colPoint.x, colPoint.y, colPoint.z);
         }

         if(var33) {
            return var32;
         } else {
            return null;
         }
      }
   }

   //? Если есть патроны в магазине, начать анимацию выстрела и пересчитаь кол-во патронов в магазине, иначе начать перезарядку.
   public final void fire() {
      if(this.frame == -1) {
         if(this.magazine.getRounds() > 0) {
            this.frame = 0;
            if(!this.twoHands) {
               this.magazine.takeRounds(1);
               return;
            }

            this.magazine.takeRounds(2);
            return;
         }

         this.magazine.reload();
      }

   }

   public final boolean isTwoHands() {
      return this.twoHands;
   }

   public final void addAmmo(int number) {
      this.magazine.addAmmo(100);
   }

}
