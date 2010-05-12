package edu.geom3D;

import edu.math.Matrix;
import edu.math.Vector;

public class Tetrahedron implements Volume{
	public Vector p1,p2,p3,p4;
	
	public Tetrahedron(Vector p1, Vector p2, Vector p3, Vector p4){
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		this.p4 = p4;
	}
	
	public void applyMatrixIn(Matrix m) {
		m.applyToIn(p1);
		m.applyToIn(p2);
		m.applyToIn(p3);
		m.applyToIn(p4);
	}

	public boolean overlaps(Volume vol) {
		throw new Error("Tetrahedron.overlaps(..) not implemented");
	}

	public void plusIn(Vector d) {
		p1.plusIn(d);
		p2.plusIn(d);
		p3.plusIn(d);
		p4.plusIn(d);
	}

	public void timesIn(float sc) {
		p1.timesIn(sc);
		p2.timesIn(sc);
		p3.timesIn(sc);
		p4.timesIn(sc);
	}

	public float volume() {
		throw new Error("Tetrahedron.volume() not implemented");
	}

	public Vector getCenter() {
		return p1.plus(p2).plusIn(p3).plusIn(p4).timesIn(0.25f);
	}

	public Volume applyMatrix(Matrix m) {
		// TODO Auto-generated method stub
		return null;
	}

}
