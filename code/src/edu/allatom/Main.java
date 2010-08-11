package edu.allatom;

import java.awt.Color;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Collection;
import java.util.List;

import edu.allatom.AminoAcidType;

import edu.geom3D.Sphere;
import edu.math.*;


public class Main {

	public static void main(String[] args) {
		Renderer renderer = new Renderer();

		String pdbfile;
		pdbfile = "pdb/1UAO.pdb"; // meget lille
		pdbfile = "pdb/1CSY.pdb"; // meget lille
		pdbfile = "pdbs_large/1ENW.pdb"; // meget lille
//		pdbfile = "scwrl_out/scwrl_1ENW.pdb"; // meget lille
//		pdbfile = "pdb/2JOF.pdb"; // lille
//		pdbfile = "pdb/2KQ6.pdb"; // 78 amino acids
//		pdbfile = "pdb/2WU9.pdb"; // grande
//		pdbfile = "pdb/16PK.pdb"; // grande
		
//		pdbfile = "pdb/1CTF_3.6.pdb"; // rasmus
//		pdbfile = "pdb/2CRO_2.9.pdb"; // rasmus
//		pdbfile = "pdb/2CRO_6.2.pdb"; // rasmus
		
		Protein p;
		try {
			p = PDBParser.parseFile(pdbfile);
		} catch(Exception e) {
			e.printStackTrace();
			return;
		}
		
		try {
			RotamerLibrary.loadDunbrach("pdb/bbind02.May.lib");
		} catch (IOException e) {
			System.out.println("Dunbrach library file not found!");
			return;
		}

//		Bender.bendRotamers(p);
		int initialCollisions= Bender.countCollisions(p);
		System.out.println("Initial collisions: "+initialCollisions);
		renderProtein(renderer,p);
//		neatSidechainStatisticsStuff(renderer, p);
//		List<AminoAcidType> typeTrace = AminoAcid.makeTypeTrace(p.aaSeq);
//		LinkedList<Atom> trace = p.getCAlphaTrace();
//////		renderUncoiledProtein(renderer,typeTrace);
//		rendAndBend(renderer, Protein.getUncoiledProtein(typeTrace), trace);

//		renderer.addToScene(trace);
//		renderProtein(renderer, p);
//		renderCollisions(renderer, p);
		
//		
////		bendAndRamaPlot(Protein.getUncoiledProtein(typeTrace), trace);
		
//		LinkedList<String> aLotOfStuffToBend = new LinkedList<String>() {
//			private static final long serialVersionUID = 2933079052992091488L;
//			{
//				add("pdb/1UAO.pdb"); // meget lille
//				add("pdb/2JOF.pdb"); // lille
//				add("pdb/2KQ6.pdb"); // 78 amino acids
//	//			add("pdb/2WU9.pdb"); // grande
//				add("pdb/1CTF_3.6.pdb"); // rasmus
//				add("pdb/2CRO_2.9.pdb"); // rasmus
//				add("pdb/2CRO_6.2.pdb"); // rasmus
//			}
//		};
//		doALotOfBending(aLotOfStuffToBend);
	}

	private static void renderProtein(Renderer renderer, Protein p) {
		Bonder.bondAtoms(p);
		renderer.addToScene(p);
		renderer.render();
		renderCollisions(renderer, p);
	}

	private static void renderCollisions(Renderer renderer, Protein p) {
		for(AminoAcid aa : p.aaSeq) {
			Collection<AminoAcid> collisions = aa.collides(p);
			if(!collisions.isEmpty()) {
				for(Atom a : aa.allatoms.values()) {
					for(AminoAcid collidee : collisions) {
						for(Atom b : collidee.allatoms.values()) {
							if(a.collides(b)) {
								renderer.addShape(new Sphere(new Vector(new Vector(a.position)
								                                        .plus(b.position)).times(0.5f), 0.5f), Color.PINK);
							}
						}
					}
				}
			}
		}
	}
	
	private static void bendAndRamaPlot(Protein p, LinkedList<Atom> trace) {
		// Statistics.dumpRamachandranPNGPlot(p, "ramachandran.png");
		Bender.bendProteinBackbone(p, trace, null);
		Statistics.dumpRamachandranSVGPlot(p, "ramachandran.svg");
	}
	private static void rendAndBend(Renderer renderer, Protein p, LinkedList<Atom> trace) {
//		Bonder.bondAtoms(p);

		// Currently the trace is exactly on top of the protein.
		// So we rotate and push the protein around to make it a problem
		renderer.addToScene(trace);
		renderer.addToScene(p);
		Bender.bendRotamers(p);
//		Bender.bendProteinBackbone(p, trace, renderer);
		System.out.println(p.cATraceRMSD(trace));
		Statistics.dumpRamachandranSVGPlot(p, "ramachandran.svg");
		renderCollisions(renderer, p);
		renderer.render();
//		Statistics.dumpRamachandranSVGPlot(p, "ramachandran.svg");
	}
	
	private static void renderUncoiledProtein(Renderer renderer, List<AminoAcidType> typeTrace) {
//		List<AminoAcidType> aminoAcidTypes = new LinkedList<AminoAcidType>();
//		aminoAcidTypes.add(AminoAcidType.VAL);
//		aminoAcidTypes.add(AminoAcidType.GLU);
//		aminoAcidTypes.add(AminoAcidType.LYS);
//		aminoAcidTypes.add(AminoAcidType.GLN);
//		aminoAcidTypes.add(AminoAcidType.ARG);
//		aminoAcidTypes.add(AminoAcidType.GLU);
//		aminoAcidTypes.add(AminoAcidType.GLU);
//		aminoAcidTypes.add(AminoAcidType.GLU);
//		aminoAcidTypes.add(AminoAcidType.GLU);
//		aminoAcidTypes.add(AminoAcidType.GLU);
//		aminoAcidTypes.add(AminoAcidType.GLU);
		renderer.addToScene(Protein.getUncoiledProtein(typeTrace));
	}

	private static void neatSidechainStatisticsStuff(Renderer renderer, Protein p) {
//		AminoAcidType type = AminoAcidType.LYS;
		for(AminoAcidType type : AminoAcidType.values()) {
//		AminoAcidType type = AminoAcidType.CYS; {
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
	
	private static void doALotOfBending(List<String> filenames) {
		for(String filename : filenames) {
			long startTime = System.nanoTime();
			Protein p = null;
			try {
				p = PDBParser.parseFile(filename);
			} catch(Exception e) {
				System.err.println("Error reading/parsing file: " + filename);
				continue;
			}
			try {
				List<AminoAcidType> typeTrace = AminoAcid.makeTypeTrace(p.aaSeq);
				LinkedList<Atom> trace = p.getCAlphaTrace();
				System.out.println("PDB file: " + filename + " (" + trace.size() + " acids):");
				Protein protein = Protein.getUncoiledProtein(typeTrace);
				Bender.bendProteinBackbone(protein, trace, null);
				System.out.println("Minimum RMSD: " + (float)protein.minRMSD(trace)
						+ " (" + (float)protein.cATraceRMSD(trace) + ")");
			} catch(Exception e) {
				System.err.println("Error bending protein: " + filename);
				e.printStackTrace();
				continue;
			}
			System.out.println("Total time: " + (System.nanoTime()-startTime)/1000000 + " ms");
			System.out.println();
		}
	}
	
}