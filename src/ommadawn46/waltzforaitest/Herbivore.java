package ommadawn46.waltzforaitest;

import java.util.List;

public class Herbivore extends Animal {

	public Herbivore(WaltzForAITest applet, EntityControl entityControl, 
			float x, float y, float size, int energy, Animal[] parents) {
		super(applet, entityControl, x, y, size, energy, parents);
		fov = Math.PI*2 / 3;
		range *= 2;
	}

	public Herbivore(WaltzForAITest applet, EntityControl entityControl, int energy) {
		this(applet, entityControl, -1, -1, -1, energy, new Animal[]{null, null});
	}

	@Override
	public void draw(){
		applet.noStroke();
		applet.fill(200, 200, 0, 10);
		applet.arc(x, y, range, range, (float)(direction-fov), (float)(direction+fov));
		applet.fill(r, g, b, 150);
		applet.ellipse(x, y, size, size);
		applet.fill(100, 0, 0, 255);
		applet.ellipse(x, y, 10, 10);
		applet.text(energy, x, y-20);
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
			if(Util.inFieldOfView(this, entity)){
				if(entity instanceof Plant){
					if(distance < minTargetDist){
						target = entity;
						minTargetDist = distance;
					}
				}
				if(entity instanceof Herbivore && !entity.equals(this) && canCross() && ((Herbivore)entity).canCross()){
					if(distance < minFriendDist){
						friend = (Herbivore)entity;
						minFriendDist = distance;
					}
				}
				if(entity instanceof Carnivore){
					if(distance < minEnemyDist){
						enemy = entity;
						minEnemyDist = distance;
					}
				}
				if(distance < size/2 + entity.getSize()/2){
					if(entity.equals(target)){
						eat(entity);
					}else if(entity.equals(friend)){
						entityControl.crossEntity(this, (Herbivore)entity);
					}
				}
			}
		}
	}
}
