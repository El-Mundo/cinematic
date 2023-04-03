package GUI.JAVA;

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

import processing.core.*;

public class MetaVisual extends PApplet {
	private final static int DEFAULT_WIDTH = 720, DEFAULT_HEIGHT = 480;
	private final static float DEFAULT_SCALE = 0.75F, DEFAULT_RATIO = (float)DEFAULT_WIDTH / (float)DEFAULT_HEIGHT;
	private final static Dimension WINDOW_MINIMUM = new Dimension(DEFAULT_WIDTH / 2, DEFAULT_HEIGHT / 2);
	
	private static float frameOffsetX = 0, frameOffsetY = 0;
	private static float frameScale = 1.0f;

	public static Frame nativeWindow;
	public static MetaVisual main;

	public static int antialiasingLevel = 0;
	private static int state = 0;

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
		fileSelectScene = new FileSelectScene(DEFAULT_WIDTH, DEFAULT_HEIGHT, this);
				
		//To make the window adaptable for most devices
		surface.setResizable(true);
		//Add a listener so that resizing the window can be detected
		nativeWindow = ((SmoothCanvas)(surface.getNative())).getFrame();
		nativeWindow.addComponentListener(resizeListener);
		nativeWindow.setMinimumSize(WINDOW_MINIMUM);
		
		JFrame frame = (JFrame)nativeWindow;
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
}