package ommadawn46.ichalletest;


public abstract class Entity {
	protected WaltzForAITest applet;
	protected float x;
	protected float y;
	protected float size;
	protected float r, g, b;
	protected boolean alive;

	public Entity(WaltzForAITest applet){
		this.applet = applet;

		size = (float)(Math.random()*40+10);
		x = (float)(Math.random()*(applet.displayWidth-size));
		y = (float)(Math.random()*(applet.displayHeight-size));

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

	public abstract void update();

	public float getX(){return x;};
	public float getY(){return y;};
	public float getSize(){return size;};
}
