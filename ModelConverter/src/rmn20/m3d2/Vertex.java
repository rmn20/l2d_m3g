package rmn20.m3d2;

import java.util.ArrayList;
import rmn20.assimp.BoneWeightf;
import rmn20.assimp.Vertexf;


/**
 *
 * @author Roman
 */
public class Vertex {
	
	Vector3i[] atts;
	int[] attIdx;
	
	ArrayList<BoneWeighti> bones = new ArrayList<>();
	
	public Vertex(Vertexf v) {
		atts = new Vector3i[Mesh.ATTRIBUTES_COUNT];
		
		atts[Mesh.POS] = Vector3i.createPos(v.atts[Mesh.POS]);
		
		if(v.atts[Mesh.NORM] != null) {
			atts[Mesh.NORM] = Vector3i.createNorm(v.atts[Mesh.NORM]);
		}
		
		if(v.atts[Mesh.UV] != null) {
			atts[Mesh.UV] = Vector3i.createUV(v.atts[Mesh.UV]);
		}
		
		if(v.atts[Mesh.COL] != null) {
			atts[Mesh.COL] = Vector3i.createCol(v.atts[Mesh.COL]);
		}
		
		attIdx = new int[Mesh.ATTRIBUTES_COUNT];
		
		if(!v.weights.isEmpty()) {
			for(BoneWeightf weight : v.weights) {
				int quantWeight = Math.min(255, Math.round(weight.weight * 255 / v.maxWeight));
				if(quantWeight <= 0) break;
				
				bones.add(new BoneWeighti(weight.boneId, quantWeight));
			}
		}
	}
	
	public boolean equals(Object o) {
		if(o instanceof Vertex) {
			Vertex v = (Vertex) o;
			
			boolean eq = true;
			
			for(int i = 0; i < Mesh.ATTRIBUTES_COUNT; i++) {
				eq &= atts[i] == null ? v.atts[i] == null : atts[i].equals(v.atts[i]);
			}
			
			eq &= bones.equals(v.bones);
			
			return eq;
		}
		
		return super.equals(o);
	}
}
