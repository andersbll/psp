package edu.allatom;

import java.util.Map;

/**
 *  Baad asss!
 */
public class Bonder {

	private static String innerAtomPairs[][] = {{"C","CA"},{"C","O"},{"N","CA"},{"H","N"}};
	private static String innerPROAtomPairs[][] = {{"C","CA"},{"C","O"},{"N","CA"}};
	private static String nTerminalAtomPairs[][] = {{"C","CA"},{"C","O"},{"N","CA"},{"H","N"},{"H2","N"},{"H3","N"},{"N","H1"}};
	private static String cTerminalAtomPairs[][] = {{"C","CA"},{"C","O"},{"N","CA"},{"H","N"},{"C","OXT"}};

	private static String ALAAtomPairs[][] = {{"CA","CB"},{"CB","HB1"},{"CB","HB3"},{"HA","CA"},{"HB2","CB"},{"N","H"}};
	private static String TYRAtomPairs[][] = {{"CA","CB"},{"CA","HA"},{"CB","CG"},{"CB","HB2"},{"CD1","CE1"},{"CD1","CG"},{"CD2","CE2"},{"CD2","HD2"},{"CE1","CZ"},{"CE2","HE2"},{"CG","CD2"},{"CZ","CE2"},{"CZ","OH"},{"H","N"},{"HB3","CB"},{"HD1","CD1"},{"HE1","CE1"},{"OH","HH"}};
	private static String GLNAtomPairs[][] = {{"CA","CB"},{"CB","HB3"},{"CD","CG"},{"CD","OE1"},{"CG","CB"},{"CG","HG2"},{"E21","NE2"},{"E22","NE2"},{"HA","CA"},{"HB2","CB"},{"HG3","CG"},{"N","H"},{"NE2","CD"}};
	private static String GLUAtomPairs[][] = {{"CA","HA"},{"CB","CA"},{"CB","CG"},{"CB","HB3"},{"CD","OE1"},{"CD","OE2"},{"CG","CD"},{"CG","HG2"},{"H","N"},{"HB2","CB"},{"HG3","CG"}};
	private static String TRPAtomPairs[][] = {{"CA","CB"},{"CA","HA"},{"CB","CG"},{"CB","HB2"},{"CD1","NE1"},{"CD2","CE2"},{"CE2","CZ2"},{"CE2","NE1"},{"CE3","CD2"},{"CE3","CZ3"},{"CG","CD1"},{"CG","CD2"},{"CH2","CZ2"},{"CH2","HH2"},{"CZ2","HZ2"},{"CZ3","CH2"},{"H","N"},{"HB3","CB"},{"HD1","CD1"},{"HE3","CE3"},{"HZ3","CZ3"},{"NE1","HE1"}};
	private static String LEUAtomPairs[][] = {{"CA","HA"},{"CB","CA"},{"CB","CG"},{"CD1","CG"},{"CD1","D13"},{"CD2","D21"},{"CD2","D23"},{"CG","CD2"},{"CG","HG"},{"D11","CD1"},{"D12","CD1"},{"D22","CD2"},{"H","N"},{"HB2","CB"},{"HB3","CB"}};
	private static String LYSAtomPairs[][] = {{"CB","CA"},{"CB","HB2"},{"CD","CG"},{"CD","HD2"},{"CD","HD3"},{"CE","CD"},{"CG","CB"},{"H","N"},{"HA","CA"},{"HB3","CB"},{"HE2","CE"},{"HE3","CE"},{"HG2","CG"},{"HG3","CG"},{"HZ1","NZ"},{"NZ","CE"},{"NZ","HZ2"},{"NZ","HZ3"}};
	private static String ASPAtomPairs[][] = {{"CA","CB"},{"CB","HB2"},{"CB","HB3"},{"CG","CB"},{"CG","OD2"},{"HA","CA"},{"N","H"},{"OD1","CG"}};
	private static String GLYAtomPairs[][] = {{"CA","HA3"},{"H","N"},{"HA2","CA"}};
	private static String PROAtomPairs[][] = {{"CA","CB"},{"CA","HA"},{"CB","HB3"},{"CD","CG"},{"CD","N"},{"CG","CB"},{"CG","HG3"},{"HB2","CB"},{"HD2","CD"},{"HD3","CD"},{"HG2","CG"}};
	private static String SERAtomPairs[][] = {{"CA","HA"},{"CB","CA"},{"CB","HB3"},{"H","N"},{"HB2","CB"},{"HG","OG"},{"OG","CB"}};
	private static String ARGAtomPairs[][] = {{"CA","HA"},{"CB","CA"},{"CD","CG"},{"CD","HD2"},{"CG","CB"},{"CG","HG2"},{"CZ","NE"},{"CZ","NH1"},{"H","N"},{"H12","NH1"},{"H21","NH2"},{"H22","NH2"},{"HB2","CB"},{"HB3","CB"},{"HD3","CD"},{"HE","NE"},{"HG3","CG"},{"NE","CD"},{"NH1","H11"},{"NH2","CZ"}};
	private static String THRAtomPairs[][] = {{"CA","HA"},{"CB","CA"},{"CB","CG2"},{"CB","HB"},{"CG2","G21"},{"CG2","G22"},{"G23","CG2"},{"H","N"},{"OG1","CB"},{"OG1","HG1"}};

