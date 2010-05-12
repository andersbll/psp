package edu.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Toolbox {
	private static Random rand = new Random(); 

	/**
	 * Return a random number between (including) i1 and (not including) i2
	 */
	public static int randBetween(int i1, int i2){
		int max = Math.max(i1, i2);
		int min = Math.min(i1, i2);
		return (int)(rand.nextFloat()*(max-min)+min);
	}
	/**
	 * Return a random number between (including) f1 and (not including) f2
	 */
	public static float randBetween(float f1, float f2){
		float max = Math.max(f1, f2);
		float min = Math.min(f1, f2);
		return rand.nextFloat()*(max-min)+min;
	}


	/**
	 * Generate a random permutation between (including) 0 and 
	 * (not including) max
	 */
	public static int[] randPermutation(int max){
		int[] ret = new int[max];
		for(int i=0;i<max;i++){ ret[i] = i;}

		for(int i=0;i<max;i++){
			int sI = randBetween(0,max);
			int t = ret[sI];
			ret[sI] = ret[i];
			ret[i] = t;
		}
		return ret;
	}

	public static List<int[]> getAllPermutations(int max){
		int[] begin = {};

		int[] end = new int[max];
		for(int i=0;i<max;i++) end[i] = i;
		
		return permute(begin, end);
	}

	/** From http://www.java2s.com/Tutorial/Java/0100__Class-Definition/RecursivemethodtofindallpermutationsofaString.htm */
	private static List<int[]> permute(int[] beginning, int[] ending) {
		List<int[]> ret = new ArrayList<int[]>();
		if (ending.length <= 1){
			int[] perm = new int[beginning.length+ending.length];
			System.arraycopy(beginning, 0, perm, 0, beginning.length);
			System.arraycopy(ending, 0, perm, beginning.length, perm.length-beginning.length);
			ret.add(perm);
		}else{
			for (int i = 0; i < ending.length; i++) {
				try {
					int[] newEnd = new int[ending.length-1];
					System.arraycopy(ending, 0, newEnd, 0, i);
					System.arraycopy(ending, i+1, newEnd, i, ending.length-1-i);

					int[] newBegin = new int[beginning.length+1];
					System.arraycopy(beginning, 0, newBegin, 0, beginning.length);
					newBegin[beginning.length] = ending[i];
					ret.addAll(permute(newBegin, newEnd));
					
				} catch (StringIndexOutOfBoundsException exception) {
					exception.printStackTrace();
				}
			}
		}
		return ret;
	}

	/**
	 * Calculate the binomial coefficient of n and m using dynamic programming. 
	 * Copied from http://www.brpreiss.com/books/opus5/html/page460.html
	 */
	public static int binom(int n, int m){
		int[] b = new int[n+1];
		b[0] = 1;
		for(int i=1;i<=n;++i){
			b[i] = 1;
			for(int j=i-1;j>0;--j)
				b[j]+=b[j-1];
		}
		return b[m];
	}

	/**
	 * Seeds the random generater used by the randPermutation and randBetween methods.
	 * @param s
	 */
	public static void seed(long s){
		rand = new Random(s);
	}
}
