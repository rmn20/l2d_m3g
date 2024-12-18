package com;

import java.util.Vector;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

// ? Хранилище
public class Asset {

	private int[] vertexCoords; // координаты всех вершин в одном массиве: {x0, y0, z0, x1, y1, z1, ...}
	private short[] poly3vData;
	/*              ^ полигоны 3V в одном массиве:
	 * {  ИндексВершины_a0, ИндексВершины_b0, ИндексВершины_c0, au0, av0, bu0, bv0, cu0, cv0,
	 *    ИндексВершины_a1, ИндексВершины_b1, ИндексВершины_c1, au1, av1, bu1, bv1, cu1, cv1, 
	 *    ...
	 * }
	 */
	private short[] poly4vData;
	/*              ^ полигоны 4V в одном массиве:
	 * { ИндексВершины_a0, ИндексВершины_b0, ИндексВершины_c0, ИндексВершины_d0, au0, av0, bu0, bv0, cu0, cv0, du0, dv0, 
	 *   ИндексВершины_a1, ИндексВершины_b1, ИндексВершины_c1, ИндексВершины_d1, au1, av1, bu1, bv1, cu1, cv1, du1, dv1,
	 *    ...
	 * }
	 */
	private Texture texture;

	public Asset(Mesh mesh) {
		Vertex[] var2 = mesh.getVertices();
		RenderObject[] var3 = mesh.getPoligons();
		this.vertexCoords = verticesToArray(var2);
		this.poly3vData = polygons3VToArray(var2, var3);
		this.poly4vData = polygons4VToArray(var2, var3);
		this.texture = mesh.getTexture();
	}

	// из Morphing:
	// private static short[] verticesToArray(Vertex[] vertices)
	private static int[] verticesToArray(Vertex[] vertices) {
		int[] var1 = new int[vertices.length * 3];

		for(int var2 = 0; var2 < vertices.length; ++var2) {
			Vertex var3 = vertices[var2];
			var1[var2 * 3] = var3.x;
			var1[var2 * 3 + 1] = var3.y;
			var1[var2 * 3 + 2] = var3.z;
		}

		return var1;
	}

	// ?
	private static short[] polygons3VToArray(Vertex[] vertices, RenderObject[] polygons) {
		Vector var2 = new Vector();

		for(int var3 = 0; var3 < polygons.length; ++var3) {
			if(polygons[var3] instanceof Polygon3V) {
				var2.addElement(polygons[var3]);
			}
		}

		short[] var6 = new short[var2.size() * 9];

		for(int var5 = 0; var5 < var2.size(); ++var5) {
			Polygon3V var4 = (Polygon3V) var2.elementAt(var5);
			var6[var5 * 9] = (short) search(vertices, var4.a);
			var6[var5 * 9 + 1] = (short) search(vertices, var4.b);
			var6[var5 * 9 + 2] = (short) search(vertices, var4.c);
			var6[var5 * 9 + 3] = (short) var4.au;
			var6[var5 * 9 + 4] = (short) var4.av;
			var6[var5 * 9 + 5] = (short) var4.bu;
			var6[var5 * 9 + 6] = (short) var4.bv;
			var6[var5 * 9 + 7] = (short) var4.cu;
			var6[var5 * 9 + 8] = (short) var4.cv;
		}

		return var6;
	}

	// ?
	private static short[] polygons4VToArray(Vertex[] vertices, RenderObject[] polygons) {
		Vector var2 = new Vector();

		for(int var3 = 0; var3 < polygons.length; ++var3) {
			if(polygons[var3] instanceof Polygon4V) {
				var2.addElement(polygons[var3]);
			}
		}

		short[] var6 = new short[var2.size() * 12];

		for(int var5 = 0; var5 < var2.size(); ++var5) {
			Polygon4V var4 = (Polygon4V) var2.elementAt(var5);
			var6[var5 * 12] = (short) search(vertices, var4.a);
			var6[var5 * 12 + 1] = (short) search(vertices, var4.b);
			var6[var5 * 12 + 2] = (short) search(vertices, var4.c);
			var6[var5 * 12 + 3] = (short) search(vertices, var4.d);
			var6[var5 * 12 + 4] = (short) var4.au;
			var6[var5 * 12 + 5] = (short) var4.av;
			var6[var5 * 12 + 6] = (short) var4.bu;
			var6[var5 * 12 + 7] = (short) var4.bv;
			var6[var5 * 12 + 8] = (short) var4.cu;
			var6[var5 * 12 + 9] = (short) var4.cv;
			var6[var5 * 12 + 10] = (short) var4.du;
			var6[var5 * 12 + 11] = (short) var4.dv;
		}

		return var6;
	}

