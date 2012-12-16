package logic;

import rosick.jglsdk.glm.*;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class Car extends Item {
	
	Road currentRoad;
	boolean onRoad;
	
	public Car() {
		super(0.4f,0.1f,0.1f);
		location.z = -20;
		onRoad = false;
	}
	
	public Car(Road currentRoad) {
		super(0.4f,0.1f,0.1f);
		location.z = -20;
		changeLocation(currentRoad.getStart());
		this.currentRoad = currentRoad;
		
		onRoad = true;
	}
	
	public void update(float deltaT) {	
		
		if (Mouse.isButtonDown(0)) {
			float x = (float)Mouse.getX()/500f - 0.5f;
			float y = (float)Mouse.getY()/500f - 0.5f;
			x *= 10;
			y *= 10;
			
			Vec3 aim = new Vec3(x,y,location.z);
			
			changeDirection(Vec3.sub(aim,location));	
			
			location.add(getNormalisedDirection().scale(0.001f));
		} else if (onRoad) {
			if (Vec3.roughEqual(location,currentRoad.getTarget(),0.0001f)) {
				currentRoad.switchTarget();
			}
			
			changeDirection(Vec3.sub(currentRoad.getTarget(),location));	
			location.add(getNormalisedDirection().scale(0.001f));	
		}
	}
}
