package edu.allatom;


import java.awt.Color;
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
	
	public static void bendProteinBackbone(Protein p, LinkedList<Atom>  trace, Renderer renderer) {
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
			aaIterator.previous();
			AminoAcid aaPrev = aaIterator.next();
			aa = aaIterator.next();
			traceCA = traceIterator.next();
			if (!(aaIterator.hasNext() || aaIterator.hasNext())) {
				break;
			}
			aaNext = aaIterator.next();
			traceCANext = traceIterator.next();
			aaIterator.previous();
			traceIterator.previous();

			ca = aa.getAtom("CA");
			Atom c = aa.getAtom("C");
			Atom n = aa.getAtom("N");
			caNext = aaNext.getAtom("CA");
			Atom nNext = aaNext.getAtom("N");
			Atom cPrev = aaPrev.getAtom("C");
			float bestPhi = 0.0f;
			float bestPsi = 0.0f;
			float bestDistance = Float.MAX_VALUE;

			for (int iter = 0; iter < 20; iter++) {
				// scramble
				Line psiRotationAxiss = new Line(ca.position,
						atomDistance(ca, c));
				m = TransformationMatrix3D.createRotation(psiRotationAxiss,
						(float) (Math.random() * Math.PI * 4));
				p.transformProtein(m, aaIterator.nextIndex() - 1,
						RotationType.PSI);
				Line phiRotationAxiss = new Line(ca.position,
						atomDistance(n, ca));
				m = TransformationMatrix3D.createRotation(phiRotationAxiss,
						(float) (Math.random() * Math.PI * 4));
				p.transformProtein(m, aaIterator.nextIndex() - 1,
						RotationType.PHI);
				
				for(int step=0; step<100; step++) {
					Line phiRotationAxis = new Line(ca.position, atomDistance(n, ca));
					Vector v1 = phiRotationAxis.projection(caNext.position);
					Vector v2 = phiRotationAxis.projection(traceCANext.position);
					Vector v = v1.plus(v2).times(.5f);
					float phiAngleDiff = v.vectorTo(traceCANext.position)
							.angleFull(v.vectorTo(caNext.position),
									atomDistance(n, ca));
					phiAngleDiff = (float) ((phiAngleDiff>0 ? 1 : -1) * 2*Math.PI/100);
					m = TransformationMatrix3D.createRotation(phiRotationAxis,
							phiAngleDiff);
					p.transformProtein(m, aaIterator.nextIndex() - 1,
							RotationType.PHI);
	
					Line psiRotationAxis = new Line(ca.position, atomDistance(ca, c));
					v1 = psiRotationAxis.projection(caNext.position);
					v2 = psiRotationAxis.projection(traceCANext.position);
					v = v1.plus(v2).times(.5f);
					float psiAngleDiff = v.vectorTo(traceCANext.position)
							.angleFull(v.vectorTo(caNext.position),
									atomDistance(ca, c));
					psiAngleDiff = (float) ((phiAngleDiff>0 ? 1 : -1) * 2*Math.PI/100);
					m = TransformationMatrix3D.createRotation(psiRotationAxis,
							psiAngleDiff);
					p.transformProtein(m, aaIterator.nextIndex() - 1,
							RotationType.PSI);
					
					System.out.println(": " + atomDistance(caNext, traceCANext).length());
				}

				float distance = atomDistance(caNext, traceCANext).length();
				System.out.println("distance candidate:" + distance);
				if (distance < bestDistance) {
					bestPhi = Vector.dihedralAngle(atomDistance(cPrev, n),
							atomDistance(n, ca), atomDistance(ca, c));
					bestPsi = Vector.dihedralAngle(atomDistance(n, ca),
							atomDistance(ca, c), atomDistance(c, nNext));
					bestDistance = distance;
				}
			}

			
//			 renderer.addToScene(ca.position, .4f, Color.YELLOW);
//			 renderer.addToScene(caNext.position, .4f, Color.MAGENTA);
//			 renderer.addToScene(c.position, .4f, Color.ORANGE);

			
			Line psiRotationAxiss = new Line(ca.position,
					atomDistance(ca, c));
			m = TransformationMatrix3D.createRotation(psiRotationAxiss,
					(float) (Math.random() * Math.PI * 4));
			p.transformProtein(m, aaIterator.nextIndex() - 1,
					RotationType.PSI);
			Line phiRotationAxiss = new Line(ca.position,
					atomDistance(n, ca));
			m = TransformationMatrix3D.createRotation(phiRotationAxiss,
					(float) (Math.random() * Math.PI * 4));
			p.transformProtein(m, aaIterator.nextIndex() - 1,
					RotationType.PHI);

			
			float phiDiff = bestPhi - Vector.dihedralAngle(atomDistance(cPrev, n),
					atomDistance(n, ca), atomDistance(ca, c));
			Line phiRotationAxis = new Line(ca.position, atomDistance(n, ca));
			m = TransformationMatrix3D.createRotation(phiRotationAxis, phiDiff);
			p.transformProtein(m, aaIterator.nextIndex() - 1, RotationType.PHI);

			float psiDiff = bestPsi - Vector.dihedralAngle(atomDistance(n, ca),
					atomDistance(ca, c), atomDistance(c, nNext));
			Line psiRotationAxis = new Line(ca.position, atomDistance(ca, c));
			m = TransformationMatrix3D.createRotation(psiRotationAxis,
					psiDiff);
			p.transformProtein(m, aaIterator.nextIndex() - 1, RotationType.PSI);
			float distance = atomDistance(caNext, traceCANext).length();
			System.out.println("new distance:" + distance+"/"+bestDistance);

//			break;
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
