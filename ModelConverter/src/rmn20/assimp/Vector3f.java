package rmn20.assimp;

import org.lwjgl.assimp.AIColor4D;
import org.lwjgl.assimp.AIVector3D;

/**
 *
 * @author Roman
 */
public class Vector3f {
	public float x, y, z;
	
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3f(AIVector3D v) {
		this.x = v.x();
		this.y = v.y();
		this.z = v.z();
	}
	
	public Vector3f(AIColor4D v) {
		this.x = v.r();
		this.y = v.g();
		this.z = v.b();
	}
}
