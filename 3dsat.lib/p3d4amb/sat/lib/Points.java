package p3d4amb.sat.lib;

import p3d4amb.sat.lib.SATTest.PointType;

/** represents the points to be diplayed. The are compute by the point provider */
public class Points {
	
	public int colorIntensity = 100; // between 0 and 100 
	
	public PointType[][] points;
	
	/**
	 * Instantiates a new points.
	 *
	 * @param width the width
	 * @param height the height
	 * @param colorhintintensity the colorhintintensity beween 0 and 100
	 */
	public Points(int width, int height, int colorhintintensity) {
		points = new PointType[width][height];
		colorIntensity = colorhintintensity;
	}

	
}
