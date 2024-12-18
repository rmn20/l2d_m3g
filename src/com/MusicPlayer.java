package com;

import java.io.InputStream;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;


public final class MusicPlayer {

   private InputStream is;
   private Player player;


   public MusicPlayer(String file) throws Exception {
      String var2 = file.toLowerCase();
      String var3 = "";
      if(var2.endsWith(".wav")) {
         var3 = "audio/X-wav";
      }

      if(var2.endsWith(".midi") || var2.endsWith(".mid")) {
         var3 = "audio/midi";
      }

      if(var2.endsWith(".amr")) {
         var3 = "audio/amr";
      }

      this.is = this.getClass().getResourceAsStream(file);
      this.player = Manager.createPlayer(this.is, var3);
      this.player.realize();
      this.player.prefetch();
      this.player.setLoopCount(1);
   }

   public final void setLoopCount(int count) {
      this.player.setLoopCount(-1);
   }

   public final void start() {
      try {
         this.player.setMediaTime(0L);
         if(this.player.getState() != 400) {
            this.player.start();
         }

      } catch (MediaException var2) {
         var2.printStackTrace();
      }
   }

   public final void stop() {
      try {
         this.player.stop();
         this.player.deallocate();
      } catch (MediaException var2) {
         var2.printStackTrace();
      }
   }

   public final void destroy() {
      try {
         if(this.player != null) {
            this.stop();
            this.is.close();
            this.player.close();
            this.is = null;
            this.player = null;
         }
      } catch (Exception var2) {
         var2.printStackTrace();
      }
   }

   static {
      System.getProperty("supports.mixing").endsWith("true");
   }
}
