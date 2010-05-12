package edu.math;



public class Point {
	float  x,y,z;
	public Point(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector vectorTo(Point p){
		Vector v = new Vector(p.x-x, p.y-y, p.z-z);
		return v;
	}

	public float distanceSquared(Point p){
		float dx = p.x-x;
		float dy = p.y-y;
		float dz = p.z-z;
		return dx*dx + dy*dy + dz*dz;
	}
	public final float distance(Point p){
		return (float)Math.sqrt(distanceSquared(p));
	}
	
	public final float x(){ return x; }
	public final float y(){ return y; }
	public final float z(){ return z; }

	public final void setX(float x){ this.x = x; valuesChanged(); }
	public final void setY(float y){ this.y = y; valuesChanged(); }
	public final void setZ(float z){ this.z = z; valuesChanged(); }
	public final void set(int c, float v){
		switch(c){
		case 0:	this.x = v; valuesChanged();break;
		case 1:	this.y = v; valuesChanged();break;
		case 2:	this.z = v; valuesChanged();break;
		} 
	}
	
	protected void valuesChanged(){}
	public boolean equals(Object o){
		if(o instanceof Point){
			Point v = (Point)o;
			float epsilon = 0.00001f;
			return Math.abs(v.x-x)<epsilon && Math.abs(v.y-y)<epsilon && Math.abs(v.z-z)<epsilon;
		}
		return false;
	}
	
	public String toString(){
		return String.format("Point(%.2f,%.2f,%.2f)", x,y,z);
	}
}
