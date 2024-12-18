package com;

public final class Character {

	private static Vector3D tmpVec = new Vector3D(); // ? collisionPoint из Ray
	private int player_radius; // ?
	private int player_height; // ?
	private Matrix transform = new Matrix();
	private Vector3D speed = new Vector3D();
	private boolean onFloor = false;
	private boolean col = false; // col из Character.collisionTest(Home home)

	public Character(int radius, int height) {
		this.reset();
		this.set(0, 0);
	}

	public final void set(int radius, int height) {
		this.player_radius = radius;
		this.player_height = height;
	}

	public final void reset() {
		this.onFloor = this.col = false;
		this.speed.set(0, 0, 0);
		this.transform.setIdentity();
	}

	public final void collisionTest(int part, House house) {
		tmpVec.set(this.transform.m03, this.transform.m13 + this.player_height, this.transform.m23);
		this.col = house.sphereCast(part, tmpVec, this.player_radius);
		if(this.col) {
			this.transform.m03 = tmpVec.x;
			this.transform.m13 = tmpVec.y - this.player_height;
			this.transform.m23 = tmpVec.z;
		}

		this.onFloor = false;
		if((part = house.a_int_sub(part, this.transform.m03, this.transform.m13 + this.player_height, this.transform.m23)) != Integer.MAX_VALUE && part > this.transform.m13) {
			this.transform.m13 = part;
			this.onFloor = true;
		}

	}

	// ? расстояние до другого персонажа
	public final long distance(Character character) {
		Matrix var2 = this.transform;
		Matrix var5 = character.transform;
		int var3 = var2.m03 - var5.m03;
		int var4 = var2.m13 - var5.m13;
		int var6 = var2.m23 - var5.m23;
		return (long) var3 * (long) var3 + (long) var4 * (long) var4 + (long) var6 * (long) var6;
	}

	public static void collisionTest(Character c1, Character c2) {
		Matrix var2 = c1.transform;
		Matrix var3 = c2.transform;
		int var10 = c1.player_radius + c2.player_radius;
		int var11 = var2.m03 - var3.m03;
		int var4 = var2.m13 - var3.m13;
		int var5 = var2.m23 - var3.m23;
		if(Math.abs(var11) <= var10 && Math.abs(var4) <= var10 && Math.abs(var5) <= var10) {
			long var8;
			if((var8 = (long) var11 * (long) var11 + (long) var4 * (long) var4 + (long) var5 * (long) var5) < (long) (var10 * var10)) {
				if(var8 != 0L) {
					var8 = (long) ((int) (1.0F / MathUtils.invSqrt((float) var8)));
				} else {
					var11 = 1;
				}

				var10 = (int) ((long) var10 - var8);
				Vector3D var12;
				(var12 = new Vector3D(var11, var4, var5)).setLength(var10 / 2);
				move(var2, var12.x, var12.y, var12.z);
				move(var3, -var12.x, -var12.y, -var12.z);
			}

		}
	}

	private static void move(Matrix matrix, int dx, int dy, int dz) {
		matrix.m03 += dx;
		matrix.m13 += dy;
		matrix.m23 += dz;
	}

	public final void moveZ(int d) {
		if(this.onFloor) {
			this.speed.x += this.transform.m02 * d >> 14;
			this.speed.z += this.transform.m22 * d >> 14;
		}

	}

	public final void moveX(int d) {
		if(this.onFloor) {
			this.speed.x += this.transform.m00 * d >> 14;
			this.speed.z += this.transform.m20 * d >> 14;
		}

	}

	public final void rotY(int angle) {
		this.transform.rotY(angle);
	}

	public final void jump(int jump, float force) {
		if(this.onFloor) {
			this.speed.y += jump;
			this.speed.x = (int) ((float) this.speed.x * force);
			this.speed.y = (int) ((float) this.speed.y * force);
			this.speed.x = (int) ((float) this.speed.x * force);
		}

	}

	public final void update() {
		tmpVec.set(this.speed);
		int var1 = (int) ((float) this.player_radius * 0.8F);
		if(tmpVec.lengthSquared() > var1 * var1) {
			tmpVec.setLength(var1);
		}

		this.transform.m03 += tmpVec.x;
		this.transform.m13 += tmpVec.y;
		this.transform.m23 += tmpVec.z;
	}

	public final Matrix getTransform() {
		return this.transform;
	}

	public final int getRadius() {
		return this.player_radius;
	}

	public final int getHeight() {
		return this.player_height;
	}

	public final Vector3D getSpeed() {
		return this.speed;
	}

	public final boolean isOnFloor() {
		return this.onFloor;
	}

	public final boolean isCollision() {
		return this.col;
	}
}
