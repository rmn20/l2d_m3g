package rmn20.m3d2;

import rmn20.assimp.AIBoneData;

/**
 *
 * @author Roman
 */
public class Bone {
	int parent;
	float[] offsetMatrix;
	float[] invOffsetMatrix;
	
	float[] matrix;
	
	public Bone(AIBoneData bone) {
		parent = bone.parent;
		offsetMatrix = bone.offsetMatrix;
	}
	
	public void calcMatrices(Bone parent) {
		if(offsetMatrix == null) {
			if(parent != null) offsetMatrix = parent.offsetMatrix;
			else {
				offsetMatrix = new float[] {
					1, 0, 0, 0,
					0, 1, 0, 0,
					0, 0, 1, 0,
					0, 0, 0, 1
				};
			}
		}
		
		invOffsetMatrix = new float[16];
		invertMatrix(offsetMatrix, invOffsetMatrix);
		
		if(parent == null) {
			matrix = invOffsetMatrix;
		} else {
			matrix = new float[16];
			mulMatrix(parent.offsetMatrix, invOffsetMatrix, matrix);
		}
	}
	
	private static boolean invertMatrix(float[] mat, float[] result) {
		float[] tmp1 = new float[12];
		float[] tmp2 = new float[16];
		float[] tmp3 = new float[16];

		for (int i = 0; i < 4; ++i) {
			tmp2[i + 0] = mat[i * 4 + 0];
			tmp2[i + 4] = mat[i * 4 + 1];
			tmp2[i + 8] = mat[i * 4 + 2];
			tmp2[i + 12] = mat[i * 4 + 3];
		}

		tmp1[0] = tmp2[10] * tmp2[15];
		tmp1[1] = tmp2[11] * tmp2[14];
		tmp1[2] = tmp2[9] * tmp2[15];
		tmp1[3] = tmp2[11] * tmp2[13];
		tmp1[4] = tmp2[9] * tmp2[14];
		tmp1[5] = tmp2[10] * tmp2[13];
		tmp1[6] = tmp2[8] * tmp2[15];
		tmp1[7] = tmp2[11] * tmp2[12];
		tmp1[8] = tmp2[8] * tmp2[14];
		tmp1[9] = tmp2[10] * tmp2[12];
		tmp1[10] = tmp2[8] * tmp2[13];
		tmp1[11] = tmp2[9] * tmp2[12];
		
		tmp3[0] = tmp1[0] * tmp2[5] + tmp1[3] * tmp2[6] + tmp1[4] * tmp2[7];
		tmp3[0] -= tmp1[1] * tmp2[5] + tmp1[2] * tmp2[6] + tmp1[5] * tmp2[7];
		tmp3[1] = tmp1[1] * tmp2[4] + tmp1[6] * tmp2[6] + tmp1[9] * tmp2[7];
		tmp3[1] -= tmp1[0] * tmp2[4] + tmp1[7] * tmp2[6] + tmp1[8] * tmp2[7];
		tmp3[2] = tmp1[2] * tmp2[4] + tmp1[7] * tmp2[5] + tmp1[10] * tmp2[7];
		tmp3[2] -= tmp1[3] * tmp2[4] + tmp1[6] * tmp2[5] + tmp1[11] * tmp2[7];
		tmp3[3] = tmp1[5] * tmp2[4] + tmp1[8] * tmp2[5] + tmp1[11] * tmp2[6];
		tmp3[3] -= tmp1[4] * tmp2[4] + tmp1[9] * tmp2[5] + tmp1[10] * tmp2[6];
		tmp3[4] = tmp1[1] * tmp2[1] + tmp1[2] * tmp2[2] + tmp1[5] * tmp2[3];
		tmp3[4] -= tmp1[0] * tmp2[1] + tmp1[3] * tmp2[2] + tmp1[4] * tmp2[3];
		tmp3[5] = tmp1[0] * tmp2[0] + tmp1[7] * tmp2[2] + tmp1[8] * tmp2[3];
		tmp3[5] -= tmp1[1] * tmp2[0] + tmp1[6] * tmp2[2] + tmp1[9] * tmp2[3];
		tmp3[6] = tmp1[3] * tmp2[0] + tmp1[6] * tmp2[1] + tmp1[11] * tmp2[3];
		tmp3[6] -= tmp1[2] * tmp2[0] + tmp1[7] * tmp2[1] + tmp1[10] * tmp2[3];
		tmp3[7] = tmp1[4] * tmp2[0] + tmp1[9] * tmp2[1] + tmp1[10] * tmp2[2];
		tmp3[7] -= tmp1[5] * tmp2[0] + tmp1[8] * tmp2[1] + tmp1[11] * tmp2[2];
		
		tmp1[0] = tmp2[2] * tmp2[7];
		tmp1[1] = tmp2[3] * tmp2[6];
		tmp1[2] = tmp2[1] * tmp2[7];
		tmp1[3] = tmp2[3] * tmp2[5];
		tmp1[4] = tmp2[1] * tmp2[6];
		tmp1[5] = tmp2[2] * tmp2[5];
		tmp1[6] = tmp2[0] * tmp2[7];
		tmp1[7] = tmp2[3] * tmp2[4];
		tmp1[8] = tmp2[0] * tmp2[6];
		tmp1[9] = tmp2[2] * tmp2[4];
		tmp1[10] = tmp2[0] * tmp2[5];
		tmp1[11] = tmp2[1] * tmp2[4];
		
		tmp3[8] = tmp1[0] * tmp2[13] + tmp1[3] * tmp2[14] + tmp1[4] * tmp2[15];
		tmp3[8] -= tmp1[1] * tmp2[13] + tmp1[2] * tmp2[14] + tmp1[5] * tmp2[15];
		tmp3[9] = tmp1[1] * tmp2[12] + tmp1[6] * tmp2[14] + tmp1[9] * tmp2[15];
		tmp3[9] -= tmp1[0] * tmp2[12] + tmp1[7] * tmp2[14] + tmp1[8] * tmp2[15];
		tmp3[10] = tmp1[2] * tmp2[12] + tmp1[7] * tmp2[13] + tmp1[10] * tmp2[15];
		tmp3[10] -= tmp1[3] * tmp2[12] + tmp1[6] * tmp2[13] + tmp1[11] * tmp2[15];
		tmp3[11] = tmp1[5] * tmp2[12] + tmp1[8] * tmp2[13] + tmp1[11] * tmp2[14];
		tmp3[11] -= tmp1[4] * tmp2[12] + tmp1[9] * tmp2[13] + tmp1[10] * tmp2[14];
		tmp3[12] = tmp1[2] * tmp2[10] + tmp1[5] * tmp2[11] + tmp1[1] * tmp2[9];
		tmp3[12] -= tmp1[4] * tmp2[11] + tmp1[0] * tmp2[9] + tmp1[3] * tmp2[10];
		tmp3[13] = tmp1[8] * tmp2[11] + tmp1[0] * tmp2[8] + tmp1[7] * tmp2[10];
		tmp3[13] -= tmp1[6] * tmp2[10] + tmp1[9] * tmp2[11] + tmp1[1] * tmp2[8];
		tmp3[14] = tmp1[6] * tmp2[9] + tmp1[11] * tmp2[11] + tmp1[3] * tmp2[8];
		tmp3[14] -= tmp1[10] * tmp2[11] + tmp1[2] * tmp2[8] + tmp1[7] * tmp2[9];
		tmp3[15] = tmp1[10] * tmp2[10] + tmp1[4] * tmp2[8] + tmp1[9] * tmp2[9];
		tmp3[15] -= tmp1[8] * tmp2[9] + tmp1[11] * tmp2[10] + tmp1[5] * tmp2[8];
		
		float tmpV = tmp2[0] * tmp3[0] + tmp2[1] * tmp3[1] + tmp2[2] * tmp3[2] + tmp2[3] * tmp3[3];
		tmpV = 1.0F / tmpV;

		for (int i = 0; i < 16; ++i) {
			result[i] = tmp3[i] * tmpV;
		}

		return true;
	}
	
	private static void mulMatrix(float[] mat1, float[] mat2, float[] result) {
		float[] res = new float[16];

		for(int x = 0; x < 4; ++x) {
			for(int y = 0; y < 4; ++y) {
				int yOffset = y << 2;
				
				res[yOffset + x] += mat2[0 + x] * mat1[yOffset + 0];
				res[yOffset + x] += mat2[4 + x] * mat1[yOffset + 1];
				res[yOffset + x] += mat2[8 + x] * mat1[yOffset + 2];
				res[yOffset + x] += mat2[12 + x] * mat1[yOffset + 3];
			}
		}

		System.arraycopy(res, 0, result, 0, 16);
	}
}
