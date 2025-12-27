package com;

import java.util.Vector;

public class Room {

	private final int id;
	private MeshData mesh;
	private final int minx, maxx, miny, maxy, minz, maxz; //room size
	private boolean openSky;
	
	private Portal[] portals;
	private int x1, y1, x2, y2; //viewport

	public Room(MeshData mesh, int id) {
		this.mesh = mesh;
		this.id = id;

		Vector3D min = mesh.getAABBMin(), max = mesh.getAABBMax();
		
		this.minx = min.x;
		this.maxx = max.x;
		this.miny = min.y;
		this.maxy = max.y;
		this.minz = min.z;
		this.maxz = max.z;

		int centerX = (this.maxx + this.minx) / 2;
		int centerZ = (this.maxz + this.minz) / 2;
		this.openSky = true;
		
		Ray ray = new Ray();
		ray.start.set(centerX, miny - 1, centerZ);
		ray.dir.set(0, (maxy - miny) + 1, 0);
		RayCast.rayCast(mesh, ray);

		if(ray.collision && ray.normal.y > 2048) openSky = false;
	}

	public final void destroy() {
		this.mesh.destroy();
		this.mesh = null;
		this.portals = null;
	}

	public final void setPortals(Portal[] portals) {
		this.portals = portals;
	}

	public final Portal[] getPortals() {
		return this.portals;
	}

	public final MeshData getMesh() {
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

	public final void addViewport(int x1, int y1, int x2, int y2) {
		if(x1 < this.x1) this.x1 = x1;
		if(y1 < this.y1) this.y1 = y1;
		if(x2 > this.x2) this.x2 = x2;
		if(y2 > this.y2) this.y2 = y2;
	}

	public final void renderRoom(Renderer g3d) {
		g3d.addMesh(mesh.getM3GMesh(), null, null);
	}

	public final void renderObjects(Renderer g3d, Vector roomObjects) {
		for(int i = 0; i < roomObjects.size(); ++i) {
			RoomObject obj = (RoomObject) roomObjects.elementAt(i);
			if(obj.getPart() == this.id) {
				obj.render(g3d);
			}
		}
	}

	/* boolean sphereCast(Vector3D pos, int rad)
	 * и из SphereCast
	 * public static boolean isSphereAABBCollision(Vector3D pos, int rad, int minx, int maxx, int minz, int maxz)
	 */
	public final boolean sphereCast(Vector3D pos, int rad) {
		if(pos.x + rad >= minx && 
			pos.y + rad >= miny && 
			pos.z + rad >= minz && 
			pos.x - rad <= maxx && 
			pos.y - rad <= maxy && 
			pos.z - rad <= maxz) {
			
			return SphereCast.sphereCast(this.mesh, pos, rad);
		}
		
		return false;
	}
	
	public final void rayCast(Ray ray) {
		if(RayCast.isRayAABBCollision(ray, minx, maxx, miny, maxy, minz, maxz)) {
			RayCast.rayCast(mesh, ray);
		}
	}

	public final int getMinX() {
		return this.minx;
	}

	public final int getMaxX() {
		return this.maxx;
	}

	public final int getMinY() {
		return this.miny;
	}

	public final int getMaxY() {
		return this.maxy;
	}

	public final int getMinZ() {
		return this.minz;
	}

	public final int getMaxZ() {
		return this.maxz;
	}

	public final int getViewportMinX() {
		return this.x1;
	}

	public final int getViewportMinY() {
		return this.y1;
	}

	public final int getViewportMaxX() {
		return this.x2;
	}

	public final int getViewportMaxY() {
		return this.y2;
	}

	public final boolean viewportContains(int x1, int y1, int x2, int y2) {
		return x1 >= this.x1 && y1 >= this.y1 && x2 <= this.x2 && y2 <= this.y2;
	}
	//public aq() {}
}
