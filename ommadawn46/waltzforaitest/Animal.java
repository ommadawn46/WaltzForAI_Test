package ommadawn46.waltzforaitest;

import java.util.List;

public class Animal extends Entity{
	private float range;
	private float speed;
	private double direction;
	private Entity target;

	public Animal(WaltzForAITest applet, EntityControl entityControl, int energy) {
		super(applet, entityControl, energy);
		range = size * 5;
		speed = 30 / size;

		direction = Math.random()*2*Math.PI;
		target = null;
	}

	@Override
	public void update() {
		searchTargetAndCollisionDetection();
		changeDirection();
		move();
		energy--;
		entityControl.plusEnergy(1);
		if(energy <= 0){
			entityControl.dieEntity(this);
		}
	}

	@Override
	public void draw(){
		if(target != null){
			applet.stroke(0);
			applet.line(x, y, target.getX(), target.getY());
			applet.noStroke();
		}

		applet.fill(200, 200, 0, 10);
		applet.ellipse(x, y, range, range);
		applet.fill(r, g, b, 150);
		applet.ellipse(x, y, size, size);
		applet.fill(100, 0, 0, 255);
		applet.ellipse(x, y, 10, 10);
		applet.text(energy, x, y-20);
	}

	private void changeDirection(){
		if(target != null){
			direction = Util.getRadian(x, y, target.getX(), target.getY());
		}else if(y < size/2){
			direction = Math.random()*Math.PI;
		}else if(entityControl.getWorldWidth() < x + size/2){
			direction = Math.random()*Math.PI+Math.PI/2;
		}else if(entityControl.getWorldHeight() < y + size/2){
			direction = Math.random()*Math.PI+Math.PI;
		}else if(x < size/2){
			direction = Math.random()*Math.PI+Math.PI*3/2;
		}else if((int)(Math.random()*100) == 0){
			direction += Math.random()*Math.PI-Math.PI/2;
		}
	}

	private void move(){
		x += Math.cos(direction)*speed;
		y += Math.sin(direction)*speed;
	}

	private void searchTargetAndCollisionDetection(){
		if(target != null && !target.isAlive()){
			target = null;
		}

		float minDistance = range;

		List<Entity> entities = entityControl.getGridWorld().searchEntityInArea(x, y, range);
		for(Entity entity: entities){
			float distance = Util.getDistance(x, y, entity.getX(), entity.getY());
			if(distance < range/2 + entity.getSize()/2){
				if(entity instanceof Plant){
					if(distance < minDistance){
						target = entity;
						minDistance = distance;
					}
				}
				if(distance < size/2 + entity.getSize()/2){
					if(entity.equals(target)){
						eat(entity);
					}
				}
			}
		}
	}

	private void eat(Entity entity){
		energy += entity.getEnergy();
		entity.setEnergy(0);
		entityControl.dieEntity(entity);
	}
}
