package edu.j3dScene;

import edu.geom3D.Shape;
import edu.math.Vector;

public class TextShape implements Shape {
	public String text;
	public Vector pos;
	public float height;
	public TextShape(String t, Vector p){
		this(t,p,0.1f);
	}
	public TextShape(String t, Vector p, float height){
		text = t;
		pos = p;
		this.height = height;
	}
	public Vector getCenter() {
		return pos.clone();
	}

}
