package edu.allatom;

import java.io.File;
import edu.math.Vector;
import edu.math.Plane;
import edu.math.Point;

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
			float sigma = stddev(entry.getValue());
			averages.put(entry.getKey(), mu);
			System.out.println(entry.getKey() + " average: " + mu);
			System.out.println(entry.getKey() + " stddev: " + sigma + " \n");
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


	public static float stddev(List<Float> list) {
		float mu = average(list);
		List<Float> l = new LinkedList<Float>();
		for(float v : list) {
			l.add((float)Math.pow((v - mu),2.0));
		}
		return (float)Math.sqrt(average(l));
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

	private static float distance(Map<String, Float> bondlengths, Point a, Point b, String name) {
        float dist = a.distance(b);
        bondlengths.put(name, dist);
        return dist;
	}


	/* Returns angle A in radians */
	public static float cos_relation(float a, float b, float c) {
		return (float)Math.acos((Math.pow(b, 2) + Math.pow(c, 2) - Math.pow(a,2))/ (2*b*c));
	}

	private static float angle(Map<String, Float> bondlengths, float dist_a, float dist_b, float dist_c, String name) {
		if(dist_a < Float.POSITIVE_INFINITY && dist_b < Float.POSITIVE_INFINITY && dist_c < Float.POSITIVE_INFINITY) {
			float angle = cos_relation(dist_a, dist_b, dist_c);
            angle = (float) (angle/Math.PI) * 180f;
			if(Float.isNaN(angle)) {
			   return Float.POSITIVE_INFINITY;
			} else {
				bondlengths.put(name, angle);
				return angle;
			}
		}
		return Float.POSITIVE_INFINITY;
	}	

    //If you work with a plane defined by point and normal
    public static Point ClosestPointOnPlane(Plane plane, Point point)
    {
        float distance = plane.getNorm().dot(new Vector(point).minus(new Vector(plane.getP())));
        return (new Vector(plane.getNorm().times(distance))).minus(new Vector(point));
    }


	public static Map<String, Float> computeBackboneBondLengths (AminoAcid aa) {
		HashMap<String, Float> bondlengths = new HashMap<String, Float>();

		Atom H = aa.getAtom("H");
		Atom N = aa.getAtom("N");
		Atom CA = aa.getAtom("CA");
        Atom CB = aa.getAtom("CB");
		Atom HA = aa.getAtom("HA");
		Atom C = aa.getAtom("C");
		Atom O = aa.getAtom("O");
		Atom next_N = null;
		Atom next_CA = null;
        Atom next_H = null;
		if(C != null) {
			next_N = C.followBond("N");
			if(next_N != null) {
				next_CA = next_N.followBond("CA");
                next_H = next_N.followBond("H");
            }
		}

		/* Compute bond lengths */
		float N_CA = distance(bondlengths, N, CA, "N-CA");
		float CA_C = distance(bondlengths, CA, C, "CA-C");
		float CA_CB = distance(bondlengths, CA, CB, "CA-CB");
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
        
        if(next_N != null && next_CA != null && C != null && next_H != null) {
            Plane planeH = new Plane(new Vector(next_N.position), next_N.vectorTo(next_CA).cross(next_N.vectorTo(C)));
            Point projH = planeH.projectOnto(new Vector (next_H.position));
            float distHPlaneH = distance(bondlengths, projH, next_H.position, "distHPlaneH");
            float CtoprojH = distance(bondlengths, C.position, projH, "C..projH");
            float NtoprojH = distance(bondlengths, next_N.position, projH, "N..projH");
                    float C_N_projH = angle(bondlengths, CtoprojH, C_N, NtoprojH, "projHangle");
        }

        if(N != null && CA != null && C != null && HA != null) {
            Plane planeHA = new Plane(new Vector(CA.position), CA.vectorTo(N).cross(CA.vectorTo(C)));
            Point projHA = planeHA.projectOnto(new Vector (HA.position)); 
            float distHAPlaneHA = distance(bondlengths, projHA, HA.position, "distHAPlaneHA");
            float NtoprojHA = distance(bondlengths, N.position, projHA, "N..projHA");
            float CAtoprojHA = distance(bondlengths, CA.position, projHA, "CA..projHA");
            float N_CA_projHA = angle(bondlengths, NtoprojHA, N_CA, CAtoprojHA, "projHAangle");
        }

        if(N != null && CA != null && C != null && CB != null) {
            Plane planeCB = new Plane(new Vector(CA.position), CA.vectorTo(N).cross(CA.vectorTo(C)));
            Point projCB = planeCB.projectOnto(new Vector (CB.position));
            float distCBPlaneCB = distance(bondlengths, projCB, CB.position, "distCBPlaneCB");
            float NtoprojCB = distance(bondlengths, N.position, projCB, "N..projCB");
            float CAtoprojCB = distance(bondlengths, CA.position, projCB, "CA..projCB");
            float CB_CA_projCB = angle(bondlengths, distCBPlaneCB, CA_CB, CAtoprojCB, "CB_CA_projCB");
            float N_CA_projCB = angle(bondlengths, NtoprojCB, N_CA, CAtoprojCB, "N_CA_projCB");
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
