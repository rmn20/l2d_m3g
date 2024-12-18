package com;

public final class Morphing {

	private short[][] animation;
	private Mesh mesh;

	public final void destroy() {
		this.animation = null;
		this.mesh = null;
	}

	/* short[][] getVertices()
	 * и
	 * private static short[] verticesToArray(Vertex[] vertices)
	 * и
	 * public static Morphing create(Mesh[] meshes)
	 */
	public static short[][] create(Mesh[] meshes) {
		short[][] var1 = new short[meshes.length][];

		for(int var2 = 0; var2 < meshes.length; ++var2) {
			Mesh var3 = meshes[var2];
			Vertex[] var7;
			short[] var4 = new short[(var7 = var3.getVertices()).length * 3];

			for(int var5 = 0; var5 < var7.length; ++var5) {
				Vertex var6 = var7[var5];
				var4[var5 * 3] = (short) var6.x;
				var4[var5 * 3 + 1] = (short) var6.y;
				var4[var5 * 3 + 2] = (short) var6.z;
			}

			var1[var2] = var4;
		}

		return var1;
	}

	public Morphing() {
	}

	public Morphing(short[][] animation, Mesh mesh) {
		this.animation = animation;
		this.mesh = mesh;
	}

	/* public void setFrame(int frame)
	 * и
	 * void interpolation(Vertex[] result)
	 * и
	 * private void interpolation(final short[] srcA, final short[] srcB, final Vertex[] result, final int frame)
	 */
	public final void setFrame(int frame) {
		int var4 = frame;
		Mesh var3 = this.mesh;
		short[][] var2 = this.animation;

		for(Morphing var13 = this; var4 < 0; var4 += 1024 * var13.animation.length) {
			;
		}

		int var5 = (frame = var4 / 1024) % var2.length;
		frame = (frame + 1) % var2.length;
		var4 %= 1024;
		short[] var10000 = var2[var5];
		short[] var10001 = var2[frame];
		int var17 = var4;
		short[] var14 = var10001;
		short[] var18 = var10000;
		Vertex[] var16 = var3.getVertices();
		var5 = 1024 - var4;

		for(int var6 = 0; var6 < var16.length; ++var6) {
			int var7 = var6 * 3;
			short var8 = var18[var7];
			short var9 = var18[var7 + 1];
			short var10 = var18[var7 + 2];
			short var11 = var14[var7];
			short var12 = var14[var7 + 1];
			short var19 = var14[var7 + 2];
			var16[var6].set((var8 * var5 >> 10) + (var11 * var17 >> 10), (var9 * var5 >> 10) + (var12 * var17 >> 10), (var10 * var5 >> 10) + (var19 * var17 >> 10));
		}

	}

	public final Mesh getMesh() {
		return this.mesh;
	}
}
