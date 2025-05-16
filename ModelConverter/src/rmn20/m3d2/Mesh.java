package rmn20.m3d2;

import java.util.ArrayList;
import rmn20.assimp.AIMeshData;
import rmn20.assimp.AISubMeshData;

/**
 *
 * @author Roman
 */
public class Mesh {
	public static int ATTRIBUTES_COUNT = 4;
	public static int POS = 0, NORM = 1, UV = 2, COL = 3;
	
	public String name;
	
	public ArrayList<Vertex> verts = new ArrayList<>();
	public ArrayList<SubMesh> submeshes = new ArrayList<>();
	
	public boolean hasNorms, hasUVs, hasCols;
	
	public Vector3i 
		posMin = new Vector3i(Integer.MAX_VALUE), 
		posMax = new Vector3i(Integer.MIN_VALUE);
	
	public Vector3i 
		uvMin = new Vector3i(Integer.MAX_VALUE), 
		uvMax = new Vector3i(Integer.MIN_VALUE);
	
	public Mesh(AIMeshData inMesh) {
		name = inMesh.name;
		
		for(AISubMeshData inSubmesh : inMesh.submeshes) {
			SubMesh submesh = new SubMesh(inSubmesh, this);
			addSubMesh(submesh);
		}
	}
	
	public void addSubMesh(SubMesh mesh) {
		submeshes.add(mesh);
	}
	
	public int getVertexId(Vertex v) {
		int id = verts.indexOf(v);
		
		if(id != -1) {
			return id;
		} else {
			verts.add(v);
			
			posMin.min(v.atts[Mesh.POS]);
			posMax.max(v.atts[Mesh.POS]);
			
			if(v.atts[Mesh.NORM] != null) hasNorms = true;
			
			if(v.atts[Mesh.UV] != null) {
				hasUVs = true;
				uvMin.min(v.atts[Mesh.UV]);
				uvMax.max(v.atts[Mesh.UV]);
			}
			
			if(v.atts[Mesh.COL] != null) hasCols = true;
			
			if(verts.size() > 65535) throw new Error("Too many unique vertices in mesh " + name);
			
			return verts.size() - 1;
		}
	}
}
