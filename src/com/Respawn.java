package com;

public class Respawn {

	final Vector3D point;
	final int part;

	public Respawn(Vector3D point, House house) {
		this.point = point;
		this.part = house.computePart(-1, point.x, point.z);
		if(this.part == -1) {
			System.out.println("ERROR: неправильная точка старта " + point.x + " " + point.y + " " + point.z);
		}

	}

	//public u() {}
	public static Scene createScene(int width, int height, String file) {
		IniFile ini;

		float scale = (ini = IniFile.createFromResource(file)).getFloat("WORLD_SCALE");
		Texture tex = Texture.createTexture(ini.getString("WORLD_TEXTURE"));
		MeshData[] meshes = MeshData.loadMeshes3D2(ini.getString("WORLD_MODEL"), tex.img, scale, false, true);
		String skyModel = ini.getString("SKYBOX_MODEL");

		String skyTex = ini.getString("SKYBOX_TEXTURE");
		Skybox skybox = null;
		if(skyModel != null && skyTex != null) {
			skybox = new Skybox(ini.getString("SKYBOX_MODEL"), ini.getString("SKYBOX_TEXTURE"));
		}

		House var13 = HouseCreator.create(meshes);
		Respawn var14 = readPoints(ini.getString("START"), var13)[0];
		Respawn var15 = readPoints(ini.getString("FINISH"), var13)[0];
		Respawn[] var16 = readPoints(ini.getString("ENEMIES"), var13);

		int var8 = ini.getInt("ENEMY_COUNT");
		int var11 = ini.getInt("FREQUENCY");
		Scene var9;
		(var9 = new Scene(width, height, var13, var14, var15, var16, var8, var11)).getHouse().setSkybox(skybox);
		return var9;
	}

	private static Respawn[] readPoints(String file, House house) {
		if(file == null) {
			return null;
		} else {
			file = file;
			StringBuffer var2 = new StringBuffer();

			int var3;
			for(var3 = 0; var3 < file.length(); ++var3) {
				char var4;
				if((var4 = file.charAt(var3)) != 32) {
					var2.append(var4);
				}
			}

			String[] var5;
			Respawn[] var6 = new Respawn[(var5 = MeshData.cutOnStrings(var2.toString(), ';')).length];

			for(var3 = 0; var3 < var6.length; ++var3) {
				int[] var7 = MeshData.cutOnInts(var5[var3], ',');
				Vector3D var8 = new Vector3D(var7[0], var7[1], var7[2]);
				var6[var3] = new Respawn(var8, house);
			}

			return var6;
		}
	}
}
