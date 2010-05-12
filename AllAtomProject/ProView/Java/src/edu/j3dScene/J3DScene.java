package edu.j3dScene;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.geometry.Cone;
import com.sun.j3d.utils.geometry.Cylinder;
import com.sun.j3d.utils.geometry.Text2D;
import com.sun.j3d.utils.universe.SimpleUniverse;

import edu.geom3D.Box;
import edu.geom3D.Capsule;
import edu.geom3D.RSS;
import edu.geom3D.Shape;
import edu.geom3D.Sphere;
import edu.geom3D.Tetrahedron;
import edu.geom3D.Triangle;
import edu.math.Matrix;
import edu.math.Vector;

/** A graphics class for viewing scenes using Java3D. 
 * All the <code>Shape</code>-subclasses specified in the <code>edu.geom3D</code> 
 * package can be added to a <code>J3DScene</code> object and are automatically 
 * painted on a <code>Canvas3D</code> object. For 
 * instance the following code creates a scene with a cylinder and a red 
 * transparent box and adds the canvas to a frame. 
 * <pre>
 * J3DScene scene = new J3DScene();
 * scene.addShape(  new Cylinder(new Vector(1,0,0), new Vector(0.5,0.5, 0.3), 0.1f) );
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
 * </pre>
 * Text can be added to the scene as well and will always face the camera. 
 * 
 * The <code>repaint()</code> method must be called every time the position of 
 * shapes has changed and the canvas should be updated. The pointers 
 * to added shapes are stored, so subsequent changes in the <code>box</code> 
 * object in the above code will be visible on the canvas when <code>repaint()</code> 
 * is called. The following example shows how to animate a sphere rotating around origo.
 * <pre>
 *  J3DScene scene = new J3DScene();
 *  Sphere sphere = new Sphere( new Vector(1,0,0), 0.1f); 
 *  scene.addShape(sphere);
 *  float t = 0;
 *  while(true){
 * 		t+=0.01f;
 * 		sphere.center = new Vector(Math.cos(t), Math.sin(t), 0);
 * 		scene.repaint();
 * 		try{ Thread.sleep(30); }catch(InterruptedException exc){}
 *  }
 * </pre>
 * 
 * A static method is supplied for conveniently creating a frame containing a scene-viewer. 
 * The following example shows how to quickly create a <code>J3DScene</code> object 
 * that is shown in a frame and ready for use:
 * <pre>
 * J3DScene scene = J3DScene.createJ3DSceneInFrame();
 * scene.setAxisEnabled(true);
 * scene.addShape(  new Cylinder(new Vector(1,0,0), new Vector(0,1,0), 0.1f) );
 * </pre>
 * @author R. Fonseca
 */
public class J3DScene {
	private Canvas3D canvas;
	private BranchGroup sceneRoot, scene;
	private CamBehavior camBehavior;
	private RebuildBehavior rebuildBehavior;
	private OrbitBehavior orbitBehavior;
	private Timer repaintTimer;

	private final BoundingSphere bounds = new BoundingSphere(new Point3d(0,0,0), 5000);
	private Background background;

	private final Map<Shape,BranchGroup> shapeTransforms = new HashMap<Shape,BranchGroup>();
	private final Map<Shape,Color> primitives = new HashMap<Shape,Color>();
//	private Vector lightDir = new Vector(0,-1,-5);
	private Vector sceneCenter = new Vector(0,0,0);
	private final List<Shape> axisElements = new ArrayList<Shape>();


	/** Set color of background. */
	public void setBackgroundColor(Color c){
		background.setColor(c.getRed()/255f, c.getGreen()/255f, c.getBlue()/255f);
	}

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
	/** Add a text-object at the specified position. */
	public void addText(String t, Vector pos){ 
		addShape(new TextShape(t,pos), Color.GRAY); 
	}
	public void addText(String t, Vector pos, float height){ 
		addShape(new TextShape(t,pos,height), Color.GRAY); 
	}
	public void addText(String t, Vector pos, float height, Color c){
		addShape(new TextShape(t,pos,height), c);
	}

