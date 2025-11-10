package rmn20.m3d2;

/**
 *
 * @author Roman
 */
public class BoneWeighti {
	public int boneId;
	public int weight;

	public BoneWeighti(int boneId, int weight) {
		this.boneId = boneId;
		this.weight = weight;
	}
	
	public boolean equals(Object o) {
		if(o instanceof BoneWeighti) {
			BoneWeighti b = (BoneWeighti) o;
			
			return boneId == b.boneId && weight == b.weight;
		}
		
		return super.equals(o);
	}
}
