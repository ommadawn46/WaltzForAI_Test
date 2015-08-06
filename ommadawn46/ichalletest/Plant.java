package ommadawn46.ichalletest;


public class Plant extends Entity{
	private float visualSize;
	public Plant(WaltzForAITest applet) {
		super(applet);
		visualSize = (float)(size / Math.sqrt(2));
	}

	@Override
	public void update() {
		applet.fill(0, 200, 0, 20);
		applet.ellipse(x, y, size, size);
		applet.fill(r, g, b, 150);
		applet.rect(x-visualSize/2, y-visualSize/2, visualSize, visualSize);
		applet.fill(0, 200, 0, 255);
		applet.ellipse(x, y, 10, 10);
	}
}
