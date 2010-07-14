package edu.allatom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import edu.math.Matrix;
import edu.math.Point;
import edu.math.TransformationMatrix3D;
import edu.math.Vector;

public class AminoAcid {
	public final AminoAcidType type;
	public final Map<String, Atom> allatoms;
	
	public Rotamer rotamer = null;

	public AminoAcid(AminoAcidType type) {
		this.type = type;
		allatoms = new TreeMap<String, Atom>();
	}

	public Atom getAtom(String atomName) {
		return allatoms.get(atomName);
	}
	
	public AminoAcid(AminoAcidType type, List<Atom> atoms) {
		this.type = type;
		allatoms = new HashMap<String, Atom>();
		for(Atom a : atoms) {
			addAtom(a);
		}
	}
	
	public double psi() {
		Atom Catom = this.allatoms.get("C");
		Point neighborN = null;
		for(Atom a : Catom.getBonds()) {
			if(a.label.equals("N")){
				neighborN = a.position;
			}
		}
		Point C = Catom.position;
		Point CA = this.allatoms.get("CA").position;
		Point N = this.allatoms.get("N").position;
		Vector v1 = neighborN.vectorTo(C);
		Vector v2 = C.vectorTo(CA);
		Vector v3 = CA.vectorTo(N);
		return Vector.dihedralAngle(v1, v2, v3) * 180 / Math.PI;
	}
	
	public double phi() {
		Point C = this.allatoms.get("C").position;
		Point CA = this.allatoms.get("CA").position;
		Atom Natom = this.allatoms.get("N");
		Point N = Natom.position;
		Point neighborC = null;
		for(Atom a : Natom.getBonds()) {
			if(a.label.equals("C")){
				neighborC = a.position;
			}
		}
		Vector v1 = C.vectorTo(CA);
		Vector v2 = CA.vectorTo(N);
		Vector v3 = N.vectorTo(neighborC);
		return Vector.dihedralAngle(v1, v2, v3) * 180 / Math.PI;
	}

	public void addAtom(Atom a) {
		allatoms.put(a.label, a);
	}
	
	public Collection<Atom> getAtoms() {
		return allatoms.values();
	}

	public List<Atom> getBackboneAtoms() {
		List<Atom> atoms = new LinkedList<Atom>();
		atoms.add(getAtom("CA"));
		atoms.add(getAtom("C"));
		atoms.add(getAtom("O"));
		atoms.add(getAtom("N"));
		atoms.add(getAtom("HA"));
		if(type!=AminoAcidType.PRO) {
			atoms.add(getAtom("H"));
		}

		while(atoms.contains(null)) {
			atoms.remove(null);
		}
		return atoms;
	}
	
	public String toString() {
		String s = type.name() + ":";
		for(String a : allatoms.keySet()) {
			s += "\n  " + a;
		}
		return s;
	}	
		
	
	/**
	 * Apple the given rotamer by positioning the atoms of the sidechain
	 * accordingly.
	 * Does not take collisions into consideration.
	 * 
	 * @param rotamer
	 */
	public void applyRotamer(Rotamer rotamer) {
		this.rotamer = rotamer;
        
        resetSidechainChiAngles(this.type, this.allatoms.values());

		List<Atom> atoms = new LinkedList<Atom>();
		for(int i=0; i<type.chiAngleCount; i++) {
			for(String label : type.chiAffectedAtomNames[i]) {
				atoms.add(allatoms.get(label));
			}
			setChiAngle(i, rotamer.chis[i], type, allatoms.values());
		}
	}
	
	/**
	 * Checks if any atoms of this amino acid collides with any atoms in the
	 * given protein.
	 * Any sidechains of amino acids that have not yet had a rotamer applied
	 * are ignored.
	 * 
	 * @param protein
	 * @return One of the colliding amino acids, or null if there are no collisions
	 */
	public Collection<AminoAcid> collides(Protein protein) {
		Collection<AminoAcid> collidees = new LinkedList<AminoAcid>();
		for(AminoAcid aa : protein.aaSeq) {
			if(aa != this) {
				Iterable<Atom> possibleCollisions;
				if(aa.rotamer == null) {
					possibleCollisions = aa.getBackboneAtoms();
				} else {
					possibleCollisions = aa.allatoms.values();
				}
				check: for(Atom a : possibleCollisions) {
					for(Atom b : allatoms.values()) {
						if(a.collides(b)) {
							collidees.add(aa);
							break check;
						}
					}
				}
			}
		}
		return collidees;
	}
	
	private static Atom getAtomByLabel(Collection<Atom> atoms, String label) {
		for(Atom a : atoms) {
			if(a.label.matches(label)) {
				return a;
			}
		}
		return null;
	}
	
