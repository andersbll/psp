package edu.jScene;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import static java.lang.Math.sin;
import static java.lang.Math.cos;
import static java.lang.Math.PI;

import javax.media.j3d.*;

import edu.geom3D.Triangle;
import edu.math.Vector;

/** 
 * @author R.Fonseca
 */
public class Triangle3D extends Shape3D {

	public Triangle3D(float size, Appearance app) {
		super();


		List<Vector> verts = new LinkedList<Vector>();
		List<Vector> normals = new LinkedList<Vector>();

		float[] vertArr = {0,0,0,  size,0,0,  0,size,0};//new float[verts.size()*3];
		float[] normArr = {0,0,1,  0,0,1,  0,0,1};//new float[normals.size()*3];

		TriangleArray caps = new TriangleArray(3, TriangleArray.COORDINATES | TriangleArray.NORMALS);

		caps.setCoordinates(0, vertArr);
		caps.setNormals(0, normArr);

		caps.setCapability(Geometry.ALLOW_INTERSECT);
		setGeometry(caps);
		if(app==null)
			setAppearance(new Appearance());
		else
			setAppearance(app);
	}



}