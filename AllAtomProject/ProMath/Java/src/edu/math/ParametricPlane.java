package edu.math;


public class ParametricPlane {
	public Vector p;
	public Vector n, v1, v2;
	protected Matrix projInv;
	
	/** 
	 * 8 + 31 = 39HOps
	 */
	public ParametricPlane(Vector p, Vector v1, Vector v2){
		this.p = p;
		this.n = v1.cross(v2).normIn();
		this.v1 = v1;
		this.v2 = v2;
		this.projInv = Matrix.createColumnMatrix(v1, v2, n).invertIn();
	}
	
	/** Projects the point v onto this plane and returns the parameters of 
	 * the projected point (scaling of v1, of v2 and finally the distance or 
	 * scaling along n). 9HOps
	 */
	public float[] projectPoint(Vector v){
		Vector x = p.vectorTo(v);
		projInv.applyToIn(x);
		return new float[]{x.x, x.y, x.z};
	}
	
	/** Returns a parameter setting for the line describing the 
	 * intersection with this plane. Assumes there's a point 
	 * intersection.
	 */
	public float intersectionParameter(Line l){
		//From http://www.thepolygoners.com/tutorials/lineplane/lineplane.html
		return n.dot(l.point.vectorTo(p))/n.dot(l.direction);
	}
	
	public Point getP(){
		return p;
	}
	public Vector getP(float[] pars){
		return p.plus(v1.times(pars[0])).plusIn(v2.times(pars[1]));
	}
}
