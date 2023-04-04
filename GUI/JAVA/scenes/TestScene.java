package GUI.JAVA.scenes;

import java.awt.Graphics2D;
import java.io.File;

import GUI.JAVA.gui.*;
import GUI.JAVA.interactables.*;
import GUI.JAVA.main.MetaVisual;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

public class TestScene extends Scene {
	public File file;
	Button[] buttons;
	AntialiasingButtonGroup smoothSetting;

	public TestScene(int width, int height, PApplet parent) {
		super(width, height, parent);
		
		smoothSetting = new AntialiasingButtonGroup();
		
		buttons = new Button[3];
		buttons[0] = new ButtonGroup.GroupedButton(GraphicResouces.SMOOTH_OFF, 128, 8, 2, smoothSetting);
		buttons[1] = new ButtonGroup.GroupedButton(GraphicResouces.SMOOTH_2X, 192, 8, 2, smoothSetting);
		buttons[2] = new ButtonGroup.GroupedButton(GraphicResouces.SMOOTH_3X, 240, 8, 2, smoothSetting);
	}

	@Override
	public void drawContent() {
		background(255);

		fill(0);
		textSize(80);
		text("Hello World.", 20, 120);
		image(GraphicResouces.SMOOTH, 16, 10, 96, 20);
		
		//image(GraphicResouces.res_map, 0, 0);
		PGraphics g = MetaVisual.main.createGraphics(GraphicResouces.res_map.width, GraphicResouces.res_map.height);
		g.beginDraw();
		g.fill(255, 0, 0);
		g.noStroke();
		g.beginShape(QUAD_STRIP);
		g.colorMode(HSB, 255);
			drawGradient(900, 400, 240, g);
			drawGradient(960, 580, 320, g);
		g.endShape();
		g.endDraw();
		PImage p = g.copy();
		p.mask(GraphicResouces.res_map);
		image(p, 0, 0);
		
		for(Button b : buttons) {
			b.display(this);
		}
	}

	void drawGradient(float x, float y, int radius, PGraphics g) {
		float h = 128;
		float tr = 0;
		for (int r = radius; r > 0; --r) {
			g.fill(h, 128, 128, tr);
			g.ellipse(x, y, r, r);
			tr = PApplet.map(r, radius, 0, 0, 255);
		}
	}

}
