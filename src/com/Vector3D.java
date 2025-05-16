package com;

public final class Vector3D {

	public int x;
	public int y;
	public int z;

	public Vector3D() {
	}

	public Vector3D(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3D(Vector3D v) {
		set(v);
	}

	public final void set(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public final void set(Vector3D v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}

	public final void add(Vector3D v) {
		this.x += v.x;
		this.y += v.y;
		this.z += v.z;
	}

	public final void sub(Vector3D v) {
		this.x -= v.x;
		this.y -= v.y;
		this.z -= v.z;
	}
	
	public final void mulfp8(int mul) {
		x = (x * mul) >> 8;
		y = (y * mul) >> 8;
		z = (z * mul) >> 8;
	}

	public final void setFromRotation(int xRot, int yRot) {
		float xr = xRot * MathUtils.FPI * 2 / (1 << 14);
		float yr = yRot * MathUtils.FPI * 2 / (1 << 14);

		final float xa = 1f;
		float ya = xa * (float) Math.cos(xr);
		float yaYDSin = ya * (float) -Math.sin(yr);
		float yaYDCos = ya * (float) -Math.cos(yr);

		x = (int) (yaYDSin * (1 << 14));
		y = (int) (xa * (float) Math.sin(xr) * (1 << 14));
		z = (int) (yaYDCos * (1 << 14));
	}

	public final int lengthSquared() {
		return this.x * this.x + this.y * this.y + this.z * this.z;
	}

	public final void setLength(int len) {
		int var2;
		if((var2 = this.x * this.x + this.y * this.y + this.z * this.z) != len * len) {
			float var3 = MathUtils.invSqrt((float) var2) * (float) len;
			this.x = (int) ((float) this.x * var3);
			this.y = (int) ((float) this.y * var3);
			this.z = (int) ((float) this.z * var3);
		}
	}

	public final int dot(Vector3D v) {
		return this.x * v.x + this.y * v.y + this.z * v.z;
	}

	public final String toString() {
		return this.x + " " + this.y + " " + this.z;
	}
}
