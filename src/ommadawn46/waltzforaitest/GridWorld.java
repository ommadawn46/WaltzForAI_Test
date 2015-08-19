package ommadawn46.waltzforaitest;

import java.util.ArrayList;
import java.util.List;

import ommadawn46.waltzforaitest.entity.Entity;

public class GridWorld {
	EntityControl entityControl;
	private int gridWidth, gridHeight;
	private final int maxWidth, maxHeight;
	private List<List<List<Entity>>> cluster;

	public GridWorld(EntityControl entityControl, int gridWidth, int gridHeight){
		this.entityControl = entityControl;
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		maxWidth = (int)(entityControl.getWorldWidth()/gridWidth);
		maxHeight = (int)(entityControl.getWorldHeight()/gridHeight);

		cluster = new ArrayList<List<List<Entity>>>(maxWidth+1);
		for(int i = 0; i <= maxWidth; i++){
			List<List<Entity>> xCluster = new ArrayList<List<Entity>>(maxHeight+1);
			for(int j = 0; j <= maxHeight; j++){
				xCluster.add(new ArrayList<Entity>());
			}
			cluster.add(xCluster);
		}
	}

	public GridWorld(EntityControl entityControl){
		this(entityControl, 150, 150);
	}

	public List<Entity> searchEntityInArea(float x, float y, float range){
		int gridX = (int)(x/gridWidth), gridY = (int)(y/gridHeight);

		double radius = range + Entity.maxSize;
		int verticalOver = (int)(radius / gridWidth) + 1;
		int horizontalOver = (int)(radius / gridHeight) + 1;

		int leftX = gridX - verticalOver < 0 ? 0 : gridX - verticalOver;
		int rightX = maxWidth < gridX + verticalOver ? maxWidth : gridX + verticalOver;

		int leftY = gridY - horizontalOver < 0 ? 0 : gridY - horizontalOver;
		int rightY = maxHeight < gridY + horizontalOver ? maxHeight : gridY + horizontalOver;

		List<Entity> entities = new ArrayList<Entity>();
		for(int entityX = leftX; entityX <= rightX; entityX++){
			for(int entityY = leftY; entityY <= rightY; entityY++){
				//applet.fill(0, 0, 0, 10);
				//applet.rect(entityX*gridWidth, entityY*gridHeight, gridWidth, gridHeight);

				entities.addAll(cluster.get(entityX).get(entityY));
			}
		}
		return entities;
	}

	public void update(List<Entity> entities){
		refresh();
		for(Entity entity: entities){
			addEntity(entity);
		}
	}

	private void addEntity(Entity entity){
		cluster.get((int)(entity.getX()/gridWidth)).get((int)(entity.getY()/gridHeight)).add(entity);
	}

	private void refresh(){
		for(List<List<Entity>> xCluster: cluster){
			for(List<Entity> grid: xCluster){
				grid.clear();
			}
		}
	}
}