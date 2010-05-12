package edu.geom2D.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.geom2D.Circle;
import edu.math.Toolbox;
import edu.math.Vector2D;

public class CircleTest {

	@Test
	public void testCircleVector2DVector2D() {
		Circle c = new Circle(new Vector2D(1,1), new Vector2D(-1,1));
		assertEquals(new Vector2D(0,1), c.getCenter() );
		assertTrue(1f==c.getRadius() );
	}

	@Test
	public void testCircleVector2DVector2DVector2D() {
		Circle c = new Circle(new Vector2D(1,1), new Vector2D(-1,1), new Vector2D(0,2));
		assertEquals(new Vector2D(0,1), c.getCenter() );
		assertTrue(1f==c.getRadius() );
	}

	@Test
	public void testContains() {
		Circle c = new Circle(new Vector2D(1,1), new Vector2D(-1,1), new Vector2D(0,2));
		assertTrue(c.contains(new Vector2D(0,0.01f)));
		assertFalse(c.contains(new Vector2D(0,-0.01f)));
		assertFalse(c.contains(new Vector2D(1,1-0.01f)));
	}

	@Test
	public void testMinimumEnclosingCircle_bruteforce() {
		List<Vector2D> points = new ArrayList<Vector2D>();
		points.add(new Vector2D(1,0));
		points.add(new Vector2D(-1,0));
		points.add(new Vector2D(0,0.9));
		points.add(new Vector2D(0.1,0.5));
		points.add(new Vector2D(-0.9,-0.001));
		points.add(new Vector2D(-0.2,0.4));
		points.add(new Vector2D(0.3,-0.1));
		points.add(new Vector2D(0,-0.8));
		
		Circle mec = Circle.minimumEnclosingCircle_bruteforce(points);
		assertEquals(new Vector2D(0,0), mec.getCenter());
		assertTrue(1f==mec.getRadius());
		
		for(int it=0;it<10;it++){
			points.clear();
			for(int pc=0;pc<5;pc++){
				points.add(new Vector2D(Toolbox.randBetween(-1, 1), Toolbox.randBetween(-1, 1)));
			}
			mec = Circle.minimumEnclosingCircle_bruteforce(points);
			for(Vector2D v: points) assertTrue(mec.contains(v));
		}
	}

}
