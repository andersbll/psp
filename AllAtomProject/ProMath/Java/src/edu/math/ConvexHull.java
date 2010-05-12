package edu.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ConvexHull {

	public static List<Vector2D> grahamScan2D(List<Vector2D> points){
		
		Vector2D p0 = points.get(0);
		for(Vector2D p: points) if(p.y<p0.y || (p.y==p0.y&&p.x<p0.x) ) p0 = p;
		
		Vector2D[] sorted = new Vector2D[points.size()-1];
		Vector x = new Vector(1,0,0);
		int c=0;
		for(Vector2D p: points){
			if(p==p0) continue;
			sorted[c] = p;
			sorted[c].z = p.y>=p0.y?x.angle(p0.vectorTo(sorted[c])):(2*(float)Math.PI)-x.angle(p0.vectorTo(sorted[c]));
			c++;
		}
		
		Arrays.sort(sorted, new Comparator<Vector>(){
			public int compare(Vector o1, Vector o2) {
				return Float.compare(o1.z, o2.z);
			}});
	
		for(Vector2D p: points) p.z = 0;
		
		List<Vector2D> ret = new ArrayList<Vector2D>();
		ret.add(p0);
		ret.add(sorted[0]);
		ret.add(sorted[1]);
		for(int i=2;i<sorted.length;i++){
			while(true){
				float cross = ret.get(ret.size()-2).vectorTo(ret.get(ret.size()-1)).crossIn(ret.get(ret.size()-1).vectorTo(sorted[i])).z;
				if(cross<0) ret.remove(ret.size()-1);
				else break;
			}
			ret.add(sorted[i]);
		}
		
		return ret;
	}
}
