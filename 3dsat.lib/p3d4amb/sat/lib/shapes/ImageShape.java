package p3d4amb.sat.lib.shapes;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

/*! \page Immagini
*
* Le immagini sono tutte 400x400. La densità dei pixel è controllata (tra .45 e .7) sopratuutto tra di loro. Esistono i seguenti set di immagini:
* 
* 		LANG("langimages","bird","car","cat","circle","man","star"), 
		LEA("leaimages","apple","circle","house","square"), 
		LETTERS ("letters","letterA","letterC","letterE","letterK","letterM","letterZ"),
		LEA_CONTORNO("leaimages","apple","circle","house","square"), 
		PACMAN("pacman","pacmanD","pacmanL","pacmanR","pacmanU"), 
		TNO("tno","circle","square","star","triangle");
*/


/**
 *  shape che rappresentano immagini.
 */
public class ImageShape extends Shape {

	/** The Constant SEPARATOR. */
	private static final char SEPARATOR = '/'; // NON USARE File.separator;

	/** The Constant logger. */
	private static final Logger logger = Logger
			.getLogger(ImageShape.class);

	/** The Constant IMG_DATA_SIZE. */
	// resize at 300
	private static final int IMG_DATA_SIZE = 300;

	/** The Constant CENTER_X. */
	private static final int CENTER_X = IMG_DATA_SIZE / 2;

	/** The Constant CENTER_Y. */
	private static final int CENTER_Y = IMG_DATA_SIZE / 2;

	/** The image data. */
	private ImageData imageData;

	/** The Constant ICON_WIDTH. */
	// icons size
	private static final int ICON_WIDTH = 48;

	/** The Constant ICON_HIGTH. */
	private static final int ICON_HIGTH = 56;

	// size to show /IMG_DATA_SIZE
	/** The ratio. */
	// ratio = 2: show double
	private double ratio = 1;

	/** The name. */
	private String name;

	/** The size. */
	// current size
	private int size = IMG_DATA_SIZE;

	/** The belongs. */
	// utilizzando le coordinate cartesiane con origine l'upper left
	private boolean[][] belongs;

	/**
	 * Instantiates a new image shape.
	 *
	 * @param imageName the image name
	 */
	ImageShape(String imageName) {
		logger.debug("loading shape " +imageName);
		// load the image data
		InputStream inputImageStream = getClass().getResourceAsStream(imageName);
		if (inputImageStream == null){
			throw new RuntimeException("image " + imageName + " not found");
		}
		imageData = new ImageData(inputImageStream);
		// setup belongs
		belongs = new boolean[IMG_DATA_SIZE][IMG_DATA_SIZE];
		ImageData scaledImage = imageData
				.scaledTo(IMG_DATA_SIZE, IMG_DATA_SIZE);
		for (int x = 0; x < IMG_DATA_SIZE; x++) {
			for (int y = 0; y < IMG_DATA_SIZE; y++) {
				belongs[x][y] = (scaledImage.getAlpha(x, y) != 0);
			}
		}
		// get the name
		name = imageName.substring(0, imageName.indexOf("."));
	}

	/* (non-Javadoc)
	 * @see p3d4amb.sat.lib.shapes.Shape#belongs(int, int)
	 */
	@Override
	public boolean belongs(int x, int y) {
		// trasformo le coordinate dall'origine all'upper left corner
		// 0 -> + IMG_DATA_SIZE/2
		// IMG_DATA_SIZE/2 -> IMG_DATA_SIZE
		// - IMG_DATA_SIZE/2 -> 0
		int deltaX = (int) (x / ratio) + CENTER_X;
		int deltaY = (int) (y / ratio) + CENTER_Y;
		if (deltaX >= IMG_DATA_SIZE || deltaY >= IMG_DATA_SIZE || deltaX < 0
				|| deltaY < 0)
			return false;
		return belongs[deltaX][deltaY];
	}

	/* (non-Javadoc)
	 * @see p3d4amb.sat.lib.shapes.Shape#resize(int)
	 */
	@Override
	public void resize(int size) {
		this.ratio = ((double) size) / IMG_DATA_SIZE;
		this.size = size;
	}

	/* (non-Javadoc)
	 * @see p3d4amb.sat.lib.shapes.Shape#getSize()
	 */
	@Override
	public int getSize() {
		return size;
	}

	/* (non-Javadoc)
	 * @see p3d4amb.sat.lib.shapes.Shape#getIcon(org.eclipse.swt.widgets.Display)
	 */
	@Override
	public Image getIcon(Display display) {
		return new Image(display, imageData.scaledTo(ICON_WIDTH, ICON_HIGTH));
	}

	/**
	 * The Enum ImageSet.
	 */
	public enum ImageSet {
		
		/** The lang. */
		LANG("langimages","bird","car","cat","circle","man","star"), 
		
		/** The lea. */
		LEA("leaimages","apple","circle","house","square"), 
		
		/** The letters. */
		LETTERS ("letters","letterA","letterC","letterE","letterK","letterM","letterZ"),
		
		/** The lea contorno. */
		LEA_CONTORNO("leaimages_contour","apple","circle","house","square"), 
		
		/** The pacman. */
		PACMAN("pacman","pacmanD","pacmanL","pacmanR","pacmanU"), 
		
		/** The tno. */
		TNO("tno","circle","square","star","triangle");
		
		/** The subdir. */
		String subdir;
		
		/** The images. */
		String[] images;
		
		/**
		 * Instantiates a new image set.
		 *
		 * @param p the p
		 * @param images the images
		 */
		ImageSet(String p, String ... images){
			subdir = p;
			this.images = images;
		}
	}

	/**
	 *  images similar to LANG TEST *.
	 *
	 * @param is the is
	 * @return the shapes
	 */
	static public List<ImageShape> getShapes(ImageSet is) {
		List<ImageShape> shapes = new ArrayList<>();
		// funzionerebbe solo se non è un JAR!!!
		/*URL dir  = ImageShape.class.getResource(path);
		File dirF = new File(dir.getFile());
		for(String f: dirF.list(new FilenameFilter() {
			
			@Override
			public boolean accept(File arg0, String arg1) {
				return arg1.endsWith(".png");
			}
		})){
			// do not use file name
			logger.debug("loading " + f + " in " + dir);
			System.out.println(f);			
		}*/
		for(String i : is.images){
			shapes.add(new ImageShape(is.subdir + SEPARATOR + i + ".png"));
		}
		return shapes;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}

}
