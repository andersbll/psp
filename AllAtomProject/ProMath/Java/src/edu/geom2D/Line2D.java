package edu.geom2D;

import edu.math.Vector;
import edu.math.Vector2D;



public class Line2D {
	Vector2D point;
	Vector2D direction;
	public Line2D(Vector2D p, Vector2D d){
		this.point = p;
		this.direction = d;
	}
	
	public Vector2D getPoint(){ return point; }
	public Vector2D getDirection(){ return direction; }
	
	public Vector2D intersection(Line2D l){
		
		return getPoint(intersectionParameter(l));
	}
	
	public String toString(){
		return "Line2D[p="+point+",v="+direction+"]";
	}

	public float intersectionParameter(Line2D l) {
		float denom = l.direction.y()*direction.x()-l.direction.x()*direction.y();
		Vector2D c = l.point.vectorTo(point);
		float s = (l.direction.x()*c.y()-l.direction.y()*c.x())/denom;
		return s;
	}

	public Vector2D getPoint(float t) {
		return point.plus(direction.times(t));
	}
}
