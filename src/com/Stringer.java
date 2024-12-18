package com;

import java.util.Hashtable;

public class Stringer {

   private final Hashtable hashtable; // Хранит значения параметров из текстовых файлов (название параметра, значение параметра)


   //public e() {}

   public static Weapon createWeapon(int index) {
      return   index == 0?new Weapon("/pistol.png", "/fire.png", 1.0F, 1.0F, 20, 4, 2, false, 12, 10):
              (index == 1?new Weapon("/pistol.png", "/fire.png", 1.0F, 1.0F, 40, 4, 2, true, 24, 15):
              (index == 2?new Weapon("/shotgun.png", "/fire.png", 0.755F, 1.0F, 90, 5, 2, false, 6, 15):
              (index == 3?new Weapon("/m16.png", "/fire.png", 0.82F, 0.9F, 35, 2, 2, false, 30, 10):
              (index == 4?new Weapon("/uzi.png", "/fire.png", 0.55F, 0.9F, 20, 1, 1, false, 30, 10):null))));
   }

   // Создает объект Stringer
   public static Stringer createFromResource(String file) {
      file = Mesh.getStringFromResource(file); // записывает все строки из файла в одну строку, потом
      return new Stringer(file);               // по этой строке заполняет hashtable
   }

   // заполняет hashtable значениями из строки str
   public Stringer(String str) {
      this.hashtable = new Hashtable();
      String[] var5 = Mesh.cutOnStrings(str, '\n');

      for(int var2 = 0; var2 < var5.length; ++var2) {
         int var3;
         if((var3 = var5[var2].indexOf(61)) >= 0) { // 61 - код символа равно ( = )
            String var4 = var5[var2].substring(0, var3).trim();
            String var6 = var5[var2].substring(var3 + 1).trim();
            this.hashtable.put(var4, var6);
         }
      }

   }

   public String getString(String key) {
      String var2;
      if((var2 = (String)this.hashtable.get(key)) == null) {
         System.out.println("Stringer: " + key + " not found");
      }

      return var2;
   }

   public int getInt(String key) {
      return Integer.parseInt(this.getString(key));
   }

   public float getFloat(String key) {
      return Float.parseFloat(this.getString(key));
   }
}
