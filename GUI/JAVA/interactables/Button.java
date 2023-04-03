package GUI.JAVA.interactables;

import processing.core.PImage;
import GUI.JAVA.gui.Interactable;

public abstract class Button extends Interactable {
	
	public Button(PImage image, int x, int y, float scale) {
		super(image, x, y, scale);
	}

}