	/** Sets the location that the camera looks at to the center of all the shapes added 
	 * to the scene.  */
	public void centerCamera(){
		Vector sceneCenter = Vector.O().clone();
		for(Entry<Shape, Color> entry: primitives.entrySet()){
			try{
				sceneCenter.plusIn(entry.getKey().getCenter());
			}catch(Error e){};
		}
		
		sceneCenter.timesIn(1f/primitives.entrySet().size());

		Transform3D transform = new Transform3D();
		transform.setTranslation(toJ3DVec(sceneCenter.times(-1f)));
		TransformGroup tg = ((TransformGroup)((TransformGroup)sceneRoot.getChild(0)).getChild(0));
		tg.setTransform(transform);

	}

	/** Zooms such that the maximal distance between two objects is within the view */
	public void autoZoom(){
		float maxDist = 0;
		for(Entry<Shape, Color> entry: primitives.entrySet()){
			for(Entry<Shape, Color> entry2: primitives.entrySet()){
				float d = entry.getKey().getCenter().distance(entry2.getKey().getCenter());
				if(d>maxDist) maxDist=d;
			}
		}
		if(maxDist>0){
			this.camBehavior.setScale(2/(maxDist+10));
			this.repaint();
		}
	}

	private boolean parallelProjection = false;
	/** Enables and disables parallel projection (as opposed to perspective projection). */
	public void setParallelProjection(boolean enabled) {
		if(enabled && !parallelProjection){
			canvas.getView().setProjectionPolicy(View.PARALLEL_PROJECTION);
		}
		if(!enabled && parallelProjection){
			canvas.getView().setProjectionPolicy(View.PERSPECTIVE_PROJECTION);
		}
		parallelProjection = enabled;
	}


	private class RotThread extends Thread {
		boolean stop = false;
		int axis;
		RotThread(int axis){
			this.axis = axis;
		}
		public void run() {
			stop = false;
			while(!stop){
				camBehavior.rotate(0.01f,axis);
				try {Thread.sleep(40);} catch (InterruptedException e) {	}
			}
		}
	}
	private RotThread rotThread;
	/** 
	 * Toggles rotation 
	 * @param i Axis to rotate around (0=x, 1=y, 2=z) 
	 */
	private void toggleRotation(int i){
		if(i!=1) return;
		//Thread t = new RotThread();
		//t.start();
		if(rotThread!=null && rotThread.isAlive()){
			rotThread.stop = true;
			rotThread = null;
		}else{
			rotThread = new RotThread(i);
			rotThread.start();
		}
	}
	public void toggleRotation(){
		toggleRotation(1);
	}


