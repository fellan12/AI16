class Estimator{

	static int numOfStates;
	static int obsSequenceLength;
	static int numOfObs;

	static double numer = 0;
	static double denom = 0;

	public Estimator(int numOfStates, int numOfObs, int obsSequenceLength){
		this.numOfObs = numOfObs;
		this.numOfStates = numOfStates;
		this.obsSequenceLength = obsSequenceLength;
	}

	public static double[] re_Estimate_Init(double[] initState, double[][] gamma){
		// re-estimate initState (pi)
		for (int i = 0; i < numOfStates; i++) {
			initState[i] = gamma[i][0];
		}

		return initState;
	}

	public static double[][] re_Estimate_A(double[][] transMatrix, double[][] gamma, double[][][] digamma){
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

		return transMatrix;
	}

	public static double[][] re_Estimate_B(double[][] emissionMatrix, double[][] gamma, int[] obsSequence){
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

		return emissionMatrix;
	}	
}