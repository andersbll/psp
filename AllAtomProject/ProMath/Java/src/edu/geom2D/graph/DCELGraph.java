package edu.geom2D.graph;

import java.util.LinkedList;
import java.util.List;

import edu.math.Line;
import edu.math.Vector2D;

/**
 * A doubly connected edge-list representation of a 2D graph. For specifications
 * of the inner classes see [M. De Berg] p. 32
 * @author R.Fonseca
 */
public class DCELGraph {
	private Face inftyFace = new Face();
	private List<HalfEdge> edges = new LinkedList<HalfEdge>();
	private List<Vertex> vertices = new LinkedList<Vertex>();
	private List<Face> faces = new LinkedList<Face>();

	public DCELGraph(){
		faces.add(inftyFace);
	}

	public Vertex createVertex(Vector2D point){
		Face f = findFace(point);

		Vertex v = new Vertex();
		v.point = point;
		v.incidentEdge = new HalfEdge();
		v.incidentEdge.origin = v;
		v.incidentEdge.next = v.incidentEdge;
		v.incidentEdge.face = f;
		v.incidentEdge.twin = v.incidentEdge;
		v.incidentEdge.prev = v.incidentEdge;
		f.innerComponents.add(v.incidentEdge);
		vertices.add(v);
		return v;
	}

	public HalfEdge connectVertices(Vertex v1, Vertex v2){
		HalfEdge v1v2 = new HalfEdge();
		HalfEdge v2v1 = new HalfEdge();

		if(		v1.incidentEdge.twin==v1.incidentEdge && 
				v2.incidentEdge.twin==v2.incidentEdge){

			v1v2.next = v2v1;
			v1v2.prev = v2v1;
			v1v2.origin  = v1;
			v1v2.twin = v2v1;
			v1v2.face = v1.incidentEdge.face;

			v2v1.next = v1v2;
			v2v1.prev = v1v2;
			v2v1.origin  = v2;
			v2v1.twin = v2v1;
			v2v1.face = v2.incidentEdge.face;

			v1v2.face.innerComponents.remove(v1.incidentEdge);
			v1v2.face.innerComponents.remove(v2.incidentEdge);
			v1v2.face.innerComponents.add(v1v2);
			v1.incidentEdge = v1v2;
			v2.incidentEdge = v2v1;

		}else if(v1.incidentEdge.twin==v1.incidentEdge){
			HalfEdge e2o = v2.getLeftmostOutgoing(v1.point.vectorTo(v2.point));
			HalfEdge e2i = e2o.prev;

			v1v2.next = e2o;
			v1v2.prev = v2v1;
			v1v2.origin  = v1;
			v1v2.twin = v2v1;
			v1v2.face = e2o.face;

			v2v1.next = v1v2;
			v2v1.prev = e2i;
			v2v1.origin = v2;
			v2v1.twin = v1v2;
			v2v1.face = e2o.face;

			e2i.next = v2v1;
			e2o.prev = v1v2;
			v1.incidentEdge = v1v2;
			v2.incidentEdge = v2v1;
		}else if(v2.incidentEdge.twin==v2.incidentEdge){
			HalfEdge e1o = v1.getLeftmostOutgoing(v2.point.vectorTo(v1.point));
			HalfEdge e1i = e1o.prev;


			v1v2.next = v2v1;
			v1v2.prev = e1i;
			v1v2.origin  = v1;
			v1v2.twin = v2v1;
			v1v2.face = e1o.face;

			v2v1.next = e1o;
			v2v1.prev = v1v2;
			v2v1.origin = v2;
			v2v1.twin = v1v2;
			v2v1.face = e1o.face;

			e1o.prev = v2v1;
			e1i.next = v1v2;
			v1.incidentEdge = v1v2;
			v2.incidentEdge = v2v1;
		}else{

			HalfEdge e1o = v1.getLeftmostOutgoing(v2.point.vectorTo(v1.point));
			HalfEdge e1i = e1o.prev;
			HalfEdge e2o = v2.getLeftmostOutgoing(v1.point.vectorTo(v2.point));
			HalfEdge e2i = e2o.prev;


			v1v2.next = e2o;
			v1v2.prev = e1i;
			v1v2.origin  = v1;
			v1v2.twin = v2v1;
			v1v2.face = e2o.face;

			v2v1.next = e1o;
			v2v1.prev = e2i;
			v2v1.origin = v2;
			v2v1.twin = v1v2;
			v2v1.face = e1o.face;

			//Check if the face is split
			HalfEdge e;
			for(e=e1o; e.origin!=v1&&e.origin!=v2;e = e.next);
			if(e.origin==v2){//Split faces
				Face newFace = new Face();
				newFace.outerComponent = e1o;
				for(e=e1o; e.origin!=v2;e = e.next) {
					e.face = newFace;
				}
				v2v1.face = newFace;
				v1v2.face.outerComponent = v1v2;
			}else{
				Face f = e1o.face;
				for(e=e1o; e.origin!=v1;e = e.next) {
					if(f.innerComponents.remove(e)) break;
				}
			}
			e1o.prev = v2v1;
			e2i.next = v2v1;
			e1i.next = v1v2;
			e2o.prev = v1v2;
			v1.incidentEdge = v1v2;
			v2.incidentEdge = v2v1;
		}
		return v1v2;
	}

