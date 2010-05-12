package edu.geom3D;

import java.util.Arrays;
import java.util.List;

import edu.math.Matrix;
import edu.math.ParametricPlane;
import edu.math.Vector;

/** 
 * Implementation of a Rectangular Swept Sphere that supports overlap check and construction from 
 * a point-set and two RSS'.
 * @author Rasmus
 */
public class RSS implements Volume{
	public Rectangle rectangle;
	public float radius;

	public RSS(Vector center, Vector[] bases, float radius){
		this.rectangle = new Rectangle(center, bases);
		this.radius = radius;
	}

	public boolean overlaps(Volume vol) {
		if(vol instanceof RSS) return overlaps((RSS)vol);
		throw new Error("Unimplemented ("+vol.getClass().getSimpleName()+")");
	}

	/**
	 * 128HOps 
	 */
	public boolean overlaps(RSS rss){
		float sqRads= (radius+rss.radius); sqRads = sqRads*sqRads; //1HOp
		float sqCenterDist = rectangle.center.distanceSquared(rss.rectangle.center);//3HOps
		if(sqCenterDist<=sqRads) return true;

		return rectangle.distanceSquared(rss.rectangle)<=sqRads; //124HOps
		/*Vector thisCenter = this.getCenter();
		Vector rssCenter = rss.getCenter();
		Vector normal = rectangle.bases[0].cross(rectangle.bases[1]);

		//Check edges
		Capsule[] edges = getRims();
		Capsule[] rssEdges = rss.getRims();
		for(Capsule e: rssEdges){
			if(		edges[0].p1.vectorTo(e.p1).dot(rectangle.bases[0])>0 || 
					edges[0].p1.vectorTo(e.p2).dot(rectangle.bases[0])>0		){
				if(edges[0].overlaps(e)) return true;
			}
			if(		edges[1].p1.vectorTo(e.p1).dot(rectangle.bases[1].times(-1))>0 || 
					edges[1].p1.vectorTo(e.p2).dot(rectangle.bases[1].times(-1))>0		){
				if(edges[1].overlaps(e)) return true;
			}
			if(		edges[2].p1.vectorTo(e.p1).dot(rectangle.bases[0].times(-1))>0 || 
					edges[2].p1.vectorTo(e.p2).dot(rectangle.bases[0].times(-1))>0		){
				if(edges[2].overlaps(e)) return true;
			}
			if(		edges[3].p1.vectorTo(e.p1).dot(rectangle.bases[1])>0 || 
					edges[3].p1.vectorTo(e.p2).dot(rectangle.bases[1])>0		){
				if(edges[3].overlaps(e)) return true;
			}
		}

		for(Capsule e: rssEdges){
			//if( e.p)
		}
		 */
	}

	public Volume applyMatrix(Matrix m) {
		throw new Error("Unimplemented");		
	}

	public void applyMatrixIn(Matrix m) {
		throw new Error("Unimplemented");		
	}


	public void plusIn(Vector d) {
		throw new Error("Unimplemented");		
	}

	public void timesIn(float sc) {
		throw new Error("Unimplemented");
	}

	public float volume() {
		float boxVol = rectangle.bases[0].length()*rectangle.bases[1].length()*radius*8;
		float cylVol = 2*(rectangle.bases[0].length()+rectangle.bases[1].length())*(float)Math.PI*radius*radius;
		float sphereVol = (float)Math.PI*radius*radius*radius*4f/3;
		return boxVol+cylVol+sphereVol;
	}

	public Vector getCenter() {
		return rectangle.center;
	}
	
	public String toString(){
		return String.format("RSS[center:%s,bases[%s,%s],radius:%f]", rectangle.center, rectangle.bases[0], rectangle.bases[1],radius);
	}


	/** Return rims as capsules. The first is in the direction of extents[0], the rest comes 
	 * in clockwise order 
	 * @return rims as capsules
	 */
	@SuppressWarnings("unused")
	private Capsule[] getRims(){
		Vector[] corners = rectangle.getCorners(); //Corners in clockwise order
		return new Capsule[]{
				new Capsule(corners[0],corners[1],radius),
				new Capsule(corners[1],corners[2],radius),
				new Capsule(corners[2],corners[3],radius),
				new Capsule(corners[3],corners[0],radius),
		};
	}




	public static RSS createBoundingRSS_covariance(List<Vector> points){

		Matrix covMatr = Matrix.createCovarianceMatrix(points);
		Vector[] eigenVecs = covMatr.getEigenvectors();

		Vector tmp;
		if(eigenVecs[0].length()<eigenVecs[1].length()) {tmp = eigenVecs[0]; eigenVecs[0] = eigenVecs[1]; eigenVecs[1] = tmp; }
		if(eigenVecs[0].length()<eigenVecs[2].length()) {tmp = eigenVecs[0]; eigenVecs[0] = eigenVecs[2]; eigenVecs[2] = tmp; }
		if(eigenVecs[1].length()<eigenVecs[2].length()) {tmp = eigenVecs[1]; eigenVecs[1] = eigenVecs[2]; eigenVecs[2] = tmp; }

		eigenVecs[0].normIn();
		eigenVecs[1].normIn();
		eigenVecs[2].normIn();
		RSS ret = createBoundingRSSFromBases(eigenVecs, points);

		return ret;
	}

	private static RSS createBoundingRSSFromBases(Vector[] bases, List<Vector> points){
		//Find radius along the third base
		float lowestDot = Float.POSITIVE_INFINITY, highestDot = Float.NEGATIVE_INFINITY;
		for(Vector p: points){
			float dot = bases[2].dot(p);
			if(dot<lowestDot){	lowestDot = dot;	}
			if(dot>highestDot){	highestDot = dot;	}
		}
		float radius = (highestDot-lowestDot)/2;
		ParametricPlane P = new ParametricPlane(bases[2].times((highestDot+lowestDot)/2), bases[0], bases[1]);
		System.out.printf("Plane center %s, bases: [%s,%s], radius: %f\n",P.p, P.v1, P.v2, radius);
		
		//Slide half-cylinder caps along projection to the bases[0] and bases[1] planes.
		//dots contains the min and max along bases[0] and min and max along bases[1]
		float[] dots = {Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, 
				Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY};
		for(Vector p: points){
			float[] proj = P.projectPoint(p);
			float delta = (float)Math.sqrt(radius*radius-proj[2]*proj[2]);
			System.out.printf("Point: %s, projection: [%f , %f , %f], delta: %f .. %f %f\n", p,proj[0],proj[1],proj[2],delta, proj[0]+delta, proj[0]-delta);
			if(proj[0]+delta<dots[0]){	dots[0] = proj[0]+delta;	}
			if(proj[0]-delta>dots[1]){	dots[1] = proj[0]-delta;	}
			if(proj[1]+delta<dots[2]){	dots[2] = proj[1]+delta;	}
			if(proj[1]-delta>dots[3]){	dots[3] = proj[1]-delta;	}
		}
		System.out.printf("Final dots: %s\n",Arrays.toString(dots));
		//TODO: Fix corners
		
		
		float[] pars = { (dots[0]+dots[1])/2, (dots[2]+dots[3])/2 };
		float[] dim = { (dots[1]-dots[0])/2, (dots[3]-dots[2])/2 };
		Vector center = new Vector(P.getP(pars));
		Vector[] rssBases = {bases[0].times(dim[0]), bases[1].times(dim[1])};

		return new RSS(center, rssBases, radius);
	}
}
