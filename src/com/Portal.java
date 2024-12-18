package com;

public final class Portal {

	private static boolean a_boolean_fld = true;
	private Room room;
	private Vertex[] poly;
	private Vertex[] b_Array_of_ak_fld;
	private int a_int_fld;
	private Vector3D normal;
	private int minx;
	private int maxx;
	private int miny;
	private int maxy;
	private boolean[] a_Array_of_boolean_fld = new boolean[8];
	private boolean b_boolean_fld;
	private int f_int_fld;
	private int g_int_fld;

	public Portal(Vertex[] meshes) {
		this.poly = meshes;
		Vertex var10001 = this.poly[0];
		Vertex var10002 = this.poly[1];
		Vertex var4 = this.poly[2];
		Vertex var3 = var10002;
		Vertex var2 = var10001;
		this.normal = MathUtils.createNormal(var10001.x, var2.y, var2.z, var3.x, var3.y, var3.z, var4.x, var4.y, var4.z);
		if(meshes.length != 4) {
			System.out.println("PORTAL: предупреждение: нестандартное количество вершин в портале " + meshes.length);
		}

		this.b_Array_of_ak_fld = new Vertex[8];

		for(int var5 = 0; var5 < this.b_Array_of_ak_fld.length; ++var5) {
			this.b_Array_of_ak_fld[var5] = new Vertex();
		}

	}

	public final void destroy() {
		this.room = null;

		for(int var1 = 0; var1 < this.poly.length; ++var1) {
			this.poly[var1] = null;
		}

		this.poly = null;
		this.b_Array_of_ak_fld = null;
	}

	public final Vertex[] getVertices() {
		return this.poly;
	}

	public final Room getRoom() {
		return this.room;
	}

	public final void setRoom(Room room) {
		this.room = room;
	}

	public final int getMinX() {
		return this.minx;
	}

	public final int getMinY() {
		return this.miny;
	}

	public final int getMaxX() {
		return this.maxx;
	}

	public final int getMaxY() {
		return this.maxy;
	}

