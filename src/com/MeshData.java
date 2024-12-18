package com;

import java.io.DataInputStream;
import java.io.InputStream;
import java.util.Vector;
import javax.microedition.m3g.Appearance;
import javax.microedition.m3g.CompositingMode;
import javax.microedition.m3g.Material;
import javax.microedition.m3g.Mesh;
import javax.microedition.m3g.PolygonMode;
import javax.microedition.m3g.TriangleStripArray;
import javax.microedition.m3g.VertexArray;
import javax.microedition.m3g.VertexBuffer;

public class MeshData {

	//todo optimize data storage and loading
	private Texture texture;
	
	private float scale;
	private short[] verts;
	private short[] p3v, p4v;
	private byte[] p3uv, p4uv; //used for animation loading
	private short[] p3vNorms, p4vNorms; //used for physics
	
	private Mesh meshM3G;

	// из ResourceLoader
	public static String getStringFromResource(String file) {
		InputStream var1 = null;

		try {
			var1 = (new Object()).getClass().getResourceAsStream(file);
			StringBuffer var5 = new StringBuffer();

			int var2;
			while((var2 = var1.read()) != -1) {
				if(var2 != 13) {
					var2 = var2 >= 192 && var2 <= 255 ? var2 + 848 : var2;
					var5.append((char) var2);
				}
			}

			var1.close();
			return var5.toString();
		} catch(Exception var4) {
			System.out.println("ERROR in getStr: " + var4);
			if(var1 != null) {
				try {
					var1.close();
				} catch(Exception var3) {
					;
				}
			}

			return null;
		}
	}

	/* Возращает вектор, содержащий фрагменты строки str,
	 заключенные между друмя символами d или между первым символом в str 
	 и первым символом d (для первого фрагмента)
	 */
	private static Vector fragments(String str, char d) {
		Vector var2 = new Vector();
		int var5 = 0;

		while(var5 < str.length()) {
			while(var5 < str.length() && str.charAt(var5) == d) {
				++var5;
			}

			int var3;
			for(var3 = var5; var5 < str.length() && str.charAt(var5) != d; ++var5) {
				;
			}

			if(var3 < var5) {
				String var6 = str.substring(var3, var5);
				var2.addElement(var6);
			}
		}

		return var2;
	}

	public static String[] cutOnStrings(String str, char d) {
		Vector var2;
		String[] var3 = new String[(var2 = fragments(str, d)).size()];
		var2.copyInto(var3);
		return var3;
	}

	public static int[] cutOnInts(String str, char d) {
		Vector var3;
		int[] var4 = new int[(var3 = fragments(str, ',')).size()];

		for(int var2 = 0; var2 < var4.length; ++var2) {
			var4[var2] = Integer.parseInt((String) var3.elementAt(var2));
		}

		return var4;
	}

	private MeshData(short[] vertices, float scale, short[] p4v, short[] p3v, byte[] p4uv, byte[] p3uv, Mesh meshM3G) {
		this.verts = vertices;
		this.scale = scale;
		this.p4v = p4v;
		this.p3v = p3v;
		this.p4uv = p4uv;
		this.p3uv = p3uv;
		this.meshM3G = meshM3G;
	}
	
