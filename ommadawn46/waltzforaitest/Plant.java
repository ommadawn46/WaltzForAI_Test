package ommadawn46.waltzforaitest;


public class Plant extends Entity{
	private float visualSize;
	public Plant(WaltzForAITest applet) {
		super(applet);
		visualSize = (float)(size / Math.sqrt(2));
	}

	@Override
	public void update(boolean isDrawn) {
		if(isDrawn){
			draw();
		}
	}
	
	private void draw(){
		applet.fill(0, 200, 0, 20);
		applet.ellipse(x, y, size, size);
		applet.fill(r, g, b, 150);
		applet.rect(x-visualSize/2, y-visualSize/2, visualSize, visualSize);
		applet.fill(0, 200, 0, 255);
		applet.ellipse(x, y, 10, 10);
	}
}
