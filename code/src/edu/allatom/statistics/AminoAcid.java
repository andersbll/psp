package edu.allatom.statistics;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AminoAcid {

	public final Type type;
	public final Map<String, Atom> allatoms; 

	public AminoAcid(Type type) {
		this.type = type;
		allatoms = new TreeMap<String, Atom>();
	}

	public AminoAcid(Type type, List<Atom> atoms) {
		this.type = type;
		allatoms = new HashMap<String, Atom>();
		for(Atom a : atoms) {
			addAtom(a);
		}
	}
	
	public void addAtom(Atom a) {
		allatoms.put(a.name, a);
	}
	
	public Collection<Atom> getAtoms() {
		return allatoms.values();
	}
	
	public String toString() {
		String s = type.name() + ":";
		for(String a : allatoms.keySet()) {
			s += "\n  " + a;
		}
		return s;
	}
	
//	public enum SequencePosition {
//		N_TERMINUS, MIDDLE, C_TERMINUS;
//	}

	public enum Type {
		ALA, 
		ARG,
		ASN,
		ASP,
		CYS,
		GLU,
		GLN,
		GLY,
		HIS,
		ILE,
		LEU,
		LYS,
		MET,
		PHE,
		PRO,
		SER,
		THR,
		TRP,
		TYR,
		VAL;
		
//		public final String name;
//		Type(String name3) {
//			this.name = name3;
//		}
		public static Type fromName(String name) {
			for(Type t : values()) {
				if(name.equalsIgnoreCase(t.name())) {
					return t;
				}
			}
			return null;
		}
	}

}
