package edu.j3dScene;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import edu.geom3D.Box;
import edu.geom3D.Capsule;
import edu.math.Matrix;
import edu.math.Vector;
import edu.Toolbox;

/** A graphics class for rendering POV-ray scenes
 * @author R. Fonseca
 */
public class PRGraphics {
	private float[] col = {1,1,1,0};
	StringBuilder scene = new StringBuilder();

	public Color getColor(){
		return new Color(col[0], col[1], col[2], (1-col[3]));
	}
	public void setColor(Color c){ 
		col[0] = c.getRed()*1f/255f;
		col[1] = c.getGreen()*1f/255f;
		col[2] = c.getBlue()*1f/255f;
		col[3] = 1-(c.getAlpha()*1f/255f);
	}

	public void drawCapsule(Capsule caps){
		drawCapsule(caps.p1, caps.p2,caps.rad);
	}


	public void drawWireframeCapsule(Capsule v, float wireframeThickness) {
		Color old = getColor();
		setColor(new Color(old.getRed(),old.getGreen(), old.getBlue(), 30));
		drawCapsule(v.p1, v.p2, v.rad);
		setColor(old);

		List<Vector> poly = new LinkedList<Vector>();
		addHalfSphereContourPoints(v.p1, v.rad, v.p2.vectorTo(v.p1), camPos, poly);
		addHalfSphereContourPoints(v.p2, v.rad, v.p1.vectorTo(v.p2), camPos, poly);

		if(poly.isEmpty()) return;

		Vector prev = null;
		for(Vector p: poly){
			if(prev!=null) drawCapsule(prev, p, wireframeThickness);
			prev = p;
		}
		drawCapsule(prev, poly.get(0), wireframeThickness);

	}
	private void addHalfSphereContourPoints(Vector center, float rad, Vector hsDir, Vector eye, List<Vector> points){
		double EP = Math.sqrt(eye.distanceSquared(center)-rad*rad);
		if(rad/EP<0.001) return;

		float theta = (float)(Math.PI/2-Math.asin(Math.min(1,EP/eye.distance(center))));
		//System.out.println(theta);
		Vector up = eye.vectorTo(center).cross(hsDir).normIn().timesIn(rad);
		Matrix.createRotationMatrix(-theta, hsDir.norm()).applyToIn(up);
		up.plusIn(center);

		Vector down = eye.vectorTo(center).cross(hsDir).normIn().timesIn(-rad);
		Matrix.createRotationMatrix(theta, hsDir.norm()).applyToIn(down);
		down.plusIn(center);

		Vector middle = up.plus(down).timesIn(0.5f);

		Vector x = hsDir.norm();
		Vector y = middle.vectorTo(up).normIn();

		//points.add(up);
		//points.add(down);


		for(float t=-(float)Math.PI/2;t<Math.PI/2;t+=Math.PI/(2*16)){
			Vector xt = x.times((float)Math.cos(t)).timesIn(rad);
			Vector yt = y.times((float)Math.sin(t)).timesIn(rad);
			points.add(middle.plus( xt.plus(yt) ));
		}
	}


	public void drawCapsule(Vector start, Vector end, float rad){
		scene.append(String.format("sphere_sweep{\n"));
		scene.append(String.format("  linear_spline 2,\n"));
		scene.append(String.format("  <%.3f,%.3f,%.3f>, %.3f\n", start.x(), start.y(), start.z(), rad));
		scene.append(String.format("  <%.3f,%.3f,%.3f>, %.3f\n", end.x(), end.y(), end.z(), rad));
		scene.append(String.format("  texture{\n"));
		scene.append(String.format("    pigment{ color rgbt<%.1f,%.1f,%.1f,%.1f> }\n",col[0], col[1], col[2],col[3]));
		scene.append(String.format("    finish{ specular 0.2 }\n"));
		scene.append(String.format("  }\n}\n\n"));
	}
	public void drawWireframeBox(Box box, float wireframeThickness){
		float[] extents = box.extents;
		Vector[] bases = box.bases; 
		Vector point = box.p;
		Vector[] sBases = new Vector[3];
		for(int b=0;b<3;b++) sBases[b] = bases[b].times(extents[b]);

		this.drawCapsule(point, point.plus(sBases[0]), wireframeThickness);
		this.drawCapsule(point, point.plus(sBases[1]), wireframeThickness);
		this.drawCapsule(point, point.plus(sBases[2]), wireframeThickness);

		Vector opposite =point.plus(sBases[0]).plus(sBases[1]).plus(sBases[2]); 
		this.drawCapsule(opposite, opposite.minus(sBases[0]), wireframeThickness);
		this.drawCapsule(opposite, opposite.minus(sBases[1]), wireframeThickness);
		this.drawCapsule(opposite, opposite.minus(sBases[2]), wireframeThickness);

		this.drawCapsule(point.plus(sBases[0]),point.plus(sBases[0]).plus(sBases[1]),  wireframeThickness);
		this.drawCapsule(point.plus(sBases[0]),point.plus(sBases[0]).plus(sBases[2]),  wireframeThickness);
		this.drawCapsule(point.plus(sBases[1]),point.plus(sBases[0]).plus(sBases[1]),  wireframeThickness);
		this.drawCapsule(point.plus(sBases[1]),point.plus(sBases[1]).plus(sBases[2]),  wireframeThickness);
		this.drawCapsule(point.plus(sBases[2]),point.plus(sBases[0]).plus(sBases[2]),  wireframeThickness);
		this.drawCapsule(point.plus(sBases[2]),point.plus(sBases[1]).plus(sBases[2]),  wireframeThickness);
	}
	public void drawBox(Box box){
		float[] extents = box.extents;
		Vector[] bases = box.bases; 
		Vector point = box.p;
		scene.append(String.format("box{\n"));
		scene.append(String.format("  %s, %s\n", toPRVec(Vector.O()), toPRVec(new Vector(extents[0],extents[1],extents[2]))));
		scene.append(String.format("  matrix < %f, %f, %f, %f, %f, %f, %f, %f, %f, 0,0,0>\n",
				bases[0].x(), bases[0].y(), bases[0].z(),
				bases[1].x(), bases[1].y(), bases[1].z(),
				bases[2].x(), bases[2].y(), bases[2].z()));

		scene.append(String.format("  translate %s\n", toPRVec( point )));
		scene.append(String.format("  texture{\n"));
		scene.append(String.format("    pigment{ color rgbt<%.1f,%.1f,%.1f,%.1f> }\n",col[0], col[1], col[2],col[3]));
		scene.append(String.format("    finish{ specular 0.2 }\n"));
		scene.append(String.format("  }\n}\n\n"));
	}

