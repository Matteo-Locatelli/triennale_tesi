package p3d4amb.sat.lib.shapes;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;


// TODO: Auto-generated Javadoc
/**
 *  
 * represents a shape to be recognized.
 */
public abstract class Shape {
	
	
	
	/**
	 *  dice se il punto appartiene alla shape oppure è fuori.
	 *
	 * @param x la x del punto,  a partire dall'origine
	 * x = 0, y = 0 sarebbe il punto centrale della shape
	 * @param y the y
	 * @return true, if successful
	 */
	public abstract boolean belongs(int x, int y);

	/**
	 *  resize the picture.
	 *
	 * @param size the size
	 */
	public abstract void resize(int size);

	/**
	 *  
	 *
	 * @param display the display
	 * @return the icon representing the shape
	 */
	public abstract Image getIcon(Display display);

	/**
	 *  get current size.
	 *
	 * @return the size
	 */
	public abstract int getSize();
}
