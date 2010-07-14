package edu.allatom;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.media.j3d.Canvas3D;

import edu.geom3D.Cylinder;
import edu.geom3D.Shape;
import edu.geom3D.Sphere;
import edu.j3dScene.J3DScene;
import edu.math.Vector;

public class Renderer extends J3DScene implements KeyListener {
	enum RenderMode {
		SPHERES,
		STICKS,
	}
	private class ShapeStuff {
		Shape shape;
		Color c;
		public ShapeStuff(Shape shape, Color c) {
			this.shape = shape;
			this.c = c;
		}
	}
	private RenderMode renderMode = RenderMode.STICKS;
	public static boolean renderH = false;
	public static boolean renderBackboneOnly = true;
	
	private final static int C_ALPHA_OPACITY = 100;
	// render mode STICKS:
	private final static float STICK_WIDTH = .3f;
	// render mode SPHERES:
	private final static float ATOM_DEFAULT_RENDER_SIZE = .75f / 2;
	private final static float HYDROGEN_RENDER_SIZE = ATOM_DEFAULT_RENDER_SIZE / 2;
	private final static float BINDING_RENDER_WIDTH = .1f;
	
	// last rendered protein
	List<Protein> proteins;
	List<List<Atom>> calphatraces;
	List<ShapeStuff> otherShapes;

	// list of all shapes added to the scene
	List<Shape> shapes = new LinkedList<Shape>();

	private Color bondColor1 = Color.GREEN;
	private Color bondColor2 = Color.YELLOW;
	private Color bondColor = bondColor1;
//	private J3DScene scene;
	
	public Renderer() {
		super();
		proteins = new ArrayList<Protein>();
		calphatraces = new ArrayList<List<Atom>>();
		otherShapes = new ArrayList<ShapeStuff>();
		J3DScene.createJ3DSceneInFrame(this);
		setAxisEnabled(true);
	}
	public void redraw() {
		centerCamera();
		autoZoom();
	}
	
	public void render() {
		for(Shape s : shapes) {
			removeShape(s);
		}
		shapes.clear();
		
		for(Protein p : proteins) {
			render(p);
		}
		for(List<Atom> c : calphatraces) {
			render(c);
		}
		for(ShapeStuff s : otherShapes) {
			addShape(s.shape, s.c);
		}
		redraw();
	}

	public void addToScene(Protein p) {
		proteins.add(p);
	}

	public void addToScene(Vector pos, float size, Color color) {
		ShapeStuff s = new ShapeStuff(new Sphere(pos, size), color);
		otherShapes.add(s);
	}
	public void addToScene(Vector startPos, Vector endPos, float width, Color color) {
		ShapeStuff s = new ShapeStuff(new Cylinder(startPos, endPos, width), color);
		otherShapes.add(s);
	}

	public void addToScene(List<Atom> c) {
		calphatraces.add(c);
	}
	
	private void render(Protein p) {
//		List<AminoAcid> aaSeq = p.aaSeq.subList(0, 5);
		List<AminoAcid> aaSeq = p.aaSeq;
		for(AminoAcid aa : aaSeq) {
			if(bondColor == bondColor1) {
				bondColor = bondColor2;
			} else {
				bondColor = bondColor1;			
			}
			render(aa);
		}
	}
	
