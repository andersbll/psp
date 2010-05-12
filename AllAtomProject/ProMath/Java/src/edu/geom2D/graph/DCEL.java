package edu.geom2D.graph;

import java.util.*;

class Vertex implements Cloneable {
    private double x; // x-Koordinate
    private double y; // y-Koordinate
    private Edge incidentEdge; // eine inzidente Kante
    private String name; // der Name

    /* Eine Iteratorklasse, welche die inzidenten Kanten einer
       Ecke zyklisch durchl�uft
     */
    private class VertexEdgeIterator implements Iterator<Edge> {
	// wir verwenden einen internen Iterator
	Iterator<Edge> iter;

	/* es wird zun�chst eine Liste von den inzidenten Kanten angelegt 
	   und dann iteriert
	 */
	public VertexEdgeIterator() {
	    // der Vector, der die Kantern speichert
	    Vector<Edge> edges = new Vector<Edge>();
	    // alle Kanten hinzuf�gen
	    Edge e = incidentEdge;
	    do {
		edges.add(e);
		e = (e.getTwin()).getNext();
	    } while (e != incidentEdge);
	    // den Iterator merken
	    iter = edges.iterator();
	}

	// alles durchreichen
	public boolean hasNext() {
	    return iter.hasNext();
	}

	public Edge next() {
	    return iter.next();
	}

	// gibt es nicht
	public void remove() {
	    throw new UnsupportedOperationException();
	}
    }

    // die Kanten durchlaufen
    public Iterator<Edge> traverseEdges() {
	return new VertexEdgeIterator();
    }

    // getter/setter-Methoden

    public Edge getIncidentEdge() {
	return incidentEdge;
    }

    public double getX() {
	return x;
    }

    public double getY() {
	return y;
    }

    public String getName() {
	return name;
    }

    public void setIncidentEdge(Edge e) {
	incidentEdge = e;
    }

    public void setX(double x) {
	this.x = x;
    }

    public void setY(double y) {
	this.y = y;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String toString() {
	return "Punkt " + name + "(" + x + " ," + y + ") " + incidentEdge.getName();
    }
}

class Face implements Cloneable {
    private Edge outerComponent; // der �u�ere Rand
    private Vector<Face> innerComponents = new Vector<Face>(); // die inneren R�nder
    private String name; // der Name

    /*
      Klone die Facette, der Vector wird geklont, aber nicht seine Inhalte
    */
    public Face clone() {
	Face f = new Face();
	f.outerComponent = this.outerComponent;
	f.innerComponents = new Vector<Face>(this.innerComponents.size());
	Iterator<Face> it = this.innerComponents.iterator();
	while (it.hasNext()) {
	    f.innerComponents.add(it.next());
	}
	return f;
    }

    /* Iteratorklasse zum Iteriereren �ber die Fl�chen */
    private class FaceEdgeIterator implements Iterator {
	Iterator iter;

	/* F�ge den Rand, der start enth�lt, zu vect hinzu */
	private void addEdges(Edge start, Vector vect) {
	    if (start == null)
		return;
	    Edge dummy = start; 
	    vect.add(dummy);
	    while(dummy.getNext() != start) {
		dummy = dummy.getNext();
		vect.add(dummy);
	    }
	}

	/* Im Konstruktor werden alle Kanten eingef�gt
	 */
	public FaceEdgeIterator() {
	    Vector edges = new Vector();
	    addEdges(outerComponent, edges);
	    Iterator inner = innerComponents.iterator();
	    while (inner.hasNext()) {
		Edge e = (Edge)inner.next();
		addEdges(e, edges);
	    } 
	    iter = edges.iterator();
	}

	// Operationen durchreichen
	public boolean hasNext() {
	    return iter.hasNext();
	}

	public Object next() {
	    return iter.next();
	}

	public void remove() {
	    throw new UnsupportedOperationException();
	}
    }

    // Iterator f�r die Ecken holen
    public Iterator traverseEdges() {
	return new FaceEdgeIterator();
    }

    // getter/setter-Methoden
    public Edge getOuterComponent() {
	return outerComponent;
    }

    public Vector getInnerComponents() {
	return innerComponents;
    }

    public String getName() {
	return name;
    }

    public void setOuterComponent(Edge e) {
	this.outerComponent = e;
    }

    public void setInnerComponents(Vector v) {
	innerComponents = v;
    }

    public void setName(String name) {
	this.name = name;
    }

    // die angrenzende Fl�che holen
    public Face getAdjacentFace(Edge e) {
	if (e.getIncidentFace() != this)
	    throw new IllegalArgumentException();
	return (e.getTwin()).getIncidentFace();
    }

