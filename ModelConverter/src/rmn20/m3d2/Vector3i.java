package rmn20.m3d2;

import rmn20.assimp.Vector3f;

/**
 *
 * @author Roman
 */
public class Vector3i {
	public static int positionScale = 256;
	public static int uvScale = 256;
	
	public int x, y, z;
	
	public Vector3i(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3i(int v) {
		x = y = z = v;
	}
	
	public Vector3i(Vector3i v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}
	
	public void min(Vector3i v) {
		x = Math.min(x, v.x);
		y = Math.min(y, v.y);
		z = Math.min(z, v.z);
	}
	
	public void max(Vector3i v) {
		x = Math.max(x, v.x);
		y = Math.max(y, v.y);
		z = Math.max(z, v.z);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Vector3i) {
			Vector3i v = (Vector3i) o;
			return v.x == x && v.y == y && v.z == z;
		} 
		
		return super.equals(o);
	}
	
	public static Vector3i createPos(Vector3f v) {
		int x = Math.round(v.x * positionScale);
		int y = Math.round(v.y * positionScale);
		int z = Math.round(v.z * positionScale);
		
		if(
			x < Short.MIN_VALUE || x > Short.MAX_VALUE ||
			y < Short.MIN_VALUE || y > Short.MAX_VALUE ||
			z < Short.MIN_VALUE || z > Short.MAX_VALUE
		) {
			throw new Error("Position overflow!");
		}
		
		return new Vector3i(x, y, z);
	}
	
	public static Vector3i createNorm(Vector3f v) {
		int x = Math.round(v.x * 127);
		int y = Math.round(v.y * 127);
		int z = Math.round(v.z * 127);
		
		x = Math.min(Math.max(x, Byte.MIN_VALUE), Byte.MAX_VALUE);
		y = Math.min(Math.max(y, Byte.MIN_VALUE), Byte.MAX_VALUE);
		z = Math.min(Math.max(z, Byte.MIN_VALUE), Byte.MAX_VALUE);
		
		return new Vector3i(x, y, z);
	}
	
	public static Vector3i createUV(Vector3f v) {
		int x = Math.round(v.x * uvScale);
		int y = Math.round(v.y * uvScale);
		
		if(
			x < Short.MIN_VALUE || x > Short.MAX_VALUE ||
			y < Short.MIN_VALUE || y > Short.MAX_VALUE
		) {
			throw new Error("UV overflow!");
		}
		
		return new Vector3i(x, y, 0);
	}
	
	public static Vector3i createCol(Vector3f v) {
		int r = Math.round(v.x * 255);
		int g = Math.round(v.y * 255);
		int b = Math.round(v.z * 255);
		
		r = Math.min(Math.max(r, 0), 255);
		g = Math.min(Math.max(g, 0), 255);
		b = Math.min(Math.max(b, 0), 255);
		
		return new Vector3i(r, g, b);
	}
}
