package com;

public final class Ray {

	boolean collision = false;
	int distance;
	final Vector3D collisionPoint = new Vector3D();
	final Vector3D start = new Vector3D();
	final Vector3D dir = new Vector3D();

	public Ray() {
		this.reset();
	}

	public final boolean isCollision() {
		return this.collision;
	}

	public final int getDistance() {
		return this.distance;
	}

	public final Vector3D getCollisionPoint() {
		return this.collisionPoint;
	}

	public final void reset() {
		this.collision = false;
		this.distance = Integer.MAX_VALUE;
	}

	public final Vector3D getStart() {
		return this.start;
	}

	public final Vector3D getDir() {
		return this.dir;
	}
}
