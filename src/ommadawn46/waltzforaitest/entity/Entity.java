package ommadawn46.waltzforaitest.entity;

import ommadawn46.waltzforaitest.EntityControl;
import ommadawn46.waltzforaitest.WaltzForAITest;


public abstract class Entity {
	protected WaltzForAITest applet;
	protected EntityControl entityControl;
	protected float x;
	protected float y;
	public static final float minSize = 10;
	public static final float maxSize = 50;
	protected float size; // 本体の半径
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
		if(this.x < maxSize || entityControl.getWorldWidth() - maxSize < this.x){
			this.x = (float)(Math.random()*(entityControl.getWorldWidth()-maxSize*2))+maxSize;
		}
		this.y = y;
		if(this.y < maxSize || entityControl.getWorldHeight() - maxSize < this.y){
			this.y = (float)(Math.random()*(entityControl.getWorldHeight()-maxSize*2))+maxSize;
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
	public float getSize(){return size;}
	public int getEnergy(){return energy;}
	public void setEnergy(int energy){this.energy = energy;}
	public void subEnergy(int energy){this.energy -= energy;}
}
