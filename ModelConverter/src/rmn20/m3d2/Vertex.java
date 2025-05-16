package rmn20.m3d2;

import rmn20.assimp.Vertexf;


/**
 *
 * @author Roman
 */
public class Vertex {
	
	Vector3i[] atts;
	int[] attIdx;
	
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
	}
	
	public boolean equals(Object o) {
		if(o instanceof Vertex) {
			Vertex v = (Vertex) o;
			
			boolean eq = true;
			
			for(int i = 0; i < Mesh.ATTRIBUTES_COUNT; i++) {
				eq &= atts[i] == null ? v.atts[i] == null : atts[i].equals(v.atts[i]);
			}
			
			return eq;
		}
		
		return super.equals(o);
	}
}
