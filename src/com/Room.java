package com;

import java.io.DataInputStream;
import java.io.InputStream;
import java.util.Vector;

public class Room {

	private final int id;
	private Mesh mesh;
	private final int minx;
	private final int maxx;
	private final int minz;
	private final int maxz;
	private boolean openSky;
	private Portal[] portals;
	private int x1;
	private int y1;
	private int x2;
	private int y2;

	public Room(Mesh mesh, int id) {
		this.mesh = mesh;
		this.id = id;
		this.minx = mesh.minX();
		this.maxx = mesh.maxX();
		this.minz = mesh.minZ();
		this.maxz = mesh.maxZ();
		id = (this.maxx + this.minx) / 2;
		int var3 = (this.maxz + this.minz) / 2;
		this.openSky = true;
		RenderObject[] var6 = mesh.getPoligons();

		for(int var4 = 0; var4 < var6.length; ++var4) {
			RenderObject var5 = var6[var4];
			if(isPointOnPolygon(id, var3, var5) && var5.ny > 2048) {
				this.openSky = false;
			}
		}

	}

	public final void destroy() {
		this.mesh.destroy();
		this.mesh = null;

		for(int var1 = 0; var1 < this.portals.length; ++var1) {
			this.portals[var1].destroy();
			this.portals[var1] = null;
		}

		this.portals = null;
	}

	public final void setPortals(Portal[] portals) {
		this.portals = portals;
	}

	public final Portal[] getPortals() {
		return this.portals;
	}

	public final Mesh getMesh() {
		return this.mesh;
	}

	public final int getId() {
		return this.id;
	}

	public final boolean isOpenSky() {
		return this.openSky;
	}

