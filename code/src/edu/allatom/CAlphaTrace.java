package edu.allatom;

import java.util.LinkedList;
import java.util.List;

public class CAlphaTrace {
	
	List<CAlpha> cAlphaAtoms;
	
	public CAlphaTrace(Protein p) {
		cAlphaAtoms = new LinkedList<CAlpha>();
		CAlpha caOld = null;
		for(AminoAcid aa : p.aaSeq) {
			CAlpha ca = new CAlpha(new Atom(aa.allatoms.get("CA")));
			if(caOld != null) {
				caOld.setNext(ca);
				ca.setPrev(caOld);
			}
			cAlphaAtoms.add(ca);
			caOld = ca;
		}
	}

	
	public class CAlpha {
		public Atom c;
		public CAlpha prev;
		public CAlpha next;
		public CAlpha(Atom c){
			this.c = c;
		}
		public CAlpha(Atom a, CAlpha next, CAlpha prev){
			this.c = a;
			this.prev = prev;
			this.next = next;
		}
		public void setPrev(CAlpha prev) {
			this.prev = prev;
		}
		public void setNext(CAlpha next) {
			this.next = next;
		}		
	}
}
