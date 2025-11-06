package rmn20.m3d2;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Roman
 */
public class Exporter3D2 {
	
	public static int 
		statPosSize, statNormSize, statUVSize, statColSize, 
		statBonesSize,
		statIndicesSize;
	
	public static final void exportModel(DataOutputStream dos, Model mdl) throws IOException {
		statPosSize = statNormSize = statUVSize = statColSize = statIndicesSize = 0;
		statBonesSize = 0;
		
		dos.writeBytes("3D2\0");
		dos.writeShort(0); //Format version

		//Attributes scale
		dos.writeFloat(Vector3i.positionScale);
		dos.writeFloat(Vector3i.uvScale);
		
		//AABB
		dos.writeShort(mdl.posMin.x);
		dos.writeShort(mdl.posMin.y);
		dos.writeShort(mdl.posMin.z);
		
		dos.writeShort(mdl.posMax.x);
		dos.writeShort(mdl.posMax.y);
		dos.writeShort(mdl.posMax.z);

		//Meshes
		dos.writeShort(mdl.meshes.size());

		System.out.println("\n" + mdl.meshes.size() + " meshes in file");

		for(int i = 0; i < mdl.meshes.size(); i++) {
			exportMesh(dos, mdl.meshes.get(i));
		}

		//Stats
		System.out.printf("\nPos  data size: %.1f kb\n", statPosSize / 1024f);
		System.out.printf("Norm data size: %.1f kb\n", statNormSize / 1024f);
		System.out.printf("UV   data size: %.1f kb\n", statUVSize / 1024f);
		System.out.printf("Col  data size: %.1f kb\n", statColSize / 1024f);
		System.out.printf("Bones data size: %.1f kb\n", statBonesSize / 1024f);
		System.out.printf("PIdx data size: %.1f kb\n", statIndicesSize / 1024f);
	}
	
	private static final void fillEmptyAttributes(Mesh mesh) {
		for(int i = 0; i < mesh.verts.size(); i++) {
			Vertex v = mesh.verts.get(i);
			
			if(mesh.hasNorms && v.atts[Mesh.NORM] == null) v.atts[Mesh.NORM] = new Vector3i(0);
			if(mesh.hasUVs && v.atts[Mesh.UV] == null) v.atts[Mesh.UV] = new Vector3i(0);
			if(mesh.hasCols && v.atts[Mesh.COL] == null) v.atts[Mesh.COL] = new Vector3i(127);
		}
	}
	
	private static final int getVectorId(ArrayList<Vector3i> list, Vector3i v) {
		int id = list.indexOf(v);
		
		if(id != -1) {
			return id;
		} else {
			list.add(v);
			return list.size() - 1;
		}
	}
	
	private static final void writeAttribute(
		DataOutputStream dos, 
		ArrayList<Vertex> verts,
		int attributeId,
		int dims, 
		boolean useShortX,
		boolean useShortY,
		boolean useShortZ
	) throws IOException {
		for(int i = 0; i < verts.size(); i++) {
			Vector3i v = verts.get(i).atts[attributeId];
			
			if(useShortX) dos.writeShort(v.x);
			else dos.writeByte(v.x);

			if(dims >= 2){
				if(useShortY) dos.writeShort(v.y);
				else dos.writeByte(v.y);
			}

			if(dims >= 3){
				if(useShortZ) dos.writeShort(v.z);
				else dos.writeByte(v.z);
			}
		}
		
		//Stats
		int elementSize = 
			(useShortX?2:1) + 
			(dims>=2 ? (useShortY?2:1) : 0) + 
			(dims>=3 ? (useShortZ?2:1) : 0);
		
		int sizeNaive = elementSize * verts.size();
		
		if(attributeId == Mesh.POS) statPosSize += sizeNaive;
		if(attributeId == Mesh.NORM) statNormSize += sizeNaive;
		if(attributeId == Mesh.UV) statUVSize += sizeNaive;
		if(attributeId == Mesh.COL) statColSize += sizeNaive;
	}
	
	private static final void writeAttribute(
		DataOutputStream dos, 
		ArrayList<Vertex> verts,
		int attributeId,
		int dims, 
		boolean useShort
	) throws IOException {
		writeAttribute(dos, verts, attributeId, dims, useShort, useShort, useShort);
	}
	
