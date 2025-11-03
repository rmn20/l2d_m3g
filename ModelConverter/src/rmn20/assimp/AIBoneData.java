package rmn20.assimp;

import java.nio.FloatBuffer;
import org.lwjgl.assimp.AIBone;
import org.lwjgl.assimp.AINode;
import org.lwjgl.system.MemoryUtil;

/**
 *
 * @author Roman
 */
public class AIBoneData {
	public String name;
	public int parent;
	public float[] offsetMatrix;
	
	public AIBoneData(AINode node, int parent) {
		this.name = node.mName().dataString();
		this.parent = parent;
	}
	
	public void setOffsetMatrix(AIBone bone) {
		FloatBuffer mat = MemoryUtil.memFloatBuffer(bone.mOffsetMatrix().address(), 16);
		offsetMatrix = new float[16];
		
		for(int i = 0; i < 16; i++) {
			offsetMatrix[i] = mat.get(i);
		}
	}
}
