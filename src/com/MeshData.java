package com;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import javax.microedition.m3g.Appearance;
import javax.microedition.m3g.CompositingMode;
import javax.microedition.m3g.Fog;
import javax.microedition.m3g.Image2D;
import javax.microedition.m3g.Mesh;
import javax.microedition.m3g.PolygonMode;
import javax.microedition.m3g.Texture2D;
import javax.microedition.m3g.TriangleStripArray;
import javax.microedition.m3g.VertexArray;
import javax.microedition.m3g.VertexBuffer;

public class MeshData {

	//todo optimize data storage and loading
	private Mesh m3gMesh;
	private Texture texture;
	
	private Vector3D aabbMin, aabbMax;
	
	//used for physics
	private float scale;
	private int offsetX, offsetY, offsetZ;
	private int quadsCount, trisCount;
	private short[] verts, pols, polNorms;

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

	private MeshData(Mesh meshM3G, Vector3D aabbMin, Vector3D aabbMax) {
		this.m3gMesh = meshM3G;
		this.aabbMin = aabbMin;
		this.aabbMax = aabbMax;
	}

	public void destroy() {
		m3gMesh = null;
		texture = null;
		verts = pols = polNorms = null;
	}
	
	private void setPhysicsData(
			short[] verts, short[] pols,
			int quadsCount, int trisCount,
			float scale,
			int offsetX, int offsetY, int offsetZ
			) {
		this.verts = verts;
		this.pols = pols;
		
		this.quadsCount = quadsCount;
		this.trisCount = trisCount;
		
		this.scale = scale;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.offsetZ = offsetZ;
	}
	
	public void calculateNormals() {
		short[] verts = this.verts;
		short[] pols = this.pols;
		short[] polNorms = new short[(quadsCount + trisCount) * 3];
		
		for(int vtxPerPoly = 4, pIdx = 0, nIdx = 0; vtxPerPoly >= 3; vtxPerPoly--) {
			
			int pEnd = vtxPerPoly == 4 ? quadsCount * 4 : pols.length;
			
			for(; pIdx < pEnd; pIdx += vtxPerPoly, nIdx++) {
				int ax = verts[pols[pIdx] * 3 + 0], 
					ay = verts[pols[pIdx] * 3 + 1], 
					az = verts[pols[pIdx] * 3 + 2];

				int bx = verts[pols[pIdx + 1] * 3 + 0], 
					by = verts[pols[pIdx + 1] * 3 + 1], 
					bz = verts[pols[pIdx + 1] * 3 + 2];

				int cx = verts[pols[pIdx + vtxPerPoly - 1] * 3 + 0], 
					cy = verts[pols[pIdx + vtxPerPoly - 1] * 3 + 1], 
					cz = verts[pols[pIdx + vtxPerPoly - 1] * 3 + 2];

				Vector3D norm = MathUtils.createNormal(
					ax, ay, az, 
					bx, by, bz, 
					cx, cy, cz
				);
				
				polNorms[nIdx * 3] = (short) norm.x;
				polNorms[nIdx * 3 + 1] = (short) norm.y;
				polNorms[nIdx * 3 + 2] = (short) norm.z;
			}
		}
		
		this.polNorms = polNorms;
	}
	
	public Vector3D getVertex(int idx, boolean applyTransformation) {
		Vector3D v = new Vector3D(verts[idx * 3], verts[idx * 3 + 1], verts[idx * 3 + 2]);
		
		if(applyTransformation) {
			v.x = (int) ((v.x + offsetX) * scale);
			v.y = (int) ((v.y + offsetY) * scale);
			v.z = (int) ((v.z + offsetZ) * scale);
		}
		
		return v;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
		if(m3gMesh != null) m3gMesh.getAppearance(0).getTexture(0).setImage(texture.img); //todo why
	}

	public Texture getTexture() {
		return texture;
	}
	
	public float getScale() {
		return scale;
	}
	
	public int getOffsetX() {
		return offsetX;
	}
	
	public int getOffsetY() {
		return offsetY;
	}
	
	public int getOffsetZ() {
		return offsetZ;
	}
	
