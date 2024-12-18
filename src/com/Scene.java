package com;

import java.util.Random;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;

public class Scene {

	private final Random random;
	private Vector respawns;
	private Bot[] bots;
	private int frame;
	private Graphics3D g3d;
	private House house;
	private int miny;
	private Respawn start;
	private Respawn finish;
	private Respawn[] enemies; // респауны ботов
	private final int frequency;
	private final int max_enemy_count; // Максимально возможное кол-во ботов на сцене
	private int enemy_count; // Кол-во добавленных ботов
	private int part;

	Scene(int width, int height, House house, Respawn start, Respawn finish, Respawn[] enemies, int max_enemy_count, int frequency) {
		this.random = new Random();
		this.respawns = new Vector();
		this.frame = 0;
		this.g3d = new Graphics3D(width, height);
		this.house = house;
		this.start = start;
		this.finish = finish;
		this.enemies = enemies;
		this.max_enemy_count = max_enemy_count;
		this.frequency = frequency;
		this.countWorldSize(house);
		this.bots = new Bot[5];
		this.bots[0] = new BigZombie(new Vector3D());

		for(width = 1; width < this.bots.length; ++width) {
			this.bots[width] = new Zombie(new Vector3D());
		}

	}

	public final void reset() {
		Vector var1 = this.house.getObjects();
		int var2 = 0;

		while(var2 < var1.size()) {
			if(var1.elementAt(var2) instanceof Bot) {
				var1.removeElementAt(var2);
			} else {
				++var2;
			}
		}

		this.frame = 0;
		this.enemy_count = 0;
		this.part = -1;
	}

	public final void destroy() {
		this.g3d.destroy();
		this.g3d = null;
		this.house.destroy();
		this.house = null;

		for(int var1 = 0; var1 < this.bots.length; ++var1) {
			this.bots[var1].destroy();
			this.bots[var1] = null;
		}

		this.bots = null;
	}

	private void countWorldSize(House house) {
		int var2 = Integer.MAX_VALUE;
		int var3 = Integer.MIN_VALUE;
		int var4 = Integer.MAX_VALUE;
		int var5 = Integer.MIN_VALUE;
		int var6 = Integer.MAX_VALUE;
		int var7 = Integer.MIN_VALUE;
		Room[] var11 = house.getRooms();

		for(int var8 = 0; var8 < var11.length; ++var8) {
			Room var9;
			Mesh var10 = (var9 = var11[var8]).getMesh();
			var2 = Math.min(var2, var9.getMinX());
			var4 = Math.min(var4, var10.minY());
			var6 = Math.min(var6, var9.getMinZ());
			var3 = Math.max(var3, var9.getMaxX());
			var5 = Math.max(var5, var10.maxY());
			var7 = Math.max(var7, var9.getMaxZ());
		}

		System.out.println("World size:");
		System.out.println("Size X " + (var3 - var2));
		System.out.println("Size Y " + (var5 - var4));
		System.out.println("Size Z " + (var7 - var6));
		this.miny = var4;
	}

	private int random(int x) {
		return Math.abs(this.random.nextInt()) % x;
	}

	public final Vector3D getStartPoint() {
		return this.start.point;
	}

	// добавляет в Vector respawns респауны ботов из комнаты (?) под номером part 
	private void addRespawn(int part, Vector respawns) {
		for(int var3 = 0; var3 < this.enemies.length; ++var3) {
			Respawn var4;
			if((var4 = this.enemies[var3]).part == part) {
				respawns.addElement(var4);
			} else {
				if(this.enemies[var3].respa == true)
					this.enemies[var3].mode = -127;
			}
		}

	}

	public final int render(int part) {
		part = this.house.render(this.g3d, part);
		this.g3d.render();
		return part;
	}

	public final void flush(Graphics g, int x, int y) {
		this.g3d.flush(g, 0, y);
	}

