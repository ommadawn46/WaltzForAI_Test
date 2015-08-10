package ommadawn46.waltzforaitest;


public abstract class Entity {
	protected WaltzForAITest applet;
	protected EntityControl entityControl;
	protected float x;
	protected float y;
	protected static final float minSize = 10;
	protected static final float maxSize = 50;
	protected float size;
	protected float r, g, b;
	protected int energy;
	protected boolean alive;

	public Entity(WaltzForAITest applet, EntityControl entityControl, float x, float y, float size, int energy){
		this.applet = applet;
		this.entityControl = entityControl;

		this.size = size;
		if(this.size < minSize || maxSize < this.size){
			this.size = (float)(Math.random()*(maxSize-minSize)+minSize);
		}
		this.x = x;
		if(this.x < this.size/2 || entityControl.getWorldWidth() - this.size/2 < this.x){
			this.x = (float)(Math.random()*(entityControl.getWorldWidth()-this.size))+this.size/2;
		}
		this.y = y;
		if(this.y < this.size/2 || entityControl.getWorldHeight() - this.size/2 < this.y){
			this.y = (float)(Math.random()*(entityControl.getWorldHeight()-this.size))+this.size/2;
		}

		r = (float)(Math.random()*200)+55;
		g = (float)(Math.random()*200)+55;
		b = (float)(Math.random()*200)+55;

		this.energy = energy;

		alive = true;
	}

	public Entity(WaltzForAITest applet, EntityControl entityControl, int energy){
		this(applet, entityControl, -1, -1, -1, energy);
	}

	public boolean isAlive(){
		return alive;
	}
	public void die(){
		alive = false;
	}

	public abstract void update();
	public abstract void draw();

	public float getX(){return x;}
	public float getY(){return y;}
	public float getMaxSize(){return maxSize;}
	public float getSize(){return size;}
	public int getEnergy(){return energy;}
	public void setEnergy(int energy){this.energy = energy;}
	public void subEnergy(int energy){this.energy -= energy;}
}
