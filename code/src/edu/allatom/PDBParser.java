package edu.allatom;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import edu.math.Vector;
import edu.geom3D.Sphere;
import edu.j3dScene.J3DScene;

public class PDBParser {
	
	static class AtomList {
		
		static class Atom {
			public final Type type;
			public final double x, y, z;
			public enum Type {
				H, C, N, O, S, UNKNOWN,
			}
			public Atom(Type type, double x, double y, double z) {
				this.type = type;
				this.x = x;
				this.y = y;
				this.z = z;
			}
		}
		
		private List<Atom> atoms = new LinkedList<Atom>();
		
		public AtomList() {
		}
		
		public void add(Atom atom) {
			atoms.add(atom);
		}
		
		public void render() {
			J3DScene scene = J3DScene.createJ3DSceneInFrame();
			for(AtomList.Atom a: atoms){
				Color c = Color.PINK;
				float size = .75f;
				switch(a.type) {
				case H: c = Color.GRAY.brighter(); size /= 2; break;
				case C: c = Color.DARK_GRAY; break;
				case N: c = Color.BLUE; break;
				case O: c = Color.RED; break;
				case S: c = Color.YELLOW; break;
				}
				scene.addShape(new Sphere(new Vector(a.x, a.y, a.z), size), c);
			}
			scene.setAxisEnabled(true);
			scene.centerCamera();
			scene.autoZoom();
		}
	}
	
	private AtomList atomList;
	
	public PDBParser() {
	}
	
	public void parseFile(String filename) throws FileNotFoundException, IOException {
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
		
		// make list of data for each atom
		List<String> atomsData = new LinkedList<String>();
		for(String line : firstModel.split("\n")) {
			if(line.startsWith("ATOM ")) {
				atomsData.add(line);
			}
		}
		
		// make the atom list
		atomList = new AtomList();
		for(String atomData : atomsData) {
			double x = Double.parseDouble(atomData.substring(31, 38));
			double y = Double.parseDouble(atomData.substring(39, 46));
			double z = Double.parseDouble(atomData.substring(47, 54));
			String typeString = atomData.substring(77, 78).trim();
			AtomList.Atom.Type type;
			if(typeString.equals("H")) {
				type = AtomList.Atom.Type.H;
			} else if(typeString.equals("C")) {
				type = AtomList.Atom.Type.C;
			} else if(typeString.equals("N")) {
				type = AtomList.Atom.Type.N;
			} else if(typeString.equals("O")) {
				type = AtomList.Atom.Type.O;
			} else if(typeString.equals("S")) {
				type = AtomList.Atom.Type.S;
			} else {
				System.err.println("Unknown atom type: " + typeString);
				type = AtomList.Atom.Type.UNKNOWN;
			}
			atomList.add(new AtomList.Atom(type, x, y, z));
		}
		
	}
	
	public void render() {
		atomList.render();
	}
	
	public AtomList getAtomList() {
		return atomList;
	}
	
	public static void main(String[] args) {
		PDBParser p = new PDBParser();
		try {
			p.parseFile("pdb/1UAO.pdb");
//			p.parseFile("pdb/2JOF.pdb");
			p.render();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
