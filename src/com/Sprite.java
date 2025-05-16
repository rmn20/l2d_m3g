package com;

import javax.microedition.m3g.Appearance;
import javax.microedition.m3g.CompositingMode;
import javax.microedition.m3g.Sprite3D;

public final class Sprite {

	private Vector3D pos = new Vector3D();
	private int offsetX = 0, offsetY = 0; //todo?? post transform pre project offset
	private int scale = 5;
	public boolean mirX = false, mirY = false;
	public Sprite3D s3d;

	public Sprite(Texture tex) {
		CompositingMode cm = new CompositingMode();
		cm.setBlending(CompositingMode.ALPHA);
		cm.setAlphaWriteEnable(false);
		cm.setAlphaThreshold(0.001f);
		cm.setDepthWriteEnable(true); //todo sprite sorting
		cm.setDepthTestEnable(false);

		Appearance ap = new Appearance();
		ap.setCompositingMode(cm);

		s3d = new Sprite3D(true, tex.img, ap);
		s3d.setCrop(0, 0, tex.w, tex.h);
	}

	public final void setScale(int scale) {
		this.scale = scale;
	}

	public final int getScale() {
		return scale;
	}

	public final void destroy() {
		s3d = null;
	}

	public final void setOffset(int x, int y) {
		this.offsetX = x;
		this.offsetY = y;
	}

	public final int getOffsetX() {
		return this.offsetX;
	}

	public final int getOffseY() {
		return this.offsetY;
	}

	public final Vector3D getPosition() {
		return this.pos;
	}

	public final int getHeight() {
		return s3d.getCropHeight() * this.scale;
	}

	public final int getWidth() {
		return s3d.getCropWidth() * this.scale;
	}

	public final boolean isVisible(int x1, int y1, int x2, int y2) {
		//todo
		return true;
		/*this.sz = this.pos.rz;
		 return this.sz > 0 ? false : (this.pos.sx < x2 && this.pos.sy < y2 ? this.size.sx > x1 && this.size.sy > y1 : false);*/
	}
}
