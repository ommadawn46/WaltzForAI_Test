package ommadawn46.waltzforaitest;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class EntityControl extends Thread {
	private WaltzForAITest applet;
	private GridWorld gridWorld;
	private List<Entity> entityList;
	private float worldWidth, worldHeight;
	private int energy;
	private int fps;
	private boolean suspended;
	private boolean running;

	public EntityControl(WaltzForAITest applet, int energy, int worldWidth, int worldHeight){
		this.applet = applet;
		this.energy = energy;
		this.worldWidth = worldWidth;
		this.worldHeight = worldHeight;

		gridWorld = new GridWorld(this);
		entityList = new ArrayList<Entity>();
		fps = 60;
		suspended = false;
		running = true;

		while(this.energy > 0){
			int spend = (int)(Math.random()*80+20);
			entityList.add(new Plant(applet, this, spend));
			this.energy -= spend;

			spend = (int)(Math.random()*3200+2000);
			entityList.add(new Herbivore(applet, this, spend));
			this.energy -= spend;

			spend = (int)(Math.random()*3200+2000);
			entityList.add(new Carnivore(applet, this, spend));
			this.energy -= spend;
		}

		applet.setEntityList(entityList);
	}

	public EntityControl(WaltzForAITest applet){
		this(applet, 10000000, 10000, 10000);
	}

	@Override
	public void run() {
		long error = 0;
		long oldTime;
		long newTime = System.currentTimeMillis() << 16;
		while (running) {
			while (suspended) {
			    synchronized(this) {
			    	try {
						wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
			    }
			}

			oldTime = newTime;
		  	update();
		  	newTime = System.currentTimeMillis() << 16;

		  	long idealSleep = (1000 << 16) / fps;
		  	long sleepTime = idealSleep - (newTime - oldTime) - error;
		  	if (sleepTime < 0){
		  		sleepTime = 0;
		  	}

		  	oldTime = newTime;
		  	try {
		  		Thread.sleep(sleepTime >> 16);
		  	} catch (InterruptedException e) {
		  		e.printStackTrace();
		  	}
		  	newTime = System.currentTimeMillis() << 16;
		  	error = newTime - oldTime - sleepTime;
		}
	}

	private void update(){
		gridWorld.update(entityList);

		List<Entity> updateList = new ArrayList<Entity>(entityList);
		applet.setEntityList(updateList);

		for(Entity entity: updateList){
			if(entity.isAlive()){
				entity.update();
			}
		}
	}

	public void energyDrop(Entity entity, int amount){
		entity.subEnergy(amount);
		energy += amount;
		if(energy > 0 && (int)(Math.random()*50) == 0){
			int spend = (int)(Math.random()*80+20);
			float spawnX = entity.getX()+(int)(Math.random()*400)-200;
			float spawnY = entity.getY()+(int)(Math.random()*400)-200;
			entityList.add(new Plant(applet, this, spawnX, spawnY, -1, spend));
			energy -= spend;
		}
	}

	public <T extends Animal> void crossEntity(T e1, T e2){
		if(!e1.getClass().equals(e2.getClass()) || !e1.canCross() || !e2.canCross()){
			return;
		}
		int babyEnagy = Math.min(e1.getEnergy(), e2.getEnergy())/3;
		e1.subEnergy(babyEnagy);
		e2.subEnergy(babyEnagy);
		e1.resetAge();
		e2.resetAge();
		try {
			entityList.add(e1.getClass().getConstructor(WaltzForAITest.class, EntityControl.class, float.class, float.class, float.class, int.class).
			newInstance(applet, this, (e1.getX()+e2.getX())/2, (e1.getY()+e2.getY())/2, (e1.getSize()+e2.getSize())/2, babyEnagy*2));
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}

	public void dieEntity(Entity entity){
		entityList.remove(entity);
		entity.die();
	}

	public synchronized void resumeOrSuspend() {
		if(suspended){
			suspended = false;
			notify();
		}else{
			suspended = true;
		}
	}

	public void stopThread(){
		running = false;
	}

	public GridWorld getGridWorld(){return gridWorld;}
	public void plusGameSpeed(){fps++;}
	public void minusGameSpeed(){fps = fps <= 1 ? 1 : fps-1;}
	public double getGameSpeed(){return fps/60.0;}
	public int getEnergy(){return energy;}
	public float getWorldWidth(){return worldWidth;}
	public float getWorldHeight(){return worldHeight;}
	public boolean isSuspended(){return suspended;}
}
