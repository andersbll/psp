package edu.geom2D.graph.tests;

import edu.geom2D.graph.DCELGraph;
import edu.geom2D.graph.DCELGraph.Face;
import edu.geom2D.graph.DCELGraph.HalfEdge;
import edu.math.Vector2D;
import junit.framework.TestCase;

public class DCELGraphTest extends TestCase {


	
	public void testCreateVertex() {
		DCELGraph G = new DCELGraph();

		Face f = G.findFace(new Vector2D(1,1));
		assertEquals(0, f.innerComponents().size());
		G.createVertex(new Vector2D(0,0));
		assertEquals(1, f.innerComponents().size());
		G.createVertex(new Vector2D(2,0));
		assertEquals(2, f.innerComponents().size());
		G.createVertex(new Vector2D(2,1));
		assertEquals(3, f.innerComponents().size());
		G.createVertex(new Vector2D(0,2));
		assertEquals(4, f.innerComponents().size());
	}

	public void testConnectVertices() {
		DCELGraph G = new DCELGraph();

		Face f = G.findFace(new Vector2D(1,1));
		G.createVertex(new Vector2D(0,0));
		G.createVertex(new Vector2D(2,0));
		G.createVertex(new Vector2D(2,1));
		G.createVertex(new Vector2D(0,2));
		assertEquals(4, f.innerComponents().size());
		G.connectVertices(f.innerComponents().get(0).origin(), f.innerComponents().get(1).origin());
		assertEquals(3, f.innerComponents().size());
	}

	public void testFindFaceVector2D() {
		DCELGraph G = new DCELGraph();
	}

}
