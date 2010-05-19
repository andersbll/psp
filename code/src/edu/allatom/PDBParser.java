package edu.allatom;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import edu.math.Matrix;
import edu.math.Point;
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
				AminoAcid.Type aaType = AminoAcid.Type.fromName(getAAName(line));
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
	
	private static Point getPosition(String s) {
		float x = Float.parseFloat(s.substring(31, 38));
		float y = Float.parseFloat(s.substring(39, 46));
		float z = Float.parseFloat(s.substring(47, 54));
		return new Point(x,y,z);
	}
	
	public static Atom parseAtom(String atomLine) {
		Point position = getPosition(atomLine);
		String typeString = atomLine.substring(77, 78).trim();
		String name = atomLine.substring(13, 16).trim();
		Atom.Type type = Atom.Type.fromName(typeString);
		return new Atom(type, name, position);
	}
	
	public static void main(String[] args) {
		Protein p;
		try {
			p = parseFile("pdb/1UAO.pdb");
//			p = parseFile("pdb/2JOF.pdb");
		} catch(Exception e) {
			e.printStackTrace();
			return;
		}
		Bonder.bondAtoms(p);

		Renderer renderer = new Renderer();
		renderer.render(p);

//		for(AminoAcid aa : p.aaSeq.subList(1, p.aaSeq.size()-2)) {
//			aa.calculatePsi();
//			aa.calculatePhi();
//			System.out.println("");
//		}
		
		CAlphaTrace trace = new CAlphaTrace(p);
		
		Vector v = new Vector(3.1, 2.2, 1.3);
		Matrix m1 = TransformationMatrix3D.createTranslation(v);
		Matrix m2 = TransformationMatrix3D.createRotation((float) Math.PI, v.normIn());
		Matrix m = m1.applyTo(m2);
		p.transformProtein(m);
		
		Bender.bendProteinBackbone(p, trace);

//		m = TransformationMatrix3D.createTranslation(new Vector(5, 0.1, 0.1));
//		p.transformProtein(m);

		renderer.addAndRender(p);
	}
	
}
