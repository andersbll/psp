package edu.geom2D;

import java.util.List;

import edu.math.Vector;
import edu.math.Vector2D;

public class Circle {
	Vector2D center;
	public float radius;
	public Circle(Vector2D center, float radius){
		this.center = center;
		this.radius = radius;
	}
	public Circle(Vector2D p1, Vector2D p2){
		this(p1.plus(p2).timesIn(0.5f), p1.distance(p2)/2f);
	}
	public Circle(Vector2D p1, Vector2D p2, Vector2D p3){
		Vector z = new Vector(0,0,1);
		Line2D l1 = new Line2D(p1.plus(p2).timesIn(0.5f), new Vector2D(p1.vectorTo(p2).cross(z)));
		Line2D l2 = new Line2D(p1.plus(p3).timesIn(0.5f), new Vector2D(p1.vectorTo(p3).crossIn(z)));
		center = l1.intersection(l2);
		radius = center.distance(p1);
	}

	public Circle(Circle c1, Circle c2){
		if(c1.contains(c2)){
			center = c1.center.clone();
			radius = c1.radius;
		}else if(c2.contains(c1)){
			center = c2.center.clone();
			radius = c2.radius;
		}else if(c1.center.equals(c2.center)){
			center = c1.center.clone();
			radius = Math.max(c1.radius, c2.radius);
		}else{
			Line2D l = new Line2D(c1.center, c1.center.vectorTo(c2.center).normIn());
			center = l.getPoint(0.5f*c1.center.distance(c2.center)-c1.radius/2+c2.radius/2);
			radius = center.distance(c1.center)+c1.radius;
		}
	}


	/** Construct circle that touches all specified circles using a numerical method. 
	 * Assumes that two of these circles have equal radii. */
	public Circle(Circle circle1, Circle circle2, Circle circle3){

		Circle c1 = circle1;
		Circle c2 = circle2;
		Circle c3 = circle3;

		Circle tmp;
		if( (tmp=new Circle(c1,c2)).contains(c3)) { center = tmp.center; radius = tmp.radius; return;}
		if( (tmp=new Circle(c1,c3)).contains(c2)) { center = tmp.center; radius = tmp.radius; return;}
		if( (tmp=new Circle(c2,c3)).contains(c1)) { center = tmp.center; radius = tmp.radius; return;}

		Circle enc = ApolloniusSolver.solveApollonius(c1, c2, c3, 1, 1, 1);
		this.center = enc.center;
		this.radius = enc.radius;
		
		/*if(Math.abs(c1.radius-c3.radius)<c1.radius*0.0001) { c2 = c3; c3 = circle2; }
		else if(Math.abs(c2.radius-c3.radius)<c2.radius*0.0001) { c1 = c3; c3 = circle1; }
		//Now c1 and c2 have equal radii


		Line2D l = c1.bisector(c2);
		if(l.direction.dot(c1.center.vectorTo(c3.center))<0) l.direction.timesIn(-1);
		Line2D l0 = c1.bisector(c3);
		//9HOps
		
		float t = l.intersectionParameter(l0) + 1.7f; //5
		float t0 = t;
		double radMax = Math.max(c1.radius, c3.radius);
		Vector2D p = l.getPoint(t);//3
		float diff = Math.abs(p.distance(c1.center)-p.distance(c3.center)-c3.radius+c1.radius);//8
		float delta = 1;
		int it = 0;
		while(diff>0.001 && Math.abs(delta)>0.001){
			float newT = t+delta;
			Vector2D newP = l.getPoint(newT);//3
			float d1 = newP.distance(c1.center);//4
			float d3 = newP.distance(c3.center);//4
			float newDiff = Math.abs( (d1+c1.radius)-(d3+c3.radius));
			if(newDiff>diff || Math.abs(newT-t0)>radMax ) 	delta*=-0.25;//1
			//else				delta*=1.2;
			diff = newDiff;
			t = newT;
			p = l.getPoint(t);//3
			it++;
		}
		//System.out.println("Took "+it+" iterations");
		center = l.getPoint(t);//3
		radius = Math.max(p.distance(c1.center)+c1.radius, p.distance(c3.center)+c3.radius);//8
		*/
		//The part that Apollonius can solve (68HOps) takes (assuming 10 iterations) 16 + 10*15 + 11 = 177HOps
	}