	private static String GLYTerminalAtomPairs[][] = {{"HA2","CA"},{"HA3","CA"},{"N","H1"}};

	public static void bondAtoms(Protein p) {
		AminoAcid firstaa = p.aaSeq.get(0);
		bondNTerminusAtoms(firstaa);
		
		AminoAcid oldaa = firstaa;
		for (AminoAcid aa : p.aaSeq.subList(1, p.aaSeq.size() - 1)) {
			bondInnerAtoms(aa);
			bondSideChainAtoms(aa);
			bondAtoms(oldaa.allatoms.get("C"), aa.allatoms.get("N"));
			oldaa = aa;
		}
		
		AminoAcid lastaa = p.aaSeq.get(p.aaSeq.size()-1);
		bondCTerminusAtoms(lastaa);
		bondAtoms(oldaa.allatoms.get("C"), lastaa.allatoms.get("N"));
	}

	public static void bondNTerminusAtoms(AminoAcid aa) {
		bondAtomPairs(aa.allatoms, nTerminalAtomPairs);
		switch (aa.type) {
		case GLY:
			bondAtomPairs(aa.allatoms, GLYTerminalAtomPairs);
			break;
		default:
			bondSideChainAtoms(aa);
		}
	}

	public static void bondInnerAtoms(AminoAcid aa) {
		bondAtomPairs(aa.allatoms, nTerminalAtomPairs);
		switch (aa.type) {
		case PRO:
			bondAtomPairs(aa.allatoms, innerPROAtomPairs);
			break;
		default:
			bondAtomPairs(aa.allatoms, innerAtomPairs);
		}
	}

	public static void bondCTerminusAtoms(AminoAcid aa) {
		bondAtomPairs(aa.allatoms, cTerminalAtomPairs);
		switch (aa.type) {
//		case GLY:
//			bondAtomPairs(aa.allatoms, GLYTerminusAtomPairs);
//			break;
		default:
			bondSideChainAtoms(aa);
		}
	}
	
	public static void bondSideChainAtoms(AminoAcid aa) {
		String atomPairs[][] = null;
		switch (aa.type) {
		case ALA:
			atomPairs = ALAAtomPairs;
			break;
		case ARG:
			atomPairs = ARGAtomPairs;
			break;
		case ASP:
			atomPairs = ASPAtomPairs;
			break;
		case GLN:
			atomPairs = GLNAtomPairs;
			break;
		case GLU:
			atomPairs = GLUAtomPairs;
			break;
		case GLY:
			atomPairs = GLYAtomPairs;
			break;
		case LEU:
			atomPairs = LEUAtomPairs;
			break;
		case LYS:
			atomPairs = LYSAtomPairs;
			break;
		case PRO:
			atomPairs = PROAtomPairs;
			break;
		case SER:
			atomPairs = SERAtomPairs;
			break;
		case THR:
			atomPairs = THRAtomPairs;
			break;
		case TRP:
			atomPairs = TRPAtomPairs;
			break;
		case TYR:
			atomPairs = TYRAtomPairs;
			break;
		default:
			System.out.print("Av, forkert aminosyre:");
			return;
		}
		if(!bondAtomPairs(aa.allatoms, atomPairs)) {
			System.out.print("Av, aminosyre indeholder ikke genkendte atomer:\n  ");
			printAtomBonds(aa);
		}
	}

	private static boolean bondAtomPairs(Map<String,Atom> atoms, String pairs[][]) {
		for (String pair[] : pairs) {
			Atom a0 = atoms.get(pair[0]);
			Atom a1 = atoms.get(pair[1]);
			if(a0==null || a1==null) {
				return false;
			}
			bondAtoms(a0,a1);
		}		
		return true;
	}
	
	public static void bondAtoms(Atom a1, Atom a2) {
		a1.bondsTo.add(a2);
		a2.bondsTo.add(a1);
	}
	
	public static void printAtomBonds(Protein p) {
		for (AminoAcid aa : p.aaSeq.subList(1, p.aaSeq.size() - 1)) {
			printAtomBonds(aa);
		}
	}

	public static void printAtomBonds(AminoAcid aa) {
		String atomPairs = "private static String " + aa.type.name()
				+ "AtomPairs[][] = {";
		for (Atom a1 : aa.allatoms.values()) {
			for (Atom a2 : aa.allatoms.values()) {
				if (a1.position.x() < a2.position.x()
						&& !isInPairList(a1, a2, innerAtomPairs)) {
					double distance = a1.position.distance(a2.position);
					if (distance > 0
							&& distance < Atom.BINDING_DISTANCE_THRESHOLD) {
						atomPairs += "{\"" + a1 + "\",\"" + a2 + "\"},";
					}
				}
			}
		}
		atomPairs = atomPairs.substring(0, atomPairs.length() - 1) + "};";
		System.out.println(atomPairs);

	}
	
	private static boolean isInPairList(Atom a1, Atom a2, String pairList[][]) {
		for(String pair[] : pairList) {
			if(pair[0].equals(a1.toString()) && pair[1].equals(a2.toString())){
				return true;
			} 
			if(pair[0].equals(a2.toString()) && pair[1].equals(a1.toString())){
				return true;
			} 
		}
		return false;
	}

}
