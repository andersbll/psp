package edu.geom3D;

import edu.math.Matrix;
import edu.math.Vector;

/**
 * An interface for bounding volumes. 
 * @author ras
 */
public interface Volume extends Shape{
	
	public boolean overlaps(Volume vol);
	public float volume();

	public void timesIn(float sc);

	public void applyMatrixIn(Matrix m);
	public Volume applyMatrix(Matrix m);

	public void plusIn(Vector d);
}
