package logic;

import rosick.jglsdk.glm.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class Car extends Vehicle {
	
	Road currentRoad;
	
	public Car() {
		super();
	}
	
	public Car(Road currentRoad) {
		super();
		changeLocation(currentRoad.getStart());
		this.currentRoad = currentRoad;
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
		} else {
			if (Vec3.roughEqual(location,currentRoad.getTarget(),0.1f)) {
				currentRoad.switchTarget();
			}
			
			changeDirection(Vec3.sub(currentRoad.getTarget(),location));	
			location.add(getNormalisedDirection().scale(0.001f));	
		}
		
		
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			location.x -= deltaT*0.01f;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			location.x += deltaT*0.01f;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			location.y += deltaT*0.01f;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			location.y -= deltaT*0.01f;
		}
		
		
	}
}
