package ommadawn46.waltzforaitest;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import ommadawn46.waltzforaitest.entity.Animal;
import ommadawn46.waltzforaitest.entity.Carnivore;
import ommadawn46.waltzforaitest.entity.Entity;
import ommadawn46.waltzforaitest.entity.Plant;

public class EntityControl extends Thread {
	private WaltzForAITest applet;
	private GridWorld gridWorld;
	private List<Entity> entityList;
	private float worldWidth, worldHeight;
	private long energy;
	private int idealFPS;
	private LinkedList<Long> processTime;
	private double realFPS;
	private boolean suspended;
	private boolean running;

	public EntityControl(WaltzForAITest applet, long energy, int worldWidth, int worldHeight){
		this.applet = applet;
		this.energy = energy;
		this.worldWidth = worldWidth;
		this.worldHeight = worldHeight;

		gridWorld = new GridWorld(this);
		entityList = new ArrayList<Entity>();
		realFPS = idealFPS = 60;
		processTime = new LinkedList<Long>();
		for(int i = 0; i < 60; i++){
			processTime.add(1000L/idealFPS);
		}
		suspended = false;
		running = true;

		while(this.energy > 0){
			int spend = (int)(Math.random()*800+200);
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
		long beforeTime, oldTime;
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

			beforeTime = oldTime = newTime;
		  	update();
		  	newTime = System.currentTimeMillis() << 16;

		  	long idealSleep = (1000 << 16) / idealFPS;
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
		  	processTime.pop();
		  	processTime.addLast((newTime-beforeTime) >> 16);
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
		
		double timeSum = 0;
		for(long time: processTime){
			timeSum += time;
		}
		realFPS = 1000.0/(timeSum/processTime.size());
	}

	public void energyDrop(Entity entity, int amount){
		entity.subEnergy(amount);
		energy += amount;
		if(energy > 0 && (int)(Math.random()*50) == 0){
			int spend = (int)(Math.random()*800+200);
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
		Animal[] parents = new Animal[]{e1, e2};
		try {
			entityList.add(e1.getClass().getConstructor(WaltzForAITest.class, EntityControl.class, float.class, float.class, float.class, int.class, Animal[].class).
			newInstance(applet, this, (e1.getX()+e2.getX())/2, (e1.getY()+e2.getY())/2, e1.getSize()+e2.getSize(), babyEnagy*2, parents));
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isRelatedSpecies(Animal e1, Animal e2){
		HashSet<Animal> descent1 = new HashSet<Animal>(), descent2 = new HashSet<Animal>();
		descent1.add(e1); descent2.add(e2);
		checkParents(e1, descent1, 3);
		checkParents(e2, descent2, 3);
		for(Animal parent: descent1){
			if(descent2.contains(parent)){
				return true;
			}
		}
		return false;
	}
	
	private void checkParents(Animal animal, HashSet<Animal> descent, int depth){
		if(depth < 0){
			return;
		}
		Animal[] parents = animal.getParents();
		if(descent.add(parents[0]) && parents[0] != null){
			checkParents(parents[0], descent, depth-1);
		}
		if(descent.add(parents[1]) && parents[1] != null){
			checkParents(parents[1], descent, depth-1);
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
	public void plusGameSpeed(){idealFPS++;}
	public void minusGameSpeed(){idealFPS = idealFPS <= 1 ? 1 : idealFPS-1;}
	public double getGameSpeed(){return idealFPS/60.0;}
	public double getFPS(){return realFPS;}
	public long getEnergy(){return energy;}
	public float getWorldWidth(){return worldWidth;}
	public float getWorldHeight(){return worldHeight;}
	public boolean isSuspended(){return suspended;}
}