	public Mesh getM3GMesh() {
		return m3gMesh;
	}
	
	public short[] getVerts() {
		return verts;
	}
	
	public short[] getPols() {
		return pols;
	}
	
	public short[] getNorms() {
		return polNorms;
	}
	
	public int getQuadsCount() {
		return quadsCount;
	}
	
	public int getTrisCount() {
		return trisCount;
	}
	
	public Vector3D getAABBMin() {
		return aabbMin;
	}
	
	public Vector3D getAABBMax() {
		return aabbMax;
	}

	public static MeshData[] loadMeshes3D2(
			String file, 
			Image2D img,
			float scale, 
			boolean animation, 
			boolean persCorrection
			) {
		MeshData[] meshes = null;
		InputStream is = null;
		DataInputStream dis = null;

		try {
			is = (new Object()).getClass().getResourceAsStream(file);
			dis = new DataInputStream(is);
			
			dis.skipBytes(4);//Format
			int version = dis.readUnsignedShort(); //Format version
			
			float posScale = dis.readFloat();
			float uvScale = dis.readFloat();
			
			int aabbMinX = dis.readShort();
			int aabbMinY = dis.readShort();
			int aabbMinZ = dis.readShort();
			
			int aabbMaxX = dis.readShort();
			int aabbMaxY = dis.readShort();
			int aabbMaxZ = dis.readShort();
			
			meshes = new MeshData[dis.readUnsignedShort()];

			for(int i = 0; i < meshes.length; i++) {
				meshes[i] = loadMesh3D2(
					dis, 
					img,
					scale / posScale,
					1f / uvScale,
					persCorrection
				);
			}
		} catch(Exception ex) {
			System.err.println("ERROR in Loader.Load: " + ex);
			ex.printStackTrace();
		} finally {
			try {
				dis.close();
				is.close();
			} catch(Exception ex) {}
		}

		return meshes;
	}
	
	/*private static Fog fog = new Fog();
	
	static {
		fog.setMode(Fog.EXPONENTIAL);
		fog.setDensity(1.5E-5f);
		fog.setColor(0xE0CED8);
	}*/
	