	/**
	 * <code>getChiAngle</code> calculates the specified chi angle of the protein
	 *
	 * @param angleNumber number of the chi angle (0-3)
	 * @param type the type of amino acid
	 * @param atoms the atoms of the aminoacid
	 * @return the specified chi angle
	 */
	public static double getChiAngle(int angleNumber, AminoAcidType type, Collection<Atom> atoms) {
		Atom dihedralAtoms[] = new Atom[4];

		// Which atoms are used to calculate the chi angle depends on
		// which chi angle to compute
		switch(angleNumber) {
		case 0:
			dihedralAtoms[0] = getAtomByLabel(atoms, "N");
			dihedralAtoms[1] = getAtomByLabel(atoms, "CA");
			dihedralAtoms[2] = getAtomByLabel(atoms, "CB");
			dihedralAtoms[3] = getAtomByLabel(atoms, ".G.?");
			break;
		case 1:
			dihedralAtoms[0] = getAtomByLabel(atoms, "CA");
			dihedralAtoms[1] = getAtomByLabel(atoms, "CB");
			dihedralAtoms[2] = getAtomByLabel(atoms, ".G.?");
			dihedralAtoms[3] = getAtomByLabel(atoms, ".D.?");
			break;
		case 2:
			dihedralAtoms[0] = getAtomByLabel(atoms, "CB");
			dihedralAtoms[1] = getAtomByLabel(atoms, ".G");
			dihedralAtoms[2] = getAtomByLabel(atoms, ".D");
			dihedralAtoms[3] = getAtomByLabel(atoms, ".E.?");
			break;
		case 3:
			dihedralAtoms[0] = getAtomByLabel(atoms, ".G");
			dihedralAtoms[1] = getAtomByLabel(atoms, ".D");
			dihedralAtoms[2] = getAtomByLabel(atoms, ".E");
			dihedralAtoms[3] = getAtomByLabel(atoms, ".Z");
			break;
		}
		// Determine the chi angle by the four atom positions
		return Vector.dihedralAngle(
				dihedralAtoms[0].position,
				dihedralAtoms[1].position,
				dihedralAtoms[2].position,
				dihedralAtoms[3].position);
	}
	
	public static void setChiAngle(int angleNumber, double angle, AminoAcidType type, Collection<Atom> atoms) {
		Vector rotationVector = null;
		Atom rotationAtoms[] = null;

		// Find the two atoms that define the vector to rotate around
		switch(angleNumber) {
		case 0:
			rotationAtoms = new Atom[]{
					getAtomByLabel(atoms, "CA"), getAtomByLabel(atoms, "CB")};
			break;
		case 1:
			rotationAtoms = new Atom[]{
					getAtomByLabel(atoms, "CB"), getAtomByLabel(atoms, ".G.?")};
			break;
		case 2:
			rotationAtoms = new Atom[]{
					getAtomByLabel(atoms, ".G"), getAtomByLabel(atoms, ".D")};
			break;
		case 3:
			rotationAtoms = new Atom[]{
					getAtomByLabel(atoms, ".D"), getAtomByLabel(atoms, "[^H]E")};
			break;
		}
//		System.out.println(rotationAtoms[0] + ", " + rotationAtoms[0]);
		rotationVector = rotationAtoms[0].position.vectorTo(
				rotationAtoms[1].position);
		Matrix rotation = TransformationMatrix3D.createRotation(
				new Vector(rotationAtoms[1].position), rotationVector,
				(float) angle);
		
		// Rotate all atoms affected by the change in chi-angle
		Collection<String> atomsToRotate = getChiAffectedAtoms(angleNumber, type);
        for(String name : atomsToRotate) {
            Atom a = getAtomByLabel(atoms, name);
            a.position = rotation.applyTo(new Vector(a.position));
		}
	}

	public static Collection<String> getChiAffectedAtoms(int angleNumber, AminoAcidType type) {
		TreeSet<String> affected = new TreeSet<String>();
		for(int i = angleNumber; i < type.chiAngleCount; i++) {
			String[] atoms = type.chiAffectedAtomNames[i];
			for(int j = 0; j < atoms.length; j++)
				affected.add(atoms[j]);
		}
		return affected;
	}

