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
		
		short[] pols = portalsMesh.getPols();
		
		int quadsCount = portalsMesh.getQuadsCount();
		int trisCount = portalsMesh.getTrisCount();
		
		Portal[] portals = new Portal[quadsCount + trisCount];
		System.out.println(portals.length + " portals");
		
		for(int i = 0; i < portals.length; i++) {
			Vector3D[] portalVerts;

			if(i < quadsCount) {
				int tmpI = i * 4; 
				
				portalVerts = new Vector3D[] {
					portalsMesh.getVertex(pols[tmpI], true),
					portalsMesh.getVertex(pols[tmpI + 1], true),
					portalsMesh.getVertex(pols[tmpI + 2], true),
					portalsMesh.getVertex(pols[tmpI + 3], true)
				};
			} else {
				int tmpI = (quadsCount * 4) + (i - quadsCount) * 3;
				
				portalVerts = new Vector3D[] {
					portalsMesh.getVertex(pols[tmpI], true),
					portalsMesh.getVertex(pols[tmpI + 1], true),
					portalsMesh.getVertex(pols[tmpI + 2], true)
				};
			}
			
			Room room1 = null, room2 = null;
			int room1CommonVtx = 0, room2CommonVtx = 0;
			
			for(int t=0; t<rooms.length; t++) {
				Room room = rooms[t];
				
				int vtxCommon = countCommonVtx(room.getMesh(), portalVerts);
				
				if(vtxCommon > 0) {
					if(vtxCommon > room1CommonVtx) {
						room2 = room1;
						room2CommonVtx = room1CommonVtx;
						
						room1 = room;
						room1CommonVtx = vtxCommon;
					} else if(vtxCommon > room2CommonVtx) {
						room2 = room;
						room2CommonVtx = vtxCommon;
					}
				}
			}
			
			Portal p = new Portal(portalVerts);
			portals[i] = p;
			
			setPortalRooms(p, room1, room2);
			
			if(room1 != null) roomPortals[room1.getId()].addElement(p);
			if(room2 != null) roomPortals[room2.getId()].addElement(p);
		}
		
		for(int i=0; i<rooms.length; i++) {
			Vector portalsVec = roomPortals[i];
			Portal[] tmp = new Portal[portalsVec.size()];
			portalsVec.copyInto(tmp);
			
			rooms[i].setPortals(tmp);
		}

		countPortals(rooms, portals);
		Room[][] neighbours = createNeighbours(rooms);
		return new House(rooms, neighbours, portals);
	}

	private static void countPortals(Room[] rooms, Portal[] portals) {
		int disconnectedPortals = 0;
		int disconnectedRooms = 0;

		for(int i = 0; i < rooms.length; ++i) {
			Portal[] roomPortals = rooms[i].getPortals();
			if(roomPortals == null || roomPortals.length == 0) {
				++disconnectedRooms;
			}
		}

		for(int t = 0; t < portals.length; ++t) {
			Portal p = portals[t];
			if(p.getRoomFront() == null || p.getRoomBack() == null) {
				++disconnectedPortals;
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
				Room room1 = portals[t].getRoomFront();
				Room room2 = portals[t].getRoomBack();
				
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

	private static int countCommonVtx(MeshData mesh, Vector3D[] portalVerts) {
		//AABB check
		Vector3D aabbMin = mesh.getAABBMin();
		Vector3D aabbMax = mesh.getAABBMax();
		
		//Check if at least one portal vertex is inside AABB
		for(int pVtx = 0; pVtx < portalVerts.length; pVtx++) {
			Vector3D v = portalVerts[pVtx];
			
			if(
				v.x < aabbMin.x - 50 || v.y < aabbMin.y - 50 || v.z < aabbMin.z - 50 ||
				v.x > aabbMax.x + 50 || v.y > aabbMax.y + 50 || v.z > aabbMax.z + 50
			) {
				if(pVtx == portalVerts.length - 1) return 0;
			} else {
				break;
			}
		}
		
		//Count common vertices
		int commonVertsBitmask = 0;
		int commonVerts = 0;
		
		Vector3D tmp = new Vector3D();
		int meshVertsCount = mesh.getVerts().length / 3;
			
		for(int meshVtx = 0; meshVtx < meshVertsCount; meshVtx++) {
			Vector3D v = mesh.getVertex(meshVtx, true);
			
			for(int pVtx = 0; pVtx < portalVerts.length; pVtx++) {
				//Skip already counted portal verts
				if((commonVertsBitmask & (1 << pVtx)) != 0) continue;
				
				Vector3D v2 = portalVerts[pVtx];
				
				tmp.set(v);
				tmp.sub(v2);
				
				if(Math.abs(tmp.x) <= 50 && Math.abs(tmp.y) <= 50 && Math.abs(tmp.z) <= 50) {
					commonVertsBitmask |= 1 << pVtx;
					commonVerts++;
					if(commonVerts == portalVerts.length) return commonVerts;
				}
			}
		}

		return commonVerts;
	}
	
	private static void setPortalRooms(Portal p, Room room1, Room room2) {
		if(room1 == null && room2 == null) return;
		/*Vector3D pCenter = p.getCenter();
		Vector3D pNormal = p.getNormal();
		
		int res = calculateRoomPortalDirection(room1, pCenter, pNormal);
		int room1Front = res & 0xff, room1Back = (res >> 8) & 0xff;
		
		res = calculateRoomPortalDirection(room2, pCenter, pNormal);
		int room2Front = res & 0xff, room2Back = (res >> 8) & 0xff;
		
		if(Math.max(room1Front, room1Back) > Math.max(room2Front, room2Back)) {
			if(room1Back > room1Front) {
				Room tmp = room2;
				room2 = room1;
				room1 = tmp;
			}
		} else {
			if(room2Front > room2Back) {
				Room tmp = room2;
				room2 = room1;
				room1 = tmp;
			}
		}*/
		
		Vector3D tmpVec = new Vector3D();
		float room1Size = Float.POSITIVE_INFINITY, room2Size = Float.POSITIVE_INFINITY;
		
		if(room1 != null) {
			tmpVec.set(room1.getMesh().getAABBMax());
			tmpVec.sub(room1.getMesh().getAABBMin());
			room1Size = (float) tmpVec.x * tmpVec.y * tmpVec.z;
		}
		
		if(room2 != null) {
			tmpVec.set(room2.getMesh().getAABBMax());
			tmpVec.sub(room2.getMesh().getAABBMin());
			room2Size = (float) tmpVec.x * tmpVec.y * tmpVec.z;
		}
		
		if(room1Size < room2Size) {
			tmpVec.set(room1.getMesh().getAABBMin());
			tmpVec.add(room1.getMesh().getAABBMax());
			tmpVec.x /= 2;
			tmpVec.y /= 2;
			tmpVec.z /= 2;
			
			tmpVec.sub(p.getCenter());
			int dot = tmpVec.dot(p.getNormal());
			
			//Make first room back room
			if(dot < 0) {
				Room tmp = room2;
				room2 = room1;
				room1 = tmp;
			}
		} else {
			tmpVec.set(room2.getMesh().getAABBMin());
			tmpVec.add(room2.getMesh().getAABBMax());
			tmpVec.x /= 2;
			tmpVec.y /= 2;
			tmpVec.z /= 2;
			
			tmpVec.sub(p.getCenter());
			int dot = tmpVec.dot(p.getNormal());
			
			//Make second room front room
			if(dot > 0) {
				Room tmp = room2;
				room2 = room1;
				room1 = tmp;
			}
		}
		
		p.setRooms(room1, room2);
	}
	
	/*private static int calculateRoomPortalDirection(Room room, Vector3D pCenter, Vector3D pNormal) {
		if(room == null) return 0;
		
		//Select 8 points at the room border and one point at the room center
		//And check which points are in front of the portal or at the back
		Vector3D aabbMin = room.getMesh().getAABBMin();
		Vector3D aabbMax = room.getMesh().getAABBMax();
		
		Vector3D tmpVec = new Vector3D();
		int roomFrontVtx = 0, roomBackVtx = 0;
		
		for(int i = 0; i < 9; i++) {
			tmpVec.set(
				(((i & 1) > 0) ? aabbMax : aabbMin).x,
				(((i & 2) > 0) ? aabbMax : aabbMin).y, 
				(((i & 4) > 0) ? aabbMax : aabbMin).z
				);
			
			if(i == 8) {
				tmpVec.set(aabbMin);
				tmpVec.add(aabbMax);
				tmpVec.x /= 2;
				tmpVec.y /= 2;
				tmpVec.z /= 2;
			}
			
			tmpVec.sub(pCenter);
			int dot = tmpVec.dot(pNormal);
			
			if(dot > 0) roomFrontVtx++;
			else if(dot < 0) roomBackVtx++;
		}
		
		return roomFrontVtx | (roomBackVtx << 8);
	}*/
}
