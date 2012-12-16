package logic;

import rosick.jglsdk.glm.*;

public class Road {
	private float length;
	private final Vec3 start;
	private final Vec3 end;
	private boolean toEnd;
	
	public Road(Vec3 start, Vec3 end) {
		this.toEnd = true;
		this.start = start;
		this.end = end;
	}
	
	public Vec3 getStart() {
		return start;
	}
	
	public Vec3 getTarget() {
		if (toEnd){
			return end;
		}
		
		return start;
	}
	
	public void switchTarget() {
		if(toEnd) {
			toEnd = false;
		}
		toEnd = true;
	}
}