	public final boolean isVisible(Graphics3D g3d, int x1, int y1, int x2, int y2) {
		Matrix var6 = g3d.getCamera();
		int var9 = var6.m23;
		int var8 = var6.m13;
		int var7 = var6.m03;
		Vertex var10 = this.poly[0];
		int var11 = var7 - var10.x;
		int var12 = var8 - var10.y;
		int var13 = var9 - var10.z;
		if(Math.abs(var11 * this.normal.x + var12 * this.normal.y + var13 * this.normal.z >> 12) < 300) {
			this.minx = x1;
			this.miny = y1;
			this.maxx = x2;
			this.maxy = y2;
			this.a_int_fld = 0;
			return true;
		} else {
			Vertex[] var43 = this.b_Array_of_ak_fld;
			Vertex[] var41 = this.poly;
			int var42 = y2;
			var9 = x2;
			var8 = y1;
			var7 = x1;
			Graphics3D var37 = g3d;
			Matrix var46 = g3d.getInvCamera();

			int var14;
			for(var14 = 0; var14 < var41.length; ++var14) {
				var41[var14].transform(var46);
			}

			var14 = 0;

			Vertex var17;
			int var16;
			Vertex var18;
			for(var13 = 0; var13 < var41.length; ++var13) {
				if((var16 = var13 + 1) > var41.length - 1) {
					var16 = 0;
				}

				var17 = var41[var13];
				var18 = var41[var16];
				if(var17.rz <= 0 || var18.rz <= 0) {
					if(var17.rz < 0 && var18.rz < 0) {
						a_void_sub(var43[var14], var17);
						++var14;
					}

					if(var17.rz < 0 && var18.rz > 0) {
						a_void_sub(var43[var14], var17);
						++var14;
						a_void_sub(var17, var18, var43[var14]);
						++var14;
					}

					if(var17.rz > 0 && var18.rz < 0) {
						a_void_sub(var17, var18, var43[var14]);
						++var14;
					}
				}
			}

			Vertex var15;
			for(var13 = 0; var13 < var14; ++var13) {
				(var15 = var43[var13]).project(var37);
				if(var15.rz >= 0) {
					if(var15.sx > var7 && var15.sx < var9) {
						var15.sx = var15.sx > (var9 + var7) / 2 ? var9 : var7;
					}

					if(var15.sy > var8 && var15.sy < var42) {
						var15.sy = var15.sy > (var42 + var8) / 2 ? var42 : var8;
					}
				}
			}

			Vertex var51;
			for(var13 = 0; var13 < var14; ++var13) {
				var15 = a_ak_sub(var43, var14, var13);
				var51 = a_ak_sub(var43, var14, var13 + 1);
				if(var15.sx < var7 && var51.sx < var7) {
					var17 = a_ak_sub(var43, var14, var13 - 1);
					var18 = a_ak_sub(var43, var14, var13 + 2);
					a_void_sub(var15, var17, var7);
					a_void_sub(var51, var18, var7);
				}

				if(var15.sx > var9 && var51.sx > var9) {
					var17 = a_ak_sub(var43, var14, var13 - 1);
					var18 = a_ak_sub(var43, var14, var13 + 2);
					a_void_sub(var15, var17, var9 - 1);
					a_void_sub(var51, var18, var9 - 1);
				}
			}

			this.a_int_fld = var14;
			if(this.a_int_fld < 3) {
				return false;
			} else {
				Portal var36 = this;
				Vertex var44 = this.b_Array_of_ak_fld[0];
				this.maxx = var44.sx;
				this.minx = var44.sx;
				this.maxy = var44.sy;
				this.miny = var44.sy;

				for(var12 = 1; var12 < var36.a_int_fld; ++var12) {
					if((var44 = var36.b_Array_of_ak_fld[var12]).sx < var36.minx) {
						var36.minx = var44.sx;
					}

					if(var44.sx > var36.maxx) {
						var36.maxx = var44.sx;
					}

					if(var44.sy < var36.miny) {
						var36.miny = var44.sy;
					}

					if(var44.sy > var36.maxy) {
						var36.maxy = var44.sy;
					}
				}

				if(var36.maxx >= x1 && var36.maxy >= y1 && var36.minx <= x2 && var36.miny <= y2) {
					boolean var57;
					label453:
					{
						var36 = this;
						if(a_boolean_fld) {
							for(var8 = 0; var8 < var36.a_Array_of_boolean_fld.length; ++var8) {
								var36.a_Array_of_boolean_fld[var8] = false;
							}

							var36.b_boolean_fld = false;
							var36.f_int_fld = var36.g_int_fld = 0;

							for(var8 = 0; var8 < var36.a_int_fld; ++var8) {
								Vertex var40 = var36.b_Array_of_ak_fld[var8];
								var36.f_int_fld += var40.sx;
								var36.g_int_fld += var40.sy;
							}

							var36.f_int_fld /= var36.a_int_fld;
							var36.g_int_fld /= var36.a_int_fld;
							TMPElement[] var39 = g3d.getBuffer();
							var9 = g3d.getSize();
							var42 = var36.poly[0].rz;

							for(var11 = 1; var11 < var36.poly.length; ++var11) {
								if((var12 = var36.poly[var11].rz) < var42) {
									var42 = var12;
								}
							}

							for(var11 = 0; var11 < var9; ++var11) {
								Polygon4V var47;
								int var10000;
								RenderObject var45;
								RenderObject var50;
								Polygon3V var48;
								if((var50 = var45 = var39[var11].obj) instanceof Polygon3V) {
									var16 = (var48 = (Polygon3V) var50).a.rz;
									if(var48.b.rz > var16) {
										var16 = var48.b.rz;
									}

									if(var48.c.rz > var16) {
										var16 = var48.c.rz;
									}

									var10000 = var16;
								} else if(var50 instanceof Polygon4V) {
									var16 = (var47 = (Polygon4V) var50).a.rz;
									if(var47.b.rz > var16) {
										var16 = var47.b.rz;
									}

									if(var47.c.rz > var16) {
										var16 = var47.c.rz;
									}

									if(var47.d.rz > var16) {
										var16 = var47.d.rz;
									}

									var10000 = var16;
								} else {
									var10000 = 0;
								}

								var13 = var10000;
								if(var10000 > var42) {
									if(var13 - var42 <= 10000) {
										label417:
										{
											if(var45 instanceof Polygon3V) {
												var48 = (Polygon3V) var45;
												if(var36.a_boolean_sub(var48.a)) {
													var57 = true;
													break label417;
												}

												if(var36.a_boolean_sub(var48.b)) {
													var57 = true;
													break label417;
												}

												if(var36.a_boolean_sub(var48.c)) {
													var57 = true;
													break label417;
												}
											} else if(var45 instanceof Polygon4V) {
												var47 = (Polygon4V) var45;
												if(var36.a_boolean_sub(var47.a)) {
													var57 = true;
													break label417;
												}

												if(var36.a_boolean_sub(var47.b)) {
													var57 = true;
													break label417;
												}

												if(var36.a_boolean_sub(var47.c)) {
													var57 = true;
													break label417;
												}

												if(var36.a_boolean_sub(var47.d)) {
													var57 = true;
													break label417;
												}
											}

											var57 = false;
										}

										if(var57) {
											continue;
										}
									}

									int var19;
									int var21;
									int var20;
									int var23;
									int var22;
									int var25;
									int var24;
									int var27;
									int var26;
									Vertex var34;
									Vertex var32;
									int var49;
									int var53;
									int var52;
									if(var45 instanceof Polygon4V) {
										var51 = (var47 = (Polygon4V) var45).a;
										var17 = var47.b;
										var18 = var47.c;
										Vertex var30 = var47.d;
										Vertex var35 = var30;
										var34 = var18;
										Vertex var33 = var17;
										var32 = var51;
										if(a_boolean_sub(var51.sx, var51.sy, var17.sx, var17.sy, var18.sx, var18.sy)) {
											var35 = var51;
											var32 = var30;
											var34 = var17;
											var33 = var18;
										}

										Portal var31 = var36;
										var12 = var32.sx;
										x1 = var32.sy;
										var13 = var33.sx;
										y1 = var33.sy;
										var14 = var34.sx;
										var49 = var34.sy;
										var16 = var35.sx;
										var53 = var35.sy;
										var52 = var12;
										if(var13 < var12) {
											var52 = var13;
										}

										if(var14 < var52) {
											var52 = var14;
										}

										if(var16 < var52) {
											var52 = var16;
										}

										var19 = x1;
										if(y1 < x1) {
											var19 = y1;
										}

										if(var49 < var19) {
											var19 = var49;
										}

										if(var53 < var19) {
											var19 = var53;
										}

										var20 = var12;
										if(var13 > var12) {
											var20 = var13;
										}

										if(var14 > var20) {
											var20 = var14;
										}

										if(var16 > var20) {
											var20 = var16;
										}

										var21 = x1;
										if(y1 > x1) {
											var21 = y1;
										}

										if(var49 > var21) {
											var21 = var49;
										}

										if(var53 > var21) {
											var21 = var53;
										}

										var22 = var13 - var12;
										var23 = var14 - var13;
										var24 = var16 - var14;
										var25 = var12 - var16;
										var26 = y1 - x1;
										var27 = var49 - y1;
										x2 = var53 - var49;
										y2 = x1 - var53;

										int var56;
										for(var7 = 0; var7 < var31.a_int_fld; ++var7) {
											if(!var31.a_Array_of_boolean_fld[var7]) {
												Vertex var28;
												int var29 = (var28 = var31.b_Array_of_ak_fld[var7]).sx;
												var56 = var28.sy;
												if(var29 >= var52 && var56 >= var19 && var29 <= var20 && var56 <= var21) {
													var31.a_Array_of_boolean_fld[var7] = var22 * (var56 - x1) <= (var29 - var12) * var26 && var23 * (var56 - y1) <= (var29 - var13) * var27 && var24 * (var56 - var49) <= (var29 - var14) * x2 && var25 * (var56 - var53) <= (var29 - var16) * y2;
												}
											}
										}

										if(!var31.b_boolean_fld) {
											var7 = var31.f_int_fld;
											var56 = var31.g_int_fld;
											if(var7 >= var52 && var56 >= var19 && var7 <= var20 && var56 <= var21) {
												var31.b_boolean_fld = var22 * (var56 - x1) <= (var7 - var12) * var26 && var23 * (var56 - y1) <= (var7 - var13) * var27 && var24 * (var56 - var49) <= (var7 - var14) * x2 && var25 * (var56 - var53) <= (var7 - var16) * y2;
											}
										}
									} else if(var45 instanceof Polygon3V) {
										var51 = (var48 = (Polygon3V) var45).a;
										var17 = var48.b;
										var18 = var48.c;
										var34 = var18;
										var32 = var51;
										if(a_boolean_sub(var51.sx, var51.sy, var17.sx, var17.sy, var18.sx, var18.sy)) {
											var34 = var51;
											var32 = var18;
										}

										Portal var38 = var36;
										x2 = var32.sx;
										y2 = var32.sy;
										var12 = var17.sx;
										x1 = var17.sy;
										var13 = var34.sx;
										y1 = var34.sy;
										var14 = x2;
										if(var12 < x2) {
											var14 = var12;
										}

										if(var13 < var14) {
											var14 = var13;
										}

										var49 = y2;
										if(x1 < y2) {
											var49 = x1;
										}

										if(y1 < var49) {
											var49 = y1;
										}

										var16 = x2;
										if(var12 > x2) {
											var16 = var12;
										}

										if(var13 > var16) {
											var16 = var13;
										}

										var53 = y2;
										if(x1 > y2) {
											var53 = x1;
										}

										if(y1 > var53) {
											var53 = y1;
										}

										var52 = var12 - x2;
										var19 = var13 - var12;
										var20 = x2 - var13;
										var21 = x1 - y2;
										var22 = y1 - x1;
										var23 = y2 - y1;

										for(var24 = 0; var24 < var38.a_int_fld; ++var24) {
											if(!var38.a_Array_of_boolean_fld[var24]) {
												Vertex var55;
												var26 = (var55 = var38.b_Array_of_ak_fld[var24]).sx;
												var27 = var55.sy;
												if(var26 >= var14 && var27 >= var49 && var26 <= var16 && var27 <= var53) {
													var38.a_Array_of_boolean_fld[var24] = var52 * (var27 - y2) <= (var26 - x2) * var21 && var19 * (var27 - x1) <= (var26 - var12) * var22 && var20 * (var27 - y1) <= (var26 - var13) * var23;
												}
											}
										}

										if(!var38.b_boolean_fld) {
											var24 = var38.f_int_fld;
											var25 = var38.g_int_fld;
											if(var24 >= var14 && var25 >= var49 && var24 <= var16 && var25 <= var53) {
												var38.b_boolean_fld = var52 * (var25 - y2) <= (var24 - x2) * var21 && var19 * (var25 - x1) <= (var24 - var12) * var22 && var20 * (var25 - y1) <= (var24 - var13) * var23;
											}
										}
									}

									Portal var54 = var36;
									if(!var36.b_boolean_fld) {
										var57 = false;
									} else {
										var13 = 0;

										while(true) {
											if(var13 >= var54.a_int_fld) {
												var57 = true;
												break;
											}

											if(!var54.a_Array_of_boolean_fld[var13]) {
												var57 = false;
												break;
											}

											++var13;
										}
									}

									if(var57) {
										var57 = true;
										break label453;
									}
								}
							}
						}

						var57 = false;
					}

					if(!var57) {
						return true;
					}
				}

				return false;
			}
		}
	}

