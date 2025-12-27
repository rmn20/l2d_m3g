package com;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public final class House {

	private Room[] rooms;
	private Room[][] neighbours;
	private Portal[] portals;
	private Vector nearRooms = new Vector(); // ? или renderedRooms
	private Vector reachedPortals = new Vector();
	private Skybox skybox;
	private Vector objects = new Vector(); // из Room
	
	private final Ray ray = new Ray();

	public House(Room[] rooms, Room[][] neighbours, Portal[] portals) {
		this.rooms = rooms;
		this.neighbours = neighbours;
		this.portals = portals;
	}

	public final void destroy() {
		for(int i = 0; i < this.rooms.length; ++i) {
			this.rooms[i].destroy();
			this.rooms[i] = null;
		}
		for(int i = 0; i < this.portals.length; ++i) {
			this.portals[i].destroy();
			this.portals[i] = null;
		}

		this.rooms = null;
		this.neighbours = null;
		this.portals = null;
		
		this.nearRooms.removeAllElements();
		this.nearRooms = null;
		this.reachedPortals.removeAllElements();
		this.reachedPortals = null;
		
		if(this.skybox != null) {
			this.skybox.destroy();
			this.skybox = null;
		}
		
		this.objects = null;
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
			return nearRooms;
		} else {
			if(rooms[part] != null) {
				nearRooms.addElement(this.rooms[part]);
			}

			Room[] neigh = neighbours[part];

			for(int i = 0; i < neigh.length; ++i) {
				if(neigh[i] != null) {
					nearRooms.addElement(neigh[i]);
				}
			}

			return nearRooms;
		}
	}

	public final boolean sphereCast(int part, Vector3D pos, int rad) {
		Vector var7 = this.getNearRooms(part);
		boolean var4 = false;

		for(int var5 = 0; var5 < var7.size(); ++var5) {
			Room var6 = (Room) var7.elementAt(var5);
			var4 |= var6.sphereCast(pos, rad);
		}

		return var4;
	}

	public final void rayCast(int part, Ray ray) {
		Vector nearRooms = getNearRooms(part);
		
		for(int i = 0; i < nearRooms.size(); ++i) {
			Room room = (Room) nearRooms.elementAt(i);
			room.rayCast(ray);
		}
	}

	// ??? максимальное значение y дома (house), которое ниже точки (x,y,z)
	public final int getFloorY(int part, int x, int y, int z) {
		ray.reset();
		ray.start.set(x, y, z);
		
		Vector nearRooms = getNearRooms(part);

		for(int i = 0; i < nearRooms.size(); ++i) {
			Room room = (Room) nearRooms.elementAt(i);
			
			ray.dir.set(0, -(room.getMaxY() - room.getMinY()), 0);
			room.rayCast(ray);
		}
		
		if(ray.collision && ray.normal.y < 0) return ray.collisionPoint.y;
		return Integer.MAX_VALUE;
	}

	public final int render(Renderer g3d, int part, Vector3D camPos) {
		if(skybox != null) skybox.resetViewport();
		int rooms = 0;
		
		if(part != -1) {
			for(int i=0; i<reachedPortals.size(); i++) {
				Portal p = (Portal) reachedPortals.elementAt(i);
				p.resetViewport();
			}
			
			nearRooms.removeAllElements();
			reachedPortals.removeAllElements();
			render(g3d, this.rooms[part], null, 0, 0, g3d.getWidth(), g3d.getHeight());
			
			for(int i=0; i<nearRooms.size(); i++) {
				Room room = (Room) nearRooms.elementAt(i);
				
				g3d.setClip(
					room.getViewportMinX(), 
					room.getViewportMinY(), 
					room.getViewportMaxX(), 
					room.getViewportMaxY()
				);
				
				room.renderRoom(g3d);
				room.renderObjects(g3d, objects);
			}

			rooms = nearRooms.size();
		}

		if(skybox != null && skybox.isVisible()) {
			skybox.render(g3d, camPos);
		}

		return rooms;
	}

	private void render(Renderer g3d, Room mainRoom, Portal prevPortal, int x1, int y1, int x2, int y2) {
		if(mainRoom == null) return;
		
		if(nearRooms.contains(mainRoom)) {
			mainRoom.addViewport(x1, y1, x2, y2);
		} else {
			mainRoom.setViewport(x1, y1, x2, y2);
			nearRooms.addElement(mainRoom);
		}

		if(mainRoom.isOpenSky() && skybox != null) {
			skybox.addViewport(x1, y1, x2, y2);
		}

		Portal[] portals = mainRoom.getPortals();

		for(int i = 0; i < portals.length; ++i) {
			Portal portal = portals[i];
			if(portal == prevPortal) continue;
			
			if(!reachedPortals.contains(portal)) reachedPortals.addElement(portal);
			if(portal.getVisibleRoomId(g3d) == mainRoom.getId()) continue;
			if(!portal.isVisible(g3d, x1, y1, x2, y2)) continue;

			int minX = portal.getMinX();
			int minY = portal.getMinY();
			int maxX = portal.getMaxX();
			int maxY = portal.getMaxY();

			if(minX < x1) minX = x1;
			if(minY < y1) minY = y1;

			if(maxX > x2) maxX = x2;
			if(maxY > y2) maxY = y2;

			int sizeX = maxX - minX;
			int sizeY = maxY - minY;
			if((sizeX < 20 && sizeY < 20) || sizeX < 5 || sizeY < 5) continue;
			
			Room room = portal.getRoomFront();
			if(room == mainRoom) room = portal.getRoomBack();

			if(room != null) {
				if(nearRooms.contains(room) && room.viewportContains(minX, minY, maxX, maxY)) continue;
				render(g3d, room, portal, minX, minY, maxX, maxY);
			} else if(skybox != null) {
				skybox.addViewport(minX, minY, maxX, maxY);
			}
		}
	}

	public void debugRender(Graphics g, int x, int y) {
		int oldColor = g.getColor();
		
		for(int i = 0; i < reachedPortals.size(); i++) {
			Portal p = (Portal) reachedPortals.elementAt(i);
			if(!p.isViewportCalculated()) continue;

			Vector3D[] verts = p.getProjVertices();
			int projVertsCount = p.getProjVertsCount();

			for(int t = 0; t < projVertsCount; t++) {
				Vector3D v = verts[t];
				Vector3D v2 = verts[(t + 1) % projVertsCount];
				
				g.setColor(0xffff00);
				g.drawLine(v.x - 1 + x, v.y - 1 + y, v2.x - 1 + x, v2.y - 1 + y);
				g.setColor(0x00ff00);
				g.fillRect(v.x - 1 + x, v.y - 1 + y, 3, 3);
			}
		}
		
		for(int i = 0; i < nearRooms.size(); i++) {
			Room room = (Room) nearRooms.elementAt(i);

			g.setColor(0x00ff00);
			g.drawRect(
					room.getViewportMinX()+ x, room.getViewportMinY() + y,
					room.getViewportMaxX() - 1 - room.getViewportMinX(),
					room.getViewportMaxY() - 1 - room.getViewportMinY());
		}
		
		if(skybox != null && skybox.isVisible()) {
			g.setColor(0x00ffff);
			g.drawRect(
					skybox.getViewportMinX()+ x, skybox.getViewportMinY() + y,
					skybox.getViewportMaxX() - 1 - skybox.getViewportMinX(),
					skybox.getViewportMaxY() - 1 - skybox.getViewportMinY());
		}
		
		g.setColor(oldColor);
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
			int x = obj.getPosX();
			int z = obj.getPosZ();
			
			int oldPart = obj.getPart();
			int part = computePart(oldPart, x, z);
			
			if(part == -1) {
				System.out.println("House.recalculatePart: newPart == -1  x=" + x + " z=" + z + "  " + obj);
			} else {
				obj.setPart(part);
			}
		}
	}

	//todo disable back face culling
	public final int computePart(int oldPart, int x, int z) {
		if(oldPart != -1) {
			Room oldRoom = rooms[oldPart];
			
			ray.reset();
			ray.start.set(x, oldRoom.getMaxY() + 1, z);
			ray.dir.set(0, -(oldRoom.getMaxY() - oldRoom.getMinY() + 1), 0);
			
			oldRoom.rayCast(ray);
			if(ray.collision) return oldPart;

			int newPart = computePartRoomsList(neighbours[oldPart], x, z);
			if(newPart != -1) return newPart;
		}

		int newPart = computePartRoomsList(rooms, x, z);
		return newPart;
	}
	
	private final int computePartRoomsList(Room[] rooms, int x, int z) {
		int newPart = -1;
		int maxY = Integer.MIN_VALUE;
		
		for(int i = 0; i < rooms.length; i++) {
			Room room = rooms[i];
		
			ray.reset();
			ray.start.set(x, room.getMaxY() + 1, z);
			ray.dir.set(0, -(room.getMaxY() - room.getMinY() + 1), 0);
			
			room.rayCast(ray);
			
			if(ray.collision && ray.collisionPoint.y > maxY) {
				maxY = ray.collisionPoint.y;
				newPart = room.getId();
			}
		}
		
		return newPart;
	}
}
