package edu.geom3D;

import java.util.ArrayList;
import java.util.List;

import edu.geom2D.Circle;
import edu.math.Line;
import edu.math.Matrix;
import edu.math.Plane;
import edu.math.Toolbox;
import edu.math.Vector;
import edu.math.Vector2D;

/**
 * A capsule (also known as a line-swept-sphere or sometimes 'cigar') class. The capsule is 
 * represented by two endpoints and a radius, and is a cylinder capped with hemispheres. 
 * Distance calculations (and thereby collision checks) can be performed very fast, but 
 * finding the minimum capsule bounding a set of points can be somewhat time-consuming and 
 * no well-documented methods exist for doing this. For a heuristic see Christer Ericson's
 * "Real-time Collision Detection". 
 * @author R. Fonseca
 */
public class Capsule implements Volume{
	public Vector p1, p2;
	public float rad;

	/**Construct a capsule using two endpoints (center of hemispheres) and a radius (used 
	 * both for hemispheres and cylinder shape). 
	 */
	public Capsule(Vector p1, Vector p2, float r){
		this.p1 = p1;
		this.p2 = p2;
		this.rad = r;
	}
	public Capsule(Vector p1, Vector p2, double r){
		this(p1,p2,(float)r);
	}

	public static Capsule createBoundingCapsule(List<Vector> points){
		return createBoundingCapsule_CovarianceFit(points);
	}

