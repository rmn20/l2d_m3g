package com;

import javax.microedition.lcdui.Graphics;
import javax.microedition.m3g.Background;
import javax.microedition.m3g.Camera;
import javax.microedition.m3g.Graphics3D;
import javax.microedition.m3g.Node;
import javax.microedition.m3g.Transform;

public final class Renderer {
	
	private final Graphics3D g3d = Graphics3D.getInstance();
	private final Background bck = new Background();
	
	public final Vector3D camPos = new Vector3D();
	public final Vector3D camRot = new Vector3D();
	private final Camera cam = new Camera();
	private final Transform camPers = new Transform();
	private final float[] camPersTmp = new float[16], camPersTmp2 = new float[16];
	private final Transform camTrans = new Transform();
	private final Transform invCam = new Transform();
	
	private final Transform tmpTrans = new Transform();
	
	private int renderX, renderY;
	public int width, height;
	public float viewportPhysW, viewportPhysH;
	public float projXscale, projYscale;
	public float nearPlane;
	
	//public float lightX = 475, lightY = 1500, lightZ = 7000;

	public Renderer(int width, int height) {
		this.width = width;
		this.height = height;
		
		float fovy = 73.5f;
		nearPlane = 10;
		
		setPerspective(camPersTmp, fovy, (float) width / height, nearPlane, 300000);
		System.arraycopy(camPersTmp, 0, camPersTmp2, 0, 16);
		camPers.set(camPersTmp);
		cam.setGeneric(camPers);
		
		bck.setColorClearEnable(false);
		
		viewportPhysH = (float)(Math.tan(Math.toRadians(fovy / 2.0f)) * nearPlane) * 2f;
		viewportPhysW = viewportPhysH * width / height;
		
		projXscale = width / viewportPhysW;
		projYscale = height / viewportPhysH;
		
		//Hashtable params = g3d.getProperties();
		//System.out.println("maxLights: " + params.get("maxLights"));
	}

	public final void destroy() {
		//??? useless
	}

	public final int getWidth() {
		return this.width;
	}

	public final int getHeight() {
		return this.height;
	}
	
	private void setPerspective(float[] mat, float fovy, float aspect, float near, float far) {
		float tmp1 = (float) Math.tan(Math.toRadians(fovy / 2.0f));
		float tmp2 = far - near;

		mat[0] = 1.0f / (aspect * tmp1);
		mat[5] = 1.0f / tmp1;
		mat[10] = -(near + far) / tmp2;
		mat[11] = -2.0f * near * far / tmp2;
		mat[14] = -1.0f;
	}

	public final void setCamera(Vector3D pos, Vector3D rot) {
		camPos.set(pos);
		camRot.set(rot);
		
		camTrans.setIdentity();
		camTrans.postTranslate(pos.x, pos.y, pos.z);
		camTrans.postRotate(rot.y * 360f / (1 << 14), 0, 1, 0);
		camTrans.postRotate(rot.x * 360f / (1 << 14), 1, 0, 0);
		camTrans.postRotate(rot.z * 360f / (1 << 14), 0, 0, 1);
		
		//cam.getCompositeTransform(camTrans);
		//cam.getCompositeTransform(invCam);
		invCam.set(camTrans);
		invCam.invert();
	}
	
	public final Transform getInvCam() {
		return invCam;
	}
	
	public final void setClip(int x1, int y1, int x2, int y2) {
		try {
			int w = width;
			int h = height;

			float[] mat = camPersTmp2;
			float[] matBck = camPersTmp;

			mat[0] = matBck[0] * w / (x2 - x1);
			//mat[2] = (x1 - (w - (x2 - x1)) / 2) * 2 / (x2 - x1);
			mat[2] = (float)(x1 + x2 - w) / (x2 - x1);

			mat[5] = matBck[5] * h / (y2 - y1);
			mat[6] = (float)-(y1 + y2 - h) / (y2 - y1);

			camPers.set(mat);
			cam.setGeneric(camPers);
			g3d.setCamera(cam, camTrans);
			
			g3d.setViewport(x1 + renderX, y1 + renderY, x2 - x1, y2 - y1);
		} catch (Exception e) {
			System.out.println(x1 + " " + y1 + " " + x2 + " " + y2);
			e.printStackTrace();
		}
	}

	public final void addSprite(Sprite obj) {
		Transform mat = tmpTrans;
		mat.setIdentity();
		
		Vector3D pos = obj.getPosition();
		mat.postTranslate(pos.x, pos.y, pos.z);
		mat.postScale(
				(obj.mirX ? -1 : 1) * obj.getWidth(), 
				(obj.mirY ? -1 : 1) * obj.getHeight(), 
				(obj.mirX ? -1 : 1) * obj.getWidth()
		);
		
		g3d.render(obj.s3d, mat);
	}

	public final void addMesh(Node node, Vector3D pos, Vector3D rot) {
		if(node == null) return; //todo WHY
		
		if(pos == null && rot == null) {
			g3d.render(node, null);
			return;
		}
		
		Transform mat = tmpTrans;
		
		mat.setIdentity();
		if(pos != null) mat.postTranslate(pos.x, pos.y, pos.z);
		if(rot != null) {
			mat.postRotate(rot.y * 360f / (1 << 14), 0, 1, 0);
			mat.postRotate(rot.x * 360f / (1 << 14), 1, 0, 0);
			mat.postRotate(rot.z * 360f / (1 << 14), 0, 0, 1);
		}
		
		g3d.render(node, mat);
	}

	public final void prepareRender(Graphics g, int x, int y) {
		this.renderX = x;
		this.renderY = y;
		g3d.bindTarget(g, true, Graphics3D.OVERWRITE);
		g3d.setViewport(x, y, width, height);
		g3d.clear(bck);
		
		/*Light light = new Light();
		light.setMode(Light.OMNI);
		light.setColor(0xffffff);
		light.setAttenuation(0, 0.0001f, 0);
		
		Transform tmpMat = new Transform();
		tmpMat.postTranslate(lightX, lightY, lightZ);
		
		g3d.resetLights();
		g3d.addLight(light, tmpMat);*/
	}

	public final void flush(Graphics g) {
		g3d.releaseTarget();
	}
}
