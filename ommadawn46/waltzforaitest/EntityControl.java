package ommadawn46.waltzforaitest;

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

		while(energy > 0){
			int spend = (int)(Math.random()*80+20);
			entityList.add(new Plant(applet, this, spend));
			energy -= spend;

			spend = (int)(Math.random()*3200+2000);
			entityList.add(new Animal(applet, this, spend));
			energy -= spend;
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
		for(int i = 0; i < 10; i++){
			if(energy > 0){
				int spend = (int)(Math.random()*80+20);
				entityList.add(new Plant(applet, this, spend));
				energy -= spend;
			}else{
				break;
			}
		}
		gridWorld.update(entityList);

		List<Entity> updateList = new ArrayList<Entity>(entityList);
		applet.setEntityList(updateList);

		for(Entity entity: updateList){
			if(entity.isAlive()){
				entity.update();
			}
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
	public float getWorldWidth(){return worldWidth;}
	public float getWorldHeight(){return worldHeight;}
	public boolean isSuspended(){return suspended;}
	public void plusEnergy(int plusEnergy){energy += plusEnergy;}
}
