package rmn20.assimp;

import java.util.ArrayList;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIBone;
import org.lwjgl.assimp.AINode;

/**
 *
 * @author Roman
 */
public class AIMeshData {
	
	public String name;
	public ArrayList<AISubMeshData> submeshes;
	
	public ArrayList<AIBoneData> bones;
	public ArrayList<AIBoneData> roots;
	
	AIMeshData(String name) {
		this.name = name;
		
		submeshes = new ArrayList<>();
		
		bones = new ArrayList<>();
		roots = new ArrayList<>();
	}
	
	void addSubMesh(AISubMeshData mesh) {
		submeshes.add(mesh);
	}
	
	private AIBoneData addBone(AINode node, int parent) {
		AIBoneData data = new AIBoneData(node, parent);
		
		bones.add(data);
		int boneIdx = bones.size() - 1;
		
		PointerBuffer children = node.mChildren();
		int numChildren = node.mNumChildren();
		
		for(int i = 0; i < numChildren; i++) {
			AINode child = AINode.create(children.get(i));
			addBone(child, boneIdx);
		}
		
		return data;
	}
	
	private void addArmature(AINode armature) {
		roots.add(addBone(armature, -1));
	}
	
	private int findBone(String name) {
		for(int i = 0; i < bones.size(); i++) {
			if(bones.get(i).name.equals(name)) return i;
		}
		
		return -1;
	}
	
	int getBoneId(AIBone bone) {
		String boneName = bone.mName().dataString();
		
		int id = findBone(boneName);
		
		if(id == -1) {
			addArmature(bone.mArmature());
			id = findBone(boneName);
			
			if(id == -1) throw new Error("Invalid skeleton");
		}
		
		AIBoneData boneData = bones.get(id);
		if(boneData.offsetMatrix == null) boneData.setOffsetMatrix(bone);
		
		return id;
	}
	
}
