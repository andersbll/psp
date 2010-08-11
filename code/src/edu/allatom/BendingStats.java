package edu.allatom;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import edu.math.Matrix;
import edu.math.TransformationMatrix3D;
import edu.math.Vector;

public class BendingStats {

	static PrintWriter file;

	
	public static void main(String[] args) {
//		rotamerBenderStats();
//		rotamerBenderRMSD();
		rmsdStats();
	}
	
	public static void rmsdStats() {
		try {
			file = new PrintWriter(new FileWriter("./bendingstats.py"));
			RotamerLibrary.loadDunbrach("bbind02.May.lib");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		String proteins = "";
			File path = new File("./pdbs_large/");
			for (File f : path.listFiles()) {
				Protein p;
				Protein otherProtein;
				if (!f.toString().endsWith(".pdb")) {
					continue;
				}
				String proteinName = f.getName();
				proteinName = proteinName.substring(0, proteinName
						.lastIndexOf('.'));
				try {
					p = PDBParser.parseFile(f.toString());
					otherProtein = PDBParser.parseFile("./scwrl_out/scwrl_"+proteinName+".pdb");
				} catch (Exception e) {
//					f.renameTo(new File(dir, f.getName()));
					e.printStackTrace();
					continue;
				}
							
//				System.out.println("woop "+p.aaSeq.size()+"   wppå "+otherProtein.aaSeq.size());
				double rmsd = p.minRMSD(otherProtein);
				int collisions = Bender.countCollisions(otherProtein);
				print("    ('" + proteinName + "'," + p.aaSeq.size() + "," + rmsd + "," + collisions+"),\n");
				if(rmsd >= 0) {
					proteins += "\""+proteinName+"\", ";
				}
			}
		file.close();
		System.out.println(proteins);
	}
	
	public static void benderStats(String[] args) {
	int i = Integer.parseInt(args[0]);
	System.out.println(i);
	try {
		file = new PrintWriter(new FileWriter("./bendingstats"+i+".py"));
		RotamerLibrary.loadDunbrach("bbind02.May.lib");
	} catch (Exception e) {
		e.printStackTrace();
		return;
	}
//	print("bending_stats = [\n");

//	for (int i = 0; i <= 23; i++) {
		Bender.WINDOW_SIZE = 2 + i;
		Bender.WINDOW_REPETITIONS = 25 - i;

		print("  ["+Bender.ROTAMER_SEARCH_DEPTH
				+ "," + Bender.WINDOW_SIZE
				+ "," + Bender.WINDOW_REPETITIONS+",[\n");

		Protein p;
		File path = new File("./pdb/");
		for (File f : path.listFiles()) {
			if (!f.toString().endsWith(".pdb")) {
				continue;
			}
			// System.out.println("PROTEIN:: " + f.toString());
			try {
				p = PDBParser.parseFile(f.toString());
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}

			List<AminoAcidType> typeTrace = AminoAcid
					.makeTypeTrace(p.aaSeq);
			LinkedList<Atom> trace = p.getCAlphaTrace();
			String proteinName = f.getName();
			proteinName = proteinName.substring(0, proteinName
					.lastIndexOf('.'));
			print("    ['" + proteinName + "'," + trace.size() + ",[\n");
			Bender.bendProteinBackbone(Protein
					.getUncoiledProtein(typeTrace), trace, null);
			print("    ]],\n");

		}
		print("  ]],\n");
//	}
//	print("]\n");
	file.close();
}
	public static void rotamerBenderRMSD() {
		try {
			file = new PrintWriter(new FileWriter("./bendingstats.py"));
			RotamerLibrary.loadDunbrach("bbind02.May.lib");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		String[] pdbz = {"2VKZ", "1ENW", "1GJS", "1EQK", "1J1H", "1AB2", "1ILO", "1JDQ", "1C89", "3DX4", "1G03", "1JAU", "3D4Y", "1I11", "1IYM", "1F4I", "1EL0", "1AKP", "1I35", "1HDJ", "1FJD", "1H8B", "1GUW", "3BUB", "1J26", "1H7D", "1BHI", "1JMQ", "1HN3", "1IMT", "1HP2", "1HYI", "1JNS", "1I17", "1GO5", "1HIC", "1G6E", "1DQB", "2ZXE", "1JM7", "1EQ1", "1APJ", "3BUQ", "1BOE", "1HLY", "1DDM", "1ABZ", "1JBA", "2F1A", "1D9N", "1G2H", "1FTT", "1AJ3", "1IOJ", "1AW0", "1AOY", "1FOV", "1DRS", "1D1R", "1ERD", "1EJQ", "1CFE", "1FRY", "1H7V", "1BCT", "1FYJ", "1CLD", "1CPZ", "1GH8", "2UV9", "1JJG", "1DWM", "1DXZ", "1DGZ", "1J8K", "2W1B", "1E4T", "1DDB", "1CKV", "1ECI", "1ADR", "1FCT", "1GHC", "3I3B", "3HZ3", "3A3Y", "1EMX", "1CSY", "1CCV", "2RDD", "1F8Z", "1F07", "2F7R", "1GXH", "1DP3", "1BZG", "1J9I", "1JOY", "1BZK", "1IYC", "1FGP", "1DQC", "2F18", "3DX3", "1BF9", "1CTL", "3DYP", "1IRH", "3IAP", "1IML", "1E41", "2Q1F", "3BUD", "1FLI", "1GCF", "1HBW", "1B22", "1JFB", "1EIK", "1BY0", "3DDG", "1ITY", "1FEX", "1CX1", "3DYM", "1D8B", "1J8C", "1AFO", "1GYZ", "1J0T", "1FSH", "2XI2", "1BYM", "1FI6", "3DDF", "1HS7", "1HYW", "1JEG", "1FAD", "1IBX", "1BRV", "1DU6", "1J7M", "1GGW", "1IRZ", "1B9R", "1JKZ", "1IEH", "1HMA", "3D51", "1CR8", "1IE6", "1HI7", "1IBA", "3D9B", "1E88", "1EXE", "1HX2", "1I16", "1ILY", "1JKN", "1DBY", "1E4U", "3CZJ", "1BW5", "1JH4", "1H5P", "1I26", "1EZE", "1IXD", "1IBI", "1IQT", "3BVV", "1JBI", "1FJ7", "3BGA", "1D4B", "1FHO", "1FH3", "1AWE", "1EZY", "1JJR", "1CIX", "3I3D", "1IUR", "2UVB", "1D5V", "1GXE", "1E4R", "1GJX", "1CW5", "1JCU", "1BXD", "1CE4", "1G7D", "1J6Y", "1F5X", "1C07", "1G9P", "1AGG", "1ERP", "1EHX", "1J3G", "1J3T", "1C01", "3EJR", "1EV0", "1FAQ", "3E1F", "1AYJ", "1C2N", "1J7Q", "1FYB", "1AUU", "1D2L", "1J3X", "1HD6", "1FJN", "1JO6", "1E0E", "1EIW", "1BO9", "1HA9", "1DRO", "1FAF", "1IG6", "3BUP", "1BDE", "3EJU", "1DIP", "1DG4", "1H67", "1IV0", "1BW3", "1HKY", "1GH9", "1AQ5", "1B64", "1F7E", "1DTK", "1HV2", "1F62", "1G91", "1F54", "1FZD", "1BY6", "1JRF", "1FJC", "1AW6", "1GKG", "3BVU", "1ANS", "1JRJ", "1HCP", "1BA5", "1ADZ", "1FO5", "3DX0", "3BLB", "1JFN", "2I6W", "1JT8", "3I3E", "1IIE", "1CE3", "1H20", "1EF4", "1BQZ", "1JH3", "2F7Q", "1JN7", "1DGN", "1AWW", "1EO1", "1BF0", "1EGX", "1FZT", "1I6Z", "1EYF", "1HO9", "1BPV", "1BY1", "1C4E", "1EXK", "1JGK", "1IW4", "1BK8", "1H2O", "1GP8", "1BAK", "1HXV", "1AXH", "1H8C", "1GL5", "1H3Z", "1JQR", "1C7V", "1AJY", "1JQ4", "1BQ0", "1AFP", "1IQ3", "1GHH", "1B8W", "1DSK", "1GE9", "1AH9", "1HHN", "3IAQ", "1DK2", "1BNB", "3DYO", "1E5G", "1C20", "1E17", "1GPH", "3BVT", "1B4R", "1HN6", "1JE3", "2UV8", "3CZN", "1J2O", "1CXW", "3BUI", "1IFW", "1EMW", "3CZS", "3DX2", "1D1D", "2FYV", "1DX8", "1G9L", "1H95", "1HF9", "1CMZ", "1E0G", "1AZ6", "1EO0", "1HAE", "1H0Z", "1BAL", "1APQ", "3CMM", "1B03", "1G25", "1IE5", "1IRG", "1G7E", "1DEC", "1G84", "1APF", "1GKN", "1D8J", "3EJQ", "1IMU", "1F5Y", "1JI8", "3BVW", "1HA6", "1HA8", "1HYK", "1COK", "1CN2", "1IRY", "1A6S", "1EIJ", "1IX5", "1C3Y"};
//			File path = new File("./pdbs_large/");
		for (String proteinName : pdbz) {
				Protein p;
				Protein trueProtein;
				try {
					String filepath = "./pdbs_large/"+proteinName+".pdb";
					p = PDBParser.parseFile(filepath);
					trueProtein = PDBParser.parseFile(filepath);
				} catch (Exception e) {
//					f.renameTo(new File(dir, f.getName()));
					e.printStackTrace();
					continue;
				}
				print("    ['" + proteinName + "'," + p.aaSeq.size() + ",");
				try {
					Bender.bendRotamers(p);
				} catch (Exception e) {
					print("skinke");					
				}
				double rmsd = p.minRMSD(trueProtein);
				print(", " + rmsd);				
				print("],\n");
			}
		file.close();
	}

	public static void rotamerBenderStats() {
	    File dir = new File("./pdb_crap/");
		try {
			file = new PrintWriter(new FileWriter("./bendingstats.py"));
			RotamerLibrary.loadDunbrach("bbind02.May.lib");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
			File path = new File("./pdbs_large/");
			for (File f : path.listFiles()) {
				Protein p;
				Protein trueProtein;
				if (!f.toString().endsWith(".pdb")) {
					continue;
				}
				try {
					p = PDBParser.parseFile(f.toString());
					trueProtein = PDBParser.parseFile(f.toString());
				} catch (Exception e) {
					f.renameTo(new File(dir, f.getName()));
					e.printStackTrace();
					continue;
				}
				String proteinName = f.getName();
				proteinName = proteinName.substring(0, proteinName
						.lastIndexOf('.'));
				print("    ['" + proteinName + "'," + p.aaSeq.size() + ",");
				try {
					Bender.bendRotamers(p);
				} catch (Exception e) {
					f.renameTo(new File(dir, f.getName()));
					print("skinke");					
				}
				double rmsd = p.minRMSD(trueProtein);
				print(", " + rmsd);

				
				print("],\n");
			}
		file.close();
	}

	
	public static void print(String s) {
		file.print(s);
		file.flush();
		System.out.print(s);
	}
}
