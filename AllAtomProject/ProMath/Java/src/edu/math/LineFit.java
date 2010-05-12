package edu.math;

public class LineFit {

	private static void tryAgain(){
		float[] xs = new float[100];
		float[] ys = new float[xs.length];
		int i=0;
		float var = 0.1f;
		for(float x=0;x<1;x+=(1f/xs.length)){
			xs[i] = x+((float)Math.random()*var*2-var);
			ys[i++] = x+((float)Math.random()*var*2-var);
			//System.out.println("(x,y)=("+xs[i]+","+ys[i]+")");
			if(i==xs.length) break;
		}
		float[] ret = linFit(xs, ys);
		for(float f: ret)
			System.out.println("> "+f);
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		tryAgain();
	}
	// "Java Tech"
	//  Code provided with book for educational purposes only.
	//  No warranty or guarantee implied.
	//  This code freely available. No copyright claimed.
	//  2003

	/**
	 *  Use the Least Squares fit method for fitting a
	 *  straight line to 2-D data for measurements
	 *  y[i] vs. dependent variable x[i]. This fit assumes
	 *  there are errors only on the y measuresments as
	 *  given by the sigmaY array.<br><br>
	 *  See, e.g. Press et al., "Numerical Recipes..." for details
	 *  of the algorithm.
	 */
	public static float[] linFit(float[] x, float[] y){
		assert(x.length==y.length);
		float s=0.0f,sx=0.0f,sy=0.0f,sxx=0.0f,syy=0.0f,sxy=0.0f,del;

		// Null sigmaY implies a constant error which drops
		// out of the divisions of the sums.
		s = x.length;
		for(int i=0; i < x.length; i++){
			sx  += x[i];
			sy  += y[i];
			sxx += x[i]*x[i];
			sxy += x[i]*y[i];
			syy += y[i]*y[i];
		}

		del = s*sxx - sx*sx;

		float[] parameters = new float[3];
		// Intercept
		parameters[0] = (sxx*sy -sx*sxy)/del;
		// Slope
		parameters[1] = (s*sxy -sx*sy)/del;


		// Errors (sd**2) on the:
		// intercept
		//parameters[2] = sxx/del;
		// and slope
		//parameters[3] = s/del;

		float meanX = sx/s;
		float meanY = sy/s;
		float SSxx = sxx-s*meanX*meanX;
		float SSyy = syy-s*meanY*meanY;
		float SSxy = sxy-s*meanX*meanY;
		parameters[2] = SSxy*SSxy/(SSxx*SSyy);
		
		return parameters;
	}

}
