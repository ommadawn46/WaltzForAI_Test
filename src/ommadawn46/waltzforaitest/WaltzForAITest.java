package ommadawn46.waltzforaitest;

import java.util.ArrayList;
import java.util.List;

import ommadawn46.waltzforaitest.entity.Entity;
import ommadawn46.waltzforaitest.entity.Plant;
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
	private List<Entity> entityList;
	private EntityControl entityControl;

	public static void main(String args[]){
		PApplet.main(new String[] { "--present", "ommadawn46.waltzforaitest.WaltzForAITest" });
	}

	public WaltzForAITest(){
		basePos = new PVector();
		dragPos = new PVector();
		dragging = false;
		scale = 1f;
		entityList = new ArrayList<Entity>();
		entityControl = new EntityControl(this);
	}

	@Override
	public void setup(){
		size(displayWidth, displayHeight);
		basePos.set(0, 0);
		entityControl.start();
	}

	@Override
	public void draw(){
		background(255);

		scale(scale);
		pushMatrix();
		moveDisplayArea();

		stroke(0);
		noFill();
		rect(0, 0, entityControl.getWorldWidth(), entityControl.getWorldHeight());
		noStroke();

		int plantNum = 0, herbivoreNum = 0, carnivoreNum = 0;
		for(Entity entity: entityList){
			if(entity instanceof Plant){
				plantNum++;
			}else if(entity instanceof Herbivore){
				herbivoreNum++;
			}else{
				carnivoreNum++;
			}
			if(withinDisplayArea(entity)){
				entity.draw();
			}
		}
		popMatrix();
		scale(1/scale);

		fill(0);
		text(String.format("(%.0f,%.0f)  Speed x%.2f  Zoom x%.2f"+(entityControl.isSuspended()?"  SUSPENDED":""),
				basePos.x, basePos.y, entityControl.getGameSpeed(), scale), 10, 20);
		text(String.format("FPS: %.1f", entityControl.getFPS()), 10, 40);
		text(String.format("Plant: %d", plantNum), 10, 60);
		text(String.format("Herbivore: %d", herbivoreNum), 10, 80);
		text(String.format("Carnivore: %d", carnivoreNum), 10, 100);
	}

	private void moveDisplayArea(){
		if(dragging){
			basePos.add((mouseX - dragPos.x)/scale, (mouseY - dragPos.y)/scale, 0);
			dragPos.set(mouseX, mouseY);
		}
		translate(basePos.x+(displayWidth/2)/scale, basePos.y+(displayHeight/2)/scale);
	}

	@Override
	public void keyPressed(KeyEvent e){
		switch(e.getKeyCode()){
		case UP: scale += 0.01; break;
		case DOWN: scale = scale <= 0.02f ? 0.01f : scale-0.01f; break;
		case LEFT: entityControl.minusGameSpeed(); break;
		case RIGHT: entityControl.plusGameSpeed(); break;
		case ' ': entityControl.resumeOrSuspend(); break;
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
		float scaledWidth = (displayWidth/2)/scale+entity.getSize();
		float scaledHeight = (displayHeight/2)/scale+entity.getSize();
		if(-scaledWidth <= basePos.x+entity.getX() && basePos.x+entity.getX() <= scaledWidth &&
				-scaledHeight <= basePos.y+entity.getY() && basePos.y+entity.getY() <= scaledHeight){
			return true;
		}
		return false;
	}

	public void setEntityList(List<Entity> entityList){this.entityList = entityList;}
}
