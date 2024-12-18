package com;

final class Blood {

	private static final Texture bloodTex = Texture.createTexture("/blood.png");
	
	private GameObject parent;
	private int frame = Integer.MAX_VALUE;
	private Sprite sprite;

	public Blood(GameObject obj) {
		parent = obj;

		sprite = new Sprite(bloodTex);
		sprite.setScale(5);
	}

	public final void reset() {
		sprite.setScale(5);
		frame = Integer.MAX_VALUE;
	}

	public final void destroy() {
		parent = null;
		sprite.destroy();
		sprite = null;
	}

	public final void bleed() {
		frame = 0;
		sprite.mirX = !sprite.mirX;
		sprite.mirY = !sprite.mirY;
	}

	public final void render(Renderer g3d, int sz) {
		Character parentCh = parent.getCharacter();
		Vector3D parentPos = parentCh.getPosition();

		sprite.getPosition().set(parentPos.x, parentPos.y + parentCh.getHeight(), parentPos.z);

		frame++;
		sprite.setScale(5 * frame);
		sprite.setOffset(0, -sprite.getHeight() / 2 - frame * 40);
		g3d.addSprite(sprite);
	}

	public final boolean isBleeding() {
		return frame < 7;
	}
}
