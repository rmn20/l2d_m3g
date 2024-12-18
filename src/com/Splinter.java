 package com;

final class Splinter {

	private static Texture texture = Texture.createTexture("/splinter.png");
	private int frame = Integer.MAX_VALUE;
	private Sprite sprite;

	public Splinter() {
		this.sprite = new Sprite(texture);
		this.sprite.setScale(5);
	}

	public final void set(int x, int y, int z) {
		this.sprite.getPosition().set(x, y, z);
		this.frame = 0;
	}

	public final void render(Renderer g3d, int sz) {
		++this.frame;
		this.sprite.setScale(5 * this.frame);
		this.sprite.setOffset(0, -this.sprite.getHeight() / 2 - this.frame * 40);
		g3d.addSprite(this.sprite);
	}

	// true, если проигрывается анимация осколка
	public final boolean isShatters() {
		return this.frame < 3;
	}
}