	private final boolean a_boolean_sub(Vertex var1) {
		for(int var2 = 0; var2 < this.poly.length; ++var2) {
			Vertex var3;
			int var4 = (var3 = this.poly[var2]).x - var1.x;
			int var5 = var3.y - var1.y;
			int var6 = var3.z - var1.z;
			if(var4 < 0) {
				var4 = -var4;
			}

			if(var5 < 0) {
				var5 = -var5;
			}

			if(var6 < 0) {
				var6 = -var6;
			}

			if(var4 < 400 && var5 < 400 && var6 < 400) {
				return true;
			}
		}

		return false;
	}

	private static final boolean a_boolean_sub(int var0, int var1, int var2, int var3, int var4, int var5) {
		return (var0 - var2) * (var3 - var5) > (var1 - var3) * (var2 - var4);
	}

	private static final Vertex a_ak_sub(Vertex[] var0, int var1, int var2) {
		if(var2 < 0) {
			var2 += var1;
		}

		var2 %= var1;
		return var0[var2];
	}

	private static final void a_void_sub(Vertex var0, Vertex var1, int var2) {
		int var10001;
		if(var0.sx != var1.sx && var0.sy != var1.sy) {
			int var3 = (var1.sy - var0.sy << 12) / (var1.sx - var0.sx);
			var10001 = var0.sy + (var3 * (var2 - var0.sx) >> 12);
		} else {
			var10001 = var0.sy;
		}

		var0.sy = var10001;
		var0.sx = var2;
	}

	private static final void a_void_sub(Vertex var0, Vertex var1, Vertex var2) {
		int var3;
		if((var3 = var1.rz - var0.rz) == 0) {
			var3 = 1;
		}

		int var4 = (var1.sx - var0.sx << 12) / var3;
		int var6 = (var1.sy - var0.sy << 12) / var3;
		var3 = var0.sx - (var4 * var0.rz >> 12);
		int var5 = var0.sy - (var6 * var0.rz >> 12);
		var2.x = var2.sx = var3;
		var2.y = var2.sy = var5;
		var2.z = var2.rz = 0;
	}

	private static final void a_void_sub(Vertex var0, Vertex var1) {
		var0.x = var0.sx = var1.sx;
		var0.y = var0.sy = var1.sy;
		var0.z = var0.rz = var1.rz;
	}
}
