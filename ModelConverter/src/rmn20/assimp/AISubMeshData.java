package rmn20.assimp;

import java.nio.IntBuffer;
import java.util.ArrayList;
import org.lwjgl.assimp.AIColor4D;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIVector3D;

/**
 *
 * @author Roman
 */
public class AISubMeshData {
	public int matId;
	
	public ArrayList<Vertexf> verts;
	
	public ArrayList<Integer> quads;
	public ArrayList<Integer> tris;
	
	public AISubMeshData(
		int matId,
		AIVector3D.Buffer verts, 
		AIVector3D.Buffer norms, 
		AIVector3D.Buffer uvs, 
		AIColor4D.Buffer cols,
		int vtxCount
	) {
		this.matId = matId;
		
		this.verts = new ArrayList<>();
		
		for(int i = 0; i < vtxCount; i++) {
			this.verts.add(new Vertexf(verts, norms, uvs, cols, i));
		}
		
		quads = new ArrayList<>();
		tris = new ArrayList<>();
	}
	
	public void addFace(
		AIMeshData mesh, 
		AIFace face
	) {
		int numIndices = face.mNumIndices();
		
		if(numIndices < 3 || numIndices > 4) {
			throw new Error("Found face with invalid vertex count in " + mesh.name);
		}
		
		IntBuffer indices = face.mIndices();
		
		int a = indices.get(0);
		int b = indices.get(1);
		int c = indices.get(2);
		
		if(numIndices == 3) {
			tris.add(a);
			tris.add(b);
			tris.add(c);
		} else {
			int d = indices.get(3);
			
			quads.add(a);
			quads.add(b);
			quads.add(d);
			quads.add(c);
		}
	}
	
}
