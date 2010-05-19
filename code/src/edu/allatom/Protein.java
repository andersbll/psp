package edu.allatom;

import java.util.LinkedList;
import java.util.List;

import edu.math.Matrix;
import edu.math.Point;
import edu.math.Vector;

public class Protein {
	
	List<AminoAcid> aaSeq;
	
	public Protein(List<AminoAcid> aaSeq) {
		this.aaSeq = aaSeq;
	}

	public List<Atom> getAtoms() {
		List<Atom> atoms = new LinkedList<Atom>();
		for(AminoAcid aa : aaSeq) {
			atoms.addAll(aa.getAtoms());
		}
		return atoms;
	}
	
	public void transformProtein(Matrix m) {
		for(Atom a : getAtoms()) {
			Vector v = new Vector(a.position);
			a.position = new Point(m.applyToIn(v));
		}
	}

	
}
