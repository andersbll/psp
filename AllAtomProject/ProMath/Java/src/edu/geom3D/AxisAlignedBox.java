package edu.geom3D;

import edu.math.Vector;

public class AxisAlignedBox extends Box {

	public AxisAlignedBox(Vector corner, float[] extents){
		super(corner, new Vector[]{new Vector(1,0,0), new Vector(0,1,0), new Vector(0,0,1)}, extents);
	}
}