	public static Capsule createBoundingCapsule_SqDistLinFit(List<Vector> points){
		int N = points.size();
		float maxDist = 0;
		Vector m1=null, m2=null;
		for(int i=0;i<N;i++){
			for(int j=i+1;j<N;j++){
				float dist = points.get(i).distance(points.get(j));
				if(dist>maxDist){
					maxDist = dist;
					m1 = points.get(i);
					m2 = points.get(j);
				}
			}
		}

		Line line = new Line(m1, m1.vectorTo(m2));
		float perm = maxDist*0.01f;
		int ITMAX = 10000;
		for(int it=0;it<ITMAX;it++){
			Line tmp = line.clone();
			float max1 = 0f;
			for(Vector p:points){ 
				float dist = tmp.distance(p);
				//if(dist>max1) max1 = dist;
				max1+=dist*dist;
			}

			float damper = (ITMAX-it)*1f/ITMAX;
			tmp.getPoint().setX(tmp.getPoint().x()+Toolbox.randBetween(-perm,perm)*damper);
			tmp.getPoint().setY(tmp.getPoint().y()+Toolbox.randBetween(-perm,perm)*damper);
			tmp.getPoint().setZ(tmp.getPoint().z()+Toolbox.randBetween(-perm,perm)*damper);
			tmp.getDirection().setX(tmp.getDirection().x()+Toolbox.randBetween(-perm,perm)*damper);
			tmp.getDirection().setY(tmp.getDirection().y()+Toolbox.randBetween(-perm,perm)*damper);
			tmp.getDirection().setZ(tmp.getDirection().z()+Toolbox.randBetween(-perm,perm)*damper);

			float max2 = 0f;
			for(Vector p:points){ 
				float dist = tmp.distance(p);
				//if(dist>max2) max2 = dist;
				max2+=dist*dist;
			}
			if(max2<max1) line = tmp;
		}

		//Calc rad
		float rad = 0;
		for(Vector p:points){ 
			float dist = line.distance(p);
			if(dist>rad) rad = dist;
		}

		//Now cap the ends
		float lowerT =  Float.POSITIVE_INFINITY;
		float upperT =  Float.NEGATIVE_INFINITY;
		for(Vector point: points){
			Vector D = line.getDirection();
			Vector P = line.getPoint();
			float a = D.dot(D);
			float b = 2*D.dot(P.minus(point));
			float c = point.dot(point) + P.dot(P) - 2*(point.dot(P))-rad*rad;
			float d = Math.max(0.f, b*b-4*a*c);
			float highT = (-b+(float)Math.sqrt(d))/(2*a);
			float lowT = (-b-(float)Math.sqrt(d))/(2*a);
			if(highT<lowerT)  lowerT = highT;
			if(lowT>upperT)   upperT = lowT;
		}

		Vector newP1 = line.getPoint().plus(line.getDirection().times(lowerT));
		Vector newP2 = line.getPoint().plus(line.getDirection().times(upperT));

		return new Capsule(newP1, newP2, rad);
	}
	public static Capsule createBoundingCapsule_MaxDistLinFit(List<Vector> points){
		int N = points.size();
		float maxDist = 0;
		Vector m1=null, m2=null;
		for(int i=0;i<N;i++){
			for(int j=i+1;j<N;j++){
				float dist = points.get(i).distance(points.get(j));
				if(dist>maxDist){
					maxDist = dist;
					m1 = points.get(i);
					m2 = points.get(j);
				}
			}
		}

		Line line;
		if(points.size()==1) line = new Line(m1, Vector.createRandomVector()); 
		else line = new Line(m1, m1.vectorTo(m2));

		float perm = maxDist*0.01f;
		int ITMAX = 1000;
		for(int it=0;it<ITMAX;it++){
			Line tmp = line.clone();
			float max1 = 0f;
			for(Vector p:points){ 
				float dist = tmp.distance(p);
				if(dist>max1) max1 = dist;
			}

			float damper = (ITMAX-it)*1f/ITMAX;
			tmp.getPoint().setX(tmp.getPoint().x()+Toolbox.randBetween(-perm,perm)*damper);
			tmp.getPoint().setY(tmp.getPoint().y()+Toolbox.randBetween(-perm,perm)*damper);
			tmp.getPoint().setZ(tmp.getPoint().z()+Toolbox.randBetween(-perm,perm)*damper);
			tmp.getDirection().setX(tmp.getDirection().x()+Toolbox.randBetween(-perm,perm)*damper);
			tmp.getDirection().setY(tmp.getDirection().y()+Toolbox.randBetween(-perm,perm)*damper);
			tmp.getDirection().setZ(tmp.getDirection().z()+Toolbox.randBetween(-perm,perm)*damper);

			float max2 = 0f;
			for(Vector p:points){ 
				float dist = tmp.distance(p);
				if(dist>max2) max2 = dist;
			}
			if(max2<max1) line = tmp;
		}

		//Calc rad
		float rad = 0;
		for(Vector p:points){ 
			float dist = line.distance(p);
			if(dist>rad) rad = dist;
		}

		//Now cap the ends
		float lowerT =  Float.POSITIVE_INFINITY;
		float upperT =  Float.NEGATIVE_INFINITY;
		for(Vector point: points){
			Vector D = line.getDirection();
			Vector P = line.getPoint();
			float a = D.dot(D);
			float b = 2*D.dot(P.minus(point));
			float c = point.dot(point) + P.dot(P) - 2*(point.dot(P))-rad*rad;
			float d = Math.max(0.f, b*b-4*a*c);
			float highT = (-b+(float)Math.sqrt(d))/(2*a);
			float lowT = (-b-(float)Math.sqrt(d))/(2*a);
			if(highT<lowerT)  lowerT = highT;
			if(lowT>upperT)   upperT = lowT;
		}

		Vector newP1 = line.getPoint().plus(line.getDirection().times(lowerT));
		Vector newP2 = line.getPoint().plus(line.getDirection().times(upperT));

		return new Capsule(newP1, newP2, rad);
	}

	public static Capsule createBoundingCapsule_CovarianceFit(List<Vector> points){
		if(points.size()==1)
			return new Capsule(points.get(0).clone(), points.get(0).clone(), 0);
		if(points.size()==2)
			return new Capsule(points.get(0).clone(), points.get(1).clone(), 0);

		Matrix covMatr = Matrix.createCovarianceMatrix(points);
		Vector[] eigenVecs = covMatr.getEigenvectors();
		Vector dir = eigenVecs[0];
		if(eigenVecs[1].length()>dir.length()) dir = eigenVecs[1];
		if(eigenVecs[2].length()>dir.length()) dir = eigenVecs[2];

		InfCylinder iCyl = InfCylinder.createMinRadCylinderFromDirection(points, dir);
		Capsule ret = iCyl.capWithHalfSpheres(points);
		return ret;
	}

	public static Capsule createBoundingCapsule_MaxDist(List<Vector> points){
		if(points.size()==1)
			return new Capsule(points.get(0).clone(), points.get(0).clone(), 0);

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

		InfCylinder iCyl = InfCylinder.createMinRadCylinderFromDirection(points, dir);
		Capsule ret = iCyl.capWithHalfSpheres(points);
		return ret;
	}

