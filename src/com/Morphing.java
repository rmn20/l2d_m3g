package com;

import javax.microedition.m3g.IndexBuffer;
import javax.microedition.m3g.Mesh;
import javax.microedition.m3g.MorphingMesh;
import javax.microedition.m3g.VertexBuffer;

public final class Morphing {

	private Mesh firstMesh;
	private MorphingMesh morph;
	private float[] weights;

	public final void destroy() {
		firstMesh = null;
		morph = null;
		weights = null;
	}

	public Morphing(MeshData[] meshes) {
		firstMesh = meshes[0].getM3GMesh();
		
		if(meshes.length > 1) {
			VertexBuffer[] targets = new VertexBuffer[meshes.length - 1];
			for(int i=0; i<targets.length; i++) {
				targets[i] = (VertexBuffer) meshes[i + 1].getM3GMesh().getVertexBuffer();
			}

			morph = new MorphingMesh(
					(VertexBuffer) firstMesh.getVertexBuffer(), 
					targets, 
					(IndexBuffer) firstMesh.getIndexBuffer(0), 
					firstMesh.getAppearance(0)
					);
			weights = new float[targets.length];
		}
	}

	public final void setFrame(int time) {
		if(morph == null) return;
		
		for(int i=0; i<weights.length; i++) weights[i] = 0;
		
		int frame1 = (time / 1024) % (1 + weights.length);
		int frame2 = (frame1 + 1) % (1 + weights.length);
		
		int delta = time & 1023;
		
		if(frame1 > 0) weights[frame1 - 1] = (1024 - delta) / 1024f;
		if(frame2 > 0) weights[frame2 - 1] = delta / 1024f;
		
		morph.setWeights(weights);
	}

	public final Mesh getMesh() {
		return morph != null ? morph : firstMesh;
	}
}
