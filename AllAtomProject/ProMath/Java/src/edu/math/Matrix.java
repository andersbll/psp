package edu.math;

import java.util.List;

import Jama.EigenvalueDecomposition;

public class Matrix {
	private int N, M;
	private float[][] coords;
	public Matrix(int N, int M){
		this.N = N;
		this.M = M;
		coords = new float[N][M];
		for(int i=0;i<N;i++) for(int j=0;j<M;j++) coords[i][j] = 0.f;
	}
	public float[] getCoordArray(){
		float[] cArr = new float[M*N];
		int c=0;
		for(int i=0;i<N;i++) for(int j=0;j<M;j++) cArr[c++] = coords[i][j];
		return cArr;
	}
	public double[] getDoubleCoordArray(){
		double[] cArr = new double[M*N];
		int c=0;
		for(int i=0;i<N;i++) for(int j=0;j<M;j++) cArr[c++] = coords[i][j];
		return cArr;
	}
	public float[] getNormalizedCoordArray(){
		float[] cArr = new float[(M+1)*(N+1)];
		int c=0;
		for(int i=0;i<N;i++) {
			for(int j=0;j<M;j++) cArr[c++] = coords[i][j];
			cArr[c++] = 0;
		}
		cArr[c++] = 0;
		cArr[c++] = 0;
		cArr[c++] = 0;
		cArr[c++] = 1;
		return cArr;
	}

	public static Matrix createColumnMatrix(Vector v1, Vector v2, Vector v3){
		Matrix ret = new Matrix(3,3);
		ret.coords[0][0] = v1.x;
		ret.coords[1][0] = v1.y;
		ret.coords[2][0] = v1.z;
		ret.coords[0][1] = v2.x;
		ret.coords[1][1] = v2.y;
		ret.coords[2][1] = v2.z;
		ret.coords[0][2] = v3.x;
		ret.coords[1][2] = v3.y;
		ret.coords[2][2] = v3.z;
		return ret;
	}
	public static Matrix create4x4ColumnMatrix(Vector v1, Vector v2, Vector v3, Vector v4){
		Matrix ret = new Matrix(4,4);
		ret.coords[0][0] = v1.x;
		ret.coords[1][0] = v1.y;
		ret.coords[2][0] = v1.z;
		ret.coords[3][0] = 0;
		ret.coords[0][1] = v2.x;
		ret.coords[1][1] = v2.y;
		ret.coords[2][1] = v2.z;
		ret.coords[3][1] = 0;
		ret.coords[0][2] = v3.x;
		ret.coords[1][2] = v3.y;
		ret.coords[2][2] = v3.z;
		ret.coords[3][2] = 0;
		ret.coords[0][3] = v4.x;
		ret.coords[1][3] = v4.y;
		ret.coords[2][3] = v4.z;
		ret.coords[3][3] = 1;
		return ret;
	}
	public static Matrix createRowMatrix(Vector v1, Vector v2, Vector v3){
		Matrix ret = new Matrix(3,3);
		ret.coords[0][0] = v1.x;
		ret.coords[0][1] = v1.y;
		ret.coords[0][2] = v1.z;
		ret.coords[1][0] = v2.x;
		ret.coords[1][1] = v2.y;
		ret.coords[1][2] = v2.z;
		ret.coords[2][0] = v3.x;
		ret.coords[2][1] = v3.y;
		ret.coords[2][2] = v3.z;
		return ret;
	}

	public static Matrix createRotateYMatrix(float angle){
		float cosA = (float)Math.cos(angle);
		float sinA = (float)Math.sin(angle);
		Matrix ret = new Matrix(3,3);
		ret.coords[0][0] = cosA;
		ret.coords[1][0] = 0;
		ret.coords[2][0] = -sinA;
		ret.coords[0][1] = 0;
		ret.coords[1][1] = 1;
		ret.coords[2][1] = 0;
		ret.coords[0][2] = sinA;
		ret.coords[1][2] = 0;
		ret.coords[2][2] = cosA;
		return ret;
	}
	
	/** TODO: Comment */
	public static Matrix createRotationMatrix(float angle, Vector v){
		float ux = v.x;
		float uy = v.y;
		float uz = v.z;
		float cosA = (float)Math.cos(angle);
		float sinA = (float)Math.sin(angle);

		Matrix ret = new Matrix(3,3);
		ret.coords[0][0] = ux*ux + cosA*(1.0f-ux*ux);
		ret.coords[1][0] = ux*uy*(1.0f-cosA) + uz*sinA;
		ret.coords[2][0] = uz*ux*(1.0f-cosA) - uy*sinA;

		ret.coords[0][1] = ux*uy*(1.0f-cosA) - uz*sinA;
		ret.coords[1][1] = uy*uy + cosA*(1.0f-uy*uy);
		ret.coords[2][1] = uy*uz*(1.0f-cosA) + ux*sinA;

		ret.coords[0][2] = uz*ux*(1.0f-cosA) + uy*sinA;
		ret.coords[1][2] = uy*uz*(1.0f-cosA) - ux*sinA;
		ret.coords[2][2] = uz*uz + cosA*(1.0f-uz*uz);
		return ret;
	}

