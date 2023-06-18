package p3d4amb.sat.lib.shapes;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

// TODO: Auto-generated Javadoc
/**
 *  represents an empty shape .
 *
 * @author garganti
 */
public class NullShape extends Shape{

	/* (non-Javadoc)
	 * @see p3d4amb.sat.lib.shapes.Shape#belongs(int, int)
	 */
	@Override
	public boolean belongs(int x, int y) {
		// no point in here
		return false;
	}

	/* (non-Javadoc)
	 * @see p3d4amb.sat.lib.shapes.Shape#resize(int)
	 */
	@Override
	public void resize(int x) {
		// do nothing
	}

	/* (non-Javadoc)
	 * @see p3d4amb.sat.lib.shapes.Shape#getIcon(org.eclipse.swt.widgets.Display)
	 */
	@Override
	public Image getIcon(Display display) {
		ImageData imageData = new ImageData(getClass().getResourceAsStream("random48x56.png"));
		return new Image(display, imageData);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "null";
	}

	/* (non-Javadoc)
	 * @see p3d4amb.sat.lib.shapes.Shape#getSize()
	 */
	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}

