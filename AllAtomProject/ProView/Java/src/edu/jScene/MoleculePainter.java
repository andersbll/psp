package edu.jScene;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.media.j3d.Canvas3D;
import javax.swing.JFrame;

import edu.geom3D.Cylinder;
import edu.geom3D.Sphere;
import edu.math.Vector;

public class MoleculePainter {
	public static Color nColor = Color.BLUE;
	public static Color cColor = Color.GRAY;
	public static Color oColor = Color.RED;
	public static Color hColor = Color.WHITE;
	public static Color sColor = Color.YELLOW;
	public static Color bondColor = Color.GRAY.brighter();
	public static final int N = 0;
	public static final int C = 1;
	public static final int O = 2;
	public static final int H = 3;
	public static final int S = 4;
	
	
	private final J3DGraphics scene = new J3DGraphics(); 
	private final List<Sphere> spheres = new ArrayList<Sphere>();
	private final List<Cylinder> cylinders = new ArrayList<Cylinder>();
	
	public MoleculePainter(){
		//scene.repaintRepeatedly(30);
	}
	
	public void setupCoords(List<double[]> coords, List<Integer> types ){
		assert coords.size() == types.size();
		scene.removeAllShapes();
		spheres.clear();
		cylinders.clear();
		
		for(int i=0;i<coords.size();i++){
			double[] c = coords.get(i);
			int t = types.get(i);
			
			Sphere s = new Sphere(new Vector(c[0],c[1],c[2]), 1);
			Color col = Color.GRAY;
			switch(t){
			case N: s.radius = 0.71f; col = nColor; break;
			case C: s.radius = 0.69f; col = cColor; break;
			case O: s.radius = 0.66f; col = oColor; break;
			case H: s.radius = 0.31f; col = hColor; break;
			case S: s.radius = 1.05f; col = sColor; break;
			}
			scene.addShape(s, col);
			spheres.add(s);
		}
		
		for(int i=0;i<coords.size();i++){
			double vdwRad1 = 0;
			switch(types.get(i)){
			case N: vdwRad1 = 1.55; break;
			case C: vdwRad1 = 1.7; break;
			case O: vdwRad1 = 1.5; break;
			case H: vdwRad1 = 1.2; break;
			case S: vdwRad1 = 1.8; break;
			}
			for(int j=i+1;j<coords.size();j++){
				double vdwRad2 = 0;
				switch(types.get(j)){          
				case N: vdwRad2 = 1.55; break; 
				case C: vdwRad2 = 1.7; break;  
				case O: vdwRad2 = 1.5; break;  
				case H: vdwRad2 = 1.2; break;  
				case S: vdwRad2 = 1.8; break;  
				}                  
				
				float d = spheres.get(i).center.distance(spheres.get(j).center);
				if(d < 1.05*(vdwRad1+vdwRad2)/2){
					Cylinder c = new Cylinder(spheres.get(i).center, spheres.get(j).center, 0.2f);

					scene.addShape(c, bondColor);
					cylinders.add(c);
				}
			}
		}
		scene.centerCamera();
	}
	
	public void centerView(){ scene.centerCamera(); }
	
	
	public void updateCoords(List<double[]> coords){
		for(int i=0;i<coords.size();i++){
			double[] c = coords.get(i);
			Vector scenePoint = spheres.get(i).center;
			scenePoint.setX((float)c[0]);
			scenePoint.setY((float)c[1]);
			scenePoint.setZ((float)c[2]);
		}
		
		scene.repaint();
	}
	
	public Canvas3D getCanvas(){
		return scene.getCanvas();
	}
	
	public static MoleculePainter createPainterInFrame(){
		
		MoleculePainter mp = new MoleculePainter();
		
		JFrame f = new JFrame("Molecule painter");
		f.setSize(900, 700);
		f.getContentPane().add(mp.scene.getCanvas());
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
		
		return mp;
	}
	
	public static void main(String[] args){
		MoleculePainter mp = createPainterInFrame();
		List<double[]> coords = new ArrayList<double[]>();
		List<Integer> types = new ArrayList<Integer>();
		coords.add(new double[]{0,0,0});	types.add(C);
		coords.add(new double[]{1.7,0,0});	types.add(C);
		coords.add(new double[]{2.7,0,0});	types.add(C);
		coords.add(new double[]{3.7,0,0});	types.add(C);
		coords.add(new double[]{4.7,0,0});	types.add(C);
		coords.add(new double[]{1.7,1,0});	types.add(N);
		coords.add(new double[]{3.7,-1,0});	types.add(O);
		
		mp.setupCoords(coords, types);

		while(true){
			coords.get(2)[2]+=0.01;
			mp.updateCoords(coords);
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
