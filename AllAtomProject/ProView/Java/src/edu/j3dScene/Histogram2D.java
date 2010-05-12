package edu.j3dScene;

import java.awt.Color;

import edu.geom3D.Box;
import edu.math.Vector;

/** A small helper class for generating 3D histograms using J3DScene. 
 * @author R. Fonseca
 * @note Implementing this took exactly 10 minutes. 
 */
public class Histogram2D {
	public static void showHistogram(float[][] data){
		J3DScene scene = J3DScene.createJ3DSceneInFrame();
		for(int r=0;r<data.length;r++){
			for(int c=0;c<data.length;c++){
				Vector p = new Vector(r,c,0);
				Vector[] bases = { new Vector(1,0,0), new Vector(0,1,0), new Vector(0,0,1) };
				float[] extents = {1, 1, data[r][c]};
				Box box = new Box(p,bases,extents);
				
				Color col = new Color(data[r][c]/10, data[r][c]/20, data[r][c]/20);
				
				scene.addShape(box, col);
			}
		}
	}
	
	public static void main(String[] args){
		float[][] data = {
				{0, 2, 1, 0, 0, 1},
				{1, 3, 2, 0, 0, 0},
				{0, 2, 2, 0, 0, 2},
				{2, 5, 4, 0, 2,10},
				{1, 3, 4, 0, 0, 4},
				{0, 2, 2, 0, 0, 1}
		};
		showHistogram(data);
	}
}
