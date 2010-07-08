package edu.allatom;


import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import edu.allatom.Protein.RotationType;
import edu.math.Line;
import edu.math.Matrix;
import edu.math.TransformationMatrix3D;
import edu.math.Vector;


public class BenderCCDWindow {
	
	private static final int WINDOW_SIZE = 8;
	private static final float DAMPING_FACTOR = .2f;
	private static final int WINDOW_REPETITIONS = 10;
	private static final int REPETITIONS = 3;
	
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
		for(int repetition=0; repetition<REPETITIONS; repetition++) {
		ListIterator<AminoAcid> aaIterator = p.aaSeq.listIterator();
		ListIterator<Atom> traceIterator = trace.listIterator();
		
		// align the first 2 CA's before starting iterative bending.
		// extract first amino acids 
		AminoAcid aa = aaIterator.next();
		AminoAcid aaNext = Util.iteratorPeek(aaIterator);
		Atom ca = aa.getAtom("CA");
		Atom caNext = aaNext.getAtom("CA");
		Atom tCA = traceIterator.next();
		Atom traceCANext = Util.iteratorPeek(traceIterator);
		// translate so starting CA coincides with the first CA of the trace
		Vector t = ca.vectorTo(tCA);// atomDistance(ca, traceCA);
		Matrix m = TransformationMatrix3D.createTranslation(t);
		p.transformProtein(m);
		// rotate so the second CA is as close as possible to the second CA of
		// the trace
		Vector normal = tCA.vectorTo(traceCANext).cross(
				tCA.vectorTo(caNext)).normIn();
		float angle = -tCA.vectorTo(traceCANext).angle(tCA.vectorTo(caNext));
		
		m = TransformationMatrix3D.createRotation(
				new Vector(tCA.position), normal, angle);
		p.transformProtein(m);
		
//		aaIterator.next();
//		traceIterator.next();
		// loop over the residues and bend
		while(aaIterator.nextIndex()<p.aaSeq.size()
				&& traceIterator.nextIndex()<trace.size()) {
//			System.out.println("aaIterator.nextIndex() "+aaIterator.nextIndex());
			// extract residues from window
			List<AminoAcid> backboneAA = new ArrayList<AminoAcid>();
			List<Atom> traceCA = new ArrayList<Atom>();
			int lookaheadWindow = 0;
			while(lookaheadWindow<WINDOW_SIZE
					&& aaIterator.hasNext() && traceIterator.hasNext()) {
				backboneAA.add(aaIterator.next());
				traceCA.add(traceIterator.next());
				lookaheadWindow++;
			}
			// hurrah for Java-iteratorer
			while (lookaheadWindow>1) {
				aaIterator.previous();
				traceIterator.previous();
				 lookaheadWindow--;
			}
			
			// index of current residue offset
			int aaIndex = aaIterator.nextIndex() - 1;
			
			// adjust phi and psi of residues in window
			// TODO use a distance threshold instead of fixed loop limit?
			for(int step=0; step<WINDOW_REPETITIONS; step++) {
//				System.out.println(p.aaSeq.size()-aaIndex-1);
				for(int windowOffset=0; 
						windowOffset<Math.min(WINDOW_SIZE-1, p.aaSeq.size()-aaIndex-1); 
						windowOffset++) {
//					System.out.println("  windowOffset: "+(windowOffset));
					aa = backboneAA.get(windowOffset);
					ca = aa.getAtom("CA");
					Atom c = aa.getAtom("C");
					Atom n = aa.getAtom("N");
					List<AminoAcid> lookaheadBackboneWindow =
						backboneAA.subList(windowOffset+1, backboneAA.size());
					List<Atom> lookaheadTraceWindow =
						traceCA.subList(windowOffset+1, traceCA.size());

					boolean debug = step==9 && aaIndex==1 && windowOffset==0;

					// find optimal phi rotation angle
					Line phiRotationAxis = new Line(new Vector(ca.position), n.vectorTo(ca));
					float phiAngleDiff = angleDiff(phiRotationAxis, lookaheadBackboneWindow, 
							lookaheadTraceWindow, renderer, false);
					p.rotate(phiAngleDiff, aaIndex+windowOffset, RotationType.PHI);

					// find optimal psi rotation angle
					Line psiRotationAxis = new Line(new Vector(ca.position), ca.vectorTo(c));
					float psiAngleDiff = angleDiff(psiRotationAxis, lookaheadBackboneWindow, 
							lookaheadTraceWindow, renderer, false);
					p.rotate(psiAngleDiff, aaIndex+windowOffset, RotationType.PSI);				
					if(debug) {
						renderer.addToScene(new Vector(ca.position), 0.4f, Color.MAGENTA);
						renderer.addToScene(new Vector(c.position), 0.4f, Color.MAGENTA);
//						return;
					}
				}
			}
//			return;
		}
		int collisionsBeforeElimination = 0;
		for(AminoAcid aaa : p.aaSeq) {
			if(aaa.collides(p) != null) {
				collisionsBeforeElimination++;
			}
		}
		int collisionsAfterElimination = tryToEliminateCollisions(p);
		int actualCollisionsAfterElimination = 0;
		for(AminoAcid aaa : p.aaSeq) {
			if(aaa.collides(p) != null) {
				actualCollisionsAfterElimination++;
			}
		}
		int collisionsAfterMoreElimination = tryToEliminateCollisions(p);
		int actualCollisionsAfterMoreElimination = 0;
		for(AminoAcid aaa : p.aaSeq) {
			if(aaa.collides(p) != null) {
				actualCollisionsAfterMoreElimination++;
			}
		}
		System.out.println("Collisions initial/elimination/more: "
//				+ initialCollisions + "/" + collisions + "/"
				+ collisionsBeforeElimination + "/"
				+ collisionsAfterElimination + "(" + actualCollisionsAfterElimination + ")/"
				+ collisionsAfterMoreElimination + "(" + actualCollisionsAfterMoreElimination + ")");
		System.out.println(p.cATraceRMSD(trace));
	}
	}

	private static float angleDiff(Line rotationAxis,
			List<AminoAcid> backboneAA, List<Atom> traceCA, Renderer renderer, boolean debug) {
		float angleDiff = 0;
		int positiveAngles = 0;
		int negativeAngles = 0;
		float positiveAngleDiff = 0;
		float negativeAngleDiff = 0;
		for(int l=0; l<backboneAA.size(); l++) {
//			System.out.println("woop: "+l);
			Vector caNext = new Vector(backboneAA.get(l).getAtom("CA").position);
			Vector traceCANext = new Vector(traceCA.get(l).position);

			Vector caNextRotationCenter =
					rotationAxis.projection(caNext);
			Vector traceCANextRotationCenter =
					rotationAxis.projection(traceCANext);
			// find optimal phi rotation angle
			float thisAngle = traceCANextRotationCenter.vectorTo(
					traceCANext).angleFull(caNextRotationCenter.
							vectorTo(caNext), rotationAxis.getDirection());
			if(Math.abs(thisAngle)>Math.PI/2.) {
				positiveAngleDiff += thisAngle;
				positiveAngles++;
			} else {
				negativeAngleDiff += thisAngle;	
				negativeAngles++;
			}
//			if(thisAngle>0) {
//				if(debug) {
//				System.out.println("old vinkel:" + thisAngle);
//				thisAngle = (float) (-2*Math.PI - thisAngle);
//				} else
//				thisAngle = (float) (-Math.PI-Math.abs(thisAngle));
//				if(debug)
//				System.out.println("new vinkel:" + thisAngle);
//			}
//			angleDiff += thisAngle;
			if(debug) {
				renderer.addToScene(caNext, 0.4f, Color.YELLOW);
				renderer.addToScene(traceCANext, 0.4f, Color.RED);
				renderer.addToScene(caNextRotationCenter, 0.4f, Color.ORANGE);
				renderer.addToScene(traceCANextRotationCenter, 0.4f, Color.PINK);
				System.out.println("lokal vinkel:" + thisAngle);
			}
		}
		if(positiveAngles>0)
		positiveAngleDiff /= positiveAngles;
		if(negativeAngles>0)
		negativeAngleDiff /= negativeAngles;
		angleDiff = positiveAngleDiff + negativeAngleDiff;
		if(positiveAngles>0 && negativeAngles>0)
			angleDiff /= 2.;
		angleDiff *= DAMPING_FACTOR;
//		angleDiff /= backboneAA.size(); 
		if(debug)
			System.out.println("vinkel:" + angleDiff);
		return angleDiff;
	}

	/**
	 * Tries to push the sidechains around to eliminate collisions.
	 * This resets the rotamers used by the amino acids.
	 */
	public static int tryToEliminateCollisions(Protein p) {
		int collisionsLeft = 0;
		for(AminoAcid aa : p.aaSeq) {
			aa.resetUsedRotamers();
		}
		int i=0;
		for(AminoAcid aa : p.aaSeq) {
			i++;
			if(aa.collides(p) != null) {
				if(aa.nextCollisionlessRotamer(p)) {
					continue;
				}
			} else {
				continue;
			}
			List<AminoAcid> previousCollidees = new LinkedList<AminoAcid>();
			outer: while(aa.collides(p) != null) {
				AminoAcid collidee = aa.collides(p);
				if(collidee == null) {
					break;
				}
				if(previousCollidees.contains(collidee)) {
					collisionsLeft++;
					break;
				}
				previousCollidees.add(collidee);
				aa.resetUsedRotamers();
				while(aa.collides(p) == collidee) {
					if(!collidee.nextCollisionlessRotamer(p)) {
						if(!aa.nextRotamer()) {
							collisionsLeft++;
							break outer;
						}
					}
				}
			}
		}
		return collisionsLeft;
	}
}
