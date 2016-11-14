import java.util.Scanner;

public class HMM2 {

	static Scanner in = new Scanner(System.in);
	static double[][] transMatrix;
	static double[][] emissionMatrix;
	static double[][] initState;
	static double[][] alpha;

	static int[] obsSequence;
	static int numOfObs;
	static int n;

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
		obsSequenceLength = in.nextInt();
		obsSequence = new int[obsSequenceLength];
		for (int i = 0; i < obsSequenceLength; i++) {
			obsSequence[i] = in.nextInt();
		}

		// number of states
		n = trow;

		double[][] alpha = alphaPass();

		double sum = 0;

		for (int r = 0; r < alpha.length; r++) {
			sum += alpha[r][alpha[0].length-1];
		}
		System.out.println(sum);
	}


	public static double[][] alphaPass() {
		// initialize alpha-matrix
		alpha = new double[n][obsSequenceLength];

		// compute alpha-0(i)
		for (int i = 0; i < n; i++) {
			alpha[i][0] = initState[0][i]*emissionMatrix[i][obsSequence[0]];
		}

		// compute alpha-t(i)
		for (int t = 1; t < obsSequenceLength; t++) {
			for (int i = 0; i < n; i++) {
				alpha[i][t] = 0;
				for (int j = 0; j < n; j++) {
					alpha[i][t] += alpha[j][t-1] * transMatrix[j][i]; 
				}
				alpha[i][t] *= emissionMatrix[i][obsSequence[t]];
			}
		}
		return alpha;
	}
}