	private void render(AminoAcid aa) {
		Collection<Atom> atoms;
		if(renderBackboneOnly) {
			atoms = aa.getBackboneAtoms();
		} else {
			atoms = aa.getAtoms();
		}
		for(Atom a: atoms){
			if(!renderH && a.type == Atom.Type.H) {
				continue;
			}
			Color c = Color.PINK;
			float size = ATOM_DEFAULT_RENDER_SIZE;
			switch(a.type) {
			case H: c = Color.GRAY.brighter(); size = HYDROGEN_RENDER_SIZE; break;
			case C: c = Color.DARK_GRAY; break;
			case N: c = Color.BLUE; break;
			case O: c = Color.RED; break;
			case S: c = Color.YELLOW; break;
			}
			switch(renderMode) {
			case SPHERES: {
				Sphere sphere = new Sphere(new Vector(a.position), size);
				addShape(sphere, c);
				shapes.add(sphere);
				for(Atom neighbor : a.getBonds()) {
					if(!renderH && neighbor.type == Atom.Type.H) {
						continue;
					}
					if(renderBackboneOnly &&
					   aa.getBackboneAtoms().contains(neighbor.label)) {
						continue;
					}
					if(a.position.x() <= neighbor.position.x()) {
						Cylinder cylinder = new Cylinder(
								new Vector(a.position),
								new Vector(neighbor.position),
								BINDING_RENDER_WIDTH);
						addShape(cylinder, bondColor);
						shapes.add(cylinder);
					}
				}
				break;
			} case STICKS: {
				Shape sphere = new Sphere(new Vector(a.position), STICK_WIDTH);
//				System.out.println("sphere: "+new Vector(a.position));
				if(a.position.x()!=a.position.x()) {
					break;
				}
				addShape(sphere, c);
				shapes.add(sphere);
				for(Atom neighbor : a.getBonds()) {
					if(!renderH && neighbor.type == Atom.Type.H) {
						continue;
					}
					if(renderBackboneOnly &&
							aa.getBackboneAtoms().contains(neighbor.label)) {
						continue;
					}
					Vector midway = new Vector(
							(a.position.x()+neighbor.position.x()) / 2,
							(a.position.y()+neighbor.position.y()) / 2,
							(a.position.z()+neighbor.position.z()) / 2);
//					System.out.println("cylinder: "+a.name+" "+new Vector(a.position)+"  "+new Vector(midway));
					if(midway.x()!=midway.x()) {
						break;
					}
					Shape cylinder = new Cylinder(
							new Vector(a.position),
							new Vector(midway),
							STICK_WIDTH);
					addShape(cylinder, c);
					shapes.add(cylinder);
				}
				break;
			}}
		}		
	}
	
	private void render(List<Atom> trace) {
//		ListIterator<Atom> iterator = trace.subList(0,5).listIterator();
		ListIterator<Atom> iterator = trace.listIterator();
		while(true) {
			Atom a = iterator.next();
			if(!iterator.hasNext()) {
				break;
			}
			Atom aNext = iterator.next();
			iterator.previous();
			Color c = new Color(
					Color.DARK_GRAY.getRed(),
					Color.DARK_GRAY.getGreen(),
					Color.DARK_GRAY.getBlue(),
					C_ALPHA_OPACITY);
			switch(renderMode) {
			case SPHERES: {
//				Sphere sphere = new Sphere(new Vector(a.position), size);
//				addShape(sphere, c);
//				shapes.add(sphere);
//				for(Atom neighbor : a.getBonds()) {
//					if(!renderH && neighbor.type == Atom.Type.H) {
//						continue;
//					}
//					if(renderBackboneOnly &&
//							!AminoAcid.backBoneAtomNames.contains(neighbor.name)) {
//						continue;
//					}
//					if(a.position.x() <= neighbor.position.x()) {
//						Cylinder cylinder = new Cylinder(
//								new Vector(a.position),
//								new Vector(neighbor.position),
//								BINDING_RENDER_WIDTH);
//						addShape(cylinder, bondColor);
//						shapes.add(cylinder);
//					}
//				}
//				break;
			} case STICKS: {
//				System.out.println(new Vector(a.position));
				Shape sphere = new Sphere(new Vector(a.position), STICK_WIDTH);
				if(a.position.x()!=a.position.x()) {
					break;
				}
				addShape(sphere, Color.DARK_GRAY);
				shapes.add(sphere);
				if(aNext != null) {
					Shape cylinder = new Cylinder(
							new Vector(a.position),
							new Vector(aNext.position),
							STICK_WIDTH);
					addShape(cylinder, c);
					shapes.add(cylinder);
				}
				break;
			}}
		}		
	}
	
	public Canvas3D getCanvas() {
		Canvas3D c3d = super.getCanvas();
		c3d.addKeyListener(this);
		return c3d;
	}
	
	/**
	 * M: toggles render mode
	 * H: toggles hydrogen
	 * S: toggles sidechains
	 */
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_M) {
			if(renderMode == RenderMode.SPHERES) {
				renderMode = RenderMode.STICKS;
			} else {
				renderMode = RenderMode.SPHERES;
			}
			render();
		} else if(e.getKeyCode() == KeyEvent.VK_H) {
			renderH = !renderH;
			render();
		} else if(e.getKeyCode() == KeyEvent.VK_D) {
			renderBackboneOnly = !renderBackboneOnly;
			render();
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
	}
	@Override
	public void keyTyped(KeyEvent e) {
	}
	
}
