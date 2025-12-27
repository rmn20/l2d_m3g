package com;

public abstract class RoomObject {

	private int oldX, oldY, oldZ;
	private int part;

	public abstract void render(Renderer g3d);

	public final int getPart() {
		return part;
	}

	public void setPart(int part) {
		this.part = part;
	}

	public abstract int getPosX();
	public abstract int getPosY();
	public abstract int getPosZ();

	protected boolean isNeedRecomputePart() {
		int x = getPosX();
		int y = getPosY();
		int z = getPosZ();

		if(oldX == x && oldY == y && oldZ == z && part != -1) {
			return false;
		} else {
			oldX = x;
			oldY = y;
			oldZ = z;

			return true;
		}
	}
}
