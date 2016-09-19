import java.util.Scanner;

public class HMM4 {

	static Scanner in = new Scanner(System.in);
	static double[][] transMatrix;
	static double[][] emissionMatrix;
	static double[][] initState;
	static double[][] alpha;
	static double[][] beta;
	static double[][][] digamma;
	static double[][] gamma;
	static double[] c; // scale vector

	static int[] obsSequence;
	static int numOfObs; // number of observations in the sequence
	static int ecol;
	static int numOfStates; // number of states

	static int maxIters = 1000;
	static int iters = 0;
	static double oldLogProb = Double.NEGATIVE_INFINITY;
	static double logProb;

	static final boolean prt = false;

	public static void main (String[] args) {
		// Create transition matrix start guess
		int trow = in.nextInt();
		int tcol = in.nextInt();
		transMatrix = Matrix.createMatrix(trow, tcol, in);
		// Matrix.printMatrix(transMatrix);

		// Create emission matrix start guess
		int erow = in.nextInt();
		ecol = in.nextInt();
		emissionMatrix = Matrix.createMatrix(erow, ecol, in);
		// Matrix.printMatrix(emissionMatrix);

		// Create initial state probability matrix
		int isrow = in.nextInt();
		int iscol = in.nextInt();
		initState = Matrix.createMatrix(isrow, iscol, in);
		// Matrix.printMatrix(initState);

		// Create observation sequence array
		numOfObs = in.nextInt();
		obsSequence = new int[numOfObs];
		for (int i = 0; i < numOfObs; i++) {
			obsSequence[i] = in.nextInt();
		}

		numOfStates = trow;

		alphaPass();
		betaPass();
		gamma();
		reestimateModel();
		calcLogProb();

		//Matrix.printMatrix(alpha);
		//Matrix.printMatrix(beta);

		//System.out.println("oldlog; " + oldLogProb);
		//System.out.println("log: " + logProb);

		while (iters < maxIters && logProb > oldLogProb) {

			oldLogProb = logProb;
			//System.out.println("oldLogProb: " + oldLogProb);
			alphaPass();
			betaPass();
			gamma();
			reestimateModel();
			calcLogProb();
			//System.out.println("oldlog: " + oldLogProb);
			//System.out.println("log: " + logProb);
			
			//System.out.println(oldLogProb);
			iters++;
		}

		//System.out.println("iters: " + iters);
		// for (int i = 0; i < 10000; i++) {
		// 	alphaPass();
		// 	betaPass();
		// 	gamma();
		// 	reestimateModel();
		// 	//print("re-estimate");
		// 	//print("matrix A:");
		// 	//Matrix.printMatrix(transMatrix);
		// 	//print("Matrix B:");
		// 	//Matrix.printMatrix(emissionMatrix);
		// }
		
		System.out.print(trow + " " + tcol + " ");
		Matrix.printMatrix(transMatrix);
		System.out.print(erow + " " + ecol + " ");
		Matrix.printMatrix(emissionMatrix);
		


	}


	public static void alphaPass() {
		// initialize alpha-matrix
		alpha = new double[numOfStates][numOfObs];
		c = new double[numOfObs];

		// compute alpha-0(i)
		c[0] = 0;
		for (int i = 0; i < numOfStates; i++) {
			alpha[i][0] = initState[0][i]*emissionMatrix[i][obsSequence[0]];
			c[0] += alpha[i][0];
		}

		// scale alpha-0(i)
		c[0] = 1/c[0];
		for (int i = 0; i < numOfStates; i++) {
			alpha[i][0] *= c[0];
		}

		// compute alpha-t(i)
		for (int t = 1; t < numOfObs; t++) {
			c[t] = 0;
			for (int i = 0; i < numOfStates; i++) {
				alpha[i][t] = 0;
				for (int j = 0; j < numOfStates; j++) {
					alpha[i][t] += alpha[j][t-1] * transMatrix[j][i]; 
				}
				alpha[i][t] *= emissionMatrix[i][obsSequence[t]];
				c[t] += alpha[i][t];
			}

			// scale alpha-t(i)
			c[t] = 1/c[t];
			for (int i = 0; i < numOfStates; i++) {
				alpha[i][t] *= c[t];
			}
		}
	}

	public static void betaPass() {
		// initialize beta-matrix
		beta = new double[numOfStates][numOfObs];

		// compute beta-(T-1)(i)
		for (int i = 0; i < numOfStates; i++) {
			beta[i][numOfObs-1] = c[numOfObs-1];
		}

		// compute beta-t(i)
		for (int t = numOfObs-2; t >= 0; t--) {
			for (int i = 0; i < numOfStates; i++) {
				beta[i][t] = 0;
				for (int j = 0; j < numOfStates; j++) {
					beta[i][t] += transMatrix[i][j]*emissionMatrix[j][obsSequence[t+1]]*beta[j][t+1]; 
				}
				// scale beta-t(i)
				beta[i][t] *= c[t];
			}
		}
	}

	public static void gamma() {
		// initialize digamma-matrix and gamma-vector;
		digamma = new double[numOfStates][numOfStates][numOfObs];
		gamma = new double[numOfStates][numOfObs];
		
		double denom = 0;

		for (int t = 0; t < numOfObs-1; t++) {

			// calculate denominator
			denom = 0;
			for (int i = 0; i < numOfStates; i++) {
				for (int j = 0; j < numOfStates; j++) {
					denom += alpha[i][t]*transMatrix[i][j]*emissionMatrix[j][obsSequence[t+1]]*beta[j][t+1]; 
				}
			}

			// calculate the rest
			for (int i = 0; i < numOfStates; i++) {
				gamma[i][t] = 0;
				for (int j = 0; j < numOfStates; j++) {
					digamma[i][j][t] = (alpha[i][t]*transMatrix[i][j]*emissionMatrix[j][obsSequence[t+1]]*beta[j][t+1])/denom;
					gamma[i][t] += digamma[i][j][t]; 
				}
			}
		}

		// special case for gamma(T-1)(i)
		denom = 0;
		for (int i = 0; i < numOfStates; i++) {
			denom += alpha[i][numOfObs-1];
		}

		for (int i = 0; i< numOfStates; i++) {
			gamma[i][numOfObs-1] = alpha[i][numOfObs-1]/denom;
		}
	}

	public static void reestimateModel() {
		// re-estimate initState (pi)
		for (int i = 0; i < numOfStates; i++) {
			initState[0][i] = gamma[i][0];
		}

		// re-estimate transMatrix (a)
		double numer;
		double denom;
		for (int i = 0; i < numOfStates; i++) {
			for (int j = 0; j < numOfStates; j++) {
				numer = 0;
				denom = 0;
				for (int t = 0; t < numOfObs-1; t++) {
					numer += digamma[i][j][t];
					denom += gamma[i][t];
				}
				transMatrix[i][j] = numer/denom;
			}
		}

		// re-estimate emissionMatrix (b)
		for (int i = 0; i < numOfStates; i++) {
			for (int j = 0; j < ecol; j++) {
				numer = 0;
				denom = 0;
				for (int t = 0; t < numOfObs; t++) {
					if (obsSequence[t] == j) {
						numer += gamma[i][t];
					}
					denom += gamma[i][t];
				}
				emissionMatrix[i][j] = numer/denom;
			}
		}

	}

	public static void calcLogProb() {
		logProb = 0;
		for (int i = 0; i < numOfObs; i++) {
			logProb += Math.log(c[i]);

		}
		logProb = -logProb;
	}

	public static void print (String tmp) {
		if (prt) {
			System.out.println(tmp);
		}
	}

	

}