	public static Capsule createBoundingCapsule_Sampling(List<Vector> points){
		InfCylinder icyl = InfCylinder.createMinRadBoundingCylinder_Sampling(points);
		return icyl.capWithHalfSpheres(points);
	}

	public static Capsule createBoundingCapsule_Iterative(List<Vector> points){
		if(points.size()==1)
			return new Capsule(points.get(0).clone(), points.get(0).clone(), 0);

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
		InfCylinder iCyl = InfCylinder.createMinRadCylinderFromDirection(points, dir);
		Capsule minCap = iCyl.capWithHalfSpheres(points);
		float minVol = minCap.volume();

		for(int it=0;it<1000;it++){
			Vector oldDir = dir.clone();
			dir.plusIn(Vector.createRandomVector().timesIn(1f/dir.length())).normIn();
			iCyl = InfCylinder.createMinRadCylinderFromDirection(points, dir);
			Capsule tmp = iCyl.capWithHalfSpheres(points);
			float vol = tmp.volume();
			if(vol<minVol){
				minVol = vol;
				minCap = tmp;
			}else{
				dir = oldDir;
			}
		}
		return minCap;

	}

	public static Capsule createBoundingCapsule_FourPointFast(Vector p1, Vector p2, Vector p3, Vector p4){
		Capsule c1 = createBoundingCapsule_ThreePoint(p1,p2,p3);
		if(c1.contains(p4)){ return c1; }
		else{
			Capsule c2 = createBoundingCapsule_ThreePoint(p1,p2,p4);
			if(c2.contains(p3)){ return c2; }
			else{
				Capsule c3 = createBoundingCapsule_ThreePoint(p1,p3,p4);
				if(c3.contains(p2)){ return c3; }
				else{
					Capsule c4 = createBoundingCapsule_ThreePoint(p2,p3,p4);
					if(c4.contains(p1)){ return c4; }
				}
			}
		}

		return null;
	}
	public static Capsule createBoundingCapsule_FourPoint(Vector p1, Vector p2, Vector p3, Vector p4){
		Capsule ret = createBoundingCapsule_FourPointFast(p1,p2,p3,p4);
		if(ret!=null) return ret;
		List<Vector> points = new ArrayList<Vector>(4);
		points.add(p1);points.add(p2);points.add(p3);points.add(p4);

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

		float minVol = Float.POSITIVE_INFINITY;
		Capsule minCap = null;
		for(int it=0;it<10;it++){
			Vector oldDir = dir.clone();
			dir.plusIn(Vector.createRandomVector().timesIn(dir.length()/10)).normIn();
			InfCylinder iCyl = InfCylinder.createMinRadCylinderFromDirection(points, dir);
			Capsule tmp = iCyl.capWithHalfSpheres(points);
			float vol = tmp.volume();
			if(vol<minVol){
				minVol = vol;
				minCap = tmp;
			}else{
				dir = oldDir;
				it--;
			}
		}
		return minCap;
	}

	public static Capsule createBoundingCapsule_ThreePoint(Vector p1, Vector p2, Vector p3){
		Vector v12 = p1.vectorTo(p2), v21 = p2.vectorTo(p1);
		Vector v23 = p2.vectorTo(p3);
		Vector v13 = p1.vectorTo(p3);
		double a1 = p1.vectorTo(p2).angle(p1.vectorTo(p3));
		double a2 = p2.vectorTo(p1).angle(p2.vectorTo(p3));
		double a3 = p3.vectorTo(p1).angle(p3.vectorTo(p2));

		double h1 = Math.sin(a3)*p3.distance(p1);
		double h2 = Math.sin(a1)*p1.distance(p2);
		double h3 = Math.sin(a2)*p2.distance(p3);
		if(h1<h2 && h1<h3){
			float r = (float)h1/2;
			Vector hVec = v23.cross(v21).crossIn(v23).normIn().timesIn(r);
			Vector c1 = p2.plus(hVec);
			Vector c2 = p3.plus(hVec);
			return new Capsule(c1,c2, r);
		}else if(h2<h1 && h2<h3){
			float r = (float) h2/2;
			//System.out.println(v13+" .. "+v12+" .. "+v13.cross(v12));
			Vector hVec = v13.cross(v12).crossIn(v13).normIn().timesIn(r);
			Vector c1 = p1.plus(hVec);
			Vector c2 = p3.plus(hVec);
			return new Capsule(c1,c2, r);
		}else{
			float r = (float) h3/2;
			Vector hVec = v21.cross(v23).crossIn(v21).normIn().timesIn(r);
			Vector c1 = p2.plus(hVec);
			Vector c2 = p1.plus(hVec);
			return new Capsule(c1,c2, r);
		}
	}
	
	

