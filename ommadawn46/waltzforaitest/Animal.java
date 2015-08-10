package ommadawn46.waltzforaitest;


public abstract class Animal extends Entity{
	protected float range;
	protected float speed;
	protected double direction;
	protected int age;
	protected Entity target;
	protected Animal friend;
	protected Entity enemy;

	public Animal(WaltzForAITest applet, EntityControl entityControl, float x, float y, float size, int energy) {
		super(applet, entityControl, x, y, size, energy);
		range = this.size * 5;
		speed = 30 / this.size;

		direction = Math.random()*2*Math.PI;
		age = 0;

		target = null;
		friend = null;
		enemy = null;
	}

	public Animal(WaltzForAITest applet, EntityControl entityControl, int energy) {
		this(applet, entityControl, -1, -1, -1, energy);
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
		if(y < size/2){
			direction = Math.random()*Math.PI;
		}else if(entityControl.getWorldWidth() < x + size/2){
			direction = Math.random()*Math.PI+Math.PI/2;
		}else if(entityControl.getWorldHeight() < y + size/2){
			direction = Math.random()*Math.PI+Math.PI;
		}else if(x < size/2){
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
}
