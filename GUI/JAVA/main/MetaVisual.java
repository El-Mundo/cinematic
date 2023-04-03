package GUI.JAVA.main;

/*
 * @version 2.0.1
 * @author Shuangyuan Cao
 * @since 0.0.1
 * 
 * Github page:
 * https://github.com/El-Mundo/cinematic
 */

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;

import processing.core.*;
import processing.awt.PSurfaceAWT.SmoothCanvas;
import GUI.JAVA.scenes.TestScene;

public class MetaVisual extends PApplet {
	private final static int DEFAULT_WIDTH = 1920, DEFAULT_HEIGHT = 1080;
	private final static float DEFAULT_SCALE = 0.75F, DEFAULT_RATIO = (float)DEFAULT_WIDTH / (float)DEFAULT_HEIGHT;
	private final static Dimension WINDOW_MINIMUM = new Dimension(DEFAULT_WIDTH / 2, DEFAULT_HEIGHT / 2);
	
	private static float frameOffsetX = 0, frameOffsetY = 0;
	private static float frameScale = 1.0f;

	public static Frame nativeWindow;
	public static MetaVisual main;
	public static boolean forceQuit = false;

	public static int antialiasingLevel = 0;
	private static int state = -1;

	public static int cursorX, cursorY, pCursorX, pcursorY;
	public static boolean clicked = false, pClicked = false;

	private TestScene testScene;

	public static void main(String args[]) {
		PApplet.main("GUI.JAVA.MetaVisual");
	}

	@Override
	public void settings() {
		//Adapt display rate to the screen
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int height = (int)(screenSize.height * DEFAULT_SCALE);
		frameScale = (float)height / (float)DEFAULT_HEIGHT;
		int width = (int)(DEFAULT_WIDTH * frameScale);
		size(width, height, JAVA2D);
		
		//Allocate this PApplet object's address to the static 'main' instance
		main = this;
		
		//Disable anti-aliasing at beginning
		noSmooth();
	}

	@Override
	public void setup() {
		//surface.setIcon(GraphicResouces.ICON);
		//Initialize the first scene
		testScene = new TestScene(DEFAULT_WIDTH, DEFAULT_HEIGHT, this);
				
		//To make the window adaptable for most devices
		surface.setResizable(true);
		//Add a listener so that resizing the window can be detected
		nativeWindow = ((SmoothCanvas)(surface.getNative())).getFrame();
		nativeWindow.addComponentListener(resizeListener);
		nativeWindow.setMinimumSize(WINDOW_MINIMUM);
		
		JFrame frame = (JFrame)nativeWindow;
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void draw() {
		background(0);
		translate(frameOffsetX, frameOffsetY);
		scale(frameScale);
		
		switch (state) {
		case -1: //Test scene
			if(testScene != null) {
				testScene.draw();
				image(testScene, 0, 0);
			}
			break;
		}
				
		pCursorX = cursorX;
		pcursorY = cursorY;
		pClicked = clicked;
		cursorX = (int)((float)(this.mouseX - frameOffsetX) / frameScale);
		cursorY = (int)((float)(this.mouseY - frameOffsetY) / frameScale);
		circle(cursorX, cursorY, 6.0f / frameScale);
		
		if(forceQuit) exit();
	}
	
	@Override
	public void mousePressed() {
		clicked = true;
	}
	
	@Override
	public void mouseReleased() {
		clicked = false;
	}
	
	@Override
	public void mouseExited() {
		clicked = false;
	}
	
	@Override
	public void focusLost() {
		clicked = false;
		pClicked = false;
	}
	
	public void resize() {
		//Use the shorter edge to define the scale factor
		if((float)width / (float)height < DEFAULT_RATIO) {
			//When the new ratio is higher than the default, use width for resizing
			frameScale = (float)width / DEFAULT_WIDTH;
			frameOffsetY = ((float)height - (float)DEFAULT_HEIGHT * frameScale) * 0.5f;
			frameOffsetX = 0;
		}else {
			//When the new ratio is wider than the default, use height for resizing
			frameScale = (float)height / DEFAULT_HEIGHT;
			frameOffsetX = ((float)width - (float)DEFAULT_WIDTH * frameScale) * 0.5f;
			frameOffsetY = 0;
		}
	}
	
	private static ComponentListener resizeListener = new ComponentListener() {
		@Override
		public void componentResized(ComponentEvent e) {
			((MetaVisual)main).resize();
		}
		
		@Override
		public void componentShown(ComponentEvent e) {}
		@Override
		public void componentMoved(ComponentEvent e) {}
		@Override
		public void componentHidden(ComponentEvent e) {}
	};
	
	public int getState() {
		return state;
	}
	
}