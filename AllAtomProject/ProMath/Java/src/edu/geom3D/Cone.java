package edu.geom3D;

import edu.math.Matrix;
import edu.math.Vector;

/**
 * A cone class. The cone is represented by the endpoints and a radius. 
 * @author R. Fonseca
 */
public class Cone implements Volume{
	public Vector p1, p2;
	public float rad;

	public Cone(Vector p1, Vector p2, float r){
		this.p1 = p1;
		this.p2 = p2;
		this.rad = r;
	}


	public boolean overlaps(Volume vol) {
		throw new Error("Cone.overlaps(..) not implemented");
	}
	
	public boolean contains(Vector p){
		throw new Error("Cone.containts(..) not implemented");
	}

	public void timesIn(float sc) {
		p1.timesIn(sc);
		p2.timesIn(sc);
		rad*=sc;
	}


	public void applyMatrixIn(Matrix m) {
		m.applyToIn(p1);
		m.applyToIn(p2);
	}


	public void plusIn(Vector d) {
		p1.plusIn(d);
		p2.plusIn(d);
	}


	public float volume() {
		float vol = (float)Math.PI*rad*rad*p1.distance(p2)/3f;
		return vol;
	}


	public Vector getCenter() {
		return p1.plus(p2).timesIn(0.5f);
	}


	public Volume applyMatrix(Matrix m) {
		// TODO Auto-generated method stub
		return null;
	}
}
