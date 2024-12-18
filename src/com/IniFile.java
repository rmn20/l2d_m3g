package com;

import java.util.Hashtable;

public class IniFile {

   private final Hashtable hashtable; // Хранит значения параметров из текстовых файлов (название параметра, значение параметра)
   
   // Создает объект Stringer
   public static IniFile createFromResource(String file) {
      file = MeshData.getStringFromResource(file); // записывает все строки из файла в одну строку, потом
      return new IniFile(file);               // по этой строке заполняет hashtable
   }

   // заполняет hashtable значениями из строки str
   public IniFile(String str) {
      this.hashtable = new Hashtable();
      String[] var5 = MeshData.cutOnStrings(str, '\n');

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
