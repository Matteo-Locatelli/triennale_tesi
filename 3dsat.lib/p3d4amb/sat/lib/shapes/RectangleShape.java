package p3d4amb.sat.lib.shapes;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

// TODO: Auto-generated Javadoc
/**
 * represents a rectangle shape to be recognized with fixed proportion of the display
 * NO LONGER USED.
 */
public class RectangleShape extends GeometricShape {
	
	/** The w. */
	int w; /** The h. */
 int h;
	 
	/* (non-Javadoc)
	 * @see p3d4amb.sat.lib.shapes.Shape#belongs(int, int)
	 */
	@Override
	public boolean belongs(int x, int y) {
		return (-(w / 6) < x && x < (w / 6) && -(h / 6) < y && y < (h / 6) );

	}

	/* (non-Javadoc)
	 * @see p3d4amb.sat.lib.shapes.Shape#getIcon(org.eclipse.swt.widgets.Display)
	 */
	@Override
	public Image getIcon(Display display) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see p3d4amb.sat.lib.shapes.Shape#resize(int)
	 */
	@Override
	public void resize(int width) {
		w = width;
		h = width;
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
