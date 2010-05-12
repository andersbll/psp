package edu.jScene;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;

import javax.media.j3d.*;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Text2D;
import com.sun.j3d.utils.universe.SimpleUniverse;

import edu.geom3D.Box;
import edu.geom3D.Capsule;
import edu.geom3D.Shape;
import edu.geom3D.Sphere;
import edu.geom3D.Tetrahedron;
import edu.geom3D.Triangle;
import edu.math.Matrix;
import edu.math.Vector;

/** A graphics class for viewing scenes using java3d. 
 * <code>Shape</code>-subclasses can be added 
 * to a <code>J3DGraphics</code> object and are automatically painted on a 
 * <code>Canvas3D</code> object. For 
 * instance the following code creates a scene with a cylinder and a red 
 * transparent box and adds the canvas to a frame. 
 * <pre>
 * J3DGraphics scene = new J3DGraphics();
 * scene.addVolume(  new Cylinder(new Vector(1,0,0), new Vector(0.5,0.5, 0.3)) );
 * 
 * Vector boxCorner = new Vector(-1,0,0);
 * Vector[] boxBases = {new Vector(1,0,0), new Vector(0,1,0), new Vector(0,0,1)};
 * float[] boxExtents = {0.8f, 1, 2};
 * Box box = new Box( boxCorner, boxBases, boxExtents );
 * scene.addShape( box, new Color(200,0,0,100) );
 * 
 * Canvas3D canvas = scene.getCanvas();
 * 
 * JFrame frame = new JFrame();
 * frame.setSize(400,400);
 * frame.getContentPane().add( canvas );
 * frame.setVisible(true);
 * 
 * scene.repaint();
 * </pre>
 * The <code>repaint()</code> method must be called every time a change 
 * has occured in the scene, and the canvas should be updated. The pointers 
 * to added volumes are stored, so subsequent changes in the <code>box</code> 
 * object in the above code will be visible on the canvas when <code>repaint()</code> 
 * is called. For convenience the following example shows how to quickly create a 
 * <code>J3DGraphics</code> object that is already shown in a frame and ready for use:
 * <pre>
 * J3DGraphics scene = J3DGraphics.createJ3DGraphicsInFrame();
 * scene.paintAxis = true;
 * scene.addShape(  new Cylinder(new Vector(1,0,0), new Vector0,1,0)) );
 * scene.repaint();
 * </pre>
 * @author R. Fonseca
 */
public class J3DGraphics {
	private Canvas3D canvas;
	private BranchGroup sceneRoot, scene;
	private CamBehavior camBehavior;
	private RebuildBehavior rebuildBehavior;
	private Timer repaintTimer;

	private final BoundingSphere bounds = new BoundingSphere(new Point3d(0,0,0), 5000);
	private Background background;
	
	private final Map<Shape,BranchGroup> shapeTransforms = new HashMap<Shape,BranchGroup>();
	private final Map<Shape,Color> primitives = new HashMap<Shape,Color>();
	private Vector lightDir = new Vector(0,-1,-5);
	private Vector sceneCenter = new Vector(0,0,0);
	private final List<Shape> axisElements = new ArrayList<Shape>();

	
	/** Set color of background. The color of added text-elements will change  
	 * such that they are the inverted colors of the background color */
	public void setBackgroundColor(Color c){
		//backgroundColor = new Color(c.getRed(), c.getGreen(), c.getBlue());
		background.setColor(c.getRed()/255f, c.getGreen()/255f, c.getBlue()/255f);
		Color3f col = new Color3f();
	}

	/** Change color of specified volume */
	/*public void setColor(Shape v, Color c){
		if(!primitives.containsKey(v)){
			System.err.println("Warning: No such volume added to J3DGraphics");
			return;
		}
		primitives.remove(v);
		primitives.put(v, c);
	}*/


	/** Removes one volume from the scene. */
	public void removeShape(Shape v){
		primitives.remove(v);
		BranchGroup bg = shapeTransforms.remove(v);
		scene.removeChild(bg);
		
	}

	/** Remove all volumes from the scene. */
	public void removeAllShapes(){
		primitives.clear();
		shapeTransforms.clear();
	}

	/** Add a volume object. The standard color gray will be used */
	public void addShape(Shape v){	addShape(v,Color.gray);	}
	/** Add a volume object with a specified color */
	public void addShape(Shape v, Color c){	
		primitives.put(v, c);
		Node p = genPrimitive(v, c);
		scene.addChild(p);
	}
	/** Add a text-object at the specified position. The text will always be the 
	 * inverted color of the background for the best readability.*/
	public void addText(String t, Vector pos){ 
		addShape(new TextShape(t,pos), Color.GRAY); 
	}
	public void addText(String t, Vector pos, float height){ 
		addShape(new TextShape(t,pos,height), Color.GRAY); 
	}