    /*
      f�ge eine Kante ein. Als Parameter werden die Kanten �bergeben, die
      im Start-, bzw. Endpunkt der neuen Kante beginnen. Dies habe ich so
      gemacht, da mir kein besserer Weg eingefallen ist, wie man sonst 
      die Vor- und Nachfolgerkanten ermitteln kann, ohne die Facette bzw.
      die Nachbarn der beiden Ecken zu durchlaufen, wodurch die geforderte
      Laufzeit nicht mehr gew�hrleistet w�re.
    */
    // p = Kante, die aus dem Anfangspunkt herausgeht
    // q = Kante, die aus dem Endpunkt herausgeht
    public Face insertEdge(Edge p, Edge q) {
	if (p.getIncidentFace() != this || q.getIncidentFace() != this)
	    throw new IllegalArgumentException();
	// die neue Facette
	Face f2 = (Face)this.clone();
	// die neuen Kanten erzeugen
	Edge k = new Edge();
	Edge kt = new Edge();
	// die Kanten sind Zwillinge
	k.setTwin(kt);
	kt.setTwin(k);
	// die Anfangspunkte setzen
	k.setOrigin(p.getOrigin());
	kt.setOrigin(q.getOrigin());
	// k verzeigern
	k.setNext(q);
	k.setPrev(p.getPrev());
	// k' verzeigern
	kt.setNext(p);
	kt.setPrev(q.getPrev());
	// die inzidenten Kanten anpassen
	q.getPrev().setNext(kt);
	p.getPrev().setNext(k);
	p.setPrev(kt);
	q.setPrev(k);
	// die kleinere Facette suchen
	Edge dummy1 = k.getNext();
	Edge dummy2 = kt.getNext();
	while (dummy1 != k && dummy2 != kt) {
	    dummy1 = dummy1.getNext();
	    dummy2 = dummy2.getNext();
	}
	if (dummy1 == k) {
	    // die Fl�che von k ist die kleinere
	    this.setOuterComponent(kt);
	    kt.setIncidentFace(this);
	    f2.setOuterComponent(k);
	} else {// die Fl�che von kt ist die kleinere
	    this.setOuterComponent(k);
	    k.setIncidentFace(this);
	    f2.setOuterComponent(kt);
	}
	// die inzidente Facette setzen
	Iterator it = f2.traverseEdges();
	while (it.hasNext()) {
	    Edge e = (Edge)it.next();
	    e.setIncidentFace(f2);
	}
	return f2;
    }

    public String toString() {
	return "Facette " + name + " " + outerComponent + " " + innerComponents;
    }
}

class Edge implements Cloneable {
    private Vertex origin;
    private Edge twin;
    private Face incidentFace;
    private Edge next;
    private Edge prev;
    private String name;

    // Klone eine Kante
    public Object clone() {
	Edge e = new Edge();
	e.origin = origin;
	e.twin = twin;
	e.incidentFace = incidentFace;
	e.next = next;
	e.prev = prev;
	return e;
    }

    // getter/setter Methoden
    public Vertex getOrigin() {
	return origin;
    }

    public Edge getNext() {
	return next;
    }

    public Face getIncidentFace() {
	return incidentFace;
    }

    public Edge getTwin() {
	return twin;
    }

    public Edge getPrev() {
	return prev;
    }

    public String getName() {
	return name;
    }


    public void setIncidentFace(Face f) {
	this.incidentFace = f;
    }

    public void setTwin(Edge twin) {
	this.twin = twin;
    }

    public void setOrigin(Vertex origin) {
	this.origin = origin;
    }

    public void setNext(Edge next) {
	this.next = next;
    }

    public void setPrev(Edge prev) {
	this.prev = prev;
    }

    public void setName(String name) {
	this.name = name;
    }

    // hole den Endpunkt der Kante
    public Vertex getDest() {
	return twin.getOrigin();
    }

    // f�ge eine Ecke auf der Kante ein
    public Edge insertVertex(Vertex r) {
	// die neue Kante
	Edge k = (Edge)this.clone();
	Edge kt = (Edge)this.twin.clone();
	k.setTwin(kt); kt.setTwin(k);
	// f�r die bessere Lesbarkeit
	Vertex p = this.origin;
	Vertex q = this.twin.origin;
	// die Punkte verzeigern
	q.setIncidentEdge(kt);
	r.setIncidentEdge(k);
	// die Anfangspunkte setzen
	k.setOrigin(r);
	this.getTwin().setOrigin(r);
	// die Kanten verzeigern
	this.getNext().setPrev(k);
	this.getTwin().getPrev().setNext(kt);
	k.setPrev(this);
	this.setNext(k);
	kt.setNext(this.getTwin());
	this.getTwin().setPrev(kt);	
	// Facetten setzen
	k.setIncidentFace(this.getIncidentFace());
	kt.setIncidentFace(this.getTwin().getIncidentFace());
	return k;
    }

    public String toString() {
	return name + " Origin: " + origin.getName() + " Twin: " + twin.getName() + 
	    " Next: " + next.getName() + " Prev: " + prev.getName() + " Face: " + 
	    incidentFace.getName();
    }
}

public class DCEL {
    Vector vertices = new Vector();
    Vector faces = new Vector();
    Vector edges = new Vector();

