package p3d4amb.sat.lib.background;

/**
 *  represents the image done by a set of points.
 *
 * @author garganti
 */
public abstract class PointImage {
	
	/** The Constant MAX_WIDTH. */
	private final int MAX_WIDTH;
	
	/** The Constant MAX_HEIGHT. */
	private final int MAX_HEIGHT;
	
	/** The Constant MAX_NUM_POINTS. */
	//private static final int MAX_NUM_POINTS = MAX_WIDTH * MAX_HEIGHT;

	/** The threshold. */
	private static int threshold = 20;


	/** The points data. each point can have a byte value 
	 * the greater is the value, the greter is that change that it will be illuminated */ 
	byte[] pointsData;

	/**
	 * Instantiates a new point image.
	 * TODO be careful that sometimes the screen can have more than this points. For instance, nexus 6: 2560x1440
	 *
	 * @param MAX_WIDTH the max width
	 * @param MAX_HEIGHT the max height
	 */
	protected PointImage(int MAX_WIDTH, int MAX_HEIGHT){
		assert MAX_HEIGHT >0 && MAX_WIDTH > 0;
		this.MAX_WIDTH = MAX_WIDTH;
		this.MAX_HEIGHT = MAX_HEIGHT;
		final int MAX_NUM_POINTS = MAX_WIDTH * MAX_HEIGHT;
		pointsData = new byte[MAX_NUM_POINTS];
	}
	
	/**
	 * Positive.
	 *
	 * @param x the x
	 * @param y the y
	 * @return true id the point must be shown
	 */
	public boolean positive(int x, int y){
		return pointsData[y * MAX_WIDTH + x] > threshold;
	}

	/**
	 *  generate a new set of points.
	 */
	public abstract void reinit();

}
