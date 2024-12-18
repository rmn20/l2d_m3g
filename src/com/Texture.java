package com;

import javax.microedition.lcdui.Image;

public class Texture {

	boolean perspectiveCorrection;
	int widthBIT;
	int w;
	int h;
	int[] pixels;

	Texture() {
	}

	static final void paintAffine(Graphics3D g3d, Texture texture,
			Vertex a, int au, int av,
			Vertex b, int bu, int bv,
			Vertex c, int cu, int cv) {
		Vertex var11;
		int var36;
		if(b.sy < a.sy) {
			var11 = a;
			a = b;
			b = var11;
			var36 = au;
			au = bu;
			bu = var36;
			var36 = av;
			av = bv;
			bv = var36;
		}

		if(c.sy < a.sy) {
			var11 = c;
			c = a;
			a = var11;
			var36 = cu;
			cu = au;
			au = var36;
			var36 = cv;
			cv = av;
			av = var36;
		}

		if(c.sy < b.sy) {
			var11 = b;
			b = c;
			c = var11;
			var36 = bu;
			bu = cu;
			cu = var36;
			var36 = bv;
			bv = cv;
			cv = var36;
		}

		if(a.sy != c.sy) {
			int[] var37 = texture.pixels;
			int var12 = texture.pixels.length - 1;
			int var35 = texture.widthBIT;
			int[] var13 = g3d.display;
			int var18 = c.sy - a.sy;
			int var19 = (c.sx - a.sx << 12) / var18;
			int var20 = (cu - au << 12) / var18;
			int var21 = (cv - av << 12) / var18;
			int var22 = 0;
			int var23 = 0;
			int var24 = 0;
			if(b.sy != a.sy) {
				var18 = b.sy - a.sy;
				var22 = (b.sx - a.sx << 12) / var18;
				var23 = (bu - au << 12) / var18;
				var24 = (bv - av << 12) / var18;
			}

			var18 = b.sy - a.sy;
			int var25 = (a.sx << 12) + var19 * var18;
			int var26 = (au << 12) + var20 * var18;
			int var27 = (av << 12) + var21 * var18;
			int var28 = b.sx << 12;
			int var29 = bu << 12;
			int var30 = bv << 12;
			if((var18 = var25 - var28 >> 12) != 0) {
				int var31 = (var26 - var29) / var18;
				int var32 = (var27 - var30) / var18;
				var28 = var25 = a.sx << 12;
				var29 = var26 = au << 12;
				var30 = var27 = av << 12;
				int var33 = a.sy;

				for(int var34 = c.sy < g3d.height ? c.sy : g3d.height; var33 < var34; var30 += var24) {
					if(var33 == b.sy) {
						if(c.sy == b.sy) {
							return;
						}

						var18 = b.sy - a.sy;
						var25 = (a.sx << 12) + var19 * var18;
						var26 = (au << 12) + var20 * var18;
						var27 = (av << 12) + var21 * var18;
						var28 = b.sx << 12;
						var29 = bu << 12;
						var30 = bv << 12;
						var18 = c.sy - b.sy;
						var22 = (c.sx - b.sx << 12) / var18;
						var23 = (cu - bu << 12) / var18;
						var24 = (cv - bv << 12) / var18;
					}

					if(var33 >= 0) {
						int var14;
						int var15;
						int var17;
						int var16;
						if(var25 > var28) {
							var18 = var28 % 4096;
							var14 = var28 >> 12;
							var16 = var29;
							var17 = var30;
							var15 = var25 >> 12;
						} else {
							var18 = var25 % 4096;
							var14 = var25 >> 12;
							var16 = var26;
							var17 = var27;
							var15 = var28 >> 12;
						}

						var16 -= var31 * var18 >> 12;
						var17 -= var32 * var18 >> 12;
						if(var14 < 0) {
							var16 -= var31 * var14;
							var17 -= var32 * var14;
							var14 = 0;
						}

						if(var15 > g3d.width) {
							var15 = g3d.width;
						}

						var18 = g3d.width * var33;
						var14 += var18;

						for(var15 += var18; var15 - var14 >= 16; var17 += var32) {
							var13[var14++] = var37[(var17 >> 12 << var35) + (var16 >> 12) & var12];
							var16 += var31;
							var17 += var32;
							var13[var14++] = var37[(var17 >> 12 << var35) + (var16 >> 12) & var12];
							var16 += var31;
							var17 += var32;
							var13[var14++] = var37[(var17 >> 12 << var35) + (var16 >> 12) & var12];
							var16 += var31;
							var17 += var32;
							var13[var14++] = var37[(var17 >> 12 << var35) + (var16 >> 12) & var12];
							var16 += var31;
							var17 += var32;
							var13[var14++] = var37[(var17 >> 12 << var35) + (var16 >> 12) & var12];
							var16 += var31;
							var17 += var32;
							var13[var14++] = var37[(var17 >> 12 << var35) + (var16 >> 12) & var12];
							var16 += var31;
							var17 += var32;
							var13[var14++] = var37[(var17 >> 12 << var35) + (var16 >> 12) & var12];
							var16 += var31;
							var17 += var32;
							var13[var14++] = var37[(var17 >> 12 << var35) + (var16 >> 12) & var12];
							var16 += var31;
							var17 += var32;
							var13[var14++] = var37[(var17 >> 12 << var35) + (var16 >> 12) & var12];
							var16 += var31;
							var17 += var32;
							var13[var14++] = var37[(var17 >> 12 << var35) + (var16 >> 12) & var12];
							var16 += var31;
							var17 += var32;
							var13[var14++] = var37[(var17 >> 12 << var35) + (var16 >> 12) & var12];
							var16 += var31;
							var17 += var32;
							var13[var14++] = var37[(var17 >> 12 << var35) + (var16 >> 12) & var12];
							var16 += var31;
							var17 += var32;
							var13[var14++] = var37[(var17 >> 12 << var35) + (var16 >> 12) & var12];
							var16 += var31;
							var17 += var32;
							var13[var14++] = var37[(var17 >> 12 << var35) + (var16 >> 12) & var12];
							var16 += var31;
							var17 += var32;
							var13[var14++] = var37[(var17 >> 12 << var35) + (var16 >> 12) & var12];
							var16 += var31;
							var17 += var32;
							var13[var14++] = var37[(var17 >> 12 << var35) + (var16 >> 12) & var12];
							var16 += var31;
						}

						while(var14 < var15) {
							var13[var14++] = var37[(var17 >> 12 << var35) + (var16 >> 12) & var12];
							var16 += var31;
							var17 += var32;
						}
					}

					++var33;
					var25 += var19;
					var26 += var20;
					var27 += var21;
					var28 += var22;
					var29 += var23;
				}

			}
		}
	}

