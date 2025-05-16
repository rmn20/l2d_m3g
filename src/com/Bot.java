package com;

import java.util.Vector;

public abstract class Bot extends GameObject {

	private static final Ray ray = new Ray();
	private static final Vector3D tmpVec = new Vector3D(); // ?
	private static final Vector3D side = new Vector3D();
	private static final Vector3D up = new Vector3D();
	
	private Blood blood = new Blood(this);

	public void set(Vector3D pos) {
		Character ch = getCharacter();
		ch.reset();
		ch.getPosition().set(pos);
		
		blood.reset();
	}

	public void destroy() {
		blood.destroy();
		blood = null;
	}

	protected final boolean isNeedRecomputePart() {
		return getFrame() % 3 == 0 ? super.isNeedRecomputePart() : false;
	}

	protected final void renderBlood(Renderer g3d, int sz) {
		if(blood.isBleeding()) blood.render(g3d, 2250); //2250 should be sz?
	}

	public final boolean damage(GameObject attacker, int dmg) {
		Character ch = getCharacter();
		
		if(attacker != null && ch.isOnFloor()) {
			Vector3D pos = ch.getPosition();
			Vector3D attackerPos = attacker.getCharacter().getPosition();
			
			tmpVec.set(pos);
			tmpVec.sub(attackerPos);
			tmpVec.setLength(dmg * ch.getRadius() / 200);
			
			ch.getSpeed().add(tmpVec);
		}

		blood.bleed();
		return super.damage(attacker, dmg);
	}

	//AI
	public final void update(Scene scene) {
		if(!isDead()) {
			action(scene);
		} else {
			drop(scene);
		}

		super.update(scene);
	}

	protected abstract void action(Scene scene);

	//Fall animation at 0 hp
	protected void drop(Scene scene) {
		if(getCharacter().getRotation().x > -90 * (1 << 14) / 360) {
			getCharacter().rotX(-8);
		}
	}

	//todo? remove?
	/*protected static void increaseMeshSz(MeshData mesh, int val) {
		RenderObject[] var2 = mesh.getPolygons();

		for(val = 0; val < var2.length; ++val) {
			var2[val].sz += 1000;
		}

	}*/

	protected final boolean visbilityCheck(House house, GameObject target) {
		Vector3D pos = getCharacter().getPosition();
		Vector3D targetPos = target.getCharacter().getPosition();
		
		ray.reset();
		ray.getStart().set(pos.x, pos.y + getCharacter().getHeight(), pos.z);
		ray.getDir().set(targetPos.x - pos.x, targetPos.y - pos.y, targetPos.z - pos.z);
		house.rayCast(getPart(), ray);
		
		return !ray.isCollision();
	}

	//Returns portal connecting 2 rooms
	protected static Portal commonPortal(House house, int room1, int room2) {
		Portal[] portals = house.getRooms()[room1].getPortals(); //All portals from room 1

		for(int i = 0; i < portals.length; i++) {
			Portal portal = portals[i];
			
			int pRoom1Id = portal.getRoomFront().getId();
			int pRoom2Id = portal.getRoomBack().getId();
			
			if(pRoom1Id == room2 || pRoom2Id == room2) {
				return portal;
			}
		}

		return null;
	}

	protected static Player find(Vector objs) {
		for(int i = 0; i < objs.size(); i++) {
			GameObject obj = (GameObject) objs.elementAt(i);
			
			if(!obj.isDead() && obj instanceof Player) {
				return (Player) obj;
			}
		}

		return null;
	}

	protected final void lookAt(int posX, int posZ) {
		Vector3D pos = getCharacter().getPosition();
		Vector3D rot = getCharacter().getRotation();

		rot.y = (int) (MathUtils.atan2(posX, posZ, pos.x, pos.z) / 360f * (1 << 14));
	}

	protected static long sqr(int val) {
		return val * val;
	}
}
