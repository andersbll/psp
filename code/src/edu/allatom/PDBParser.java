package edu.allatom;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.allatom.AminoAcid.Type;
import edu.geom3D.Sphere;
import edu.math.Matrix;
import edu.math.Vector;
import edu.math.TransformationMatrix3D;
import edu.math.Vector;


public class PDBParser {
	
	public static Protein parseFile(String filename) throws FileNotFoundException, IOException {
		// read file
		StringBuilder sb = new StringBuilder();
		FileReader fr = new FileReader(filename);
		int c;
		while((c = fr.read()) != -1) {
			sb.append((char) c);
		}
		String data = sb.toString();
		
		// get first model data
		int firstModelStart = data.indexOf("\nMODEL ");
		int secondModelStart = data.indexOf("\nMODEL ", firstModelStart + 1);
		if (firstModelStart ==-1 ) {
			// if no MODEL exist, just begin from first atom
			firstModelStart = data.indexOf("\nATOM ");
			secondModelStart = data.length();
		}
		String firstModel = data.substring(firstModelStart, secondModelStart);

		List<String> atomLines = new LinkedList<String>();
		for(String line : firstModel.split("\n")) {
			if(line.startsWith("ATOM ")) {
				atomLines.add(line);
			}
		}

		int aaSeqNo = -1;
		List<AminoAcid> aaSeq = new LinkedList<AminoAcid>();
		AminoAcid aminoAcid = null;
		for(String line : atomLines) {
			int currentAASeqNo = getResSeqNo(line);
			if(aaSeqNo != currentAASeqNo) {
				aaSeqNo = currentAASeqNo;
				if(aminoAcid != null) {
					aaSeq.add(aminoAcid);
				}
				AminoAcid.Type aaType = AminoAcid.Type.valueOf(getAAName(line));
				aminoAcid = new AminoAcid(aaType);

			}
			aminoAcid.addAtom(parseAtom(line));
		}
		if(aminoAcid != null) {
			aaSeq.add(aminoAcid);
		}
		return new Protein(aaSeq);
	}

	private static int getResSeqNo(String s) {
		return Integer.parseInt(s.substring(23, 26).trim());
	}
	private static String getAAName(String s) {
		return s.substring(17, 20).trim();
	}
	
	private static Vector getPosition(String s) {
		float x = Float.parseFloat(s.substring(31, 38));
		float y = Float.parseFloat(s.substring(39, 46));
		float z = Float.parseFloat(s.substring(47, 54));
		return new Vector(x,y,z);
	}
	
	public static Atom parseAtom(String atomLine) {
		Vector position = getPosition(atomLine);
		String typeString = atomLine.substring(77, 78).trim();
		String name = atomLine.substring(13, 16).trim();
		Atom.Type type = Atom.Type.fromName(typeString);
		return new Atom(type, name, position);
	}
	
	public static void main(String[] args) {
		Protein p;
		try {
//			p = parseFile("pdb/1UAO.pdb"); // meget lille
//			p = parseFile("pdb/2JOF.pdb"); // lille
			p = parseFile("pdb/2KQ6.pdb"); // 78
//			p = parseFile("pdb/2WU9.pdb"); // grande
		} catch(Exception e) {
			e.printStackTrace();
			return;
		}
		Bonder.bondAtoms(p);
		
//		Statistics.dumpRamachandranPNGPlot(
//				p, "/Users/sben/Datalogi/allatom/ramachandran.png");
//		Statistics.dumpRamachandranSVGPlot(
//				p, "/Users/sben/Datalogi/allatom/ramachandran.svg");
//		if(2 > 1)
//			return;
		Renderer renderer = new Renderer();
//		renderer.render(p);
		

//		for(AminoAcid aa : p.aaSeq.subList(1, p.aaSeq.size()-2)) {
//			aa.calculatePsi();
//			aa.calculatePhi();
//			System.out.println("");
//		}
		
		LinkedList<Atom> trace = CAlphaTrace.CAlphaTrace(p);
		renderer.addToScene(trace);
		
		Vector v = new Vector(3.1, 2.2, 1.3);
		Matrix m1 = TransformationMatrix3D.createTranslation(v);
		Matrix m2 = TransformationMatrix3D.createRotation((float) Math.PI, v.normIn());
		Matrix m = m1.applyTo(m2);
		p.transformProtein(m);
		
		Bender.bendProteinBackbone(p, trace, renderer);
//		neatSidechainStatisticsStuff(renderer, p);

//		m = TransformationMatrix3D.createTranslation(new Vector(5, 0.1, 0.1));
//		p.transformProtein(m);

		renderer.addToScene(p);
		renderer.addShape(new Sphere(new Vector(0,0,0),.4f));
		renderer.render();
	}
	
	private static void neatSidechainStatisticsStuff(Renderer renderer, Protein p) {
		AminoAcid.Type type = Type.GLY;
		
		List<AminoAcid> instances = AminoAcid.getAminoAcidsOfType(p, type);
		System.out.println(instances.size() + " instances of " + type.name() + " found");
		
//		List<List<Atom>> sidechains = new LinkedList<List<Atom>>();
//		for(AminoAcid aa : instances) {
//			List<Atom> sidechain = new LinkedList<Atom>();
//			sidechain.addAll(aa.allatoms.values());
//			sidechains.add(sidechain);
//		}
//		List<Atom> averageAA = AminoAcid.getAverageSidechain(type, sidechains);
//		AminoAcid aa = new AminoAcid(type, averageAA);
		AminoAcid aa = instances.get(0);
		List<Atom> atoms = new LinkedList<Atom>();
		atoms.addAll(aa.allatoms.values());
//		AminoAcid.resetSidechainPosition(aa.type, atoms);
//		AminoAcid.resetSidechainChiAngles(aa.type, atoms);
		AminoAcid.resetSidechain(aa.type, atoms);
		List<AminoAcid> aaList = new LinkedList<AminoAcid>();
		aaList.add(aa);
		renderer.addToScene(new Protein(aaList));
		
		System.out.println("ugly code:\n" + AminoAcid.sidechainJavaRepresentation(atoms));
	}
	
}