	// из Mesh:
	// private static int search(Object[] objs, Object obj)
	private static int search(Object[] objs, Object obj) {
		for(int var2 = 0; var2 < objs.length; ++var2) {
			if(objs[var2] == obj) {
				return var2;
			}
		}

		return -1;
	}

	// ? из Mesh: public Mesh copy()
	public final Mesh copy() {
		int[] var3 = this.vertexCoords;
		Vertex[] var4 = new Vertex[this.vertexCoords.length / 3];

		int var5;
		for(var5 = 0; var5 < var4.length; ++var5) {
			var4[var5] = new Vertex(var3[var5 * 3], var3[var5 * 3 + 1], var3[var5 * 3 + 2]);
		}

		Vertex[] var1;
		Vertex[] var10000 = var1 = var4;
		short[] var13 = this.poly3vData;
		Vertex[] var11 = var10000;
		Polygon3V[] var16 = new Polygon3V[var13.length / 9];

		int var6;
		short var7;
		short var8;
		short var9;
		for(var6 = 0; var6 < var16.length; ++var6) {
			var7 = var13[var6 * 9];
			var8 = var13[var6 * 9 + 1];
			var9 = var13[var6 * 9 + 2];
			var16[var6] = new Polygon3V(var11[var7], var11[var8], var11[var9], (byte) var13[var6 * 9 + 3], (byte) var13[var6 * 9 + 4], (byte) var13[var6 * 9 + 5], (byte) var13[var6 * 9 + 6], (byte) var13[var6 * 9 + 7], (byte) var13[var6 * 9 + 8]);
		}

		Polygon3V[] var2 = var16;
		var13 = this.poly4vData;
		var11 = var1;
		Polygon4V[] var15 = new Polygon4V[var13.length / 12];

		for(var6 = 0; var6 < var15.length; ++var6) {
			var7 = var13[var6 * 12];
			var8 = var13[var6 * 12 + 1];
			var9 = var13[var6 * 12 + 2];
			short var10 = var13[var6 * 12 + 3];
			var15[var6] = new Polygon4V(var11[var7], var11[var8], var11[var9], var11[var10], (byte) var13[var6 * 12 + 4], (byte) var13[var6 * 12 + 5], (byte) var13[var6 * 12 + 6], (byte) var13[var6 * 12 + 7], (byte) var13[var6 * 12 + 8], (byte) var13[var6 * 12 + 9], (byte) var13[var6 * 12 + 10], (byte) var13[var6 * 12 + 11]);
		}

		Polygon4V[] var12 = var15;
		RenderObject[] var14 = new RenderObject[var16.length + var15.length];
		var5 = 0;

		for(var6 = 0; var6 < var2.length; ++var5) {
			var14[var5] = var2[var6];
			++var6;
		}

		for(var6 = 0; var6 < var12.length; ++var5) {
			var14[var5] = var12[var6];
			++var6;
		}

		Mesh var17;
		(var17 = new Mesh(var1, var14)).setTexture(this.texture);
		return var17;
	}

	public Asset() {
	}

	public static int getNumRecords(String recordStoreName) {
		try {
			RecordStore var3;
			int var1 = (var3 = RecordStore.openRecordStore(recordStoreName, true)).getNumRecords();
			var3.closeRecordStore();
			return var1;
		} catch(RecordStoreException var2) {
			var2.printStackTrace();
			return 0;
		}
	}

	public static byte[] getRecord(String recordStoreName, int recordId) {
		RecordStore var14 = null;
		byte[] var2 = null;
		boolean var8 = false;

		label77:
		{
			try {
				var8 = true;
				var2 = (var14 = RecordStore.openRecordStore(recordStoreName, true)).getRecord(1);
				var8 = false;
				break label77;
			} catch(Exception var12) {
				var12.printStackTrace();
				var8 = false;
			} finally {
				if(var8) {
					try {
						var14.closeRecordStore();
					} catch(Exception var9) {
						var9.printStackTrace();
					}

				}
			}

			try {
				var14.closeRecordStore();
			} catch(Exception var10) {
				var10.printStackTrace();
			}

			return var2;
		}

		try {
			var14.closeRecordStore();
		} catch(Exception var11) {
			var11.printStackTrace();
		}

		return var2;
	}
}
