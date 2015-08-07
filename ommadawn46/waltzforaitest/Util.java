package ommadawn46.waltzforaitest;

public class Util {
	private Util(){	}

	public static float getDistance(float x1, float y1, float x2, float y2){
		float xDist = Math.abs(x2 - x1);
		float yDist = Math.abs(y2 - y1);
		return (float)Math.sqrt(xDist*xDist + yDist*yDist);
	}
	
	public static double getRadian(float x1, float y1, float x2, float y2) {
		return Math.atan2(y2 - y1, x2 - x1);
	}
}
