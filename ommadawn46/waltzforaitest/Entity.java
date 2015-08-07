package ommadawn46.waltzforaitest;


public abstract class Entity {
	protected WaltzForAITest applet;
	protected float x;
	protected float y;
	protected static final float minSize = 10;
	protected static final float maxSize = 50;
	protected float size;
	protected float r, g, b;
	protected boolean alive;

	public Entity(WaltzForAITest applet){
		this.applet = applet;

		size = (float)(Math.random()*(maxSize-minSize)+minSize);
		
		x = (float)(Math.random()*(applet.getWorldWidth()-size))+size/2;
		y = (float)(Math.random()*(applet.getWorldHeight()-size))+size/2;

		r = (float)(Math.random()*200)+55;
		g = (float)(Math.random()*200)+55;
		b = (float)(Math.random()*200)+55;

		alive = true;
	}

	public boolean isAlive(){
		return alive;
	}
	public void die(){
		alive = false;
	}

	public abstract void update(boolean isDrawn);

	public float getX(){return x;};
	public float getY(){return y;};
	public float getMaxSize(){return maxSize;};
	public float getSize(){return size;};
}
