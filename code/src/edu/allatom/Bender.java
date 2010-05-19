package edu.allatom;


import java.util.Iterator;
import java.util.List;

import edu.math.Matrix;
import edu.math.Point;
import edu.math.TransformationMatrix3D;
import edu.math.Vector;

public class Bender {
	
	public static void bendProteinBackbone(Protein p, CAlphaTrace trace) {
		Iterator<AminoAcid> aaIterator = p.aaSeq.iterator();
		Iterator<CAlphaTrace.CAlpha> cAlphaIterator = trace.cAlphaAtoms.iterator();
		
		AminoAcid aaFirst = aaIterator.next();
		Atom caFirst = aaFirst.getAtom("CA");
		CAlphaTrace.CAlpha catFirst = cAlphaIterator.next();
		
		// translation
		Vector t = atomDistance(caFirst, catFirst.c);
		Matrix m = TransformationMatrix3D.createTranslation(t);
		
		System.out.println(catFirst.c.position);
		p.transformProtein(m);
		System.out.println(caFirst.position);

		// rotation
		AminoAcid aaOld = aaFirst;
		AminoAcid aaCurrent = aaIterator.next();
		Vector n = atomPlaneNormal(catFirst.c, catFirst.next.c, aaCurrent.getAtom("CA"));
		float angle = -atomAngle(catFirst.c, catFirst.next.c, aaCurrent.getAtom("CA"));
		Vector vectorFromOrigo = new Vector(catFirst.c.position);
		Vector vectorToOrigo = vectorFromOrigo.neg();
		Matrix m_rotate0 = TransformationMatrix3D.createTranslation(vectorToOrigo);
		Matrix m_rotate1 = TransformationMatrix3D.createRotation(angle,n);
		Matrix m_rotate2 = TransformationMatrix3D.createTranslation(vectorFromOrigo);

		p.transformProtein(m_rotate0);
		p.transformProtein(m_rotate1);
		p.transformProtein(m_rotate2);
		
		System.out.println(caFirst.position);
		
//		while(aaIterator.hasNext() && cAlphaIterator.hasNext()) {
//			AminoAcid aa = aaIterator.next();
//			CAlphaTrace.CAlpha cAlpha = cAlphaIterator.next();
//			bendResidue(aa,cAlpha);		
//		}
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
	
	private static void bendResidue(AminoAcid aa, CAlphaTrace.CAlpha cAlpha) {
		//rotation
//		Vector n = atomPlaneNormal(caFirst, cAlphaFirst.c, cAlphaFirst.next.c);
//		float angle = atomAngle(caFirst, cAlphaFirst.c, cAlphaFirst.next.c);
//		Matrix m1 = TransformationMatrix3D.createRotation(0,n);
//		Matrix m = m0.applyTo(m1);
	}

}
