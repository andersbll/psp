package edu.allatom;

import java.util.*;

/**
 *  Baad asss!
 */
public class Bonder {
	
	public static int VERBOSITY_NONE = 0;
	public static int VERBOSITY_ERRORS = 1;
	public static int VERBOSITY_WARNINGS = 2;
	public static int VERBOSITY_INFO = 3;

	public static int VERBOSITY = VERBOSITY_NONE;
	
	public final static String backboneAtomPairs[][] = {{"C","CA"},{"C","O"},{"N","CA"},{"H","N"}};
	public final static String PRObackboneAtomPairs[][] = {{"C","CA"},{"C","O"},{"N","CA"}};

	public final static String nTerminalAtomPairs[][] = {{"C","CA"},{"C","O"},{"N","CA"},{"H2","N"},{"H3","N"},{"N","H1"}};

	public final static String cTerminalAtomPairs[][] = {{"C","CA"},{"C","O"},{"N","CA"},{"H","N"},{"C","OXT"}};
	public final static String PROcTerminalAtomPairs[][] = {{"C","CA"},{"C","O"},{"N","CA"},{"C","OXT"}};
	
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
	
	public static void bondAtoms(List<AminoAcid> acids) {
		AminoAcid firstaa = acids.get(0);
		
		AminoAcid oldaa = null;
		for (AminoAcid aa : acids) {
			bondBackboneAtoms(aa);
			bondSideChainAtoms(aa);
			if(oldaa!=null) {
				bondAtoms(oldaa.allatoms.get("C"), aa.allatoms.get("N"));
			}
			oldaa = aa;
		}
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
		bondAtomPairs(aa.allatoms, atomPairs);
        if(VERBOSITY >= VERBOSITY_WARNINGS) {
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
		bondAtomPairs(aa.allatoms, atomPairs);
        if(VERBOSITY >= VERBOSITY_WARNINGS) {
            System.out.println("av, N-terminal parring fejlede");
            printAtomBonds(aa);
        }
		bondSideChainAtoms(aa);
	}

	public static void bondCTerminusAtoms(AminoAcid aa) {
		String atomPairs[][] = null;
		atomPairs = cTerminalAtomPairs;
		if(aa.type == AminoAcidType.PRO) {
			atomPairs = PROcTerminalAtomPairs;
		}

		bondAtomPairs(aa.allatoms, atomPairs);
        if(VERBOSITY >= VERBOSITY_WARNINGS) {
            System.out.println("av, C-terminal parring fejlede");
            printAtomBonds(aa);
        }
		bondSideChainAtoms(aa);
	}
	
	public static void bondSideChainAtoms(AminoAcid aa) {
		String atomPairs[][] = aa.type.atomBonds;
		if(atomPairs == null) {
			if(VERBOSITY >= VERBOSITY_WARNINGS) {
				System.out.println("Av, forkert aminosyre: ");
				printAtomBonds(aa);
			}
		}
		bondAtomPairs(aa.allatoms, atomPairs);
        if(VERBOSITY >= VERBOSITY_WARNINGS) {
            System.out.print("Av, parring af sidekæde fejlede:\n  ");
            printAtomBonds(aa);
        }
	}

	private static void bondAtomPairs(Map<String,Atom> atoms, String pairs[][]) {
		for (String pair[] : pairs) {
			Atom a0 = atoms.get(pair[0]);
			Atom a1 = atoms.get(pair[1]);
			if(a0==null || a1==null) {
				if(VERBOSITY >= VERBOSITY_WARNINGS) {
					System.out.print("parringsfejl mellem "+pair[0]+" og  "+pair[1]+":");
					if(a0==null) {
						System.out.print(" "+ pair[0]+" findes ikke");
					}
					if(a1==null) {
						System.out.print(" "+ pair[1]+" findes ikke");
					}
					System.out.println(" ");
				}
				continue;
			}
			bondAtoms(a0,a1);
		}
	}
	
	public static void bondAtoms(Atom a1, Atom a2) {
		a1.bondsTo.put(a2.label, a2);
		a2.bondsTo.put(a1.label, a1);
	}
	
	public static void printAtomBonds(Protein p) {
		for (AminoAcid aa : p.aaSeq.subList(1, p.aaSeq.size() - 1)) {
			printAtomBonds(aa);
		}
	}

	private static class AtomComparator implements Comparator<Atom> {
		public int compare(Atom a1, Atom a2) {
			return a1.label.compareTo(a2.label);
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
				if (a1.label.compareTo(a2.label) < 0
						&& !isInPairList(a1, a2, backboneAtomPairs)) {
					double distance = a1.position.distance(a2.position);
					if (distance > 0
							&& distance < 1.9) {
						atomPairs += "{\"" + a1.label + "\",\"" + a2.label + "\"},";
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
