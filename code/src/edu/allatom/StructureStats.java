package edu.allatom;

import java.io.File;

import java.util.*;

public class StructureStats {

	public static void main(String[] args) {

		Protein p;
		File path = new File("./pdb/hash");
		
		HashMap<String, List<Float>> bondlengths = new HashMap<String, List<Float>>();
		
		for(File f : path.listFiles()) {
			System.out.println("PROTEIN:: " + f.toString());
			try {
				p = PDBParser.parseFile(f.toString());
				Bonder.bondAtoms(p);			
				Map<String, List<Float>> blengths = computeBackboneBondLengths(p);

				for(Map.Entry<String, List<Float>> entry : blengths.entrySet()) {
					List<Float> lengths = bondlengths.get(entry.getKey());
					if (lengths == null) {
						lengths = new LinkedList<Float>();
						bondlengths.put(entry.getKey(), lengths);
					}
					lengths.addAll(entry.getValue());
				}
				
			} catch(Exception e) {
				e.printStackTrace();
				return;
			}
		}
		averages(bondlengths);
	}

	public static Map<String, Float> averages(Map<String, List<Float>> values) {
		HashMap<String, Float> averages = new HashMap<String, Float>();
		for(Map.Entry<String, List<Float>> entry : values.entrySet()) {
			float mu = average(entry.getValue());
			float sigma = variance(entry.getValue());
			averages.put(entry.getKey(), mu);
			System.out.println(entry.getKey() + " average: " + mu);
			System.out.println(entry.getKey() + " variance: " + sigma + " \n");
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


	public static float variance(List<Float> list) {
		float mu = average(list);
		List<Float> l = new LinkedList<Float>();
		for(float v : list) {
			l.add((float)Math.pow((v - mu),2.0));
		}
		return average(l);
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

	private static float distance(Map<String, Float> bondlengths, Atom a, Atom b, String name) {
		if(a != null && b != null) {
			float dist = a.distanceTo(b);
			bondlengths.put(name, dist);
			return dist;
		}
		
		return Float.POSITIVE_INFINITY;
	}

	/* Returns angle A in radians */
	public static float cos_relation(float a, float b, float c) {
		return (float)Math.acos((Math.pow(b, 2) + Math.pow(c, 2) - Math.pow(a,2))/ (2*b*c));
	}

	private static float angle(Map<String, Float> bondlengths, float dist_a, float dist_b, float dist_c, String name) {
		if(dist_a < Float.POSITIVE_INFINITY && dist_b < Float.POSITIVE_INFINITY && dist_c < Float.POSITIVE_INFINITY) {
			float angle = cos_relation(dist_a, dist_b, dist_c);
			if(Float.isNaN(angle)) {
			   return Float.POSITIVE_INFINITY;
			} else {
				bondlengths.put(name, angle);
				return angle;
			}
		}
		return Float.POSITIVE_INFINITY;
	}	

	public static Map<String, Float> computeBackboneBondLengths (AminoAcid aa) {
		HashMap<String, Float> bondlengths = new HashMap<String, Float>();

		Atom H = aa.getAtom("H");
		Atom N = aa.getAtom("N");
		Atom CA = aa.getAtom("CA");
		Atom HA = aa.getAtom("HA");
		Atom C = aa.getAtom("C");
		Atom O = aa.getAtom("O");
		Atom next_N = null;
		Atom next_CA = null;
		if(C != null) {
			next_N = C.followBond("N");
			if(next_N != null)
				next_CA = next_N.followBond("CA");
		}

		/* Compute bond lengths */
		float N_CA = distance(bondlengths, N, CA, "N-CA");
		float CA_C = distance(bondlengths, CA, C, "CA-C");
		float C_O = distance(bondlengths, C, O, "C-O");
		float CA_HA = distance(bondlengths, CA, HA, "CA_HA");
		float C_N = distance(bondlengths, C, next_N, "C_N");
		float N_H = distance(bondlengths, N, H, "N-H");
		
		/* These are not existing bonds, but the distances are used
		 * for computing bond angles. 
		 */
		float NtoC = distance(bondlengths, N, C, "N..C");
		float CtoCA = distance(bondlengths, C, next_CA, "C..CA");
		float CAtoO = distance(bondlengths, CA, O, "CA..O");
		float HtoCA = distance(bondlengths, H, CA, "H..CA");
		float CAtoN = distance(bondlengths, CA, N, "CA..N");
		float CtoH = distance(bondlengths, C, H, "C..H");

		/* Compute bondangles*/
		float H_N_CA = angle(bondlengths, HtoCA, N_H, N_CA, "H-N-CA");
		float N_CA_C = angle(bondlengths, NtoC, N_CA, C_N, "N-CA-C");
		float CA_C_O = angle(bondlengths, CAtoO, CA_C, C_O, "CA-C-O");
		float CA_C_N = angle(bondlengths, CAtoN, CA_C, C_N, "CA-C-N");
		float C_N_H = angle(bondlengths, CtoH, C_N, N_H, "C-N-H");
		float C_N_CA = angle(bondlengths, CtoCA, C_N, N_CA, "C-N-CA");

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
