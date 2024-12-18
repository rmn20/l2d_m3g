package com;

public final class Character {

	private static Vector3D tmpVec = new Vector3D();
	
	private Vector3D pos = new Vector3D();
	private Vector3D rot = new Vector3D();
	private Vector3D speed = new Vector3D();
	private int radius, height;
	
	private boolean onFloor = false;
	private boolean colDetected = false;

	public Character(int radius, int height) {
		reset();
		set(0, 0);
	}

	public final void set(int radius, int height) {
		this.radius = radius;
		this.height = height;
	}

	public final void reset() {
		onFloor = false;
		colDetected = false;
		
		pos.set(0, 0, 0);
		rot.set(0, 0, 0);
		speed.set(0, 0, 0);
	}

	public final void collisionTest(int part, House house) {
		tmpVec.set(pos);
		tmpVec.y += height;
		
		colDetected = house.sphereCast(part, tmpVec, radius);
		if(colDetected) {
			pos.set(tmpVec);
			pos.y -= height;
		}

		onFloor = false;
		int floorY = house.getFloorY(part, pos.x, pos.y + height, pos.z);
		if(floorY != Integer.MAX_VALUE && floorY > pos.y) {
			pos.y = floorY;
			onFloor = true;
		}
	}

	// ? расстояние до другого персонажа
	public final long distanceSquared(Character ch) {
		Vector3D pos1 = pos;
		Vector3D pos2 = ch.pos;
		
		int xDist = pos1.x - pos2.x;
		int yDist = pos1.y - pos2.y;
		int zDist = pos1.z - pos2.z;
		
		return xDist * xDist + yDist * yDist + zDist * zDist;
	}

	public static void collisionTest(Character c1, Character c2) {
		Vector3D pos1 = c1.pos;
		Vector3D pos2 = c2.pos;
		
		int rSum = c1.radius + c2.radius;
		
		int dx = pos1.x - pos2.x;
		int dy = pos1.y - pos2.y;
		int dz = pos1.z - pos2.z;
		
		if(Math.abs(dx) <= rSum && Math.abs(dy) <= rSum && Math.abs(dz) <= rSum) {
			long distSqr = (long)dx*dx + (long)dy*dy + (long)dz*dz;
			
			if(distSqr < rSum * rSum) {
				if(distSqr != 0L) {
					distSqr = (long) (1.0F / MathUtils.invSqrt(distSqr));
				} else {
					dx = 1;
				} 

				int dist = (int) (rSum - distSqr);
				
				tmpVec.set(dx, dy, dz);
				tmpVec.setLength(dist / 2);
				
				pos1.add(tmpVec);
				pos2.sub(tmpVec);
			}

		}
	}

	public final void moveZ(int d) {
		if(onFloor) {
			speed.x += (int) ((float) Math.sin(rot.y / (float)(1 << 14) * MathUtils.FPI * 2) * d);
			speed.z += (int) ((float) Math.cos(rot.y / (float)(1 << 14) * MathUtils.FPI * 2) * d);
		}

	}

	public final void moveX(int d) {
		if(onFloor) {
			speed.x += (int) ((float) Math.cos(rot.y / (float)(1 << 14) * MathUtils.FPI * 2) * d);
			speed.z -= (int) ((float) Math.sin(rot.y / (float)(1 << 14) * MathUtils.FPI * 2) * d);
		}

	}

	public final void rotY(int angle) {
		rot.y = (rot.y + angle * (1 << 14) / 360) & ((1 << 14) - 1);
	}

	public final void rotX(int angle) {
		rot.x += angle * (1 << 14) / 360;
	}

	public final void jump(int jump, float accel) {
		if(onFloor) {
			speed.y += jump;
			speed.x = (int) (speed.x * accel);
			speed.y = (int) (speed.y * accel);
			speed.z = (int) (speed.z * accel);
		}

	}

	public final void update() {
		tmpVec.set(speed);
		
		//Limit speed
		int radLimit = (int) (radius * 0.8F);
		if(tmpVec.lengthSquared() > radLimit * radLimit) {
			tmpVec.setLength(radLimit);
		}

		pos.x += tmpVec.x;
		pos.y += tmpVec.y;
		pos.z += tmpVec.z;
	}

	public final int getRadius() {
		return radius;
	}

	public final int getHeight() {
		return height;
	}

	public final Vector3D getSpeed() {
		return speed;
	}

	public final Vector3D getPosition() {
		return pos;
	}

	public final Vector3D getRotation() {
		return rot;
	}

	public final boolean isOnFloor() {
		return onFloor;
	}

	public final boolean isColDetected() {
		return colDetected;
	}
}
