package p3d4amb.sat.lib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Random;

import org.apache.log4j.Logger;

import p3d4amb.sat.lib.background.PointImage;
import p3d4amb.sat.lib.background.RandomDotImage;
import p3d4amb.sat.lib.background.StripesImage;
import p3d4amb.sat.lib.session.DepthCertBase.CertifierStatus;
import p3d4amb.sat.lib.session.DepthCertBase;
import p3d4amb.sat.lib.session.TestSession;
import p3d4amb.sat.lib.session.TestSession.SingleAnswer;
import p3d4amb.sat.lib.shapes.ImageShape;
import p3d4amb.sat.lib.shapes.ImageShape.ImageSet;
import p3d4amb.sat.lib.shapes.NullShape;
import p3d4amb.sat.lib.shapes.Shape;
import p3d4amb.sat.lib.shapes.ShapeSize;

/**
 * main class to build all the data useful for the stereo acuity test.
 * It's observable regarding its state (demo mode for example)
 */
public class SATTest extends Observable implements PointsProvider{

	/** The Constant rnd. */
	private static final Random rnd = new Random();

	/** The Constant logger. */
	private static final Logger logger = Logger.getLogger(SATTest.class); 

	private MonitorData monitorData;

	/** The shapes. */
	// the shapes for this experiment
	public List<Shape> shapes;
	
	/** required shape size*/
	private ShapeSize shapeSize;

	/** test session */
	private TestSession session;
	
	
	/** The indemomode. */
	// it is in demo mode - do not count and do not change shape and pos
	private boolean indemomode; 


	/**
	 * Instantiates a new SAT test.
	 *
	 * @param initialDepth            the initial depth
	 * @param imageSet            the image set
	 * @param includeNullShape            the include null shape
	 * @param monitorData the monitor data
	 * @param ss the ss
	 * @param certifierCLass the certifier C lass
	 */
	SATTest(int initialDepth, ImageSet imageSet, boolean includeNullShape, MonitorData monitorData, ShapeSize ss,Class<? extends DepthCertBase> certifierCLass) {
		this(new SATTestConfiguration(initialDepth, ImageShape.getShapes(imageSet), includeNullShape, monitorData, ss,certifierCLass));
	}
	
	protected SATTest(SATTestConfiguration configuration) {
		// set the set of shapes:
		shapes = new ArrayList<>(configuration.imageSet);
		// add the null shape
		if (configuration.includeNullShape)
			shapes.add(new NullShape());
		this.monitorData = configuration.monitorData;
		this.shapeSize = configuration.ss;
		this.session = new TestSession(configuration.initialDepth, configuration.certifierCLass);
		// demo enabled?
		indemomode = true;
		int mw = configuration.monitorData.monitorWidthPixels;
		int mh = configuration.monitorData.monitorHeightPixels;
		// points:
		pointsData = usestripes ? new StripesImage(mw,mh): new RandomDotImage(mw,mh);
	}

	
	/**
	 *  return the current depth (as established by the certifier) - be careful the certifier may have decided to stop.
	 *
	 * @return the current depth
	 */
	public int getCurrentDepth() {
		return session.getCurrentDepth();
	}

	/**
	 * Gets the current status.
	 *
	 * @return the current status
	 */
	public CertifierStatus getCurrentStatus() {
		return session.getCurrentStatus();
	}

	/**
	 * The Enum ChoiceResult.
	 */
	public enum ChoiceResult {

		/** The skip. */
		SKIP,
		/** The right. */
		RIGHT,
		/** The wrong. */
		WRONG,
		/** The demo. */
		DEMO
	}


	/** the current shape shown in the 3d stereo test*/
	private Shape currentShape;

	/** The current pos. */
	private Position currentPos;

	/** The change position. */
	private boolean changePosition;
	
	/** the shape as pointsData */
	private PointImage pointsData;