	public void centerCamera(){
		Vector sceneCenter = Vector.O().clone();
		float maxDist = 0;
		for(Entry<Shape, Color> entry: primitives.entrySet()){
			sceneCenter.plusIn(entry.getKey().getCenter());

			for(Entry<Shape, Color> entry2: primitives.entrySet()){
				float d = entry.getKey().getCenter().distance(entry2.getKey().getCenter());
				if(d>maxDist) maxDist=d;
			}
		}
		sceneCenter.timesIn(1f/primitives.entrySet().size());
		
		Transform3D transform = new Transform3D();
		transform.setTranslation(toJ3DVec(sceneCenter.times(-1f)));
		//TransformGroup tg = new TransformGroup(transform);
		TransformGroup tg = ((TransformGroup)((TransformGroup)sceneRoot.getChild(0)).getChild(0));
		tg.setTransform(transform);
		
		//System.out.println(maxDist);
		if(maxDist>0)
			this.camBehavior.scale(-2/maxDist+1.2f);
	}

	
	private void updateTransforms(Shape v){
		if(v instanceof Sphere) updateSphereTransforms((Sphere)v);
		if(v instanceof edu.geom3D.Cylinder) updateCylinderTransforms((edu.geom3D.Cylinder)v);
		if(v instanceof edu.geom3D.Cone) updateConeTransforms((edu.geom3D.Cone)v);
		if(v instanceof TextShape) updateTextTransforms((TextShape)v);
		if(v instanceof Box) updateBoxTransforms((Box)v);
		if(v instanceof Capsule) updateCapsuleTransforms((Capsule)v);
		if(v instanceof Triangle) updateTriangleTransforms((Triangle)v);
		if(v instanceof Tetrahedron) updateTetrahedronTransforms((Tetrahedron)v);
	}
	private void updateSphereTransforms(Sphere s){
		TransformGroup tg = (TransformGroup)shapeTransforms.get(s).getChild(0);
		
		Transform3D trans = new Transform3D();
		trans.setTranslation(toJ3DVec(s.center));
		trans.setScale(s.radius);
		
		tg.setTransform(trans);
	}
	private void updateCylinderTransforms(edu.geom3D.Cylinder c){
		
		Transform3D trans = new Transform3D();
		Vector v1 = new Vector(0,1.01,0);
		Vector v2 = c.p1.vectorTo(c.p2);
		
		if(v2.length()>0.000001 && v1.angle(v2)>0.00001 && v1.angle(v2)<Math.PI-0.00001){ 
			Matrix m = Matrix.createRotationMatrix(v1.angle(v2), v1.cross(v2).normIn());
			trans.set(m.getNormalizedCoordArray());
		}
		trans.setScale(new Vector3d(c.rad, v2.length(), c.rad));
		trans.setTranslation(toJ3DVec(c.p1.plus(c.p2).timesIn(0.5f)));
		
		((TransformGroup)shapeTransforms.get(c).getChild(0)).setTransform(trans);
	}
	private void updateConeTransforms(edu.geom3D.Cone c){
		
		Transform3D trans = new Transform3D();
		Vector v1 = new Vector(0,1,0);
		Vector v2 = c.p1.vectorTo(c.p2);
		
		if(v2.length()>0.000001 && v1.angle(v2)>0.00001){ 
			Matrix m = Matrix.createRotationMatrix(v1.angle(v2), v1.cross(v2).normIn());
			trans.set(m.getNormalizedCoordArray());
		}
		trans.setScale(new Vector3d(c.rad, v2.length(), c.rad));
		trans.setTranslation(toJ3DVec(c.p1.plus(c.p2).timesIn(0.5f)));

		((TransformGroup)shapeTransforms.get(c).getChild(0)).setTransform(trans);
	}
	private void updateTextTransforms(TextShape t){
		Transform3D transform = new Transform3D();
		transform.setTranslation(toJ3DVec(t.pos));
		transform.setScale(4*t.height);

		((TransformGroup)shapeTransforms.get(t).getChild(0)).setTransform(transform);
	}
	private void updateBoxTransforms(Box b){
		Transform3D transform = new Transform3D();
		Matrix m = Matrix.createRowMatrix(b.bases[0], b.bases[1], b.bases[2]);
		transform.set(m.getNormalizedCoordArray());
		transform.setScale(new Vector3d(b.extents[0]/2, b.extents[1]/2, b.extents[2]/2));
		transform.setTranslation(toJ3DVec(b.p.plus(
				b.bases[0].times(b.extents[0]/2)).plusIn( 
						b.bases[1].times(b.extents[1]/2)).plusIn(  
								b.bases[2].times(b.extents[2]/2)) ));

		((TransformGroup)shapeTransforms.get(b).getChild(0)).setTransform(transform);
	}
	private void updateTriangleTransforms(Triangle t){
		Transform3D transform = new Transform3D();
		Matrix m = Matrix.createColumnMatrix(t.p1.vectorTo(t.p2), t.p1.vectorTo(t.p3), t.getNormal());
		transform.set(m.getNormalizedCoordArray());
		transform.setTranslation(toJ3DVec(t.p1));

		((TransformGroup)shapeTransforms.get(t).getChild(0)).setTransform(transform);
	}
	private void updateTetrahedronTransforms(Tetrahedron t){
		Transform3D transform = new Transform3D();
		Matrix m = Matrix.createColumnMatrix(t.p1.vectorTo(t.p2), t.p1.vectorTo(t.p3), t.p1.vectorTo(t.p4));
		transform.set(m.getNormalizedCoordArray());
		transform.setTranslation(toJ3DVec(t.p1));

		((TransformGroup)shapeTransforms.get(t).getChild(0)).setTransform(transform);
	}
	private void updateCapsuleTransforms(Capsule c){
		Transform3D trans = new Transform3D();
		Vector v1 = new Vector(0,1,0);
		Vector v2 = c.p1.vectorTo(c.p2);
		trans.setScale(new Vector3d(c.rad, v2.length(), c.rad));
		if(v1.angle(v2)>0.00001){ 
			Matrix m = Matrix.createRotationMatrix(v1.angle(v2), v1.cross(v2).normIn());
			trans.set(m.getNormalizedCoordArray());
		}
		trans.setTranslation(toJ3DVec(c.p1.plus(c.p2).timesIn(0.5f)));
		

		((TransformGroup)shapeTransforms.get(c).getChild(0)).setTransform(trans);
		//shapeTransforms.get(c).setTransform(trans);
		//TransformGroup tg = shapeTransforms.get(c);
		TransformGroup tg = ((TransformGroup)shapeTransforms.get(c).getChild(0));
		
		BranchGroup bg = (BranchGroup)tg.getChild(0);
		
		trans = new Transform3D();
		trans.setScale(new Vector3d(1,c.rad/v2.length(),1));
		trans.setTranslation(new Vector3d(0,0.5,0));
		((TransformGroup)bg.getChild(0)).setTransform(trans);
		
		trans = new Transform3D();
		trans.rotX(Math.PI);
		trans.setTranslation(new Vector3d(0,-0.5,0));
		trans.setScale(new Vector3d(1,c.rad/v2.length(),1));
		((TransformGroup)bg.getChild(1)).setTransform(trans);
	}
	
	
	private Node genTriangle(Triangle t, Color color){
		Appearance app = new Appearance(); 

		Material mat = new Material();
		mat.setDiffuseColor(color.getRed()/255f,color.getGreen()/255f,color.getBlue()/255f);

		app.setMaterial(mat);

		if(color.getAlpha()<255){
			TransparencyAttributes ta = new TransparencyAttributes();
			ta.setTransparencyMode(TransparencyAttributes.NICEST);
			ta.setTransparency(1-color.getAlpha()/255f);
			app.setTransparencyAttributes(ta);
		}

		PolygonAttributes pa = new PolygonAttributes();
		pa.setCullFace(PolygonAttributes.CULL_NONE);
		pa.setBackFaceNormalFlip(true);
		app.setPolygonAttributes(pa);

		Triangle3D tri = new Triangle3D(1, app);

		TransformGroup tg = new TransformGroup();
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg.addChild(tri);
		//shapeTransforms.put(t, tg);

		BranchGroup ret = new BranchGroup();
		ret.addChild(tg);
		ret.setCapability(BranchGroup.ALLOW_DETACH);
		ret.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		ret.compile();
		shapeTransforms.put(t, ret);
		updateTriangleTransforms(t);
		return ret;
	}
	private Node genTetrahedron(Tetrahedron t, Color color){
		Appearance app = new Appearance(); 

		Material mat = new Material();
		mat.setDiffuseColor(color.getRed()/255f,color.getGreen()/255f,color.getBlue()/255f);
		app.setMaterial(mat);

		if(color.getAlpha()<255){
			TransparencyAttributes ta = new TransparencyAttributes();
			ta.setTransparencyMode(TransparencyAttributes.NICEST);
			ta.setTransparency(1-color.getAlpha()/255f);
			app.setTransparencyAttributes(ta);
		}

		PolygonAttributes pa = new PolygonAttributes();
		pa.setCullFace(PolygonAttributes.CULL_NONE);
		pa.setBackFaceNormalFlip(true);
		app.setPolygonAttributes(pa);

		List<Triangle> tList = new LinkedList<Triangle>();
		tList.add(new Triangle(new Vector(0,0,0),new Vector(1,0,0),new Vector(0,1,0)));//tList.add(new Triangle(t.p1,t.p2,t.p3));
		tList.add(new Triangle(new Vector(0,0,0),new Vector(0,0,1),new Vector(1,0,0)));//tList.add(new Triangle(t.p1,t.p4,t.p2));
		tList.add(new Triangle(new Vector(0,1,0),new Vector(0,0,0),new Vector(0,0,1)));//tList.add(new Triangle(t.p3,t.p1,t.p4));
		tList.add(new Triangle(new Vector(0,1,0),new Vector(1,0,0),new Vector(0,0,1)));//tList.add(new Triangle(t.p3,t.p2,t.p4));
		TriangleSet3D tset = new TriangleSet3D(tList, app);


		TransformGroup tg = new TransformGroup();
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg.addChild(tset);
		//shapeTransforms.put(t, tg);

		BranchGroup ret = new BranchGroup();
		ret.addChild(tg);
		ret.setCapability(BranchGroup.ALLOW_DETACH);
		ret.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		shapeTransforms.put(t, ret);
		updateTetrahedronTransforms(t);
		return ret;
	}
	private Node genCapsule(edu.geom3D.Capsule c, Color color){
		Appearance app = new Appearance(); 

		Material mat = new Material();
		mat.setDiffuseColor(color.getRed()/255f,color.getGreen()/255f,color.getBlue()/255f);
		app.setMaterial(mat);

		if(color.getAlpha()<255){
			TransparencyAttributes ta = new TransparencyAttributes();
			ta.setTransparencyMode(TransparencyAttributes.NICEST);
			ta.setTransparency(1-color.getAlpha()/255f);
			app.setTransparencyAttributes(ta);
		}

		//Cylinder cyl = new Cylinder(c.rad, c.p1.distance(c.p2), app);
		//Capsule3D cyl = new Capsule3D(c.p1.distance(c.p2), c.rad, app);
		//Capsule3D cyl = new Capsule3D(1, 1, app);
		BranchGroup capsGroup = new BranchGroup();
		capsGroup.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		
		//First hemisphere
		Transform3D trans = new Transform3D();
		trans.setTranslation(new Vector3d(0,0.5,0));
		TransformGroup tg  = new TransformGroup(trans);
		tg.addChild(new Hemisphere3D(1, app));
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		capsGroup.addChild(tg);
		
		//Second hemisphere
		trans = new Transform3D();
		trans.rotX(Math.PI);
		trans.setTranslation(new Vector3d(0,-0.5, 0));
		tg = new TransformGroup(trans);
		tg.addChild(new Hemisphere3D(1,app));
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		capsGroup.addChild(tg);
		
		capsGroup.addChild(new HollowCylinder3D(1,1,app));
		

		TransformGroup tg1 = new TransformGroup();
		tg1.addChild(capsGroup);
		tg1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg1.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
		//shapeTransforms.put(c, tg1);

		BranchGroup ret = new BranchGroup();
		ret.addChild(tg1);
		ret.setCapability(BranchGroup.ALLOW_DETACH);
		ret.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		shapeTransforms.put(c, ret);

		updateCapsuleTransforms(c);
		return ret;
	}
	private Node genCylinder(edu.geom3D.Cylinder c, Color color){
		Appearance app = new Appearance(); 

		Material mat = new Material();
		mat.setDiffuseColor(color.getRed()/255f,color.getGreen()/255f,color.getBlue()/255f);
		app.setMaterial(mat);

		if(color.getAlpha()<255){
			TransparencyAttributes ta = new TransparencyAttributes();
			ta.setTransparencyMode(TransparencyAttributes.NICEST);
			ta.setTransparency(1-color.getAlpha()/255f);
			app.setTransparencyAttributes(ta);
		}

		//Cylinder cyl = new Cylinder(c.rad, c.p1.distance(c.p2), app);
		Cylinder cyl = new Cylinder(1, 1, app);
		

		
		TransformGroup tg1 = new TransformGroup();
		tg1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg1.addChild(cyl);
		//shapeTransforms.put(c, tg1);

		BranchGroup ret = new BranchGroup();
		ret.addChild(tg1);
		ret.setCapability(BranchGroup.ALLOW_DETACH);
		ret.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		shapeTransforms.put(c, ret);
		updateCylinderTransforms(c);
		return ret;
	}
	private Node genCone(edu.geom3D.Cone c, Color color){
		Appearance app = new Appearance(); 

		Material mat = new Material();
		mat.setDiffuseColor(color.getRed()/255f,color.getGreen()/255f,color.getBlue()/255f);
		app.setMaterial(mat);

		if(color.getAlpha()<255){
			TransparencyAttributes ta = new TransparencyAttributes();
			ta.setTransparencyMode(TransparencyAttributes.NICEST);
			ta.setTransparency(1-color.getAlpha()/255f);
			app.setTransparencyAttributes(ta);
		}

		//Cylinder cyl = new Cylinder(c.rad, c.p1.distance(c.p2), app);
		Cone cyl = new Cone(1, 1, app);
		

		
		TransformGroup tg1 = new TransformGroup();
		tg1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg1.addChild(cyl);
		//shapeTransforms.put(c, tg1);

		BranchGroup ret = new BranchGroup();
		ret.addChild(tg1);
		ret.setCapability(BranchGroup.ALLOW_DETACH);
		ret.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		ret.compile();
		shapeTransforms.put(c, ret);
		updateConeTransforms(c);
		return ret;
	}
	private Node genSphere(edu.geom3D.Sphere s, Color color){
		Appearance app = new Appearance(); 

		Material mat = new Material();
		mat.setDiffuseColor(color.getRed()/255f,color.getGreen()/255f,color.getBlue()/255f);
		mat.setShininess(100f);
		mat.setSpecularColor(1, 0,0);
		app.setMaterial(mat);

		if(color.getAlpha()<255){
			TransparencyAttributes ta = new TransparencyAttributes();
			ta.setTransparencyMode(TransparencyAttributes.NICEST);
			ta.setTransparency(1-color.getAlpha()/255f);
			app.setTransparencyAttributes(ta);
		}

		com.sun.j3d.utils.geometry.Sphere sphere = 
			new com.sun.j3d.utils.geometry.Sphere(
					1,
					com.sun.j3d.utils.geometry.Sphere.GENERATE_NORMALS, 
					32,
					app);

		/*Transform3D trans = new Transform3D();
		trans.setTranslation(toJ3DVec(s.center));
		trans.setScale(s.radius);*/

		//TransformGroup tg1 = new TransformGroup(trans);
		TransformGroup tg1 = new TransformGroup();
		tg1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg1.addChild(sphere);
		//shapeTransforms.put(s, tg1);

		BranchGroup ret = new BranchGroup();
		ret.addChild(tg1);
		ret.setCapability(BranchGroup.ALLOW_DETACH);
		ret.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		shapeTransforms.put(s, ret);
		updateSphereTransforms(s);
		return ret;
	}
	private Node genBox(Box b, Color color) {
		Appearance app = new Appearance(); 

		Material mat = new Material();
		mat.setDiffuseColor(color.getRed()/255f,color.getGreen()/255f,color.getBlue()/255f);
		app.setMaterial(mat);

		if(color.getAlpha()<255){
			TransparencyAttributes ta = new TransparencyAttributes();
			ta.setTransparencyMode(TransparencyAttributes.NICEST);
			ta.setTransparency(1-color.getAlpha()/255f);
			app.setTransparencyAttributes(ta);
		}

		//com.sun.j3d.utils.geometry.Box box = new com.sun.j3d.utils.geometry.Box(v.extents[0]/2,v.extents[1]/2,v.extents[2]/2, app);
		com.sun.j3d.utils.geometry.Box box = new com.sun.j3d.utils.geometry.Box(1,1,1, app);

		
		TransformGroup tg = new TransformGroup();
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg.addChild(box);
		//shapeTransforms.put(b, tg);
		
		BranchGroup ret = new BranchGroup();
		ret.addChild(tg);
		ret.setCapability(BranchGroup.ALLOW_DETACH);
		ret.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		shapeTransforms.put(b, ret);
		updateBoxTransforms(b);
		return ret;
	}
	private Node genPrimitive(Shape v, Color c){
		if(v instanceof edu.geom3D.Cylinder)	return genCylinder((edu.geom3D.Cylinder)v, c);
		if(v instanceof edu.geom3D.Cone)	return genCone((edu.geom3D.Cone)v, c);
		else if(v instanceof Box)			return genBox((Box)v, c);
		else if(v instanceof Sphere)		return genSphere((Sphere)v, c);
		else if(v instanceof Capsule)		return genCapsule((Capsule)v, c);
		else if(v instanceof Triangle)		return genTriangle((Triangle)v, c);
		else if(v instanceof Tetrahedron)	return genTetrahedron((Tetrahedron)v, c);
		else if(v instanceof TextShape)		return genText((TextShape)v, c);
		else{ System.err.println("Warning: unknown primitive: "+v.getClass().getName()); return null; }
	}
	private Node genText(TextShape t, Color c){
		//Color c = new Color(255-c.getRed(), 255-c.getGreen(), 255-c.getBlue());
		Shape3D text3D = new Text2D(t.text, new Color3f(c),"Arial",48, Font.BOLD );

		TransformGroup subTg = new TransformGroup();
		subTg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		Billboard billboard = new Billboard(subTg);
		billboard.setSchedulingBounds( bounds );
		subTg.addChild( billboard );

		TransformGroup tg = new TransformGroup();
		tg.addChild(subTg);
		subTg.addChild(text3D);
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		//shapeTransforms.put(t, tg);
		
		BranchGroup ret = new BranchGroup();
		ret.addChild(tg);
		ret.setCapability(BranchGroup.ALLOW_DETACH);
		ret.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		shapeTransforms.put(t, ret);
		updateTextTransforms(t);
		return ret;
	}
	private Node genBackground() {
		background = new Background(new Color3f(Color.white)); 
		background.setCapability(Background.ALLOW_COLOR_WRITE);
		background.setCapability(Background.ALLOW_COLOR_READ);
		background.setApplicationBounds(bounds);
		return background;
	}
	private Node genLight(){
		Color3f light1Color = new Color3f(1f, 1f, 1f);
		Vector3f light1Direction = toJ3DVec(lightDir);
		DirectionalLight light1  = new DirectionalLight(light1Color, light1Direction);
		light1.setInfluencingBounds(bounds);
		return light1;
	}
	private Node genLight2(){
		Color3f light1Color = new Color3f(1f, 1f, 1f);
		Vector3f light1Direction = new Vector3f(0,-5f,5f);
		DirectionalLight light1  = new DirectionalLight(light1Color, light1Direction);
		light1.setInfluencingBounds(bounds);
		return light1;
	}
	private Node genAxis(){
		BranchGroup axisGroup = new BranchGroup();
		axisGroup.addChild(genPrimitive(new edu.geom3D.Cylinder(new Vector(0,0,0), new Vector(1,0,0), 0.05f), Color.RED.darker()));
		axisGroup.addChild(genPrimitive(new edu.geom3D.Cylinder(new Vector(0,0,0), new Vector(0,1,0), 0.05f), Color.GREEN.darker()));
		axisGroup.addChild(genPrimitive(new edu.geom3D.Cylinder(new Vector(0,0,0), new Vector(0,0,1), 0.05f), Color.BLUE.darker()));
		return axisGroup;
	}
	
