package com;

import java.util.Vector;
import javax.microedition.lcdui.Canvas;

public class HouseCreator {

	public final int codeLeft;
	public final int codeRight;
	public final int codeUp;
	public final int codeDown;
	public final int codeCentre;
	public final int code7;
	public final int code9;
	private boolean keyUp;
	private boolean keyDown;
	private boolean keyLeft;
	private boolean keyRight;
	private boolean keyCentre;
	private boolean key7;
	private boolean key9;

	//public l() {}
	public static House create(Mesh[] meshes) {
		RenderObject[] var1 = meshes[meshes.length - 1].getPoligons(); // meshPortals
		Room[] var2 = new Room[meshes.length - 1]; // rooms

		// создание комнат
		int var3;
		for(var3 = 0; var3 < var2.length; ++var3) {
			var2[var3] = new Room(meshes[var3], var3);
		}

		for(var3 = 0; var3 < var2.length; ++var3) {
			Room var26;
			Room var10000 = var26 = var2[var3];
			RenderObject[] var5 = var1;
			Room var4 = var10000;
			Vector var6 = new Vector();

			for(int var7 = 0; var7 < var5.length; ++var7) {
				RenderObject var9;
				Vertex[] var28;
				if((var9 = var5[var7]) instanceof Polygon3V) {
					Polygon3V var10 = (Polygon3V) var9;
					var28 = new Vertex[]{var10.a, var10.b, var10.c};
				} else if(var9 instanceof Polygon4V) {
					Polygon4V var35 = (Polygon4V) var9;
					var28 = new Vertex[]{var35.a, var35.b, var35.c, var35.d};
				} else {
					var28 = null;
				}

				Vertex[] var8 = var28;
				if(isExistsCommonCoords(var4.getMesh(), var8)) {
					Mesh var36 = var4.getMesh();
					Vector3D var34 = computeCentre(var36.getVertices());
					Vector3D var11 = computeCentre(var8);
					Vertex var27 = var8[0];
					Vertex var10001 = var8[1];
					Vertex var31 = var8[2];
					Vertex var13 = var10001;
					Vertex var12 = var27;
					long var18 = (long) (var27.y - var13.y) * (long) (var12.z - var31.z) - (long) (var12.z - var13.z) * (long) (var12.y - var31.y);
					long var20 = (long) (var12.z - var13.z) * (long) (var12.x - var31.x) - (long) (var12.x - var13.x) * (long) (var12.z - var31.z);
					long var22 = (long) (var12.x - var13.x) * (long) (var12.y - var31.y) - (long) (var12.y - var13.y) * (long) (var12.x - var31.x);
					double var24 = Math.sqrt((double) (var18 * var18 + var20 * var20 + var22 * var22)) / 4096.0D;
					int var33 = (int) ((double) var18 / var24);
					int var37 = (int) ((double) var20 / var24);
					int var38 = (int) ((double) var22 / var24);
					Vector3D var32 = new Vector3D(var33, var37, var38);
					var34.x -= var11.x;
					var34.y -= var11.y;
					var34.z -= var11.z;
					if(var32.dot(var34) >= 0) {
						reverse(var8);
					}

					var6.addElement(new Portal(var8));
				}
			}

			Portal[] var30 = new Portal[var6.size()];
			var6.copyInto(var30);
			var4.setPortals(var30);
			findRooms(var26, var2);
		}

		countPortals(var2);
		Room[][] var29 = createNeighbours(var2);
		return new House(var2, var29);
	}

	private static void countPortals(Room[] rooms) {
		int var1 = 0;
		int var2 = 0;

		for(int var3 = 0; var3 < rooms.length; ++var3) {
			Portal[] var4;
			if((var4 = rooms[var3].getPortals()) == null || var4.length == 0) {
				++var2;
			}

			for(int var5 = 0; var5 < var4.length; ++var5) {
				if(var4[var5].getRoom() == null) {
					++var1;
				}
			}
		}

		if(var1 > 0) {
			System.out.println("HouseCreator: " + var1 + " порталам не найдены комнаты");
		}

		if(var2 > 0) {
			System.out.println("HouseCreator: " + var2 + " комнатам не найдены порталы");
		}

	}

	// из MeshImage: private static Vertex computeCentre(Vertex[] vertices) {
	private static Vector3D computeCentre(Vertex[] vertices) {
		long var1 = 0L;
		long var3 = 0L;
		long var5 = 0L;

		for(int var7 = 0; var7 < vertices.length; ++var7) {
			Vertex var8 = vertices[var7];
			var1 += (long) var8.x;
			var3 += (long) var8.y;
			var5 += (long) var8.z;
		}

		var1 /= (long) vertices.length;
		var3 /= (long) vertices.length;
		var5 /= (long) vertices.length;
		return new Vector3D((int) var1, (int) var3, (int) var5);
	}