	/**
	 * check the solution and compute the next depth unless in demo: in that
	 * case the solution is not counted.
	 *
	 * @param s
	 *            the s can be null (treated as wrong) - (except the return is
	 *            skip) : it corresponds to skip by the user
	 * @return the choice result
	 */
	public ChoiceResult solutionChosen(Shape s) {
		return solutionChosen(s,0);
	}
	
	/**
	 * check the solution and compute the next depth unless in demo: in that
	 * case the solution is not counted.
	 *
	 * @param s            the s can be null (treated as wrong) - (except the return is
	 *            skip) : it corresponds to skip by the user
	 * @param timetaken the timetaken to give this answer (inmilliseconds)
	 * @return the choice result
	 */
	public ChoiceResult solutionChosen(Shape s, long timetaken) {
		// store the current depth (it will be modified when computing the
		// result)
		if (indemomode) return ChoiceResult.DEMO;
		// if not demomode		
		int savedDepth = session.getCurrentDepth();
		ChoiceResult res  = session.solutionChosen(s,currentShape,timetaken,depthAngle(savedDepth));
		logger.info("immagine: " + s + ", pixels: " + savedDepth + ", angolo: " + depthAngle(savedDepth) + ", result: "
				+ res);
		return res;
	}

	/**
	 * check solution (position) and set new depth unless in demo: in that case
	 * the solution is not counted.
	 *
	 * @param p
	 *            the p
	 * @return the choice result
	 */
	public ChoiceResult positionChosen(Position p) {
		if (indemomode) return ChoiceResult.DEMO;
		// if not demo
		ChoiceResult res = session.positionChosen(p,currentPos);
		logger.info(currentShape + ", " + session.getCurrentDepth() + " " + res + " POSITION ");
		return res;
	}

	/**
	 *compute the depth from pixel to angle
	 *
	 * @param pixeltransl
	 *            the pixeltransl
	 * @return the double angle in seconds of  di grado
	 */
	public double depthAngle(int pixeltransl) {
		// get the monitor size
		double ms = monitorData.monitorSize10thInc / 10.0;
		if (ms == 0)
			return 0;
		double alphaSec = getAngleSec(pixeltransl, monitorData.monitorWidthPixels, monitorData.monitorWidthMM,
				monitorData.monitorDistance);
		return alphaSec;
	}
	
	/**
	 *  return true if the test is not finished.
	 *
	 * @return true, if successful
	 */
	public boolean hasNextShape(){
		return getCurrentStatus().currentResult ==  TestSession.Result.CONTINUE;		
	}

	

	/** set the next shape and position and size if required */
	public void setNextShape(){
		assert getCurrentStatus().currentResult == TestSession.Result.CONTINUE;
		// position
		if (changePosition) {
			int nextPosInt = rnd.nextInt(Position.values().length);
			currentPos = Position.values()[nextPosInt];
		} else {
			currentPos = Position.CENTER;
		}
		// shape
		currentShape = shapes.get(rnd.nextInt(shapes.size()));
		// set the size
		int nextSize = getCurrentPxSize();
		setShapeSize(nextSize);
		// change the background
		pointsData.reinit();
		// change the color if in demo mode
		if (indemomode){
			if (colorShapeIntensity > 60) {
				colorShapeIntensity -= 20;
			} else if (colorShapeIntensity > 0) {
				colorShapeIntensity -= 10;
			} else{
				colorShapeIntensity = 0;
				// exitsdemo mode
				exitDemoMode();
			}
		}
		// log some messages
		logger.debug("shape " + currentShape + " position " + currentPos + " size in pxls [" + shape_width + " x " + shape_height + "]");
	}
	