	public void setAxisEnabled(boolean axisEnabled){
		if(axisEnabled && axisElements.isEmpty()){
			float rad = 0.03f;
			axisElements.add(new edu.geom3D.Cylinder(new Vector(0,0,0),new Vector(1-2*rad,0,0), rad));
			axisElements.add(new edu.geom3D.Cylinder(new Vector(0,0,0),new Vector(0,1-2*rad,0), rad));
			axisElements.add(new edu.geom3D.Cylinder(new Vector(0,0,0),new Vector(0,0,1-2*rad), rad));

			axisElements.add(new edu.geom3D.Cone(new Vector(1-2*rad,0,0),new Vector(1,0,0), 2*rad));
			axisElements.add(new edu.geom3D.Cone(new Vector(0,1-2*rad,0),new Vector(0,1,0), 2*rad));
			axisElements.add(new edu.geom3D.Cone(new Vector(0,0,1-2*rad),new Vector(0,0,1), 2*rad));

			axisElements.add(new TextShape("x", new Vector(1,0,0)));
			axisElements.add(new TextShape("y", new Vector(0,1,0)));
			axisElements.add(new TextShape("z", new Vector(0,0,1)));
		}
		if(axisEnabled){
			for(Shape s: axisElements) addShape(s, Color.GRAY);
		}else{
			for(Shape s: axisElements) removeShape(s);
		}
	}

