package edu.j3dScene;

import java.util.LinkedList;
import java.util.List;
import static java.lang.Math.sin;
import static java.lang.Math.cos;
import static java.lang.Math.PI;

import javax.media.j3d.*;

import edu.math.Vector;

/** Java3D cylinder-shape centered in origo and extending 
 * along the y-axis. It is rendered using a TriangleArray 
 * and by default uses 14 triangles. 
 * @author R.Fonseca
 */
public class HollowCylinder3D extends Shape3D {
	private int divisions = 14;

	/** Construct the cylinder shape. 
	 * @param height The distance between the defining points.
	 * @param radius The radius of the cylinder. 
	 * @param app Appearance of the cylinder. 
	 */
	public HollowCylinder3D(float height, float radius, Appearance app) {
		super();

		List<Vector> verts = new LinkedList<Vector>();
		List<Vector> normals = new LinkedList<Vector>();

		float r = radius;
		double d = PI/divisions;

		double dH = height;
		for(double theta=0;theta<PI*2;theta+=d){
			double y=-height/2;
			verts.add(new Vector(  r*sin(theta  )  , y   , r*cos(theta  )  ));
			verts.add(new Vector(  r*sin(theta+d)  , y   , r*cos(theta+d)  ));
			verts.add(new Vector(  r*sin(theta  )  , y+dH, r*cos(theta  )  ));
			verts.add(new Vector(  r*sin(theta+d)  , y+dH, r*cos(theta+d)  ));
			verts.add(new Vector(  r*sin(theta  )  , y+dH, r*cos(theta  )  ));
			verts.add(new Vector(  r*sin(theta+d)  , y   , r*cos(theta+d)  ));
			normals.add(new Vector(  sin(theta  )  , 0 , cos(theta  )  ));
			normals.add(new Vector(  sin(theta+d)  , 0 , cos(theta+d)  ));
			normals.add(new Vector(  sin(theta  )  , 0 , cos(theta  )  ));
			normals.add(new Vector(  sin(theta+d)  , 0 , cos(theta+d)  ));
			normals.add(new Vector(  sin(theta  )  , 0 , cos(theta  )  ));
			normals.add(new Vector(  sin(theta+d)  , 0 , cos(theta+d)  ));
		}

		int i=0;
		float[] vertArr = new float[verts.size()*3];
		for(Vector v: verts){ vertArr[i++] = v.x(); vertArr[i++] = v.y(); vertArr[i++] = v.z(); }
		i=0;
		float[] normArr = new float[normals.size()*3];
		for(Vector v: normals){ normArr[i++] = v.x(); normArr[i++] = v.y(); normArr[i++] = v.z(); }

		TriangleArray caps = new TriangleArray(vertArr.length/3, TriangleArray.COORDINATES | TriangleArray.NORMALS);

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