	/** Construct circle that contains all four specified circles. Assumes that 
	 * the circles have pairwise identical radii. */
	public Circle(Circle c1, Circle c2, Circle c3, Circle c4){
		//Check all pairs
		Circle c = new Circle(c1, c4);
		if(c.contains(c2) && c.contains(c3)) { center = c.center; radius = c.radius; return;}
		c = new Circle(c1, c3);
		if(c.contains(c2) && c.contains(c4)) { center = c.center; radius = c.radius; return;}
		c = new Circle(c1, c2);
		if(c.contains(c3) && c.contains(c4)) { center = c.center; radius = c.radius; return;}
		c = new Circle(c2, c3);
		if(c.contains(c1) && c.contains(c4)) { center = c.center; radius = c.radius; return;}
		c = new Circle(c2, c4);
		if(c.contains(c1) && c.contains(c3)) { center = c.center; radius = c.radius; return;}
		c = new Circle(c3, c4);
		if(c.contains(c1) && c.contains(c2)) { center = c.center; radius = c.radius; return;}

		//Check all triples
		Circle triC1 = new Circle(c1,c2,c3);
		Circle triC2 = new Circle(c1,c2,c4);
		Circle triC3 = new Circle(c1,c3,c4);
		Circle triC4 = new Circle(c2,c3,c4);

		Circle ret = null;
		if( triC1.contains(c4) ) ret = triC1;
		if( triC2.contains(c3) && (ret==null || triC2.radius<ret.radius) ) ret = triC2;
		if( triC3.contains(c2) && (ret==null || triC3.radius<ret.radius) ) ret = triC3;
		if( triC4.contains(c1) && (ret==null || triC4.radius<ret.radius) ) ret = triC4;
		if(ret==null) {

			throw new Error("Couldnt find enclosing circle");
		}
		ret = triC4;
		radius = ret.radius;
		center = ret.center;
	}

	public Vector2D getCenter(){ return center; }
	public float getRadius(){ return radius; }

	public boolean contains(Vector2D p){
		return center.distance(p)<=(radius+0.0001);
	}
	public boolean contains(Circle c){
		return radius>=center.distance(c.center)+c.radius;
	}

	/** Assumes that radii of this and c are identical */
	public Line2D bisector(Circle c){
		Vector dir = center.vectorTo(c.center).crossIn(new Vector(0,0,1)).normIn();
		return new Line2D( 
				center.plus(center.vectorTo(c.center).timesIn(0.5f)),  
				new Vector2D(dir.x(), dir.y())
		);
	}

	public static Circle minimumEnclosingCircle_bruteforce(List<Vector2D> points){
		float minRad = Float.POSITIVE_INFINITY;
		Circle minCircle = null;
		for(int i=0;i<points.size();i++){
			for(int j=0;j<points.size();j++){
				if(i==j) continue;
				Circle tmp = new Circle(points.get(i), points.get(j));
				boolean containsAll = true;
				for(Vector2D p: points) if(!tmp.contains(p)) {containsAll=false;break;}
				if(containsAll && tmp.radius<minRad){
					minRad = tmp.radius;
					minCircle = tmp;
				}
			}
		}


		for(int i=0;i<points.size();i++){
			for(int j=0;j<points.size();j++){
				for(int k=0;k<points.size();k++){
					if(i==j || i==k || j==k) continue;
					Circle tmp = new Circle(points.get(i), points.get(j), points.get(k));
					boolean containsAll = true;
					for(Vector2D p: points) if(!tmp.contains(p)) {containsAll=false;break;}
					if(containsAll && tmp.radius<minRad){
						minRad = tmp.radius;
						minCircle = tmp;
					}
				}
			}

		}
		if(minCircle==null) throw new Error("minCircle not set .. "+points.size());
		return minCircle;
	}

	public boolean equals(Circle c){
		return c.center.equals(center) && Math.abs(c.radius-radius)<0.00001;
	}
	public String toString(){
		return String.format("Circle[center:%s,radius:%.2f]", center, radius);
	}

}
