package com;

public final class SphereCast {

	private static final Vector3D temp = new Vector3D();
	private static final Vector3D nor = new Vector3D();
	
	private static final Vector3D 
			a = new Vector3D(), b = new Vector3D(), 
			c = new Vector3D(), d = new Vector3D();

	public static boolean sphereCast(MeshData mesh, Vector3D pos, int rad) {
		short[] verts = mesh.getVertices();
		
		short[] p4v = mesh.get4VPols();
		short[] p4vNorms = mesh.getP4VNorms();
		
		short[] p3v = mesh.get3VPols();
		short[] p3vNorms = mesh.getP3VNorms();
		
		int p4vCount = p4vNorms.length / 3;
		int p3vCount = p3vNorms.length / 3;
		
		int meshScalefp8 = (int) (256 * mesh.getScale());
		
		boolean col = false;
		
		for(int vtxPerPoly = 4; vtxPerPoly >= 3; vtxPerPoly--) {
			
			short[] pols = vtxPerPoly == 4 ? p4v : p3v;
			short[] norms = vtxPerPoly == 4 ? p4vNorms : p3vNorms;
			int polysCount = vtxPerPoly == 4 ? p4vCount : p3vCount;
			
			for(int i = 0; i < polysCount; i++) {
				int v1Index = pols[i * vtxPerPoly + 0];
				int v2Index = pols[i * vtxPerPoly + 1];
				int v3Index = pols[i * vtxPerPoly + 2];

				final int 
						ax = (verts[v1Index * 3 + 0] * meshScalefp8) >> 8, 
						ay = (verts[v1Index * 3 + 1] * meshScalefp8) >> 8, 
						az = (verts[v1Index * 3 + 2] * meshScalefp8) >> 8;
				final int 
						bx = (verts[v2Index * 3 + 0] * meshScalefp8) >> 8, 
						by = (verts[v2Index * 3 + 1] * meshScalefp8) >> 8, 
						bz = (verts[v2Index * 3 + 2] * meshScalefp8) >> 8;
				final int 
						cx = (verts[v3Index * 3 + 0] * meshScalefp8) >> 8, 
						cy = (verts[v3Index * 3 + 1] * meshScalefp8) >> 8, 
						cz = (verts[v3Index * 3 + 2] * meshScalefp8) >> 8;

				int maxx = ax;
				if(bx > maxx) maxx = bx;
				if(cx > maxx) maxx = cx;

				int minx = ax;
				if(bx < minx) minx = bx;
				if(cx < minx) minx = cx;

				int maxy = ay;
				if(by > maxy) maxy = by;
				if(cy > maxy) maxy = cy;

				int miny = ay;
				if(by < miny) miny = by;
				if(cy < miny) miny = cy;

				int maxz = az;
				if(bz > maxz) maxz = bz;
				if(cz > maxz) maxz = cz;

				int minz = az;
				if(bz < minz) minz = bz;
				if(cz < minz) minz = cz;
				
				int dx = 0, dy = 0, dz = 0;
				
				if(vtxPerPoly == 4) {
					int v4Index = pols[i * vtxPerPoly + 3];

					dx = (verts[v4Index * 3 + 0] * meshScalefp8) >> 8;
					dy = (verts[v4Index * 3 + 1] * meshScalefp8) >> 8; 
					dz = (verts[v4Index * 3 + 2] * meshScalefp8) >> 8;
					
					if(dx > maxx) maxx = dx;
					if(dx < minx) minx = dx;
					if(dy > maxy) maxy = dy;
					if(dy < miny) miny = dy;
					if(dz > maxz) maxz = dz;
					if(dz < minz) minz = dz;
				}
				
				if(maxx < pos.x - rad) continue;
				if(minx > pos.x + rad) continue;
				if(maxz < pos.z - rad) continue;
				if(minz > pos.z + rad) continue;
				if(maxy < pos.y - rad) continue;
				if(miny > pos.y + rad) continue;
				
				nor.x = norms[i * 3];
				nor.y = norms[i * 3 + 1];
				nor.z = norms[i * 3 + 2];

				a.set(ax, ay, az);
				b.set(bx, by, bz);
				c.set(cx, cy, cz);
				
				int dis;
				if(vtxPerPoly == 4) {
					d.set(dx, dy, dz);
					dis = distanceSphereToPolygon(a, b, c, d, nor, pos, rad);
				} else {
					dis = distanceSphereToPolygon(a, b, c, nor, pos, rad);
				}

				if(dis != Integer.MAX_VALUE && dis > 0) {
					pos.x -= nor.x * dis >> 12;
					pos.y -= nor.y * dis >> 12;
					pos.z -= nor.z * dis >> 12;
					col = true;
				}
			}
		}
		return col;
	}

	private static int distanceSphereToPolygon(Vector3D a, Vector3D b, Vector3D c, Vector3D nor,
			Vector3D point, int rad) {
		temp.x = point.x - a.x;
		temp.y = point.y - a.y;
		temp.z = point.z - a.z;
		int dot = temp.dot(nor) >> 12; //расстояние до плоскости
		if(dot > rad) return Integer.MAX_VALUE;
		//проекция на плоскость
		temp.x = point.x - (nor.x * dot >> 12);
		temp.y = point.y - (nor.y * dot >> 12);
		temp.z = point.z - (nor.z * dot >> 12);
		if(MathUtils.isPointOnPolygon(a, b, c, nor, temp)) {
			int dis = dot;
			if(dot < 0) dis = -dot;
			dis = rad - dis;
			return dis;
		}

		final int len1 = MathUtils.distanceToLine(point, a, b);
		final int len2 = MathUtils.distanceToLine(point, b, c);
		final int len3 = MathUtils.distanceToLine(point, c, a);

		int min = len1;
		if(len2 < min) min = len2;
		if(len3 < min) min = len3;
		if(min <= rad * rad) return rad - (int) (1 / MathUtils.invSqrt(min));
		
		return Integer.MAX_VALUE;
	}

	private static int distanceSphereToPolygon(Vector3D a, Vector3D b, Vector3D c, Vector3D d, Vector3D nor,
			Vector3D point, int rad) {
		temp.x = point.x - a.x;
		temp.y = point.y - a.y;
		temp.z = point.z - a.z;
		int dot = temp.dot(nor) >> 12;//расстояние до плоскости
		if(dot > rad) return Integer.MAX_VALUE;
		//проекция на плоскость
		temp.x = point.x - (nor.x * dot >> 12);
		temp.y = point.y - (nor.y * dot >> 12);
		temp.z = point.z - (nor.z * dot >> 12);
		if(MathUtils.isPointOnPolygon(a, b, c, d, nor, temp)) {
			int dis = dot;
			if(dot < 0) dis = -dot;
			dis = rad - dis;
			return dis;
		}

		final int len1 = MathUtils.distanceToLine(point, a, b);
		final int len2 = MathUtils.distanceToLine(point, b, c);
		final int len3 = MathUtils.distanceToLine(point, c, d);
		final int len4 = MathUtils.distanceToLine(point, d, a);

		int min = len1;
		if(len2 < min) min = len2;
		if(len3 < min) min = len3;
		if(len4 < min) min = len4;
		if(min <= rad * rad) return rad - (int) (1 / MathUtils.invSqrt(min));
		
		return Integer.MAX_VALUE;
	}
}
