package com;

import java.util.Vector;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

// ? Хранилище
public class Asset {

   public static int getNumRecords(String recordStoreName) {
      try {
         RecordStore var3;
         int var1 = (var3 = RecordStore.openRecordStore(recordStoreName, true)).getNumRecords();
         var3.closeRecordStore();
         return var1;
      } catch (RecordStoreException var2) {
         var2.printStackTrace();
         return 0;
      }
   }

   public static byte[] getRecord(String recordStoreName, int recordId) {
      RecordStore var14 = null;
      byte[] var2 = null;
      boolean var8 = false;

      label77: {
         try {
            var8 = true;
            var2 = (var14 = RecordStore.openRecordStore(recordStoreName, true)).getRecord(1);
            var8 = false;
            break label77;
         } catch (Exception var12) {
            var12.printStackTrace();
            var8 = false;
         } finally {
            if(var8) {
               try {
                  var14.closeRecordStore();
               } catch (Exception var9) {
                  var9.printStackTrace();
               }

            }
         }

         try {
            var14.closeRecordStore();
         } catch (Exception var10) {
            var10.printStackTrace();
         }

         return var2;
      }

      try {
         var14.closeRecordStore();
      } catch (Exception var11) {
         var11.printStackTrace();
      }

      return var2;
   }
}
