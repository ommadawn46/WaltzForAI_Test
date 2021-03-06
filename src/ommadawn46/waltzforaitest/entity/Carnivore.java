package ommadawn46.waltzforaitest.entity;

import java.util.List;

import ommadawn46.waltzforaitest.EntityControl;
import ommadawn46.waltzforaitest.Herbivore;
import ommadawn46.waltzforaitest.Util;
import ommadawn46.waltzforaitest.WaltzForAITest;

public class Carnivore extends Animal {
	public Carnivore(WaltzForAITest applet, EntityControl entityControl, 
			float x, float y, float size, int energy, Animal[] parents) {
		super(applet, entityControl, x, y, size, energy, parents);
		fov = Math.PI / 4;
		speed *= 2;
	}

	public Carnivore(WaltzForAITest applet, EntityControl entityControl, int energy) {
		this(applet, entityControl, -1, -1, -1, energy, new Animal[]{null, null});
	}

	@Override
	public void draw() {
		if(target != null){
			applet.stroke(0);
			applet.line(x, y, target.getX(), target.getY());
		}
		applet.noStroke();
		applet.fill(200, 200, 0, 10);
		applet.arc(x, y, range*2, range*2, (float)(direction-fov), (float)(direction+fov));
		applet.fill(200, 0, 0, 20);
		applet.ellipse(x, y, size*2, size*2);
		applet.fill(r, g, b, 150);
		applet.triangle((float)(x+size/0.9*Math.cos(direction)), (float)(y+size/0.9*Math.sin(direction)),
				(float)(x+size/1.1*Math.cos(direction+Math.PI*2/3)), (float)(y+size/1.1*Math.sin(direction+Math.PI*2/3)),
				(float)(x+size/1.1*Math.cos(direction-Math.PI*2/3)), (float)(y+size/1.1*Math.sin(direction-Math.PI*2/3)));
		applet.fill(200, 0, 0, 255);
		applet.ellipse(x, y, 10, 10);
		applet.text(energy, x, y-20);
		if(canCross()) applet.fill(0, 0, 100, 255);
		applet.text(age, x, y+30);
	}

	@Override
	protected void searchEntityAndCollisionDetection() {
		if(target != null &&
				(!target.isAlive() || range + target.getSize() < Util.getDistance(x, y, target.getX(), target.getY()))){
			target = null;
		}
		if(friend != null &&
				(!friend.isAlive() || !friend.canCross() || range + friend.getSize() < Util.getDistance(x, y, friend.getX(), friend.getY()))){
			friend = null;
		}
		if(enemy != null &&
				(!enemy.isAlive() || range + enemy.getSize() < Util.getDistance(x, y, enemy.getX(), enemy.getY()))){
			enemy = null;
		}

		double minTargetDist, minFriendDist, minEnemyDist;
		minTargetDist = minFriendDist = minEnemyDist = range + Entity.maxSize;

		List<Entity> entities = entityControl.getGridWorld().searchEntityInArea(x, y, range);
		for(Entity entity: entities){
			double distance = Util.getDistance(x, y, entity.getX(), entity.getY());
			if(Util.inFieldOfView(this, entity)){
				boolean isCarnivore, isRelated = false;
				if(isCarnivore = entity instanceof Carnivore){
					isRelated = entityControl.isRelatedSpecies(this, (Carnivore)entity);
				}
				if(entity instanceof Herbivore || 
						(isCarnivore && !isRelated && entity.getEnergy() < energy)){
					if(distance < minTargetDist){
						target = entity;
						minTargetDist = distance;
					}
				}
				if(isCarnivore && !entity.equals(this)){
					if(!isRelated && distance < minEnemyDist && energy < entity.getEnergy()){
						enemy = entity;
						minEnemyDist = distance;
					}else if(isRelated && distance < minFriendDist && canCross() && ((Carnivore)entity).canCross()){
						friend = (Carnivore)entity;
						minFriendDist = distance;
					}
				}
				if(distance < size + entity.getSize()){
					if(entity.equals(target)){
						eat(entity);
					}else if(entity.equals(friend)){
						entityControl.crossEntity(this, (Carnivore)entity);
					}
				}
			}
		}
	}
}
