package p3d4amb.sat.lib;

import java.util.List;

import p3d4amb.sat.lib.session.DepthCertBase;
import p3d4amb.sat.lib.shapes.ImageShape;
import p3d4amb.sat.lib.shapes.ImageShape.ImageSet;
import p3d4amb.sat.lib.shapes.Shape;
import p3d4amb.sat.lib.shapes.ShapeSize;

public class SATTestConfiguration {
	@SuppressWarnings("ucd")
	public int initialDepth;
	List<? extends Shape> imageSet;
	@SuppressWarnings("ucd")
	public boolean includeNullShape;
	@SuppressWarnings("ucd")
	public MonitorData monitorData;
	public ShapeSize ss;
	@SuppressWarnings("ucd")
	Class<? extends DepthCertBase> certifierCLass;
	
	public boolean initInDemoMode = true;

	public SATTestConfiguration(int initialDepth, ImageSet imageSet, boolean includeNullShape,
			MonitorData monitorData, ShapeSize ss, Class<? extends DepthCertBase> certifierCLass) {
		this(initialDepth, ImageShape.getShapes(imageSet), includeNullShape,
				monitorData, ss,certifierCLass);		
	}

	/**
	 * Instantiates a new SAT test configuration.
	 *
	 * @param initialDepth the initial depth
	 * @param imageSet the image set
	 * @param includeNullShape the include null shape
	 * @param monitorData the monitor data
	 * @param ss the ss
	 * @param certifierCLass the certifier C lass
	 */
	public SATTestConfiguration(int initialDepth, List<? extends Shape> imageSet, boolean includeNullShape,
			MonitorData monitorData, ShapeSize ss, Class<? extends DepthCertBase> certifierCLass) {
		this.initialDepth = initialDepth;
		this.imageSet = imageSet;
		this.includeNullShape = includeNullShape;
		this.monitorData = monitorData;
		this.ss = ss;
		this.certifierCLass = certifierCLass;
	}


	public void setImageSet(List<? extends Shape> imageSet) {
		this.imageSet = imageSet;
	}

	public void setIncludeNullShape(boolean includeNullShape) {
		this.includeNullShape = includeNullShape;
	}

	public void setMonitorData(MonitorData monitorData) {
		this.monitorData = monitorData;
	}

	public void setSs(ShapeSize ss) {
		this.ss = ss;
	}
}