	private static MeshData loadMesh3D2(
			DataInputStream dis,
			Image2D img, 
			float scale, 
			float uvScale,
			boolean persCorrection
			) throws IOException {
		//Read mesh flags
		int meshFlags = dis.readInt();

		boolean hasNorms = (meshFlags & 1) != 0;
		boolean hasUVs = (meshFlags & 2) != 0;
		boolean hasCols = (meshFlags & 4) != 0;
		
		boolean uvXInBytes = (meshFlags & 8) != 0;
		boolean uvYInBytes = (meshFlags & 16) != 0;
		
		//Load mesh AABB
		Vector3D aabbMin = new Vector3D(dis.readShort(), dis.readShort(), dis.readShort());
		Vector3D aabbMax = new Vector3D(dis.readShort(), dis.readShort(), dis.readShort());
				
		//Load pos and uv offsets
		boolean posXInBytes = (aabbMax.x - aabbMin.x) < 256;
		boolean posYInBytes = (aabbMax.y - aabbMin.y) < 256;
		boolean posZInBytes = (aabbMax.z - aabbMin.z) < 256;
		
		int offsetX = posXInBytes ? aabbMin.x + 128 : 0;
		int offsetY = posYInBytes ? aabbMin.y + 128 : 0;
		int offsetZ = posZInBytes ? aabbMin.z + 128 : 0;
		
		int uvOffsetX = 0, uvOffsetY = 0;
		if(uvXInBytes) uvOffsetX = dis.readShort() + 128;
		if(uvYInBytes) uvOffsetY = dis.readShort() + 128;
		
		//Load vertex data
		int vtxCount = dis.readUnsignedShort();
		short[] physVerts = new short[vtxCount * 3];
		
		VertexArray poses, norms = null, uvs = null, cols = null;
		
		poses = loadVertexAttribute3D2(dis, vtxCount, 3, posXInBytes, posYInBytes, posZInBytes, physVerts);
		if(hasNorms) norms = loadVertexAttribute3D2(dis, vtxCount, 3, true, true, true, null);
		if(hasUVs) uvs = loadVertexAttribute3D2(dis, vtxCount, 2, uvXInBytes, uvYInBytes, true, null);
		if(hasCols) cols = loadVertexAttribute3D2(dis, vtxCount, 3, true, true, true, null);
		
		//Store loaded attributes in vertex buffer
		VertexBuffer vb = new VertexBuffer();
		vb.setPositions(
			poses, 
			scale,
			new float[] {
				offsetX * scale,
				offsetY * scale,
				offsetZ * scale
			}
		);
		
		if(hasNorms) vb.setNormals(norms);
		if(hasUVs) vb.setTexCoords(0, uvs, uvScale, new float[] {uvOffsetX * uvScale, uvOffsetY * uvScale});
		if(hasCols) vb.setColors(cols);
		
		//Load polygonal data
		int totalQuads = dis.readUnsignedShort();
		int totalTris = dis.readUnsignedShort();
		
		short[] physPolys = new short[totalQuads * 4 + totalTris * 3];
		int physQuadPos = 0, physTriPos = totalQuads * 4;
		
		TriangleStripArray[] submeshes = new TriangleStripArray[dis.readUnsignedShort()];
		
		Appearance[] ap = new Appearance[submeshes.length];
		PolygonMode pm = new PolygonMode();
		pm.setPerspectiveCorrectionEnable(persCorrection);
		//pm.setShading(PolygonMode.SHADE_FLAT);
		CompositingMode cm = new CompositingMode();
		
		for(int i = 0; i < submeshes.length; i++) {
			ap[i] = new Appearance();
			ap[i].setPolygonMode(pm);
			ap[i].setCompositingMode(cm);
			
			Texture2D tex = new Texture2D(img);
			if(!hasCols) tex.setBlending(Texture2D.FUNC_REPLACE);
			ap[i].setTexture(0, tex);
			//ap[i].setFog(fog);
					
			int quads = dis.readUnsignedShort();
			int tris = dis.readUnsignedShort();
			
			int indices[] = new int[quads * 4 + tris * 3];
			int stripLengths[] = new int[quads + tris];
			
			for(int p = 0; p < quads; p++) {
				stripLengths[p] = 4;
				
				if(vtxCount <= 256) {
					indices[p * 4] = dis.readUnsignedByte();
					indices[p * 4 + 1] = dis.readUnsignedByte();
					indices[p * 4 + 2] = dis.readUnsignedByte();
					indices[p * 4 + 3] = dis.readUnsignedByte();
				} else {
					indices[p * 4] = dis.readUnsignedShort();
					indices[p * 4 + 1] = dis.readUnsignedShort();
					indices[p * 4 + 2] = dis.readUnsignedShort();
					indices[p * 4 + 3] = dis.readUnsignedShort();
				}
				
				//triangle strip -> proper quad (abcd -> abdc) + ccw -> cw (abcd -> dcba)
				physPolys[physQuadPos + 0] = (short) indices[p * 4 + 2];
				physPolys[physQuadPos + 1] = (short) indices[p * 4 + 3];
				physPolys[physQuadPos + 2] = (short) indices[p * 4 + 1];
				physPolys[physQuadPos + 3] = (short) indices[p * 4 + 0];
				
				physQuadPos += 4;
			}
			
			for(int p = 0; p < tris; p++) {
				stripLengths[quads + p] = 3;
				
				if(vtxCount <= 256) {
					indices[quads * 4 + p * 3] = dis.readUnsignedByte();
					indices[quads * 4 + p * 3 + 1] = dis.readUnsignedByte();
					indices[quads * 4 + p * 3 + 2] = dis.readUnsignedByte();
				} else {
					indices[quads * 4 + p * 3] = dis.readUnsignedShort();
					indices[quads * 4 + p * 3 + 1] = dis.readUnsignedShort();
					indices[quads * 4 + p * 3 + 2] = dis.readUnsignedShort();
				}
				
				//ccw -> cw (abc -> cba)
				physPolys[physTriPos + 0] = (short) indices[quads * 4 + p * 3 + 2];
				physPolys[physTriPos + 1] = (short) indices[quads * 4 + p * 3 + 1];
				physPolys[physTriPos + 2] = (short) indices[quads * 4 + p * 3 + 0];
				
				physTriPos += 3;
			}
			
			submeshes[i] = new TriangleStripArray(indices, stripLengths);
		}
		
		aabbMin.x = (int) (aabbMin.x * scale);
		aabbMin.y = (int) (aabbMin.y * scale);
		aabbMin.z = (int) (aabbMin.z * scale);
		
		aabbMax.x = (int) (aabbMax.x * scale);
		aabbMax.y = (int) (aabbMax.y * scale);
		aabbMax.z = (int) (aabbMax.z * scale);
		
		Mesh m3gMesh = new Mesh(vb, submeshes, ap);
		MeshData mesh = new MeshData(m3gMesh, aabbMin, aabbMax);
		
		mesh.setPhysicsData(
			physVerts, physPolys, 
			totalQuads, totalTris, 
			scale, 
			offsetX, offsetY, offsetZ
		);
		
		return mesh;
	}
	
