package rmn20.assimp;

import java.util.ArrayList;

/**
 *
 * @author Roman
 */
public class AIMeshData {
	
	public String name;
	public ArrayList<AISubMeshData> submeshes;
	
	AIMeshData(String name) {
		this.name = name;
		
		submeshes = new ArrayList<>();
	}
	
	void addSubMesh(AISubMeshData mesh) {
		submeshes.add(mesh);
	}
	
}
