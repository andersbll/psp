package edu.geom3D;

import java.util.List;

import edu.math.Matrix;
import edu.math.Vector;

/**
 * A cylinder class. The cylinder is represented by the endpoints and a radius. 
 * @author R. Fonseca
 */
public class Cylinder implements Volume{
	public Vector p1, p2;
	public float rad;

	public Cylinder(Vector p1, Vector p2, float r){
		this.p1 = p1;
		this.p2 = p2;
		this.rad = r;
	}


	public boolean overlaps(Volume vol) {
		throw new Error("Cylinder.overlaps(..) not implemented");
	}
	
	public boolean contains(Vector p){
		throw new Error("Cylinder.containts(..) not implemented");
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
		float cylVol = (float)Math.PI*rad*rad*p1.distance(p2);
		return cylVol;
	}


	public Vector getCenter() {
		return p1.plus(p2).timesIn(0.5f);
	}
	
	
	
	
	public static Cylinder createMinRadCylinder_Iterative(List<Vector> points){
		float maxDist = 0;
		Vector mp1=null, mp2=null;
		for(int i=0;i<points.size();i++) for(int j=0;j<i;j++){
			float dsq =points.get(i).distanceSquared(points.get(j)); 
			if(dsq>maxDist){
				maxDist = dsq;
				mp1 = points.get(i);
				mp2 = points.get(j);
			}
		}
		Vector dir = mp1.vectorTo(mp2);
		//Capsule linFitCaps = createBoundingCapsule_LinFit(points);
		//Vector dir = linFitCaps.p1.vectorTo(linFitCaps.p2);
		
		float minRad = Float.POSITIVE_INFINITY;
		InfCylinder minCyl = null;
		for(int it=0;it<100000;it++){
			Vector oldDir = dir.clone();
			dir.plusIn(Vector.createRandomVector().timesIn(dir.length()/10)).normIn();
			InfCylinder iCyl = InfCylinder.createMinRadCylinderFromDirection(points, dir.clone());
			
			if(iCyl.radius<minRad){
				minRad = iCyl.radius;
				minCyl = iCyl;
			}else{
				dir = oldDir;
			}
		}
		//return new Cylinder(minCyl.line.getPoint(), minCyl.line.getPoint().plus(minCyl.line.getDirection()), minCyl.radius);
		return minCyl.capWithDiscs(points);
		
	}
	
	public static Cylinder createBoundingCylinder_CovarianceFit(List<Vector> points){
		if(points.size()==1)
			return new Cylinder(points.get(0).clone(), points.get(0).clone(), 0);

		Matrix covMatr = Matrix.createCovarianceMatrix(points);
		Vector[] eigenVecs = covMatr.getEigenvectors();
		Vector dir = eigenVecs[0];
		if(eigenVecs[1].length()>dir.length()) dir = eigenVecs[1];
		if(eigenVecs[2].length()>dir.length()) dir = eigenVecs[2];
		
		InfCylinder iCyl = InfCylinder.createMinRadCylinderFromDirection(points, dir);
		Cylinder ret = iCyl.capWithDiscs(points);
		return ret;
	}


	public Volume applyMatrix(Matrix m) {
		// TODO Auto-generated method stub
		return null;
	}
	

}
