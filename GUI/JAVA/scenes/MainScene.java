package GUI.JAVA.scenes;

import GUI.JAVA.gui.Scene;
import processing.core.PApplet;

public class MainScene extends Scene {

	public MainScene(int width, int height, PApplet parent) {
		super(width, height, parent);
	}

	@Override
	public void drawContent() {
		fill(128, 64, 64);
		rect(0, 980, 1920, 100);
	}
	
}
