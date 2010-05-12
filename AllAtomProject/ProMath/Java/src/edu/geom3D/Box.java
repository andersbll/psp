package edu.geom3D;

import java.util.List;

import edu.math.Matrix;
import edu.math.Toolbox;
import edu.math.Vector;

/** An oriented bounding box. Specified by a corner-point, three base and
 * three extents along these bases.  
 * @author R. Fonseca
 */
public class Box implements Volume{
	public Vector p;
	public Vector[] bases;
	public float[] extents;

	/** Constructs a box with a corner in <code>p</code>, bases extending from <code>p</code> 
	 * with the specified extents. The two arrays: <code>bases</code> and <code>extents</code> 
	 * are both required to be 3 long, otherwise the bahavior of <code>Box</code> is unspecified.*/
	public Box(Vector p, Vector[] bases, float[] extents){
		this.p = p;
		this.bases = bases;
		this.extents = extents;
	}

	public Vector[] getVertices(){
		Vector[] ret = new Vector[8];
		ret[0] = p.clone();
		ret[1] = p.plus(bases[0].times(extents[0]));
		ret[2] = p.plus(bases[1].times(extents[1]));
		ret[3] = p.plus(bases[2].times(extents[2]));
		ret[4] = p.plus(bases[0].times(extents[0]).plus(bases[1].times(extents[1])));
		ret[5] = p.plus(bases[0].times(extents[0]).plus(bases[2].times(extents[2])));
		ret[6] = p.plus(bases[1].times(extents[1]).plus(bases[2].times(extents[2])));
		ret[7] = p.plus(bases[0].times(extents[0]).plus(bases[1].times(extents[1])).plus(bases[2].times(extents[2])));
		return ret;
	}

	/** Creates a minimum bounding box by testing 2048 unique orientations evenly distributed 
	 * on the unit sphere.
	 * @param points A list of points
	 * @return A box enclosing the points with as small a volume as possible.
	 */
	public static Box createBoundingBox(List<Vector> points){
		return createBoundingBox_Sampling(points);
	}


	/** Creates a minimum bounding box by testing 2048 unique orientations evenly distributed 
	 * on the unit sphere.
	 * @param points A list of points
	 * @return A box enclosing the points with as small a volume as possible.
	 */
	public static Box createBoundingBox_Sampling(List<Vector> points){

		Box minVolBox = null;
		float minVol = Float.MAX_VALUE;
		for(float phi=0;phi<Math.PI/2;phi+=Math.PI/64){
			for(float psi=0;psi<Math.PI;psi+=Math.PI/64){
				Vector[] bases = new Vector[3];
				bases[0] = new Vector(1,0,0);
				bases[1] = new Vector(0,1,0);
				bases[2] = new Vector(0,0,1);
				Matrix mx = Matrix.createRotationMatrix(phi, new Vector(1,0,0));
				Matrix my = Matrix.createRotationMatrix(psi, new Vector(0,1,0));
				mx.applyToIn(bases[0]);mx.applyToIn(bases[1]);mx.applyToIn(bases[2]);
				my.applyToIn(bases[0]);my.applyToIn(bases[1]);my.applyToIn(bases[2]);

				Box box = createBoundingBoxFromBases(bases, points);
				float vol = box.volume();
				if(vol<minVol){
					minVol = vol;
					minVolBox = box;
				}
			}
		}
		return minVolBox;
	}
	

	/** Creates a minimum bounding box by iteratively changing orientations such that
	 * the volume is always minimized.
	 * @param points A list of points
	 * @return A box enclosing the points with as small a volume as possible.
	 */
	public static Box createBoundingBox_Iterative(List<Vector> points){
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
		Vector[] bases = new Vector[3];
		bases[0] = dir.norm();
		bases[1] = Vector.createRandomVector().crossIn(dir).normIn();
		bases[2] = bases[0].cross(bases[1]);
		
		Box minVolBox = null;
		float minVol = Float.MAX_VALUE;
		for(int it=0;it<1000;it++){
			Vector[] prevBases = new Vector[3];
			prevBases[0] = bases[0].clone();
			prevBases[1] = bases[1].clone();
			prevBases[2] = bases[2].clone();
			//Box prevBox = createBoundingBoxFromBases(bases, points);
			
			Matrix m = Matrix.createRotationMatrix(Toolbox.randBetween(-0.1f, 0.1f), Vector.createRandomVector());
			m.applyToIn(bases[0]);bases[0].normIn();
			m.applyToIn(bases[1]);bases[1].normIn();
			m.applyToIn(bases[2]);bases[2].normIn();
			
			Box tmp = createBoundingBoxFromBases(bases, points);
			float volume = tmp.volume();
			if(volume<minVol){
				minVol = volume;
				minVolBox = tmp;
			}else{
				bases = prevBases;
			}
		}
		return minVolBox;
	}
	