	/** TODO: Comment */
	public static Matrix createCovarianceMatrix(List<Vector> points){
		/*Vector mean = new Vector(0,0,0);
		for(Vector p: points) mean.plusIn(p);
		mean.timesIn(1f/points.size());
		Matrix ret = new Matrix(3,3);
		for(int i=0;i<points.size();i++) {
			Vector delta = points.get(i).minus(mean);
			ret.addIn(createTensorSquare(delta));
		}
		ret.multIn(1f/points.size());
		 */
		Matrix ret = new Matrix(3,3);
		double[] means = {0,0,0};
		for(int d=0;d<3;d++){
			double mean = 0;
			for(Vector p: points) mean+=p.get(d);
			means[d] = mean/points.size();
		}
		//System.out.println("Mean "+means[0]+" .. "+means[1]+" .. "+means[2]);
		for(int i=0;i<3;i++){
			for(int j=i;j<3;j++){
				ret.coords[i][j] = (float)covariance(points, means, i,j);
				ret.coords[j][i] = ret.coords[i][j];
			}
		}

		return ret;
	}

	private static double covariance(List<Vector> points, double[] means, int dim1, int dim2){
		double ret = 0;
		for(Vector v: points){
			ret+=(v.get(dim1)-means[dim1])*(v.get(dim2)-means[dim2]);
		}

		return ret/points.size();
	}

	public static Matrix createTensorSquare(Vector v){
		Matrix ret = new Matrix(3,3);
		ret.coords[0][0] = v.x*v.x;
		ret.coords[0][1] = v.x*v.y;
		ret.coords[0][2] = v.x*v.z;
		ret.coords[1][0] = v.y*v.x;
		ret.coords[1][1] = v.y*v.y;
		ret.coords[1][2] = v.y*v.z;
		ret.coords[2][0] = v.z*v.x;
		ret.coords[2][1] = v.z*v.y;
		ret.coords[2][2] = v.z*v.z;
		//System.out.println("Tensor square: ");
		//System.out.println(ret);
		return ret;
	}

	/** Apply this matrix to the vector v. The vector v is changed and then returned. */
	public Vector applyToIn(Vector v){
		assert(N==M);
		if(N==M && N==3){
			float newX = v.x*coords[0][0] + v.y*coords[0][1] + v.z*coords[0][2];
			float newY = v.x*coords[1][0] + v.y*coords[1][1] + v.z*coords[1][2];
			float newZ = v.x*coords[2][0] + v.y*coords[2][1] + v.z*coords[2][2];
			v.x = newX;
			v.y = newY;
			v.z = newZ;
			return v;
		}else if(N==4){
			float newX = v.x*coords[0][0] + v.y*coords[0][1] + v.z*coords[0][2];
			float newY = v.x*coords[1][0] + v.y*coords[1][1] + v.z*coords[1][2];
			float newZ = v.x*coords[2][0] + v.y*coords[2][1] + v.z*coords[2][2];
			v.x = newX+coords[0][3];
			v.y = newY+coords[1][3];
			v.z = newZ+coords[2][3];
			return v;
		}
		return null;
	}

	/** Apply this matrix to another matrix. This matrix is changed and then returned */
	public Matrix applyToIn(Matrix m){
		float[][] newCoords = new float[N][M];
		for(int r=0;r<N;r++){
			for(int c=0;c<M;c++){
				newCoords[r][c] = 0; 
				for(int i=0;i<M;i++) newCoords[r][c]+=coords[r][i]*m.coords[i][c];
			}
		}
		this.coords = newCoords;
		return this;
	}

	/** Apply this matrix to another matrix. The result is returned */
	public Matrix applyTo(Matrix m) {
		float[][] newCoords = new float[N][M];
		for(int r=0;r<N;r++){
			for(int c=0;c<M;c++){
				newCoords[r][c] = 0;
				for(int i=0;i<M;i++) newCoords[r][c]+=coords[r][i]*m.coords[i][c];
				//r=3, c=0
				// newCs[3][0] = coords[3][0]*m.coords[0]
			}
		}
		Matrix ret = new Matrix(N, M);
		ret.coords = newCoords;
		return ret;
	}

	/** Add the components of two matriced */
	public Matrix addIn(Matrix m){
		assert M==m.M && N==m.N;
		for(int i=0;i<M;i++) for(int j=0;j<N;j++) coords[i][j]+=m.coords[i][j];
		return this;
	}
	
	/** Multiply the components of this matrix by a scalar. Changes and then returns this */
	public Matrix multIn(float scalar){
		for(int i=0;i<M;i++) for(int j=0;j<N;j++) coords[i][j]*=scalar;
		return this;
	}

