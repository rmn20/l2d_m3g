package com;

public abstract class GameObject extends RoomObject {

	private int frame;
	protected final Character character = new Character(0, 0);
	private int hp;

	protected final void setCharacterSize(int modelHeight) {
		this.character.set((int) ((float) modelHeight / 2.5F), (int) ((float) modelHeight * 0.75F));
	}

	protected final void rotY(int angle) {
		this.character.rotY(angle);
	}

	protected final void moveZ(int d) {
		this.character.moveZ(d);
	}

	protected final void jump(int jump, float force) {
		this.character.jump(150, 1.2F);
	}

	public void update(Scene scene) {
		this.character.update();
		this.character.collisionTest(this.getPart(), scene.getHouse());
		if(this.character.isOnFloor()) {
			Vector3D var10000 = this.character.getSpeed();
			boolean var2 = true;
			Vector3D var3 = var10000;
			var10000.x /= 4;
			var3.y /= 4;
			var3.z /= 4;
		}

		++this.frame;
	}

	// true - если персонаж убит
	public boolean damage(GameObject obj, int dmg) {
		boolean var3 = this.isDead();
		this.hp -= dmg;
		if(this.hp < 0) {
			this.hp = 0;
		}

		if(var3 != this.isDead()) {
			this.frame = 0;
			return true;
		} else {
			return false;
		}
	}

	public final Character getCharacter() {
		return this.character;
	}

	public final int getHp() {
		return this.hp;
	}

	public final boolean isDead() {
		return this.hp <= 0;
	}

	public boolean isTimeToRenew() {
		return this.isDead() && this.frame > 25;
	}

	public final void setHp(int hp) {
		this.hp = hp;
	}

	public final int getFrame() {
		return this.frame;
	}

	public final int getPosX() {
		return this.character.getPosition().x;
	}

	public final int getPosZ() {
		return this.character.getPosition().z;
	}
}
