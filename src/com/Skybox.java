package com;

import javax.microedition.m3g.Texture2D;

public class Skybox {

	private boolean animation;
	private Texture tex;
	private MeshData mesh;
	private int x1, y1, x2, y2;
	private boolean resetView;
	private int frame;

	public Skybox() {
	}

	public Skybox(String modelPath, String texturePath) {
		this.animation = false;
		this.resetView = true;
		
		this.tex = Texture.createTexture(texturePath);
		
		this.mesh = MeshData.loadMeshes3D2(modelPath, this.tex.img, 300.0F * 300, false, true)[0];

		this.mesh.getM3GMesh().getAppearance(0).getCompositingMode().setDepthWriteEnable(false);
		this.mesh.getM3GMesh().getAppearance(0).getCompositingMode().setDepthTestEnable(true);
		this.mesh.getM3GMesh().getAppearance(0).setFog(null);
	}

	public void destroy() {
		this.tex.destroy();
		this.tex = null;
		this.mesh.destroy();
		this.mesh = null;
	}

	public void resetViewport() {
		this.resetView = true;
	}

	public boolean isVisible() {
		return !this.resetView;
	}

	public void addViewport(int x1, int y1, int x2, int y2) {
		if(this.resetView) {
			this.resetView = false;
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
		} else {
			if(x1 < this.x1) this.x1 = x1;
			if(y1 < this.y1) this.y1 = y1;
			if(x2 > this.x2) this.x2 = x2;
			if(y2 > this.y2) this.y2 = y2;
		}
	}

	public void render(Renderer g3d, Vector3D camPos) {
		if(animation) {
			Texture2D tex = mesh.getM3GMesh().getAppearance(0).getTexture(0);
			tex.setTranslation((float)frame / this.tex.w, 0, 0);
			frame += 1;
			if(frame >= this.tex.w) frame = 0;
		}
		
		g3d.setClip(x1, y1, x2, y2);
		g3d.addMesh(mesh.getM3GMesh(), camPos, null);
	}

   public void setAnimation(boolean enabled) {
      this.animation = enabled;
   }
}
