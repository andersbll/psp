package edu.allatom;

import java.util.*;

/**
 *  Baad asss!
 */
public class Bonder {

	private static String backboneAtomPairs[][] = {{"C","CA"},{"C","O"},{"N","CA"},{"H","N"}};
	private static String PRObackboneAtomPairs[][] = {{"C","CA"},{"C","O"},{"N","CA"}};

	private static String nTerminalAtomPairs[][] = {{"C","CA"},{"C","O"},{"N","CA"},{"H2","N"},{"H3","N"},{"N","H1"}};

	private static String cTerminalAtomPairs[][] = {{"C","CA"},{"C","O"},{"N","CA"},{"H","N"},{"C","OXT"}};
	private static String PROcTerminalAtomPairs[][] = {{"C","CA"},{"C","O"},{"N","CA"},{"C","OXT"}};

	private static String ALAAtomPairs[][] = {{"CA","CB"},{"CA","HA"},{"CB","HB1"},{"CB","HB2"},{"CB","HB3"}};
	private static String ARGAtomPairs[][] = {{"CA","CB"},{"CA","HA"},{"CB","CG"},{"CB","HB2"},{"CB","HB3"},{"CD","CG"},{"CD","HD2"},{"CD","HD3"},{"CD","NE"},{"CG","HG2"},{"CG","HG3"},{"CZ","NE"},{"CZ","NH1"},{"CZ","NH2"},{"H11","NH1"},{"H12","NH1"},{"H21","NH2"},{"H22","NH2"},{"HE","NE"}};
	private static String ASNAtomPairs[][] = {{"CA","CB"},{"CA","HA"},{"CB","CG"},{"CB","HB2"},{"CB","HB3"},{"CG","ND2"},{"CG","OD1"},{"D21","ND2"},{"D22","ND2"}};
	private static String ASPAtomPairs[][] = {{"CA","CB"},{"CA","HA"},{"CB","CG"},{"CB","HB2"},{"CB","HB3"},{"CG","OD1"},{"CG","OD2"}};
	private static String GLNAtomPairs[][] = {{"CA","CB"},{"CA","HA"},{"CB","CG"},{"CB","HB2"},{"CB","HB3"},{"CD","CG"},{"CD","NE2"},{"CD","OE1"},{"CG","HG2"},{"CG","HG3"},{"E21","NE2"},{"E22","NE2"}};
	private static String GLUAtomPairs[][] = {{"CA","HA"},{"CB","CA"},{"CB","CG"},{"CB","HB3"},{"CD","OE1"},{"CD","OE2"},{"CG","CD"},{"CG","HG2"},{"H","N"},{"HB2","CB"},{"HG3","CG"}};
	private static String GLYAtomPairs[][] = {{"CA","HA2"},{"CA","HA3"}};
	private static String LEUAtomPairs[][] = {{"CA","CB"},{"CA","HA"},{"CB","CG"},{"CB","HB2"},{"CB","HB3"},{"CD1","CG"},{"CD1","D11"},{"CD1","D12"},{"CD1","D13"},{"CD2","CG"},{"CD2","D21"},{"CD2","D22"},{"CD2","D23"},{"CG","HG"}};
	private static String LYSAtomPairs[][] = {{"CA","CB"},{"CA","HA"},{"CB","CG"},{"CB","HB2"},{"CB","HB3"},{"CD","CE"},{"CD","CG"},{"CD","HD2"},{"CD","HD3"},{"CE","HE2"},{"CE","HE3"},{"CE","NZ"},{"CG","HG2"},{"CG","HG3"},{"HZ1","NZ"},{"HZ2","NZ"},{"HZ3","NZ"}};
	private static String PROAtomPairs[][] = {{"CA","CB"},{"CA","HA"},{"CB","CG"},{"CB","HB2"},{"CB","HB3"},{"CD","CG"},{"CD","HD2"},{"CD","HD3"},{"CD","N"},{"CG","HG2"},{"CG","HG3"}};
	private static String SERAtomPairs[][] = {{"CA","CB"},{"CA","HA"},{"CB","HB2"},{"CB","HB3"},{"CB","OG"},{"HG","OG"}};
	private static String THRAtomPairs[][] = {{"CA","HA"},{"CB","CA"},{"CB","CG2"},{"CB","HB"},{"CG2","G21"},{"CG2","G22"},{"G23","CG2"},{"H","N"},{"OG1","CB"},{"OG1","HG1"}};
	private static String TRPAtomPairs[][] = {{"CA","CB"},{"CA","HA"},{"CB","CG"},{"CB","HB2"},{"CB","HB3"},{"CD1","CG"},{"CD1","HD1"},{"CD1","NE1"},{"CD2","CE2"},{"CD2","CE3"},{"CD2","CG"},{"CE2","CZ2"},{"CE2","NE1"},{"CE3","CZ3"},{"CE3","HE3"},{"CH2","CZ2"},{"CH2","CZ3"},{"CH2","HH2"},{"CZ2","HZ2"},{"CZ3","HZ3"},{"HE1","NE1"}};
	private static String TYRAtomPairs[][] = {{"CA","CB"},{"CA","HA"},{"CB","CG"},{"CB","HB2"},{"CB","HB3"},{"CD1","CE1"},{"CD1","CG"},{"CD1","HD1"},{"CD2","CE2"},{"CD2","CG"},{"CD2","HD2"},{"CE1","CZ"},{"CE1","HE1"},{"CE2","CZ"},{"CE2","HE2"},{"CZ","OH"},{"HH","OH"}};
	private static String ILEAtomPairs[][] = {{"CA","CB"},{"CA","HA"},{"CB","CG1"},{"CB","CG2"},{"CB","HB"},{"CD1","CG1"},{"CD1","D11"},{"CD1","D12"},{"CD1","D13"},{"CG1","G12"},{"CG1","G13"},{"CG2","G21"},{"CG2","G22"},{"CG2","G23"}};
	private static String VALAtomPairs[][] = {{"CA","CB"},{"CA","HA"},{"CB","CG1"},{"CB","CG2"},{"CB","HB"},{"CG1","G11"},{"CG1","G12"},{"CG1","G13"},{"CG2","G21"},{"CG2","G22"},{"CG2","G23"}};
	private static String PHEAtomPairs[][] = {{"CA","CB"},{"CA","HA"},{"CB","CG"},{"CB","HB2"},{"CB","HB3"},{"CD1","CE1"},{"CD1","CG"},{"CD1","HD1"},{"CD2","CE2"},{"CD2","CG"},{"CD2","HD2"},{"CE1","CZ"},{"CE1","HE1"},{"CE2","CZ"},{"CE2","HE2"},{"CZ","HZ"}};
	private static String METAtomPairs[][] = {{"CA","CB"},{"CA","HA"},{"CB","CG"},{"CB","HB2"},{"CB","HB3"},{"CE","HE1"},{"CE","HE2"},{"CE","HE3"},{"CE","SD"},{"CG","HG2"},{"CG","HG3"},{"CG","SD"}};
	private static String CYSAtomPairs[][] = {{"CA","CB"},{"CA","HA"},{"CB","HB2"},{"CB","HB3"},{"CB","SG"},{"HG","SG"}};
	private static String HISAtomPairs[][] = {{"CA","CB"},{"CA","HA"},{"CB","CG"},{"CB","HB2"},{"CB","HB3"},{"CD2","CG"},{"CD2","HD2"},{"CD2","NE2"},{"CE1","HE1"},{"CE1","ND1"},{"CE1","NE2"},{"CG","ND1"},{"HE2","NE2"}};

	
	public static void bondAtoms(Protein p) {
		AminoAcid firstaa = p.aaSeq.get(0);
		bondNTerminusAtoms(firstaa);
		
		AminoAcid oldaa = firstaa;
		for (AminoAcid aa : p.aaSeq.subList(1, p.aaSeq.size() - 1)) {
			bondBackboneAtoms(aa);
			bondSideChainAtoms(aa);
			bondAtoms(oldaa.allatoms.get("C"), aa.allatoms.get("N"));
			oldaa = aa;
		}
		
		AminoAcid lastaa = p.aaSeq.get(p.aaSeq.size()-1);
		bondCTerminusAtoms(lastaa);
		bondAtoms(oldaa.allatoms.get("C"), lastaa.allatoms.get("N"));
	}