	private void updateTransforms(Shape v){
		if(v instanceof Sphere) updateSphereTransforms((Sphere)v);
		if(v instanceof edu.geom3D.Cylinder) updateCylinderTransforms((edu.geom3D.Cylinder)v);
		if(v instanceof edu.geom3D.Cone) updateConeTransforms((edu.geom3D.Cone)v);
		if(v instanceof TextShape) updateTextTransforms((TextShape)v);
		if(v instanceof Box) updateBoxTransforms((Box)v);
		if(v instanceof RSS) updateRSSTransforms((RSS)v);
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
		if(b.bases[0].cross(b.bases[1]).dot(b.bases[2])<0){
			Matrix m = Matrix.createColumnMatrix(b.bases[0], b.bases[2], b.bases[1]);
			transform.set(m.getNormalizedCoordArray());
			transform.setScale(new Vector3d(b.extents[0]/2, b.extents[2]/2, b.extents[1]/2));
			transform.setTranslation(toJ3DVec(b.p.plus(
					b.bases[0].times(b.extents[0]/2)).plusIn( 
							b.bases[2].times(b.extents[2]/2)).plusIn(  
									b.bases[1].times(b.extents[1]/2)) ));
		}else{
			Matrix m = Matrix.createColumnMatrix(b.bases[0], b.bases[1], b.bases[2]);
			transform.set(m.getNormalizedCoordArray());
			transform.setScale(new Vector3d(b.extents[0]/2, b.extents[1]/2, b.extents[2]/2));
			transform.setTranslation(toJ3DVec(b.p.plus(
					b.bases[0].times(b.extents[0]/2)).plusIn( 
							b.bases[1].times(b.extents[1]/2)).plusIn(  
									b.bases[2].times(b.extents[2]/2)) ));
		}

		//transform.setTranslation(toJ3DVec(b.p));
		((TransformGroup)shapeTransforms.get(b).getChild(0)).setTransform(transform);
//		if(b.bases[0].cross(b.bases[1]).dot(b.bases[2])>0){
//			Matrix m = Matrix.create4x4ColumnMatrix(
//					b.bases[0].times(b.extents[0]), 
//					b.bases[2].times(b.extents[2]),
//					b.bases[1].times(b.extents[1]),
//					new Vector(0,0,0) );
//			transform.set(m.getDoubleCoordArray());
//			transform.setTranslation(toJ3DVec(b.getCenter()));
//		}else{
//			Matrix m = Matrix.create4x4ColumnMatrix(
//					b.bases[0].times(b.extents[0]), 
//					b.bases[1].times(b.extents[1]),
//					b.bases[2].times(b.extents[2]),
//					new Vector(0,0,0) );
//			transform.set(m.getDoubleCoordArray());
//			transform.setTranslation(toJ3DVec(b.getCenter()));
//		}
//
//		//transform.setTranslation(toJ3DVec(b.p));
//		((TransformGroup)shapeTransforms.get(b).getChild(0)).setTransform(transform);
	}
	private void updateRSSTransforms(RSS r){
//		if(true) return;
		Transform3D trans = new Transform3D();
		Vector v2 = r.rectangle.bases[0];
		Vector v3 = r.rectangle.bases[1];
		double width = v2.length()*2;
		double height = v3.length()*2;
		double radius = r.radius;
		Matrix m = Matrix.create4x4ColumnMatrix(v2.norm(),v3.norm(),v2.cross(v3).norm(), Vector.O());
		trans.set(m.getCoordArray());
		trans.setScale(new Vector3d(width, height, radius));
		trans.setTranslation(toJ3DVec(r.getCenter()));


		((TransformGroup)shapeTransforms.get(r).getChild(0)).setTransform(trans);
		TransformGroup tg = ((TransformGroup)shapeTransforms.get(r).getChild(0));

		BranchGroup bg = (BranchGroup)tg.getChild(0);

		
		trans = new Transform3D();
		trans.setScale(new Vector3d(radius/width,radius/height,1));
		trans.setTranslation(new Vector3d(0.5,0.5,0));
		((TransformGroup)bg.getChild(0)).setTransform(trans);

		trans = new Transform3D();
		trans.rotZ(Math.PI/2);
		trans.setTranslation(new Vector3d(-0.5,0.5,0));
		trans.setScale(new Vector3d(radius/height,radius/width,1));
		((TransformGroup)bg.getChild(1)).setTransform(trans);
		
		trans = new Transform3D();
		trans.rotZ(Math.PI);
		trans.setTranslation(new Vector3d(-0.5,-0.5,0));
		trans.setScale(new Vector3d(radius/width,radius/height,1));
		((TransformGroup)bg.getChild(2)).setTransform(trans);

		trans = new Transform3D();
		trans.rotZ(3*Math.PI/2);
		trans.setTranslation(new Vector3d(0.5,-0.5,0));
		trans.setScale(new Vector3d(radius/height,radius/width,1));
		((TransformGroup)bg.getChild(3)).setTransform(trans);
		
		//Half-cylinders
		trans = new Transform3D();
//		trans.rotZ(3*Math.PI/2);
		trans.setTranslation(new Vector3d(0.5,0,0));
		trans.setScale(new Vector3d(radius/width,1,1));
		((TransformGroup)bg.getChild(4)).setTransform(trans);

		trans = new Transform3D();
		trans.rotZ(Math.PI/2);
		trans.setTranslation(new Vector3d(0,0.5,0));
		trans.setScale(new Vector3d(radius/height,1,1));
		((TransformGroup)bg.getChild(5)).setTransform(trans);

		trans = new Transform3D();
		trans.rotZ(Math.PI);
		trans.setTranslation(new Vector3d(-0.5,0,0));
		trans.setScale(new Vector3d(radius/width,1,1));
		((TransformGroup)bg.getChild(6)).setTransform(trans);

		trans = new Transform3D();
		trans.rotZ(3*Math.PI/2);
		trans.setTranslation(new Vector3d(0,-0.5,0));
		trans.setScale(new Vector3d(radius/height,1,1));
		((TransformGroup)bg.getChild(7)).setTransform(trans);
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
		Vector v2 = null;
		try{
			v2 = c.p1.vectorTo(c.p2);
		}catch(NullPointerException exc){
			System.out.println(c);
			throw exc;
		}
		float angle = 0;
		if(v1.length()>0 && v2.length()>0 && (angle=v1.angle(v2))>0.00001 && angle<Math.PI-0.00001){ 
			Matrix m = Matrix.createRotationMatrix(angle, v1.cross(v2).normIn());
			trans.set(m.getNormalizedCoordArray());
		}
		if(v2.length()<0.00001) v2.setX(0.001f);
		trans.setScale(new Vector3d(c.rad, v2.length(), c.rad));
		trans.setTranslation(toJ3DVec(c.p1.plus(c.p2).timesIn(0.5f)));


		((TransformGroup)shapeTransforms.get(c).getChild(0)).setTransform(trans);
		//shapeTransforms.get(c).setTransform(trans);
		//TransformGroup tg = shapeTransforms.get(c);
		TransformGroup tg = ((TransformGroup)shapeTransforms.get(c).getChild(0));

		BranchGroup bg = (BranchGroup)tg.getChild(0);

		trans = new Transform3D();
		trans.setScale(new Vector3d(1,c.rad/v2.length(),1));
		//trans.setScale(new Vector3d(1,1,1));
		trans.setTranslation(new Vector3d(0,0.5,0));
		((TransformGroup)bg.getChild(0)).setTransform(trans);

		trans = new Transform3D();
		trans.rotX(Math.PI);
		trans.setTranslation(new Vector3d(0,-0.5,0));
		trans.setScale(new Vector3d(1,c.rad/v2.length(),1));
		//trans.setScale(new Vector3d(1,1,1));
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
		ret.compile();
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
		Cylinder cyl = new Cylinder(1, 1,Cylinder.GENERATE_NORMALS, 32, 1, app);



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
	private Node genRSS(RSS r, Color color){
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
		float delta = 0.000f;
		
		BranchGroup capsGroup = new BranchGroup();
		capsGroup.setCapability(BranchGroup.ALLOW_CHILDREN_READ);

		//Quarterspheres
		Transform3D trans = new Transform3D();
		trans.setTranslation(new Vector3d(0.5,0.5,0));
		TransformGroup tg  = new TransformGroup(trans);
		tg.addChild(new QuarterSphere3D(1, app));
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		capsGroup.addChild(tg);

		trans = new Transform3D();
		trans.rotZ(Math.PI/2);
		trans.setTranslation(new Vector3d(-0.5,0.5, 0));
		tg = new TransformGroup(trans);
		tg.addChild(new QuarterSphere3D(1,app));
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		capsGroup.addChild(tg);
		
		trans = new Transform3D();
		trans.rotZ(Math.PI);
		trans.setTranslation(new Vector3d(-0.5,-0.5, 0));
		tg = new TransformGroup(trans);
		tg.addChild(new QuarterSphere3D(1,app));
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		capsGroup.addChild(tg);

		trans = new Transform3D();
		trans.rotZ(3*Math.PI/2);
		trans.setTranslation(new Vector3d(0.5,-0.5, 0));
		tg = new TransformGroup(trans);
		tg.addChild(new QuarterSphere3D(1,app));
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		capsGroup.addChild(tg);

		//Half-cylinders
		trans = new Transform3D();
		//trans.rotZ(3*Math.PI/2);
//		trans.setTranslation(new Vector3d(0.5,0, 0));
		tg = new TransformGroup(trans);
		tg.addChild(new HalfCylinder3D(1,1,app));
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		capsGroup.addChild(tg);

		trans = new Transform3D();
		tg = new TransformGroup(trans);
		tg.addChild(new HalfCylinder3D(1,1,app));
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		capsGroup.addChild(tg);

		trans = new Transform3D();
		tg = new TransformGroup(trans);
		tg.addChild(new HalfCylinder3D(1,1,app));
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		capsGroup.addChild(tg);

		trans = new Transform3D();
		tg = new TransformGroup(trans);
		tg.addChild(new HalfCylinder3D(1,1,app));
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		capsGroup.addChild(tg);
		
		//Bounding planes
		trans = new Transform3D();
		trans.setTranslation(new Vector3d(0,0,1));
		tg = new TransformGroup(trans);
		tg.addChild(new Rectangle3D(1+delta,1+delta,app));
		capsGroup.addChild(tg);

		trans = new Transform3D();
		trans.rotX(Math.PI);
		trans.setTranslation(new Vector3d(0,0,-1));
		tg = new TransformGroup(trans);
		tg.addChild(new Rectangle3D(1+delta,1+delta,app));
		capsGroup.addChild(tg);

		
		
		TransformGroup tg1 = new TransformGroup();
		tg1.addChild(capsGroup);
		tg1.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg1.setCapability(TransformGroup.ALLOW_CHILDREN_READ);
		//shapeTransforms.put(c, tg1);

		BranchGroup ret = new BranchGroup();
		ret.addChild(tg1);
		ret.setCapability(BranchGroup.ALLOW_DETACH);
		ret.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
		shapeTransforms.put(r, ret);

		updateRSSTransforms(r);
		return ret;
	}
	private Node genPrimitive(Shape v, Color c){
		if(v instanceof edu.geom3D.Cylinder)	return genCylinder((edu.geom3D.Cylinder)v, c);
		if(v instanceof edu.geom3D.Cone)	return genCone((edu.geom3D.Cone)v, c);
		else if(v instanceof Box)			return genBox((Box)v, c);
		else if(v instanceof RSS)			return genRSS((RSS)v, c);
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
		Color3f light1Color = new Color3f(1f,1f,1f);
		Vector3f light1Direction = new Vector3f(-1f,-1f,-1f);
		DirectionalLight light1  = new DirectionalLight(light1Color, light1Direction);
		light1.setInfluencingBounds(bounds);
		return light1;
	}
	private Node genLight2(){
		Color3f lightColor = new Color3f(1f,1f,1f);
		Vector3f lightDirection = new Vector3f(1f,1f,1f);
		DirectionalLight light  = new DirectionalLight(lightColor, lightDirection);
		light.setInfluencingBounds(bounds);
		return light;
	}
	private Node genLight3(){
		Color3f lightColor = new Color3f(1f, 1f, 1f);
		AmbientLight light  = new AmbientLight(lightColor);
		light.setInfluencingBounds(bounds);
		return light;
	}

	private boolean axisEnabled = false;
	/** Enables or disables xyz-axis from the origo */
	public void setAxisEnabled(boolean axisEnabled){
		if(axisEnabled && axisElements.isEmpty()){
			float rad = 0.015f;
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
		if(axisEnabled && !this.axisEnabled){
			for(Shape s: axisElements) addShape(s, Color.GRAY);
		}
		if(!axisEnabled && this.axisEnabled){
			for(Shape s: axisElements) removeShape(s);
		}
		this.axisEnabled = axisEnabled;
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

		//rebuildBehavior = new RebuildBehavior(tgroup);
		rebuildBehavior = new RebuildBehavior();
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
		scene.addChild(genBackground());
		scene.addChild(genLight());
		scene.addChild(genLight3());
		scene.addChild(genLight2());
		//if(paintAxis) scene.addChild(genAxis());
		tg.addChild(scene);
		//scene.addChild(tg);
		tgroup.addChild(tg);


		//tgroup.addChild(scene);
		//scene.compile();
		sceneRoot.compile();
	}

	public J3DScene(){
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
			//universe.getViewer().getView().setProjectionPolicy(View.PARALLEL_PROJECTION);
			universe.addBranchGraph(sceneRoot);
			universe.getViewingPlatform().setNominalViewingTransform();

			CamListener cl = new CamListener();
			//canvas.addMouseListener(cl);
			//canvas.addMouseMotionListener(cl);

			orbitBehavior = new OrbitBehavior(canvas,
					OrbitBehavior.PROPORTIONAL_ZOOM | OrbitBehavior.REVERSE_ROTATE
					| OrbitBehavior.REVERSE_TRANSLATE );
			//orbitBehavior.setZoomFactor(1);
			orbitBehavior.setSchedulingBounds(bounds);    
			universe.getViewingPlatform().setViewPlatformBehavior(orbitBehavior);

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
				camBehavior.rotate(dX*(float)Math.PI/damper,1);
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
			//orbitBehavior.setZoomFactor(orbitBehavior.getZoomFactor()+e.getWheelRotation());
			//System.out.println(orbitBehavior.getZoomFactor());
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
				camBehavior.rotate(0.1f,1);
			}
			if(e.getKeyCode()==KeyEvent.VK_RIGHT && shiftPressed){
				camBehavior.rotate(-0.1f,1);
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
			if(e.getKeyCode()==KeyEvent.VK_S){
				edu.j3dScene.Toolbox.writeJPEGFile("J3DScene.jpg", canvas);
				System.out.println("Stored view to J3DScene.jpg");
			}
			if(e.getKeyCode()==KeyEvent.VK_E){
				edu.j3dScene.Toolbox.writeEPSFile("J3DScene.eps", canvas);
				System.out.println("Stored view to J3DScene.eps");
			}
			if(e.getKeyCode()==KeyEvent.VK_C){
				J3DScene.this.centerCamera();
			}
			if(e.getKeyCode()==KeyEvent.VK_Z){
				J3DScene.this.autoZoom();
			}
			if(e.getKeyCode()==KeyEvent.VK_R){
				J3DScene.this.toggleRotation(1);
			}
			if(e.getKeyCode()==KeyEvent.VK_J){
				J3DScene.this.toggleRotation(0);
			}
			if(e.getKeyCode()==KeyEvent.VK_K){
				J3DScene.this.toggleRotation(1);
			}
			if(e.getKeyCode()==KeyEvent.VK_L){
				J3DScene.this.toggleRotation(2);
			}
			if(e.getKeyCode()==KeyEvent.VK_P){
				J3DScene.this.setParallelProjection(!parallelProjection);
			}
			if(e.getKeyCode()==KeyEvent.VK_A){
				J3DScene.this.setAxisEnabled(!axisEnabled);
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
		private float yAngle = 0.0f, xAngle = 0.0f, zAngle = 0.0f;
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
		@SuppressWarnings("unchecked")
		public void processStimulus(Enumeration criteria) {
//			trans.rotX(xAngle);
			trans.rotY(yAngle);
//			trans.rotZ(zAngle);
			trans.setTranslation(translation);
			trans.setScale(scale);
			transformGroup.setTransform(trans);
			wakeupOn(criterion);
			//System.out.println("Scale "+scale);
		}

		// when the mouse is clicked, postId for the behavior
		void rotate(float d, int axis) {
			switch(axis){
			case 0: xAngle+=d;break;
			case 1: yAngle+=d;break;
			case 2: zAngle+=d;break;
			}
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
		void setScale(float s){
			scale=s;
			if(scale<=0.001) scale=0.001f;
			postId(ROTATE);
		}
	}
	private class RebuildBehavior extends Behavior {
		private boolean rebuilding = false;
		//private TransformGroup tgroup;
		private WakeupCriterion criterion;

		private final int REBUILD = 5;

		// initialize behavior to wakeup on a behavior post with id = ROTATE
		public void initialize() {
			criterion = new WakeupOnBehaviorPost(this, REBUILD);
			wakeupOn(criterion);
		}

		@SuppressWarnings("unchecked")
		public void processStimulus(Enumeration criteria) {
			for(Entry<Shape, BranchGroup> entry: shapeTransforms.entrySet()){
				updateTransforms(entry.getKey());
			}
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
		J3DScene j3ds = createJ3DSceneInFrame();
		j3ds.addShape(new edu.geom3D.Cylinder(new Vector(0,1,0), new Vector(0,1,1), 0.1f), new Color(20, 20, 200, 200));
		Box box = new Box(
				new Vector(0,0,0), 
				new Vector[]{(new Vector(-1,-1,0)).normIn(),(new Vector(-1,1,0)).normIn(), new Vector(0,0,-1).normIn()}, 
				new float[]{0.5f, 0.5f, 1f});
		j3ds.addShape(box, Color.GREEN.darker());

		j3ds.setAxisEnabled(true);

		j3ds.setBackgroundColor(Color.WHITE);
		j3ds.addShape(new edu.geom3D.Capsule(new Vector(1,1,0), new Vector(3,0,0), 0.1f), new Color(20,200,20, 100));
		edu.geom3D.Cylinder cyl = new edu.geom3D.Cylinder(new Vector(0.4,0,0.1), new Vector(0.4,0.5,0), 0.1f);
		j3ds.addShape(cyl, Color.RED.darker().darker());
		Sphere s = new Sphere(new Vector(-1,-0.2,0), 0.3f);
		j3ds.addShape(s, Color.MAGENTA);
		Tetrahedron tetr = new Tetrahedron(new Vector(0,0,1), new Vector(-0.5,0,1), new Vector(-0.5,0.5,1), new Vector(-0.25,0.25, 1.25));
		j3ds.addShape(tetr, new Color(50,50,255));
		j3ds.addShape(new Triangle(new Vector(0.2f, -0.2f, 0.1f), new Vector(0.8, -0.8, 0.1), new Vector(1,-0.3, 0.1)), new Color(150,50,255));

		j3ds.centerCamera();
		j3ds.autoZoom();
	}


	/** Create a frame containing a canvas, display it and return the  
	 * J3DScene object shown in the frame. */
	public static J3DScene createJ3DSceneInFrame() {
		JFrame f = new JFrame("J3DScene-viewer");
		f.setSize(1200,800);
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);

		J3DScene j3ds = new J3DScene();
		Canvas3D canvas = j3ds.getCanvas();

		JMenuBar menubar = new JMenuBar();
		f.setJMenuBar(menubar);
		JMenu view = new JMenu("View");
		menubar.add(view);

		JMenuItem item;
		view.add(item = new JMenuItem("Export JPG"));
		class ExportActionListener implements ActionListener{
			J3DScene j3ds;
			ExportActionListener(J3DScene j3ds){ this.j3ds = j3ds; }
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				ExampleFileFilter filter = new ExampleFileFilter(new String[]{"jpg","jpeg"}, "JPG images");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showSaveDialog(j3ds.canvas);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					edu.j3dScene.Toolbox.writeJPEGFile(chooser.getSelectedFile().getAbsolutePath(), j3ds.canvas);
				}	
			}	
		}
		item.addActionListener(new ExportActionListener(j3ds));
		view.add(item = new JMenuItem("Export EPS"));
		class ExportEPSActionListener implements ActionListener{
			J3DScene j3ds;
			ExportEPSActionListener(J3DScene j3ds){ this.j3ds = j3ds; }
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				ExampleFileFilter filter = new ExampleFileFilter(new String[]{"eps"}, "Encapsulated Postscript images");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showSaveDialog(j3ds.canvas);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					edu.j3dScene.Toolbox.writeEPSFile(chooser.getSelectedFile().getAbsolutePath(), j3ds.canvas);
				}	
			}
		}
		item.addActionListener(new ExportEPSActionListener(j3ds));
		view.addSeparator();

		view.add(item = new JMenuItem("Auto-zoom (z)"));
		class AutozoomActionListener implements ActionListener{
			J3DScene j3ds;
			AutozoomActionListener(J3DScene j3ds){ this.j3ds = j3ds; }
			public void actionPerformed(ActionEvent e) {
				j3ds.autoZoom();
			}
		}
		item.addActionListener(new AutozoomActionListener(j3ds));

		view.add(item = new JMenuItem("Center view (c)"));
		class CenterActionListener implements ActionListener{
			J3DScene j3ds;
			CenterActionListener(J3DScene j3ds){ this.j3ds = j3ds; }
			public void actionPerformed(ActionEvent e) {
				j3ds.centerCamera();
			}
		}
		item.addActionListener(new CenterActionListener(j3ds));

		view.add(item = new JMenuItem("Toggle rotation (r)"));
		class RotateActionListener implements ActionListener{
			J3DScene j3ds;
			RotateActionListener(J3DScene j3ds){ this.j3ds = j3ds; }
			public void actionPerformed(ActionEvent e) {
				j3ds.toggleRotation();
			}
		}
		item.addActionListener(new RotateActionListener(j3ds));


		view.add(item = new JMenuItem("Toggle parallel projection (p)"));
		class ParallelActionListener implements ActionListener{
			J3DScene j3ds;
			ParallelActionListener(J3DScene j3ds){ this.j3ds = j3ds; }
			public void actionPerformed(ActionEvent e) {
				j3ds.setParallelProjection(!j3ds.parallelProjection);
			}
		}
		item.addActionListener(new ParallelActionListener(j3ds));

		f.getContentPane().add(canvas);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		return j3ds;
	}

}