	public final void setViewport(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public final void render(Graphics3D g3d) {
		g3d.transformAndProjectVertices(this.mesh, g3d.getInvCamera());
		g3d.addMesh(this.mesh, this.x1, this.y1, this.x2, this.y2);
	}

	public final void render(Graphics3D g3d, Vector roomObjects) {
		for(int var3 = 0; var3 < roomObjects.size(); ++var3) {
			RoomObject var4;
			if((var4 = (RoomObject) roomObjects.elementAt(var3)).getPart() == this.id) {
				var4.render(g3d, this.x1, this.y1, this.x2, this.y2);
			}
		}

	}

	/* void rayCast(Ray ray)
	 * и из RayCast
	 * public static boolean isRayAABBCollision(Ray ray, int minx, int maxx, int minz, int maxz)
	 */
	public final void rayCast(Ray ray) {
		Vector3D var2 = ray.getStart();
		Vector3D var3 = ray.getDir();
		int var4 = Math.min(var2.x, var2.x + var3.x);
		int var5 = Math.min(var2.z, var2.z + var3.z);
		int var6 = Math.max(var2.x, var2.x + var3.x);
		int var7 = Math.max(var2.z, var2.z + var3.z);
		if(var4 <= this.maxx && var5 <= this.maxz && var6 >= this.minx && var7 >= this.minz) {
			RayCast.rayCast(this.mesh, ray);
		}
	}

	/* boolean sphereCast(Vector3D pos, int rad)
	 * и из SphereCast
	 * public static boolean isSphereAABBCollision(Vector3D pos, int rad, int minx, int maxx, int minz, int maxz)
	 */
	public final boolean sphereCast(Vector3D pos, int rad) {
		return pos.x + rad >= this.minx && pos.z + rad >= this.minz && pos.x - rad <= this.maxx && pos.z - rad <= this.maxz ? SphereCast.sphereCast(this.mesh, pos, rad) : false;
	}

	// ???
	public final int a_int_sub(int x, int y, int z) {
		return x >= this.minx && z >= this.minz && x <= this.maxx && z <= this.maxz ? Scene.isPointOnMesh(this.mesh, x, y, z) : Integer.MAX_VALUE;
	}

	public final int isPointOnMesh(int x, int z) {
		if(x >= this.minx && z >= this.minz && x <= this.maxx && z <= this.maxz) {
			RenderObject[] var3 = this.mesh.getPoligons();

			for(int var4 = 0; var4 < var3.length; ++var4) {
				if(isPointOnPolygon(x, z, var3[var4])) {
					return var4;
				}
			}

			return -1;
		} else {
			return -1;
		}
	}

	private static boolean isPointOnPolygon(int x, int z, RenderObject obj) {
		Vertex var3;
		Vertex var4;
		Vertex var8;
		if(obj instanceof Polygon3V) {
			Polygon3V var9 = (Polygon3V) obj;
			short var10 = var9.ny;
			var4 = var9.c;
			var3 = var9.b;
			var8 = var9.a;
			return var10 > 0 ? MathUtils.isPointOnPolygon(x, z, var8.x, var8.z, var3.x, var3.z, var4.x, var4.z) : (var10 < 0 ? MathUtils.isPointOnPolygon(x, z, var4.x, var4.z, var3.x, var3.z, var8.x, var8.z) : false);
		} else if(obj instanceof Polygon4V) {
			Polygon4V var7 = (Polygon4V) obj;
			short var6 = var7.ny;
			Vertex var5 = var7.d;
			var4 = var7.c;
			var3 = var7.b;
			var8 = var7.a;
			return var6 > 0 ? MathUtils.isPointOnPolygon(x, z, var8.x, var8.z, var3.x, var3.z, var4.x, var4.z, var5.x, var5.z) : (var6 < 0 ? MathUtils.isPointOnPolygon(x, z, var5.x, var5.z, var4.x, var4.z, var3.x, var3.z, var8.x, var8.z) : false);
		} else {
			return false;
		}
	}

	public final int getMinX() {
		return this.minx;
	}

	public final int getMinZ() {
		return this.minz;
	}

	public final int getMaxX() {
		return this.maxx;
	}

	public final int getMaxZ() {
		return this.maxz;
	}

	//public aq() {}
	public static Mesh loadMesh(String file, float scaleX, float scaleY, float scaleZ) {
		Mesh var12 = null;
		InputStream var13 = null;
		DataInputStream var14 = null;

		try {
			var13 = (new Object()).getClass().getResourceAsStream(file);
			var14 = new DataInputStream(var13);
			var12 = createFrom3d(file, var14, 1.0F, 1.0F, 1.0F);
		} catch(Exception var10) {
			System.err.println("ERROR in Loader.Load: " + var10);
		} finally {
			try {
				var14.close();
				var13.close();
			} catch(Exception var9) {
				;
			}

		}

		return var12;
	}

	public static Mesh[] loadMeshes(String file, float scaleX, float scaleY, float scaleZ) {
		Mesh[] var4 = null;
		InputStream var5 = null;
		DataInputStream var6 = null;

		try {
			var5 = (new Object()).getClass().getResourceAsStream(file);
			var4 = new Mesh[(var6 = new DataInputStream(var5)).readInt()];

			for(int var7 = 0; var7 < var4.length; ++var7) {
				var4[var7] = createFrom3d(file, var6, scaleX, scaleY, scaleZ);
			}
		} catch(Exception var14) {
			System.err.println("ERROR in Loader.Load: " + var14);
		} finally {
			try {
				var6.close();
				var5.close();
			} catch(Exception var13) {
				;
			}

		}

		return var4;
	}

	private static Mesh createFrom3d(String file, DataInputStream is, float scaleX, float scaleY, float scaleZ) throws Exception {
		Vertex[] var5 = new Vertex[is.readShort()];

		int var7;
		int var8;
		int var9;
		for(int var6 = 0; var6 < var5.length; ++var6) {
			var7 = (int) ((float) is.readShort() * scaleX);
			var8 = (int) ((float) is.readShort() * scaleY);
			var9 = (int) ((float) is.readShort() * scaleZ);
			var5[var6] = new Vertex(var7, var8, var9);
		}

		Polygon3V[] var27 = new Polygon3V[is.readShort()];

		byte var10;
		byte var11;
		byte var12;
		byte var13;
		short var18;
		short var31;
		for(var7 = 0; var7 < var27.length; ++var7) {
			short var29 = is.readShort();
			var31 = is.readShort();
			var18 = is.readShort();
			byte var21 = is.readByte();
			byte var24 = is.readByte();
			var10 = is.readByte();
			var11 = is.readByte();
			var12 = is.readByte();
			var13 = is.readByte();
			Vertex var14 = var5[var29];
			Vertex var15 = var5[var31];
			Vertex var16 = var5[var18];
			var27[var7] = new Polygon3V(var16, var15, var14, var12, var13, var10, var11, var21, var24);
		}

		Polygon4V[] var28 = new Polygon4V[is.readShort()];

		for(var8 = 0; var8 < var28.length; ++var8) {
			var31 = is.readShort();
			var18 = is.readShort();
			short var22 = is.readShort();
			short var25 = is.readShort();
			var10 = is.readByte();
			var11 = is.readByte();
			var12 = is.readByte();
			var13 = is.readByte();
			byte var34 = is.readByte();
			byte var33 = is.readByte();
			byte var35 = is.readByte();
			byte var17 = is.readByte();
			Vertex var32 = var5[var31];
			Vertex var19 = var5[var18];
			Vertex var23 = var5[var22];
			Vertex var26 = var5[var25];
			var28[var8] = new Polygon4V(var26, var23, var19, var32, var35, var17, var34, var33, var12, var13, var10, var11);
		}

		RenderObject[] var30 = new RenderObject[var27.length + var28.length];
		var9 = 0;

		int var20;
		for(var20 = 0; var20 < var27.length; ++var20) {
			var30[var9] = var27[var20];
			++var9;
		}

		for(var20 = 0; var20 < var28.length; ++var20) {
			var30[var9] = var28[var20];
			++var9;
		}

		System.out.println("Mesh [" + file + "] вершин: " + var5.length + " полигонов: " + var30.length);
		return new Mesh(var5, var30);
	}
}