	private BranchGroup buildScene(){
		/*
		BranchGroup scene = new BranchGroup();

		Transform3D transform = new Transform3D();
		transform.setTranslation(toJ3DVec(sceneCenter.times(-1f)));
		TransformGroup tg = new TransformGroup(transform);

		BranchGroup movedScene = new BranchGroup();

		for(Entry<Shape, Color> entry: primitives.entrySet())
			movedScene.addChild(genPrimitive(entry.getKey(), entry.getValue()));
		//for(TextPrimitive tp: texts) movedScene.addChild(genTextPrimitive(tp));
		movedScene.addChild(genLight());
		movedScene.addChild(genLight2());
		movedScene.addChild(genBackground());
		if(paintAxis) movedScene.addChild(genAxis());
		tg.addChild(movedScene);
		scene.addChild(tg);

		scene.setCapability(BranchGroup.ALLOW_DETACH);
		return scene;
		*/
		return null;
	}

	private void initialBuild(){
		sceneRoot = new BranchGroup();
		sceneRoot.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		TransformGroup tgroup = new TransformGroup();
		sceneRoot.addChild(tgroup);
		tgroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tgroup.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		tgroup.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		tgroup.setCapability(TransformGroup.ALLOW_CHILDREN_READ);


		camBehavior = new CamBehavior(tgroup);
		camBehavior.setSchedulingBounds(bounds);
		sceneRoot.addChild(camBehavior);

		rebuildBehavior = new RebuildBehavior(tgroup);
		rebuildBehavior.setSchedulingBounds(bounds);
		sceneRoot.addChild(rebuildBehavior);


		//BranchGroup scene = buildScene();
		//BranchGroup scene = new BranchGroup();

		Transform3D transform = new Transform3D();
		transform.setTranslation(toJ3DVec(sceneCenter.times(-1f)));
		TransformGroup tg = new TransformGroup(transform);
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

		scene = new BranchGroup();
		//scene.setCapability(BranchGroup.ALLOW_DETACH);
		scene.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		scene.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		
		for(Entry<Shape, Color> entry: primitives.entrySet())
			scene.addChild(genPrimitive(entry.getKey(), entry.getValue()));
		//for(TextPrimitive tp: texts) movedScene.addChild(genTextPrimitive(tp));
		scene.addChild(genLight());
		scene.addChild(genLight2());
		scene.addChild(genBackground());
		//if(paintAxis) scene.addChild(genAxis());
		tg.addChild(scene);
		//scene.addChild(tg);
		tgroup.addChild(tg);
		
		
		//tgroup.addChild(scene);
		//scene.compile();
		sceneRoot.compile();
	}