	/**Find the face containing <code>point</code>*/ 
	public Face findFace(Vector2D point){
		for(HalfEdge e: inftyFace.innerComponents){
			Face f = findFace(point, e);
			if(f!=null) return f;
		}
		return inftyFace;
	}

	/** Find the face containing <code>point</code> inside the component
	 * represented by the specified halfedge
	 */
	public Face findFace(Vector2D point, HalfEdge component){

		//Check if the point is contained in a face in this component
		Vertex vNearest = findNearestVertex(point, component.origin);
		Face ret = null;
		for(HalfEdge e: vNearest.incidentEdges()){
			if(e.face!=component.face && e.face.contains(point)){
				ret = e.face;
				break;
			}
		}
		if(ret==null)	return null;

		//Check if the point is contained in an inner face of this face
		for(HalfEdge e: ret.innerComponents){
			Face innerRet = findFace(point, e);
			if(innerRet!=null) {
				ret = innerRet;
				break;
			}
		}

		return ret;
	}
	/**
	 * Locate the nearest vertex to <code>point</code> starting at the 
	 * specified vertex
	 */
	private Vertex findNearestVertex(Vector2D point, Vertex v){
		Vertex nextV = v;
		float nextVDist = nextV.point.distance(point);
		for(HalfEdge e: v.incidentEdges()){
			float d = e.destination().point.distance(point);
			if(d<nextVDist){
				nextV = e.destination();
				nextVDist = d;
			}
		}
		if(nextV==v) return v;
		else return findNearestVertex(point, nextV);
	}

	public static class HalfEdge{
		private Vertex origin;
		private HalfEdge twin, next, prev;
		private Face face;

		public HalfEdge twin(){return twin;}
		public Vertex origin(){ return origin; }
		public HalfEdge next(){ return next; }
		public HalfEdge previous(){ return prev; }
		public Face incidentFace(){ return face; }

		public Vertex destination(){ return next.origin; }
		public Vector2D getVector(){
			return origin.point.vectorTo(destination().point);
		}
	}
	public static class Vertex{
		private Vector2D point;
		private HalfEdge incidentEdge;

		public Vector2D coordinates(){ return point; }
		public HalfEdge incidentEdge(){ return incidentEdge; }

		public List<HalfEdge> incidentEdges(){
			List<HalfEdge> ret = new LinkedList<HalfEdge>();
			HalfEdge first = incidentEdge;
			ret.add(first);
			HalfEdge e = first.twin.next;
			while(e!=first){
				ret.add(e);
				e = e.twin.next;
			}
			return ret;
		}
		public HalfEdge getLeftmostOutgoing(Vector2D vec){
			//Find largest rotation-angle less than 180⁰ 
			HalfEdge e = incidentEdge;
			HalfEdge highestE = e;
			float highestAngle = vec.rotationAngle(e.getVector());//vec.cross(e.getVector()).z();
			e = e.twin.next;
			while(e!=incidentEdge){
				float angle = vec.rotationAngle(e.getVector());//vec.cross(e.getVector()).z();
				if(angle>highestAngle) {
					if( !(angle>180 && highestAngle<180) ){
						highestAngle = angle;
						highestE = e;
					}
				}
				e = e.twin.next;
			}
			return highestE;
		}
	}
	public static class Face{
		private List<HalfEdge> innerComponents = new LinkedList<HalfEdge>();
		private HalfEdge outerComponent;

		public List<HalfEdge> innerComponents(){ return innerComponents; }
		public HalfEdge outerComponents(){ return outerComponent; }

		public boolean contains(Vector2D point){
			if(outerComponent==null) 
				return true;//Its the unbounded face that contains everything
			HalfEdge firstE = outerComponent;
			HalfEdge e = firstE;
			do{
				if(!Vector2D.isLeftTurn(e.origin.point, e.destination().point, point))
					return false;
			}while(e!=firstE);
			return true;
		}
	}


	private static void printInnerComps(Face f){
		for(HalfEdge e: f.innerComponents()){
			System.out.println("Face "+f+" inner component: ");
			for(HalfEdge p = e.next(); p!=e;p=p.next()){
				System.out.println("> HalfEdge: "+e.origin().coordinates()+" -> "+e.destination().coordinates());
			}
		}
	}
}
