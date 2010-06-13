package edu.allatom;

import java.util.LinkedList;

public class CAlphaTrace {
	
//	List<CAlpha> cAlphaAtoms;
	
	public static LinkedList<Atom> CAlphaTrace(Protein p) {
		LinkedList<Atom> list = new LinkedList<Atom>();
		for(AminoAcid aa : p.aaSeq) {
			Atom ca = new Atom(aa.allatoms.get("CA"));
			list.add(ca);
		}
		return list;
	}

//	
//	public class CAlpha {
//		public Atom c;
//		public CAlpha prev;
//		public CAlpha next;
//		public CAlpha(Atom c){
//			this.c = c;
//		}
//		public CAlpha(Atom a, CAlpha next, CAlpha prev){
//			this.c = a;
//			this.prev = prev;
//			this.next = next;
//		}
//		public void setPrev(CAlpha prev) {
//			this.prev = prev;
//		}
//		public void setNext(CAlpha next) {
//			this.next = next;
//		}		
//	}
}