	static final void paintPers(Graphics3D g3d, Texture texture,
			Vertex a, int au, int av,
			Vertex b, int bu, int bv,
			Vertex c, int cu, int cv) {
		Vertex var11;
		int var35;
		if(b.sy < a.sy) {
			var11 = a;
			a = b;
			b = var11;
			var35 = au;
			au = bu;
			bu = var35;
			var35 = av;
			av = bv;
			bv = var35;
		}

		if(c.sy < a.sy) {
			var11 = c;
			c = a;
			a = var11;
			var35 = cu;
			cu = au;
			au = var35;
			var35 = cv;
			cv = av;
			av = var35;
		}

		if(b.sy > c.sy) {
			var11 = b;
			b = c;
			c = var11;
			var35 = bu;
			bu = cu;
			cu = var35;
			var35 = bv;
			bv = cv;
			cv = var35;
		}

		if(a.sy != c.sy) {
			int var12;
			if(-a.rz > 0) {
				var12 = 262294 / (-a.rz + 150);
			} else {
				var12 = 1748;
			}

			int var13;
			if(-b.rz > 0) {
				var13 = 262294 / (-b.rz + 150);
			} else {
				var13 = 1748;
			}

			int var14;
			if(-c.rz > 0) {
				var14 = 262294 / (-c.rz + 150);
			} else {
				var14 = 1748;
			}

			au *= var12;
			av *= var12;
			bu *= var13;
			bv *= var13;
			cu *= var14;
			cv *= var14;
			var35 = c.sy - a.sy;
			int var15 = (c.sx - a.sx << 12) / var35;
			int var16 = (var14 - var12 << 12) / var35;
			int var17 = (cu - au << 12) / var35;
			int var18 = (cv - av << 12) / var35;
			int var19 = 0;
			int var20 = 0;
			int var21 = 0;
			int var22 = 0;
			if(b.sy != a.sy) {
				var35 = b.sy - a.sy;
				var19 = (b.sx - a.sx << 12) / var35;
				var20 = (var13 - var12 << 12) / var35;
				var21 = (bu - au << 12) / var35;
				var22 = (bv - av << 12) / var35;
			}

			var35 = b.sy - a.sy;
			int var23 = (a.sx << 12) + var15 * var35;
			int var24 = (var12 << 12) + var16 * var35;
			int var25 = (au << 12) + var17 * var35;
			int var26 = (av << 12) + var18 * var35;
			int var27 = b.sx << 12;
			int var28 = var13 << 12;
			int var29 = bu << 12;
			int var30 = bv << 12;
			if((var35 = var23 - var27 >> 12) != 0) {
				int var31 = (var25 - var29) / var35;
				int var32 = (var26 - var30) / var35;
				int var33 = (var24 - var28) / var35;
				var23 = var27 = a.sx << 12;
				var24 = var28 = var12 << 12;
				var25 = var29 = au << 12;
				var26 = var30 = av << 12;
				int var34;
				if(b.sy > 0) {
					var35 = a.sy;
					if(a.sy < 0) {
						var23 -= var15 * var35;
						var24 -= var16 * var35;
						var25 -= var17 * var35;
						var26 -= var18 * var35;
						var27 -= var19 * var35;
						var28 -= var20 * var35;
						var29 -= var21 * var35;
						var30 -= var22 * var35;
						var35 = 0;
					}

					var34 = b.sy < g3d.height ? b.sy : g3d.height;
					paintMiniTrianglePers(g3d, texture, var35, var34, var23, var24, var25, var26, var27, var28, var29, var30, var15, var16, var17, var18, var19, var20, var21, var22, var33, var31, var32);
				}

				if(c.sy != b.sy && c.sy >= 0) {
					var35 = b.sy - a.sy;
					var23 = (a.sx << 12) + var15 * var35;
					var24 = (var12 << 12) + var16 * var35;
					var25 = (au << 12) + var17 * var35;
					var26 = (av << 12) + var18 * var35;
					var27 = b.sx << 12;
					var28 = var13 << 12;
					var29 = bu << 12;
					var30 = bv << 12;
					var35 = c.sy - b.sy;
					var19 = (c.sx - b.sx << 12) / var35;
					var20 = (var14 - var13 << 12) / var35;
					var21 = (cu - bu << 12) / var35;
					var22 = (cv - bv << 12) / var35;
					var35 = b.sy;
					if(b.sy < 0) {
						var23 -= var15 * var35;
						var24 -= var16 * var35;
						var25 -= var17 * var35;
						var26 -= var18 * var35;
						var27 -= var19 * var35;
						var28 -= var20 * var35;
						var29 -= var21 * var35;
						var30 -= var22 * var35;
						var35 = 0;
					}

					var34 = c.sy < g3d.height ? c.sy : g3d.height;
					paintMiniTrianglePers(g3d, texture, var35, var34, var23, var24, var25, var26, var27, var28, var29, var30, var15, var16, var17, var18, var19, var20, var21, var22, var33, var31, var32);
				}
			}
		}
	}

