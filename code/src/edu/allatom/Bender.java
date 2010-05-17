package edu.allatom;


import java.util.Iterator;
import java.util.List;

import edu.math.Matrix;
import edu.math.Point;
import edu.math.TransformationMatrix3D;
import edu.math.Vector;

public class Bender {
	
	public static void bendBackbone(Protein p, CAlphaTrace trace) {
		Iterator<AminoAcid> aaIterator = p.aaSeq.iterator();
		Iterator<CAlphaTrace.CAlpha> cAlphaIterator = trace.cAlphaAtoms.iterator();
		
		AminoAcid aaFirst = aaIterator.next();
		CAlphaTrace.CAlpha cAlphaFirst = cAlphaIterator.next();
		
		Atom caFirst = aaFirst.allatoms.get("CA");

		//translation
		Vector t = atomDistance(caFirst, cAlphaFirst.c);
		Matrix m0 = TransformationMatrix3D.createTranslation(t);
		
		transformProtein(p, m0);
		
		
		while(aaIterator.hasNext() && cAlphaIterator.hasNext()) {
			AminoAcid aa = aaIterator.next();
			CAlphaTrace.CAlpha cAlpha = cAlphaIterator.next();
			bendResidue(aa,cAlpha);		
		}
	}
	
	private static void transformProtein(Protein p, Matrix m) {
		for(Atom a : p.getAtoms()) {
			Vector v = new Vector(a.position);
			a.position = new Point(m.applyToIn(v));
		}
	}

	private static Vector atomPlaneNormal(Atom a0, Atom a1, Atom a2) {
		Vector v0 = atomDistance(a0, a1);
		Vector v1 = atomDistance(a0, a2);
		return v0.cross(v1).normIn();
	}

	private static Vector atomDistance(Atom a0, Atom a1) {
		return a0.position.vectorTo(a1.position);
		
	}
	
	private static void bendResidue(AminoAcid aa, CAlphaTrace.CAlpha cAlpha) {

		//rotation
//		Vector n = atomPlaneNormal(caFirst, cAlphaFirst.c, cAlphaFirst.next.c);
//		float angle = atomAngle(caFirst, cAlphaFirst.c, cAlphaFirst.next.c);
//		Matrix m1 = TransformationMatrix3D.createRotation(0,n);
//		Matrix m = m0.applyTo(m1);

	}



}
