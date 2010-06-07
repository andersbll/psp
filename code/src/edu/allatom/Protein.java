package edu.allatom;

import java.awt.Image;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import edu.math.Line;
import edu.math.Matrix;
import edu.math.TransformationMatrix3D;
import edu.math.Vector;

public class Protein {
	
	public enum RotationType { PHI, PSI, OMEGA };
	
	List<AminoAcid> aaSeq;
	
	public Protein(List<AminoAcid> aaSeq) {
		this.aaSeq = aaSeq;
	}

	public List<Atom> getAtoms() {
		List<Atom> atoms = new ArrayList<Atom>();
		for(AminoAcid aa : aaSeq) {
			atoms.addAll(aa.getAtoms());
		}
		return atoms;
	}
	
	public void transformProtein(Matrix m) {
		for(Atom a : getAtoms()) {
			Vector v = new Vector(a.position);
			a.position = m.applyToIn(v);
		}
	}

	public void transformProtein(Matrix m, int aaIdx, RotationType type) {
		int i = 0;
		for(AminoAcid aa : aaSeq) {
			if(i < aaIdx) {
				;
			} else if(i == aaIdx) {
				switch(type) {
				case PHI:
					//TODO rotér ikke alle atomer!
					for(Atom a : aa.getAtoms()) {
						Vector v = new Vector(a.position);
						a.position = m.applyToIn(v);
					}
					break;
				case PSI:
					Atom a = aa.getAtom("O");
					Vector v = new Vector(a.position);
					a.position = m.applyToIn(v);
//					for(Atom a : aa.getAtoms()) {
////						if(!(a.name.equals("CA") || a.name.equals("C") || a.name.equals("O"))) {
//							Vector v = new Vector(a.position);
//							a.position = m.applyToIn(v);
////						}
//					}					
					break;
				case OMEGA:
					//TODO
					break;
				}
			} else {
				for(Atom a : aa.getAtoms()) {
					Vector v = new Vector(a.position);
					a.position = m.applyToIn(v);
//					if(a.position.x()!=a.position.x()) {
//						System.out.println("noooo  "+a.name+"  "+aa.type.name()+"   "+m);
//					}
				}
			}
			i++;
		}
	}
	
	public double cATraceRMSD(LinkedList<Atom> trace) {
		double d = 0;
		for(int i=0; i<aaSeq.size(); i++) {
			Atom cA = aaSeq.get(i).getAtom("CA");
			Atom cAT = trace.get(i);
			double dx = cA.position.x() - cAT.position.x();
			double dy = cA.position.y() - cAT.position.y();
			double dz = cA.position.z() - cAT.position.z();
			d += dx*dx + dy*dy + dz*dz;
		}
		d /= aaSeq.size();
		d = Math.sqrt(d);
		return d;
	}
	
	public void rotate(float angle, int aaIndex, RotationType rotationType) {
		AminoAcid aa = aaSeq.get(aaIndex);
		Atom ca = aa.getAtom("CA");
		
		switch(rotationType) {
		case PHI: {
			Atom n = aa.getAtom("N");
			Line rotationAxis = new Line(ca.position, n.vectorTo(ca));
			Matrix rotation = TransformationMatrix3D.createRotation(
					rotationAxis, angle);
			transformProtein(rotation, aaIndex, RotationType.PHI);
			break;
		} case PSI: {
			Atom c = aa.getAtom("C");
			Line rotationAxis = new Line(ca.position, ca.vectorTo(c));
			Matrix rotation = TransformationMatrix3D.createRotation(
					rotationAxis, angle);
			transformProtein(rotation, aaIndex, RotationType.PSI);
			break;
		} case OMEGA: {
			throw new NotImplementedException();
		}}
	}
	
}
