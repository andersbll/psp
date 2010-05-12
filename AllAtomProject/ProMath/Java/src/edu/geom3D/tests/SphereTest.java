package edu.geom3D.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.geom3D.Sphere;
import edu.math.Line;
import edu.math.Vector;

public class SphereTest {

	@Test
	public void testOverlaps() {
		Sphere s1 = new Sphere(new Vector(0,1,0), 1);
		Sphere s2 = new Sphere(new Vector(2,1,0), 1);
		assertTrue(s1.overlaps(s2));
		s1.plusIn(new Vector(-0.01f,0,0));
		assertFalse(s1.overlaps(s2));
		s1.plusIn(new Vector(0.03f,0,0));
		assertTrue(s1.overlaps(s2));
	}

	@Test
	public void testIntersections() {
		
		Sphere s = new Sphere(new Vector(4,0,0), 1);
		Line l = new Line(new Vector(0,0,0), new Vector(1,0,0));
		
		Vector[] intersections = s.intersections(l);
		assertEquals(2, intersections.length);
		assertEquals(new Vector(4-1,0,0), intersections[0]);
		assertEquals(new Vector(4+1,0,0), intersections[1]);
	}

}
