package edu.allatom;

import java.util.*;

// AminoAcid types specification
enum AminoAcidType {
	ALA (
	     0,
	     new Atom[]{
		     //				new Atom(Atom.Type.C, "C1", new Vector(0, 0, 0)),
		     //				new Atom(Atom.Type.C, "C1", new Vector(0, 0, 1)),
	     },
	     new String[][]{{"CA","CB"},{"CA","HA"},{"CB","HB1"},{"CB","HB2"},{"CB","HB3"}}
	     ),
	ARG (
	     4,
	     new Atom[]{
	     },
	     new String[][]{{"CA","CB"},{"CA","HA"},{"CB","CG"},{"CB","HB2"},{"CB","HB3"},{"CD","CG"},{"CD","HD2"},{"CD","HD3"},{"CD","NE"},{"CG","HG2"},{"CG","HG3"},{"CZ","NE"},{"CZ","NH1"},{"CZ","NH2"},{"H11","NH1"},{"H12","NH1"},{"H21","NH2"},{"H22","NH2"},{"HE","NE"}}
	     ),
	ASN (
	     2,
	     new Atom[]{
	     },
	     new String[][]{{"CA","CB"},{"CA","HA"},{"CB","CG"},{"CB","HB2"},{"CB","HB3"},{"CG","ND2"},{"CG","OD1"},{"D21","ND2"},{"D22","ND2"}}),
		ASP (
		  2,
		  new Atom[]{
		  },
		  new String[][]{{"CA","CB"},{"CA","HA"},{"CB","CG"},{"CB","HB2"},{"CB","HB3"},{"CG","OD1"},{"CG","OD2"}}
	  ),
	  CYS (
		  1,
		  new Atom[]{
		  },
		  new String[][]{{"CA","CB"},{"CA","HA"},{"CB","HB2"},{"CB","HB3"},{"CB","SG"},{"HG","SG"}}
	  ),
	  GLN (
		  3,
		  new Atom[]{
		  },
		  new String[][]{{"CA","CB"},{"CA","HA"},{"CB","CG"},{"CB","HB2"},{"CB","HB3"},{"CD","CG"},{"CD","NE2"},{"CD","OE1"},{"CG","HG2"},{"CG","HG3"},{"E21","NE2"},{"E22","NE2"}}
	  ),
	  GLU (
		  3,
		  new Atom[]{
		  },
		  new String[][]{{"CA","HA"},{"CB","CA"},{"CB","CG"},{"CB","HB3"},{"CD","OE1"},{"CD","OE2"},{"CG","CD"},{"CG","HG2"},{"H","N"},{"HB2","CB"},{"HG3","CG"}}
	  ),
	  GLY (
		  0,
		  new Atom[]{
		  },
		  new String[][]{{"CA","HA2"},{"CA","HA3"}}
	  ),
	  HIS (
		  2,
		  new Atom[]{
		  },
		  new String[][]{{"CA","CB"},{"CA","HA"},{"CB","CG"},{"CB","HB2"},{"CB","HB3"},{"CD2","CG"},{"CD2","HD2"},{"CD2","NE2"},{"CE1","HE1"},{"CE1","ND1"},{"CE1","NE2"},{"CG","ND1"}} //FIXME abll: jeg mener at {"HE2","NE2"} skal tilføjes
	  ),
	  ILE (
		  2,
		  new Atom[]{
		  },
		  new String[][]{{"CA","CB"},{"CA","HA"},{"CB","CG1"},{"CB","CG2"},{"CB","HB"},{"CD1","CG1"},{"CD1","D11"},{"CD1","D12"},{"CD1","D13"},{"CG1","G12"},{"CG1","G13"},{"CG2","G21"},{"CG2","G22"},{"CG2","G23"}}
	  ),
	  LEU (
		  2,
		  new Atom[]{
		  },
		  new String[][]{{"CA","CB"},{"CA","HA"},{"CB","CG"},{"CB","HB2"},{"CB","HB3"},{"CD1","CG"},{"CD1","D11"},{"CD1","D12"},{"CD1","D13"},{"CD2","CG"},{"CD2","D21"},{"CD2","D22"},{"CD2","D23"},{"CG","HG"}}
	  ),
	  LYS (
		  4,
		  new Atom[]{
		  },
		  new String[][]{{"CA","CB"},{"CA","HA"},{"CB","CG"},{"CB","HB2"},{"CB","HB3"},{"CD","CE"},{"CD","CG"},{"CD","HD2"},{"CD","HD3"},{"CE","HE2"},{"CE","HE3"},{"CE","NZ"},{"CG","HG2"},{"CG","HG3"},{"HZ1","NZ"},{"HZ2","NZ"},{"HZ3","NZ"}}
	  ),
	  MET (
		  3,
		  new Atom[]{
		  },
		  new String[][]{{"CA","CB"},{"CA","HA"},{"CB","CG"},{"CB","HB2"},{"CB","HB3"},{"CE","HE1"},{"CE","HE2"},{"CE","HE3"},{"CE","SD"},{"CG","HG2"},{"CG","HG3"},{"CG","SD"}}
	  ),
	  PHE (
		  2,
		  new Atom[]{
		  },
		  new String[][]{{"CA","CB"},{"CA","HA"},{"CB","CG"},{"CB","HB2"},{"CB","HB3"},{"CD1","CE1"},{"CD1","CG"},{"CD1","HD1"},{"CD2","CE2"},{"CD2","CG"},{"CD2","HD2"},{"CE1","CZ"},{"CE1","HE1"},{"CE2","CZ"},{"CE2","HE2"},{"CZ","HZ"}}
	  ),
	  PRO (
		  2,
		  new Atom[]{
		  },
		  new String[][]{{"CA","CB"},{"CA","HA"},{"CB","CG"},{"CB","HB2"},{"CB","HB3"},{"CD","CG"},{"CD","HD2"},{"CD","HD3"},{"CD","N"},{"CG","HG2"},{"CG","HG3"}}
	  ),
	  SER (
		  1,
		  new Atom[]{
		  },
		  new String[][]{{"CA","CB"},{"CA","HA"},{"CB","HB2"},{"CB","HB3"},{"CB","OG"},{"HG","OG"}}
	  ),
	  THR (
		  1,
		  new Atom[]{
		  },
		  new String[][]{{"CA","HA"},{"CB","CA"},{"CB","CG2"},{"CB","HB"},{"CG2","G21"},{"CG2","G22"},{"G23","CG2"},{"H","N"},{"OG1","CB"},{"OG1","HG1"}}
	  ),
	  TRP (
		  2,
		  new Atom[]{
		  },
		  new String[][]{{"CA","CB"},{"CA","HA"},{"CB","CG"},{"CB","HB2"},{"CB","HB3"},{"CD1","CG"},{"CD1","HD1"},{"CD1","NE1"},{"CD2","CE2"},{"CD2","CE3"},{"CD2","CG"},{"CE2","CZ2"},{"CE2","NE1"},{"CE3","CZ3"},{"CE3","HE3"},{"CH2","CZ2"},{"CH2","CZ3"},{"CH2","HH2"},{"CZ2","HZ2"},{"CZ3","HZ3"},{"HE1","NE1"}}
	  ),
	  TYR (
		  2,
		  new Atom[]{
		  },
		  new String[][]{{"CA","CB"},{"CA","HA"},{"CB","CG"},{"CB","HB2"},{"CB","HB3"},{"CD1","CE1"},{"CD1","CG"},{"CD1","HD1"},{"CD2","CE2"},{"CD2","CG"},{"CD2","HD2"},{"CE1","CZ"},{"CE1","HE1"},{"CE2","CZ"},{"CE2","HE2"},{"CZ","OH"},{"HH","OH"}}
	  ),
	  VAL (
		  1,
		  new Atom[]{
		  },
		  new String[][]{{"CA","CB"},{"CA","HA"},{"CB","CG1"},{"CB","CG2"},{"CB","HB"},{"CG1","G11"},{"CG1","G12"},{"CG1","G13"},{"CG2","G21"},{"CG2","G22"},{"CG2","G23"}}
	       );

	int chiAngleCount;
	Atom[] sidechainAtoms;
	String[][] atomBonds;

	List<String> followBond(String atomLabel) {
		Set<String> destinations = new HashSet<String>();
		for(String s[] : atomBonds) {
			if(s[0].equals(atomLabel)) {
				destinations.add(s[1]);
			}
			if(s[1].equals(atomLabel)) {
				destinations.add(s[0]);
			}
		}
		return new LinkedList<String>(destinations);
	}

	AminoAcidType(int chiN, Atom[] atoms, String[][] bonds) {
		sidechainAtoms = atoms;
		chiAngleCount = chiN;
		atomBonds = bonds;
	}
}
