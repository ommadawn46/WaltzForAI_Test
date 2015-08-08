package ommadawn46.waltzforaitest;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PVector;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

public class WaltzForAITest extends PApplet{
	private static final long serialVersionUID = 31919383465911459L;
	private PVector basePos;
	private PVector dragPos;
	private boolean dragging;
	private float scale;
	private float worldWidth, worldHeight;
	private List<Entity> entityList;
	private GridWorld gridWorld;
	private int energy;
	private int updateSpeed;

	public static void main(String args[]){
		PApplet.main(new String[] { "--present", "ommadawn46.waltzforaitest.WaltzForAITest" });
	}
	
	public WaltzForAITest(){
		basePos = new PVector(0, 0);
		dragPos = new PVector();
		dragging = false;
		scale = 1f;
		worldWidth = 10000;
		worldHeight = 10000;
		entityList = new ArrayList<Entity>();
		gridWorld = new GridWorld(this);
		energy = 10000000;
		updateSpeed = 1;
	}

	@Override
	public void setup(){
		size(displayWidth, displayHeight);
		noStroke();
		while(energy > 0){
			int spend = (int)(Math.random()*80+20);
			entityList.add(new Plant(this, spend)); 
			energy -= spend;
			
			spend = (int)(Math.random()*3200+2000);
			entityList.add(new Animal(this, spend));
			energy -= spend;
		}
	}

	@Override
	public void draw(){
		long b = System.nanoTime();
		
		for(int i = 0; i < updateSpeed; i++){
			update();
		}
		drawDisplayArea();
		
		System.out.println(System.nanoTime() - b);
	}
	
	private void update(){
		for(int i = 0; i < 10; i++){
			if(energy > 0){
				int spend = (int)(Math.random()*80+20);
				entityList.add(new Plant(this, spend)); 
				energy -= spend;
			}else{
				break;
			}
		}
		gridWorld.update(entityList);
		
		List<Entity> updateList = new ArrayList<Entity>(entityList);
		for(Entity entity: updateList){
			if(entity.isAlive()){
				entity.update();
			}
		}
	}
	
	private void drawDisplayArea(){
		background(255);
		
		scale(scale);
		pushMatrix();
		moveDisplayArea();
		
		stroke(0);
		line(0, 0, worldWidth, 0);
		line(worldWidth, 0, worldWidth, worldHeight);
		line(worldWidth, worldHeight, 0, worldHeight);
		line(0, worldHeight, 0, 0);
		noStroke();
		
		for(Entity entity: entityList){
			if(withinDisplayArea(entity)){
				entity.draw();
			}
		}
		popMatrix();
		scale(1/scale);
		
		fill(0);
		text(String.format("(%.0f,%.0f)  Speed x%d  Zoom x%.2f", basePos.x, basePos.y, updateSpeed, scale), 10, 20);
	}
	
	private void moveDisplayArea(){
		if(dragging){
			basePos.add((mouseX - dragPos.x)/scale, (mouseY - dragPos.y)/scale, 0);
			dragPos.set(mouseX, mouseY);
		}
		translate(basePos.x, basePos.y);
	}
	
	@Override
	public void keyPressed(KeyEvent e){
		switch(e.getKeyCode()){
		case UP: scale += 0.01; break;
		case DOWN: scale = scale <= 0.01f ? 0.01f : scale-0.01f; break;
		case LEFT: updateSpeed = updateSpeed <= 0 ? 0 : updateSpeed-1; break;
		case RIGHT: updateSpeed++; break;
		}		
	}
	
	@Override
	public void mousePressed(MouseEvent e){
		dragging = true;
		dragPos.set(e.getX(), e.getY());
	}
	
	@Override
	public void mouseReleased(MouseEvent e){
		dragging = false;
	}
	
	private boolean withinDisplayArea(Entity entity){
		if(0 <= basePos.x+entity.getX()+entity.getSize()/2 && basePos.x+entity.getX()-entity.getSize()/2 <= displayWidth/scale &&
				0 <= basePos.y+entity.getY()+entity.getSize()/2 && basePos.y+entity.getY()-entity.getSize()/2 <= displayHeight/scale){
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
	public void plusEnergy(int plusEnergy){energy += plusEnergy;}
}
