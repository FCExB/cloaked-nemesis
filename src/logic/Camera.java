package logic;

import org.lwjgl.input.Keyboard;

import rosick.jglsdk.glm.Mat4;
import rosick.jglsdk.glm.Vec3;
import rosick.jglsdk.glm.Vec4;
import ui.Util;


public class Camera extends Item {
	
	float xDeg;
	float yDeg;
	float zDeg;
	
	public Camera() {
		super(1f,1f,1f);
		changeDirection(new Vec3(0,0,-1));
		xDeg = 0;
		yDeg = 0;
		zDeg = 0;
	}
	
	public void update(float deltaT) {
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			location.y += deltaT*0.01f;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			location.y -= deltaT*0.01f;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			location.x += deltaT*0.01f;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			location.x -= deltaT*0.01f;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
			location.z += deltaT*0.01f;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_F)) {
			location.z -= deltaT*0.01f;
		}
		
		
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			yDeg += 0.1;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			yDeg -= 0.1;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			xDeg += 0.1;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			xDeg -= 0.1;
		}	
	}
	
	void rotateDirection(float xDegs, float yDegs, float zDegs) {
		
	}
	
	
	@Override
	Mat4 translate() {
		Mat4 theMat = new Mat4(1.0f);	
		theMat.setColumn(3, new Vec4(Vec3.negate(location), 1.0f));
		
		return theMat;	
	}
	
	@Override
	Mat4 rotate(){
		Mat4 rotateZ = Mat4.getRotateZ(-zDeg);
		Mat4 rotateX = Mat4.getRotateX(-xDeg);
		Mat4 rotateY = Mat4.getRotateY(-yDeg);
		
		return rotateZ.mul(rotateX.mul(rotateY));
	}
}
