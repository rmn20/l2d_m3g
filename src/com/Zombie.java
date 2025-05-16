package com;

import java.util.Vector;
import javax.microedition.m3g.Node;

public final class Zombie extends Bot {

	private static final int maxHp = IniFile.createFromResource("/setting.txt").getInt("LIFE_ZOMBIE");
	private static int damageValue = 1;
	private static final int modelHeight;
	private static MeshData[] meshes;
	
	private int state = -1;
	private Morphing anim;
	private Node mesh;
	private Player enemy = null;
	private Vector3D targetPos = new Vector3D();

	static {
		Texture tex = Texture.createTexture("/zombie.png");
		meshes = MeshData.loadMeshes3D2("/zombie.3d2", tex.img, 4.5F * 300, true, false);
		
		MeshData mesh = meshes[0];

		Vector3D min = mesh.getAABBMin(), max = mesh.getAABBMax();
		modelHeight = max.y - min.y;
	}

	public Zombie(Vector3D pos) {
		anim = new Morphing(meshes);
		mesh = anim.getMesh();
		set(pos);
	}

	public final void set(Vector3D pos) {
		super.set(pos);
		setHp(maxHp);
		setCharacterSize(modelHeight);
	}

	public final void destroy() {
		super.destroy();
		enemy = null;
		//anim.getMesh().destroy();
		anim.destroy();
		anim = null;
	}

	public final void render(Renderer g3d) {
		//Matrix var6 = getCharacter().getTransform();
		//var6 = g3d.computeFinalMatrix(var6);
		if(state == 1) {
			anim.setFrame(getFrame() * 140);
		} else if(state == 2) {
			anim.setFrame(getFrame() * 140 * 5);
		}

		//g3d.transformAndProjectVertices(animation.getMesh(), var6);
		Character ch = character;
		Vector3D pos = ch.getPosition();
		Vector3D rot = ch.getRotation();
		g3d.addMesh(mesh, pos, rot);
		//increaseMeshSz(animation.getMesh(), 1000);
		renderBlood(g3d, 1500);
	}

	protected final void action(Scene scene) {
		if(getFrame() % 8 == 0) {
			House house = scene.getHouse();
			Vector objs = house.getObjects();
			Character ch = getCharacter();

			if(enemy != null && enemy.isDead()) enemy = null;
			if(enemy == null) enemy = find(objs);

			if(enemy == null) {
				state = -1;
			} else {
				Player target = enemy;

				boolean targetVisible = false;

				if(visbilityCheck(house, target)) {
					//Walk towards target
					targetPos.set(target.getCharacter().getPosition());

					targetVisible = true;
				} else {
					//Walk towards portal
					Portal portal = commonPortal(house, getPart(), target.getPart());
					if(portal != null) targetPos.set(portal.getCenter());
				}

				lookAt(targetPos.x, targetPos.z);
				long distance = ch.distanceSquared(enemy.getCharacter());

				if(targetVisible && distance <= sqr(ch.getRadius() + enemy.getCharacter().getRadius()) * 1.2F) {
					state = 2;
				} else {
					if(ch.isColDetected()) ch.jump(140, 1.2F);
					state = 1;
				}
			}
		}

		if(state == 1) {
			moveZ(140);
		}

		if(state == 2 && getFrame() % 4 == 0) {
			enemy.damage(this, damageValue);
		}

	}

	protected final void drop(Scene scene) {
		super.drop(scene);
		state = -1;
	}
}