	public void drawSphere(Vector center, float rad){
		scene.append(String.format("sphere{\n  <%.3f,%.3f,%.3f> %.3f\n", center.x(), center.y(), center.z(), rad));
		scene.append(String.format("  texture{\n"));
		scene.append(String.format("    pigment{ color rgbt<%.1f,%.1f,%.1f,%.1f> }\n",col[0], col[1], col[2],col[3]));
		scene.append(String.format("    finish{ specular 0.2 }\n"));
		scene.append(String.format("  }\n}\n\n"));
	}

	public void drawLineSegment(Vector start, Vector end, float rad){
		drawCapsule(start,end, rad);
		drawSphere(start, rad*2);
		drawSphere(end, rad*2);
	}
	public void drawAxis(){
		//scene.append(axisText);
		Color old = getColor();
		setColor(Color.red);	this.drawSphere(new Vector(1,0,0), 0.1f);
		setColor(Color.green);	this.drawSphere(new Vector(0,1,0), 0.1f);
		setColor(Color.blue);	this.drawSphere(new Vector(0,0,1), 0.1f);
		setColor(Color.black);	this.drawSphere(new Vector(0,0,0), 0.1f);
		setColor(old);
	}

	private void placeHeader(StringBuilder sb){
		sb.append("#include \"colors.inc\"\n");
		sb.append("global_settings { assumed_gamma 1.0 }\n");
		sb.append("background   { color rgb <1,1,1> }\n");
	}

	public Vector camPos = new Vector(0,0,10);
	public Vector camLook = new Vector(0,0,0);
	public Vector camSky = new Vector(0,1,0);
	public Vector l1 = new Vector(5,5,10);
	public Vector l2 = new Vector(5,-5,10);