	public final void update(Player player) {
		int var2 = player.getPart();
		Vector var3 = this.house.getObjects();
		int var5 = var2;
		Scene var4 = this;
		Vector var6 = this.house.getObjects();
		int var7;
		GameObject var8;
		int var10;

		// Уборка ботов из дальних комнат и провалившихся ботов 
		if(this.frame % 5 == 0) { // 5
			for(var7 = 0; var7 < var6.size(); ++var7) {
				var8 = (GameObject) var6.elementAt(var7);
				House var10000 = var4.house;
				int var10001 = var8.getPart();
				int var11 = var5;
				var10 = var10001;
				House var9 = var10000;

				// Проверка, в той же комнате или в соседней находится игрок
				boolean var21;
				if(var10 != -1 && var5 != -1) {
					if(var10 == var5) {
						var21 = true;
					} else {
						Room[] var12 = var9.getNeighbourRooms(var10);
						int var13 = 0;

						while(true) {
							if(var13 >= var12.length) {
								var21 = false;
								break;
							}

							if(var12[var13].getId() == var11) {
								var21 = true;
								break;
							}

							++var13;
						}
					}
				} else {
					var21 = false;
				}

				// Если игрок и бот в разных комнатах, или бот провалился, то убрать его
				if(!var21 || var8.getPart() == -1 || var8.getCharacter().getTransform().m13 < var4.miny << 1) {
					if(!var8.isDead()) {
						--var4.enemy_count;
					}

					var4.house.removeObject(var8);
				}
			}
		}

		var7 = 0;

		// Уборка трупов, если подошло убирать
		while(var7 < var6.size()) {
			if((var8 = (GameObject) var6.elementAt(var7)).isTimeToRenew()) {
				var4.house.removeObject(var8);
			} else {
				++var7;
			}
		}

		// Расстановка ботов
		int var24;
		int var26;
		if(this.part != var2 || this.frame % this.frequency == 0) {
			int var10002 = player.getPosX();
			int var10003 = player.getPosZ();
			var26 = player.getCharacter().getRadius() * 7;
			var7 = var10003;
			var24 = var10002;
			var5 = var2;
			var4 = this;

			// Если кол-во добавленных ботов < макс. кол-ва ботов, то 
			if(this.enemy_count < this.max_enemy_count) {
				Vector var27 = this.house.getObjects();

				for(var10 = 0; var10 < var4.bots.length && var4.enemy_count < var4.max_enemy_count; ++var10) {
					Bot var29;
					if((!((var29 = var4.bots[var10]) instanceof BigZombie) || var4.random(8) == 0) && !var27.contains(var29)) {
						Scene var18 = var4;
						Respawn var20;
						if(var5 == -1) {
							var20 = null;
						} else {
							var4.respawns.removeAllElements();
							// Расстановка респаунов ботов в той комнате, где нажодится игрок
							var4.addRespawn(var5, var4.respawns);
							Room[] var14 = var4.house.getNeighbourRooms(var5);

							// Расстановка респаунов ботов в соседних от игрока комнатах
							for(int var15 = 0; var15 < var14.length; ++var15) {
								var18.addRespawn(var14[var15].getId(), var18.respawns);
							}

							// Выбор рандомного респауна
							var20 = var18.respawns.isEmpty() ? null : (Respawn) var18.respawns.elementAt(var18.random(var18.respawns.size()));
						}

						// Если респаун не нулевой, 
						// тогда, если крадрат растояния от игрока до респауна > (радиус_игрока*7)^2,
						// то добавить бота в комнату
						Respawn var30 = var20;
						if(var20 != null) {
							Vector3D var32 = var30.point;
							int var31 = var32.z;
							int var19 = var30.point.x;
							var19 = var24 - var19;
							var31 = var7 - var31;
							if(var19 * var19 + var31 * var31 > var26 * var26) {
								if(var30.mode == -128 || var30.mode < var30.cmode) {
									var29.set(var32);
									if(var30.mode > -128 && var30.cmode < 255) {
										var30.cmode++;
									}
									var4.house.addObject((RoomObject) var29);
									++var4.enemy_count;
								}
							}
						}
					}
				}
			}

			this.part = var2;
		}

		Vector var23 = var3;
		var4 = this;

		// recomputePart
		GameObject var25;
		for(var24 = 0; var24 < var23.size(); ++var24) {
			var25 = (GameObject) var23.elementAt(var24);
			var4.house.recomputePart(var25);
		}

		Vector var22 = var3;

		// collisionTest
		for(var24 = 0; var24 < var22.size(); ++var24) {
			if(!(var25 = (GameObject) var22.elementAt(var24)).isDead()) {
				for(var26 = var24 + 1; var26 < var22.size(); ++var26) {
					GameObject var28;
					if(!(var28 = (GameObject) var22.elementAt(var26)).isDead()) {
						Character.collisionTest(var25.getCharacter(), var28.getCharacter());
					}
				}
			}
		}

		var23 = var3;
		var4 = this;

		// update
		for(var24 = 0; var24 < var23.size(); ++var24) {
			Vector3D var17 = (var25 = (GameObject) var23.elementAt(var24)).getCharacter().getSpeed();
			var17.y -= 20;
			var25.update(var4);
		}

		++this.frame;
	}