	public J3DGraphics(){
		this.initialBuild();
	}
	
	/** Get the canvas that displays the scene. If this method is called 
	 * several times the same <code>Canvas3D</code> object will be returned 
	 * every time.*/
	public Canvas3D getCanvas(){
		if(canvas==null){
			//initialBuild();

			GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
			canvas = new Canvas3D(config);
			SimpleUniverse universe = new SimpleUniverse(canvas);
			universe.addBranchGraph(sceneRoot);
			universe.getViewingPlatform().setNominalViewingTransform();
			CamListener cl = new CamListener();
			canvas.addMouseListener(cl);
			canvas.addMouseMotionListener(cl);
			canvas.addKeyListener(cl);
			canvas.addMouseWheelListener(cl);
		}

		return canvas;
	}

	/** Repaint the canvas. If the scene has been changed in any way the 
	 * scene displayer will update the view when <code>repaint()</code> is called 
	 * and no sooner. If the scene is repeatedly changed, and repaint repeatedly 
	 * called the viewer will show an animation. */
	public void repaint(){
		rebuildBehavior.rebuild();
	}

	/** Repaint the canvas repeatedly every <code>millisecondDelay</code> milliseconds. */
	public void repaintRepeatedly(long millisecondDelay){
		if(repaintTimer!=null){
			repaintTimer.cancel();
		}else{
			repaintTimer = new Timer();
		}
		class RepaintTask extends TimerTask{
			public void run() {
				repaint();
			}
		}
		repaintTimer.schedule(new RepaintTask(), 1, millisecondDelay);
	}

