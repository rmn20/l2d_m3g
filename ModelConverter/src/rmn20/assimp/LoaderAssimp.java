package rmn20.assimp;

import java.nio.IntBuffer;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIBone;
import org.lwjgl.assimp.AIColor4D;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AINode;
import org.lwjgl.assimp.AIPropertyStore;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;

import static org.lwjgl.assimp.Assimp.*;

/**
 *
 * @author Roman
 */
public class LoaderAssimp {
	
	public static AIModelData loadModel(String path, boolean dropNormals) {
		AIPropertyStore propStore = aiCreatePropertyStore();
		aiSetImportPropertyFloat(propStore, AI_CONFIG_PP_GSN_MAX_SMOOTHING_ANGLE, 45);
		
		int loadFlags = aiProcess_JoinIdenticalVertices |
			aiProcess_ValidateDataStructure |
			 aiProcess_ImproveCacheLocality |
			aiProcess_FindInvalidData |
			aiProcess_GenUVCoords |
			aiProcess_TransformUVCoords |
			aiProcess_FlipUVs |
			aiProcess_PopulateArmatureData |
			//aiProcess_Triangulate |
			//aiProcess_SortByPType |
			0
			;
					
		if(dropNormals) {
			loadFlags |= aiProcess_DropNormals;
		} else {
			loadFlags |= aiProcess_GenSmoothNormals;
		}
		
		AIScene scene = aiImportFileExWithProperties(
			path, 
			loadFlags,
			null,
			propStore
		);

		AIModelData model = new AIModelData();
		processAINode(model, scene, scene.mRootNode());
		
		System.out.println("Animations count: " + scene.mNumAnimations());

		aiReleasePropertyStore(propStore);
		
		return model;
	}

	private static void processAINode(AIModelData outModel, AIScene scene, AINode node) {
		//Process childs
		PointerBuffer children = node.mChildren();
		int numChildren = node.mNumChildren();
		
		for(int i = 0; i < numChildren; i++) {
			processAINode(outModel, scene, AINode.create(children.get(i)));
		}
		
		//Process meshes
		AIMeshData mesh = new AIMeshData(node.mName().dataString());
		
		PointerBuffer sceneMeshes = scene.mMeshes();
		IntBuffer meshes = node.mMeshes();
		int numMeshes = node.mNumMeshes();
		
		for(int i = 0; i < numMeshes; i++) {
			AISubMeshData submesh = createSubMesh(mesh, AIMesh.create(sceneMeshes.get(meshes.get(i))));
			if(submesh != null) mesh.addSubMesh(submesh);
		}
		
		if(!mesh.submeshes.isEmpty()) outModel.addMesh(mesh);
	}

	private static AISubMeshData createSubMesh(AIMeshData mdlMesh, AIMesh aiMesh) {
		if((aiMesh.mPrimitiveTypes() & (aiPrimitiveType_TRIANGLE | aiPrimitiveType_POLYGON)) == 0) return null;
		
		//Create submesh with all vertices
		AIVector3D.Buffer verts = aiMesh.mVertices();
		AIVector3D.Buffer norms = aiMesh.mNormals();
		AIVector3D.Buffer uvs = aiMesh.mTextureCoords(0);
		AIColor4D.Buffer cols = aiMesh.mColors(0);
		
		AISubMeshData asMesh = new AISubMeshData(
			aiMesh.mMaterialIndex(),
			verts,
			norms,
			uvs,
			cols,
			aiMesh.mNumVertices()
		);
		
		//Add bones
		PointerBuffer bones = aiMesh.mBones();
		int numBones = aiMesh.mNumBones();
		
		for(int i = 0; i < numBones; i++) {
			AIBone bone = AIBone.create(bones.get(i));
			int boneId = mdlMesh.getBoneId(bone);
			
			asMesh.addBone(bone, boneId);
		}
		
		//Add faces
		AIFace.Buffer faces = aiMesh.mFaces();
		int numFaces = aiMesh.mNumFaces();
		
		for(int i = 0; i < numFaces; i++) {
			AIFace face = faces.get(i);
			
			asMesh.addFace(
				mdlMesh, 
				face
			);
		}
		
		if(asMesh.quads.isEmpty() && asMesh.tris.isEmpty()) return null;
		return asMesh;
	}
}
