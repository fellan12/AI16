
import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;
import java.lang.Math;

class HMM4{
	static double[][] transMatrix;
	static double[][] emissionMatrix;
	static double[] initState;
	static int[] obsSequence;

	static int numOfStates;
	static int numOfObs;
	static int obsSequenceLength;

	static double[][] alpha;
	static double[][] beta;
	static double[][] gamma;
	static double[][][] digamma;

	static double[] c;

	static double logProb;
	static double oldLogProb = -Double.MAX_VALUE;
	static int iters = 0;
	static int maxIters = 10000;


	public static void getInput() throws IOException{
		Scanner stdin = new Scanner(System.in);
		int ax = stdin.nextInt();
		int ay = stdin.nextInt();

		transMatrix = new double[ax][ay];
		for(int i = 0; i < ax; i++){
			for(int j = 0; j < ay; j++){
				transMatrix[i][j] = stdin.nextDouble();
			}
		}

		int bx = stdin.nextInt();
		int by = stdin.nextInt();
		emissionMatrix = new double[bx][by];
		for(int i = 0; i < bx; i++){
			for(int j = 0; j < by; j++){
				emissionMatrix[i][j] = stdin.nextDouble();
			}
		}

		stdin.nextInt();
		int iy = stdin.nextInt();
		initState = new double[iy];
		for(int j = 0; j < iy; j++){
			initState[j] = stdin.nextDouble();
		}

		int eSeq = stdin.nextInt();
		obsSequence = new int[eSeq];
		for(int i = 0; i < eSeq; i++){
			obsSequence[i] = stdin.nextInt();
		}

		c = new double[eSeq];

		numOfStates = ax;
		numOfObs = by;
		obsSequenceLength = eSeq;

	}

	public static void calcLog() {
		logProb = 0;
		for (int i = 0; i < obsSequenceLength; i++) {
			logProb += Math.log(c[i]);
		}
		logProb = -logProb;
	}

	public static void printMatrix(double[][] matrix){
		System.out.print(matrix.length + " " + matrix[0].length + " ");
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[0].length; j++){
                System.out.print(matrix[i][j] + "   ");
            }
        }
        System.out.println();
    }

    public static void printVector(double[] vector){
    	System.out.print("1 " + vector.length + " ");
    	for(double i : vector){
    		System.out.print(i + " ");
    	}
    }

    public static void main(String[] args) throws IOException {
		getInput();

		c = new double[obsSequenceLength];

		Forward f = new Forward(numOfStates, obsSequenceLength);
		Backward b = new Backward(numOfStates, obsSequenceLength);
		GammaDigamma g = new GammaDigamma(numOfStates, obsSequenceLength);
		Estimator e = new Estimator(numOfStates, numOfObs, obsSequenceLength);

		//First Run
		alpha = f.forward(transMatrix, emissionMatrix, initState, obsSequence, c);
		beta = b.backward(transMatrix, emissionMatrix, obsSequence, c);
		g.gammaDigamma(transMatrix, emissionMatrix, obsSequence, alpha, beta);
		gamma = g.getGamma();
		digamma = g.getDiGamma();
		initState = e.re_Estimate_Init(initState, gamma);
		transMatrix = e.re_Estimate_A(transMatrix, gamma, digamma);
		emissionMatrix = e.re_Estimate_B(emissionMatrix, gamma, obsSequence);
		calcLog();

		while(iters < maxIters && logProb > oldLogProb){
			oldLogProb = logProb;
				
			alpha = f.forward(transMatrix, emissionMatrix, initState, obsSequence, c);
			beta = b.backward(transMatrix, emissionMatrix, obsSequence, c);
			g.gammaDigamma(transMatrix, emissionMatrix, obsSequence, alpha, beta);
			gamma = g.getGamma();
			digamma = g.getDiGamma();
			initState = e.re_Estimate_Init(initState, gamma);
			transMatrix = e.re_Estimate_A(transMatrix, gamma, digamma);
			emissionMatrix = e.re_Estimate_B(emissionMatrix, gamma, obsSequence);
			calcLog();

			iters++;
		}
		
		printMatrix(transMatrix);
		printMatrix(emissionMatrix);
		
	}

}