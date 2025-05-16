package rmn20.m3d2;

import java.util.ArrayList;
import rmn20.assimp.AIMeshData;
import rmn20.assimp.AIModelData;

/**
 *
 * @author Roman
 */
public class Model {
	/*public float posScale;
	public float posOffsetX, posOffsetY, posOffsetZ;
	public float uvScale;*/
	
	ArrayList<Mesh> meshes = new ArrayList<>();
	
	Vector3i 
		posMin = new Vector3i(Integer.MAX_VALUE), 
		posMax = new Vector3i(Integer.MIN_VALUE);
	
	public Model(AIModelData inModel) {
		for(AIMeshData inMesh : inModel.meshes) {
			Mesh mesh = new Mesh(inMesh);
			addMesh(mesh);
		}
	}
	
	public void addMesh(Mesh mesh) {
		meshes.add(mesh);
		
		posMin.min(mesh.posMin);
		posMax.max(mesh.posMax);
	}
}
