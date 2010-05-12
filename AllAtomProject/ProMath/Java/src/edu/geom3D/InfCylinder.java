package edu.geom3D;

import java.util.ArrayList;
import java.util.List;

import edu.geom2D.Circle;
import edu.math.Line;
import edu.math.Matrix;
import edu.math.Plane;
import edu.math.Vector;
import edu.math.Vector2D;

public class InfCylinder {
	Line line;
	float radius;

	public InfCylinder(Line l, float r){
		line = l;
		radius = r;
	}

	public Capsule capWithHalfSpheres(List<Vector> enclosedPoints){
		float lowerT = Float.POSITIVE_INFINITY, upperT=Float.NEGATIVE_INFINITY;
		/*if(radius<0.0001f){
			for(Vector p: enclosedPoints){
				float intersection = line.projectOnto(p);
				if(intersection>upperT) upperT = intersection;
				if(intersection<lowerT) lowerT = intersection;
			}
		}else{*/
		for(Vector p: enclosedPoints){
			Sphere s = new Sphere(p, radius+0.00001f);
			float[] intersections = s.intersectionParameters(line);
			if(intersections.length<2){
				float intersection = line.projectOnto(p);
				if(intersection>upperT) upperT = intersection;
				if(intersection<lowerT) lowerT = intersection;
			}else{
				if(intersections[0]>upperT) upperT = intersections[0];
				if(intersections[1]<lowerT) lowerT = intersections[1];
			}
		}
		//}
		return new Capsule(line.getPoint(lowerT), line.getPoint(upperT), radius);
	}

	public Cylinder capWithDiscs(List<Vector> enclosedPoints){
		float lowerT = Float.POSITIVE_INFINITY, upperT=Float.NEGATIVE_INFINITY;
		//System.out.println("capWithDiscs(..) .. "+line);
		for(Vector p: enclosedPoints){
			Plane plane = new Plane(p, line.getDirection());
			float intersection = plane.intersectionParameter(line);
			//System.out.println("> Point "+p+" .. intersection "+intersection);
			if(intersection>upperT) upperT = intersection;
			if(intersection<lowerT) lowerT = intersection;
		}

		return new Cylinder(line.getPoint(lowerT), line.getPoint(upperT), radius);
	}

	public Capsule capWithHalfSpheres(Capsule c1, Capsule c2) {
		float lowerT = Float.POSITIVE_INFINITY, upperT=Float.NEGATIVE_INFINITY;
		Vector[] centers = {c1.p1, c1.p2, c2.p1, c2.p2};
		float[] rads = {c1.rad, c1.rad, c2.rad, c2.rad};
		for(int i=0;i<4;i++){
			Vector p = centers[i];
			float rad = rads[i];
			
			Sphere s = new Sphere(p, radius-rad+0.00001f);
			float[] intersections = s.intersectionParameters(line);
			
			if(intersections.length<2){
				float intersection = line.projectOnto(p);
				if(intersection+rad>upperT) upperT = intersection+rad;
				if(intersection-rad<lowerT) lowerT = intersection-rad;
			}else{
				float upperIntersection = intersections[0];
				float lowerIntersection = intersections[1];
				if(upperIntersection>upperT) 	upperT = upperIntersection;
				if(lowerIntersection<lowerT) 	lowerT = lowerIntersection;
			}
		}
		return new Capsule(line.getPoint(lowerT), line.getPoint(upperT), radius);
	}

	public static InfCylinder createMinRadBoundingCylinder_Sampling(List<Vector> points){
		InfCylinder minCyl = null;
		float minRad = Float.POSITIVE_INFINITY;
		for(float phi=0;phi<Math.PI;phi+=Math.PI/64){
			for(float psi=0;psi<Math.PI;psi+=Math.PI/64){
				Vector dir = new Vector(1,0,0);
				Matrix.createRotationMatrix(phi, new Vector(0,1,0)).applyToIn(dir);
				Matrix.createRotationMatrix(psi, new Vector(0,0,1)).applyToIn(dir);
				InfCylinder tmp = createMinRadCylinderFromDirection(points, dir);
				if(tmp.radius<minRad){
					minRad = tmp.radius;
					minCyl = tmp;
				}
			}
		}
		return minCyl;
	}
	public static InfCylinder createMinRadCylinderFromDirection(List<Vector> points, Vector dir){
		Plane p = new Plane(Vector.O(), dir);
		List<Vector2D> points2d = new ArrayList<Vector2D>();
		Vector x = Vector.createRandomVector().crossIn(dir).normIn();
		Vector y = x.cross(dir).norm();
		for(Vector po: points) {
			Vector projected = p.projectOnto(po);
			Vector2D p2d = new Vector2D(x.dot(projected), y.dot(projected));
			points2d.add(p2d);
		}
		Circle mec = Circle.minimumEnclosingCircle_bruteforce(points2d);

		Vector linePoint = x.times(mec.getCenter().x()).plus(y.times(mec.getCenter().y()));
		return new InfCylinder(new Line(linePoint, dir.clone()), mec.getRadius()+mec.getRadius()*0.001f);
	}

	public static InfCylinder createMinRadCylinderFromDirection(Capsule c1, Capsule c2, Vector dir){
		Plane p = new Plane(Vector.O(), dir);
		Vector x = Vector.createRandomVector().crossIn(dir).normIn();
		Vector y = x.cross(dir).norm();
		Vector proj = p.projectOnto(c1.p1);
		Circle circle1 = new Circle(new Vector2D(x.dot(proj), y.dot(proj)), c1.rad);
		proj = p.projectOnto(c1.p2);
		Circle circle2 = new Circle(new Vector2D(x.dot(proj), y.dot(proj)), c1.rad);
		proj = p.projectOnto(c2.p1);
		Circle circle3 = new Circle(new Vector2D(x.dot(proj), y.dot(proj)), c2.rad);
		proj = p.projectOnto(c2.p2);
		Circle circle4 = new Circle(new Vector2D(x.dot(proj), y.dot(proj)), c2.rad);

		Circle mec = new Circle( circle1, circle2, circle3, circle4 );

		Vector linePoint = x.times(mec.getCenter().x()).plus(y.times(mec.getCenter().y()));

		return new InfCylinder(new Line(linePoint, dir.clone().normIn()), mec.getRadius()+mec.getRadius()*0.001f);
	}

	public String toString(){
		return String.format("InfCylinder[%s,%.2f]", line.toString(), radius);
	}
}
