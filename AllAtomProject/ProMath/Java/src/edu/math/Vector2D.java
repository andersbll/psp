package edu.math;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;


public class Vector2D extends Vector{
	public Vector2D(float dX, float dY){
		super(dX, dY, 0);
	}
	public Vector2D(float dX, float dY, float dZ){
		this(dX,dY);
	}
	public Vector2D(Point p){
		this(p.x,p.y);
	}
	public Vector2D(double dX, double dY) {
		this((float)dX,(float)dY);
	}

	/**Cached values*/
	private float a00=-100,a10=-100,a20,a01,a11,a21,a02,a12,a22;

	/** Method to reset cache */
	protected void valuesChanged(){
		a00=-100;
		a10=-100;
	}

	public Vector2D rotate(Point p, double angle){
		return rotate(p,(float)angle);
	}
	public Vector2D rotate(Point p, float angle){
		if(a00==-100 && a10==-100){
			float l = length();
			if(l==0) throw new Error("Trying to rotate around 0-vector");
			if(!(l>0) && !(l<=0) ) throw new Error("Trying to rotate NaN");

			float ux = x/l;
			float uy = y/l;
			float uz = z/l;

			a00 = (float) (ux*ux + cos(angle)*(1.0-ux*ux));
			a10 = (float) (ux*uy*(1.0-cos(angle))+uz*sin(angle));
			a20 = (float) (uz*ux*(1-cos(angle)) - uy*sin(angle));

			a01 = (float) (ux*uy*(1-cos(angle)) - uz*sin(angle));
			a11 = (float) (uy*uy + cos(angle)*(1.0-uy*uy));
			a21 = (float) (uy*uz*(1.0-cos(angle)) + ux*sin(angle));

			a02 = (float) (uz*ux*(1.0-cos(angle)) + uy*sin(angle));
			a12 = (float) (uy*uz*(1.0-cos(angle)) - ux*sin(angle));
			a22 = (float) (uz*uz + cos(angle)*(1.0 - uz*uz));
		}

		return new Vector2D(
				a00*p.x+a01*p.y+a02*p.z,
				a10*p.x+a11*p.y+a12*p.z,
				a20*p.x+a21*p.y+a22*p.z
		);
	}
	
	public float rotationAngle(Vector2D v){
		System.out.println(this.cross(v).z);
		if(this.cross(v).z>=0) 	return this.angle(v);
		else 					return (float)(2*Math.PI-this.angle(v));
	}

	public final float dot(Vector2D v){
		return x*v.x + y*v.y + z*v.z;
	}
	public final Vector2D times(float s){
		return new Vector2D(s*x, s*y, s*z);
	}
	public final Vector2D timesIn(float s){
		x*=s; y*=s; z*=s;
		return this;
	}
	public Vector2D plus(Vector2D v) {		return new Vector2D(x+v.x, y+v.y, z+v.z);	}
	
	/*public Vector2D cross(Vector2D v){
		return new Vector2D(
				y*v.z-z*v.y,
				-x*v.z+z*v.x,
				x*v.y-y*v.x
		);
	}*/
	public Vector2D norm(){
		float l = length();
		if(l==0) throw new Error("Trying to normalize 0-vector");
		if(!(l>0) && !(l<=0) ) throw new Error("Trying to normalize NaN");
		return this.times(1/l);
	}
	public Vector2D normIn(){
		float l = length();
		if(l==0) throw new Error("Trying to normalize 0-vector");
		if(!(l<0) && !(l>=0)) throw new Error("Trying to normalize NaN");
		valuesChanged();
		return timesIn(1/l);	
	}
	public final float angle(Vector2D v){
		return (float) Math.acos(this.norm().dot(v.norm()));
	}

	public String toString(){
		return String.format("Vector2D(%7.3f,%7.3f)",x,y);
	}
	public Vector2D clone(){
		return new Vector2D(this);
	}

	public Vector2D vectorTo(Vector2D p){
		Vector2D v = new Vector2D(p.x-x, p.y-y);
		return v;
	}

	public boolean equals(Object o){
		float epsilon = 0.00001f;
		if(o instanceof Vector2D){
			Vector2D v = (Vector2D)o;

			return Math.abs(v.x-x)<epsilon && Math.abs(v.y-y)<epsilon && Math.abs(v.z-z)<epsilon;
		}else{
			if(o instanceof Point){
				Point v = (Point)o;
				return Math.abs(v.x-x)<epsilon && Math.abs(v.y-y)<epsilon && Math.abs(v.z-z)<epsilon;
			}else return false;
		}
	}

	public static boolean isLeftTurn(Vector2D v1, Vector2D v2, Vector2D v3){
		Vector cross = v1.vectorTo(v2).crossIn(v2.vectorTo(v3));
		return cross.z>0;
	}

	/** Computes the convex hull using graham scan */
	public static List<Vector2D> getConvexHull(List<Vector2D> points){
		Vector2D p0 = points.get(0);
		for(Vector2D p: points)	{
			if(p.y<p0.y) p0 = p;
			else if(p.y==p0.y && p.x<p0.x) p0 = p;
		}

		List<Vector2D> ret = new ArrayList<Vector2D>();
		ret.addAll(points);
		ret.remove(p0);
		class AngleComparator implements Comparator<Vector2D>{
			Vector2D p0;
			AngleComparator(Vector2D p) { p0 = p;}
			public int compare(Vector2D o1, Vector2D o2) {
				Vector2D v = p0.vectorTo(o1);
				float angle1 = (float)Math.atan2(v.y, v.x);
				v = p0.vectorTo(o2);
				float angle2 = (float)Math.atan2(v.y, v.x);
				return Float.compare(angle1, angle2);
			}
		};
		AngleComparator ac = new AngleComparator(p0);
		Collections.sort(ret, ac);
		ret.add(0,p0);

		Stack<Vector2D> s = new Stack<Vector2D>();
		s.push(ret.get(0));
		s.push(ret.get(1));
		if(ret.size()==2) return ret;
		
		s.push(ret.get(2));
		for(int i=3;i<ret.size();i++){
			while(true){
				Vector2D top = s.pop();
				Vector2D nextToTop = s.peek();
				Vector v1 = nextToTop.vectorTo(top);
				Vector v2 = top.vectorTo(ret.get(i));
				if(v1.cross(v2).z>0){
					s.push(top);
					break;
				}
			}
			s.push(ret.get(i));
		}

		ret.clear();
		ret.addAll(s);
		return ret;
	}
	public static void main(String[] args){
		List<Vector2D> points = new LinkedList<Vector2D>();
		points.add(new Vector2D(4,4));
		points.add(new Vector2D(1,4));
		points.add(new Vector2D(4,2));
		points.add(new Vector2D(2,1));
		points.add(new Vector2D(3,1));
		points.add(new Vector2D(3,3));
		Collections.shuffle(points);

		List<Vector2D> cHull = Vector2D.getConvexHull(points);
		for(Vector2D p: cHull){
			System.out.println(p);
		}
	}

	/**
	 * Please do not change origo
	 */
	public static Vector2D O(){ return new Vector2D(0,0,0); }
}
