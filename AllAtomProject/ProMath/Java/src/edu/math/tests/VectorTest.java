package edu.math.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.math.Point;
import edu.math.Vector;

public class VectorTest {

	@Test
	public void testLength() {
	}

	@Test
	public void testRotate() {
		Vector v1 = new Vector(1,1,1);
		Point p = new Point(2,0,0);
		
		Vector pNew = v1.rotate(p, (float)(2*Math.PI/3));
		assertEquals(new Vector(0,2,0), pNew);
		pNew = v1.rotate(pNew, (float)(2*Math.PI/3));
		assertEquals(new Vector(0,0,2), pNew);
		pNew = v1.rotate(pNew, (float)(2*Math.PI/3));
		assertEquals(new Vector(2,0,0), pNew);
		
		v1 = new Vector(3,3,3);
		pNew = v1.rotate(p, (float)(2*Math.PI/3));
		assertEquals(new Vector(0,2,0), pNew);
	}

	@Test
	public void testDot() {
	}

	@Test
	public void testCross() {
	}

	@Test
	public void testNorm() {
	}

	@Test
	public void testTimes() {
	}

	@Test
	public void testAngle() {
	}

	@Test
	public void testPlusPoint() {
	}

	@Test
	public void testPlusVector() {
	}

	@Test
	public void testMinus() {
	}

	@Test
	public void testPlusIn() {
	}

	@Test
	public void testMinusIn() {
	}

	@Test
	public void testTimesIn() {
	}

	@Test
	public void testDivideIn() {
	}

	@Test
	public void testScaleXIn() {
	}

	@Test
	public void testScaleYIn() {
	}

	@Test
	public void testScaleZIn() {
	}

	@Test
	public void testCrossIn() {
	}

	@Test
	public void testEqualsObject() {
	}

	@Test
	public void testVectorTo() {
	}

	@Test
	public void testDistanceSquared() {
	}

	@Test
	public void testDistance() {
	}

}
