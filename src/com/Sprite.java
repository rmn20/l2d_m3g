package com;

public final class Sprite extends RenderObject {

	private Texture[] textures;
	private Vertex pos = new Vertex();
	private Vertex size = new Vertex();
	public boolean mirX = false;
	public boolean mirY = false;
	private int scale = 5;
	private int offsetX = 0;
	private int offsetY = 0;

	public Sprite(int var1) {
	}

	public final void setScale(int scale) {
		this.scale = scale;
	}

	public final void destroy() {
		this.textures = null;
		this.pos = null;
		this.size = null;
	}

	public final void setOffset(int x, int y) {
		this.offsetX = 0;
		this.offsetY = y;
	}

	public final Vertex getPosition() {
		return this.pos;
	}

	public final void setTextures(Texture[] textures) {
		this.textures = textures;
	}

	public final int getHeight() {
		return this.textures[0].h * this.scale;
	}

	public final boolean isVisible(int x1, int y1, int x2, int y2) {
		this.sz = this.pos.rz;
		return this.sz > 0 ? false : (this.pos.sx < x2 && this.pos.sy < y2 ? this.size.sx > x1 && this.size.sy > y1 : false);
	}

	public final void project(Matrix matrix, Graphics3D g3d) {
		int var3 = this.textures[0].w * this.scale;
		int var4 = this.getHeight();
		this.pos.transform(matrix);
		this.pos.sx += this.offsetX;
		this.pos.sy += this.offsetY;
		this.pos.sx -= var3 / 2;
		this.pos.sy += var4;
		this.size.sx = this.pos.sx + var3;
		this.size.sy = this.pos.sy - var4;
		this.size.rz = this.pos.rz;
		this.pos.project(g3d);
		this.size.project(g3d);
	}

	public final void render(Graphics3D g3d, Texture texture) {
		texture = this.textures[0];
		int var3 = this.pos.sx;
		int var4 = this.pos.sy;
		int var5 = this.size.sx;
		int var6 = this.size.sy;
		int var7;
		if(var3 > var5) {
			var7 = var3;
			var3 = var5;
			var5 = var7;
		}

		if(var4 > var6) {
			var7 = var4;
			var4 = var6;
			var6 = var7;
		}

		int[] var20 = texture.pixels;
		int var8 = texture.w;
		int var19 = texture.h;
		int var9 = var20.length;
		int[] var10 = g3d.display;
		int var11 = g3d.width;
		int var12 = 0;
		int var13 = var8;
		int var14 = 0;
		int var15 = var19;
		if(this.mirX) {
			var12 = var8;
			var13 = 0;
		}

		if(this.mirY) {
			var14 = var19;
			var15 = 0;
		}

		var12 <<= 12;
		var13 <<= 12;
		var14 <<= 12;
		var15 <<= 12;
		if(var5 != var3 && var6 != var4) {
			int var16 = (var13 - var12) / (var5 - var3);
			var15 = (var15 - var14) / (var6 - var4);
			int var17 = var12;
			var14 = var14;
			if(var4 < 0) {
				var14 -= var15 * var4;
				var4 = 0;
			}

			if(var6 > g3d.height) {
				var6 = g3d.height;
			}

			if(var3 < 0) {
				var17 = var12 - var16 * var3;
				var3 = 0;
			}

			if(var5 > var11) {
				var5 = var11;
			}

			while(var4 < var6) {
				int var18 = var3 + var11 * var4;
				var19 = var5 + var11 * var4;

				for(var13 = (var14 & -4096) * var8 + var17; var18 < var19; var13 += var16) {
					if(((var12 = var20[(var13 >> 12 & Integer.MAX_VALUE) % var9]) & -16777216) != 0) {
						var10[var18] = var12;
					}

					++var18;
				}

				++var4;
				var14 += var15;
			}

		}
	}
}
