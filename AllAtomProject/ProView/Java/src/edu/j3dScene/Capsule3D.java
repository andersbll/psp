package edu.j3dScene;

import java.util.LinkedList;
import java.util.List;
import static java.lang.Math.sin;
import static java.lang.Math.cos;
import static java.lang.Math.PI;

import javax.media.j3d.*;

import edu.math.Vector;

/** Java3D capsule-shape (also known as line-swept-sphere) centered in origo and extending 
 * along the y-axis. It is rendered using a TriangleArray and by default uses 210 triangles. 
 * @author R.Fonseca
 */
public class Capsule3D extends Shape3D {
	private int divisions = 7;

	/** Construct the capsule shape. 
	 * @param height The distance between the defining points. Notice that the actual height 
	 * of the capsule will be <code>height+2*radius</code>
	 * @param radius The radius of the capsule. 
	 * @param app Appearance of the capsule. 
	 */
	public Capsule3D(float height, float radius, Appearance app) {
		super();

		List<Vector> verts = new LinkedList<Vector>();
		List<Vector> normals = new LinkedList<Vector>();

		//Upper half-sphere
		Vector c = new Vector(0,height/2,0);
		float r = radius;
		double d = PI/divisions;

		for(double theta=0;theta<PI;theta+=d){
			for(double phi=0;phi<PI;phi+=d){
				verts.add(new Vector(  c.x() + r*sin(theta  )*cos(phi  )  ,  c.y() + r*sin(theta  )*sin(phi  )  ,  c.z() + r*cos(theta  )  ));
				verts.add(new Vector(  c.x() + r*sin(theta+d)*cos(phi  )  ,  c.y() + r*sin(theta+d)*sin(phi  )  ,  c.z() + r*cos(theta+d)  ));
				verts.add(new Vector(  c.x() + r*sin(theta  )*cos(phi+d)  ,  c.y() + r*sin(theta  )*sin(phi+d)  ,  c.z() + r*cos(theta  )  ));

				verts.add(new Vector(  c.x() + r*sin(theta+d)*cos(phi+d)  ,  c.y() + r*sin(theta+d)*sin(phi+d)  ,  c.z() + r*cos(theta+d)  ));
				verts.add(new Vector(  c.x() + r*sin(theta  )*cos(phi+d)  ,  c.y() + r*sin(theta  )*sin(phi+d)  ,  c.z() + r*cos(theta  )  ));
				verts.add(new Vector(  c.x() + r*sin(theta+d)*cos(phi  )  ,  c.y() + r*sin(theta+d)*sin(phi  )  ,  c.z() + r*cos(theta+d)  ));
				
				normals.add(new Vector(  sin(theta  )*cos(phi  )  ,  sin(theta  )*sin(phi  )  ,  cos(theta  )  ));
				normals.add(new Vector(  sin(theta+d)*cos(phi  )  ,  sin(theta+d)*sin(phi  )  ,  cos(theta+d)  ));
				normals.add(new Vector(  sin(theta  )*cos(phi+d)  ,  sin(theta  )*sin(phi+d)  ,  cos(theta  )  ));

				normals.add(new Vector(  sin(theta+d)*cos(phi+d)  ,  sin(theta+d)*sin(phi+d)  ,  cos(theta+d)  ));
				normals.add(new Vector(  sin(theta  )*cos(phi+d)  ,  sin(theta  )*sin(phi+d)  ,  cos(theta  )  ));
				normals.add(new Vector(  sin(theta+d)*cos(phi  )  ,  sin(theta+d)*sin(phi  )  ,  cos(theta+d)  ));
				//normals.add(c.vectorTo(new Vector(sin(theta+d/2)*cos(phi+d/2)  ,  c.y() + r*sin(theta+d/2)*sin(phi+d/2)  ,  c.z() + r*cos(theta+d/2))));
			}
		}
		//Lower half-sphere
		c = new Vector(0,-height/2,0);
		for(double theta=0;theta<PI;theta+=d){
			for(double phi=PI;phi<PI*2;phi+=d){
				verts.add(new Vector(  c.x() + r*sin(theta  )*cos(phi  )  ,  c.y() + r*sin(theta  )*sin(phi  )  ,  c.z() + r*cos(theta  )  ));
				verts.add(new Vector(  c.x() + r*sin(theta+d)*cos(phi  )  ,  c.y() + r*sin(theta+d)*sin(phi  )  ,  c.z() + r*cos(theta+d)  ));
				verts.add(new Vector(  c.x() + r*sin(theta  )*cos(phi+d)  ,  c.y() + r*sin(theta  )*sin(phi+d)  ,  c.z() + r*cos(theta  )  ));

				verts.add(new Vector(  c.x() + r*sin(theta+d)*cos(phi+d)  ,  c.y() + r*sin(theta+d)*sin(phi+d)  ,  c.z() + r*cos(theta+d)  ));
				verts.add(new Vector(  c.x() + r*sin(theta  )*cos(phi+d)  ,  c.y() + r*sin(theta  )*sin(phi+d)  ,  c.z() + r*cos(theta  )  ));
				verts.add(new Vector(  c.x() + r*sin(theta+d)*cos(phi  )  ,  c.y() + r*sin(theta+d)*sin(phi  )  ,  c.z() + r*cos(theta+d)  ));
				
				normals.add(new Vector(  sin(theta  )*cos(phi  )  ,  sin(theta  )*sin(phi  )  ,  cos(theta  )  ));
				normals.add(new Vector(  sin(theta+d)*cos(phi  )  ,  sin(theta+d)*sin(phi  )  ,  cos(theta+d)  ));
				normals.add(new Vector(  sin(theta  )*cos(phi+d)  ,  sin(theta  )*sin(phi+d)  ,  cos(theta  )  ));

				normals.add(new Vector(  sin(theta+d)*cos(phi+d)  ,  sin(theta+d)*sin(phi+d)  ,  cos(theta+d)  ));
				normals.add(new Vector(  sin(theta  )*cos(phi+d)  ,  sin(theta  )*sin(phi+d)  ,  cos(theta  )  ));
				normals.add(new Vector(  sin(theta+d)*cos(phi  )  ,  sin(theta+d)*sin(phi  )  ,  cos(theta+d)  ));
				//normals.add(c.vectorTo(new Vector(sin(theta+d/2)*cos(phi+d/2)  ,  c.y() + r*sin(theta+d/2)*sin(phi+d/2)  ,  c.z() + r*cos(theta+d/2))));
			}
		}

		//Cylinder
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