	public static Box createBoundingBox_CovarianceFit(List<Vector> points){
		if(points.size()==1){
			Vector[] bases = {new Vector(1,0,0), new Vector(0,1,0), new Vector(0,0,1)};
			float[] extents = {0,0,0};
			return new Box(points.get(0).clone(), bases, extents);
		}
			
		
		Matrix covMatr = Matrix.createCovarianceMatrix(points);
		Vector[] eigenVecs = covMatr.getEigenvectors();
		
		eigenVecs[0].normIn();
		eigenVecs[1].normIn();
		eigenVecs[2].normIn();
		Box b = createBoundingBoxFromBases(eigenVecs, points);
		
		return b; 
	}
	
	public static Box createBoundingBoxFromBases(Vector[] bases, List<Vector> points){
		//Find point furthest back along each base
		Vector[] fPoints = new Vector[3];
		for(int b=0;b<3;b++){
			float lowestDot = 10000000;
			for(Vector p: points){
				float dot = bases[b].dot(p);
				if(dot<lowestDot){ 
					lowestDot = dot;
					fPoints[b] = p;
				}
			}
		}

		//Solve Ax=b
		double[][] array = {
				{bases[0].x(),bases[0].y(),bases[0].z()},
				{bases[1].x(),bases[1].y(),bases[1].z()},
				{bases[2].x(),bases[2].y(),bases[2].z()}};
		Jama.Matrix A = new Jama.Matrix(array);
		Jama.Matrix b = new Jama.Matrix(new double[][]{{fPoints[0].dot(bases[0])}, {fPoints[1].dot(bases[1])}, {fPoints[2].dot(bases[2])}});
		Jama.Matrix x = A.solve(b);
		//System.out.println(new Vector(x.get(0, 0),x.get(1, 0),x.get(2, 0)));

		Vector p = new Vector(x.get(0, 0),x.get(1, 0),x.get(2, 0));

		//Find extents
		float[] extents = new float[3];
		for(int base=0;base<3;base++){
			float maxDot = -1;
			for(Vector point: points){
				float dot = p.vectorTo(point).dot(bases[base]);
				if(dot>maxDot) maxDot = dot;
			}
			extents[base] = maxDot;

		}

		return new Box(p,bases, extents);
	}



	public boolean overlaps(Volume vol) {
		throw new Error("Not yet implemented");
	}


	/** Returns the volume of the oriented bounding box	 */
	public float volume(){
		return extents[0]*extents[1]*extents[2];
	}


	/** Multiplies the box with a scalar. This means multiplying the corner-point and
	 * all the extents. */
	public void timesIn(float sc) {
		p.timesIn(sc);
		extents[0]*=sc;
		extents[1]*=sc;
		extents[2]*=sc;
	}

	/** Apply a matrix to this box. Usually this means a rotation, so the matrix is
	 * applied to the corner-point and the bases. */
	public void applyMatrixIn(Matrix m) {
		m.applyToIn(p);
		m.applyToIn(bases[0]);
		m.applyToIn(bases[1]);
		m.applyToIn(bases[2]);
	}


	public void plusIn(Vector d) {
		p.plusIn(d);
	}

	public String toString(){
		return String.format("Box[p:%s,bases:%sx%sx%s,extents:%.1fx%.1fx%.1f]", 
				p, bases[0],bases[1],bases[2], extents[0],extents[1], extents[2]);
	}

	public Vector getCenter() {
		Vector c = p.plus(bases[0].times(0.5f*extents[0]));
		c.plusIn(bases[1].times(0.5f*extents[1]));
		c.plusIn(bases[2].times(0.5f*extents[2]));
		return c;
	}

	public Volume applyMatrix(Matrix m) {
		// TODO Auto-generated method stub
		return null;
	}


/*
	public static void main(String[] args){
		Locale.setDefault(Locale.ENGLISH);
		List<Vector> points = new LinkedList<Vector>();
		for(float z=0;z<10;z+=1){
			points.add(new Vector(Math.cos(z), Math.sin(z), z));
		}


		StringBuilder sb = new StringBuilder();
		for(float phi=0;phi<2*Math.PI;phi+=Math.PI/64){
			for(float psi=0;psi<2*Math.PI;psi+=Math.PI/64){
				Vector[] bases = new Vector[3];
				bases[0] = new Vector(1,0,0);
				bases[1] = new Vector(0,1,0);
				bases[2] = new Vector(0,0,1);
				Matrix mx = Matrix.createRotationMatrix(phi, new Vector(1,0,0));
				Matrix my = Matrix.createRotationMatrix(psi, new Vector(0,1,0));
				mx.applyToIn(bases[0]);mx.applyToIn(bases[1]);mx.applyToIn(bases[2]);
				my.applyToIn(bases[0]);my.applyToIn(bases[1]);my.applyToIn(bases[2]);

				Box box = createBoundingBoxFromBases(bases, points);
				float vol = box.volume();
				sb.append(String.format("%f %f %f\n", phi, psi, vol));
			}
		}
		try{
			// Create file 
			FileWriter fstream = new FileWriter("/Users/ras/Desktop/la2.txt");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(sb.toString());
			//Close the output stream
			out.close();
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
	*/
}
