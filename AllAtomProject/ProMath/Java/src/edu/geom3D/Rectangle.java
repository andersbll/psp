package edu.geom3D;

import edu.math.ParametricPlane;
import edu.math.Vector;

public class Rectangle {
	public Vector center;
	public Vector[] bases;
	private ParametricPlane plane;
//	public float[] extents;
	
	/** 39 HOps */
	public Rectangle(Vector center, Vector[] bases){
		this.center = center;
		this.bases = new Vector[]{bases[0], bases[1]};
		plane = new ParametricPlane(center, bases[0], bases[1]);
	}

	/**
	 * 3+4+15*2+9 = 46HOps 
	 */
	public float distanceSquared(Rectangle rect){
		ParametricPlane P1 = plane;
		ParametricPlane P2 = rect.plane;

		Vector p2point;
		
		float[] pars1;
		float tmp = P2.n.dot(P1.v1);//3HOps
		if(tmp==0) 	pars1 = new float[]{0, 0}; //P1 and P2 are parallel
		else		pars1 = new float[]{P2.n.dot(P1.p.vectorTo(P2.p))/tmp, 0}; //Intersection point of planes in P1 parameters. 4HOps 
		clamp(pars1);
		float[] pars2 = P2.projectPoint(P1.getP(pars1));		//15HOps
		clamp(pars2);
		pars1 = P1.projectPoint(p2point=P2.getP(pars2));		//15HOps
		clamp(pars1);
		return P1.getP(pars1).distanceSquared(p2point);		//9HOps
	}
	private float[] clamp(float[] pars){
		if(pars[0]<-1) pars[0] = -1;
		else if(pars[0]>1) pars[0] = 1;
		
		if(pars[1]<-1) pars[1] = -1;
		else if(pars[1]>1) pars[1] = 1;
		
		return pars;
	}
	
	/** Return corners of rectangle in clockwise order */
	public Vector[] getCorners() {
		return new Vector[]{
				center.plus( bases[0]).plusIn( bases[1]),
				center.plus( bases[0]).minusIn(bases[1]),
				center.minus(bases[0]).minusIn(bases[1]),
				center.minus(bases[0]).plusIn( bases[1])
		};
	}
	
}
