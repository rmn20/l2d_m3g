package rmn20.assimp;

import java.util.ArrayList;

/**
 *
 * @author Roman
 */
public class AIModelData {
	
	public ArrayList<AIMeshData> meshes;
	
	AIModelData() {
		meshes = new ArrayList<>();
	}
	
	void addMesh(AIMeshData mesh) {
		meshes.add(mesh);
	}
}
