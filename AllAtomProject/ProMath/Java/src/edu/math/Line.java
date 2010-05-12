package edu.math;



public class Line {
	Vector point;
	Vector direction;
	public Line(Vector p, Vector d){
		this.point = p;
		this.direction = d.norm();
	}
	
	public Vector rotate(Point p, float angle){
		//System.out.println("Line.rotate("+p+","+angle+")");
		Vector ret = new Vector(p.x, p.y, p.z);
		return rotateIn(ret, angle);
	}
	
	public Vector getPoint(){ return point; }
	public Vector getDirection(){ return direction; }
	public Vector getPoint(float t){ return point.plus(direction.times(t)); }
	
	
	public Vector rotateIn(Vector p, float angle){
		Vector tmp = new Vector(p);
		tmp.minusIn(point);
		tmp = direction.rotate(tmp, angle);
		tmp.plusIn(point);
		p.setX(tmp.x);
		p.setY(tmp.y);
		p.setZ(tmp.z);
		return p;
	}	
	public Point rotateIn(Point p, float angle){
		Vector tmp = new Vector(p);
		tmp.minusIn(point);
		tmp = direction.rotate(tmp, angle);
		tmp.plusIn(point);
		p.setX(tmp.x);
		p.setY(tmp.y);
		p.setZ(tmp.z);
		return p;
	}
	
	public Vector getNormal(){
		Vector ret = new Vector(1,0,0).crossIn(direction);
		if(ret.length()==0) ret = new Vector(0,1,0).crossIn(direction);
		return ret.normIn();
	}
	public float distance(Vector v){
		float nom = direction.cross(point.minus(v)).length();
		float den = direction.length();
		return nom/den;
	}
	public float projectOnto(Vector v){
		float nom = v.vectorTo(point).dot(direction);
		float den = direction.lengthSquared();
		return -nom/den;
	}
	
	public Line clone(){
		return new Line(point.clone(), direction.clone());
	}
	
	public String toString(){
		return "Line[p="+point+",v="+direction+"]";
	}
}
