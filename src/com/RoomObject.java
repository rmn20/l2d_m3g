package com;

public abstract class RoomObject {

	private int oldX, oldZ;
	private int part;

	public abstract void render(Renderer g3d);

	public final int getPart() {
		return part;
	}

	public void setPart(int part) {
		this.part = part;
	}

	public abstract int getPosX();
	public abstract int getPosZ();

	protected boolean isNeedRecomputePart() {
		int x = getPosX();
		int z = getPosZ();

		if(oldX == x && oldZ == z && part != -1) {
			return false;
		} else {
			oldX = x;
			oldZ = z;

			return true;
		}
	}
}
