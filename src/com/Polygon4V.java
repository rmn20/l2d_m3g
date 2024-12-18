package com;

public final class Polygon4V extends RenderObject {

	public final Vertex a;
	public final Vertex b;
	public final Vertex c;
	public final Vertex d;
	public final byte au;
	public final byte av;
	public final byte bu;
	public final byte bv;
	public final byte cu;
	public final byte cv;
	public final byte du;
	public final byte dv;

	public Polygon4V(Vertex a, Vertex b, Vertex c, Vertex d, byte au, byte av, byte bu, byte bv, byte cu, byte cv, byte du, byte dv) {
		super(a, b, d);
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.au = au;
		this.av = av;
		this.bu = bu;
		this.bv = bv;
		this.cu = cu;
		this.cv = cv;
		this.du = du;
		this.dv = dv;
	}

	private static int size(int a, int b, int c, int d) {
		int var4;
		if(b < a) {
			var4 = a;
			a = b;
			b = var4;
		}

		if(c < a) {
			var4 = c;
			c = a;
			a = var4;
		}

		if(c < b) {
			c = b;
		}

		a = a < d ? a : d;
		int var10000 = c > d ? c : d;
		int var10001 = c > d ? c : d;
		return var10000 - a;
	}

	public final void render(Graphics3D g3d, Texture texture) {
		if(texture.perspectiveCorrection && (size(this.a.sx, this.b.sx, this.c.sx, this.d.sx) > 45 || size(this.a.sy, this.b.sy, this.c.sy, this.d.sy) > 45) && size(this.a.rz, this.b.rz, this.c.rz, this.d.rz) > 200) {
			Texture.paintPers(g3d, texture, this.a, this.au & 255, this.av & 255, this.b, this.bu & 255, this.bv & 255, this.d, this.du & 255, this.dv & 255);
			Texture.paintPers(g3d, texture, this.b, this.bu & 255, this.bv & 255, this.c, this.cu & 255, this.cv & 255, this.d, this.du & 255, this.dv & 255);
		} else {
			Texture.paintAffine(g3d, texture, this.a, this.au & 255, this.av & 255, this.b, this.bu & 255, this.bv & 255, this.d, this.du & 255, this.dv & 255);
			Texture.paintAffine(g3d, texture, this.b, this.bu & 255, this.bv & 255, this.c, this.cu & 255, this.cv & 255, this.d, this.du & 255, this.dv & 255);
		}
	}

	public final boolean isVisible(int x1, int y1, int x2, int y2) {
		if((this.a.sx - this.b.sx) * (this.b.sy - this.d.sy) <= (this.a.sy - this.b.sy) * (this.b.sx - this.d.sx)) {
			return false;
		} else {
			this.sz = this.a.rz + this.b.rz + this.c.rz + this.d.rz >> 2;
			return this.sz > 0 ? false : (this.a.sx <= x1 && this.b.sx <= x1 && this.c.sx <= x1 && this.d.sx <= x1 ? false : (this.a.sy <= y1 && this.b.sy <= y1 && this.c.sy <= y1 && this.d.sy <= y1 ? false : (this.a.sx >= x2 && this.b.sx >= x2 && this.c.sx >= x2 && this.d.sx >= x2 ? false : this.a.sy < y2 || this.b.sy < y2 || this.c.sy < y2 || this.d.sy < y2)));
		}
	}
}
