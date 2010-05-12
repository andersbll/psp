package edu.math;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * A representation of a cartesian float-based vector. Coordinates can be changed using the 
 * <code>setX(float x), setY(float y), setZ(float z)</code> methods and they can be retrieved 
 * using similar getter-methods. 
 * @author R. Fonseca
 */
public class Vector extends Point{
	public Vector(float dX, float dY, float dZ){
		super(dX,dY,dZ);
	}
	public Vector(Point p){
		this(p.x,p.y,p.z);
	}
	public Vector(double dX, double dY, double dZ) {
		this((float)dX,(float)dY,(float)dZ);
	}

	public final float length(){ 
		if(length==-1) length = (float) Math.sqrt(lengthSquared()); 
		return length;
	}
	public final float lengthSquared(){ 
		return x*x+y*y+z*z; 
	}

	public float get(int i){
		switch(i){
		case 0: return x;
		case 1: return y;
		case 2: return z;
		default: return Float.NaN;
		}
	}
	
	/**Cached values*/
	private float a00=-100,a10=-100,a20,a01,a11,a21,a02,a12,a22;
	private float length = -1;
	
	/** Method to reset cache */
	protected void valuesChanged(){
		a00=-100;
		a10=-100;
		length = -1;
	}
	
	public Vector rotate(Point p, double angle){
		return rotate(p,(float)angle);
	}
	public Vector rotate(Point p, float angle){
			if(a00==-100 && a10==-100){
			float l = length();
			if(l==0) throw new Error("Trying to rotate around 0-vector");
			if(!(l>0) && !(l<=0) ) throw new Error("Trying to rotate NaN");
			
			float ux = x/l;
			float uy = y/l;
			float uz = z/l;
			float sin = (float)sin(angle);
			float cos = (float)cos(angle);
			
			a00 = (float) (ux*ux + cos*(1.0-ux*ux));
			a10 = (float) (ux*uy*(1.0-cos)+uz*sin);
			a20 = (float) (uz*ux*(1-cos) - uy*sin);

			a01 = (float) (ux*uy*(1-cos) - uz*sin);
			a11 = (float) (uy*uy + cos*(1.0-uy*uy));
			a21 = (float) (uy*uz*(1.0-cos) + ux*sin);

			a02 = (float) (uz*ux*(1.0-cos) + uy*sin);
			a12 = (float) (uy*uz*(1.0-cos) - ux*sin);
			a22 = (float) (uz*uz + cos*(1.0 - uz*uz));
		}

		return new Vector(
				a00*p.x+a01*p.y+a02*p.z,
				a10*p.x+a11*p.y+a12*p.z,
				a20*p.x+a21*p.y+a22*p.z
		);
	}

	public final float dot(Vector v){
		return x*v.x + y*v.y + z*v.z;
	}
	public Vector cross(Vector v){
		return new Vector(
				y*v.z-z*v.y,
				-x*v.z+z*v.x,
				x*v.y-y*v.x
		);
	}
	public Vector norm(){
		float l = length();
		if(l==0) throw new Error("Trying to normalize 0-vector");
		if(!(l>0) && !(l<=0) ) throw new Error("Trying to normalize NaN");
		return this.times(1/l);
	}
	public Vector times(float s){
		return new Vector(s*x, s*y, s*z);
	}
	public final float angle(Vector v){
		return (float) Math.acos(Math.min(1,this.norm().dot(v.norm())));
	}
	public static float dihedralAngle(Vector v1, Vector v2, Vector v3){
		return (float)Math.atan2(
				v1.norm().timesIn(v2.length()).dot(v2.cross(v3)),
                 v1.cross(v2).dot(v2.cross(v3)));
	}
	

	public final Point plus(Point p) {		return new Point(x+p.x, y+p.y, z+p.z);	}
	public final Vector plus(Vector v) {		return new Vector(x+v.x, y+v.y, z+v.z);	}
	public final Vector minus(Vector v) {		return new Vector(x-v.x, y-v.y, z-v.z);	}

	public final Vector plusIn(Point v) {
		if(!(v.x>0) && !(v.x<=0) ) throw new Error("Trying to add NaN");
		x+=v.x;		y+=v.y;		z+=v.z;		valuesChanged();	return this;}
	public final Vector minusIn(Point v) {	x-=v.x;		y-=v.y;		z-=v.z;		valuesChanged();	return this;}
	public Vector timesIn(float s){
		if(!(s>0) && !(s<=0) ) throw new Error("Trying to multiply NaN");
		x*=s;		y*=s;		z*=s;		valuesChanged();	return this;}
	public final Vector divideIn(float s){	
		if(s==0) throw new Error("Trying to divide with 0");
		if(!(s>0) && !(s<=0) ) throw new Error("Trying to divide NaN");
		x/=s;		y/=s;		z/=s;	valuesChanged();	return this;}
	public final Vector scaleXIn(float s){	x*=s;	valuesChanged();	return this; }
	public final Vector scaleYIn(float s){	y*=s;	valuesChanged();	return this; }
	public final Vector scaleZIn(float s){	z*=s;	valuesChanged();	return this; }
	public final Vector crossIn(Vector v){	
		float newx = y*v.z-z*v.y;
		float newy = -x*v.z+z*v.x;
		float newz = x*v.y-y*v.x;
		x = newx; y=newy; z=newz;
		valuesChanged();	
		return this;
	}
	public Vector normIn(){
		float l = length();
		if(l==0) throw new Error("Trying to normalize 0-vector");
		if(!(l<0) && !(l>=0)) throw new Error("Trying to normalize NaN");
		valuesChanged();
		return timesIn(1/l);	
	}

	public String toString(){
		return String.format("Vector(%7.3f,%7.3f,%7.3f)",x,y,z);
	}
	public Vector clone(){
		return new Vector(this);
	}

	public boolean equals(Object o){
		float epsilon = 0.00001f;
		if(o instanceof Vector){
			Vector v = (Vector)o;

			return Math.abs(v.x-x)<epsilon && Math.abs(v.y-y)<epsilon && Math.abs(v.z-z)<epsilon;
		}else{
			if(o instanceof Point){
				Point v = (Point)o;
				return Math.abs(v.x-x)<epsilon && Math.abs(v.y-y)<epsilon && Math.abs(v.z-z)<epsilon;
			}else return false;
		}
	}

	public static Vector createRandomVector(){
		float min = -1;
		float max = 1;
		return new Vector(
				Toolbox.randBetween(min, max),
				Toolbox.randBetween(min, max),
				Toolbox.randBetween(min, max)).normIn();
	}

	/**
	 * Please do not change origo
	 */
	public static Vector O(){ return new Vector(0,0,0); }
}
