package edu.geom3D;

import edu.math.Vector;

public class Triangle implements Shape{
	public Vector p1, p2, p3;
	
	public Triangle(Vector p1, Vector p2, Vector p3){
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
	}

	public Vector getCenter() {
		return p1.plus(p2).plusIn(p3).timesIn(1f/3f);
	}
	
	public Vector getNormal(){
		return p1.vectorTo(p2).crossIn(p1.vectorTo(p3)).normIn();
	}
}
