package edu.geom3D;

import java.util.List;

import edu.geom2D.Line2D;
import edu.math.Line;
import edu.math.Matrix;
import edu.math.Vector;
import edu.math.Vector2D;

/** A sphere class. The sphere is represented by the coordinates of its center and a radius
 * @author R. Fonseca
 */
public class Sphere implements Volume{
	public Vector center;
	public float radius;
	public Sphere(Vector c, float r){
		center = c;
		radius = r;
	}
	
	public void applyMatrixIn(Matrix m) {
		m.applyToIn(center);
	}

	public boolean overlaps(Volume vol) {
		if(vol instanceof Sphere){
			return ((Sphere)vol).center.distance(center)<=((Sphere)vol).radius+radius;
		}
		
		throw new Error("Didnt implement collision check for Sphere and "+vol.getClass().getName());
	}

	public void plusIn(Vector d) {
		center.plusIn(d);
	}

	public void timesIn(float sc) {
		center.timesIn(sc);
		radius*=sc;
	}

	public float volume() {
		return (4f/3f)*(float)Math.PI*radius*radius*radius;
	}

	/** Get the coordinates of the intersection-points between a line and this sphere
	 * @param line
	 * @return An array containing either 0, 1, or 2 points depending on whether the line 
	 * intersects the sphere never, once or twice. 
	 */
	public Vector[] intersections(Line line){
		Vector l = line.getDirection().norm();
		Vector c = center.minus(line.getPoint());
		float lc = l.dot(c);
		float cc = c.dot(c);
		float rr = radius*radius;
		float tmp = lc*lc-cc+rr;
		if(tmp<0) return new Vector[0];
		else if(tmp==0) return new Vector[]{center.plus(l.times(lc))};
		else {
			float d1 = lc-(float)Math.sqrt(tmp);
			float d2 = lc+(float)Math.sqrt(tmp);
			return new Vector[]{line.getPoint().plus(l.times(d1)), line.getPoint().plus(l.times(d2))};
		}
	}
	
	/**
	 * Get the line-parameters of the intersection-points between a line and this sphere.
	 * @param line
	 * @return An array containing either 0, 1, or 2 parameters depending on whether the line 
	 * intersects the sphere never, once or twice. The parameters are relative to the line.
	 */
	public float[] intersectionParameters(Line line){
		Vector l = line.getDirection();//.norm();
		Vector c = center.minus(line.getPoint());
		float lc = l.dot(c);
		float cc = c.dot(c);
		float rr = radius*radius;
		float tmp = lc*lc-cc+rr;
		if(tmp<0) return new float[0];
		else if(tmp==0) return new float[]{lc};
		else {
			float d1 = lc-(float)Math.sqrt(tmp);
			float d2 = lc+(float)Math.sqrt(tmp);
			return new float[]{d1, d2};
		}
	}
	
	public boolean contains(Vector v){
		return center.distance(v)<radius;
	}
	
	private static Sphere createFrom(Vector v1, Vector v2){
		return new Sphere(v1.plus(v2).timesIn(0.5f), v1.distance(v2)*0.5f);
	}
	public static Sphere createFrom(Vector p0, Vector p1, Vector p2){
		Sphere s = createFrom(p0, p1);
		if(s.contains(p2)) return s;
		s = createFrom(p0, p2);
		if(s.contains(p1)) return s;
		s = createFrom(p1, p2);
		if(s.contains(p0)) return s;
		
		
		// create corner vectors
		Vector v0 = new Vector(p0);
		// create edge vectors
		Vector v01 = p0.vectorTo(p1).timesIn(0.5f);
		Vector v02 = p0.vectorTo(p2).timesIn(0.5f);
		// create mid vectors
		Vector m01 = v0.plus(v01);
		Vector m02 = v0.plus(v02);
		// create normal vector
		Vector normal = v01.cross(v02);
		// get center as the intersection of three planes: 
		// 1. through p0 with normal vector normalVector. 2. through m01 with v01 as normal vector.  3. through m02 with v02 as normal vector
		Matrix m = Matrix.createRowMatrix(v02, v02, normal);
		Vector tmp = new Vector(v01.dot(m01), v02.dot(m02), normal.dot(v0));
		Vector center = m.solve(tmp);
		float radius = p0.distance(center);
		
		return new Sphere(center, radius);
		
	}

	/*public Sphere createBoundingSphere(List<Vector> points){
			Sphere sphere = null;
			int k = 0;
			switch (points.size()) {
				case 0: sphere = new Sphere3d(points.get(0), points.get(1)); k = 2; break;
				case 1: sphere = new Sphere3d(points.get(0), boundaryPoints.get(0)); k = 1; break;
				case 2: sphere = new Sphere3d(boundaryPoints.get(0), boundaryPoints.get(1)); break;
				case 3: sphere = new Sphere3d(boundaryPoints.get(0), boundaryPoints.get(1), boundaryPoints.get(2)); break;
			}
			
			for (int i = k; i < n + boundaryPoints.getSize(); i++) {
				Point3d p = (Point3d)points.get(i);
				if (!boundaryPoints.contains(p)) {
					if (!sphere.isInside(p)) {
						if (boundaryPoints.getSize() < 3) {
							boundaryPoints.insert(p);
							sphere = getMinSphere(points, i-1, boundaryPoints);
							boundaryPoints.delete(p);
						}
						else sphere = new Sphere3d(boundaryPoints.get(0), boundaryPoints.get(1), boundaryPoints.get(2), p);
					}
				}
			}
			return sphere;
		}

	}
	*/
	
	public Vector getCenter() {
		return center.clone();
	}

	public Volume applyMatrix(Matrix m) {
		// TODO Auto-generated method stub
		return null;
	}
}
