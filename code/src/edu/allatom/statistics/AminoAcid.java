package edu.allatom.statistics;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import edu.math.Point;
import edu.math.Vector;

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
	
	public void calculatePsi() {
		Atom Catom = this.allatoms.get("C");
		Point neighborN = null;
		for(Atom a : Catom.bondsTo) {
			if(a.name.equals("N")){
				neighborN = a.position;
			}
		}
		Point C = Catom.position;
		Point CA = this.allatoms.get("CA").position;
		Point N = this.allatoms.get("N").position;
		Vector v1 = neighborN.vectorTo(C);
		Vector v2 = C.vectorTo(CA);
		Vector v3 = CA.vectorTo(N);
		System.out.println("psi="+Vector.dihedralAngle(v1, v2, v3)*180/Math.PI);
	}
	
	public void calculatePhi() {
		Point C = this.allatoms.get("C").position;
		Point CA = this.allatoms.get("CA").position;
		Atom Natom = this.allatoms.get("N");
		Point N = Natom.position;
		Point neighborC = null;
		for(Atom a : Natom.bondsTo) {
			if(a.name.equals("C")){
				neighborC = a.position;
			}
		}
		Vector v1 = C.vectorTo(CA);
		Vector v2 = CA.vectorTo(N);
		Vector v3 = N.vectorTo(neighborC);
		System.out.println("phi="+Vector.dihedralAngle(v1, v2, v3)*180/Math.PI);
	}

	public void addAtom(Atom a) {
		allatoms.put(a.name, a);
	}
	
	public Collection<Atom> getAtoms() {
		return allatoms.values();
	}
	public List<Atom> getBackBoneAtoms() {
		List<Atom> atoms = new LinkedList<Atom>();
		atoms.add(allatoms.get("CA"));
		atoms.add(allatoms.get("C"));
		atoms.add(allatoms.get("O"));
		atoms.add(allatoms.get("N"));
		if(type!=Type.PRO) {
			atoms.add(allatoms.get("H"));
		} else {
			atoms.add(allatoms.get("CD"));			
		}
		while(atoms.contains(null)) {
			atoms.remove(null);
		}
		return atoms;
	}
	
	public String toString() {
		String s = type.name+":";
		for(String a : allatoms.keySet()) {
			s += "\n  " + a;
		}
		return s;
	}
	
//	public enum SequencePosition {
//		N_TERMINUS, MIDDLE, C_TERMINUS;
//	}

	public enum Type {
		ALA ("ALA"), 
		ARG ("ARG"),
		ASN ("ASN"),
		ASP ("ASP"),
		CYS ("CYS"),
		GLU ("GLU"),
		GLN ("GLN"),
		GLY ("GLY"),
		HIS ("HIS"),
		ILE ("ILE"),
		LEU ("LEU"),
		LYS ("LYS"),
		MET ("MET"),
		PHE ("PHE"),
		PRO ("PRO"),
		SER ("SER"),
		THR ("THR"),
		TRP ("TRP"),
		TYR ("TYR"),
		VAL ("VAL");
		
		public final String name;
		Type(String name3) {
			this.name = name3;
		}
		public static Type fromName(String name) {
			for(Type t : values()) {
				if(name.equalsIgnoreCase(t.name)) {
					return t;
				}
			}
			return null;
		}
	}

}