	private static VertexArray loadVertexAttribute3D2(
			DataInputStream dis,
			int vtxCount,
			int dims,
			boolean xInBytes,
			boolean yInBytes,
			boolean zInBytes,
			short[] dataOnCpu
			) throws IOException {
		//Create temporary data array
		int dataCount = dis.readUnsignedShort();
		boolean useIndexing = dataCount > 0;
		if(!useIndexing) dataCount = vtxCount;
		
		short dataShort[] = null;
		byte dataBytes[] = null;
		
		if(xInBytes && yInBytes && zInBytes) dataBytes = new byte[dataCount * dims];
		else dataShort = new short[dataCount * dims];
		
		//Load attribute data
		for(int i = 0; i < dataCount; i++) {
			int x = 0, y = 0, z = 0;
			
			x = xInBytes ? dis.readByte() : dis.readShort();
			if(dims >= 2) y = yInBytes ? dis.readByte() : dis.readShort();
			if(dims >= 3) z = zInBytes ? dis.readByte() : dis.readShort();
			
			if(dataBytes != null) {
				dataBytes[i * dims] = (byte) x;
				if(dims >= 2) dataBytes[i * dims + 1] = (byte) y;
				if(dims >= 3) dataBytes[i * dims + 2] = (byte) z;
			} else {
				dataShort[i * dims] = (short) x;
				if(dims >= 2) dataShort[i * dims + 1] = (short) y;
				if(dims >= 3) dataShort[i * dims + 2] = (short) z;
			}
		}
		
		//Fill vertex data with loaded attribute data if indexing is used
		if(useIndexing) {
			short newDataShort[] = null;
			byte newDataBytes[] = null;
		
			if(dataBytes != null) newDataBytes = new byte[vtxCount * dims];
			else newDataShort = new short[vtxCount * dims];
			
			for(int i = 0; i < vtxCount; i++) {
				int idx;
				
				if(dataCount <= 256) idx = dis.readUnsignedByte();
				else idx = dis.readUnsignedShort();
			
				if(dataBytes != null) {
					System.arraycopy(dataBytes, idx * dims, newDataBytes, i * dims, dims);
				} else {
					System.arraycopy(dataShort, idx * dims, newDataShort, i * dims, dims);
				}
			}
			
			dataShort = newDataShort;
			dataBytes = newDataBytes;
		}
		
		//Fill additional on-cpu data array
		if(dataOnCpu != null) {
			if(dataBytes != null) {
				for(int i = 0; i < vtxCount * dims; i++) {
					dataOnCpu[i] = dataBytes[i];
				}
			} else {
				System.arraycopy(dataShort, 0, dataOnCpu, 0, vtxCount * dims);
			}
		}
		
		//Create vertex array
		VertexArray va;
		
		if(dataBytes != null) {
			va = new VertexArray(vtxCount, dims, 1);
			va.set(0, vtxCount, dataBytes);
		} else {
			va = new VertexArray(vtxCount, dims, 2);
			va.set(0, vtxCount, dataShort);
		}
		
		return va;
	}
}
