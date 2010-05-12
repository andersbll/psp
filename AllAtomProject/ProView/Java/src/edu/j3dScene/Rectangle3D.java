package edu.j3dScene;

import java.util.LinkedList;
import java.util.List;

import javax.media.j3d.*;

import edu.math.Vector;

/** Java3D cylinder-shape centered in origo and extending 
 * along the y-axis. It is rendered using a TriangleArray 
 * and by default uses 14 triangles. 
 * @author R.Fonseca
 */
public class Rectangle3D extends Shape3D {

	/** Construct the cylinder shape. 
	 * @param height The distance between the defining points.
	 * @param radius The radius of the cylinder. 
	 * @param app Appearance of the cylinder. 
	 */
	public Rectangle3D(float width, float height, Appearance app) {
		super();

		List<Vector> verts = new LinkedList<Vector>();
		List<Vector> normals = new LinkedList<Vector>();

		verts.add(new Vector( width/2, height/2,0));
		verts.add(new Vector(-width/2, height/2,0));
		verts.add(new Vector(-width/2,-height/2,0));
                      
		verts.add(new Vector( width/2,-height/2,0));
		verts.add(new Vector( width/2, height/2,0));
		verts.add(new Vector(-width/2,-height/2,0));

		Vector z = new Vector(0,0,1);
		normals.add(z);
		normals.add(z);
		normals.add(z);
		
		normals.add(z);
		normals.add(z);
		normals.add(z);

		int i=0;
		float[] vertArr = new float[verts.size()*3];
		for(Vector v: verts){ vertArr[i++] = (float)v.x(); vertArr[i++] = (float)v.y(); vertArr[i++] = (float)v.z(); }
		i=0;
		float[] normArr = new float[normals.size()*3];
		for(Vector v: normals){ normArr[i++] = (float)v.x(); normArr[i++] = (float)v.y(); normArr[i++] = (float)v.z(); }

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