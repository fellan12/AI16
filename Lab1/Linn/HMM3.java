import java.util.Scanner;
import java.util.Arrays;

public class HMM3 {

	static Scanner in = new Scanner(System.in);
	static double[][] transMatrix;
	static double[][] emissionMatrix;
	static double[][] initState;
	static double[][] delta;
	static int[][] argmax;

	static int[] obsSequence;
	static int numOfObs;
	static int n;

	static final boolean prt = false;

	public static void main (String[] args) {
		// Create transition matrix
		int trow = in.nextInt();
		int tcol = in.nextInt();
		transMatrix = Matrix.createMatrix(trow, tcol, in);
		//Matrix.printMatrix(transMatrix);

		// Create emission matrix
		int erow = in.nextInt();
		int ecol = in.nextInt();
		emissionMatrix = Matrix.createMatrix(erow, ecol, in);
		//Matrix.printMatrix(emissionMatrix);

		// Create initial state probability matrix
		int isrow = in.nextInt();
		int iscol = in.nextInt();
		initState = Matrix.createMatrix(isrow, iscol, in);
		//Matrix.printMatrix(initState);

		// Create observation sequence array
		numOfObs = in.nextInt();
		obsSequence = new int[numOfObs];
		for (int i = 0; i < numOfObs; i++) {
			obsSequence[i] = in.nextInt();
		}

		// number of states
		n = trow;

		double[][] delta = deltaPass();
		//Matrix.printMatrix(delta);
		//Matrix.printMatrix(argmax);

	

		// kolla största värdet i deltas sista kolumn
		double maxLast = 0;
		int idxLast = 0;
		for (int r = 0; r < delta.length; r++) {
			if (delta[r][delta[0].length-1] > maxLast) {
				maxLast = delta[r][delta[0].length-1];
				idxLast = r;
			}
		}

		String ans = idxLast + " ";
		
		for (int obs = numOfObs-1; obs > 0; obs--) {
			
			ans = ans + argmax[idxLast][obs] + " ";
			idxLast = argmax[idxLast][obs];
		}

		//reverse the sequence so that it is correct in aspect of time
		String ans1 = new StringBuilder(ans).reverse().toString();

		System.out.println(ans1.substring(1));

	}


	public static double[][] deltaPass() {
		// initialize delta-matrix
		delta = new double[n][numOfObs];
		argmax = new int[n][numOfObs];

		// compute delta-0(i)
		print("beräkna delta-0");
		double c0 = 0;
		for (int i = 0; i < n; i++) {
			delta[i][0] = initState[0][i]*emissionMatrix[i][obsSequence[0]];
			c0 += delta[i][0];
			print("delta"+i+0+": " + delta[i][0] );
		}



		// compute delta-t(i)
		for (int t = 1; t < numOfObs; t++) {
			print("t= " + t);
	
			for (int i = 0; i < n; i++) {
				print("i= " + i);
				print("obs ");
				delta[i][t] = 0;
				double[] tmp = new double[transMatrix.length];
				for (int j = 0; j < n; j++) {
					tmp[j] = delta[j][t-1]*transMatrix[j][i]*emissionMatrix[i][obsSequence[t]];
				}
				// hitta max av tmp

				delta[i][t] = findMax(tmp)[0];
				// spara argmax
				argmax[i][t] = (int) findMax(tmp)[1];

			
			}

		}

		return delta;




	}

	public static void print(String tmp) {
		if (prt) {
			System.out.println(tmp);
		}
	}

	public static double[] findMax(double[] arr) {
		double[] maxVal = new double[2];

		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > maxVal[0]) {
				maxVal[0] = arr[i];
				maxVal[1] = i;
			}
		}
		return maxVal;
	}

	

}