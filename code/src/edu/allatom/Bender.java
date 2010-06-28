package edu.allatom;


import java.awt.Color;
import java.util.LinkedList;
import java.util.ListIterator;

import edu.allatom.Protein.RotationType;
import edu.math.Line;
import edu.math.Matrix;
import edu.math.TransformationMatrix3D;
import edu.math.Vector;


public class Bender {
	
	/**
	 * Bend a protein to try to match a given c_alpha trace.
	 * 
	 * TODO Remove renderer from argument list
	 * 
	 * @param p Protein to bend
	 * @param trace Trace to try to match
	 * @param renderer Debug renderer
	 */
	public static void bendProteinBackbone(
				Protein p, LinkedList<Atom> trace, Renderer renderer) {
		ListIterator<AminoAcid> aaIterator = p.aaSeq.listIterator();
		ListIterator<Atom> traceIterator = trace.listIterator();
		
		// align the first 2 CA's before starting iterative bending.
		// extract first amino acids 
		AminoAcid aa = aaIterator.next();
		AminoAcid aaNext = Util.iteratorPeek(aaIterator);
		Atom ca = aa.getAtom("CA");
		Atom caNext = aaNext.getAtom("CA");
		Atom traceCA = traceIterator.next();
		Atom traceCANext = Util.iteratorPeek(traceIterator);
		// translate so starting CA coincides with the first CA of the trace
		Vector t = ca.vectorTo(traceCA);// atomDistance(ca, traceCA);
		Matrix m = TransformationMatrix3D.createTranslation(t);
		p.transformProtein(m);
		// rotate so the second CA is as close as possible to the second CA of
		// the trace
		Vector normal = traceCA.vectorTo(traceCANext).cross(
				traceCA.vectorTo(caNext)).normIn();
		float angle = -traceCA.vectorTo(traceCANext).angle(traceCA.vectorTo(caNext));
		
		m = TransformationMatrix3D.createRotation(
				new Vector(traceCA.position), normal, angle);
		p.transformProtein(m);
		
		// bend the remaining acids
		while(true) {
//		for(int woop=0;woop<6;woop++) {
			// extract next amino acid and trace atom
			aa = aaIterator.next();
			traceCA = traceIterator.next();
			if(!(aaIterator.hasNext() && traceIterator.hasNext())) {
				// we're done
				break;
			}
			aaNext = Util.iteratorPeek(aaIterator);
			traceCANext = Util.iteratorPeek(traceIterator);
			int aaIndex = aaIterator.nextIndex() - 1;
			
			// extract atoms from this amino acid
			ca = aa.getAtom("CA");
			Atom c = aa.getAtom("C");
			Atom n = aa.getAtom("N");
			caNext = aaNext.getAtom("CA");

			// set phi and psi to more realistic values in the hope that
			// it will give better convergence
//			p.rotate(+(float)(2/3.*Math.PI), aaIndex, RotationType.PHI);
//			p.rotate(-(float)(2/3.*Math.PI), aaIndex, RotationType.PSI);
//			System.out.println("phi:"+aa.phi()+" psi:"+aa.psi());

			// adjust phi and psi iteratively
			// TODO use some threshold instead of fixed loop limit?
			for(int step=0; step<10; step++) {
				// find points for phi rotation

				{
				// find points for psi rotation
				Line psiRotationAxis = new Line(new Vector(ca.position), ca.vectorTo(c));
				Vector caNextRotationCenter =
						psiRotationAxis.projection(new Vector(caNext.position));
				Vector traceCANextRotationCenter =
						psiRotationAxis.projection(new Vector(traceCANext.position));
				// find optimal phi rotation angle
				float psiAngleDiff = traceCANextRotationCenter.vectorTo(
						traceCANext.position).angleFull(caNextRotationCenter.
						vectorTo(caNext.position), ca.vectorTo(c));
				// perform phi rotation
				p.rotate(psiAngleDiff, aaIndex, RotationType.PSI);
//				if(woop==5 && step==9) {
//					renderer.addToScene(new Vector(ca.position), 0.4f, Color.YELLOW);
//					renderer.addToScene(new Vector(traceCA.position), 0.4f, Color.RED);
//					renderer.addToScene(traceCANextRotationCenter, 0.4f, Color.GREEN);
//					renderer.addToScene(caNextRotationCenter, 0.4f, Color.GREEN);
//					System.out.println("vinkel:" + psiAngleDiff);
//					break;
//				}
				}
				{
					Line phiRotationAxis = new Line(new Vector(ca.position), n.vectorTo(ca));
					Vector caNextRotationCenter =
							phiRotationAxis.projection(new Vector(caNext.position));
					Vector traceCANextRotationCenter =
							phiRotationAxis.projection(new Vector(traceCANext.position));
					// find optimal psi rotation angle
					float phiAngleDiff = traceCANextRotationCenter.vectorTo(
							traceCANext.position).angleFull(caNextRotationCenter.
							vectorTo(caNext.position), n.vectorTo(ca));
					// perform phi rotation
					p.rotate(phiAngleDiff, aaIndex, RotationType.PHI);
					}

			}
		}

//		// DEBUG visualize rotation before rotating
//		double caNextOldDistance = caNext.position.distance(traceCANext.position);
//		float psiAngleDiff2 = v.vectorTo(traceCANext.position)
//				.angleFull(v.vectorTo(caNext.position),
//				atomDistance(ca, c));
//		if(aaIterator.nextIndex() == 8 && step == 0) {
//			psiAngleDiff = v2.vectorTo(traceCANext.position)
//					.angleFull(v1.vectorTo(caNext.position),
//					atomDistance(ca, c));
//			renderer.addToScene(v, .4f, Color.YELLOW);
//			renderer.addToScene(traceCANext.position, .4f, Color.ORANGE.darker());
//			renderer.addToScene(caNext.position, .4f, Color.PINK);
//			renderer.addToScene(ca.position, .4f, Color.GREEN);
//			renderer.addToScene(n.position, .4f, Color.GREEN);
////			renderer.addToScene(caNext.position, .4f, Color.PINK);
//			System.out.println("tried turning " + psiAngleDiff);// +
////					"(" + psiAngleDiff2 + ")");
//			break;
//		}
		
//		// DEBUG visualize rotation after rotating
//		double caDistance = caNext.position.distance(traceCANext.position);
//		System.out.println("psi: distance reduced from " + caNextOldDistance +
//				" to " + caDistance);
//			if(aaIterator.nextIndex() == 6 && step == 0) {
//				renderer.addToScene(caNext.position, .4f, Color.PINK);
//				renderer.addToScene(traceCANext.position, .4f, Color.ORANGE.darker());
//				System.out.println("tried turning " + theta);
//				break;
//			}

	}


//	private static float atomAngle(Atom a0, Atom a1, Atom a2) {
//		Vector v0 = a0.vectorTo(a1);
//		Vector v1 = a0.vectorTo(a2);
//		return v0.angle(v1);
//	}


//	private static Vector atomPlaneNormal(Atom a0, Atom a1, Atom a2) {
//		Vector v0 = a0.vectorTo(a1);
//		Vector v1 = a0.vectorTo(a2);
//		return v0.cross(v1).normIn();
//	}
	
//	private static void bendResidue(AminoAcid aa, CAlphaTrace.CAlpha cAlpha) {
//		//rotation
////		Vector n = atomPlaneNormal(caFirst, cAlphaFirst.c, cAlphaFirst.next.c);
////		float angle = atomAngle(caFirst, cAlphaFirst.c, cAlphaFirst.next.c);
////		Matrix m1 = TransformationMatrix3D.createRotation(0,n);
////		Matrix m = m0.applyTo(m1);
//	}

}
