package rmn20.m3d2;

import java.util.ArrayList;
import rmn20.assimp.AISubMeshData;
import rmn20.assimp.Vertexf;

/**
 *
 * @author Roman
 */
public class SubMesh {
	public int matId;
	
	public ArrayList<Integer> quads = new ArrayList<>();
	public ArrayList<Integer> tris = new ArrayList<>();
	
	public SubMesh(AISubMeshData inSubMesh, Mesh mesh) {
		this.matId = inSubMesh.matId;
		
		for(int idx : inSubMesh.quads) {
			Vertexf vtxF = inSubMesh.verts.get(idx);
			Vertex vtx = new Vertex(vtxF);
			
			quads.add(mesh.getVertexId(vtx));
		}
		
		for(int idx : inSubMesh.tris) {
			Vertexf vtxF = inSubMesh.verts.get(idx);
			Vertex vtx = new Vertex(vtxF);
			
			tris.add(mesh.getVertexId(vtx));
		}
	}
}
