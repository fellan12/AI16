import java.util.Scanner;
import java.util.Arrays;

public class HMM3 {

	static Scanner in = new Scanner(System.in);
	static double[][] transMatrix;
	static double[][] emissionMatrix;
	static double[][] initState;
	static double[][] delta;
	static int[][] deltaidx;

	static int[] obsSequence;
	static int numOfObs;
	static int n;

	static final boolean prt = false;

	public static void main (String[] args) {
		// Create transition matrix
		int trow = in.nextInt();
		int tcol = in.nextInt();
		transMatrix = Matrix.createMatrix(trow, tcol, in);

		// Create emission matrix
		int erow = in.nextInt();
		int ecol = in.nextInt();
		emissionMatrix = Matrix.createMatrix(erow, ecol, in);

		// Create initial state probability matrix
		int isrow = in.nextInt();
		int iscol = in.nextInt();
		initState = Matrix.createMatrix(isrow, iscol, in);

		// Create observation sequence array
		numOfObs = in.nextInt();
		obsSequence = new int[numOfObs];
		for (int i = 0; i < numOfObs; i++) {
			obsSequence[i] = in.nextInt();
		}

		// number of states
		n = trow;

		double[][] delta = deltaPass();	

		// Look at the biggest value in deltas column, to get the last state in the sequence
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
			
			ans = ans + deltaidx[idxLast][obs] + " ";
			idxLast = deltaidx[idxLast][obs];
		}

		//reverse the sequence so that it is correct in aspect of time
		String ans1 = new StringBuilder(ans).reverse().toString();
		System.out.println(ans1.substring(1));
	}

	public static double[][] deltaPass() {
		// initialize delta-matrix
		delta = new double[n][numOfObs];
		deltaidx = new int[n][numOfObs];

		// compute delta-0(i)
		for (int i = 0; i < n; i++) {
			delta[i][0] = initState[0][i]*emissionMatrix[i][obsSequence[0]];
		}

		// compute delta-t(i)
		for (int t = 1; t < numOfObs; t++) {
			for (int i = 0; i < n; i++) {
				delta[i][t] = 0;
				double[] tmp = new double[transMatrix.length]; // the prob. of having observed O(1:t), being in state i given j as most likely state before
				for (int j = 0; j < n; j++) {
					tmp[j] = delta[j][t-1]*transMatrix[j][i]*emissionMatrix[i][obsSequence[t]];
				}
				// we choose the maximum number in tmp for delta, that is, that element whose preceding state (j) gives the highest probability
				delta[i][t] = findMax(tmp)[0];
				// saving the index for the idx-matrix
				deltaidx[i][t] = (int) findMax(tmp)[1];
			}
		}
		return delta;
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