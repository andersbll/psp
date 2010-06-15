package edu.allatom;

import java.io.File;

import java.util.*;

public class StructureStats {


	public static void main(String[] args) {

		Protein p;
		File path = new File("./pdb/hash");
		
		List<Protein> proteins = new LinkedList<Protein>();
		for(File f : path.listFiles()) {
			System.out.println("PROTEIN:: " + f.toString());
			try {
				p = PDBParser.parseFile(f.toString());
			} catch(Exception e) {
				e.printStackTrace();
				return;
			}
			
			Bonder.bondAtoms(p);
			
			proteins.add(p);
		}

		averages(computeBackboneBondLengths(proteins));
	}

	public static Map<String, Float> averages(Map<String, List<Float>> values) {
		HashMap<String, Float> averages = new HashMap<String, Float>();
		for(Map.Entry<String, List<Float>> entry : values.entrySet()) {
			float average = average(entry.getValue());
			averages.put(entry.getKey(), average);
			System.out.println(entry.getKey() + ": " + average + " Å \n");
		}
		return averages;
	}

	public static float average(List<Float> list) {
		float sum = 0;
		for(float v : list)
			sum += v;

		float average = sum/list.size();
		return average;
	}

	public static Map<String, List<Float>> computeBackboneBondLengths (Protein p) {
		HashMap<String, List<Float>> bondlengths = new HashMap<String, List<Float>>();
		for(AminoAcid aa : p.aaSeq) {
			Map<String, Float> blengths = computeBackboneBondLengths(aa);

			for(Map.Entry<String, Float> entry : blengths.entrySet()) {
				List<Float> lengths = bondlengths.get(entry.getKey());
				if (lengths == null) {
					lengths = new LinkedList<Float>();
					bondlengths.put(entry.getKey(), lengths);
				}
				lengths.add(entry.getValue());
				
			}
		}

		return bondlengths;
	}

	public static Map<String, Float> computeBackboneBondLengths (AminoAcid aa) {
		HashMap<String, Float> bondlengths = new HashMap<String, Float>();
		Atom N = aa.getAtom("N");
		Atom CA = aa.getAtom("CA");
		Atom HA = aa.getAtom("HA");
		Atom C = aa.getAtom("C");
		Atom O = aa.getAtom("O");
		Atom next_N = C.followBond("N");

		bondlengths.put("N-CA", N.distanceTo(CA));
		bondlengths.put("CA-C", CA.distanceTo(C));
		bondlengths.put("C-O", C.distanceTo(O));

		// Not all hydrogen atoms are specified
		if(HA != null)
			bondlengths.put("CA-HA", CA.distanceTo(HA));

		// Last amino acid has no next
		if(next_N != null)
			bondlengths.put("C-N", C.distanceTo(next_N));
		

		if(aa.type != AminoAcidType.PRO) {
			Atom H = aa.getAtom("H");
			if(H != null)
				bondlengths.put("N-H", N.distanceTo(H));
		}

		return bondlengths;
	}


	public static Map<String, List<Float>> computeBackboneBondLengths (List<Protein> proteins) {
		HashMap<String, List<Float>> bondlengths = new HashMap<String, List<Float>>();
		
		for(Protein p : proteins) {
			Map<String, List<Float>> blengths = computeBackboneBondLengths(p);

			for(Map.Entry<String, List<Float>> entry : blengths.entrySet()) {
				List<Float> lengths = bondlengths.get(entry.getKey());
				lengths.addAll(entry.getValue());
			}
		}
		return bondlengths;
	}
}
