package edu.allatom;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import edu.math.Matrix;
import edu.math.Point;
import edu.math.TransformationMatrix3D;
import edu.math.Vector;
import edu.math.Vector2D;

/**
 * Describe class <code>AminoAcid</code> here.
 *
 * @author <a href="mailto:dybber@waterbear">Martin Dybdal</a>
 * @version 1.0
 */
public class AminoAcid {
	
	public static final List<String> backBoneAtomNames = new LinkedList<String>() {
		private static final long serialVersionUID = -5736976839907219408L;
		{
			add("CA");
			add("C");
			add("O");
			add("N");
			add("H");
		}
	};
	
	// map of valid rotamers of each amino acid type.
	// must be loaded from a rotamer library (load*RotamerLibrary()) before use.
	private static Map<Type, List<Rotamer>> validRotamers;

	// AminoAcid types specification
	public enum Type {
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
		
		int chiAngleCount;
		Atom[] sidechainAtoms;
		String[][] atomBonds;
		Type(int chiN, Atom[] atoms, String[][] bonds) {
			sidechainAtoms = atoms;
			chiAngleCount = chiN;
			atomBonds = bonds;
		}
	}
	
	private static Random random = new Random(System.currentTimeMillis());
	
	public final Type type;
	public final Map<String, Atom> allatoms;
	
	// list of rotamers that have been tried for this amino acid
	private List<Rotamer> usedRotamers = new LinkedList<Rotamer>();
	// sum of the probabilities of the rotamers that have been tried for this
	// amino acid
	private double usedRotamersProbability = 0;
	// the chosen rotamer. if null, no rotamer is chosen yet.
	public Rotamer rotamer;

	public AminoAcid(Type type) {
		this.type = type;
		allatoms = new TreeMap<String, Atom>();
	}

	public Atom getAtom(String atomName) {
		return allatoms.get(atomName);
	}
	
	public AminoAcid(Type type, List<Atom> atoms) {
		this.type = type;
		allatoms = new HashMap<String, Atom>();
		for(Atom a : atoms) {
			addAtom(a);
		}
	}
	
	public double psi() {
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
		return Vector.dihedralAngle(v1, v2, v3) * 180 / Math.PI;
	}
	
	public double phi() {
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
		return Vector.dihedralAngle(v1, v2, v3) * 180 / Math.PI;
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
		} //else {
//			atoms.add(allatoms.get("CD"));			
//		}
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
	
//	public enum SequencePosition {
//		N_TERMINUS, MIDDLE, C_TERMINUS;
//	}

//	public enum Type {
//		ALA, 
//		ARG,
//		ASN,
//		ASP,
//		CYS,
//		GLN,
//		GLU,
//		GLY,
//		HIS,
//		ILE,
//		LEU,
//		LYS,
//		MET,
//		PHE,
//		PRO,
//		SER,
//		THR,
//		TRP,
//		TYR,
//		VAL;
//		
////		public final String name;
////		Type(String name3) {
////			this.name = name3;
////		}
//		public static Type fromName(String name) {
//			for(Type t : values()) {
//				if(name.equalsIgnoreCase(t.name())) {
//					return t;
//				}
//			}
//			return null;
//		}
//	}
	
	
	private static class Rotamer {
		double probability;
		double chis[] = new double[4];
		
		public String toString() {
			return "Rotamer (p " + probability + ", phi " + chis[0] + ", " +
					chis[1] + ", " + chis[2] + ", " + chis[3] + ")";
		}
	}
	
	/**
	 * Apply the next rotamer chosen from all valid rotamers except the ones
	 * already used.
	 * Does not check for collisions.
	 * 
	 *  @return True only if a not previously used rotamer is found.
	 */
	public boolean nextRotamer() {
		if(usedRotamersProbability >= 1) {
			return false;
		}
		double r = random.nextDouble() * (1 - usedRotamersProbability);
		for(Rotamer rotamer : validRotamers.get(type)) {
			if(!usedRotamers.contains(rotamer)) {
				usedRotamers.add(rotamer);
				usedRotamersProbability += rotamer.probability;
				if(r <= rotamer.probability) {
					applyRotamer(rotamer);
					return true;
				} else {
					r -= rotamer.probability;
				}
			}
		}
		return false;
	}
	
	/**
	 * Apply the next rotamer chosen from all valid rotamers except the ones
	 * already used, and except those that collides with the given protein.
	 * 
	 * @param protein
	 * @return true only if an not previously used collisionless rotamer is
	 * found.
	 */
	public boolean nextCollisionlessRotamer(Protein protein) {
		do {
			boolean anymore = nextRotamer();
			if(!anymore) {
				return false;
			}
		} while(collides(protein));
		return true;
	}
	
