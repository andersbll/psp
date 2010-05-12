package edu.math;


public class Plane {
	private float a,b,c,d;
	protected Vector p;
	protected Vector n;
	
	public Plane(Vector p, Vector n){
		this.p = p;
		this.n = n.norm();
		a = this.n.x;
		b = this.n.y; 
		c = this.n.z;
		d = -(a*p.x+b*p.y+c*p.z);
	}
	
	public float getDistance(Point p){
		return a*p.x+b*p.y+c*p.z+d;
	}
	
	public Vector projectOntoIn(Vector v){
		return v.minusIn(n.times(p.vectorTo(v).dot(n)));
	}
	public Vector projectOnto(Vector v){
		return v.minus(n.times(p.vectorTo(v).dot(n)));
	}
	
	/** Returns a parameter setting for the line describing the 
	 * intersection with this plane. Assumes there's a point 
	 * intersection.
	 */
	public float intersectionParameter(Line l){
		//From wikipedia
		//float d = -p.dot(n);
		//float t = (-d-l.point.dot(n))/(l.direction.dot(n));
		//From http://www.thepolygoners.com/tutorials/lineplane/lineplane.html
		float t = n.dot(l.point.vectorTo(p))/n.dot(l.direction);
		return t;
	}
	
	public Point getP(){
		return p;
	}
}