	public void calculateNormals() {
		short[] verts = this.verts;
		
		short[] p4vNorms = new short[p4v.length / 4 * 3];
		short[] p3vNorms = new short[p3v.length / 3 * 3];
		
		for(int vtxPerPoly = 4; vtxPerPoly >= 3; vtxPerPoly--) {
			
			short[] polys = vtxPerPoly == 4 ? p4v : p3v;
			short[] polyNorms = vtxPerPoly == 4 ? p4vNorms : p3vNorms;
			
			for(int i = 0, poly = 0; i < polys.length; i += vtxPerPoly, poly++) {
				int ax = verts[polys[i] * 3 + 0], 
					ay = verts[polys[i] * 3 + 1], 
					az = verts[polys[i] * 3 + 2];

				int bx = verts[polys[i + 1] * 3 + 0], 
					by = verts[polys[i + 1] * 3 + 1], 
					bz = verts[polys[i + 1] * 3 + 2];

				int cx = verts[polys[i + vtxPerPoly - 1] * 3 + 0], 
					cy = verts[polys[i + vtxPerPoly - 1] * 3 + 1], 
					cz = verts[polys[i + vtxPerPoly - 1] * 3 + 2];

				Vector3D norm = MathUtils.createNormal(
					ax, ay, az, 
					bx, by, bz, 
					cx, cy, cz
				);
				
				polyNorms[poly * 3] = (short) norm.x;
				polyNorms[poly * 3 + 1] = (short) norm.y;
				polyNorms[poly * 3 + 2] = (short) norm.z;
			}
		}
		
		this.p4vNorms = p4vNorms;
		this.p3vNorms = p3vNorms;
	}

	public void destroy() {
		verts = null;
		p4v = p3v = null;
		p4uv = p3uv = null;
		p4vNorms = p3vNorms = null;
		texture = null;
		meshM3G = null;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
		if(meshM3G != null) meshM3G.getAppearance(0).setTexture(0, texture.tex); //todo why
	}

	public void calculateAABB(Vector3D min, Vector3D max) {
		short[] verts = this.verts;
		
		int minX = Short.MAX_VALUE, minY = Short.MAX_VALUE, minZ = Short.MAX_VALUE;
		int maxX = Short.MIN_VALUE, maxY = Short.MIN_VALUE, maxZ = Short.MIN_VALUE;

		for(int i = 0; i < verts.length; i += 3) {
			int x = verts[i];
			
			if(x > maxX) maxX = x;
			if(x < minX) minX = x;
			
			int y = verts[i + 1];
			
			if(y > maxY) maxY = y;
			if(y < minY) minY = y;
			
			int z = verts[i + 2];
			
			if(z > maxZ) maxZ = z;
			if(z < minZ) minZ = z;
		}

		float scale = this.scale;
		
		min.set((int) (minX * scale), (int) (minY * scale), (int) (minZ * scale));
		max.set((int) (maxX * scale), (int) (maxY * scale), (int) (maxZ * scale));
	}
	
	public float getScale() {
		return scale;
	}

	public Texture getTexture() {
		return this.texture;
	}

	public short[] getVertices() {
		return this.verts;
	}

	public short[] get4VPols() {
		return p4v;
	}

	public short[] get3VPols() {
		return p3v;
	}

	public short[] getP4VNorms() {
		return p4vNorms;
	}

	public short[] getP3VNorms() {
		return p3vNorms;
	}

	public static MeshData loadMesh(String file, float scale, int textureSize, boolean persCorrection) {
		MeshData mesh = null;
		InputStream is = null;
		DataInputStream dis = null;

		try {
			is = (new Object()).getClass().getResourceAsStream(file);
			dis = new DataInputStream(is);
			
			mesh = createFrom3d(file, dis, scale, textureSize, null, persCorrection);
			
			mesh.p4uv = null;
			mesh.p3uv = null;
		} catch(Exception ex) {
			System.err.println("ERROR in Loader.Load: " + ex);
			ex.printStackTrace();
		} finally {
			try {
				dis.close();
				is.close();
			} catch(Exception ex) {
			}
		}

		return mesh;
	}

	public static MeshData[] loadMeshes(String file, float scale, int textureSize, boolean animation, boolean persCorrection) {
		MeshData[] meshes = null;
		InputStream is = null;
		DataInputStream dis = null;

		try {
			is = (new Object()).getClass().getResourceAsStream(file);
			dis = new DataInputStream(is);
			meshes = new MeshData[dis.readInt()];

			for(int i = 0; i < meshes.length; i++) {
				MeshData firstMesh = (i > 0 && animation) ? meshes[0] : null;
				meshes[i] = createFrom3d(file, dis, scale, textureSize, firstMesh, persCorrection);
			}

			for(int i = 0; i < meshes.length; i++) {
				MeshData mesh = meshes[i];
				mesh.p4uv = null;
				mesh.p3uv = null;
			}
		} catch(Exception ex) {
			System.err.println("ERROR in Loader.Load: " + ex);
			ex.printStackTrace();
		} finally {
			try {
				dis.close();
				is.close();
			} catch(Exception ex) {
			}
		}

		return meshes;
	}

