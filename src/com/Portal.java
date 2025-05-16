package com;

import javax.microedition.m3g.Transform;

public final class Portal {
	private final static Vector3D tmpVec = new Vector3D();

	private Room roomFront, roomBack;
	
	private boolean resetView, resetVisibleRoom;
	private int minx, maxx, miny, maxy;
	private int visibleRoomId;
	
	private Vector3D[] verts;
	private Vector3D center, normal;
	
	private float[] vertsFlt, vertsTmp;
	private Vector3D[] projVerts;
	private int projVertsCount;

	public Portal(Vector3D[] verts) {
		this.verts = verts;
		if(verts.length != 4) {
			System.out.println("PORTAL: предупреждение: нестандартное количество вершин в портале " + verts.length);
		}
		
		Vector3D a = verts[0], b = verts[1], c = verts[2];
		normal = MathUtils.createNormal(a.x, a.y, a.z, b.x, b.y, b.z, c.x, c.y, c.z);
		center = new Vector3D();
		
		vertsFlt = new float[4 * verts.length];
		vertsTmp = new float[4 * verts.length];
		
		for(int i=0; i<verts.length; i++) {
			vertsFlt[i*4 + 0] = verts[i].x;
			vertsFlt[i*4 + 1] = verts[i].y;
			vertsFlt[i*4 + 2] = verts[i].z;
			vertsFlt[i*4 + 3] = 1;
			
			center.add(verts[i]);
		}
		
		center.x /= verts.length;
		center.y /= verts.length;
		center.z /= verts.length;
		
		projVerts = new Vector3D[5];
		for(int i=0; i<projVerts.length; i++) {
			projVerts[i] = new Vector3D();
		}
	}

	public final void destroy() {
		this.roomFront = null;
		this.roomBack = null;

		for(int i = 0; i < this.verts.length; ++i) {
			this.verts[i] = null;
		}

		this.verts = null;
	}

	public final Vector3D[] getVertices() {
		return this.verts;
	}

	public final Vector3D[] getProjVertices() {
		return this.projVerts;
	}

	public final int getProjVertsCount() {
		return projVertsCount;
	}
	
	public final Vector3D getCenter() {
		return center;
	}
	
	public final Vector3D getNormal() {
		return normal;
	}

	public final Room getRoomFront() {
		return this.roomFront;
	}

	public final Room getRoomBack() {
		return this.roomBack;
	}

	public final void setRooms(Room roomFront, Room roomBack) {
		this.roomFront = roomFront;
		this.roomBack = roomBack;
	}

	public final int getMinX() {
		return this.minx;
	}

	public final int getMinY() {
		return this.miny;
	}

	public final int getMaxX() {
		return this.maxx;
	}

	public final int getMaxY() {
		return this.maxy;
	}
	
	public final void resetViewport() {
		resetView = true;
		resetVisibleRoom = true;
		visibleRoomId = -1;
	}
	
	public final boolean isViewportCalculated() {
		return !resetView;
	}
	
	public final int getVisibleRoomId(Renderer g3d) {
		if(resetVisibleRoom) {
			tmpVec.set(g3d.camPos);
			tmpVec.sub(center);
			int distanceToPlane = tmpVec.dot(normal);
			
			Room room = null;

			if(distanceToPlane > 0) room = roomBack;
			else if(distanceToPlane < 0) room = roomFront;
			
			if(room != null) visibleRoomId = room.getId();
			
			resetVisibleRoom = false;
		}
		
		return visibleRoomId;
	}

	public final boolean isVisible(Renderer g3d, int x1, int y1, int x2, int y2) {
		if(resetView) {
			resetView = false;
			calculateViewport(g3d);
		}
		
		if(minx >= x2 || maxx <= x1 || miny >= y2 || maxy <= y1) {
			return false;
		}
		
		return true;
	}

	private final void calculateViewport(Renderer g3d) {
		float nearPlane = g3d.nearPlane;
		
		Transform invCam = g3d.getInvCam();
		
		System.arraycopy(vertsFlt, 0, vertsTmp, 0, vertsFlt.length);
		invCam.transform(vertsTmp);
		
		int tmpMinX = Integer.MAX_VALUE;
		int tmpMaxX = Integer.MIN_VALUE;
		int tmpMinY = Integer.MAX_VALUE;
		int tmpMaxY = Integer.MIN_VALUE;
		
		float projXscale = g3d.projXscale;
		float projYscale = g3d.projYscale;
		
		int w2 = g3d.width >> 1, h2 = g3d.height >> 1;
		
		projVertsCount = 0;
		int hiddenVerts = 0;
		
		for(int i=0; i<verts.length; i++) {
			float ax = vertsTmp[i * 4 + 0];
			float ay = vertsTmp[i * 4 + 1];
			float az = -vertsTmp[i * 4 + 2];
			
			int bId = (i + 1) % verts.length;
			float bx = vertsTmp[bId * 4 + 0];
			float by = vertsTmp[bId * 4 + 1];
			float bz = -vertsTmp[bId * 4 + 2];
			
			if(az < nearPlane) hiddenVerts++;
			
			if(az >= nearPlane) {
				float w = nearPlane / az;

				int px = (int) ((ax * w) * projXscale + w2);
				int py = (int) ((-ay * w) * projYscale + h2);
				
				Vector3D v = projVerts[projVertsCount];
				projVertsCount++;
				v.x = px;
				v.y = py;
				v.z = (int)az;

				if(px < tmpMinX) tmpMinX = px;
				if(px > tmpMaxX) tmpMaxX = px;
				if(py < tmpMinY) tmpMinY = py;
				if(py > tmpMaxY) tmpMaxY = py;
			}
			
			if((az >= nearPlane) ^ (bz >= nearPlane)) {
				float tmpZ = (nearPlane - az) / (bz - az);
				float cx = (bx - ax) * tmpZ + ax;
				float cy = (by - ay) * tmpZ + ay;

				int px = (int) (cx * projXscale + w2);
				int py = (int) (-cy * projYscale + h2);

				Vector3D v = projVerts[projVertsCount];
				projVertsCount++;
				v.x = px;
				v.y = py;
				v.z = (int) nearPlane;

				if(px < tmpMinX) tmpMinX = px;
				if(px > tmpMaxX) tmpMaxX = px;
				if(py < tmpMinY) tmpMinY = py;
				if(py > tmpMaxY) tmpMaxY = py;
			}
		}
		
		minx = tmpMinX;
		miny = tmpMinY;
		maxx = tmpMaxX + 1;
		maxy = tmpMaxY + 1;
		
		//Near plane clip fix
		if(hiddenVerts > 0 && hiddenVerts < verts.length) {
			tmpVec.set(g3d.camPos);
			tmpVec.sub(center);
			int distanceToPlane = tmpVec.dot(normal);

			//Near clipping fix
			if(Math.abs(distanceToPlane) < (int)(nearPlane*4096)) {
				minx = 0;
				maxx = g3d.width;
				miny = 0;
				maxy = g3d.height;

				return;
			}
		}
	}
}
