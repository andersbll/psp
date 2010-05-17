package edu.allatom;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

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
	private RenderMode renderMode = RenderMode.STICKS;
	private final static boolean renderH = false;
	// render mode STICKS:
	private final static float STICK_WIDTH = .3f;
	// render mode SPHERES:
	private final static float ATOM_DEFAULT_RENDER_SIZE = .75f / 2;
	private final static float HYDROGEN_RENDER_SIZE = ATOM_DEFAULT_RENDER_SIZE / 2;
	private final static float BINDING_RENDER_WIDTH = .1f;
	
	// last rendered protein
	Protein protein;
	// list of all shapes added to the scene
	List<Shape> shapes = new LinkedList<Shape>();

	private Color bondColor1 = Color.GREEN;
	private Color bondColor2 = Color.YELLOW;
	private Color bondColor = bondColor1;
	private J3DScene scene;
	
	public Renderer() {
		super();
		scene = J3DScene.createJ3DSceneInFrame();
		setAxisEnabled(true);
	}
	public void redraw() {
		centerCamera();
		autoZoom();
	}
	
	private void rerender() {
		render(protein);
	}
	public void render(Protein p) {
		for(Shape s : shapes) {
			removeShape(s);
		}
		shapes.clear();
		
		protein = p;
		List<AminoAcid> aaSeq = p.aaSeq;
		for(AminoAcid aa : aaSeq) {
			if(bondColor == bondColor1) {
				bondColor = bondColor2;
			} else {
				bondColor = bondColor1;			
			}
			renderAminoAcid(aa);
		}
		redraw();
	}
	
	public void render(AminoAcid aa) {		
		if(bondColor == bondColor1) {
			bondColor = bondColor2;
		} else {
			bondColor = bondColor1;			
		}
		renderAminoAcid(aa);
		redraw();
	}
	
	private void renderAminoAcid(AminoAcid aa) {
		Collection<Atom> atoms = aa.getBackBoneAtoms();
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
				scene.addShape(sphere, c);
				shapes.add(sphere);
				for(Atom neighbor : a.bondsTo) {
					if(!renderH && neighbor.type == Atom.Type.H) {
						continue;
					}
					if(a.position.x() <= neighbor.position.x()) {
						Cylinder cylinder = new Cylinder(
								new Vector(a.position),
								new Vector(neighbor.position),
								BINDING_RENDER_WIDTH);
						scene.addShape(cylinder, bondColor);
						shapes.add(cylinder);
					}
				}
				break;
			} case STICKS: {
				for(Atom neighbor : a.bondsTo) {
					if(!renderH && neighbor.type == Atom.Type.H) {
						continue;
					}
					Shape sphere = new Sphere(new Vector(a.position), STICK_WIDTH);
					scene.addShape(sphere, c);
					shapes.add(sphere);
					Vector midway = new Vector(
							(a.position.x()+neighbor.position.x()) / 2,
							(a.position.y()+neighbor.position.y()) / 2,
							(a.position.z()+neighbor.position.z()) / 2);
					Shape cylinder = new Cylinder(
							new Vector(a.position),
							new Vector(midway),
							STICK_WIDTH);
					scene.addShape(cylinder, c);
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
	
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode()==KeyEvent.VK_M) {
			if(renderMode == RenderMode.SPHERES) {
				renderMode = RenderMode.STICKS;
			} else {
				renderMode = RenderMode.SPHERES;
			}
			rerender();
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
	}
	@Override
	public void keyTyped(KeyEvent e) {
	}
}
