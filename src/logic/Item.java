package logic;

import rosick.jglsdk.glm.*;
import ui.*;

public abstract class Item {
	final Vec3 location;   //Centre
	final Vec3 direction;
	final Vec3 up;
	final Vec3 dimensions; //x, y, z
	
	public Item(float x, float y, float z) {
		
		location = new Vec3(0f,0f,0f);
		
		direction = new Vec3(0f);
		direction.x = 1f;
		direction.y = 1f;
		
		up = new Vec3(0,0,1);
		
		dimensions = new Vec3(x,y,z);
	}
	
	public Vec3 getNormalisedDirection() {
		return Vec3.scale(direction, 1/direction.getMagnitude());
	}
	
	public Mat4 constructMatrix() {	
		
		return translate().mul(rotate().mul(scale()));
	}
	
	Mat4 translate() {
		Mat4 theMat = new Mat4(1.0f);	
		theMat.setColumn(3, new Vec4(location, 1.0f));
		
		return theMat;	
	}
	
	Mat4 rotate(){
		float zDeg = Util.radToDeg(Math.atan(direction.y/direction.x));
		float xDeg = Util.radToDeg(Math.atan(direction.y/direction.z));
	    float yDeg = Util.radToDeg(Math.atan(direction.z/direction.x));
		
		if(direction.x == 0f) {
			zDeg = 0;
			yDeg = 0;
		}
		if(direction.z == 0f) {
			xDeg = 0;
		}
	    
		Mat4 rotateZ = Mat4.getRotateZ(zDeg);
		Mat4 rotateX = Mat4.getRotateX(xDeg);
		Mat4 rotateY = Mat4.getRotateY(yDeg);
		
		return rotateZ.mul(rotateX.mul(rotateY));
	}
	
	Mat4 scale(){
		Mat4 theMat = new Mat4(1.0f);
		theMat.set(0, 0, dimensions.x);
		theMat.set(1, 1, dimensions.y);
		theMat.set(2, 2, dimensions.z);
		
		return theMat;
	}
	
	public abstract void update(float deltaT);

	protected void changeDirection(Vec3 next) {
		direction.x = next.x;
		direction.y = next.y;
		direction.z = next.z;	
	}
	
	protected void changeLocation(Vec3 next) {
		location.x = next.x;
		location.y = next.y;
		location.z = next.z;	
	}
	
}
