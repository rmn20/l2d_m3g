package com;

public class Respawn {

	final Vector3D point;
	final int part;

	public Respawn(Vector3D point, House house) {
		this.point = point;
		part = house.computePart(-1, point.x, point.z);
		
		if(part == -1) {
			System.out.println("ERROR: неправильная точка старта " + point.x + " " + point.y + " " + point.z);
		}

	}

	//public u() {}
	public static Scene createScene(int width, int height, String file) {
		IniFile ini = IniFile.createFromResource(file);

		float scale = ini.getFloat("WORLD_SCALE");
		Texture tex = Texture.createTexture(ini.getString("WORLD_TEXTURE"));
		MeshData[] meshes = MeshData.loadMeshes3D2(ini.getString("WORLD_MODEL"), tex.img, scale, false, true);
		
		House house = HouseCreator.create(meshes);
		
		String skyModel = ini.getString("SKYBOX_MODEL");
		String skyTex = ini.getString("SKYBOX_TEXTURE");
		Skybox skybox = null;
		
		if(skyModel != null && skyTex != null) {
			skybox = new Skybox(ini.getString("SKYBOX_MODEL"), ini.getString("SKYBOX_TEXTURE"));
			house.setSkybox(skybox);
		}

		Respawn start = readPoints(ini.getString("START"), house)[0];
		Respawn finish = readPoints(ini.getString("FINISH"), house)[0];
		Respawn[] spawnPoints = readPoints(ini.getString("ENEMIES"), house);

		int enemyCount = ini.getInt("ENEMY_COUNT");
		int spawnFrequency = ini.getInt("FREQUENCY");
		
		Scene scene = new Scene(width, height, house, start, finish, spawnPoints, enemyCount, spawnFrequency);
		
		return scene;
	}

	private static Respawn[] readPoints(String file, House house) {
		if(file == null) {
			return null;
		} else {
			StringBuffer strBuf = new StringBuffer();

			for(int i = 0; i < file.length(); i++) {
				char ch = file.charAt(i);
				if(ch != ' ') strBuf.append(ch);
			}

			String[] strPoints = MeshData.cutOnStrings(strBuf.toString(), ';');
			Respawn[] spawnPoints = new Respawn[strPoints.length];

			for(int i = 0; i < spawnPoints.length; i++) {
				int[] xyz = MeshData.cutOnInts(strPoints[i], ',');
				Vector3D pos = new Vector3D(xyz[0], xyz[1], xyz[2]);
				spawnPoints[i] = new Respawn(pos, house);
			}

			return spawnPoints;
		}
	}
}
