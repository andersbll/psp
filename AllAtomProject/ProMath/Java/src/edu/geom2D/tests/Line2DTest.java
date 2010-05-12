package edu.geom2D.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.geom2D.Line2D;
import edu.math.Vector2D;

public class Line2DTest {

	@Test
	public void testIntersection() {
		Line2D l1 = new Line2D(new Vector2D(0,1), new Vector2D(1,0));
		Line2D l2 = new Line2D(new Vector2D(2,-1), new Vector2D(0,3));
		
		//Expecting s (l1) to be 2 and t(l2) to be 0.66
		//Expecting intersection at (2,1)
		assertEquals(new Vector2D(2,1), l1.intersection(l2));
		assertEquals(new Vector2D(2,1), l2.intersection(l1));
	}

}
