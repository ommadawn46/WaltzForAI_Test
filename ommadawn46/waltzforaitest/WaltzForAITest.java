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
		line(0, 0, entityControl.getWorldWidth(), 0);
		line(entityControl.getWorldHeight(), 0, entityControl.getWorldWidth(), entityControl.getWorldHeight());
		line(entityControl.getWorldWidth(), entityControl.getWorldHeight(), 0, entityControl.getWorldHeight());
		line(0, entityControl.getWorldHeight(), 0, 0);
		noStroke();

		List<Entity> drawList = new ArrayList<Entity>(entityList);
		for(Entity entity: drawList){
			if(withinDisplayArea(entity)){
				entity.draw();
			}
		}
		popMatrix();
		scale(1/scale);

		fill(0);
		text(String.format("(%.0f,%.0f)  Speed x%.2f  Zoom x%.2f"+(entityControl.isSuspended()?"  SUSPENDED":""),
				basePos.x, basePos.y, entityControl.getGameSpeed(), scale), 10, 20);
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
		if(0 <= basePos.x+(displayWidth/2)/scale+entity.getX()+entity.getSize()/2 && basePos.x+entity.getX()-entity.getSize()/2 <= (displayWidth/2)/scale &&
				0 <= basePos.y+(displayHeight/2)/scale+entity.getY()+entity.getSize()/2 && basePos.y+entity.getY()-entity.getSize()/2 <= (displayHeight/2)/scale){
			return true;
		}
		return false;
	}

	public void setEntityList(List<Entity> entityList){this.entityList = entityList;}
}
