package com;

import java.util.Vector;
import javax.microedition.lcdui.Canvas;

public class HouseCreator {

	//public l() {}
	public static House create(MeshData[] meshes) {
		
		Room[] rooms = new Room[meshes.length - 1]; // rooms
		Vector[] roomPortals = new Vector[rooms.length];

		// создание комнат
		for(int i = 0; i < rooms.length; ++i) {
			MeshData mesh = meshes[i];
			mesh.calculateNormals();
			rooms[i] = new Room(mesh, i);
			roomPortals[i] = new Vector();
		}
		
		MeshData portalsMesh = meshes[meshes.length - 1];
		float meshScale = portalsMesh.getScale();
		
		short[] verts = portalsMesh.getVertices();
		short[] p4vs = portalsMesh.get4VPols(), p3vs = portalsMesh.get3VPols();
		
		int p4vsCount = p4vs.length / 4, p3vsCount = p3vs.length / 3;
		Portal[] portals = new Portal[p4vsCount + p3vsCount];
		System.out.println(portals.length + " portals");
		
		for(int i = 0; i < portals.length; i++) {
			Vector3D[] portalVerts;

			if(i < p3vsCount) {
				portalVerts = new Vector3D[] {
					new Vector3D(verts, p3vs[i * 3], meshScale), 
					new Vector3D(verts, p3vs[i * 3 + 1], meshScale), 
					new Vector3D(verts, p3vs[i * 3 + 2], meshScale)
				};
			} else {
				int tmpI = i - p3vsCount;
				portalVerts = new Vector3D[] {
					new Vector3D(verts, p4vs[tmpI * 4], meshScale), 
					new Vector3D(verts, p4vs[tmpI * 4 + 1], meshScale), 
					new Vector3D(verts, p4vs[tmpI * 4 + 2], meshScale),
					new Vector3D(verts, p4vs[tmpI * 4 + 3], meshScale)
				};
			}
			
			Room room1 = null, room2 = null;
			
			for(int t=0; t<rooms.length; t++) {
				Room room = rooms[t];
				
				if(isExistsCommonCoords(room.getMesh(), portalVerts)) {
					if(room1 == null) room1 = room;
					else if(room2 == null) room2 = room;
					else break;
				}
			}
			
			Portal p = new Portal(portalVerts);
			p.setRooms(room1, room2);
			portals[i] = p;
			
			if(room1 != null) roomPortals[room1.getId()].addElement(p);
			if(room2 != null) roomPortals[room2.getId()].addElement(p);
		}
		
		for(int i=0; i<rooms.length; i++) {
			Vector portalsVec = roomPortals[i];
			Portal[] tmp = new Portal[portalsVec.size()];
			portalsVec.copyInto(tmp);
			
			rooms[i].setPortals(tmp);
		}

		countPortals(rooms);
		Room[][] neighbours = createNeighbours(rooms);
		return new House(rooms, neighbours, portals);
	}

	private static void countPortals(Room[] rooms) {
		int disconnectedPortals = 0;
		int disconnectedRooms = 0;

		for(int i = 0; i < rooms.length; ++i) {
			Portal[] portals = rooms[i].getPortals();
			if(portals == null || portals.length == 0) {
				++disconnectedRooms;
			}

			for(int t = 0; t < portals.length; ++t) {
				Portal p = portals[t];
				if(p.getRoom1() == null || p.getRoom2() == null) {
					++disconnectedPortals;
				}
			}
		}

		if(disconnectedPortals > 0) {
			System.out.println("HouseCreator: " + disconnectedPortals + " порталам не найдены комнаты");
		}

		if(disconnectedRooms > 0) {
			System.out.println("HouseCreator: " + disconnectedRooms + " комнатам не найдены порталы");
		}

	}

	private static Room[][] createNeighbours(Room[] rooms) {
		Room[][] neighbours = new Room[rooms.length][];

		for(int i = 0; i < neighbours.length; ++i) {
			Room room = rooms[i];
			
			Portal[] portals = room.getPortals();
			Vector nearRooms = new Vector();

			for(int t = 0; t < portals.length; ++t) {
				Room room1 = portals[t].getRoom1();
				Room room2 = portals[t].getRoom2();
				
				if(room1 != room && room1 != null && !nearRooms.contains(room1)) {
					nearRooms.addElement(room1);
				}
				
				if(room2 != room && room2 != null && !nearRooms.contains(room1)) {
					nearRooms.addElement(room2);
				}
			}

			neighbours[i] = new Room[nearRooms.size()];
			nearRooms.copyInto(neighbours[i]);
		}

		return neighbours;
	}

	private static boolean isExistsCommonCoords(MeshData mesh, Vector3D[] poly) {
		int var2 = 0;

		while(var2 < poly.length) {
			Vector3D v = poly[var2];
			short[] verts = mesh.getVertices();
			int vertsCount = verts.length / 3;
			float meshScale = mesh.getScale();
			int var5 = 0;

			while(true) {
				boolean hit;
				if(var5 < vertsCount) {
					Vector3D v2 = new Vector3D(verts, var5, meshScale);
					if(v2.x / 50 != v.x / 50 || v2.y / 50 != v.y / 50 || v2.z / 50 != v.z / 50) {
						var5++;
						continue;
					}

					hit = true;
				} else {
					hit = false;
				}

				if(hit) {
					return true;
				}

				++var2;
				break;
			}
		}

		return false;
	}
}
