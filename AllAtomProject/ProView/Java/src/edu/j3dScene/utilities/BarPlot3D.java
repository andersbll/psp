package edu.j3dScene.utilities;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import edu.geom3D.Box;
import edu.geom3D.Capsule;
import edu.geom3D.Cylinder;
import edu.j3dScene.J3DScene;
import edu.math.Vector;
import edu.math.Vector2D;

public class BarPlot3D {
	public Color barSideColor = new Color(200,200,200,100);
	public float barXWidth = 1, barYWidth=1, topPlateHeight = 0.02f;
	public Palette palette = new Palette();
	public Vector2D palettePos  = new Vector2D(0,0);
	public List<Vector> points;

	public BarPlot3D(List<Vector> points){
		this.points = points;
	}

	public J3DScene displayPlot(){
		J3DScene ret = J3DScene.createJ3DSceneInFrame();
		for(Vector p: points)
			paintBar(ret, p);
		paintAxis(ret);
		paintPalette(ret);
		return ret;
	}

	private void paintBar(J3DScene scene, Vector p){
		Vector planeCenter = new Vector(p.x(), p.y(), 0);
		Vector[] units = {new Vector(1,0,0), new Vector(0,1,0), new Vector(0,0,1)};

		Box bottomBox = new Box(
				planeCenter.minus(units[0].times(barXWidth/2)).minusIn(units[1].times(barYWidth/2)),
				units,
				new float[]{barXWidth, barYWidth, p.z()-topPlateHeight*2}
		);
		scene.addShape(bottomBox, barSideColor);
		Box topBox = new Box(
				p.minus(units[0].times(barXWidth/2)).minusIn(units[1].times(barYWidth/2)),
				units,
				new float[]{barXWidth, barYWidth, topPlateHeight}
		);
		scene.addShape(topBox, palette.getColor(p.z()));

	}
	
	private void paintAxis(J3DScene scene){
		float xLength = 0;
		float yLength = 0;
		for(Vector p: points){
			if(p.x()>xLength) xLength = p.x();
			if(p.y()>yLength) yLength = p.y();
		}
		xLength+=this.barXWidth;
		yLength+=this.barYWidth;
		
		float rad = Math.max(xLength, yLength)/200;
		
		Capsule xCaps = new Capsule(new Vector(0,0,0), new Vector(xLength,0,0), rad);
		Capsule yCaps = new Capsule(new Vector(0,0,0), new Vector(0,yLength,0), rad);
		scene.addShape(xCaps, Color.BLACK);
		scene.addShape(yCaps, Color.BLACK);
		
	}

	private void paintPalette(J3DScene scene){
		float increment = 0.01f;
		
		float xLength = 0;
		float yLength = 0;
		for(Vector p: points){
			if(p.x()>xLength) xLength = p.x();
			if(p.y()>yLength) yLength = p.y();
		}
		float paletteRad = Math.max(xLength, yLength)/20; 
		
		for(float z=palette.minValue;z<palette.maxValue;z+=increment){
			Vector zVec1 = new Vector(0,z,0);
			Vector zVec2 = new Vector(0,z+increment,0);
			Cylinder c = new Cylinder(palettePos.plus(zVec1), palettePos.plus(zVec2), paletteRad);
			scene.addShape(c, palette.getColor(z));
		}
	}
	
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<Vector> points = new ArrayList<Vector>();
		points.add(new Vector(0.05,0.05,0.1));
		points.add(new Vector(0.15,0.05,0.13));
		points.add(new Vector(0.25,0.05,0.2));
		points.add(new Vector(0.05,0.15,0.21));
		points.add(new Vector(0.15,0.15,0.15));
		points.add(new Vector(0.25,0.15,0.3));
		points.add(new Vector(0.05,0.25,0.4));
		points.add(new Vector(0.15,0.25,0.4));
		points.add(new Vector(0.25,0.25,0.5));
		BarPlot3D barPlot = new BarPlot3D(points);
		barPlot.barXWidth = 0.09f;
		barPlot.barYWidth = 0.09f;
		barPlot.topPlateHeight = 0.002f;
		barPlot.palette = new ThreeColorPalette(0,0.5f);
		barPlot.displayPlot();
	}


	public static class Palette{
		public float minValue, maxValue;
		public Palette(){ this(0, 1); }
		public Palette(float min, float max){
			minValue = min;
			maxValue = max;
		}
		Color getColor(float value){
			float scaledVal = ((value-minValue)/(maxValue-minValue));
			scaledVal = Math.min(1, Math.max(0, scaledVal));
			scaledVal = (float)Math.sqrt(scaledVal);
			return new Color(0, 0, scaledVal);
		}
	}
	public static class ThreeColorPalette extends Palette{
		public Color c1 = Color.YELLOW, c2 = Color.RED, c3 = Color.GREEN.darker();
		public ThreeColorPalette(float min, float max){
			super(min,max);
			
		}
		Color getColor(float value){
			float t = ((value-minValue)/(maxValue-minValue));
			t = Math.min(1, Math.max(0, t));
			t = (float)Math.sqrt(t);
			if(t>1) return c3;
			if(t<0) return c1;
			float r1 = c1.getRed()*(t<0.5?(1-2*t):0);
			float g1 = c1.getGreen()*(t<0.5?(1-2*t):0);
			float b1 = c1.getBlue()*(t<0.5?(1-2*t):0);
			float r2 = c2.getRed()*(t<0.5?2*t:(2-2*t));
			float g2 = c2.getGreen()*(t<0.5?2*t:(2-2*t));
			float b2 = c2.getBlue()*(t<0.5?2*t:(2-2*t));
			float r3 = c3.getRed()*(t>0.5?(-1+2*t):0);
			float g3 = c3.getGreen()*(t>0.5?(-1+2*t):0);
			float b3 = c3.getBlue()*(t>0.5?(-1+2*t):0);
			return new Color((int)(r1+r2+r3), (int)(g1+g2+g3), (int)(b1+b2+b3));
		}
	}
}
