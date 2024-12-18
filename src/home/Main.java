package home;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

import com.Font;
import com.Stringer;
import com.Asset;
import com.SplashScreen;

public final class Main extends MIDlet {

   private boolean run = false;
   private int lastLevel; // номер поледнего уровня
   private Font font;
   private Stringer gameText;
   private boolean sound = false; // true, ести звук включен
   private int displaySize = 100;
   private int availableLevel = 1; // номер доступного уровня


   private static boolean isExist(String file) {
      try {
         InputStream var2;
         if((var2 = file.getClass().getResourceAsStream(file)) == null) {
            return false;
         } else {
            var2.close();
            return true;
         }
      } catch (Exception var1) {
         return false;
      }
   }

   protected final void startApp() {
      if(!this.run) {
         this.run = true;

         int var1;
         for(var1 = 0; var1 < 50 && isExist("/level" + (var1 + 1) + ".txt"); ++var1) {
            ;
         }

         this.lastLevel = var1;
         this.setLanguage("/english.txt");
         Main var4 = this;
         this.sound = false;
         this.displaySize = 100;
         this.availableLevel = 1;

         try {
            if(Asset.getNumRecords("ZOMBIE") != 0) {
               byte[] var2 = Asset.getRecord("ZOMBIE", 1);
               ByteArrayInputStream var5 = new ByteArrayInputStream(var2);
               DataInputStream var6 = new DataInputStream(var5);
               var4.sound = var6.readBoolean();
               var4.displaySize = var6.readInt();
               var4.availableLevel = var6.readInt();
            }
         } catch (Exception var3) {
            var3.printStackTrace();
         }

         this.setCurrent(new SplashScreen(this));
      }

   }

   protected final void pauseApp() {}

   protected final void destroyApp(boolean var1) {}

   public final void setCurrent(Canvas canvas) {
      Display.getDisplay(this).setCurrent(canvas);
   }

   public final void setLanguage(String file) {
      this.gameText = Stringer.createFromResource(file);
      this.font = new Font(this.gameText.getString("FONT"));
   }

   public final Font getFont() {
      return this.font;
   }

   public final Stringer getGameText$6783a6a7() {
      return this.gameText;
   }

   public final void setSound(boolean flag) {
      this.sound = flag;
   }

   public final boolean isSound() {
      return this.sound;
   }

   public final void setDisplaySize(int size) {
      if(size < 50) {
         size = 50;
      }

      if(size > 100) {
         size = 100;
      }

      this.displaySize = size;
   }

   public final int getDisplaySize() {
      return this.displaySize;
   }

   public final int getAvailableLevelCount() {
      return Math.min(this.availableLevel, this.lastLevel);
   }

   public final void addAvailableLevel(int level) {
      if(level + 1 > this.availableLevel && level + 1 <= this.lastLevel) {
         this.availableLevel = level + 1;
         this.saveSettingToStore();
      }

   }

   public final boolean isLastLevel(int level) {
      return level >= this.lastLevel;
   }

   public final void saveSettingToStore() {
      try {
         String var1 = "ZOMBIE";

         try {
            RecordStore.deleteRecordStore(var1);
         } catch (RecordStoreException var5) {
            var5.printStackTrace();
         }

         ByteArrayOutputStream var7 = new ByteArrayOutputStream();
         DataOutputStream var2;
         (var2 = new DataOutputStream(var7)).writeBoolean(this.sound);
         var2.writeInt(this.displaySize);
         var2.writeInt(this.availableLevel);
         byte[] var3 = var7.toByteArray();
         var2.close();
         var7.close();
         String var9 = "ZOMBIE";
         byte[] var8 = var3;

         try {
            RecordStore var10;
            (var10 = RecordStore.openRecordStore(var9, true)).addRecord(var8, 0, var8.length);
            var10.closeRecordStore();
         } catch (RecordStoreException var4) {
            var4.printStackTrace();
            return;
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }
}
