package rmn20;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import rmn20.assimp.AIModelData;
import rmn20.assimp.LoaderAssimp;
import rmn20.m3d2.Exporter3D2;
import rmn20.m3d2.Model;

/**
 *
 * @author Roman
 */
public class Main {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		boolean dropNormals = false;
			
		for(int i = 0; i < args.length; i++) {
			String arg = args[i];
			
			//todo add flags to change pos and uv precision
			//todo add flag to change max bone count 
			if(arg.startsWith("-")) {
				if(arg.equals("-dropnormals")) dropNormals = true;
			}
		}
			
		for(int i = 0; i < args.length; i++) {
			String arg = args[i];
			
			if(arg.startsWith("-")) {
				continue;
			}
			
			AIModelData inModel = LoaderAssimp.loadModel(arg, dropNormals);
			Model outModel = new Model(inModel);

			String outFilename = arg;

			int dotIdx = outFilename.lastIndexOf('.');
			if(dotIdx != -1) outFilename = outFilename.substring(0, dotIdx);
			outFilename += ".3d2";

			File file = new File(outFilename);

			try {
				if(!file.exists()) file.createNewFile();
				DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));

				Exporter3D2.exportModel(dos, outModel);

				dos.close();
			} catch(IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
