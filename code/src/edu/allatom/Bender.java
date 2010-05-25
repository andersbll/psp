package edu.allatom;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import edu.allatom.Protein.RotationType;
import edu.math.Line;
import edu.math.Matrix;
import edu.math.TransformationMatrix3D;
import edu.math.Vector;

public class Bender {
	
	public static void bendProteinBackbone(Protein p, LinkedList<Atom>  trace) {
		ListIterator<AminoAcid> aaIterator = p.aaSeq.listIterator();
		ListIterator<Atom> traceIterator = trace.listIterator();
		
		AminoAcid aa = aaIterator.next();
		AminoAcid aaNext = aaIterator.next();
		Atom ca = aa.getAtom("CA");
		Atom caNext = aaNext.getAtom("CA");
		Atom traceCA = traceIterator.next();
		Atom traceCANext = traceIterator.next();
		aaIterator.previous(); //oops, went too far - lulz @ dybber
		traceIterator.previous();
		
		// translation
		Vector t = atomDistance(ca, traceCA);
		Matrix m = TransformationMatrix3D.createTranslation(t);
		p.transformProtein(m);

		// rotation
		Vector normal = atomPlaneNormal(traceCA, traceCANext, caNext);
		float angle = -atomAngle(traceCA, traceCANext, caNext);
		m = TransformationMatrix3D.createRotation(traceCA.position, normal, angle);
		p.transformProtein(m);
				
		while(true) {
			System.out.println("hej");
			aa = aaIterator.next();
			if(!(aaIterator.hasNext() || aaIterator.hasNext())) {
				break;
			}
			aaNext = aaIterator.next();
			aaIterator.previous();
			traceCANext = traceIterator.next();
			
			ca = aa.getAtom("CA");
			Atom c = aa.getAtom("C");
			Atom n = aa.getAtom("N");
			caNext = aaNext.getAtom("CA");
			for(int i=0;i<1;i++) {
				Line phiRotationAxis = new Line(ca.position, atomDistance(n, ca));
				Vector v1 = phiRotationAxis.projection(caNext.position);
				Vector v2 = phiRotationAxis.projection(traceCANext.position);
				Vector v = v1.plus(v2).times(.5f);
				
				float phiAngleDiff = v.vectorTo(v1).angleFull(v.vectorTo(v2), atomDistance(n, ca));
				m = TransformationMatrix3D.createRotation(phiRotationAxis, phiAngleDiff);
				p.transformProtein(m,aaIterator.nextIndex()-1,RotationType.PHI);
				
				Line psiRotationAxis = new Line(ca.position, atomDistance(ca, c));
				v1 = psiRotationAxis.projection(caNext.position);
				v2 = psiRotationAxis.projection(traceCANext.position);
				v = v1.plus(v2).times(.5f);
				
				float psiAngleDiff = v.vectorTo(v1).angleFull(v.vectorTo(v2), atomDistance(ca, c));
				System.out.println(v.vectorTo(v2));
				System.out.println(psiAngleDiff+" "+v.vectorTo(v1)+" "+v.vectorTo(v2)+"\n");
				m = TransformationMatrix3D.createRotation(psiRotationAxis, psiAngleDiff);
				p.transformProtein(m,aaIterator.nextIndex()-1,RotationType.PSI);
			}
			break;
		}
	}
	

	private static float atomAngle(Atom a0, Atom a1, Atom a2) {
		Vector v0 = atomDistance(a0, a1);
		Vector v1 = atomDistance(a0, a2);
		return v0.angle(v1);
	}


	private static Vector atomPlaneNormal(Atom a0, Atom a1, Atom a2) {
		Vector v0 = atomDistance(a0, a1);
		Vector v1 = atomDistance(a0, a2);
		return v0.cross(v1).normIn();
	}

	private static Vector atomDistance(Atom a0, Atom a1) {
		return a0.position.vectorTo(a1.position);		
	}

	private static Vector atomProjectPointDistance(Atom a0, Atom a1) {
		return a0.position.vectorTo(a1.position);		
	}

//	private static void bendResidue(AminoAcid aa, CAlphaTrace.CAlpha cAlpha) {
//		//rotation
////		Vector n = atomPlaneNormal(caFirst, cAlphaFirst.c, cAlphaFirst.next.c);
////		float angle = atomAngle(caFirst, cAlphaFirst.c, cAlphaFirst.next.c);
////		Matrix m1 = TransformationMatrix3D.createRotation(0,n);
////		Matrix m = m0.applyTo(m1);
//	}

}