	public static void bondBackboneAtoms(AminoAcid aa) {
		String atomPairs[][] = null;
		switch (aa.type) {
		case PRO:
			atomPairs = PRObackboneAtomPairs;
			break;
		default:
			atomPairs = backboneAtomPairs;
		}
		if(!bondAtomPairs(aa.allatoms, atomPairs)) {
			System.out.println("av, parring af backbone fejlede:\n");
			printAtomBonds(aa);
		}
	}
	
	public static void bondNTerminusAtoms(AminoAcid aa) {
		String atomPairs[][] = null;
		switch (aa.type) {
//		case GLY:
//			atomPairs = GLYnTerminalAtomPairs;
//			break;
		default:
			atomPairs = nTerminalAtomPairs;
		}
		if(!bondAtomPairs(aa.allatoms, atomPairs)) {
			System.out.println("av, N-terminal parring fejlede");
			printAtomBonds(aa);
		}
		bondSideChainAtoms(aa);
	}

	public static void bondCTerminusAtoms(AminoAcid aa) {
		String atomPairs[][] = null;
		switch (aa.type) {
		case PRO:
			atomPairs = PROcTerminalAtomPairs;
			break;
		default:
			atomPairs = cTerminalAtomPairs;
		}
		if(!bondAtomPairs(aa.allatoms, atomPairs)) {
			System.out.println("av, C-terminal parring fejlede");
			printAtomBonds(aa);
		}
		bondSideChainAtoms(aa);
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
		case ASN:
			atomPairs = ASNAtomPairs;
			break;
		case ASP:
			atomPairs = ASPAtomPairs;
			break;
		case CYS:
			atomPairs = CYSAtomPairs;
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
		case HIS:
			atomPairs = HISAtomPairs;
			break;
		case ILE:
			atomPairs = ILEAtomPairs;
			break;
		case LEU:
			atomPairs = LEUAtomPairs;
			break;
		case LYS:
			atomPairs = LYSAtomPairs;
			break;
		case MET:
			atomPairs = METAtomPairs;
			break;
		case PHE:
			atomPairs = PHEAtomPairs;
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
		case VAL:
			atomPairs = VALAtomPairs;
			break;
		default:
			System.out.println("Av, forkert aminosyre: ");
			printAtomBonds(aa);
			return;
		}
		if(!bondAtomPairs(aa.allatoms, atomPairs)) {
			System.out.print("Av, parring af sidekæde fejlede:\n  ");
			printAtomBonds(aa);
		}
	}

	private static boolean bondAtomPairs(Map<String,Atom> atoms, String pairs[][]) {
		for (String pair[] : pairs) {
			Atom a0 = atoms.get(pair[0]);
			Atom a1 = atoms.get(pair[1]);
			if(a0==null || a1==null) {
				System.out.println("a0:"+pair[0]+"  a1:"+pair[1]);
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

	private static class AtomComparator implements Comparator<Atom> {
		public int compare(Atom a1, Atom a2) {
			return a1.name.compareTo(a2.name);
		}
	}
	
	public static void printAtomBonds(AminoAcid aa) {
		AtomComparator comparator = new AtomComparator();

		String atomPairs = "private static String " + aa.type.name()
				+ "AtomPairs[][] = {";
		SortedSet<Atom> sortedAtoms = new TreeSet<Atom>(comparator);
		sortedAtoms.addAll(aa.allatoms.values());
		for (Atom a1 : sortedAtoms) {
			SortedSet<Atom> sortedAtoms2 = new TreeSet<Atom>(comparator);
			sortedAtoms2.addAll(aa.allatoms.values());
			for (Atom a2 : sortedAtoms2) {
				if (a1.name.compareTo(a2.name) < 0
						&& !isInPairList(a1, a2, backboneAtomPairs)) {
					double distance = a1.position.distance(a2.position);
					if (distance > 0
							&& distance < 1.9) {
						atomPairs += "{\"" + a1.name + "\",\"" + a2.name + "\"},";
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
