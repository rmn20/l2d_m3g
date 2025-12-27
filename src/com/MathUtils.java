package com;

public final class MathUtils {
	public static float FPI = (float) Math.PI;
	public static float PI_TO_RAD = (float) (180.0 / Math.PI);

	public static float invSqrt(float val) {
		float var1 = 0.5F * val;
		int var2 = 1597463007 - (Float.floatToIntBits(val) >> 1);
		float var3 = val = Float.intBitsToFloat(var2);
		
		return var3 * (1.5F - var1 * var3 * var3);
	}

	//x range from -1 to 1
	public static float atan(float x) {
		return x * (FPI * 0.25f - 0.273f * (Math.abs(x) - 1));
	}

	public static float atan2(float x1, float z1, float x2, float z2) {
		float y = x2 - x1;
		float x = z2 - z1;
		
		if(x == 0) {
			if(y > 0) return 90;
			else return -90; //y == 0 is undefined
		}
		
		float res;
		
		if(Math.abs(y) < Math.abs(x)) {
			res = PI_TO_RAD * atan(y / x);
		} else {
			float tmp = x / y;
			res = -PI_TO_RAD * atan(tmp);
			if(tmp > 0) res += 90;
			else res -= 90;
		}
		
		if(x > 0) {
			if(y >= 0) res += 180;
			else res -= 180;
		}
		
		return res;
	}

	public static Vector3D createNormal(int ax, int ay, int az, int bx, int by, int bz, int cx, int cy, int cz) {
		double x = (double) ((long) (ay - by) * (long) (az - cz) - (long) (az - bz) * (long) (ay - cy));
		double y = (double) ((long) (az - bz) * (long) (ax - cx) - (long) (ax - bx) * (long) (az - cz));
		double z = (double) ((long) (ax - bx) * (long) (ay - cy) - (long) (ay - by) * (long) (ax - cx));
		
		double len = Math.sqrt(x * x + y * y + z * z) / 4096;
		
		return new Vector3D((int) (x / len), (int) (y / len), (int) (z / len));
	}

	public static int distanceToLine(Vector3D point, Vector3D a, Vector3D b) {
		int var3 = b.x - a.x;
		int var4 = b.y - a.y;
		int var13 = b.z - a.z;
		int var5 = point.x - a.x;
		int var6 = point.y - a.y;
		int var7 = point.z - a.z;
		long var9 = (long) var3 * (long) var3 + (long) (var4 * var4) + (long) (var13 * var13);
		long var11 = 0L;
		if(var9 != 0L) {
			var11 = ((long) var5 * (long) var3 + (long) (var6 * var4) + (long) (var7 * var13) << 14) / var9;
		}

		if(var11 < 0L) {
			var11 = 0L;
		}

		if(var11 > 16384L) {
			var11 = 16384L;
		}

		var3 = a.x + (int) ((long) var3 * var11 >> 14);
		var4 = a.y + (int) ((long) var4 * var11 >> 14);
		var13 = a.z + (int) ((long) var13 * var11 >> 14);
		var3 -= point.x;
		var4 -= point.y;
		var13 -= point.z;
		return var3 * var3 + var4 * var4 + var13 * var13;
	}

	public static int distanceToRay(Vector3D point, Vector3D a, Vector3D dir) {
		int var3 = dir.x;
		int var4 = dir.y;
		int var13 = dir.z;
		int var5 = point.x - a.x;
		int var6 = point.y - a.y;
		int var7 = point.z - a.z;
		long var9 = (long) (var3 * var3 + var4 * var4 + var13 * var13);
		long var11 = 0L;
		if(var9 != 0L) {
			var11 = ((long) var5 * (long) var3 + (long) (var6 * var4) + (long) (var7 * var13) << 14) / var9;
		}

		if(var11 < 0L) {
			var11 = 0L;
		}

		if(var11 > 16384L) {
			var11 = 16384L;
		}

		var3 = a.x + (int) ((long) var3 * var11 >> 14);
		var4 = a.y + (int) ((long) var4 * var11 >> 14);
		var13 = a.z + (int) ((long) var13 * var11 >> 14);
		var3 -= point.x;
		var4 -= point.y;
		var13 -= point.z;
		return var3 * var3 + var4 * var4 + var13 * var13;
	}

