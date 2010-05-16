package edu.allatom.statistics;

import java.awt.Color;
import java.util.Collection;
import java.util.List;

import edu.geom3D.Cylinder;
import edu.geom3D.Sphere;
import edu.j3dScene.J3DScene;
import edu.math.Vector;

public class Renderer {
	private final static float ATOM_DEFAULT_RENDER_SIZE = .75f / 2;
	private final static float HYDROGEN_RENDER_SIZE = ATOM_DEFAULT_RENDER_SIZE / 2;
	private final static float BINDING_RENDER_WIDTH = .1f;


	private Color bondColor = Color.LIGHT_GRAY;
	private J3DScene scene;
	
	public Renderer() {
		scene = J3DScene.createJ3DSceneInFrame();
		scene.setAxisEnabled(true);
		
	}
	public void redraw() {
		scene.centerCamera();
		scene.autoZoom();	
	}
	
	public void render(Protein p) {		
		List<AminoAcid> aaSeq = p.aaSeq;
		for(AminoAcid aa : aaSeq) {
			if(bondColor == Color.LIGHT_GRAY) {
				bondColor = Color.BLACK;
			} else {
				bondColor = Color.LIGHT_GRAY;			
			}
			renderAminoAcid(aa, scene);
		}
		redraw();
	}
	
	private void renderAminoAcid(AminoAcid aa, J3DScene scene) {
		Collection<Atom> atoms = aa.allatoms.values();
		for(Atom a: atoms){
			Color c = Color.PINK;
			float size = ATOM_DEFAULT_RENDER_SIZE;
			switch(a.type) {
			case H: c = Color.GRAY.brighter(); size = HYDROGEN_RENDER_SIZE; break;
			case C: c = Color.DARK_GRAY; break;
			case N: c = Color.BLUE; break;
			case O: c = Color.RED; break;
			case S: c = Color.YELLOW; break;
			}
			scene.addShape(new Sphere(new Vector(a.position), size), c);
			for(Atom neighbor : a.bondsTo) {
				if(a.position.x() <= neighbor.position.x()) {
					scene.addShape(new Cylinder(
							new Vector(a.position),
							new Vector(neighbor.position),
							BINDING_RENDER_WIDTH), bondColor);
				}
			}
		}		
	}
}
