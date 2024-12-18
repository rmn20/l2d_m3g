package com;

import home.Main;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public final class GameScreen extends Canvas implements Runnable {

   private final Main main;
   private final Font font;
   private final int levelNumber; // Номер уровня
   private final Object hudInfo; // Исп-ся для перехода на след. уровень. Хранит кол-во денег и патронов в каждом оружии
   private final int width; // Ширина экрана
   private final int height; // Высота экрана
   private boolean сhanged = true; // true, если произошло изменение
   private HouseCreator keys;
   private int key; // Код нажатой клавиши
   private int x; // x точки нажатия на экран
   private int y; // y точки нажатия на экран
   private int dirX; // x вектора, в направлении которого провели пальцем по экрану (dirX=x2-x1) 
   private int dirY; // y вектора, в направлении которого провели пальцем по экрану (dirY=y2-y1)
   private Thread thread;
   private boolean run;
   private boolean paused = false; // true, если нажали на паузу
   private int framesToEnd = 0; // счетчик кадров в течении которых выводятся сообщения "УРОВЕНЬ ЗАВЕРШЕН" и "КОНЕЦ ИГРЫ"
   private int framesToExit = 0; // счетчик кадров в течении которых выводится сообщение "НАЙДИТЕ ВЫХОД"
   private int hp; // здоровье игрока
   private int rounds; // кол-во патронов в магазине
   private int money;
   private int frags; // счетчик фрагов
   private Player player;
   private Scene scene;
   private Image imgSight;
   private Image imgLife;
   private Image imgPatron;
   private Image imgMoney;
   private Image imgSkull;
   private MusicPlayer musicPlayer;


   public GameScreen(Main main, String levelFile, int levelNumber, Object hudInfo) {
      this.main = main;
      this.levelNumber = levelNumber;
      this.font = main.getFont();
      this.hudInfo = hudInfo;
      this.setFullScreenMode(true);
      this.width = this.getWidth();
      this.height = this.getHeight();

      try {
         this.keys = new HouseCreator(this);
         this.imgSight = this.createImage("/sight.png");
         this.imgLife = this.createImage("/life.png");
         this.imgPatron = this.createImage("/patron.png");
         this.imgMoney = this.createImage("/money.png");
         this.imgSkull = this.createImage("/skull.png");
         this.scene = Respawn.createScene(this.width, (int)((float)this.height / 1.25F * ((float)main.getDisplaySize() / 100.0F)), levelFile);
         if(this.scene.getHouse().getSkybox() != null) {
            this.scene.getHouse().getSkybox().a_void_sub(true);
         }

         this.player = new Player(this.scene.getG3D().getWidth(), this.scene.getG3D().getHeight(), this.scene.getStartPoint(), this.hudInfo);
         this.scene.getHouse().addObject((RoomObject)this.player);
         if(main.isSound()) {
            this.musicPlayer = new MusicPlayer("/music.mid");
            this.musicPlayer.setLoopCount(-1);
         }

      } catch (Exception var5) {
         var5.printStackTrace();
      }
   }

   private Image createImage(String file) {
      try {
         return Arsenal.resize(Image.createImage(file), (float)this.width / 240.0F, (float)this.height / 320.0F);
      } catch (Exception var2) {
         System.out.println("ERROR create image " + file);
         return null;
      }
   }

   private void destroy() {
      try {
         this.scene.destroy();
         this.scene = null;
         this.player.destroy();
         this.player = null;
         this.imgSight = this.imgLife = this.imgPatron = this.imgMoney = this.imgSkull = null;
         if(this.musicPlayer != null) {
            this.musicPlayer.stop();
            this.musicPlayer.destroy();
            this.musicPlayer = null;
         }

      } catch (Exception var2) {
         var2.printStackTrace();
      }
   }

   private final void drawMessage(Graphics g, String str) {
      Graphics3D var3 = this.scene.getG3D();
      int var4 = this.height / 2 - var3.getHeight() / 2;
      this.font.drawString(g, str, var3.getWidth() / 2, var3.getHeight() / 2 + this.imgSight.getHeight() + var4, 3);
   }

   public final void paint(Graphics g) {
      Graphics3D var2 = this.scene.getG3D();
      boolean var3 = this.player.isDead();
      int var4 = this.player.getCharacter().getHeight();
      if(var3 && (var4 = (int)((float)var4 / Math.max(0.4F * (float)this.player.getFrame(), 1.0F))) < this.player.getCharacter().getRadius()) {
         var4 = this.player.getCharacter().getRadius();
      }

      Matrix var5;
      Matrix var10000 = var5 = this.player.getCharacter().getTransform();
      var10000.m13 += var4;
      var2.setCamera(var5);
      var5.m13 -= var4;
      var4 = this.height / 2 - var2.getHeight() / 2;
      this.scene.render(this.player.getPart());
      int var6;
      int var7;
      int var8;
      int var9;
      int[] var12;
      if(var3) {
         var12 = var2.getDisplay();

         for(var9 = 0; var9 < var12.length; ++var9) {
            var6 = (var8 = var12[var9]) >> 16 & 255;
            var7 = var8 >> 8 & 255;
            var8 &= 255;
            var8 = (var6 + var7 + var8) / 3;
            var12[var9] = var8 << 16 | var8 << 8 | var8;
         }
      }

      if(this.player.isDamaged()) {
         var12 = var2.getDisplay();

         for(var9 = 0; var9 < var12.length; ++var9) {
            var6 = (var8 = var12[var9]) >> 16 & 255;
            var7 = var8 >> 8 & 255;
            var8 &= 255;
            var12[var9] = (var6 + var7 + var8) / 3 << 16;
         }
      }

      this.scene.flush(g, 0, var4);
      if(!var3) {
         g.setClip(0, var4, var2.getWidth(), var2.getHeight());
         this.player.getArsenal().drawWeapon(g, var4, var2.getWidth(), var2.getHeight());
         g.setClip(0, 0, this.width, this.height);
      }

      g.drawImage(this.imgSight, var2.getWidth() / 2, var4 + var2.getHeight() / 2, 3);
      if(var3) {
         this.drawMessage(g, this.main.getGameText$6783a6a7().getString("GAME_OVER"));
      } else if(this.framesToEnd > 0) {
         if(!this.main.isLastLevel(this.levelNumber)) {
            this.drawMessage(g, this.main.getGameText$6783a6a7().getString("LEVEL_COMPLETE"));
         } else {
            this.drawMessage(g, this.main.getGameText$6783a6a7().getString("GAME_COMPLETE"));
         }
      } else if(this.framesToExit > 0 && this.framesToExit < 45) {
         this.drawMessage(g, this.main.getGameText$6783a6a7().getString("FIND_EXIT"));
      } else if(this.player.getHp() <= 15) {
         if(this.scene.getFrame() / 8 % 2 == 0) {
            this.drawMessage(g, this.main.getGameText$6783a6a7().getString("BUY_MEDICINE_CHEST"));
         }
      } else if(this.player.getArsenal().currentWeapon().getAmmo() <= 20 && this.scene.getFrame() / 8 % 2 == 0) {
         this.drawMessage(g, this.main.getGameText$6783a6a7().getString("BUY_PATRONS"));
      }

      int var10;
      if(this.сhanged) {
         g.setColor(0);
         g.fillRect(0, 0, this.width, var4);
         g.fillRect(0, var4 + var2.getHeight(), this.width, this.height - (var4 + var2.getHeight()));
         var10 = var4 / 2;
         g.drawImage(this.imgMoney, 4, var10, 6);
         this.font.drawString(g, " " + this.player.getMoney(), 4 + this.imgMoney.getWidth(), var10, 6);
         g.drawImage(this.imgSkull, this.width - 4, var10, 10);
         this.font.drawString(g, this.player.getFrags() + "/" + this.scene.getEnemyCount(), this.width - 4 - this.imgSkull.getWidth(), var10, 10);
         var10 = this.height - var4 / 2;
         g.drawImage(this.imgLife, 4, var10, 6);
         this.font.drawString(g, " " + this.player.getHp(), this.imgLife.getWidth(), var10, 6);
         g.drawImage(this.imgPatron, this.width - 4, var10, 10);
         Weapon var13 = this.player.getArsenal().currentWeapon();
         this.font.drawString(g, var13.getRounds() + "/" + var13.getAmmo() + " ", this.width - this.imgPatron.getWidth(), var10, 10);
         this.сhanged = false;
      }

      if(this.paused) {
         g.setColor(0);
         g.fillRect(0, 0, this.width, var4);
         g.fillRect(0, var4 + var2.getHeight(), this.width, this.height - (var4 + var2.getHeight()));

         for(var10 = 0; var10 < this.height; var10 += 2) {
            g.drawLine(0, var10, this.width, var10);
         }

         Stringer var11 = this.main.getGameText$6783a6a7();
         this.font.drawString(g, var11.getString("PAUSE"), this.width / 2, this.height / 2, 3);
         this.font.drawString(g, var11.getString("MENU"), this.width - 4, this.height - 4, 40);
         this.font.drawString(g, var11.getString("CONTINUE"), 4, this.height - 4, 36);
      }

   }

   protected final void pointerPressed(int x, int y) {
      this.x = x;
      this.y = y;
      if(GUIScreen.isLeftSoft(x, y, this.getWidth(), this.getHeight())) {
         this.keyPressed(this.keys.code7);
      }

      if(GUIScreen.isRightSoft(x, y, this.getWidth(), this.getHeight())) {
         this.keyPressed(this.keys.code9);
      }

   }

   protected final void pointerDragged(int x, int y) {
      this.dirX = x - this.x;
      this.dirY = y - this.y;
      this.x = x;
      this.y = y;
   }

   protected final void pointerReleased(int x, int y) {
      this.x = x;
      this.y = y;
      this.dirX = this.dirY = 0;
   }

   protected final void keyPressed(int key) {
      this.key = key;
      this.keys.keyPressed(key);
      if(this.paused) {
         if(this.key == this.keys.code7) {
            this.key = 0;
            this.paused = false;
            this.start();
         } else if(this.key == this.keys.code9) {
            this.destroy();
            Menu var3 = new Menu(this.main);
            this.main.setCurrent(var3);
         }
      } else if(this.key == this.keys.code9) {
         this.paused = true;
         this.stop();
         this.repaint();
      } else if((this.key == 49 || this.key == this.keys.code7) && !this.player.isDead()) {
         this.stop();
         this.main.setCurrent(new Shop(this.main, this, this.player));
      }

      if(key == -26) {
         Matrix var4 = this.player.getCharacter().getTransform();
         System.out.println(var4.m03 + ", " + var4.m13 + ", " + var4.m23 + ";");
      }

   }

   protected final void keyReleased(int key) {
      this.key = 0;
      this.keys.keyReleased(key);
   }

   public final void run() {
      while(this.run) {
         try {
            long var1 = System.currentTimeMillis();
            if(!this.player.isDead()) {
               if(this.keys.keyUp()) {
                  this.player.moveForward();
               }

               if(this.keys.keyDown()) {
                  this.player.moveBackward();
               }

               if(this.keys.keyLeft()) {
                  this.player.rotLeft();
               }

               if(this.keys.keyRight()) {
                  this.player.rotRight();
               }

               if(this.keys.key7()) {
                  this.player.moveLeft();
               }

               if(this.keys.key9()) {
                  this.player.moveRight();
               }

               if(this.keys.keyCentre()) {
                  this.player.fire();
               }

               if(this.key == 42) {
                  this.player.rotX(-3);
               }

               if(this.key == 35) {
                  this.player.rotX(3);
               }

               if(this.key == 48) {
                  this.player.jump();
               }

               if(this.key == 51) {
                  this.key = 0;
                  this.player.getArsenal().next(this.scene.getG3D().getWidth(), this.scene.getG3D().getHeight());
               }

               if(this.dirX * this.dirX > this.dirY * this.dirY) {
                  if(this.dirX < 0) {
                     this.player.rotLeft();
                  }

                  if(this.dirX > 0) {
                     this.player.rotRight();
                  }
               } else {
                  if(this.dirY > 0) {
                     this.player.rotX(-3);
                  }

                  if(this.dirY < 0) {
                     this.player.rotX(3);
                  }
               }
            }

            if(this.player.isTimeToRenew()) {
               this.framesToEnd = this.framesToExit = 0;
               this.scene.reset();
               this.player.set(this.scene.getG3D().getWidth(), this.scene.getG3D().getHeight(), this.scene.getStartPoint(), this.hudInfo);
            }

            this.scene.update(this.player);
            if(this.scene.getFrame() % 2 == 0) {
               if(this.framesToEnd == 0 && this.scene.isLevelCompleted(this.player)) {
                  this.framesToEnd = 1;
               }

               if(this.framesToExit == 0 && this.scene.isWinner(this.player)) {
                  this.framesToExit = 1;
               }
            }

            if(this.framesToEnd > 0) {
               ++this.framesToEnd;
            }

            if(this.framesToExit > 0) {
               ++this.framesToExit;
            }

            if(this.framesToEnd > 45) {
               this.main.addAvailableLevel(this.levelNumber);
               Object var11 = this.player.getHUDInfo();
               this.stop();
               this.destroy();
               Menu var12 = new Menu(this.main);
               LevelSelection var13 = new LevelSelection(this.main, var12, var11);
               this.main.setCurrent(var13);
               return;
            }

            if(!this.сhanged) {
               Weapon var3 = this.player.getArsenal().currentWeapon();
               this.сhanged = this.player.getHp() != this.hp || var3.getRounds() != this.rounds || this.player.getMoney() != this.money || this.player.getFrags() != this.frags;
               if(this.сhanged) {
                  this.hp = this.player.getHp();
                  this.rounds = var3.getRounds();
                  this.money = this.player.getMoney();
                  this.frags = this.player.getFrags();
               }
            }

            if(!this.сhanged) {
               Graphics3D var9 = this.scene.getG3D();
               int var4 = this.height / 2 - var9.getHeight() / 2;
               this.repaint(0, var4, var9.getWidth(), var9.getHeight());
            } else {
               this.repaint();
            }

            this.serviceRepaints();
            long var10 = System.currentTimeMillis();
            long var5;
            if((var5 = 50L - (var10 - var1)) < 3L) {
               var5 = 3L;
            }

            Thread.sleep(var5);
         } catch (Exception var8) {
            var8.printStackTrace();

            try {
               Thread.sleep(200L);
            } catch (InterruptedException var7) {
               var7.printStackTrace();
            }
         }
      }

   }

   public final void start() {
      this.сhanged = true;
      if(!this.run) {
         this.run = true;
         this.thread = new Thread(this);
         this.thread.start();
      }

      GameScreen var1 = this;
      if(this.musicPlayer != null) {
         try {
            var1.musicPlayer.start();
            return;
         } catch (Exception var2) {
            var2.printStackTrace();
         }
      }

   }

   private void stop() {
      if(this.run) {
         this.run = false;
         this.thread = null;
      }

      GameScreen var1 = this;
      if(this.musicPlayer != null) {
         try {
            var1.musicPlayer.stop();
            return;
         } catch (Exception var2) {
            var2.printStackTrace();
         }
      }

   }
}