	    public static boolean isPointOnPolygon(Vector3D point, Vector3D a, Vector3D b, Vector3D c, Vector3D d, Vector3D normal) {
        final int nx = normal.x>0 ? normal.x : -normal.x;
        final int ny = normal.y>0 ? normal.y : -normal.y;
        final int nz = normal.z>0 ? normal.z : -normal.z;

        if (  nx >= ny && nx >= nz ) {
            if(normal.x >= 0) {
                return isPointOnPolygon( point.z, point.y, a.z, a.y, b.z, b.y, c.z, c.y, d.z, d.y );
            } else {
                return isPointOnPolygon( point.z, point.y, d.z, d.y, c.z, c.y, b.z, b.y, a.z, a.y );
            }
        }
        if (  ny >= nx && ny >= nz ) {
            if(normal.y >= 0) {
                return isPointOnPolygon( point.x, point.z, a.x, a.z, b.x, b.z, c.x, c.z, d.x, d.z );
            } else {
                return isPointOnPolygon( point.x, point.z, d.x, d.z, c.x, c.z, b.x, b.z, a.x, a.z );
            }
        }
        if (  nz >= nx && nz >= ny ) {
            if(normal.z <= 0) {
                return isPointOnPolygon( point.x, point.y, a.x, a.y, b.x, b.y, c.x, c.y, d.x, d.y );
            } else {
                return isPointOnPolygon( point.x, point.y, d.x, d.y, c.x, c.y, b.x, b.y, a.x, a.y );
            }
        }
        return true;
    }
    
    public static boolean isPointOnPolygon(Vector3D point, Vector3D a, Vector3D b, Vector3D c, Vector3D normal) {
        final int nx = normal.x>0 ? normal.x : -normal.x;
        final int ny = normal.y>0 ? normal.y : -normal.y;
        final int nz = normal.z>0 ? normal.z : -normal.z;

        if (  nx >= ny && nx >= nz ) {
            if(normal.x >= 0) {
                return isPointOnPolygon( point.z, point.y, a.z, a.y, b.z, b.y, c.z, c.y );
            } else {
                return isPointOnPolygon( point.z, point.y, c.z, c.y, b.z, b.y, a.z, a.y );
            }
        }
        if (  ny >= nx && ny >= nz ) {
            if(normal.y >= 0) {
                return isPointOnPolygon( point.x, point.z, a.x, a.z, b.x, b.z, c.x, c.z );
            } else {
                return isPointOnPolygon( point.x, point.z, c.x, c.z, b.x, b.z, a.x, a.z );
            }
        }
        if (  nz >= nx && nz >= ny ) {
            if(normal.z <= 0) {
                return isPointOnPolygon( point.x, point.y, a.x, a.y, b.x, b.y, c.x, c.y );
            } else {
                return isPointOnPolygon( point.x, point.y, c.x, c.y, b.x, b.y, a.x, a.y );
            }
        }
        return true;
    }
    
    public static boolean isPointOnPolygon(int px, int pz, 
                                           int ax, int az,
                                           int bx, int bz,
                                           int cx, int cz, 
                                           int dx, int dz,
                                           int norY) {
        if(norY > 0) {
            return isPointOnPolygon(px, pz, ax, az, bx, bz, cx, cz, dx, dz);
        }
        if(norY < 0) {
            return isPointOnPolygon(px, pz, dx, dz, cx, cz, bx, bz, ax, az);
        }
        return false;
    }
    
    public static boolean isPointOnPolygon(int px, int pz, 
                                           int ax, int az,
                                           int bx, int bz,
                                           int cx, int cz, 
                                           int norY) {
        if(norY > 0) {
            return isPointOnPolygon(px, pz, ax, az, bx, bz, cx, cz);
        }
        if(norY < 0) {
            return isPointOnPolygon(px, pz, cx, cz, bx, bz, ax, az);
        }
        return false;
    }
    
    public static boolean isPointOnPolygon(int px, int py, int x1, int y1, int x2, int y2, int x3, int y3, int x4, int y4) {
        return  (x2-x1)*(py-y1) <= (px-x1)*(y2-y1) &&
                (x3-x2)*(py-y2) <= (px-x2)*(y3-y2) &&
                (x4-x3)*(py-y3) <= (px-x3)*(y4-y3) &&
                (x1-x4)*(py-y4) <= (px-x4)*(y1-y4);
    }
    
    public static boolean isPointOnPolygon(int px, int py, int x1, int y1, int x2, int y2, int x3, int y3) {
        return  (x2-x1)*(py-y1) <= (px-x1)*(y2-y1) &&
                (x3-x2)*(py-y2) <= (px-x2)*(y3-y2) && 
                (x1-x3)*(py-y3) <= (px-x3)*(y1-y3);
    }
}
