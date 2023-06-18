package p3d4amb.sat.lib.background;

import java.util.Random;

// TODO: Auto-generated Javadoc
/**
 *  random dots.
 *
 * @author garganti
 */
public class RandomDotImage extends PointImage{
	
	/** The Constant rnd. */
	private static final Random rnd = new Random();

	/**
	 * Instantiates a new random dot image.
	 *
	 * @param MAX_WIDTH the max width
	 * @param MAX_HEIGHT the max height
	 */
	public RandomDotImage(int MAX_WIDTH, int MAX_HEIGHT){
		super(MAX_WIDTH, MAX_HEIGHT);
		reinit();
	}

	/**
	 *  generate a new set of points.
	 */
	@Override
	public void reinit() {
		rnd.nextBytes(pointsData);
	}


}
