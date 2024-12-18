package com;

import java.util.Vector;

public final class House {

   private Room[] rooms;
   private Room[][] neighbours;
   private Vector nearRooms = new Vector(); // ? или renderedRooms
   private Vector renderedPortals = new Vector();
   private Skybox skybox;
   private Vector objects = new Vector(); // из Room


   public House(Room[] rooms, Room[][] neighbours) {
      this.rooms = rooms;
      this.neighbours = neighbours;
   }

   public final void destroy() {
      for(int var1 = 0; var1 < this.rooms.length; ++var1) {
         this.rooms[var1].destroy();
         this.rooms[var1] = null;
      }

      this.rooms = null;
      this.neighbours = null;
      this.nearRooms.removeAllElements();
      this.nearRooms = null;
      if(this.skybox != null) {
         this.skybox.destroy();
         this.skybox = null;
      }

   }

   public final void setTexture(Texture texture) {
      for(int var2 = 0; var2 < this.rooms.length; ++var2) {
         this.rooms[var2].getMesh().setTexture(texture);
      }

   }

   public final void setSkybox(Skybox skybox) {
      this.skybox = skybox;
   }

   public final Skybox getSkybox() {
      return this.skybox;
   }

   public final Room[] getRooms() {
      return this.rooms;
   }

   public final Room[] getNeighbourRooms(int part) {
      return this.neighbours[part];
   }

   private Vector getNearRooms(int part) {
      this.nearRooms.removeAllElements();
      if(part == -1) {
         return this.nearRooms;
      } else {
         if(this.rooms[part] != null) {
            this.nearRooms.addElement(this.rooms[part]);
         }

         Room[] var3 = this.neighbours[part];

         for(int var2 = 0; var2 < var3.length; ++var2) {
            if(var3[var2] != null) {
               this.nearRooms.addElement(var3[var2]);
            }
         }

         return this.nearRooms;
      }
   }

   public final boolean sphereCast(int part, Vector3D pos, int rad) {
      Vector var7 = this.getNearRooms(part);
      boolean var4 = false;

      for(int var5 = 0; var5 < var7.size(); ++var5) {
         Room var6 = (Room)var7.elementAt(var5);
         var4 |= var6.sphereCast(pos, rad);
      }

      return var4;
   }

   public final void rayCast(int part, Ray ray) {
      Vector var5 = this.getNearRooms(part);

      for(int var3 = 0; var3 < var5.size(); ++var3) {
         ((Room)var5.elementAt(var3)).rayCast(ray);
      }

   }

   // ??? максимальное значение y дома (house), которое ниже точки (x,y,z)
   public final int a_int_sub(int part, int x, int y, int z) {
      int var5 = Integer.MIN_VALUE;
      Vector var8 = this.getNearRooms(part);

      for(int var6 = 0; var6 < var8.size(); ++var6) {
         int var7;
         if((var7 = ((Room)var8.elementAt(var6)).a_int_sub(x, y, z)) < y && var7 > var5) {
            var5 = var7;
         }
      }

      return var5;
   }

   public final int render(Graphics3D g3d, int part) {
      if(this.skybox != null && this.skybox.a_boolean_sub()) {
         this.skybox.render(g3d);
         g3d.render();
         this.skybox.b_void_sub();
      }

      if(part == -1) {
         return 0;
      } else {
         this.nearRooms.removeAllElements();
         this.renderedPortals.removeAllElements();
         this.render(g3d, this.rooms[part], 0, 0, g3d.getWidth(), g3d.getHeight());

         for(part = 0; part < this.nearRooms.size(); ++part) {
            ((Room)this.nearRooms.elementAt(part)).render(g3d, this.objects);
         }

         return this.nearRooms.size();
      }
   }

   private void render(Graphics3D g3d, Room mainRoom, int x1, int y1, int x2, int y2) {
      if(mainRoom != null) {
         mainRoom.setViewport(x1, y1, x2, y2);
         mainRoom.render(g3d);
         this.nearRooms.addElement(mainRoom);
         if(mainRoom.isOpenSky() && this.skybox != null) {
            this.skybox.a_void_sub(x1, y1, x2, y2);
         }

         Portal[] var15 = mainRoom.getPortals();

         for(int var7 = 0; var7 < var15.length; ++var7) {
            Portal var8 = var15[var7];
            if(!this.nearRooms.contains(var8.getRoom()) && var8.isVisible(g3d, x1, y1, x2, y2)) {
               int var9 = var8.getMinX();
               int var10 = var8.getMinY();
               int var11 = var8.getMaxX();
               int var12 = var8.getMaxY();
               if(var9 < x1) {
                  var9 = x1;
               }

               if(var10 < y1) {
                  var10 = y1;
               }

               if(var11 > x2) {
                  var11 = x2;
               }

               if(var12 > y2) {
                  var12 = y2;
               }

               int var13 = var11 - var9;
               int var14 = var12 - var10;
               if((var13 >= 20 || var14 >= 20) && var13 >= 5 && var14 >= 5) {
                  this.renderedPortals.addElement(var8);
                  this.render(g3d, var8.getRoom(), var9, var10, var11, var12);
               }
            }
         }

      }
   }

   public final Vector getObjects() {
      return this.objects;
   }

   public final void addObject(RoomObject obj) {
      this.recomputePart(obj);
      if(obj.getPart() == -1) {
         System.out.println("House: новый обьект не лежит не на одной комнате");
      } else if(!this.objects.contains(obj)) {
         this.objects.addElement(obj);
      } else {
         System.out.println("House: такой обьект уже добавлен");
      }
   }

   public final void removeObject(RoomObject obj) {
      if(!this.objects.removeElement(obj)) {
         System.out.println("House: такого обьекта уже нет");
      }

   }

   public final void recomputePart(RoomObject obj) {
      if(obj.isNeedRecomputePart()) {
         int var2 = obj.getPosX();
         int var3 = obj.getPosZ();
         int var4 = obj.getPart();
         if((var4 = this.computePart(var4, var2, var3)) == -1) {
            System.out.println("House.recalculatePart: newPart == -1  x=" + var2 + " z=" + var3 + "  " + obj);
         } else {
            obj.setPart(var4);
         }
      }
   }

   public final int computePart(int oldPart, int x, int z) {
      if(oldPart != -1) {
         if(this.rooms[oldPart].isPointOnMesh(x, z) != -1) {
            return oldPart;
         }

         Room[] var5 = this.neighbours[oldPart];

         for(int var4 = 0; var4 < var5.length; ++var4) {
            if(var5[var4].isPointOnMesh(x, z) != -1) {
               return var5[var4].getId();
            }
         }
      }

      for(oldPart = 0; oldPart < this.rooms.length; ++oldPart) {
         if(this.rooms[oldPart].isPointOnMesh(x, z) != -1) {
            return oldPart;
         }
      }

      return -1;
   }
}
