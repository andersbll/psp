package edu.protModel;

import edu.math.Matrix;
import edu.math.Vector;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Create next-transform. This will depend on the type of atom-group and how 
 * the internal atoms are placed in AtomGroup. It also depends on the torsion 
 * angle. The methods are not described here since they are much better 
 * illustrated graphically.
 */
public class NextTransform {
	public final int type;
	public Matrix transform;
	private double torsionAngle;

	public NextTransform(int type, double torsionAngle){
		this.type = type;
		this.torsionAngle = torsionAngle;
		updateTransform();
	}

	public double getTorsion(){
		return torsionAngle;
	}
	public void setTorsion(double angle){
		this.torsionAngle = angle;
		updateTransform();
	}
	
	protected void updateTransform(){
		switch(type){
		case AtomGroup.N_TERMINAL: updateN_TERMINALTransform();break;
		case AtomGroup.C_TERMINAL: updateC_TERMINALTransform();break;
		case AtomGroup.CN_GROUP: updateCN_GROUPTransform();break;
		case AtomGroup.CA_GROUP: updateCA_GROUPTransform();break;
		default: throw new Error("Undefined type "+type);
		}
	}
	protected void updateN_TERMINALTransform(){
		Vector nextX = new Vector(1,0,0);
		Vector nextY = new Vector(0,-1,0);
		
		Vector rot = new Vector(cos(PI/6),-sin(PI/6),0); 
		nextX = rot.rotate(nextX, torsionAngle);
		nextY = rot.rotate(nextY, torsionAngle);
		Vector nextZ = nextX.cross(nextY);
		
		Vector nextTranslate = rot.timesIn(1.46f);
		transform = Matrix.create4x4ColumnMatrix(nextX, nextY, nextZ, nextTranslate);
	}
	protected void updateC_TERMINALTransform(){
		transform = Matrix.createIdentityMatrix(4);
	}
	protected void updateCN_GROUPTransform(){

		Vector nextX = new Vector(1,0,0);
		Vector nextY = new Vector(0,1,0);
		
		Vector rot = new Vector(cos(PI/6),sin(PI/6),0); 
		nextX = rot.rotate(nextX, torsionAngle);
		nextY = rot.rotate(nextY, torsionAngle);
		
		Vector nextZ = nextX.cross(nextY);
		Vector nextTranslate = new Vector(2.29,0,0);
		transform = Matrix.create4x4ColumnMatrix(nextX, nextY, nextZ, nextTranslate);
	}
	protected void updateCA_GROUPTransform(){
		Vector nextX = new Vector(1,0,0);
		Vector nextY = new Vector(0,-1,0);
		
		Vector rot = new Vector(cos(PI/6),-sin(PI/6),0); 
		nextX = rot.rotate(nextX, torsionAngle);
		nextY = rot.rotate(nextY, torsionAngle);
		Vector nextZ = nextX.cross(nextY);
		
		Vector nextTranslate = rot.timesIn(1.51f);
		transform = Matrix.create4x4ColumnMatrix(nextX, nextY, nextZ, nextTranslate);
	}
}
