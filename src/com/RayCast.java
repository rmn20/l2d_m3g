package com;

/**
 *
 * @author Roman
 */
public class RayCast {
	
	private static final Vector3D colPoint = new Vector3D();
	private static final Vector3D v1 = new Vector3D(), v2 = new Vector3D(), v3 = new Vector3D(), v4 = new Vector3D();
	private static final Vector3D normal = new Vector3D();

	public static boolean isRayAABBCollision(Ray ray, int minx, int maxx, int miny, int maxy, int minz, int maxz) {
		Vector3D start = ray.getStart();
		Vector3D dir = ray.getDir();
		
		int startX = Math.min(start.x, start.x + dir.x);
		int startY = Math.min(start.y, start.y + dir.y);
		int startZ = Math.min(start.z, start.z + dir.z);
		
		int endX = Math.max(start.x, start.x + dir.x);
		int endY = Math.max(start.y, start.y + dir.y);
		int endZ = Math.max(start.z, start.z + dir.z);
		
		return !(
			startX > maxx || startY > maxy || startZ > maxz || 
			endX < minx || endY < miny || endZ < minz
		);
	}

	public static void rayCast(MeshData mesh, Ray ray) {
		final Vector3D v1 = RayCast.v1, v2 = RayCast.v2, v3 = RayCast.v3, v4 = RayCast.v4;
		final Vector3D normal = RayCast.normal, colPoint = RayCast.colPoint;
		
		final Vector3D start = ray.start;
		final Vector3D dir = ray.dir;
		
		short[] verts = mesh.getVerts();
		
		short[] pols = mesh.getPols();
		short[] norms = mesh.getNorms();
		
		int quadsCount = mesh.getQuadsCount();
		int trisCount = mesh.getTrisCount();
		
		int meshScalefp8 = (int) (256 * mesh.getScale());
		int meshOffsetX = (int) (mesh.getOffsetX() * mesh.getScale());
		int meshOffsetY = (int) (mesh.getOffsetY() * mesh.getScale());
		int meshOffsetZ = (int) (mesh.getOffsetZ() * mesh.getScale());

		final int sx = start.x, sy = start.y, sz = start.z;
		final int ex = sx + dir.x, ey = sy + dir.y, ez = sz + dir.z;
		
		int x1 = (((Math.min(sx, ex) - meshOffsetX) << 8) / meshScalefp8) - 1;
		int y1 = (((Math.min(sy, ey) - meshOffsetY) << 8) / meshScalefp8) - 1;
		int z1 = (((Math.min(sz, ez) - meshOffsetZ) << 8) / meshScalefp8) - 1;

		int x2 = (((Math.max(sx, ex) - meshOffsetX) << 8) / meshScalefp8) + 1;
		int y2 = (((Math.max(sy, ey) - meshOffsetY) << 8) / meshScalefp8) + 1;
		int z2 = (((Math.max(sz, ez) - meshOffsetZ) << 8) / meshScalefp8) + 1;

		final int dirLen = dir.length();
		
		for(int vtxPerPoly = 4, pIdx = 0, nIdx = 0; vtxPerPoly >= 3; vtxPerPoly--) {
			
			int pEnd = vtxPerPoly == 4 ? quadsCount * 4 : pols.length;
			
			for(; pIdx < pEnd; pIdx += vtxPerPoly, nIdx += 3) {
				int v1Index = pols[pIdx + 0] * 3;
				int v2Index = pols[pIdx + 1] * 3;
				int v3Index = pols[pIdx + 2] * 3;

				int 
					ax = verts[v1Index + 0], 
					ay = verts[v1Index + 1], 
					az = verts[v1Index + 2];
				
				int 
					bx = verts[v2Index + 0], 
					by = verts[v2Index + 1], 
					bz = verts[v2Index + 2];
				
				int 
					cx = verts[v3Index + 0], 
					cy = verts[v3Index + 1], 
					cz = verts[v3Index + 2];
				
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
					int v4Index = pols[pIdx + 3] * 3;

					dx = verts[v4Index + 0];
					dy = verts[v4Index + 1]; 
					dz = verts[v4Index + 2];
					
					if(dx > maxx) maxx = dx;
					if(dx < minx) minx = dx;
					if(dy > maxy) maxy = dy;
					if(dy < miny) miny = dy;
					if(dz > maxz) maxz = dz;
					if(dz < minz) minz = dz;
				}
				
				if(maxx < x1) continue;
				if(minx > x2) continue;
				if(maxz < z1) continue;
				if(minz > z2) continue;
				if(maxy < y1) continue;
				if(miny > y2) continue;
				
				normal.x = norms[nIdx];
				normal.y = norms[nIdx + 1];
				normal.z = norms[nIdx + 2];
				
				ax = ((ax * meshScalefp8) >> 8) + meshOffsetX;
				ay = ((ay * meshScalefp8) >> 8) + meshOffsetY;
				az = ((az * meshScalefp8) >> 8) + meshOffsetZ;
				
				bx = ((bx * meshScalefp8) >> 8) + meshOffsetX;
				by = ((by * meshScalefp8) >> 8) + meshOffsetY;
				bz = ((bz * meshScalefp8) >> 8) + meshOffsetZ;
				
				cx = ((cx * meshScalefp8) >> 8) + meshOffsetX;
				cy = ((cy * meshScalefp8) >> 8) + meshOffsetY;
				cz = ((cz * meshScalefp8) >> 8) + meshOffsetZ;

				v1.set(ax, ay, az);
				v2.set(bx, by, bz);
				v3.set(cx, cy, cz);
				
				int dis;
				if(vtxPerPoly == 4) {
					dx = ((dx * meshScalefp8) >> 8) + meshOffsetX;
					dy = ((dy * meshScalefp8) >> 8) + meshOffsetY;
					dz = ((dz * meshScalefp8) >> 8) + meshOffsetZ;
				
					v4.set(dx, dy, dz);
					dis = rayCast(v1, v2, v3, v4, normal, start, dir, colPoint);
				} else {
					dis = rayCast(v1, v2, v3, normal, start, dir, colPoint);
				}

				if(dis != Integer.MAX_VALUE) {
					int distance = dirLen * dis >> 12;
					
					if(distance < ray.distance) {
						ray.collision = true;
						ray.distance = distance;
						
						ray.collisionPoint.set(colPoint);
						ray.normal.set(normal);
					}
				}
			}
		}

	}

	private static int rayCast(Vector3D a, Vector3D b, Vector3D c, Vector3D nor, Vector3D start, Vector3D dir, Vector3D pos) {
		pos.set(start.x - a.x, start.y - a.y, start.z - a.z);
		int dot = dir.dot(nor) >> 12;
		if(dot <= 0) return Integer.MAX_VALUE;
		dot = -pos.dot(nor) / dot;
		if(dot < 0 || dot > 4096) return Integer.MAX_VALUE;
		pos.set(start.x + (dir.x * dot >> 12),
				start.y + (dir.y * dot >> 12),
				start.z + (dir.z * dot >> 12));
		if(MathUtils.isPointOnPolygon(pos, a, b, c, nor)) return dot;
		return Integer.MAX_VALUE;
	}

	private static int rayCast(Vector3D a, Vector3D b, Vector3D c, Vector3D d, Vector3D nor, Vector3D start, Vector3D dir, Vector3D pos) {
		pos.set(start.x - a.x, start.y - a.y, start.z - a.z);
		int dot = dir.dot(nor) >> 12;
		if(dot <= 0) return Integer.MAX_VALUE;
		dot = -pos.dot(nor) / dot;
		if(dot < 0 || dot > 4096) return Integer.MAX_VALUE;
		pos.set(start.x + (dir.x * dot >> 12),
				start.y + (dir.y * dot >> 12),
				start.z + (dir.z * dot >> 12));
		if(MathUtils.isPointOnPolygon(pos, a, b, c, d, nor)) return dot;
		return Integer.MAX_VALUE;
	}
}