	private static final void paintMiniTrianglePers(Graphics3D g3d, Texture texture, int y_start, int y_end,
			int x_start, int wz_start, int uz_start, int vz_start,
			int x_end, int wz_end, int uz_end, int vz_end,
			int dx_start, int dwz_start, int duz_start, int dvz_start,
			int dx_end, int dwz_end, int duz_end, int dvz_end,
			int dwz, int duz, int dvz) {
		int[] var23 = texture.pixels;
		int var24 = texture.pixels.length - 1;
		int var41 = texture.widthBIT;
		int[] var25 = g3d.display;

		for(int var40 = y_start; var40 < y_end; ++var40) {
			int var27;
			int var26;
			long var28;
			int var31;
			int var30;
			int var34;
			int var35;
			int var32;
			int var33;
			if(x_start > x_end) {
				var28 = (long) (x_end % 4096);
				var34 = x_end >> 12;
				var35 = x_start >> 12;
				var26 = uz_end;
				var27 = uz_start;
				var30 = vz_end;
				var31 = vz_start;
				var32 = wz_end;
				var33 = wz_start;
			} else {
				var28 = (long) (x_start % 4096);
				var34 = x_start >> 12;
				var35 = x_end >> 12;
				var26 = uz_start;
				var27 = uz_end;
				var30 = vz_start;
				var31 = vz_end;
				var32 = wz_start;
				var33 = wz_end;
			}

			if(var34 < 0) {
				var26 -= duz * var34;
				var30 -= dvz * var34;
				var32 -= dwz * var34;
				var34 = 0;
			}

			if(var35 > g3d.width) {
				var35 -= g3d.width;
				var27 -= duz * var35;
				var31 -= dvz * var35;
				var33 -= dwz * var35;
				var35 = g3d.width;
			}

			y_start = var40 * g3d.width;
			var34 += y_start;
			y_start = (var35 += y_start) - var34;
			var26 = (int) ((long) var26 - ((long) duz * var28 >> 12));
			var30 = (int) ((long) var30 - ((long) dvz * var28 >> 12));
			if((var32 = (int) ((long) var32 - ((long) dwz * var28 >> 12))) == 0) {
				return;
			}

			int var42 = (int) (((long) var26 << 12) / (long) var32);

			int var29;
			int var38;
			int var39;
			for(var29 = (int) (((long) var30 << 12) / (long) var32); y_start >= 16; y_start -= 16) {
				int var36 = var26 + (duz << 4);
				int var37 = var30 + (dvz << 4);
				var32 += dwz << 4;
				var38 = (int) (((long) var36 << 12) / (long) var32);
				var39 = (int) (((long) var37 << 12) / (long) var32);
				var26 = var38 - var42 >> 4;
				var30 = var39 - var29 >> 4;
				var25[var34++] = var23[(var29 >> 12 << var41) + (var42 >> 12) & var24];
				var42 += var26;
				var29 += var30;
				var25[var34++] = var23[(var29 >> 12 << var41) + (var42 >> 12) & var24];
				var42 += var26;
				var29 += var30;
				var25[var34++] = var23[(var29 >> 12 << var41) + (var42 >> 12) & var24];
				var42 += var26;
				var29 += var30;
				var25[var34++] = var23[(var29 >> 12 << var41) + (var42 >> 12) & var24];
				var42 += var26;
				var29 += var30;
				var25[var34++] = var23[(var29 >> 12 << var41) + (var42 >> 12) & var24];
				var42 += var26;
				var29 += var30;
				var25[var34++] = var23[(var29 >> 12 << var41) + (var42 >> 12) & var24];
				var42 += var26;
				var29 += var30;
				var25[var34++] = var23[(var29 >> 12 << var41) + (var42 >> 12) & var24];
				var42 += var26;
				var29 += var30;
				var25[var34++] = var23[(var29 >> 12 << var41) + (var42 >> 12) & var24];
				var42 += var26;
				var29 += var30;
				var25[var34++] = var23[(var29 >> 12 << var41) + (var42 >> 12) & var24];
				var42 += var26;
				var29 += var30;
				var25[var34++] = var23[(var29 >> 12 << var41) + (var42 >> 12) & var24];
				var42 += var26;
				var29 += var30;
				var25[var34++] = var23[(var29 >> 12 << var41) + (var42 >> 12) & var24];
				var42 += var26;
				var29 += var30;
				var25[var34++] = var23[(var29 >> 12 << var41) + (var42 >> 12) & var24];
				var42 += var26;
				var29 += var30;
				var25[var34++] = var23[(var29 >> 12 << var41) + (var42 >> 12) & var24];
				var42 += var26;
				var29 += var30;
				var25[var34++] = var23[(var29 >> 12 << var41) + (var42 >> 12) & var24];
				var42 += var26;
				var29 += var30;
				var25[var34++] = var23[(var29 >> 12 << var41) + (var42 >> 12) & var24];
				var42 += var26;
				var29 += var30;
				var25[var34++] = var23[(var29 >> 12 << var41) + (var42 >> 12) & var24];
				var26 = var36;
				var30 = var37;
				var32 = var32;
				var42 = var38;
				var29 = var39;
			}

			if(y_start > 0) {
				var38 = (int) (((long) var27 << 12) / (long) var33);
				var39 = (int) (((long) var31 << 12) / (long) var33);
				var26 = (var38 - var42) / y_start;

				for(var30 = (var39 - var29) / y_start; var34 < var35; ++var34) {
					var25[var34] = var23[(var29 >> 12 << var41) + (var42 >> 12) & var24];
					var42 += var26;
					var29 += var30;
				}
			}

			x_start += dx_start;
			wz_start += dwz_start;
			uz_start += duz_start;
			vz_start += dvz_start;
			x_end += dx_end;
			wz_end += dwz_end;
			uz_end += duz_end;
			vz_end += dvz_end;
		}

	}

	private Texture(Image img) {
		this.perspectiveCorrection = false;
		this.w = img.getWidth();
		this.h = img.getHeight();
		this.widthBIT = widthToBIT(this.w);
		this.pixels = new int[this.w * this.h];
		img.getRGB(this.pixels, 0, this.w, 0, 0, this.w, this.h);
	}

	public void destroy() {
		this.pixels = null;
	}

	private static int widthToBIT(int w) {
		for(int var1 = 0; var1 < 32; ++var1) {
			if(w >> var1 == 1 && 1 << var1 == w) {
				return var1;
			}
		}

		return 0;
	}

	public int[] getPixels() {
		return this.pixels;
	}

	public void setPerspectiveCorrection(boolean perspectiveCorrection) {
		this.perspectiveCorrection = perspectiveCorrection;
	}

	public static Texture createTexture(String file) {
		try {
			return new Texture(Image.createImage(file));
		} catch(Exception var2) {
			System.err.println("ERROR in createTexture " + file + ": " + var2);
			return null;
		}
	}
}