	private static float clamp(float s){
		if(s<0) return 0;
		if(s>1) return 1;
		return s;
	}

	public float distanceToPoint(Vector point){
		Vector d = p1.vectorTo(p2);
		float t = clamp( -(p1.minus(point).dot(d))/(d.dot(d)) );
		return ( p1.plus(d.timesIn(t)).minusIn(point) ).length();
	}
	public boolean overlaps(Capsule capsule){
		float minDist = closestSegmentPoint(capsule); 
		return minDist<=(rad+capsule.rad);
	}

	/** 
	 * 32HOps at most. 
	 */
	public float closestSegmentPoint(Capsule capsule){
		Vector startPoint1 = p1;
		Vector startPoint2 = capsule.p1;

		Vector dir1 = startPoint1.vectorTo(p2);
		Vector dir2 = capsule.p1.vectorTo(capsule.p2);
		
		if(dir1.length()<0.000001 && dir2.length()<0.00001 )
			return startPoint1.distance(startPoint2);
		if(dir1.length()<0.000001) return closestSegmentPoint(startPoint2, capsule.p2, startPoint1);
		if(dir2.length()<0.000001) return closestSegmentPoint(startPoint1, p2, startPoint2);
		//System.out.println("len1 "+d1.length()+" .. len2 "+d2.length());
		
		Vector r = startPoint1.minus(startPoint2);
		float a = dir1.dot(dir1);//|S1| squared       .. 3HOp
		float e = dir2.dot(dir2);//|S2| squared       .. 3HOp
		float f = dir2.dot(r);//                    .. 3HOp
		float c = dir1.dot(r);//                    .. 3HOp
		float b = dir1.dot(dir2);//                   .. 3HOp
		float denom = a*e-b*b;//                  .. 2HOp

		//If segments not parallel, compute closest point on L1 and L2
		//and clamp to S1
		float s, t;
		if(denom!=0.0f)  s = clamp( (b*f-c*e)/denom );//      .. 3HOp
		else             s = 0.0f;

		//Compute point on L2 closest to S1(S)
		float tnom = b*s+f;//                     .. 1HOp

		//If t in [0,1] done. Else clamp t and recompute and clamp s
		//.. 1 HOp
		if(tnom<0.0f){
			t = 0.0f;
			s = clamp(-c/a);
		}else if(tnom>e){
			t = 1.0f;
			s = clamp( (b-c)/a );
		}else{
			t = tnom/e;
		}

		Vector c1 = startPoint1.plus(dir1.timesIn(s));//      vec-scalar mult  .. 3HOp
		Vector c2 = startPoint2.plus(dir2.timesIn(t));//      vec-scalar mult  .. 3HOp
		return c1.distance(c2);	                //          .. 4HOp
	}
	
	public static float closestSegmentPoint(Vector p11, Vector p12, Vector p2){
		Line l = new Line(p11, p11.vectorTo(p12));
		float t = l.projectOnto(p2);
		t = clamp(t)*p11.distance(p12);
		return l.getPoint(t).distance(p2);
	}


	public boolean overlaps(Volume vol) {
		if(vol instanceof Capsule) return overlaps((Capsule)vol);
		throw new Error("Unimplemented");
	}