	/** Solves the system thisáx=v and returns x using the Jama package.*/
	public Vector solve(Vector v){
		double[][] AArray = new double[M][N];
		for(int i=0;i<M;i++) for(int j=0;j<N;j++) AArray[i][j] = coords[i][j];
		double[][] vArray = new double[3][1];
		for(int i=0;i<3;i++) for(int j=0;j<1;j++) vArray[i][j] = v.get(i);
		Jama.Matrix A = new Jama.Matrix(AArray);
		Jama.Matrix x = A.solve(new Jama.Matrix(vArray));

		return new Vector(x.get(0, 0), x.get(1, 0), x.get(2, 0));
	}

	/** Return the inverse of this matrix. Works only for 3x3 matrices. Uses 31HOPs. */
	public Matrix invertIn(){
		if(N==3 && M==3){
			float[][] newCoords = new float[N][M];
			newCoords[0][0] = coords[1][1]*coords[2][2] - coords[1][2]*coords[2][1];
			newCoords[0][1] = coords[0][2]*coords[2][1] - coords[0][1]*coords[2][2];
			newCoords[0][2] = coords[0][1]*coords[1][2] - coords[0][2]*coords[1][1];
			
			newCoords[1][0] = coords[1][2]*coords[2][0] - coords[1][0]*coords[2][2];
			newCoords[1][1] = coords[0][0]*coords[2][2] - coords[0][2]*coords[2][0];
			newCoords[1][2] = coords[0][2]*coords[1][0] - coords[0][0]*coords[1][2];
			
			newCoords[2][0] = coords[1][0]*coords[2][1] - coords[1][1]*coords[2][0];
			newCoords[2][1] = coords[0][1]*coords[2][0] - coords[0][0]*coords[2][1];
			newCoords[2][2] = coords[0][0]*coords[1][1] - coords[0][1]*coords[1][0];//18HOPs
			
			float det = coords[0][0]*newCoords[0][0]+coords[0][1]*newCoords[1][0]+coords[0][2]*newCoords[2][0]; //3HOPs
			this.coords = newCoords;
			return(this.multIn(1/det));//10HOps
		}
		return this;
	}
	
	public float determinant(){
		if(N==3 && M==3){
			float ret = coords[0][0]*(coords[1][1]*coords[2][2]-coords[1][2]*coords[2][1]);
			ret -= 		coords[0][1]*(coords[1][0]*coords[2][2]-coords[1][2]*coords[2][0]);
			ret += 		coords[0][2]*(coords[1][0]*coords[2][1]-coords[1][1]*coords[2][0]);
			return ret;
			
		}
		throw new Error("Unimplemented");
	}

	public Vector[] getEigenvectors(){
		double[][] array = new double[M][N];
		for(int i=0;i<M;i++) for(int j=0;j<N;j++) array[i][j] = coords[i][j];
		Jama.Matrix A = new Jama.Matrix(array);
		EigenvalueDecomposition ed = new EigenvalueDecomposition(A);
		Jama.Matrix eigenM = ed.getV();
		Vector[] ret = new Vector[eigenM.getColumnDimension()];
		double[] eigenVals = ed.getRealEigenvalues();
		for(int i=0;i<ret.length;i++){
			ret[i] = new Vector(eigenM.get(0, i), eigenM.get(1, i), eigenM.get(2, i)).timesIn((float)eigenVals[i]);
			//ret[i] = new Vector(eigenM.get(0, i), eigenM.get(1, i), eigenM.get(2, i));
		}
		return ret;
	}
	public double[] getEigenValues(){
		assert M==3&&N==3;
		double a = -1;
		double b = coords[0][0]+coords[1][1]+coords[2][2];
		double c = -coords[0][0]*coords[2][2]-
		coords[1][1]*coords[2][2]-
		coords[0][0]*coords[1][1]+
		coords[1][2]*coords[2][1]+
		coords[0][1]*coords[1][0]+
		coords[0][2]*coords[2][0];
		double d = coords[0][0]*coords[1][1]*coords[2][2]-
		coords[0][0]*coords[1][2]*coords[2][1]+
		coords[0][1]*coords[1][2]*coords[2][0]-
		coords[0][1]*coords[1][0]*coords[2][2]+
		coords[0][2]*coords[1][0]*coords[2][1]-
		coords[0][2]*coords[1][1]*coords[2][0];
		double[] cubePars = {a,b,c,d};
		double[] roots = Polynomial.solve(cubePars);

		if(Math.abs(roots[3])>0.000001){
			return new double[]{roots[0]};
		}else{
			if(Math.abs(roots[1]-roots[2])<0.000001)
				return new double[]{roots[0], roots[1]};
			else
				return new double[]{roots[0], roots[1],roots[2]};
		}
	}


	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<M;i++) {
			sb.append('|');
			for(int j=0;j<N;j++) {
				sb.append(String.format("%5f", coords[i][j]));
				sb.append(' ');
			}
			sb.append('|');
			sb.append('\n');
		}
		return sb.toString();
	}
	public static Matrix createIdentityMatrix(int n) {
		Matrix ret = new Matrix(n,n);
		for(int i=0;i<n;i++) ret.coords[i][i] = 1;
		return ret;
	}
	public float get(int i, int j) {
		return coords[i][j];
	}
}
