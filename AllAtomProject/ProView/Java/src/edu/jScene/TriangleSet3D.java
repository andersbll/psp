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
public class TriangleSet3D extends Shape3D {
	private final Collection<Triangle> triangles;

	public TriangleSet3D(Collection<Triangle> triangles, Appearance app) {
		super();
		this.triangles = triangles;

		
		List<Vector> verts = new LinkedList<Vector>();
		List<Vector> normals = new LinkedList<Vector>();

		for(Triangle t: triangles){
			Vector n = t.p1.vectorTo(t.p2).crossIn(t.p1.vectorTo(t.p3));
			if(n.lengthSquared()>0.000001){
				verts.add(t.p1.clone());
				verts.add(t.p2.clone());
				verts.add(t.p3.clone());
				normals.add(n);
				normals.add(n);
				normals.add(n);
			}
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