    // Alles zur Erzeugung des Beispiels, siehe Bild
    static Vertex a,b,c,d,e,f,g,h;
    static Edge ea, eb, ec, ed, ee, ef, eg, eh, ei;
    static Edge eat, ebt, ect, edt, eet, eft, egt, eht, eit;
    static Face fa, fb, fc, fd;
   
    public static Vertex createVertex(String name, double x, double y) {
	Vertex result = new Vertex();
	result.setName(name); result.setX(x); result.setY(y);
	return result;
    }

    public static Edge createEdge(String name, Vertex origin, Vertex dest) {
	Edge result = new Edge();
	Edge twin = new Edge();
	origin.setIncidentEdge(result);
	dest.setIncidentEdge(twin);
	result.setName(name); twin.setName(name+"'");
	result.setTwin(twin); twin.setTwin(result);
	result.setOrigin(origin); twin.setOrigin(dest);
	return result;
    }

    public static void connect(Edge e, Edge f) {
	e.setNext(f); f.setPrev(e);
    }

    public static Face createFace(String name, Edge outer, Edge inner) {
	Face result = new Face();
	result.setName(name);
	result.setOuterComponent(outer);
	if (outer != null) {
	    Edge dummy = outer;
	    do {
		dummy.setIncidentFace(result);
		dummy = dummy.getNext();
	    } while (dummy != outer);
	}
	if (inner != null) {
	    Vector v = result.getInnerComponents();
	    v.add(inner);
	    Edge dummy = inner;
	    do {
		dummy.setIncidentFace(result);
		dummy = dummy.getNext();
	    } while (dummy != inner);
	}
	return result;
    }

    public static void createExample() {
	a = createVertex("A", 0, 0);
	b = createVertex("B", 100, 0);
	c = createVertex("C", 0, 100);
	d = createVertex("D", 100, 100);
	e = createVertex("E", 1, 1);
	f = createVertex("F", 2, 1);
	g = createVertex("G", 1, 2);
	h = createVertex("H", 2, 2);
	ea = createEdge("a", a, b);
	eat  = ea.getTwin();
	eb = createEdge("b", b, d);
	ebt  = eb.getTwin();
	ec = createEdge("c", d, c);
	ect  = ec.getTwin();
	ed = createEdge("d", c, a);
	edt  = ed.getTwin();
	ee = createEdge("e", b, c);
	eet  = ee.getTwin();
	ef = createEdge("f", e, f);
	eft  = ef.getTwin();
	eg = createEdge("g", f, h);
	egt  = eg.getTwin();
	eh = createEdge("h", h, g);
	eht  = eh.getTwin();
	ei = createEdge("i", g, e);
	eit  = ei.getTwin();
	connect(ea, ee); connect(ee, ed); connect(ed, ea);
	connect(eb, ec); connect(ec, eet); connect(eet, eb);
	connect(eat, edt); connect(edt, ect); connect(ect, ebt); connect(ebt, eat);
	connect(ef, eg); connect(eg, eh); connect(eh, ei); connect(ei, ef);
	connect(eft, eit); connect(eit, eht); connect(eht, egt); connect(egt, eft);
	fa = createFace("A", null, eat);
	fb = createFace("B", eb, null);
	fc = createFace("C", ea , egt);
	fd = createFace("D", eh, null);
    }

    public static void printAll() {
	System.out.println(fa.toString());
	System.out.println(fb.toString());
	System.out.println(fc.toString());
	System.out.println(fd.toString());
    }

    public static void traverseFace(Face f) {
	System.out.println("Traversiere " + f);
	Iterator it = f.traverseEdges();
	while(it.hasNext()) {
	    System.out.println("\t"+it.next());
	}
    }

    public static void traverseVertex(Vertex v) {
	System.out.println("Traversiere " + v);
	Iterator it = v.traverseEdges();
	while(it.hasNext()) {
	    System.out.println("\t"+it.next());
	}
    }

    public static void adjFace(Face f, Edge e) {
	System.out.println(f.getName() + " ist �ber " + e.getName() + " mit " + 
			   f.getAdjacentFace(e).getName() + " verbunden.");
    }
    public static void main(String[] args) {
	createExample();
	traverseFace(fa);
	traverseFace(fc);
	traverseVertex(c);
	traverseVertex(d);
	adjFace(fa, ect);
	adjFace(fd, eg);
	System.out.println("neuer Punkt auf c");
	Vertex v = new Vertex(); v.setName("neuer Punkt");
	ec.insertVertex(v);
	traverseFace(fb);
	traverseFace(fa);
	System.out.println("neue Kante in D");
	Face f = fd.insertEdge(ef, eh);
	f.setName("neue Facette");
	traverseFace(fd);
	traverseFace(f);
    }
}

