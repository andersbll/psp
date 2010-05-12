package edu.protModel;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import edu.geom3D.Sphere;
import edu.j3dScene.J3DScene;
import edu.math.Matrix;

/** 
 * Representation of the structure of the backbone of a protein chain using dihedral angles 
 * phi and psi. The structure is represented using a list of atom-groups that each hold both 
 * internal coordinates (their own coordinate frame) and global coordinates (in the global 
 * coordinate system (1,0,0),(0,1,0),(0,0,1). The global coordinates are calculated based on 
 * a list of transformation matrices. Each transformation moves an atom-coordinate from its 
 * internal coordinate frame to the <it>preceeding</it> coordinate frame. To move from 
 * coordinate frame r to the origin you have to apply all the preceeding tranformations 
 * to r.
 */
public class PhiPsiBackboneStructure extends ProteinStructure{
	public List<AtomGroup> atomGroups = new ArrayList<AtomGroup>();
	public List<NextTransform> transforms = new ArrayList<NextTransform>();

	
	/** Construct a backbone structure from the specified sequence. Since side-chains 
	 * are not added yet the sequence itself is not used, only its length.
	 * @param sequence
	 */
	public PhiPsiBackboneStructure(char[] sequence){
		createAtomGroups(sequence);
		createTransforms();
		updateGlobalCoordinates();
	}
	
	/** 
	 * Change the phi-angle of amino acid r
	 * @param r
	 * @param phi
	 */
	public void setPhi(int r, double phi){
		transforms.get(2*r).setTorsion(phi);
		updateGlobalCoordinates();
	}
	/** 
	 * Change the psi-angle of amino acid r
	 * @param r
	 * @param psi
	 */
	public void setPsi(int r, double psi){
		transforms.get(2*r+1).setTorsion(psi);
		updateGlobalCoordinates();
	}
	/**
	 * Change both phi and psi-angles of amino acid r
	 * @param r
	 * @param phi
	 * @param psi
	 */
	public void setPhiPsi(int r, double phi, double psi){
		transforms.get(2*r).setTorsion(phi);
		transforms.get(2*r+1).setTorsion(psi);
		updateGlobalCoordinates();
	}
	
	/** After changing a transform or internal coordinates in an atom-group, the 
	 * global coordinates needs to be updated. This method recalculates ALL coordinates 
	 * even though only those after the change needs to be recalculated.
	 * TODO: Optimize
	 */
	protected void updateGlobalCoordinates(){
		Matrix m = Matrix.createIdentityMatrix(4);
		for(int g = 0;g<atomGroups.size();g++){
			atomGroups.get(g).updateAtomPositions(m);
			m.applyToIn(transforms.get(g).transform);
		}
	}
	
	/** Use the sequence to create atom-groups. Uses only the length of sequence.
	 * @param sequence
	 */
	protected void createAtomGroups(char[] sequence){
		atomGroups.add(new AtomGroup(AtomGroup.N_TERMINAL));
		atomGroups.add(new AtomGroup(AtomGroup.CA_GROUP));
		for(int r=1;r<sequence.length;r++){
			atomGroups.add(new AtomGroup(AtomGroup.CN_GROUP));
			atomGroups.add(new AtomGroup(AtomGroup.CA_GROUP));
		}
		atomGroups.add(new AtomGroup(AtomGroup.C_TERMINAL));
	}
	
	/** Create a list of transforms. Assumes that createAtomGroups has been called
	 * previously.
	 */
	protected void createTransforms(){
		for(AtomGroup ag: atomGroups){
			transforms.add(new NextTransform(ag.type, 0));
		}
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for(AtomGroup ag: atomGroups){
			for(Atom a: ag.atoms){
				sb.append(a.type+" "+a.posInProtein+"\n");
			}
		}
		return sb.toString();
	}
	
	/** Display the current structure in a frame using the J3DScene viewer. 
	 * Assumes java3d is installed */
	public void display(){
		J3DScene scene = J3DScene.createJ3DSceneInFrame();
		for(AtomGroup ag: atomGroups){
			for(Atom a: ag.atoms){
				Color c = Color.GRAY;
				if(a.type == Atom.N) c = Color.BLUE;
				if(a.type == Atom.O) c = Color.RED;
				scene.addShape(new Sphere(a.posInProtein,0.75f), c);
			}
		}
		scene.setAxisEnabled(true);
		scene.centerCamera();
		scene.autoZoom();
	}
	
	
	
	public static void main(String[] args){
		//Construct a protein structure. The contents of the sequence doesnt matter as long as side-chains
		//are not modeled.
		PhiPsiBackboneStructure struc = new PhiPsiBackboneStructure("IIIIIIIIIIII".toCharArray());
		
		//Generate a helix. Look up a Ramachandran plot
		for(int r=0;r<11;r++)
			struc.setPhiPsi(r, -Math.PI/2, -Math.PI/3);
		
		//Display it
		struc.display();
	}
}
