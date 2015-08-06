package ommadawn46.ichalletest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import processing.core.PApplet;

public class WaltzForAITest extends PApplet{
	private static final long serialVersionUID = 31919383465911459L;
	private List<Entity> entityList = new ArrayList<Entity>();
	private List<Entity> xOrderList = new ArrayList<Entity>();
	private List<Entity> yOrderList = new ArrayList<Entity>();
	private Comparator<Entity> xAsc = new Comparator<Entity>() {
		@Override
		public int compare(Entity e1, Entity e2) {
			return Float.compare(e1.getX(), e2.getX());
		}
	 };
	 private Comparator<Entity> yAsc = new Comparator<Entity>() {
		@Override
		public int compare(Entity e1, Entity e2) {
			return Float.compare(e1.getY(), e2.getY());
		}
	};

	public static void main(String args[]){
		PApplet.main(new String[] { "--present", "ommadawn46.ichalletest.IChalleTest" });
	}

	@Override
	public void setup(){
		size(displayWidth, displayHeight);

		for(int i = 0; i < 100; i++){
			addToList(new Plant(this));
			addToList(new Animal(this));
		}
	}

	@Override
	public void draw(){
		addToList(new Plant(this));
		sortList();
		noStroke();
		background(255);
		List<Entity> updateList = new ArrayList<Entity>(entityList);
		for(Entity entity: updateList){
			if(entity.isAlive()){
				entity.update();
			}
		}
	}

	public void dieEntity(Entity entity){
		removeFromList(entity);
		entity.die();
	}

	private void addToList(Entity entity){
		entityList.add(entity);
		xOrderList.add(entity);
		yOrderList.add(entity);
	}
	private void removeFromList(Entity entity){
		entityList.remove(entity);
		xOrderList.remove(entity);
		yOrderList.remove(entity);
	}
	private void sortList(){
		Collections.sort(xOrderList, xAsc);
		Collections.sort(yOrderList, yAsc);
	}

	public List<Entity> getXList(){return xOrderList;}
	public List<Entity> getYList(){return yOrderList;}
}
