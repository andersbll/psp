package edu.allatom;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import com.sun.tools.internal.ws.wsdl.document.jaxws.Exception;

import edu.math.Point;
import edu.math.Vector;

public class AminoAcid {
	
	public static final List<String> backBoneAtomNames = new LinkedList<String>() {
		private static final long serialVersionUID = -5736976839907219408L;
		{
			add("CA");
			add("C");
			add("O");
			add("N");
			add("H");
//			add("CD");
		}
	};
	
	// map of valid rotamers of each amino acid type.
	// must be loaded from a rotamer library (load*RotamerLibrary()) before use.
	private static Map<Type, List<Rotamer>> validRotamers;
	// number of chi angles for each amino acid
	private static Map<Type, Integer> chiCountByType = new TreeMap<Type, Integer>() {
		private static final long serialVersionUID = 4133784286918804422L;
		{
			put(Type.ALA, 0);
			put(Type.ARG, 4);
			put(Type.ASN, 2);
			put(Type.ASP, 2);
			put(Type.CYS, 1);
			put(Type.GLN, 3);
			put(Type.GLU, 3);
			put(Type.GLY, 0);
			put(Type.HIS, 2);
			put(Type.ILE, 2);
			put(Type.LEU, 2);
			put(Type.LYS, 4);
			put(Type.MET, 3);
			put(Type.PHE, 2);
			put(Type.PRO, 2);
			put(Type.SER, 1);
			put(Type.THR, 1);
			put(Type.TRP, 2);
			put(Type.TYR, 2);
			put(Type.VAL, 1);
		}
	};
	
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

	public enum Type {
		ALA, 
		ARG,
		ASN,
		ASP,
		CYS,
		GLN,
		GLU,
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
			
			Type type = Type.fromName(tokens.get(0));
			if(type == null) {
				System.out.println("Unknown amino acid in rotamer library: " +
						tokens.get(0));
			}
			Rotamer rotamer = new Rotamer();
			rotamer.probability = Double.parseDouble(tokens.get(7)) / 100;
			for(int p=0; p<chiCountByType.get(type); p++) {
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

}