	/**
	 * return the current shape 
	 *
	 * @return the next shape
	 */
	public Shape getCurrentShape() {
		return currentShape;		
	}
	
	
	/**
	 * Gets the angle sec.
	 *
	 * @param pixeltransl
	 *            number of pixels of translation
	 * @param pixelWidth
	 *            the pixel width of the monitor
	 * @param monitorWidthMM
	 *            monitor width in mm
	 * @param monitorDistance
	 *            in centimeters
	 * @return the angle sec di grado
	 */
	public static double getAngleSec(int pixeltransl, int pixelWidth, int monitorWidthMM, int monitorDistance) {
		// get number of pixels in diagonal
		// double npd =
		// Math.sqrt(monitorWidthMM.getHeight()*monitorWidthMM.getHeight() +
		// monitorWidthMM.getWidth()*monitorWidthMM.getWidth());
		// get the size of a pixel in mm = diagonal/ number of pixels
		// double ps = (ms * 25.4) / npd;
		// get the deltaLayers in mm
		double deltaMM = pixeltransl * monitorWidthMM / (double) pixelWidth;
		// distance is default 40 cm
		double distanceMM = monitorDistance * 10;
		// compute angle
		// atan2(y, x) is the angle in radians between the positive x-axis of a
		// plane and the point given by the coordinates (x, y) on it.
		double alpha = Math.atan2(deltaMM, distanceMM);
		// convert to degree gardes in seconds
		double alphaSec = Math.toDegrees(alpha) * 3600;
		return alphaSec;
	}

	
	/** returns the shapes the user must choose from */
	public List<Shape> getShapes() {
		return shapes;
	}

	static public enum PointType {
		//
		OFF, LEFT, RIGHT, BOTH,
		// this is iseful if one wants to color somehow a point (during demo mode for example)
		LEFT_COLORED;

	}
	/** if the shape must be colored (during demo mode for example)
	 * it can be between 0 and 100 - 0 no color, 100 full color*/
	static private int colorShapeIntensity = 0; 

	/** traslazione tra i due piani */
	static protected int deltaLayers = 10;

	/** The Constant BORDER_DISTANCE. */
	private static final int BORDER_DISTANCE = 10;

	/** The center x. */
	protected int centerX;

	/** The center y. */
	protected int centerY;

	/**
	 * Sets the position.
	 *
	 * @param p
	 *            the new position
	 */
	// position in which the shape is placed
	private final void centerShape(int width, int height) {
		assert currentPos != null;
		// first set the X
		switch (currentPos) {
		case NORTH_EAST:
		case SOUTH_EAST:
			centerX = shape_width / 2 + BORDER_DISTANCE - session.getCurrentDepth();
			break;
		case CENTER:
			centerX = width / 2;
			break;
		default:
			// case NORTH_WEST:
			// case SOUTH_WEST:
			centerX = width - (shape_width / 2) - BORDER_DISTANCE - session.getCurrentDepth();
		}
		// set the y
		switch (currentPos) {
		case NORTH_EAST:
		case NORTH_WEST:
			centerY = shape_height / 2 + BORDER_DISTANCE;
			break;
		case CENTER:
			centerY = height / 2;
			break;
		default:
			// case SOUTH_EAST:
			// case SOUTH_WEST:
			centerY = height - (shape_height / 2) - BORDER_DISTANCE;
		}
		logger.debug("set position " + currentPos + " centerX=" + centerX + " centerY=" + centerY);
	}

	// the desired area for the shape
	/** The shape_width. */
	// since it can be moved
	private static int shape_width;

	/** The shape_height. */
	private static int shape_height;

	/** use strips (useful for testing) instead of dots - not in the constructor to leave it simple */
	static private boolean usestripes = false;

	/**
	 * Sets the shape size.
	 *
	 * @param ss
	 *            shape size in pixels
	 */
	private void setShapeSize(int ss) {
		currentShape.resize(ss);
		shape_width = ss;
		shape_height = ss;
	}

	/**
	 * Sets the use stripes.
	 *
	 * @param usestripes the new use stripes
	 */
	static public void setUseStripes(boolean us) {
		usestripes= us;		
	}

	/**
	 * Checks if is indemomode.
	 *
	 * @return true, if is indemomode
	 */
	public boolean isIndemomode() {
		return indemomode;
	}

	/**
	 * Exit demo mode.
	 */
	public void exitDemoMode() {
		assert indemomode == true;
		logger.info("Existing demo mode - starting real test");
		this.indemomode = false;
		// notify the observers!
		setChanged();
		notifyObservers();
	}


	
	