	private static MeshData createFrom3d(
			String file, DataInputStream is, 
			float scale, int textureSize, 
			MeshData animBaseMesh,
			boolean persCorrection
		) throws Exception {
		
		//Load vertices
		short[] verts = new short[is.readShort() * 3];

		Vector3D minV = new Vector3D(Short.MAX_VALUE, Short.MAX_VALUE, Short.MAX_VALUE);
		Vector3D maxV = new Vector3D(Short.MIN_VALUE, Short.MIN_VALUE, Short.MIN_VALUE);
		
		for(int i = 0; i < verts.length; i += 3) {
			short x = is.readShort();
			short y = is.readShort();
			short z = is.readShort();
			
			verts[i + 0] = x;
			verts[i + 1] = y;
			verts[i + 2] = z;
			
			if(x < minV.x) minV.x = x;
			if(x > maxV.x) maxV.x = x;
			if(y < minV.y) minV.y = y;
			if(y > maxV.y) maxV.y = y;
			if(z < minV.z) minV.z = z;
			if(z > maxV.z) maxV.z = z;
		}

		//Load 3v polygons data
		short[] pols3v = new short[is.readShort() * 3];
		byte[] p3uv = new byte[pols3v.length * 2];

		for(int i = 0; i < pols3v.length; i += 3) {
			short v3Index = is.readShort();
			short v2Index = is.readShort();
			short v1Index = is.readShort();
			
			pols3v[i + 0] = v1Index;
			pols3v[i + 1] = v2Index;
			pols3v[i + 2] = v3Index;
			
			p3uv[i * 2 + 4] = (byte) ((is.readByte() & 0xff) - 128);
			p3uv[i * 2 + 5] = (byte) ((is.readByte() & 0xff) - 128);
			
			p3uv[i * 2 + 2] = (byte) ((is.readByte() & 0xff) - 128);
			p3uv[i * 2 + 3] = (byte) ((is.readByte() & 0xff) - 128);
			
			p3uv[i * 2 + 0] = (byte) ((is.readByte() & 0xff) - 128);
			p3uv[i * 2 + 1] = (byte) ((is.readByte() & 0xff) - 128);
		}

		//Load 4v polygons data
		short[] pols4v = new short[is.readShort() * 4];
		byte[] p4uv = new byte[pols4v.length * 2];

		for(int i = 0; i < pols4v.length; i += 4) {
			short v4Index = is.readShort();
			short v3Index = is.readShort();
			short v2Index = is.readShort();
			short v1Index = is.readShort();
			
			pols4v[i + 0] = v1Index;
			pols4v[i + 1] = v2Index;
			pols4v[i + 2] = v3Index;
			pols4v[i + 3] = v4Index;
			
			p4uv[i * 2 + 6] = (byte) ((is.readByte() & 0xff) - 128);
			p4uv[i * 2 + 7] = (byte) ((is.readByte() & 0xff) - 128);
			
			p4uv[i * 2 + 4] = (byte) ((is.readByte() & 0xff) - 128);
			p4uv[i * 2 + 5] = (byte) ((is.readByte() & 0xff) - 128);
			
			p4uv[i * 2 + 2] = (byte) ((is.readByte() & 0xff) - 128);
			p4uv[i * 2 + 3] = (byte) ((is.readByte() & 0xff) - 128);
			
			p4uv[i * 2 + 0] = (byte) ((is.readByte() & 0xff) - 128);
			p4uv[i * 2 + 1] = (byte) ((is.readByte() & 0xff) - 128);
		}
		
		short[] genPols3v = (animBaseMesh != null) ? animBaseMesh.p3v : pols3v;
		short[] genPols4v = (animBaseMesh != null) ? animBaseMesh.p4v : pols4v;
		
		Mesh mesh = null;
		
		if(genPols3v.length + genPols4v.length > 0) {
			VertexArray poses = new VertexArray(genPols3v.length + genPols4v.length, 3, 2);
			short[] posesData = new short[poses.getVertexCount() * poses.getComponentCount()];
			int posesIndex = 0;
			
			/*VertexArray norms = new VertexArray(genPols3v.length + genPols4v.length, 3, 1);
			byte[] normsData = new byte[poses.getVertexCount() * poses.getComponentCount()];
			int normsIndex = 0;*/

			VertexArray uvms = new VertexArray(genPols3v.length + genPols4v.length, 2, 1);
			byte[] uvData = new byte[uvms.getVertexCount() * uvms.getComponentCount()];
			int uvIndex = 0;

			int[] stripLengths = new int[genPols3v.length / 3 + genPols4v.length / 4];
			int polyIndex = 0;
		
			byte[] genUV3v = (animBaseMesh != null) ? animBaseMesh.p3uv : p3uv;
			byte[] genUV4v = (animBaseMesh != null) ? animBaseMesh.p4uv : p4uv;

			int p3vCount = genPols3v.length / 3;
			for(int i = 0; i < p3vCount; i++) {
				stripLengths[polyIndex] = 3;
				polyIndex++;
				
				short v1Index = genPols3v[i * 3 + 0];
				short v2Index = genPols3v[i * 3 + 1];
				short v3Index = genPols3v[i * 3 + 2];
				
				//abc -> cba
				System.arraycopy(verts, v3Index * 3, posesData, posesIndex + 0, 3);
				System.arraycopy(verts, v2Index * 3, posesData, posesIndex + 3, 3);
				System.arraycopy(verts, v1Index * 3, posesData, posesIndex + 6, 3);
				posesIndex += 9;

				System.arraycopy(genUV3v, i * 6 + 4, uvData, uvIndex + 0, 2);
				System.arraycopy(genUV3v, i * 6 + 2, uvData, uvIndex + 2, 2);
				System.arraycopy(genUV3v, i * 6 + 0, uvData, uvIndex + 4, 2);
				uvIndex += 6;
				
				/*int ax = verts[v1Index * 3 + 0], 
					ay = verts[v1Index * 3 + 1], 
					az = verts[v1Index * 3 + 2];

				int bx = verts[v2Index * 3 + 0], 
					by = verts[v2Index * 3 + 1], 
					bz = verts[v2Index * 3 + 2];

				int cx = verts[v3Index * 3 + 0], 
					cy = verts[v3Index * 3 + 1], 
					cz = verts[v3Index * 3 + 2];

				Vector3D norm = MathUtils.createNormal(
					ax, ay, az, 
					bx, by, bz, 
					cx, cy, cz
				);
				
				normsData[normsIndex + 0] = normsData[normsIndex + 3] = normsData[normsIndex + 6] = (byte) (-norm.x * 127 / 4096);
				normsData[normsIndex + 1] = normsData[normsIndex + 4] = normsData[normsIndex + 7] = (byte) (-norm.y * 127 / 4096);
				normsData[normsIndex + 2] = normsData[normsIndex + 5] = normsData[normsIndex + 8] = (byte) (-norm.z * 127 / 4096);
				normsIndex += 9;*/
			}

			int p4vCount = genPols4v.length / 4;
			for(int i = 0; i < p4vCount; i++) {
				stripLengths[polyIndex] = 4;
				polyIndex++;
				
				short v1Index = genPols4v[i * 4 + 0];
				short v2Index = genPols4v[i * 4 + 1];
				short v3Index = genPols4v[i * 4 + 2];
				short v4Index = genPols4v[i * 4 + 3];
				
				//abcd -> abdc
				//dcba -> dcab
				System.arraycopy(verts, v4Index * 3, posesData, posesIndex + 0, 3);
				System.arraycopy(verts, v3Index * 3, posesData, posesIndex + 3, 3);
				System.arraycopy(verts, v1Index * 3, posesData, posesIndex + 6, 3);
				System.arraycopy(verts, v2Index * 3, posesData, posesIndex + 9, 3);
				posesIndex += 12;

				System.arraycopy(genUV4v, i * 8 + 6, uvData, uvIndex + 0, 2);
				System.arraycopy(genUV4v, i * 8 + 4, uvData, uvIndex + 2, 2);
				System.arraycopy(genUV4v, i * 8 + 0, uvData, uvIndex + 4, 2);
				System.arraycopy(genUV4v, i * 8 + 2, uvData, uvIndex + 6, 2);
				uvIndex += 8;
				
				/*int ax = verts[v1Index * 3 + 0], 
					ay = verts[v1Index * 3 + 1], 
					az = verts[v1Index * 3 + 2];

				int bx = verts[v2Index * 3 + 0], 
					by = verts[v2Index * 3 + 1], 
					bz = verts[v2Index * 3 + 2];

				int cx = verts[v4Index * 3 + 0], 
					cy = verts[v4Index * 3 + 1], 
					cz = verts[v4Index * 3 + 2];

				Vector3D norm = MathUtils.createNormal(
					ax, ay, az, 
					bx, by, bz, 
					cx, cy, cz
				);
				
				normsData[normsIndex + 0] = normsData[normsIndex + 3] = normsData[normsIndex + 6] = normsData[normsIndex + 9] = (byte) (-norm.x * 127 / 4096);
				normsData[normsIndex + 1] = normsData[normsIndex + 4] = normsData[normsIndex + 7] = normsData[normsIndex + 10] = (byte) (-norm.y * 127 / 4096);
				normsData[normsIndex + 2] = normsData[normsIndex + 5] = normsData[normsIndex + 8] = normsData[normsIndex + 11] = (byte) (-norm.z * 127 / 4096);
				normsIndex += 12;*/
			}

			poses.set(0, poses.getVertexCount(), posesData);
			uvms.set(0, uvms.getVertexCount(), uvData);
			//norms.set(0, norms.getVertexCount(), normsData);

			VertexBuffer vb = new VertexBuffer();
			vb.setPositions(poses, scale, new float[] {0,0,0});
			vb.setTexCoords(0, uvms, 1f / textureSize, new float[] {128f / textureSize, 128f / textureSize, 128f / textureSize});
			//vb.setNormals(norms);

			TriangleStripArray tsa = new TriangleStripArray(0, stripLengths);

			/*Material mat = new Material();
			mat.setColor(Material.AMBIENT, 0);
			mat.setColor(Material.DIFFUSE, 0xffffff);//0);
			mat.setColor(Material.EMISSIVE, 0x808080);//0xffffff);
			mat.setVertexColorTrackingEnable(false);*/

			PolygonMode pm = new PolygonMode();
			pm.setShading(PolygonMode.SHADE_SMOOTH);//FLAT);
			pm.setCulling(PolygonMode.CULL_BACK);
			pm.setPerspectiveCorrectionEnable(persCorrection);

			CompositingMode cm = new CompositingMode();
			//cm.setAlphaWriteEnable(false); //enable for better performance

			Appearance ap = new Appearance();
			//ap.setMaterial(mat);
			ap.setPolygonMode(pm);
			ap.setCompositingMode(cm);

			mesh = new Mesh(vb, tsa, ap);
		}

		System.out.println("Mesh [" + file + "] вершин: " + (verts.length / 3) + " полигонов: " + (pols4v.length / 4 + pols3v.length / 3));
		System.out.println("Size: " + (maxV.x - minV.x + 1) + ", " + (maxV.y - minV.y + 1) + ", " + (maxV.z - minV.z + 1));
		
		MeshData md = new MeshData(verts, scale, pols4v, pols3v, p4uv, p3uv, mesh);
		
		return md;
	}

	public Mesh getM3GMesh() {
		return meshM3G;
	}
}
