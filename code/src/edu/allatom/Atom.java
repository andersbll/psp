package edu.allatom;


import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

import edu.math.Point;
import edu.math.Vector;

public class Atom {
	public final static double COLLISION_RADIUS = 1.5;
	public final static double BINDING_DISTANCE_THRESHOLD = 1.6;
	public enum Type {
		H, C, N, O, S, 
			UNKNOWN;

		public static Type fromName(String name) {
			for(Type t : values()) {
				if(name.equalsIgnoreCase(t.name())) {
					return t;
				}
			}
			return null;
		}
	}

	public final Type type;
	public final String label; // e.g. CA or HA3
	public Point position;
	public Map<String, Atom> bondsTo = new HashMap<String, Atom>();

	public Atom(Type type, String label, Point position) {
		this.type = type;
		this.label = label;
		this.position = position;
	}

	public Atom(Atom a) {
		this.type = a.type;
		this.label = a.label;
		this.position = new Vector(a.position);
	}

	public Collection<Atom> getBonds() {
		return bondsTo.values();
	}

	public Atom followBond(String neighbour) {
		return bondsTo.get(neighbour);
	}

	public String toString() {
		String s = label;
		return s;
	}

	public Vector vectorTo(Atom to) {
		return position.vectorTo(to.position);
	}

	public float distanceTo(Atom to) {
		return vectorTo(to).length();
	}
	
	public boolean collides(Atom atom) {
		return distanceTo(atom) < COLLISION_RADIUS*2;
	}

	public boolean isBackboneAtom() {
		return this.type != Type.UNKNOWN;
	}
}
