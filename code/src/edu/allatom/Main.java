package edu.allatom;

import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

import edu.allatom.AminoAcidType;

import edu.geom3D.Sphere;
import edu.math.Matrix;
import edu.math.Vector;
import edu.math.TransformationMatrix3D;



public class Main {

	public static void main(String[] args) {
		Renderer renderer = new Renderer();

		String pdbfile;
//		pdbfile = "pdb/1UAO.pdb"; // meget lille
		pdbfile = "pdb/2JOF.pdb"; // lille
//		pdbfile = "pdb/2KQ6.pdb"; // 78 amino acids
//		pdbfile = "pdb/2WU9.pdb"; // grande
		
		Protein p;
		try {
			p = PDBParser.parseFile(pdbfile);
		} catch(Exception e) {
			e.printStackTrace();
			return;
		}

//		renderProtein(renderer, p);
		neatSidechainStatisticsStuff(renderer, p);
//		renderUncoiledProtein(renderer);

		renderer.render();

		// Statistics.dumpRamachandranPNGPlot(p, "ramachandran.png");
		// Statistics.dumpRamachandranSVGPlot(p, "ramachandran.svg");
	}
	
	private static void renderProtein(Renderer renderer, Protein p) {
		Bonder.bondAtoms(p);
		
		LinkedList<Atom> trace = CAlphaTrace.CAlphaTrace(p);

		// Currently the trace is exactly on top of the protein.
		// So we rotate and push the protein around to make it a problem
		Vector v = new Vector(3.1, 2.2, 1.3);
		Matrix m1 = TransformationMatrix3D.createTranslation(v);
		Matrix m2 = TransformationMatrix3D.createRotation((float) Math.PI, v.normIn());
		Matrix m = m1.applyTo(m2);

		p.transformProtein(m);
		renderer.addToScene(trace);
		renderer.addToScene(p);
		Bender.bendProteinBackbone(p, trace, renderer);
	}
	
	private static void renderUncoiledProtein(Renderer renderer) {
		List<AminoAcidType> aminoAcidTypes = new LinkedList<AminoAcidType>();
		aminoAcidTypes.add(AminoAcidType.ALA);
		aminoAcidTypes.add(AminoAcidType.GLY);
		aminoAcidTypes.add(AminoAcidType.GLU);
		aminoAcidTypes.add(AminoAcidType.GLU);
		aminoAcidTypes.add(AminoAcidType.GLU);
//		aminoAcidTypes.add(AminoAcidType.GLU);
//		aminoAcidTypes.add(AminoAcidType.GLU);
//		aminoAcidTypes.add(AminoAcidType.GLU);
//		aminoAcidTypes.add(AminoAcidType.GLU);
//		aminoAcidTypes.add(AminoAcidType.GLU);
//		aminoAcidTypes.add(AminoAcidType.GLU);
		renderer.addToScene(Protein.getUncoiledProtein(aminoAcidTypes));
	}

	private static void neatSidechainStatisticsStuff(Renderer renderer, Protein p) {
//		AminoAcidType type = AminoAcidType.LYS;
		for(AminoAcidType type : AminoAcidType.values()) {
		
			List<AminoAcid> instances = AminoAcid.getAminoAcidsOfType(p, type);
			System.out.println(instances.size() + " instances of " + type.name() + " found");
			if(instances.size() == 0) {
				continue;
			}
			
			List<List<Atom>> sidechains = new LinkedList<List<Atom>>();
			for(AminoAcid aa : instances) {
				List<Atom> sidechain = new LinkedList<Atom>();
				sidechain.addAll(aa.allatoms.values());
				AminoAcid.resetSidechain(type, sidechain);
				sidechains.add(sidechain);
			}
			List<Atom> averageAA = AminoAcid.getAverageSidechain(type, sidechains);
			AminoAcid aa = new AminoAcid(type, averageAA);
	//		AminoAcid aa = instances.get(4);
			List<Atom> atoms = new LinkedList<Atom>();
			atoms.addAll(aa.allatoms.values());
	//		AminoAcid.resetSidechainPosition(aa.type, atoms);
	//		AminoAcid.resetSidechainChiAngles(aa.type, atoms);
	//		AminoAcid.resetSidechain(aa.type, atoms);
			List<AminoAcid> aaList = new LinkedList<AminoAcid>();
			aaList.add(aa);
			renderer.addToScene(new Protein(aaList));
	//		
	//		aa = instances.get(1);
	//		atoms = new LinkedList<Atom>();
	//		atoms.addAll(aa.allatoms.values());
	////		AminoAcid.resetSidechainPosition(aa.type, atoms);
	////		AminoAcid.resetSidechainChiAngles(aa.type, atoms);
	//		AminoAcid.resetSidechain(aa.type, atoms);
	//		aaList = new LinkedList<AminoAcid>();
	//		aaList.add(aa);
	//		renderer.addToScene(new Protein(aaList));
			
	//		List<List<Atom>> atomsList = new LinkedList<List<Atom>>();
	//		atomsList.add(atoms);
	//		List<Atom> averageAtom = AminoAcid.getAverageSidechain(type, atomsList);
	//		List<AminoAcid> averageAcidList = new LinkedList<AminoAcid>();
	//		averageAcidList.add(new AminoAcid(type, averageAtom));
	//		renderer.addToScene(new Protein(averageAcidList));
			
			System.out.println("ugly code:\n" + AminoAcid.sidechainJavaRepresentation(atoms));
		}
	}
}