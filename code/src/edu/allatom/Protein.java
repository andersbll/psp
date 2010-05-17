package edu.allatom;

import java.util.LinkedList;
import java.util.List;

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
	
	
}