	private void placeCamera(StringBuilder sb){

		sb.append("camera{\n");
		sb.append(String.format("  look_at %s\n",toPRVec(camLook)));
		sb.append(String.format("  location %s\n",toPRVec(camPos)));
		sb.append(String.format("  sky %s\n",toPRVec(camSky)));
		sb.append("}\n\n");
		sb.append("global_settings{  max_trace_level 255 }\n");

		sb.append("light_source{\n");
		sb.append(String.format("  %s\n",toPRVec(l1)));
		sb.append(String.format("  color rgb <1,1,1>\n"));
		sb.append("}\n\n");
		/*sb.append("light_source{\n");
		sb.append(String.format("  %s\n",toPRVec(l2)));
		sb.append(String.format("  color rgb <1,1,1>\n"));
		sb.append("}\n\n");*/
	}
	public void renderRotation(String toFile){
		int steps = 10;
		Vector origCamPos = camPos.clone();
		Vector delta = camSky.cross(camPos.vectorTo(camLook)).normIn();
		delta.timesIn(camPos.vectorTo(camLook).length()*2f/steps);
		delta.plusIn(camPos.vectorTo(camLook).timesIn(0.02f));
		for(int s=0;s<steps;s++){
			String fname = String.format("%s_%02d%s", 
					toFile.substring(0, toFile.length()-4),
					s,
					toFile.substring(toFile.length()-4));
			render(fname, false);
			camPos.plusIn(delta);
		}
		camPos = origCamPos;

		try {
			String absPath = new File(toFile).getCanonicalPath();
			//System.out.println(absPath+" .. "+File.separatorChar+" .. "+absPath.lastIndexOf(File.separatorChar));
			absPath = absPath.substring(0,absPath.lastIndexOf(File.separatorChar))+"/*png";
			System.out.println(absPath);
			String[] cmd2 = {"/usr/bin/open", absPath};
			Process proc2 = Runtime.getRuntime().exec(cmd2);
			proc2.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void render(String toFile){
		render(toFile, true);
	}
	public void render(String toFile, boolean display){
		StringBuilder sb = new StringBuilder();
		placeHeader(sb);
		placeCamera(sb);
		sb.append(scene.toString());
		File f = new File(toFile.replace("png", "pov"));
		Toolbox.writeToFile(sb.toString(), f.getAbsolutePath(), false);
		try {
			String[] cmd = {"/usr/local/bin/povray", "+W800", "+H600","+Q6", f.getAbsolutePath()};
			Process proc = Runtime.getRuntime().exec(cmd);
			proc.waitFor();

			f = new File(toFile);
			System.out.println("Stored image in "+f.getAbsolutePath());

			if(display){
				String[] cmd2 = {"/usr/bin/open", f.getAbsolutePath()};
				Process proc2 = Runtime.getRuntime().exec(cmd2);
				proc2.waitFor();
			}

		}catch(Exception exc){/*handle exception*/}
	}
	private static String toPRVec(Vector v){
		return String.format("<%.2f, %.2f, %.2f>", v.x(),v.y(),v.z());
	}

	static String axisText = 
		"#include \"textures.inc\"                                                    \n"+
		"                                                                             \n"+
		"#declare Axis_texture = //                                                   \n"+
		"pigment {                                                                    \n"+
		"  gradient <1,0,0>                                                           \n"+
		"  color_map {[0.0 color <1,1,1>]                                             \n"+
		"    [0.5 color <1,1,1>]                                                      \n"+
		"    [0.5 color <1,0,0>]                                                      \n"+
		"    [1.0 color <1,0,0>]                                                      \n"+
		"  }                                                                          \n"+
		"    scale 2                                                                  \n"+
		"}                                                                            \n"+
		"                                                                             \n"+
		"                                                                             \n"+
		"#declare Diameter = 0.1;                                                     \n"+
		"#declare Length = 3;                                                         \n"+
		"#declare axis = //                                                           \n"+
		"union {                                                                      \n"+
		"  cylinder {                                                                 \n"+
		"    <0, 0, 0>, <Length, 0, 0>, Diameter                                      \n"+
		"  }                                                                          \n"+
		"  cone { <Length, 0, 0>, Diameter * 2, <Length + Diameter * 5 , 0, 0>, 0 }   \n"+
		"  texture { Axis_texture}                                                    \n"+
		"//    pigment { Candy_Cane                                                   \n"+
		"//      color rgb <1, 1, 1>                                                  \n"+
		"//    }                                                                      \n"+
		"   finish {ambient 0.1 diffuse 0.9 phong 1}                                  \n"+
		"//  }                                                                        \n"+
		"}                                                                            \n"+
		"                                                                             \n"+
		"#declare XYZ_axis = //                                                        \n"+
		"union {                                                                      \n"+
		"   text {  ttf   \"crystal.ttf\",  \"0\",  .1,   0 scale 0.5                 \n"+
		"      pigment{color<1,1,0> }                                                 \n"+
		"   }                                                                         \n"+
		"  union {                                                                    \n"+
		"  object {axis }                                                             \n"+
		"    text {  ttf   \"crystal.ttf\",  \"X\",  .1,   0 scale 0.5                \n"+
		"      pigment{color<1,1,0> }                                                 \n"+
		"	translate <Length, Diameter*2,Diameter*2>}                                 \n"+
		"    }                                                                        \n"+
		"   union {                                                                   \n"+
		"     object {axis }                                                          \n"+
		"     text {  ttf   \"crystal.ttf\",  \"Y\",  .1,   0  scale 0.5              \n"+
		"       pigment{color<1,1,0>} 	                                             \n"+
		"       translate <Length, Diameter*2,Diameter*2>}                            \n"+
		"     rotate <0,90,0>                                                         \n"+
		"   }                                                                         \n"+
		"  union {                                                                    \n"+
		"    object {axis }                                                           \n"+
		"    text {  ttf   \"crystal.ttf\",  \"Z\",  .1,   0                          \n"+
		"      rotate <0,0,-90>                                                       \n"+
		"      scale 0.5                                                              \n"+
		"      pigment{color<1,1,0>}                                                  \n"+
		"      translate <Length,Diameter*2,Diameter*2>                               \n"+
		"    }                                                                        \n"+
		"    rotate <0,0,90>                                                          \n"+
		"  }                                                                          \n"+
		"}                                                                            \n"+
		"                                                                             \n"+
		"object {XYZ_axis}                                                            \n";


}
