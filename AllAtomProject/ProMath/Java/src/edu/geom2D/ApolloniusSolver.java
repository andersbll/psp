package edu.geom2D;

import edu.math.Polynomial;
import edu.math.Vector2D;

public class ApolloniusSolver {

	/** Solves the Apollonius problem of finding a circle tangent to three other circles in the plane. 
	 * The method uses approximately 68 heavy operations (multiplication, division, square-roots).
	 * @param c1 One of the circles in the problem 
	 * @param c2 One of the circles in the problem
	 * @param c3 One of the circles in the problem
	 * @param s1 An indication if the solution should be externally or internally tangent (+1/-1) to c1
	 * @param s2 An indication if the solution should be externally or internally tangent (+1/-1) to c2
	 * @param s3 An indication if the solution should be externally or internally tangent (+1/-1) to c3
	 * @return The solution to the problem of Apollonius. 
	 */
	public static Circle solveApollonius(Circle c1, Circle c2, Circle c3, int s1, int s2, int s3){
		float x1 = c1.center.x();
		float y1 = c1.center.y();
		float r1 = c1.radius;
		float x2 = c2.center.x();
		float y2 = c2.center.y();
		float r2 = c2.radius;
		float x3 = c3.center.x();
		float y3 = c3.center.y();
		float r3 = c3.radius;

		float v11 = 2*x2 - 2*x1;
		float v12 = 2*y2 - 2*y1;
		float v13 = x1*x1 - x2*x2 + y1*y1 - y2*y2 - r1*r1 + r2*r2;
		float v14 = 2*s2*r2 - 2*s1*r1;
		//14HOps

		float v21 = 2*x3 - 2*x2;
		float v22 = 2*y3 - 2*y2;
		float v23 = x2*x2 - x3*x3 + y2*y2 - y3*y3 - r2*r2 + r3*r3;
		float v24 = 2*s3*r3 - 2*s2*r2;
		//28HOps
		
		float w12 = v12/v11;
		float w13 = v13/v11;
		float w14 = v14/v11;
		//31HOps
		
		float w22 = v22/v21-w12;
		float w23 = v23/v21-w13;
		float w24 = v24/v21-w14;
		//34HOps
		
		float P = -w23/w22;
		float Q = w24/w22;
		float M = -w12*P-w13;
		float N = w14 - w12*Q;
		//38HOps
		
		float a = N*N + Q*Q - 1;
		float b = 2*M*N - 2*N*x1 + 2*P*Q - 2*Q*y1 + 2*s1*r1;
		float c = x1*x1 + M*M - 2*M*x1 + P*P + y1*y1 - 2*P*y1 - r1*r1;
		//59HOps
		
		double[] quadSols = Polynomial.solve(new double[]{a,b,c}); //7 Hops
		float rs = (float)quadSols[0];
		float xs = M+N*rs;
		float ys = P+Q*rs;
		//68HOps
		
		return new Circle(new Vector2D(xs,ys), rs);
	}
	
}
