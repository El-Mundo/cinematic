package GUI.JAVA.interactables;

import GUI.JAVA.main.MetaVisual;

public class AntialiasingButtonGroup extends ButtonGroup {

	public AntialiasingButtonGroup() {
		super(0);
	}

	@Override
	public void interact() {
		if(sel >= 0 && sel < 3)
			MetaVisual.antialiasingLevel = sel;
	}

}