	public static final void exportMesh(DataOutputStream dos, Mesh mesh) throws IOException {
		boolean hasNorms = mesh.hasNorms, 
			hasUVs = mesh.hasUVs, 
			hasCols = mesh.hasCols;
		
		Vector3i posMin = mesh.posMin, posMax = mesh.posMax;
		Vector3i uvMin = mesh.uvMin, uvMax = mesh.uvMax;
		
		ArrayList<Vertex> verts = mesh.verts;
		ArrayList<SubMesh> submeshes = mesh.submeshes;
		
		fillEmptyAttributes(mesh);
		
		//Write flags
		boolean uvXInBytes = false, uvYInBytes = false, uvInBytes = false;
		boolean hasBones = !mesh.bones.isEmpty();
		
		if(hasUVs) {
			uvXInBytes = uvMax.x - uvMin.x < 256;
			uvYInBytes = uvMax.y - uvMin.y < 256;
			uvInBytes = uvXInBytes || uvYInBytes;
		}
		
		int flags = 0;
		
		flags |= hasNorms ? 1 : 0;
		flags |= hasUVs ? 2 : 0;
		flags |= hasCols ? 4 : 0;
		
		flags |= uvXInBytes ? 8 : 0;
		flags |= uvYInBytes ? 16 : 0;
		
		flags |= hasBones ? 32 : 0;
		
		dos.writeInt(flags);
		
		//Write mesh AABB
		dos.writeShort(posMin.x);
		dos.writeShort(posMin.y);
		dos.writeShort(posMin.z);
		
		dos.writeShort(posMax.x);
		dos.writeShort(posMax.y);
		dos.writeShort(posMax.z);
		
		//Apply offsets for byte encoding
		boolean posXInBytes = (posMax.x - posMin.x) < 256;
		boolean posYInBytes = (posMax.y - posMin.y) < 256;
		boolean posZInBytes = (posMax.z - posMin.z) < 256;
		
		Vector3i posOffset = new Vector3i(0);
		if(posXInBytes) posOffset.x = posMin.x + 128;
		if(posYInBytes) posOffset.y = posMin.y + 128;
		if(posZInBytes) posOffset.z = posMin.z + 128;

		for(int i = 0; i < verts.size(); i++) {
			Vector3i v = verts.get(i).atts[Mesh.POS];

			v.x -= posOffset.x;
			v.y -= posOffset.y;
			v.z -= posOffset.z;
		}
		
		Vector3i uvOffset = null;
		if(uvInBytes) {
			uvOffset = new Vector3i(0);
			
			if(uvXInBytes) {
				uvOffset.x = uvMin.x + 128;
				dos.writeShort(uvMin.x);
			}
			
			if(uvYInBytes) {
				uvOffset.y = uvMin.y + 128;
				dos.writeShort(uvMin.y);
			}
			
			for(int i = 0; i < verts.size(); i++) {
				Vector3i v = verts.get(i).atts[Mesh.UV];
				
				v.x -= uvOffset.x;
				v.y -= uvOffset.y;
			}
		}
		
		//Write bones
		if(hasBones) {
			dos.writeByte(mesh.bones.size());

			for(Bone bone : mesh.bones) {
				dos.writeByte(bone.parent);

				for(int i = 0; i < 16; i++) {
					dos.writeFloat(bone.matrix[i]);
				}
			}
			
			statBonesSize += 1 + mesh.bones.size() * (1 + 16 * 4);
		}
		
		//Calculate lists of unique attributes
		//Unused?
		ArrayList<Vector3i> uniquePoses = new ArrayList<>();
		ArrayList<Vector3i> uniqueNorms = new ArrayList<>();
		ArrayList<Vector3i> uniqueUVs = new ArrayList<>();
		ArrayList<Vector3i> uniqueCols = new ArrayList<>();
		
		for(int i = 0; i < verts.size(); i++) {
			Vertex v = verts.get(i);
			
			v.attIdx[Mesh.POS] = getVectorId(uniquePoses, v.atts[Mesh.POS]);
			if(hasNorms) v.attIdx[Mesh.NORM] = getVectorId(uniqueNorms, v.atts[Mesh.NORM]);
			if(hasUVs) v.attIdx[Mesh.UV] = getVectorId(uniqueUVs, v.atts[Mesh.UV]);
			if(hasCols) v.attIdx[Mesh.COL] = getVectorId(uniqueCols, v.atts[Mesh.COL]);
		}
		
		//Write all vertex attributes
		dos.writeShort(verts.size());
		
		writeAttribute(dos, verts, Mesh.POS, 3, !posXInBytes, !posYInBytes, !posZInBytes);
		if(hasNorms) writeAttribute(dos, verts, Mesh.NORM, 3, false);
		if(hasUVs) writeAttribute(dos, verts, Mesh.UV, 2, !uvXInBytes, !uvYInBytes, false);
		if(hasCols) writeAttribute(dos, verts, Mesh.COL, 3, false);
		
		//Write polygonal data
		int totalQuads = 0, totalTris = 0;
		for(int i = 0; i < submeshes.size(); i++) {
			SubMesh submesh = submeshes.get(i);
			
			totalQuads += submesh.quads.size() / 4;
			totalTris += submesh.tris.size() / 3;
		}
		
		dos.writeShort(totalQuads);
		dos.writeShort(totalTris);
		dos.writeShort(submeshes.size());
		
		for(int i = 0; i < submeshes.size(); i++) {
			SubMesh submesh = submeshes.get(i);
			ArrayList<Integer> quads = submesh.quads;
			ArrayList<Integer> tris = submesh.tris;
			
			//Write indices
			dos.writeShort(quads.size() / 4);
			dos.writeShort(tris.size() / 3);
			
			for(int t = 0; t < quads.size(); t++) {
				int idx = quads.get(t);
				
				if(verts.size() <= 256) {
					dos.writeByte(idx);
					statIndicesSize++;
				} else {
					dos.writeShort(idx);
					statIndicesSize += 2;
				}
			}
			
			for(int t = 0; t < tris.size(); t++) {
				int idx = tris.get(t);
				
				if(verts.size() <= 256) {
					dos.writeByte(idx);
					statIndicesSize++;
				} else {
					dos.writeShort(idx);
					statIndicesSize += 2;
				}
			}
		}
		
		//Restore original data after applying offsets
		for(int i = 0; i < verts.size(); i++) {
			Vector3i v = verts.get(i).atts[Mesh.POS];

			v.x += posOffset.x;
			v.y += posOffset.y;
			v.z += posOffset.z;
		}
		
		if(uvInBytes) {
			for(int i = 0; i < verts.size(); i++) {
				Vector3i v = verts.get(i).atts[Mesh.UV];
				
				v.x += uvOffset.x;
				v.y += uvOffset.y;
			}
		}
		
		//Write bones weights
		int maxBonesPerVtx = 0;
		if(hasBones) {
			for(int i = 0; i < verts.size(); i++) {
				Vertex v = verts.get(i);
				maxBonesPerVtx = Math.max(v.bones.size(), maxBonesPerVtx);
			}
			
			dos.writeByte(maxBonesPerVtx);
			statBonesSize++;
			
			for(int i = 0; i < verts.size(); i++) {
				Vertex v = verts.get(i);
				
				for(int w = 0; w < maxBonesPerVtx; w++) {
					if(w >= v.bones.size()) {
						dos.writeByte(255);
						statBonesSize++;
						break;
					}
					
					dos.writeByte(v.bones.get(0).boneId);
					statBonesSize++;
					
					if(maxBonesPerVtx > 1) {
						dos.writeByte(v.bones.get(0).weight);
						statBonesSize++;
					}
				}
			}
		}
		
		System.out.printf(
			"\n\"%s\" mesh stats:\n" + 
			"hasNorms: %b\nhasUVs: %b\nhasCols: %b\n" +
			"Bones: %d\n" + 
			"Max bones per vtx: %d\n" + 
			"Quads: %d\n" + 
			"Tris: %d\n" + 
			"Verts: %d\n" + 
			"Unique positions: %d\n" + 
			"Unique norms: %d\n" + 
			"Unique uvs: %d\n" + 
			"Unique cols: %d\n" + 
			"Position byte encoding: %b %b %b\n" + 
			"UV byte encoding: %b %b\n",
			
			mesh.name,
			hasNorms, hasUVs, hasCols,
			mesh.bones.size(),
			maxBonesPerVtx,
			totalQuads,
			totalTris,
			verts.size(),
			uniquePoses.size(),
			uniqueNorms.size(),
			uniqueUVs.size(),
			uniqueCols.size(),
			posXInBytes,
			posYInBytes,
			posZInBytes,
			uvXInBytes,
			uvYInBytes
		);
	}
}
