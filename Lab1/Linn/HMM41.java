import java.util.Scanner;

public class HMM41 {

	static Scanner in = new Scanner(System.in);

	static double[][] transMatrix;
	static double[][] emissionMatrix;
	static double[][] initState;
	static int[] obsSequence;

	static int numOfStates;
	static int numOfObs;
	static int obsSequenceLength;

	static double[][] alpha;
	static double[][] beta;
	static double[][] gamma;
	static double[][][] digamma;

	static double[] c; // scale vector

	static int maxIters = 10000;
	static int iters = 0; // global??
	static double logProb;
	static double oldLogProb = Double.NEGATIVE_INFINITY;

	public static void main(String[] args) {

		// Create transmission matrix start guess
		int trow = in.nextInt();
		int tcol = in.nextInt();
		transMatrix = Matrix.createMatrix(trow, tcol, in);
		// Matrix.printMatrix(transMatrix);

		// Create emission matrix start guess
		int erow = in.nextInt();
		int ecol = in.nextInt();
		emissionMatrix = Matrix.createMatrix(erow, ecol, in);
		// Matrix.printMatrix(emissionMatrix);

		// Create initial state probability distribution
		int irow = in.nextInt();
		int icol = in.nextInt();
		initState = Matrix.createMatrix(irow, icol, in);
		// Matrix.printMatrix(initState);

		// Create observation sequence
		obsSequenceLength = in.nextInt();
		obsSequence = new int[obsSequenceLength];
		for (int i = 0; i < obsSequenceLength; i++) {
			//System.out.print(i);
			obsSequence[i] = in.nextInt();
		}

		numOfStates = trow;
		numOfObs = ecol;

		// Initialize scale vector;
		c = new double[obsSequenceLength];
		alphaPass();
		betaPass();
		gamma();
		reestimate();
		calcLog();

		while(iters < maxIters && logProb > oldLogProb) {
			oldLogProb = logProb;

			alphaPass();
			betaPass();
			gamma();
			reestimate();
			calcLog();

			iters++;
		}

		System.out.print(trow + " " + tcol + " ");
		Matrix.printMatrix(transMatrix);
		System.out.print(erow + " " + ecol + " ");
		Matrix.printMatrix(emissionMatrix);

		// System.out.println("iters: " + iters);
		// Matrix.printMatrix(transMatrix);
		// Matrix.printMatrix(emissionMatrix);
	}

	public static void alphaPass() {
		// Initialize alpha
		alpha = new double[numOfStates][obsSequenceLength];

		// compute a0(i)
		c[0] = 0;
		for (int i = 0; i < numOfStates; i++) {
			alpha[i][0] = initState[0][i]*emissionMatrix[i][obsSequence[0]];
			c[0] += alpha[i][0];
		}

		// scale a0(i)
		c[0] = 1/c[0];
		for (int i = 0; i < numOfStates; i++) {
			alpha[i][0] *= c[0];
		}

		// compute at(i)
		for (int t = 1; t < obsSequenceLength; t++) {
			c[t] = 0;
			for (int i = 0; i < numOfStates; i++) {
				alpha[i][t] = 0;
				for (int j = 0; j < numOfStates; j++) {
					alpha[i][t] += alpha[j][t-1]*transMatrix[j][i];
				}
				alpha[i][t] *= emissionMatrix[i][obsSequence[t]];
				c[t] += alpha[i][t];
			}

			// scale at(i)
			c[t] = 1/c[t];
			for (int i = 0; i < numOfStates; i++) {
				alpha[i][t] *= c[t];
			}
		}
	}

	public static void betaPass() {
		// Initialize beta
		beta = new double[numOfStates][obsSequenceLength];

		// compute b(t-1)(i)
		for (int i = 0; i < numOfStates; i++) {
			beta[i][obsSequenceLength-1] = c[obsSequenceLength-1];
		}

		// compute bt(i)
		for (int t = obsSequenceLength-2; t >= 0; t--) {
			for (int i = 0; i < numOfStates; i++) {
				beta[i][t] = 0;
				for (int j = 0; j < numOfStates; j++) {
					beta[i][t] += transMatrix[i][j]*emissionMatrix[j][obsSequence[t+1]]*beta[j][t+1];
				} 
				beta[i][t] *= c[t];
			}
		}
	}

	public static void gamma() {
		// initialize gamma and digamma matrices;
		gamma = new double[numOfStates][obsSequenceLength];
		digamma = new double[numOfStates][numOfStates][obsSequenceLength];
		double denom;

		// calculate gamma and digamma
		for (int t = 0; t < obsSequenceLength-1; t++) {
			denom = 0;
			for (int i = 0; i < numOfStates; i++) {
				for (int j = 0; j < numOfStates; j++) {
					denom += alpha[i][t]*transMatrix[i][j]*emissionMatrix[j][obsSequence[t+1]]*beta[j][t+1];
				}
			}
			for (int i = 0; i < numOfStates; i++) {
				gamma[i][t] = 0;
				for (int j = 0; j < numOfStates; j++) {
					digamma[i][j][t] = alpha[i][t]*transMatrix[i][j]*emissionMatrix[j][obsSequence[t+1]]*beta[j][t+1] / denom;
					gamma[i][t] += digamma[i][j][t]; 
				}
			}
		}

		// special case for gamma(T-1)(i)
		denom = 0;
		for (int i = 0; i < numOfStates; i++) {
			denom += alpha[i][obsSequenceLength-1];
		}
		for (int i = 0; i < numOfStates; i++) {
			gamma[i][obsSequenceLength-1] = alpha[i][obsSequenceLength-1] / denom;
		}
	}

	public static void reestimate() {
		double numer;
		double denom;
		// re-estimate initState (pi)
		for (int i = 0; i < numOfStates; i++) {
			initState[0][i] = gamma[i][0];
		}

		// re-estimate trans-matrix (A)
		for (int i = 0; i < numOfStates; i++) {
			for (int j = 0; j < numOfStates; j++) {
				numer = 0;
				denom = 0;
				for (int t = 0; t < obsSequenceLength-1; t++) {
					numer += digamma[i][j][t];
					denom += gamma[i][t];
				}
				transMatrix[i][j] = numer / denom;
			}
		}

		// re-estimate emission-matrix (B)
		for (int i = 0; i < numOfStates; i++) {
			for (int j = 0; j < numOfObs; j++) {
				numer = 0;
				denom = 0;
				for (int t = 0; t < obsSequenceLength; t++) {
					if (obsSequence[t] == j) {
						numer += gamma[i][t];
					}
					denom += gamma[i][t];
				}
				emissionMatrix[i][j] = numer / denom;
			}
		}
	}

	public static void calcLog() {
		logProb = 0;
		for (int i = 0; i < obsSequenceLength; i++) {
			logProb += Math.log(c[i]);
		}
		logProb = -logProb;
	}





























}