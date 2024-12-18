package com;

import java.util.Vector;

public final class BigZombie extends Bot {

	private static final int max_hp = Stringer.createFromResource("/setting.txt").getInt("LIFE_BIG_ZOMBIE");
	private static int damage_value = 7; // урон
	private static short[][] data;
	private static Asset animData;
	private static final int model_height;
	private int state = -1; // состояние (-1 - стоять, 1 - идти, 2 - атаковать игрока)
	private Morphing anim;
	private Player enemy = null;
	private Vector3D dir = new Vector3D(); // ?

	public BigZombie(Vector3D pos) {
		this.anim = new Morphing(data, animData.copy());
		this.set(pos);
	}

	public final void set(Vector3D pos) {
		super.set(pos);
		this.setHp(max_hp);
		this.setCharacterSize(model_height);
	}

	public final void destroy() {
		super.destroy();
		this.enemy = null;
		this.anim.getMesh().destroy();
		this.anim.destroy();
		this.anim = null;
	}

	public final void render(Graphics3D g3d, int x1, int y1, int x2, int y2) {
		Matrix var6 = this.getCharacter().getTransform();
		var6 = g3d.computeFinalMatrix(var6);
		if(this.state == 1) {
			this.anim.setFrame(this.getFrame() * 135);
		}

		if(this.state == 2) {
			this.anim.setFrame(this.getFrame() * 135 << 2);
		}

		g3d.transformAndProjectVertices(this.anim.getMesh(), var6);
		g3d.addMesh(this.anim.getMesh(), x1, y1, x2, y2);
		increaseMeshSz(this.anim.getMesh(), 1000);
		this.renderBlood(g3d, 1500);
	}

	// ? Поведение зомби (движение, поиск врага)
	protected final void action(Scene scene) {
		if(this.getFrame() % 8 == 0) {
			House var7;
			Vector var2 = (var7 = scene.getHouse()).getObjects();
			if(this.enemy != null && this.enemy.isDead()) {
				this.enemy = null;
			}

			if(this.enemy == null) {
				this.enemy = find(var2);
			}

			if(this.enemy != null) {
				Vector3D var4 = this.dir;
				Player var3 = this.enemy;
				boolean var10000;
				if(this.notCollided(var7, var3)) {
					Matrix var8 = var3.getCharacter().getTransform();
					var4.set(var8.m03, var8.m13, var8.m23);
					var10000 = true;
				} else {
					Portal var9;
					if((var9 = commonPortal(var7, this.getPart(), var3.getPart())) != null) {
						computeCentre(var9, var4);
					}

					var10000 = false;
				}

				boolean var10 = var10000;
				this.lookAt(this.dir.x, this.dir.z);
				long var5 = this.getCharacter().distance(this.enemy.getCharacter());
				if(var10 && (float) var5 <= (float) sqr(this.getCharacter().getRadius() + this.enemy.getCharacter().getRadius()) * 1.2F) {
					this.state = 2;
				} else {
					if(this.getCharacter().isCollision() && !var10) {
						this.getCharacter().jump(202, 1.2F);
					}

					if(var5 > sqr(this.getCharacter().getRadius() * 11)) {
						this.getCharacter().jump(135, 1.5F);
					}

					this.state = 1;
				}
			} else {
				this.state = -1;
			}
		}

		if(this.state == 1) {
			this.moveZ(135);
		}

		if(this.state == 2 && this.getFrame() % 7 == 0) {
			this.enemy.damage(this, damage_value);
		}

	}

	static {
		Texture texture;
		(texture = Texture.createTexture("/big_zombie.png")).setPerspectiveCorrection(false);
		Mesh[] meshes;
		Mesh mesh;
		(mesh = (meshes = Room.loadMeshes("/big_zombie.3d", 50.0F, 50.0F, 50.0F))[0]).setTexture(texture);
		model_height = mesh.maxY() - mesh.minY();
		animData = new Asset(mesh);
		data = Morphing.create(meshes);
	}
}
