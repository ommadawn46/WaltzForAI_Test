package ommadawn46.ichalletest;

import java.util.ArrayList;
import java.util.List;

public class Animal extends Entity{
	private float range;
	private float speed;
	private Arrow direction;
	private Entity target;
	private int eaten;

	public Animal(WaltzForAITest applet) {
		super(applet);
		range = size * 5;
		speed = 30 / size;

		switch((int)(Math.random()*4)){
		case 0: direction = Arrow.UP; break;
		case 1: direction = Arrow.DOWN; break;
		case 2: direction = Arrow.RIGHT; break;
		case 3: direction = Arrow.LEFT; break;
		default: break;
		}
		target = null;
		eaten = 0;
	}

	@Override
	public void update() {
		searchTarget();
		searchHitEntity();
		changeDirection();
		move();
		draw();
	}

	private void draw(){
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
		applet.text(eaten, x, y-20);
	}

	private void changeDirection(){
		if(target != null){
			if(Math.abs(x-target.getX()) > Math.abs(y-target.getY())){
				if(x < target.getX()){
					direction = Arrow.RIGHT;
				}else{
					direction = Arrow.LEFT;
				}
			}else{
				if(y < target.getY()){
					direction = Arrow.DOWN;
				}else{
					direction = Arrow.UP;
				}
			}
		}else if(y < size/2){
			direction = Arrow.DOWN;
		}else if(x < size/2){
			direction = Arrow.RIGHT;
		}else if(applet.displayHeight < y + size/2){
			direction = Arrow.UP;
		}else if(applet.displayWidth < x + size/2){
			direction = Arrow.LEFT;
		}else if((int)(Math.random()*300) == 0){
			switch(direction){
			case UP: direction = (int)(Math.random()*2)==0 ? Arrow.RIGHT : Arrow.LEFT; break;
			case DOWN: direction = (int)(Math.random()*2)==0 ? Arrow.RIGHT : Arrow.LEFT; break;
			case LEFT: direction = (int)(Math.random()*2)==0 ? Arrow.UP : Arrow.DOWN; break;
			case RIGHT: direction = (int)(Math.random()*2)==0 ? Arrow.UP : Arrow.DOWN; break;
			default: break;
			}
		}
	}

	private void move(){
		switch (direction) {
		case UP: y -= speed; break;
		case DOWN: y += speed; break;
		case RIGHT: x += speed; break;
		case LEFT: x -= speed; break;
		default: break;
		}
	}

	private void searchTarget(){
		if(target != null && !target.isAlive()){
			target = null;
		}

		float minDistance = range;

		List<Entity> entities = searchInRangeEntity(range);
		for(Entity entity: entities){
			if(entity instanceof Plant){
				float distance = (float)Math.sqrt(Math.pow(x-entity.getX(), 2) + Math.pow(y-entity.getY(), 2));
				if(distance < minDistance){
					target = entity;
					minDistance = distance;
				}
			}
		}
	}

	private void searchHitEntity(){
		List<Entity> entities = searchInRangeEntity(size);
		for(Entity entity: entities){
			if(entity.equals(target)){
				eat(entity);
			}
		}
	}

	private List<Entity> searchInRangeEntity(float range){
		List<Entity> entities = new ArrayList<Entity>();

		List<Entity> xList = applet.getXList();
		int xpos = xList.indexOf(this);
		int cxpos = xpos - 1;
		while(0 <= cxpos){
			Entity entity = xList.get(cxpos--);
			if(x-entity.getX() < range/2+entity.getSize()/2){
				if(withinRange(entity, range) && !entities.contains(entity)){
					entities.add(entity);
				}
			}else{
				break;
			}
		}
		cxpos = xpos + 1;
		while(cxpos < xList.size()){
			Entity entity = xList.get(cxpos++);
			if(entity.getX()-x < range/2+entity.getSize()/2){
				if(withinRange(entity, range) && !entities.contains(entity)){
					entities.add(entity);
				}
			}else{
				break;
			}
		}

		List<Entity> yList = applet.getYList();
		int ypos = yList.indexOf(this);
		int cypos = ypos - 1;
		while(0 <= cypos){
			Entity entity = yList.get(cypos--);
			if(y-entity.getY() < range/2+entity.getSize()/2){
				if(withinRange(entity, range) && !entities.contains(entity)){
					entities.add(entity);
			}
			}else{
				break;
			}
		}
		cypos = ypos + 1;
		while(cypos < yList.size()){
			Entity entity = yList.get(cypos++);
			if(entity.getY()-y < range/2+entity.getSize()/2){
				if(withinRange(entity, range) && !entities.contains(entity)){
					entities.add(entity);
				}
			}else{
				break;
			}
		}

		return entities;
	}

	private boolean withinRange(Entity entity, float range){
		if(Math.sqrt(Math.pow(x-entity.getX(), 2) + Math.pow(y-entity.getY(), 2)) < range/2 + entity.getSize()/2){
			return true;
		}
		return false;
	}

	private void eat(Entity entity){
		eaten++;
		applet.dieEntity(entity);
	}
}

enum Arrow{
	UP, DOWN, RIGHT, LEFT
}
