package edu.allatom;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BendingStats {

	static PrintWriter file;

	public static void main(String[] args) {

		int i = Integer.parseInt(args[0]);
		System.out.println(i);
		try {
			file = new PrintWriter(new FileWriter("./bendingstats"+i+".py"));
			RotamerLibrary.loadDunbrachRotamerLibrary("bbind02.May.lib");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
//		print("bending_stats = [\n");

//		for (int i = 0; i <= 23; i++) {
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
				LinkedList<Atom> trace = CAlphaTrace.CAlphaTrace(p);
				String proteinName = f.getName();
				proteinName = proteinName.substring(0, proteinName
						.lastIndexOf('.'));
				print("    ['" + proteinName + "'," + trace.size() + ",[\n");
				Bender.bendProteinBackbone(Protein
						.getUncoiledProtein(typeTrace), trace, null);
				print("    ]],\n");

			}
			print("  ]],\n");
//		}
//		print("]\n");
		file.close();
	}

	public static void print(String s) {
		file.print(s);
		file.flush();
		System.out.print(s);
	}
}
