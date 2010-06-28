package edu.allatom;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;


public class Statistics {
	
	private static final int RAMACHRANDRAN_PLOT_SIZE = 500;
	private static final int RAMACHRANDRAN_PLOT_POINT_SIZE = 6;
	
	private static String ramachandranSVGPlot(Protein p) {
		String plot = "<?xml version=\"1.0\" standalone=\"no\"?>\n" +
				"<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\"\n" + 
				"\"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">\n\n" +
				
				"<svg width=\"100%\" height=\"100%\" version=\"1.1\"\n" +
				"xmlns=\"http://www.w3.org/2000/svg\">\n\n";
		
		plot += "<line x1=\"50%\" y1=\"0%\" x2=\"50%\" y2=\"100%\" " +
				"style=\"stroke:rgb(0,0,0); stroke-width:.2%\"/>\n";
		plot += "<line x1=\"0%\" y1=\"50%\" x2=\"100%\" y2=\"50%\" " +
				"style=\"stroke:rgb(0,0,0); stroke-width:.2%\"/>\n\n";
		for(AminoAcid aa : p.aaSeq) {
			try {
//				System.out.println("phi " + aa.phi() + ", psi " + aa.psi());
				double x = (aa.phi() / 360. + 0.5) * 100;
				double y = 100 - (aa.psi() / 360. + 0.5) * 100;
				plot += "<circle cx=\"" + x + "%\" cy=\"" + y +
						"%\" r=\".3%\" stroke=\"black\" " +
						"stroke-width=\"0\" fill=\"black\"/>\n";
			} catch(NullPointerException e) {
				System.out.println("Unable to get phi or psi");
			}
		}
		plot += "\n</svg>\n";
		
		return plot;
	}
	
	public static void dumpRamachandranSVGPlot(Protein p, String filename) {
		String plot = ramachandranSVGPlot(p);
		System.out.println("image generated");
		FileWriter writer;
		try {
			writer = new FileWriter(new File(filename));
			writer.write(plot);
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not write ramachandran plot to file: " +
					filename);
			return;
		}
		System.out.println("image saved to " + filename);
	}
	
	private static BufferedImage ramachandranPNGPlot(Protein p) {
		BufferedImage image = new BufferedImage(RAMACHRANDRAN_PLOT_SIZE,
				RAMACHRANDRAN_PLOT_SIZE, BufferedImage.TYPE_BYTE_GRAY);
		Graphics g = image.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, RAMACHRANDRAN_PLOT_SIZE, RAMACHRANDRAN_PLOT_SIZE);
		
		g.setColor(Color.BLACK);
		
		g.drawLine(RAMACHRANDRAN_PLOT_SIZE/2, 0,
				RAMACHRANDRAN_PLOT_SIZE/2, RAMACHRANDRAN_PLOT_SIZE);
		g.drawLine(0, RAMACHRANDRAN_PLOT_SIZE/2,
				RAMACHRANDRAN_PLOT_SIZE, RAMACHRANDRAN_PLOT_SIZE/2);
		
		for(AminoAcid aa : p.aaSeq) {
			try {
				System.out.println("phi " + aa.phi() + ", psi " + aa.psi());
				int x = (int) ((aa.phi() / 360. + 0.5) *
						RAMACHRANDRAN_PLOT_SIZE);
				int y = (int) (RAMACHRANDRAN_PLOT_SIZE -
						(aa.psi() / 360. + 0.5) * RAMACHRANDRAN_PLOT_SIZE);
				g.fillOval(
						x - RAMACHRANDRAN_PLOT_POINT_SIZE/2,
						y - RAMACHRANDRAN_PLOT_POINT_SIZE/2,
						RAMACHRANDRAN_PLOT_POINT_SIZE,
						RAMACHRANDRAN_PLOT_POINT_SIZE);
			} catch(NullPointerException e) {
				System.out.println("Unable to get phi or psi");
			}
		}
		
		return image;
	}
	
	public static void dumpRamachandranPNGPlot(Protein p, String filename) {
		BufferedImage plot = ramachandranPNGPlot(p);
		System.out.println("image generated");
		try {
			ImageIO.write(plot, "PNG", new File(filename));
		} catch (IOException e) {
			System.out.println("Could not write ramachandran plot to file: " +
					filename);
			return;
		}
		System.out.println("image saved to " + filename);
	}

}
