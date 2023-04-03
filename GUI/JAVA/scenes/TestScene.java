package GUI.JAVA.scenes;

import java.io.File;

import GUI.JAVA.gui.*;
import GUI.JAVA.interactables.*;
import processing.core.PApplet;

public class TestScene extends Scene {
	public File file;
	Button[] buttons;
	AntialiasingButtonGroup smoothSetting;

	public TestScene(int width, int height, PApplet parent) {
		super(width, height, parent);
		
		/*smoothSetting = new AntialiasingButtonGroup();
		amplitudeFilterSwitch = new AmplitudeFilterButtonGroup();
		
		buttons = new Button[8];
		buttons[0] = new ButtonGroup.GroupedButton(GraphicResouces.SMOOTH_OFF, 128, 8, 2, smoothSetting);
		buttons[1] = new ButtonGroup.GroupedButton(GraphicResouces.SMOOTH_2X, 192, 8, 2, smoothSetting);
		buttons[2] = new ButtonGroup.GroupedButton(GraphicResouces.SMOOTH_3X, 240, 8, 2, smoothSetting);*/
	}

	@Override
	public void drawContent() {
		background(255);

		fill(0);
		textSize(80);
		text("Hello World.", 20, 120);
		
		/*for(Button b : buttons) {
			b.display(this);
		}*/
	}

}
