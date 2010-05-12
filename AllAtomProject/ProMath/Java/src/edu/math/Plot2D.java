package edu.math;

public class Plot2D {
	private float[] xs;
	private float[] ys;
	
	public Plot2D(int points){
		xs = new float[points];
		ys = new float[points];
	}
	public void setPoint(int p, float x, float y){
		xs[p] = x;
		ys[p] = y;
	}
	
	public float[] linFit(){
		float s=0.0f,sx=0.0f,sy=0.0f,sxx=0.0f,syy=0.0f,sxy=0.0f;

		s = xs.length;
		for(int i=0; i < s; i++){
			sx  += xs[i];
			sy  += ys[i];
			sxx += xs[i]*xs[i];
			sxy += xs[i]*ys[i];
			syy += ys[i]*ys[i];
		}

		float[] parameters = new float[3];
		float meanX = sx/s;
		float meanY = sy/s;
		float SSxx = sxx-sx*meanX;
		float SSyy = syy-sy*meanY;
		float SSxy = sxy-sx*meanY;
		parameters[1] = SSxy/SSxx;						//Set b
		parameters[0] = meanY-parameters[1]*meanX;		//Set a
		parameters[2] = SSxy*SSxy/(SSxx*SSyy);			//Set r^2
		
		return parameters;
	}
	
	public String toGnuplotString(){
		String ret = "#Plot2D";
		for(int i=0;i<xs.length;i++){
			ret +=xs[i]+" "+ys[i]+"\n";
		}
		return ret;
	}
}
