package ommadawn46.waltzforaitest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class GridWorld {
	WaltzForAITest applet;
	private int gridWidth, gridHeight;
	private final int maxWidth, maxHeight;
	private List<List<List<Entity>>> cluster;

	public GridWorld(WaltzForAITest applet, int gridWidth, int gridHeight){
		this.applet = applet;
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		maxWidth = (int)(applet.getWorldWidth()/gridWidth);
		maxHeight = (int)(applet.getWorldHeight()/gridHeight);

		cluster = new ArrayList<List<List<Entity>>>();
		for(int i = 0; i <= maxWidth; i++){
			List<List<Entity>> xCluster = new ArrayList<List<Entity>>();
			for(int j = 0; j <= maxHeight; j++){
				xCluster.add(new ArrayList<Entity>());
			}
			cluster.add(xCluster);
		}
	}

	public GridWorld(WaltzForAITest applet){
		this(applet, 1000, 1000);
	}

	public List<Entity> searchEntityInArea(float x, float y, float range){
		List<HashSet<Integer>> grids = new ArrayList<HashSet<Integer>>();
		for(int i = 0; i <= maxWidth; i++){
			grids.add(new HashSet<Integer>());
		}

		int gridX = (int)(x/gridWidth), gridY = (int)(y/gridHeight);

		grids.get(gridX).add(gridY);
		searchGridInRange(gridX, gridY, x, y, range+Entity.maxSize, grids);

		List<Entity> entities = new ArrayList<Entity>();
		for(int entityX = 0; entityX <= maxWidth; entityX++){
			HashSet<Integer> xGrid = grids.get(entityX);
			for(int entityY: xGrid){
				//applet.fill(0, 0, 0, 10);
				//applet.rect(entityX*gridWidth, entityY*gridHeight, gridWidth, gridHeight);

				List<Entity> grid = cluster.get(entityX).get(entityY);
				for(Entity entity: grid){
					entities.add(entity);
				}
			}
		}
		return entities;
	}

	private void searchGridInRange(int gridX, int gridY, float x, float y, float range, List<HashSet<Integer>> grids){
		if(Util.getDistance(gridX*gridWidth, gridY*gridHeight, x, y) < range/2){
			if(0 <= gridX-1 && grids.get(gridX-1).add(gridY)){
				searchGridInRange(gridX-1, gridY, x, y, range, grids);
			}
			if(0 <= gridX-1 && 0 <= gridY-1 && grids.get(gridX-1).add(gridY-1)){
				searchGridInRange(gridX-1, gridY-1, x, y, range, grids);
			}
			if(0 <= gridY-1 && grids.get(gridX).add(gridY-1)){
				searchGridInRange(gridX, gridY-1, x, y, range, grids);
			}
		}
		if(Util.getDistance((gridX+1)*gridWidth, gridY*gridHeight, x, y) < range/2){
			if(0 <= gridY-1 && grids.get(gridX).add(gridY-1)){
				searchGridInRange(gridX, gridY-1, x, y, range, grids);
			}
			if(gridX+1 <= maxWidth && 0 <= gridY-1 && grids.get(gridX+1).add(gridY-1)){
				searchGridInRange(gridX+1, gridY-1, x, y, range, grids);
			}
			if(gridX+1 <= maxWidth && grids.get(gridX+1).add(gridY)){
				searchGridInRange(gridX+1, gridY, x, y, range, grids);
			}
		}
		if(Util.getDistance((gridX+1)*gridWidth, (gridY+1)*gridHeight, x, y) < range/2){
			if(gridX+1 <= maxWidth && grids.get(gridX+1).add(gridY)){
				searchGridInRange(gridX+1, gridY, x, y, range, grids);
			}
			if(gridX+1 <= maxWidth && gridY+1 <= maxHeight && grids.get(gridX+1).add(gridY+1)){
				searchGridInRange(gridX+1, gridY+1, x, y, range, grids);
			}
			if(gridY+1 <= maxHeight && grids.get(gridX).add(gridY+1)){
				searchGridInRange(gridX, gridY+1, x, y, range, grids);
			}
		}
		if(Util.getDistance(gridX*gridWidth, (gridY+1)*gridHeight, x, y) < range/2){
			if(gridY+1 <= maxHeight && grids.get(gridX).add(gridY+1)){
				searchGridInRange(gridX, gridY+1, x, y, range, grids);
			}
			if(0 <= gridX-1 && gridY+1 <= maxHeight && grids.get(gridX-1).add(gridY+1)){
				searchGridInRange(gridX-1, gridY+1, x, y, range, grids);
			}
			if(0 <= gridX-1 && grids.get(gridX-1).add(gridY)){
				searchGridInRange(gridX-1, gridY, x, y, range, grids);
			}
		}
		if(0 <= gridX-1 && x-range/2 < gridX*gridWidth){
			grids.get(gridX-1).add(gridY);
		}
		if(0 <= gridY-1 && y-range/2 < gridY*gridHeight){
			grids.get(gridX).add(gridY-1);
		}
		if(gridX+1 <= maxWidth && (gridX+1)*gridWidth < x+range/2){
			grids.get(gridX+1).add(gridY);
		}
		if(gridY+1 <= maxHeight && (gridY+1)*gridHeight < y+range/2){
			grids.get(gridX).add(gridY+1);
		}
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