	private static void reverse(Vertex[] vertices) {
		Vertex[] var1 = new Vertex[vertices.length];

		for(int var2 = 0; var2 < var1.length; ++var2) {
			var1[var2] = vertices[vertices.length - 1 - var2];
		}

		System.arraycopy(var1, 0, vertices, 0, var1.length);
	}

	private static void findRooms(Room room, Room[] rooms) {
		Portal[] var2 = room.getPortals();
		int var3 = 0;

		while(var3 < var2.length) {
			Portal var4 = var2[var3];
			int var5 = 0;

			while(true) {
				if(var5 < rooms.length) {
					Room var6;
					if((var6 = rooms[var5]) == room || !isExistsCommonCoords(var6.getMesh(), var4.getVertices())) {
						++var5;
						continue;
					}

					var4.setRoom(var6);
				}

				++var3;
				break;
			}
		}

	}

	private static Room[][] createNeighbours(Room[] rooms) {
		Room[][] var1 = new Room[rooms.length][];

		for(int var2 = 0; var2 < var1.length; ++var2) {
			Portal[] var3 = rooms[var2].getPortals();
			Vector var4 = new Vector();

			for(int var5 = 0; var5 < var3.length; ++var5) {
				Room var6 = var3[var5].getRoom();
				if(!var4.contains(var6)) {
					var4.addElement(var6);
				}
			}

			var1[var2] = new Room[var4.size()];
			var4.copyInto(var1[var2]);
		}

		return var1;
	}

	private static boolean isExistsCommonCoords(Mesh mesh, Vertex[] poly) {
		int var2 = 0;

		while(var2 < poly.length) {
			Vertex var4 = poly[var2];
			Vertex[] var3 = mesh.getVertices();
			int var5 = 0;

			while(true) {
				boolean var10000;
				if(var5 < var3.length) {
					Vertex var6;
					if((var6 = var3[var5]).x / 50 != var4.x / 50 || var6.y / 50 != var4.y / 50 || var6.z / 50 != var4.z / 50) {
						++var5;
						continue;
					}

					var10000 = true;
				} else {
					var10000 = false;
				}

				if(var10000) {
					return true;
				}

				++var2;
				break;
			}
		}

		return false;
	}

	public HouseCreator(Canvas canvas) {
		if(select(canvas, -26)) {
			this.codeLeft = -61;
			this.codeRight = -62;
			this.codeDown = -60;
			this.codeUp = -59;
			this.codeCentre = -26;
			this.code7 = -1;
			this.code9 = -4;
		} else if(select(canvas, -20)) {
			this.codeLeft = -2;
			this.codeRight = -5;
			this.codeDown = -6;
			this.codeUp = -1;
			this.codeCentre = -20;
			this.code7 = -21;
			this.code9 = -22;
		} else {
			this.codeLeft = -3;
			this.codeRight = -4;
			this.codeDown = -2;
			this.codeUp = -1;
			this.codeCentre = -5;
			this.code7 = -6;
			this.code9 = -7;
		}
	}

	private static boolean select(Canvas canvas, int keyCode) {
		try {
			return canvas.getKeyName(keyCode).toUpperCase().indexOf("SELECT") != -1;
		} catch(Exception var2) {
			return false;
		}
	}

	public void keyPressed(int key) {
		if(key == 50 || key == this.codeUp) {
			this.keyUp = true;
		}

		if(key == 56 || key == this.codeDown) {
			this.keyDown = true;
		}

		if(key == 52 || key == this.codeLeft) {
			this.keyLeft = true;
		}

		if(key == 54 || key == this.codeRight) {
			this.keyRight = true;
		}

		if(key == 53 || key == this.codeCentre) {
			this.keyCentre = true;
		}

		if(key == 55) {
			this.key7 = true;
		}

		if(key == 57) {
			this.key9 = true;
		}

	}

	public void keyReleased(int key) {
		if(key == 50 || key == this.codeUp) {
			this.keyUp = false;
		}

		if(key == 56 || key == this.codeDown) {
			this.keyDown = false;
		}

		if(key == 52 || key == this.codeLeft) {
			this.keyLeft = false;
		}

		if(key == 54 || key == this.codeRight) {
			this.keyRight = false;
		}

		if(key == 53 || key == this.codeCentre) {
			this.keyCentre = false;
		}

		if(key == 55) {
			this.key7 = false;
		}

		if(key == 57) {
			this.key9 = false;
		}

	}

	public boolean keyUp() {
		return this.keyUp;
	}

	public boolean keyDown() {
		return this.keyDown;
	}

	public boolean keyLeft() {
		return this.keyLeft;
	}

	public boolean keyRight() {
		return this.keyRight;
	}

	public boolean keyCentre() {
		return this.keyCentre;
	}

	public boolean key7() {
		return this.key7;
	}

	public boolean key9() {
		return this.key9;
	}
}