	private static Vector3f toJ3DVec(Vector v){ return new Vector3f(v.x(), v.y(), v.z() ); }

	private class CamListener extends MouseAdapter implements MouseMotionListener, MouseWheelListener, KeyListener{
		private boolean shiftPressed = false;
		private Point lastPoint = null;
		private long lastTime = System.currentTimeMillis();
		public void mousePressed(MouseEvent e) {	lastPoint = e.getPoint();		}
		public void mouseReleased(MouseEvent e) {	lastPoint = null; }
		public void mouseClicked(MouseEvent e){
			rebuildBehavior.rebuild();
		}

		public void mouseDragged(MouseEvent e) {
			if(lastPoint==null) {
				lastPoint = e.getPoint();
				lastTime = System.currentTimeMillis();
				return;
			}
			Point point = e.getPoint();
			float dX = point.x-lastPoint.x;
			float dY = point.y-lastPoint.y;
			float damper = Math.max(10, (float)(System.currentTimeMillis()-lastTime))*10f;

			if(shiftPressed){
				camBehavior.rotate(dX*(float)Math.PI/damper);
			}else{
				Vector delta = new Vector(dX, -dY, 0).timesIn(1/damper);
				camBehavior.translate(new Vector3d(delta.x(), delta.y(), delta.z()));
			}
			lastPoint = point;
			lastTime = System.currentTimeMillis();

		}

