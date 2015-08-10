package ommadawn46.waltzforaitest;

public class Util {
	private Util(){	}

	public static double getDistance(float x1, float y1, float x2, float y2){
		float xDist = Math.abs(x2 - x1);
		float yDist = Math.abs(y2 - y1);
		return Math.sqrt(xDist*xDist + yDist*yDist);
	}

	public static double getRadian(float x1, float y1, float x2, float y2) {
		return Math.atan2(y2 - y1, x2 - x1);
	}

	public static double getRadianSub(double deg1, double deg2){
		double digreeSub = deg2 - deg1;
		digreeSub -= (int)(digreeSub/(Math.PI*2))*Math.PI*2;
		return digreeSub > Math.PI ? digreeSub - Math.PI*2 : digreeSub;
	}
}
