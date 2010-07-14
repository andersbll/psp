package edu.allatom;

public class Rotamer implements Comparable {
	double probability;
	double chis[] = new double[4];
	
	public String toString() {
		return "Rotamer (p " + probability + ", phi " + chis[0] + ", " +
			chis[1] + ", " + chis[2] + ", " + chis[3] + ")";
	}
	
	public int compareTo(Object o) {
		Rotamer r = (Rotamer)o;
		if(r.probability > this.probability) return 1;
		else return -1;
	}
}
