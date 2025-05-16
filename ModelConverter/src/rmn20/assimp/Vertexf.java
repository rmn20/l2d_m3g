package rmn20.assimp;

import org.lwjgl.assimp.AIColor4D;
import org.lwjgl.assimp.AIVector3D;
import rmn20.m3d2.Mesh;

/**
 *
 * @author Roman
 */
public class Vertexf {
	
	public Vector3f[] atts;
	
	public Vertexf(
		AIVector3D.Buffer verts, 
		AIVector3D.Buffer norms, 
		AIVector3D.Buffer uvs, 
		AIColor4D.Buffer cols,
		int index
	) {
		atts = new Vector3f[Mesh.ATTRIBUTES_COUNT];
		
		atts[Mesh.POS] = new Vector3f(verts.get(index));
		
		if(norms != null) atts[Mesh.NORM] = new Vector3f(norms.get(index));
		if(uvs != null) atts[Mesh.UV] = new Vector3f(uvs.get(index));
		if(cols != null) atts[Mesh.COL] = new Vector3f(cols.get(index));
	}
}
