package com;

// Магазин (для патронов)
final class Magazine {

   private final short capacity; // Максимальное кол-во патронов в магазине (вместимость)
   private short ammo; // Боезапас
   private short rounds; // Текущее кол-во патронов
   private final short reloadTime; // Продолжительность (кол-во циклов отрисовки) перезарядки
   private short frame = -1; // Текущее кол-во пройденных циклов перезарядки. Если -1, перезарядка не нужна, если >=0, начинается перезарядка


   public Magazine(int capacity, int reloadTime) {
      this.capacity = (short)capacity;
      this.reloadTime = (short)reloadTime;
   }

   public final void setAmmo(int ammo) {
      this.ammo = (short)ammo;
   }

   public final void addAmmo(int number) {
      this.ammo = (short)(this.ammo + number);
   }

   // ? Если есть патроны, начать перезарядку
   final void reload() {
      if(this.ammo != 0) {
         if(this.frame == -1) {
            this.frame = 0;
         }

      }
   }

   // ? Пересчет кол-ва пройденных циклов перезарядки
   final void update() {
      if(this.frame >= 0) {
         ++this.frame;
      }

      if(this.frame > this.reloadTime) {
         this.frame = -1;
         this.recount();
      }

   }

   // ? Пересчет кол-ва патронов
   final void recount() {
      this.rounds = (short)Math.min(this.capacity, this.ammo);
      this.ammo -= this.rounds;
   }

   // ? true, если нужно перезаряжаться (продолжать перезарядку)
   final boolean isReloading() {
      return this.frame != -1;
   }

   // ? Процент перезарядки
   final int percentage() {
      return 100 * this.frame / this.reloadTime;
   }

   final int getRounds() {
      return this.rounds;
   }

   final int getAmmo() {
      return this.ammo;
   }

   // Пересчет кол-ва патронов в магазине
   final void takeRounds(int number) {
      this.rounds = (short)(this.rounds - number);
   }
}
