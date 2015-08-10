package ommadawn46.waltzforaitest;

import java.util.List;

public class Carnivore extends Animal {
	int rank;

	public Carnivore(WaltzForAITest applet, EntityControl entityControl, float x, float y, float size,
			int energy) {
		super(applet, entityControl, x, y, size, energy);
		speed *= 2;
		rank = (int)this.size / 10;
	}

	public Carnivore(WaltzForAITest applet, EntityControl entityControl, int energy) {
		this(applet, entityControl, -1, -1, -1, energy);
	}

	@Override
	public void draw() {
		applet.noStroke();
		applet.fill(200, 200, 0, 10);
		applet.arc(x, y, range, range, (float)(direction-Math.PI/4), (float)(direction+Math.PI/4));
		applet.fill(200, 0, 0, 20);
		applet.ellipse(x, y, size, size);
		applet.fill(r, g, b, 150);
		applet.triangle((float)(x+size/1.8*Math.cos(direction)), (float)(y+size/1.8*Math.sin(direction)),
				(float)(x+size/2.2*Math.cos(direction+Math.PI*2/3)), (float)(y+size/2.2*Math.sin(direction+Math.PI*2/3)),
				(float)(x+size/2.2*Math.cos(direction-Math.PI*2/3)), (float)(y+size/2.2*Math.sin(direction-Math.PI*2/3)));
		applet.fill(200, 0, 0, 255);
		applet.ellipse(x, y, 10, 10);
		applet.text(energy, x, y-20);
		applet.text(rank, x+20, y);
		if(canCross()) applet.fill(0, 0, 100, 255);
		applet.text(age, x, y+30);
	}

	@Override
	protected void searchEntityAndCollisionDetection() {
		if(target != null &&
				(!target.isAlive() || range/2 + target.getSize()/2 < Util.getDistance(x, y, target.getX(), target.getY()))){
			target = null;
		}
		if(friend != null &&
				(!friend.isAlive() || !friend.canCross() || range/2 + friend.getSize()/2 < Util.getDistance(x, y, friend.getX(), friend.getY()))){
			friend = null;
		}
		if(enemy != null &&
				(!enemy.isAlive() || range/2 + enemy.getSize()/2 < Util.getDistance(x, y, enemy.getX(), enemy.getY()))){
			enemy = null;
		}

		double minTargetDist, minFriendDist, minEnemyDist;
		minTargetDist = minFriendDist = minEnemyDist = range/2 + Entity.maxSize/2;


		List<Entity> entities = entityControl.getGridWorld().searchEntityInArea(x, y, range);
		for(Entity entity: entities){
			double distance = Util.getDistance(x, y, entity.getX(), entity.getY());
			if(distance < range/2 + entity.getSize()/2 &&
					Math.abs(Util.getRadianSub(direction, Util.getRadian(x, y, entity.getX(), entity.getY()))) < Math.PI/4){
				if(entity instanceof Herbivore || (entity instanceof Carnivore && ((Carnivore)entity).getRank() < rank)){
					if(distance < minTargetDist){
						target = entity;
						minTargetDist = distance;
					}
				}
				if(entity instanceof Carnivore && !entity.equals(this)){
					if(rank < ((Carnivore)entity).getRank() && distance < minEnemyDist){
						enemy = entity;
						minEnemyDist = distance;
					}else if(rank == ((Carnivore)entity).getRank() && canCross() && ((Carnivore)entity).canCross()){
						if(distance < minFriendDist){
							friend = (Carnivore)entity;
							minFriendDist = distance;
						}
					}
				}
				if(distance < size/2 + entity.getSize()/2){
					if(entity.equals(target)){
						eat(entity);
					}else if(entity.equals(friend)){
						entityControl.crossEntity(this, (Carnivore)entity);
					}
				}
			}
		}
	}

	public int getRank(){return rank;}
}
