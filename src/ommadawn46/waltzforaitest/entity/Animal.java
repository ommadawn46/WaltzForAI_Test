package ommadawn46.waltzforaitest.entity;

import ommadawn46.waltzforaitest.EntityControl;
import ommadawn46.waltzforaitest.Util;
import ommadawn46.waltzforaitest.WaltzForAITest;


public abstract class Animal extends Entity{
	protected float range; // 視野の半径
	protected double fov; // 視野の角度
	protected float speed;
	protected double direction;
	protected int age;
	protected Animal[] parents;
	protected Entity target;
	protected Animal friend;
	protected Entity enemy;

	public Animal(WaltzForAITest applet, EntityControl entityControl, 
			float x, float y, float size, int energy, Animal[] parents) {
		super(applet, entityControl, x, y, size, energy);
		fov = Math.PI;
		range = this.size * 2.5f;
		speed = 30 / this.size;

		direction = Math.random()*2*Math.PI;
		age = 0;

		this.parents = parents;
		target = null;
		friend = null;
		enemy = null;
	}

	public Animal(WaltzForAITest applet, EntityControl entityControl, int energy) {
		this(applet, entityControl, -1, -1, -1, energy, new Animal[]{null, null});
	}

	@Override
	public void update() {
		searchEntityAndCollisionDetection();
		changeDirection();
		move();
		entityControl.energyDrop(this, 1);
		if(energy <= 0){
			entityControl.dieEntity(this);
		}
		age++;
	}

	private void move(){
		x += Math.cos(direction)*speed;
		y += Math.sin(direction)*speed;
	}

	private void changeDirection(){
		if(y < size){
			direction = Math.random()*Math.PI;
		}else if(entityControl.getWorldWidth() < x + size){
			direction = Math.random()*Math.PI+Math.PI/2;
		}else if(entityControl.getWorldHeight() < y + size){
			direction = Math.random()*Math.PI+Math.PI;
		}else if(x < size){
			direction = Math.random()*Math.PI+Math.PI*3/2;
		}else if(enemy != null){
			direction = Util.getRadian(enemy.getX(), enemy.getY(), x, y);
		}else if(friend != null){
			direction = Util.getRadian(x, y, friend.getX(), friend.getY());
		}else if(target != null){
			direction = Util.getRadian(x, y, target.getX(), target.getY());
		}else if((int)(Math.random()*100) == 0){
			direction += Math.random()*Math.PI-Math.PI/2;
		}
	}

	protected abstract void searchEntityAndCollisionDetection();

	public boolean canCross(){
		if(age > 3600 && energy > 5000){
			return true;
		}
		return false;
	}

	public void resetAge(){
		age = 0;
	}

	protected void eat(Entity entity){
		energy += entity.getEnergy();
		entity.setEnergy(0);
		entityControl.dieEntity(entity);
	}
	
	public Animal[] getParents(){return parents;}
	public double getDirection(){return direction;}
	public float getRange(){return range;}
	public double getFOV(){return fov;}
}
