package edu.math;

import static java.lang.Math.sqrt;
import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.cos;
import static java.lang.Math.acos;
import static java.lang.Math.PI;

public class Polynomial {
	public final double[] parameters;
	
	Polynomial(double[] pars){
		parameters = pars;
	}
	
	public double[] solve(){
		return solve(parameters);
	}
	public static double[] solve(double[] parameters){
		if(parameters.length==3) return solveSecondDegree(parameters);
		if(parameters.length==4) return solveThirdDegree(parameters);
		
		return null;
	}

	public static double[] solve(double a, double b, double c){
		return solveSecondDegree(new double[]{a,b,c});
	}
	
	/** 
	 * Solves quadratic equation. Uses 3 to 7 HOps.  
	 */
	private static double[] solveSecondDegree(double[] parameters){
		double a = parameters[0];
		double b = parameters[1];
		double c = parameters[2];
		double D = b*b-4*a*c;
		if(D<0) return new double[]{};
		if(D==0) return new double[]{-b/(2*a)};
		else {
			double aa = 2*a;
			double sqD = Math.sqrt(D);
			return new double[]{(-b-sqD)/aa, (-b+sqD)/aa};
		}
	}
	
	private static double[] solveThirdDegree(double[] parameters){
		double a = parameters[1]/parameters[0];
		double b = parameters[2]/parameters[0];
		double c = parameters[3]/parameters[0];
		
		double Q = (3*b-a*a)/9;
		//    R := (9*a1*a2 - 27*a0 - 2*a2^3)/54
		double R = (9*a*b-27*c-2*a*a*a)/54;
	    //D := Q^3 + R^2                          % polynomial discriminant
		double D = Q*Q*Q + R*R;
		
		double root1, root2, root3, im;
		
		if(D>=0){		//Complex or duplicate roots
			//S := sgn(R + sqrt(D))*rabs(R + sqrt(D))^(1/3)
			double S = sign(R+sqrt(D))*pow(abs(R+sqrt(D)),1/3d);
	        //T := sgn(R - sqrt(D))*rabs(R - sqrt(D))^(1/3)
			double T = sign(R-sqrt(D))*pow(abs(R-sqrt(D)),1/3d);
			
			//z1 := -a2/3 + (S + T)
			root1 = -(a/3) + S+T;
			//z2 := -a2/3 - (S + T)/2             % real part of complex root
			root2 = -(a/3) - (S+T)/2;
	        //z3 := -a2/3 - (S + T)/2             % real part of complex root
			root3 = -(a/3) - (S+T)/2;
	        //im := rabs(sqrt(3)*(S - T)/2)       % complex part of root pair
			im = abs(sqrt(3)*(S-T)/2);
		}else{
			//th := arccos(R/sqrt( -Q^3))
			double th = acos(R/sqrt(-Q*Q*Q));
	        
	        //z1 := 2*sqrt(-Q)*cos(th/3) - a2/3
			root1 = 2*sqrt(-Q)*cos(th/3)-a/3;
	        //z2 := 2*sqrt(-Q)*cos((th + 2*pi)/3) - a2/3
			root2 = 2*sqrt(-Q)*cos((th+2*PI)/3)-a/3;
	        //z3 := 2*sqrt(-Q)*cos((th + 4*pi)/3) - a2/3
			root3 = 2*sqrt(-Q)*cos((th+4*PI)/3)-a/3;
			//im := 0
			im = 0;
		}
		
		
		
		return new double[]{root1,root2, root3, im};
	}
	
	private static double sign(double n){
		if(n<0) return -1;
		else return 1;
	}
	
	
	public static void main(String[] args){
		double a = -1, b=0, c=0, d=10;
		Polynomial p = new Polynomial(new double[]{a,b,c,d});
		double[] roots = p.solve();
		for(double r: roots) System.out.println(r);
	}
}
