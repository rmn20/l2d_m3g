package com;

public abstract class RenderObject {

	public int sz;
	public final short nx;
	public final short ny;
	public final short nz;

	RenderObject() {
		this.nx = this.ny = this.nz = 0;
	}

	// computeNormal
	RenderObject(Vertex a, Vertex b, Vertex c) {
		long var4 = (long) (a.y - b.y) * (long) (a.z - c.z) - (long) (a.z - b.z) * (long) (a.y - c.y);
		long var6 = (long) (a.z - b.z) * (long) (a.x - c.x) - (long) (a.x - b.x) * (long) (a.z - c.z);
		long var8 = (long) (a.x - b.x) * (long) (a.y - c.y) - (long) (a.y - b.y) * (long) (a.x - c.x);
		double var10 = Math.sqrt((double) (var4 * var4 + var6 * var6 + var8 * var8)) / 4096.0D;
		this.nx = (short) ((int) ((double) var4 / var10));
		this.ny = (short) ((int) ((double) var6 / var10));
		this.nz = (short) ((int) ((double) var8 / var10));
	}

	public abstract void render(Graphics3D g3d, Texture texture);

	public abstract boolean isVisible(int x1, int y1, int x2, int y2);
}