	public final House getHouse() {
		return this.house;
	}

	public final Graphics3D getG3D() {
		return this.g3d;
	}

	public final int getFrame() {
		return this.frame;
	}

	public final int getEnemyCount() {
		return this.max_enemy_count;
	}

	public final boolean isLevelCompleted(Player player) {
		Character var3;
		Matrix var4 = (var3 = player.getCharacter()).getTransform();
		Vector3D var2 = this.finish.point;
		int var10000 = var4.m03;
		int var10001 = var4.m13;
		int var10002 = var4.m23;
		int var10003 = var2.x;
		int var10004 = var2.y;
		int var10005 = var2.z;
		int var8 = var3.getRadius();
		int var7 = var10005;
		int var6 = var10004;
		int var5 = var10003;
		int var11 = var10002;
		int var10 = var10001;
		int var9 = var10000;
		var9 = var5 - var9;
		var10 = var6 - var10;
		var11 = var7 - var11;
		return (Math.abs(var9) <= var8 && Math.abs(var10) <= var8 && Math.abs(var11) <= var8 ? var9 * var9 + var10 * var10 + var11 * var11 <= var8 * var8 : false) && this.isWinner(player);
	}

	public final boolean isWinner(Player player) {
		return player.getFrags() >= this.max_enemy_count;
	}

	//public an() {}
	// ???
	public static int isPointOnMesh(Mesh mesh, int x, int y, int z) {
		RenderObject[] var8 = mesh.getPoligons();
		int var4 = Integer.MIN_VALUE;

		for(int var5 = 0; var5 < var8.length; ++var5) {
			RenderObject var6;
			int var9;
			if((var6 = var8[var5]) instanceof Polygon3V) {
				Polygon3V var7 = (Polygon3V) var6;
				if(MathUtils.isPointOnPolygon(x, z, var7.a, var7.b, var7.c, var7.ny) && (var9 = a_int_sub(var7.a, var7.nx, var7.ny, var7.nz, x, y, z)) < y && var9 > var4) {
					var4 = var9;
				}
			}

			if(var6 instanceof Polygon4V) {
				Polygon4V var10 = (Polygon4V) var6;
				if(MathUtils.isPointOnPolygon(x, z, var10.a, var10.b, var10.c, var10.d, var10.ny) && (var9 = a_int_sub(var10.a, var10.nx, var10.ny, var10.nz, x, y, z)) < y && var9 > var4) {
					var4 = var9;
				}
			}
		}

		if(var4 != Integer.MIN_VALUE) {
			return var4;
		} else {
			return Integer.MAX_VALUE;
		}
	}

	// ???
	private static int a_int_sub(Vertex a, int nx, int ny, int nz, int px, int py, int pz) {
		if(ny >= 0) {
			return Integer.MAX_VALUE;
		} else {
			px -= a.x;
			int var7 = py - a.y;
			int var8 = pz - a.z;
			return py - (px * nx + var7 * ny + var8 * nz) / ny;
		}
	}
}
