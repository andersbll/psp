package edu.protModel;

import edu.math.Vector;

/** 
 * Representation of an atom in an atom-group. Contains two positions: One that is 
 * internal to the group and one that is global to the protein. This class' responsibility
 * is mainly to hold values. 
 */
public class Atom {
	/*CA, CP, N and O are backbones. The rest are sidechain types */
	public static final int CA = 0;
	public static final int CP = 1;
	public static final int N = 2;
	public static final int O = 3;
	
	/* Create types for each side chain atom as well. */
	public static final int C1 = 4;
	public static final int C2 = 5;
	public static final int C3 = 6;
	public static final int C4 = 7;
	// ...
	
	

	public int type;
	
	/** Position of atom in atom-group. Only changes if group contains side-chain */
	public Vector posInGroup;
	
	/** Position of atom in protein. May change if any preceeding atom-group changes */
	public Vector posInProtein;
	
	/** Constructor. Initialize type and internal position. */
	public Atom(int type, Vector pos){
		this.type = type;
		this.posInGroup = pos;
	}
	
}