	/**
	 * Build the point to visualize
	 * @param width
	 *            of the image
	 * @param height
	 *            of the image
	 * @return
	 */
	@Override
	public Points getPoints(int width, int height) {
		// set the position (it can change)
		centerShape(width, height);
		return getPointFromShape(width, height,centerX, centerY,session.getCurrentDepth(), currentShape, pointsData);
	}

	/**
	 * 
	 * @param width larghezza dell'immagine da creare
	 * @param height altezza
	 * @param centerX centro della shape
	 * @param centerY centro sella shape
	 * @param currentDepth   disparità corrente  
	 * @param currentShape   la shape corrente da visualizzare
	 * @param pointsData l'immagine di sfondo da cui partire (randomdot - oppure stipes per debug)
	 * @return
	 */
	static public Points getPointFromShape(int width, int height, int centerX, int centerY, int currentDepth, Shape currentShape, PointImage pointsData) {
		Points result = new Points(width,height,colorShapeIntensity);
		// dive the translation between the two
		int depthTranslationX = currentDepth/2;
		int depthTranslationY = currentDepth - depthTranslationX;
		// init points
		// TODO optimization: init to OFF only those that ones that will not on
		for (int x = 0 ; x < width ; x++) {
			for (int y = 0; y < height; y++) 
				result.points[x][y] = PointType.OFF;
		}
		// in only one pass (every point is first decided if left and then eventually right)
		for (int x = 0 ; x < width; x++) {
			for (int y = 0; y < height; y++) {
				// se devo muoverlo
				// passa le relative riesptto il centro
				boolean toMove = currentShape.belongs(x - centerX, y - centerY);
				// se entra nella parte che è stata mossa
				boolean movedAreaX = currentShape.belongs(x - centerX - depthTranslationX, y - centerY);
				// se entra nella parte che è stata mossa 
				boolean movedAreaY = currentShape.belongs(x - centerX + depthTranslationY, y - centerY);
				if (pointsData.positive(x, y)) {
					if (toMove) {
						// appartiene alla shape: transla di deltaLayers + depth
						if (x + deltaLayers + depthTranslationX < width)
							result.points[x + deltaLayers + depthTranslationX][y]= colorShapeIntensity > 0 ? PointType.LEFT_COLORED : PointType.LEFT;
						// right (if already left put both) - a point is always first set to LEFT because of the 
						// layer translation						
						if (x - depthTranslationY > 0)
							result.points[x - depthTranslationY][y]= result.points[x - depthTranslationY][y] == PointType.OFF ? PointType.RIGHT : PointType.BOTH;
					}
					// disegna comunque
					if (!movedAreaX && x + deltaLayers < width) {
						// transla solo di deltaLayers (per non permettere il
						// riconoscimento delle immagini)
						result.points[x + deltaLayers][y] = PointType.LEFT;
					}
					if (!movedAreaY) {
						// TODO and if colored???
						result.points[x][y] = result.points[x][y] == PointType.OFF ? PointType.RIGHT : PointType.BOTH;
					}
				}
			}
		}
		return result;
	}


	/**
	 * Gets the next size.
	 *
	 * @return the size in pixels
	 */
	public int getCurrentPxSize() {
		// get the dimension in millimeters
		int dimMM = shapeSize.size(depthAngle(getCurrentDepth()));
		// in pixels:
		return  (monitorData.monitorWidthPixels * dimMM)/monitorData.monitorWidthMM;
	}
	
	/**
	 * Sets the color shape.
	 *
	 * @param b
	 *            the new color shape: between 0 and 100 
	 */
	public static void setColorShape(int b) {
		//assert b 
		colorShapeIntensity = b;
	}

	/**
	 * 
	 * @return the results of the tests
	 */
	public List<String> getSessionResults(){
		return session.getSessionResults();
	}
	
	
	/**
	 * 
	 * @return the results of the tests
	 */
	public List<SingleAnswer> getSessionAnswers(){
		return session.getSessionAnswers();
	}

	
}