		public void mouseWheelMoved(MouseWheelEvent e){
			float damper = Math.max(10, (float)(System.currentTimeMillis()-lastTime))*10f;
			camBehavior.scale(e.getWheelRotation()/damper);
			lastTime = System.currentTimeMillis();
		}

		public void mouseMoved(MouseEvent e) {}
		public void keyPressed(KeyEvent e) {
			if( e.getKeyCode()==KeyEvent.VK_SHIFT )	shiftPressed = true;

			if(e.getKeyCode()==KeyEvent.VK_DOWN && shiftPressed){
				float damper = Math.max(10, (float)(System.currentTimeMillis()-lastTime));
				camBehavior.scale(10f/damper);
				lastTime = System.currentTimeMillis();
			}
			if(e.getKeyCode()==KeyEvent.VK_UP && shiftPressed){
				float damper = Math.max(10, (float)(System.currentTimeMillis()-lastTime));
				camBehavior.scale(-10f/damper);
				lastTime = System.currentTimeMillis();
			}
			if(e.getKeyCode()==KeyEvent.VK_LEFT && shiftPressed){
				camBehavior.rotate(0.1f);
			}
			if(e.getKeyCode()==KeyEvent.VK_RIGHT && shiftPressed){
				camBehavior.rotate(-0.1f);
			}

			if(e.getKeyCode()==KeyEvent.VK_UP && !shiftPressed){
				camBehavior.translate(new Vector3d(0,-0.1,0));
			}
			if(e.getKeyCode()==KeyEvent.VK_DOWN && !shiftPressed){
				camBehavior.translate(new Vector3d(0,0.1,0));
			}
			if(e.getKeyCode()==KeyEvent.VK_LEFT && !shiftPressed){
				camBehavior.translate(new Vector3d(0.1,0,0));
			}
			if(e.getKeyCode()==KeyEvent.VK_RIGHT && !shiftPressed){
				camBehavior.translate(new Vector3d(-0.1,0,0));
			}
		}
		public void keyReleased(KeyEvent e) {
			if( e.getKeyCode()==KeyEvent.VK_SHIFT ) shiftPressed = false;
		}
		public void keyTyped(KeyEvent e) {}

	}

	private static class CamBehavior extends Behavior {

		private TransformGroup transformGroup;
		private Transform3D trans = new Transform3D();
		private WakeupCriterion criterion;
		private float yAngle = 0.0f;
		private Vector3d translation = new Vector3d(0,0,0);
		private float scale = 1f;



		private final int ROTATE = 1;

		// create a new RotateBehavior
		CamBehavior(TransformGroup tg) {	transformGroup = tg;	}

		// initialize behavior to wakeup on a behavior post with id = ROTATE
		public void initialize() {
			criterion = new WakeupOnBehaviorPost(this, ROTATE);
			wakeupOn(criterion);
		}

		// processStimulus to rotate the cube
		public void processStimulus(Enumeration criteria) {
			trans.rotY(yAngle);
			trans.setTranslation(translation);
			trans.setScale(scale);
			transformGroup.setTransform(trans);
			wakeupOn(criterion);
		}

		// when the mouse is clicked, postId for the behavior
		void rotate(float dY) {
			yAngle+=dY;
			postId(ROTATE);
		}
		void translate(Vector3d delta){
			translation.add(delta);
			postId(ROTATE);
		}
		void scale(float s){
			scale-=s;
			if(scale<=0.001) scale=0.001f;
			postId(ROTATE);
		}
	}
	private class RebuildBehavior extends Behavior {
		private boolean rebuilding = false;
		private TransformGroup tgroup;
		private WakeupCriterion criterion;

		private final int REBUILD = 5;

