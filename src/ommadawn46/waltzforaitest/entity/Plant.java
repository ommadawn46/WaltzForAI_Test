package ommadawn46.waltzforaitest.entity;

import ommadawn46.waltzforaitest.EntityControl;
import ommadawn46.waltzforaitest.WaltzForAITest;


public class Plant extends Entity{
	private float visualSize;

	public Plant(WaltzForAITest applet, EntityControl entityControl, float x, float y, float size, int energy) {
		super(applet, entityControl, x, y, size, energy);
		this.size = 50 * this.energy/1000;
		visualSize = (float)(this.size*2 / Math.sqrt(2));
	}

	public Plant(WaltzForAITest applet, EntityControl entityControl, int energy) {
		this(applet, entityControl, -1, -1, -1, energy);
	}

	@Override
	public void update() {
	}

	@Override
	public void draw(){
		applet.noStroke();
		applet.fill(0, 200, 0, 20);
		applet.ellipse(x, y, size*2, size*2);
		applet.fill(r, g, b, 150);
		applet.rect(x-visualSize/2, y-visualSize/2, visualSize, visualSize);
		applet.fill(0, 200, 0, 255);
		applet.ellipse(x, y, 10, 10);
		applet.text(energy, x, y-20);
	}
}
