package edu.allatom;


import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class RotamerLibrary {
	// map of valid rotamers of each amino acid type.
	// must be loaded from a rotamer library (load*RotamerLibrary()) before use.
	private static Map<AminoAcidType, List<Rotamer>> validRotamers;	

	public static List<Rotamer> lookupRotamers(AminoAcidType aatype) {
		return validRotamers.get(aatype);
	}

	public static Rotamer mostLikelyRotamer(AminoAcidType aatype) {
		// The most likely is the first one in the list, as they are
		// sorted by probability.
		return validRotamers.get(aatype).get(0);
	}

	public static void loadDunbrach(String filename) throws IOException {
		// read file
		StringBuilder sb = new StringBuilder();
		FileReader fr = new FileReader(filename);
		int c;
		while((c = fr.read()) != -1) {
			sb.append((char) c);
		}
		String data = sb.toString();
		
		// parse data
		validRotamers = new TreeMap<AminoAcidType, List<Rotamer>>();
		String lines[] = data.substring(data.indexOf("\nARG ")).split("\n");
		AminoAcidType previousType = null;
		List<Rotamer> rotamersOfCurrentType = new LinkedList<Rotamer>();
		for(String line : lines) {
			if(line.trim().length() == 0) {
				continue;
			}
			List<String> tokens = new ArrayList<String>(20);
			for(String token : line.split(" ")) {
				if(token.length() > 0) {
					tokens.add(token);
				}
			}
			
			AminoAcidType type = AminoAcidType.valueOf(tokens.get(0));
			if(type == null) {
				System.out.println("Unknown amino acid in rotamer library: " +
						tokens.get(0));
			}
			Rotamer rotamer = new Rotamer();
			rotamer.probability = Double.parseDouble(tokens.get(7)) / 100;
			for(int p=0; p<AminoAcidType.valueOf(type.toString()).chiAngleCount; p++) {
				double degrees = Double.parseDouble(tokens.get(11 + 2*p));
                rotamer.chis[p] = (degrees/180.0) * Math.PI;
			}
			
			if(type != previousType && previousType != null) {
                Collections.sort(rotamersOfCurrentType);
				validRotamers.put(previousType, rotamersOfCurrentType);
				rotamersOfCurrentType = new LinkedList<Rotamer>();
			}
			rotamersOfCurrentType.add(rotamer);
			previousType = type;
		}
        Collections.sort(rotamersOfCurrentType);
		validRotamers.put(previousType, rotamersOfCurrentType);
		
		Rotamer alaRotamer = new Rotamer();
		alaRotamer.probability = 1;
		List<Rotamer> alaRotamers = new LinkedList<Rotamer>();
		alaRotamers.add(alaRotamer);
		validRotamers.put(AminoAcidType.ALA, alaRotamers);
		
		Rotamer glyRotamer = new Rotamer();
		glyRotamer.probability = 1;
		List<Rotamer> glyRotamers = new LinkedList<Rotamer>();
		glyRotamers.add(glyRotamer);
		validRotamers.put(AminoAcidType.GLY, glyRotamers);
	}
}