	/**
	 * Apple the given rotamer by positioning the atoms of the sidechain
	 * accordingly.
	 * Does not take collisions into consideration.
	 * 
	 * @param rotamer
	 */
	private void applyRotamer(Rotamer rotamer) {
		this.rotamer = rotamer;
		//TODO
	}
	
	/**
	 * Checks if any atoms of this amino acid collides with any atoms in the
	 * given protein.
	 * Any sidechains of amino acids that have not yet had a rotamer applied
	 * are ignored.
	 * 
	 * @param protein
	 * @return
	 */
	private boolean collides(Protein protein) {
		for(AminoAcid aa : protein.aaSeq) {
			if(aa != this) {
				Iterable<Atom> collidees;
				if(aa.rotamer == null) {
					collidees = aa.getBackBoneAtoms();
				} else {
					collidees = aa.allatoms.values();
				}
				for(Atom a : collidees) {
					for(Atom b : allatoms.values()) {
						if(a.collides(b)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	static void loadDunbrachRotamerLibrary(String filename) throws IOException {
		// read file
		StringBuilder sb = new StringBuilder();
		FileReader fr = new FileReader(filename);
		int c;
		while((c = fr.read()) != -1) {
			sb.append((char) c);
		}
		String data = sb.toString();
		
		// parse data
		validRotamers = new TreeMap<Type, List<Rotamer>>();
		String lines[] = data.substring(data.indexOf("\nARG ")).split("\n");
		Type previousType = null;
		List<Rotamer> rotamersOfCurrentType = new LinkedList<Rotamer>();
		for(String line : lines) {
			if(line.trim().length() == 0) {
				continue;
			}
			List<String> tokens = new ArrayList<String>(20);
			for(String token : line.split(" ")) {
				if(token.length() > 0) {
					tokens.add(token);
				}
			}
			
			Type type = Type.valueOf(tokens.get(0));
			if(type == null) {
				System.out.println("Unknown amino acid in rotamer library: " +
						tokens.get(0));
			}
			Rotamer rotamer = new Rotamer();
			rotamer.probability = Double.parseDouble(tokens.get(7)) / 100;
			for(int p=0; p<Type.valueOf(type.toString()).chiAngleCount; p++) {
				rotamer.chis[p] = Double.parseDouble(tokens.get(11 + 2*p));
			}
			
			if(type != previousType && previousType != null) {
				validRotamers.put(previousType, rotamersOfCurrentType);
				rotamersOfCurrentType = new LinkedList<Rotamer>();
			}
			rotamersOfCurrentType.add(rotamer);
			previousType = type;
		}
		validRotamers.put(previousType, rotamersOfCurrentType);
		
		Rotamer alaRotamer = new Rotamer();
		alaRotamer.probability = 1;
		List<Rotamer> alaRotamers = new LinkedList<Rotamer>();
		alaRotamers.add(alaRotamer);
		validRotamers.put(Type.ALA, alaRotamers);
		
		Rotamer glyRotamer = new Rotamer();
		glyRotamer.probability = 1;
		List<Rotamer> glyRotamers = new LinkedList<Rotamer>();
		glyRotamers.add(glyRotamer);
		validRotamers.put(Type.GLY, glyRotamers);
		
//		List<Rotamer> aspRotamers = validRotamers.get(Type.ARG);
//		double p = 0;
//		for(Rotamer r : aspRotamers) {
//			System.out.println(r);
//			p += r.probability;
//		}
//		System.out.println("p: " + p);
		
//		throw new IOException();
	}
	
	
	// functions for running statistics and stuff on sidechains
	

	private static Atom getAtomByLabel(List<Atom> atoms, String label) {
		for(Atom a : atoms) {
			if(a.name.matches(label)) {
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
	private static double getChiAngle(int angleNumber, Type type, List<Atom> atoms) {
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
	

	private static List<Atom> getSidechainAtomsAfter(
			Type type, List<Atom> atoms, Atom terminator) {
		List<String> labels = new LinkedList<String>();
		
		labels.add("N");
		labels.add("C");
		labels.add("CA");
		labels.add("O");
		labels.add("H");
		if(type == Type.GLY) {
			labels.add("HA2");
		} else {
			labels.add("HA");
		}
		
		String a = "CA";
		while(!a.equals(terminator.name)) {
			String next = null;
			for(String neighbor : type.followBond(a)) {
				if(!labels.contains(neighbor)) {
					labels.add(neighbor);
					if(!neighbor.startsWith("H")) {
						next = neighbor;
					}
				}
			}
			a = next;
		}
		
		List<Atom> atomsAfter = new LinkedList<Atom>();
		for(Atom atom : atoms) {
			if(!labels.contains(atom.name)) {
				atomsAfter.add(atom);
			}
		}
		
		return atomsAfter;
	}

	
	private static void setChiAngle(int angleNumber, double angle, Type type, List<Atom> atoms) {
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
					getAtomByLabel(atoms, ".D"), getAtomByLabel(atoms, ".E")};
			break;
		}
		rotationVector = rotationAtoms[0].position.vectorTo(
				rotationAtoms[1].position);
		Matrix rotation = TransformationMatrix3D.createRotation(
				new Vector(rotationAtoms[1].position), rotationVector,
				(float) angle);
		
		// Rotate all atoms affected by the change in chi-angle
		List<Atom> atomsToRotate = getSidechainAtomsAfter(type, atoms, rotationAtoms[1]);
		for(Atom a : atomsToRotate) {
			a.position = rotation.applyTo(new Vector(a.position));
		}
	}

	// Set the chi angle to zero
	private static void resetChiAngle(int angleNumber, Type type, List<Atom> atoms) {
		double chiAngleBefore = getChiAngle(angleNumber, type, atoms);
		System.out.println("Chi angle before: " + chiAngleBefore);
		setChiAngle(angleNumber, -getChiAngle(angleNumber, type, atoms), type, atoms);
		double chiAngleAfter = getChiAngle(angleNumber, type, atoms);
		System.out.println("Chi angle after: " + chiAngleAfter);
	}
	// Filter amino acids
	public static List<AminoAcid> getAminoAcidsOfType(Protein protein, Type type) {
		List<AminoAcid> acids = new LinkedList<AminoAcid>();
		for(AminoAcid acid : protein.aaSeq) {
			if(acid.type.equals(type)) {
				acids.add(acid);
			}
		}
		return acids;
	}
	public static void resetSidechainChiAngles(Type type, List<Atom> atoms) {
		for(int chi=0; chi<type.chiAngleCount; chi++) {
			resetChiAngle(chi, type, atoms);
		}
	}

	public static void resetSidechain(Type type, List<Atom> atoms) {
		resetSidechainChiAngles(type, atoms);
		resetSidechainPosition(type, atoms);
		Atom cbeta = getAtomByLabel(atoms, "CB");
		Vector2D xnorm = new Vector2D(1,0);
		Vector cbeta_vec = new Vector(cbeta.position);
		
		Vector2D yproj = new Vector2D(cbeta_vec.x(), cbeta_vec.z());
		Vector2D zproj = new Vector2D(cbeta_vec.x(), cbeta_vec.y());
		float yproj_angle = yproj.rotationAngle(xnorm);
		float zproj_angle = zproj.rotationAngle(xnorm);

		Matrix rotation1 = TransformationMatrix3D.createRotation(new Vector(0,0,0), 
		                                                         new Vector(0,1,0),
		                                                         (float) yproj_angle);

		Matrix rotation2 = TransformationMatrix3D.createRotation(new Vector(0,0,0), 
		                                                         new Vector(0,0,1),
		                                                         (float) zproj_angle);

		Matrix rotation = rotation1.applyTo(rotation2);

		for(Atom a : atoms) {
			a.position = rotation.applyTo(new Vector(a.position));
		}
	}

	// Translate sidechain to (0,0,0)
	public static void resetSidechainPosition(Type type, List<Atom> atoms) {
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
			Type type, List<List<Atom>> sidechains) {
		//TODO fix this function!
		List<String> labels = new LinkedList<String>();
		for(Atom a : sidechains.get(0)) {
			labels.add(a.name);
		}
		List<Atom> averageSidechain = new LinkedList<Atom>();
		for(String label : labels) {
			Vector averagePosition = new Vector(0, 0, 0);
			Atom a = null;
			for(List<Atom> sidechain : sidechains) {
				a = getAtomByLabel(sidechain, label);
				averagePosition.plusIn(a.position);
			}
			averagePosition.divideIn(sidechains.size());
			Atom averageAtom = new Atom(a.type, label, averagePosition);
			averageSidechain.add(averageAtom);
		}
		
		Bonder.bondSideChainAtoms(new AminoAcid(type, averageSidechain));
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
			s += "	new Atom(Atom.Type." + a.type + "), \"" + a.name + "\", " +
					"new Vector(" + a.position.x() + ", " + a.position.y() +
					", " + a.position.z() + ")),\n";
		}
		s += "},\n";
		return s;
	}

}
