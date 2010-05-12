package edu.math;



public class LineSegment {
	public Vector p1, p2;
	public LineSegment(Vector p1, Vector p2){
		this.p1 = p1;
		this.p2 = p2;
		
	}
	
	
	public Vector getDirection(){ return p1.vectorTo(p2); }
	
	
	public LineSegment clone(){
		return new LineSegment(p1.clone(), p2.clone());
	}
	
	public String toString(){
		return "LineSegment[p1="+p1+",p2="+p2+"]";
	}
}
