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
	
	private static final int WINDOW_SIZE = 10;
	private static final float DAMPING_FACTOR = 1f;
	private static final int WINDOW_REPETITIONS = 1;
	private static final int REPETITIONS = 50;
	
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
		
		// loop over the residues and bend
		while(aaIterator.nextIndex()<p.aaSeq.size()
				&& traceIterator.nextIndex()<trace.size()) {
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
					Line phiRotationAxis = new Line(new Vector(n.position), ca.vectorTo(n));
					float phiAngleDiff = angleFromCCD(phiRotationAxis, lookaheadBackboneWindow, 
							lookaheadTraceWindow, renderer, false);
					p.rotate(phiAngleDiff, aaIndex+windowOffset, RotationType.PHI);

					// find optimal psi rotation angle
					Line psiRotationAxis = new Line(new Vector(ca.position), c.vectorTo(ca));
					float psiAngleDiff = angleFromCCD(psiRotationAxis, lookaheadBackboneWindow, 
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

	private static float angleFromCCD(Line rotationAxis,
			List<AminoAcid> backboneAA, List<Atom> traceCA, Renderer renderer, boolean debug) {
		int lookahead = backboneAA.size();
		
		float[] r_mags = new float[lookahead];
		Vector[] r_units = new Vector[lookahead];
		Vector[] fs = new Vector[lookahead];
		Vector[] s_units = new Vector[lookahead];

		// collect vectors
		for(int l=0; l<lookahead; l++) {
			Vector M = new Vector(backboneAA.get(l).getAtom("CA").position);
			Vector F = new Vector(traceCA.get(l).position);
			Vector O = rotationAxis.projection(M);

			Vector r = O.vectorTo(M);
			Vector r_hat = r.norm();
			float r_mag = r.length();
			Vector f = O.vectorTo(F);
			Vector s_hat = r_hat.cross(rotationAxis.getDirection());

			r_mags[l] = r_mag;
			r_units[l] = r_hat;
			fs[l] = f;
			s_units[l] = s_hat;
		}
		
		// calculate angle
		float tan_alpha_denom = 0;
		float tan_alpha_num = 0;
		for(int i=0; i<lookahead; i++) {
			Vector f = fs[i];
			Vector s_unit = s_units[i];
			float r_mag = r_mags[i];
			Vector r_unit = r_units[i];
			tan_alpha_num += f.dot(s_unit)*r_mag;
			tan_alpha_denom += f.dot(r_unit)*r_mag;	
		}
		float tan_alpha = tan_alpha_num/tan_alpha_denom;
		float alpha = (float)Math.atan(tan_alpha);
		
		// determine 2. derivative
		float derivative = 0;
		for(int i=0; i<lookahead; i++) {
			Vector f = fs[i];
			Vector s_unit = s_units[i];
			float r_mag = r_mags[i];
			Vector r_unit = r_units[i];
			derivative += Math.cos(alpha)*f.dot(r_unit)*2*r_mag
					+ Math.sin(alpha)*f.dot(s_unit)*2*r_mag;
		}
		
		// adjust angle according to 2. derivative
        if (derivative<0) {
//            alpha += Math.PI;
            // Current alpha_rad is incorrect; change it by pi radians.
            if (alpha >= 0.0) {
                alpha -= Math.PI;
            } else {
                alpha += Math.PI;
            }
        }
		return alpha*DAMPING_FACTOR ;
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
