package edu.geom2D;

import java.util.List;

import edu.math.Vector2D;

public class Capsule2D {
	public Vector2D p1, p2;
	public float radius;
	
	public Capsule2D(Vector2D p1, Vector2D p2, float radius){
		this.p1 = p1;
		this.p2 = p2;
		this.radius = radius;
	}
	
	public static Capsule2D createBoundingCapsule_mesh(List<Vector2D> points){
		
		return null;
	}
	
}
