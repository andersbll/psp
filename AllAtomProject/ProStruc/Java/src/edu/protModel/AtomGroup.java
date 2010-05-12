package edu.protModel;

import java.util.ArrayList;
import java.util.List;

import edu.math.Matrix;
import edu.math.Vector;

/** 
 * A group of atoms in a protein. Responsibility is to create and hold 
 * internal position of atoms and to hold global positions of atoms.
 */
public class AtomGroup {
	public static final int N_TERMINAL = 0;
	public static final int C_TERMINAL = 1;
	public static final int CN_GROUP = 2;
	public static final int CA_GROUP = 3;

	public final int type;
	public List<Atom> atoms = new ArrayList<Atom>();

	public AtomGroup(int type){
		this.type = type;
		createInternalAtoms();
	}

	
	/** 
	 * Update the global position of atoms using the transform matrix. 
	 * Applies transform to posInGroup and the result is posInProtein.
	 */
	public void updateAtomPositions(Matrix transform){
		for(Atom a: atoms){
			a.posInProtein = transform.applyToIn(a.posInGroup.clone());
		}
	}
	
	
	/** 
	 * Create atoms in this group and place them correctly .. that is 
	 * set each atoms posInGroup. This is only done once upon creation
	 */
	protected void createInternalAtoms(){
		switch(type){
		case N_TERMINAL: createN_TERMINALAtoms();break;
		case C_TERMINAL: createC_TERMINALAtoms();break;
		case CN_GROUP: createCN_GROUPAtoms();break;
		case CA_GROUP: createCA_GROUPAtoms();break;
		default: throw new Error("Undefined type "+type);
		}
	}
	protected void createN_TERMINALAtoms(){
		atoms.add(new Atom(Atom.N, new Vector(0,0,0)));
	}
	protected void createC_TERMINALAtoms(){
		atoms.add(new Atom(Atom.CP, new Vector(0,0,0)));
	}
	protected void createCN_GROUPAtoms(){
		double CNDist = 1.32;
		atoms.add(new Atom(Atom.CP, new Vector(0,0,0)));
		atoms.add(new Atom(Atom.O, new Vector(0,+1.24,0)));
		atoms.add(new Atom(Atom.N, new Vector(CNDist*Math.cos(Math.PI/6),-CNDist*Math.sin(Math.PI/6),0)));
	}
	protected void createCA_GROUPAtoms(){
		atoms.add(new Atom(Atom.CA, new Vector(0,0,0)));
	}
}
