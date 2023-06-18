package p3d4amb.sat.lib.background;

/**
 *  immagine composta da strisce.
 *
 * @author garganti
 */
public class StripesImage extends PointImage {

	/** The Constant STRIP_WIDTH. */
	private static final int STRIP_WIDTH = 1;
	
	/** The Constant EVERY_POINTS. */
	private static final int EVERY_POINTS = 8;

	private static final boolean WHITE_STRIPES = true;

	/**
	 * Instantiates a new stripes image.
	 */
	public StripesImage(int MAX_WIDTH, int MAX_HEIGHT) {
		super(MAX_WIDTH, MAX_HEIGHT);
		for (int x = 0; x < MAX_WIDTH; x++) {
			for (int y = 0; y < MAX_HEIGHT; y++) {
				if (x % EVERY_POINTS < STRIP_WIDTH) {
					pointsData[y * MAX_WIDTH + x] = WHITE_STRIPES ? Byte.MAX_VALUE :Byte.MIN_VALUE;
				} else {
					pointsData[y * MAX_WIDTH + x] = WHITE_STRIPES ? Byte.MIN_VALUE : Byte.MAX_VALUE;
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see p3d4amb.sat.lib.background.PointImage#reinit()
	 */
	@Override
	public void reinit() {
		// do nothing (no change in stripes)
	}

}