	// Set the chi angle to zero
	public static void resetChiAngle(int angleNumber, AminoAcidType type, Collection<Atom> atoms) {
		// if(angleNumber == 0) {
		// 	Atom gamma = getAtomByLabel(atoms, ".G.?");
		// 	setChiAngle(0, -Math.PI/2 + Math.atan2(gamma.position.y(), gamma.position.z()), type, atoms);
		// } else {
//			double chiAngleBefore = getChiAngle(angleNumber, type, atoms);
//			System.out.println("Chi angle before: " + chiAngleBefore);
			setChiAngle(angleNumber, -getChiAngle(angleNumber, type, atoms), type, atoms);
//			double chiAngleAfter = getChiAngle(angleNumber, type, atoms);
//			System.out.println("Chi angle after: " + chiAngleAfter);
//		}
	}
	// Filter amino acids
	public static List<AminoAcid> getAminoAcidsOfType(Protein protein, AminoAcidType type) {
		List<AminoAcid> acids = new LinkedList<AminoAcid>();
		for(AminoAcid acid : protein.aaSeq) {
			if(acid.type.equals(type)) {
				acids.add(acid);
			}
		}
		return acids;
	}
	public static void resetSidechainChiAngles(AminoAcidType type, Collection<Atom> atoms) {
		for(int chi=0; chi<type.chiAngleCount; chi++) {
			resetChiAngle(chi, type, atoms);
		}
	}

	public static void resetSidechain(AminoAcidType type, Collection<Atom> atoms) {
		// Translate side chain such that CA is (0,0,0)
		resetSidechainPosition(type, atoms);
		
		// Find the first atom in the side chain
		Atom cbeta = getAtomByLabel(atoms, "CB");
		if(cbeta == null) {
			cbeta = getAtomByLabel(atoms, "HA3");
		}
		
		// Rotate such that the CA-CB bond follows the x-axis
		Vector cbeta_vec = new Vector(cbeta.position);
		float yproj_angle = (float)Math.atan2(cbeta_vec.z(), cbeta_vec.x());
		Matrix rotationY = TransformationMatrix3D.createRotation(
				new Vector(0,0,0), 
				new Vector(0,1,0),
				(float) yproj_angle);
		for(Atom a : atoms) {
			a.position = rotationY.applyTo(new Vector(a.position));
		}

		cbeta_vec = new Vector(cbeta.position);
		float zproj_angle = -(float)Math.atan2(cbeta_vec.y(),cbeta_vec.x());
		Matrix rotationZ = TransformationMatrix3D.createRotation(
		          new Vector(0,0,0), 
		          new Vector(0,0,1),
		          (float) zproj_angle);
		for(Atom a : atoms) {
			a.position = rotationZ.applyTo(new Vector(a.position));
		}
		
		resetSidechainChiAngles(type, atoms);
	}

	// Translate sidechain to (0,0,0)
	public static void resetSidechainPosition(AminoAcidType type, Collection<Atom> atoms) {
		Vector translation = getAtomByLabel(atoms, "CA").
				position.vectorTo(new Point(0, 0, 0));
		for(Atom a : atoms) {
			a.position = translation.plus(a.position);
		}
	}

	/**
	 * Calculate the average position of all side chain atoms
	 */
	public static List<Atom> 
		getAverageSidechain(
			AminoAcidType type, List<List<Atom>> sidechains) {
		List<String> labels = new LinkedList<String>();
		List<Atom> shortestSidechain = null; // yes, they should have the same length...
		for(List<Atom> s : sidechains) {
			if(shortestSidechain == null
					|| shortestSidechain.size() < shortestSidechain.size());
			shortestSidechain = s;
		}
		for(Atom a : shortestSidechain) {
			labels.add(a.label);
		}
		List<Atom> averageSidechain = new LinkedList<Atom>();
		for(String label : labels) {
			Vector averagePosition = new Vector(0, 0, 0);
			Atom a = null;
			int found = 0;
			for(List<Atom> sidechain : sidechains) {
				a = getAtomByLabel(sidechain, label);
				if(a != null) {
					averagePosition.plusIn(a.position);
					found++;
				}
			}
			averagePosition.divideIn(found);
			Atom averageAtom = new Atom(a.type, label, averagePosition);
			averageSidechain.add(averageAtom);
		}
		
		Bonder.bondSideChainAtoms(new AminoAcid(type, averageSidechain));
		Bonder.bondBackboneAtoms(new AminoAcid(type, averageSidechain));
		return averageSidechain;
	}
	
	/**
	 * Writes Java code that defines an array containing all atoms in the list.
	 *
	 * @return a <code>String</code> containing the java code
	 */
	public static String sidechainJavaRepresentation(List<Atom> atoms) {
		String s = "";
		s += "new Atom[]{\n";
		for(Atom a : atoms) {
			s += "	new Atom(Atom.Type." + a.type + ", \"" + a.label + "\", " +
					"new Point(" + a.position.x() + "f, " + a.position.y() +
					"f, " + a.position.z() + "f)),\n";
		}
		s += "},\n";
		return s;
	}
	
	public static List<AminoAcidType> makeTypeTrace(List<AminoAcid> trace) {
		List<AminoAcidType> typeTrace = new LinkedList<AminoAcidType>();
		for(AminoAcid aa : trace) {
			typeTrace.add(aa.type);
		}
		return typeTrace;
	}
}
