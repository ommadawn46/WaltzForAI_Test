package ommadawn46.waltzforaitest;


public abstract class Entity {
	protected WaltzForAITest applet;
	protected float x;
	protected float y;
	protected static final float minSize = 10;
	protected static final float maxSize = 50;
	protected float size;
	protected float r, g, b;
	protected int energy;
	protected boolean alive;

	public Entity(WaltzForAITest applet, int energy){
		this.applet = applet;

		size = (float)(Math.random()*(maxSize-minSize)+minSize);
		
		x = (float)(Math.random()*(applet.getWorldWidth()-size))+size/2;
		y = (float)(Math.random()*(applet.getWorldHeight()-size))+size/2;

		r = (float)(Math.random()*200)+55;
		g = (float)(Math.random()*200)+55;
		b = (float)(Math.random()*200)+55;

		this.energy = energy;
		
		alive = true;
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
}
