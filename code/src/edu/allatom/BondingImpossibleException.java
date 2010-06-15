package edu.allatom;

import java.lang.Exception;

class BondingImpossibleException extends Exception {
	public BondingImpossibleException(String message) {
		super(message);
	}
}