		// create a new RotateBehavior
		RebuildBehavior(TransformGroup tgroup) {	this.tgroup = tgroup;	}

		// initialize behavior to wakeup on a behavior post with id = ROTATE
		public void initialize() {
			criterion = new WakeupOnBehaviorPost(this, REBUILD);
			wakeupOn(criterion);
		}

		public void processStimulus(Enumeration criteria) {
			/*if(sceneChangedStructurally){
				tgroup.removeAllChildren();
				BranchGroup scene = buildScene();
				tgroup.addChild(scene);
				sceneChangedStructurally = false;
			}else{
			*/
				for(Entry<Shape, BranchGroup> entry: shapeTransforms.entrySet()){
					updateTransforms(entry.getKey());
				}
            /*
			}
			*/
			wakeupOn(criterion);
			rebuilding = false;
		}

		// when the mouse is clicked, postId for the behavior
		synchronized void rebuild() {
			if(rebuilding) return;
			rebuilding = true;
			postId(REBUILD);
		}
	}

	public static void main(String[] args) {
		JFrame f = new JFrame("J3DTest");
		f.setSize(600,600);
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);

		J3DGraphics j3dg = new J3DGraphics();
		j3dg.addShape(new edu.geom3D.Cylinder(new Vector(0,1,0), new Vector(0,1,1), 0.1f), new Color(20, 20, 200, 200));
		Box box = new Box(
				new Vector(0,0,0), 
				new Vector[]{(new Vector(-1,-1,0)).normIn(),(new Vector(-1,1,0)).normIn(), new Vector(0,0,-1).normIn()}, 
				new float[]{0.5f, 0.5f, 1f});
		j3dg.addShape(box);

		/*float x = -1f, y = 0.7f, dY = 0.08f; 
		int c=0;
		j3dg.addText("JScene-viewer", new Vector(x, y-(c++)*dY, 0.2f));
		j3dg.addText("import edu.jScene.*;", new Vector(x, y-(c++)*dY, 0.2f));
		j3dg.addText("import edu.math.*;", new Vector(x, y-(c++)*dY, 0.2f));
		j3dg.addText("import edu.geom3D.*;", new Vector(x, y-(c++)*dY, 0.2f));
		j3dg.addText("...", new Vector(x, y-(c++)*dY, 0.2f));
		j3dg.addText("J3DGraphics j3dg = J3DGraphics.createJ3DGraphicsInFrame();", new Vector(x, y-(c++)*dY, 0.2f));
		j3dg.addText("j3dg.addVolume( new Cylinder(new Vector(1,0,0), new Vector(0,1,0), 0.1f) );", new Vector(x, y-(c++)*dY, 0.2f));
		j3dg.addText("j3dg.addText( \"Test text\", new Vector(1,1,0) );", new Vector(x, y-(c++)*dY, 0.2f));
		j3dg.addText("j3dg.repaint();", new Vector(x, y-(c++)*dY, 0.2f));
		*/

		j3dg.setAxisEnabled(true);
		Canvas3D canvas = j3dg.getCanvas();
		f.getContentPane().add(canvas);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		j3dg.setBackgroundColor(Color.BLACK);
		j3dg.addShape(new edu.geom3D.Capsule(new Vector(1,0,0), new Vector(1,1,0), 0.1f), new Color(20,200,20, 100));
		edu.geom3D.Cylinder cyl = new edu.geom3D.Cylinder(new Vector(0.4,0,0.1), new Vector(0.4,0.5,0), 0.1f);
		j3dg.addShape(cyl);
		Sphere s = new Sphere(new Vector(-1,-0.2,0), 0.3f);
		j3dg.addShape(s, Color.MAGENTA);
		Tetrahedron tetr = new Tetrahedron(new Vector(0,0,1), new Vector(-0.5,0,1), new Vector(-0.5,0.5,1), new Vector(-0.25,0.25, 1.25));
		j3dg.addShape(tetr, new Color(50,50,255, 100));
		
		j3dg.addShape(new Triangle(new Vector(0.2f, -0.2f, 0.1f), new Vector(0.8, -0.8, 0.1), new Vector(1,-0.3, 0.1)), new Color(255,50,255, 100));

		//j3dg.setColor(box, Color.BLUE);
		j3dg.centerCamera();
		j3dg.removeShape(tetr);
		/*
		j3dg.repaintRepeatedly(30);

		Timer t = new Timer();
		class MoveSphere extends TimerTask{
			Tetrahedron t;
			J3DGraphics g;
			public void run() {
				t.p1 = t.p1.plus(new Vector(0.001f,0,0));
			}
		}
		MoveSphere tt = new MoveSphere();
		tt.t = tetr;
		tt.g = j3dg;
		t.schedule(tt, 100, 10);*/
	}

	/** Create a frame containing a canvas, display it and return the  
	 * J3DGraphics object shown in the frame. */
	public static J3DGraphics createJ3DGraphicsInFrame() {
		JFrame f = new JFrame("JScene-viewer");
		f.setSize(600,600);
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);

		J3DGraphics j3dg = new J3DGraphics();
		Canvas3D canvas = j3dg.getCanvas();
		f.getContentPane().add(canvas);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		return j3dg;
	}

}
