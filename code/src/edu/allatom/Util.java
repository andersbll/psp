package edu.allatom;

import java.util.ListIterator;
import java.util.NoSuchElementException;


public class Util {
	// Lulz @ Java
	public static <E> E iteratorPeek(ListIterator<E> iterator) throws NoSuchElementException {
		E elem = iterator.next();
		iterator.previous();
		return elem;
	}
}
