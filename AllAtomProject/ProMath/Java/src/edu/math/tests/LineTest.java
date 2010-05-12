package edu.math.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.math.Line;
import edu.math.Point;
import edu.math.Vector;

public class LineTest {

	@Test
	public void testRotate() {
		Line l = new Line(new Vector(-0.4f,1,0.4f), new Vector(1,0,-1));
		Point p = new Point(0,0,0);
		Point rot1 = l.rotate(p, (float)Math.PI/2);
		Point rot2 = l.rotate(p, (float)Math.PI);
		Point rot3 = l.rotate(p, (float)Math.PI*2f);
		System.out.println(rot1);
		System.out.println(rot2);
		System.out.println(rot3);
		assertTrue(1==rot1.y());
		assertTrue(rot1.x()<0);
		assertTrue(rot1.z()<0);
		assertTrue(new Point(0,2,0).equals(rot2));
		assertEquals(p, rot3);
		
		l = new Line(new Vector(2,1,0), new Vector(0,0,1));
		p = new Point(2,0,0);
		l.rotateIn(p, 90*(float)Math.PI/180);
		assertTrue(p.equals(new Point(3,1,0)));
		System.out.println(p);
		p = new Point(2,0,0);
		l.rotateIn(p, 180*(float)Math.PI/180);
		assertTrue(p.equals(new Point(2,2,0)));
		System.out.println(p);
		
		l = new Line(new Vector(0,0,0), new Vector(  0.342, -0.940,  0.000));
		p = new Point(-0.380f, -1.275f,  0.000f);
		l.rotateIn(p, 180*(float)Math.PI/180);
		System.out.printf("%s\n",p);
		assertTrue(Math.abs(p.z())<0.00001);
		
		l = new Line(new Vector(1,0,0), new Vector(  0, 0, 1f));
		p = new Point(0,0,0);
		l.rotateIn(p, 90*(float)Math.PI/180);
		System.out.printf("%s\n",p);
		assertTrue(Math.abs(p.z())<0.00001);
		
	}

}
