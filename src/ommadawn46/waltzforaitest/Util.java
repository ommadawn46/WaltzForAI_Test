package ommadawn46.waltzforaitest;

import ommadawn46.waltzforaitest.entity.Animal;
import ommadawn46.waltzforaitest.entity.Entity;

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
	
	public static boolean inFieldOfView(Animal subject, Entity object){
		float sx = subject.getX(), sy = subject.getY();
		float ox = object.getX(), oy = object.getY(), os = object.getSize();
		double distance = getDistance(sx, sy, ox, oy);
		if(distance < os){
			return true;
		}
		
		float sr = subject.getRange();
		double direction = subject.getDirection(), fov = subject.getFOV();
		if(distance < sr+os && Math.abs(getRadianSub(direction, getRadian(sx, sy, ox, oy))) < fov){
			return true;
		}
		/*
		float dx = ox - sx, dy = oy - sy;
		
		double vx = (float)Math.cos(direction-fov)*sr;
		double vy = (float)Math.sin(direction-fov)*sr;
		double a = vx * vx + vy * vy;
		double b = -(dx*vx + dy*vy);
		double c = dx *  + dy * dy - sr * sr;
		double d = b * b - a * c;
		if(d >= 0){
			double t = (-b - Math.sqrt(d)) / a;
			if(t <= 0 && t <= 1){
				return true;
			}
		}
		
		vx = (float)Math.cos(direction+fov)*sr;
		vy = (float)Math.sin(direction+fov)*sr;
		a = vx * vx + vy * vy;
		b = -(dx*vx + dy*vy);
		c = dx * dx + dy * dy - sr * sr;
		d = b * b - a * c;
		if(d >= 0){
			double t = (-b - Math.sqrt(d)) / a;
			if(t <= 0 && t <= 1){
				return true;
			}
		}*/
		
		return false;
	}
}