	public boolean contains(Vector p){
		Line l = new Line(p1, p1.vectorTo(p2));
		float t = l.projectOnto(p);
		if(t>1) t=1; else if(t<0) t=0;
		return l.getPoint(t).distance(p)<=this.rad;
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

	public Volume applyMatrix(Matrix m) {
		Capsule ret = new Capsule(p1.clone(), p2.clone(), rad);
		m.applyToIn(ret.p1);
		m.applyToIn(ret.p2);
		return ret;
	}

	public void plusIn(Vector d) {
		p1.plusIn(d);
		p2.plusIn(d);
	}


	public float volume() {
		float sphereVols = (4f/3f)*(float)Math.PI*rad*rad*rad;
		float cylVol = (float)Math.PI*rad*rad*p1.distance(p2);
		return sphereVols+cylVol;
	}

	public Vector getCenter() {
		return p1.plus(p2).timesIn(0.5f);
	}
	
	public String toString(){
		return "Capsule["+p1+","+p2+",rad="+rad+"]";
	}

	public static Capsule createBoundingCapsule_CovarianceFit(Capsule v1, Capsule v2) {
		List<Vector> points = new ArrayList<Vector>();
		points.add(v1.p1);
		points.add(v1.p2);
		points.add(v2.p1);
		points.add(v2.p2);

		Matrix m = Matrix.createCovarianceMatrix(points);
		Vector[] eigVs = m.getEigenvectors();
		Vector maxEig = eigVs[0];
		if(eigVs[1].length()>maxEig.length()) maxEig = eigVs[1];
		if(eigVs[2].length()>maxEig.length()) maxEig = eigVs[2];
		
		//Capsule c = Capsule.createBoundingCapsule_CovarianceFit(points);
		//c.rad+=Math.max(v1.rad, v2.rad);
		//return c;
		
		InfCylinder iCyl = InfCylinder.createMinRadCylinderFromDirection(v1, v2, maxEig.normIn());
		return iCyl.capWithHalfSpheres(v1, v2);
	}
	public static Capsule createBoundingCapsule_MaxDist(Capsule v1, Capsule v2) {
		float[] rads = {v1.rad, v1.rad, v2.rad, v2.rad};
		Vector[] points = {v1.p1, v1.p2, v2.p1, v2.p2};
		float[][] distMap = new float[4][4];
		for(int i=0;i<4;i++) for(int j=i+1;j<4;j++) distMap[i][j] = points[i].distance(points[j])+rads[i]+rads[j];
		
		int m1=0, m2=1;
		for(int i=0;i<4;i++){
			for(int j=i+1;j<4;j++){
				if(distMap[i][j]>distMap[m1][m2]){
					m1 = i;
					m2 = j;
				}
			}
		}
		Vector dir = points[m1].vectorTo(points[m2]).normIn();
		//dir = new Vector3d(1,0,0);
		int exclude = 0;
		//if( m1<2 && m2<2 ) ;//A circle enclosing only 0,2 and 3 must be created
		if(m1>1 && m2>1) exclude = 2;//A circle enclosing only 0,1 and 3 must be created
		else {
			if(rads[m1]>rads[m2]) exclude = m2;//A circle enclosing all but m2 must be created
			else exclude = m1;//A circle enclosing all but m1 must be created;
		}
		
		InfCylinder iCyl = createCylinderFromDirAndThreeSpheres(dir,rads,points,exclude);
		//InfCylinder iCyl = InfCylinder.createMinRadCylinderFromDirection(v1, v2, points[m1].vectorTo(points[m2]).normIn());
		Capsule ret = iCyl.capWithHalfSpheres(v1, v2);
		return ret;
	}
	private static final InfCylinder createCylinderFromDirAndThreeSpheres(Vector dir, float[] rads, Vector[] points, int exclude){
		Plane p = new Plane(Vector.O(), dir);
		
		Vector x = Vector.createRandomVector().crossIn(dir).normIn();
		Vector y = x.cross(dir).norm();
		Circle[] cArr = new Circle[3];
		int c=0;
		for(int i=0;i<3;i++){
			if(exclude==c) c++;
			Vector proj = p.projectOnto(points[c]);
			cArr[i] = new Circle(new Vector2D(x.dot(proj), y.dot(proj)), rads[c]);
			c++;
		}

		Circle mec = new Circle( cArr[0],cArr[1],cArr[2]);
		Vector linePoint = x.times(mec.getCenter().x()).plus(y.times(mec.getCenter().y()));
		return new InfCylinder(new Line(linePoint,dir), mec.getRadius());
	}

	public static Capsule createBoundingCapsule(Sphere v1, Sphere v2) {
		List<Vector> points = new ArrayList<Vector>();
		points.add(v1.center);
		points.add(v2.center);
		Capsule c = new Capsule(v1.center, v2.center, Math.max(v1.radius, v2.radius));
		return c;
	}

}
