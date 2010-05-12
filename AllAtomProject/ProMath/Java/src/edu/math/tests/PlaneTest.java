package edu.math.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.math.*;

public class PlaneTest {
	
	@Test
	public void testGetDistance() {
		Plane plane = new Plane(new Vector(0,0,1), new Vector(0,0,2));
		assertTrue(-1==plane.getDistance(new Point(0,0,0)));
		assertTrue(-1==plane.getDistance(new Point(1,1,0)));
		assertTrue(0.5==plane.getDistance(new Point(1,1,1.5f)));
		assertTrue(0==plane.getDistance(new Point(1,1,1f)));
	}

	@Test
	public void testProjectOntoIn() {
		Plane plane = new Plane(new Vector(0,0,1), new Vector(0,0,2));
		Vector v = new Vector(1,1,2);
		assertEquals(plane.projectOntoIn(v), new Vector(1,1,1));
		assertEquals(v, new Vector(1,1,1));
	}

}
