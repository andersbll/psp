package edu.allatom;

import java.util.LinkedList;
import java.util.List;

import edu.allatom.AminoAcid.Type;
import edu.math.Point;


public class Atom {
	public final static double BINDING_DISTANCE_THRESHOLD = 1.6;


	public final Type type;
	public final String name;
	public final Point position;
	public List<Atom> bondsTo = new LinkedList<Atom>();


	public Atom(Type type, String name, Point position) {
		this.type = type;
		this.name = name;
		this.position = position;
	}
	
	public String toString() {
		String s = name;
		return s;
	}
	
	
	public enum Type {
		H ("H"),
		C ("C"),
		N ("N"),
		O ("O"),
		S ("S"),
		UNKNOWN ("UNKNOWN");

		public final String name;
		Type(String name3) {
			this.name = name3;
		}
		public static Type fromName(String name) {
			for(Type t : values()) {
				if(name.equalsIgnoreCase(t.name	)) {
					return t;
				}
			}
			return null;
		}
	}

}
