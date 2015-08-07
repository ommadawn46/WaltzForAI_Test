package ommadawn46.waltzforaitest;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;

public class WaltzForAITest extends PApplet{
	private static final long serialVersionUID = 31919383465911459L;
	private float worldWidth, worldHeight;
	private List<Entity> entityList;
	private GridWorld gridWorld;

	public static void main(String args[]){
		PApplet.main(new String[] { "--present", "ommadawn46.waltzforaitest.WaltzForAITest" });
	}
	
	public WaltzForAITest(){
		worldWidth = 10000;
		worldHeight = 10000;
		entityList = new ArrayList<Entity>();
		gridWorld = new GridWorld(this);
	}

	@Override
	public void setup(){
		size(displayWidth, displayHeight);
		
		for(int i = 0; i < 10000; i++){
			entityList.add(new Animal(this));
			entityList.add(new Plant(this));
		}
	}

	@Override
	public void draw(){
		long b = System.nanoTime();
		entityList.add(new Plant(this));
		gridWorld.update(entityList);
		noStroke();
		background(255);
		List<Entity> updateList = new ArrayList<Entity>(entityList);
		for(Entity entity: updateList){
			if(entity.isAlive()){
				entity.update(withinDisplay(entity));
			}
		}
		System.out.println(System.nanoTime() - b);
	}
	
	private boolean withinDisplay(Entity entity){
		if(0 <= entity.getX()+entity.getSize()/2 && entity.getX()-entity.getSize()/2 <= displayWidth &&
				0 <= entity.getY()+entity.getSize()/2 && entity.getY()-entity.getSize()/2 <= displayHeight){
			return true;
		}
		return false;
	}

	public void dieEntity(Entity entity){
		entityList.remove(entity);
		entity.die();
	}

	public float getWorldWidth(){return worldWidth;}
	public float getWorldHeight(){return worldHeight;}
	public GridWorld getGridWorld(){return gridWorld;}
}
