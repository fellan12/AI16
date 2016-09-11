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

		double[][] alpha = alphaPass();
		//Matrix.printMatrix(alpha);

		double sum = 0;

		for (int r = 0; r < alpha.length; r++) {
			sum += alpha[r][alpha[0].length-1];
		}
		System.out.println(sum);
	}


	public static double[][] alphaPass() {
		// initialize alpha-matrix
		alpha = new double[n][numOfObs];

		// compute alpha-0(i)
		print("beräkna alpha-0");
		double c0 = 0;
		for (int i = 0; i < n; i++) {
			alpha[i][0] = initState[0][i]*emissionMatrix[i][obsSequence[0]];
			c0 += alpha[i][0];
			print("alfa"+i+0+": " + alpha[i][0] );
		}

		// // scale alpha-0(i)
		// c0 = 1/c0;
		// for (int i = 0; i < n; i++) {
		// 	alpha[i][0] *= c0;
		// }


		// compute alpha-t(i)
		for (int t = 1; t < numOfObs; t++) {
			print("t= " + t);
			double ct = 0;
			for (int i = 0; i < n; i++) {
				print("i= " + i);
				print("obs ");
				alpha[i][t] = 0;
				for (int j = 0; j < n; j++) {
					alpha[i][t] += alpha[j][t-1] * transMatrix[j][i]; 
				}
				//System.out.println("alpha innan b "  + "t: " + t + " " + alpha[i][t]);
				alpha[i][t] *= emissionMatrix[i][obsSequence[t]];
				//System.out.println("alpha efter b " + alpha[i][t]);

				ct += alpha[i][t];
			}

			// // scale alpha-t(i)
			// ct = 1/ct;
			// for (int i = 0; i < n; i++) {
			// 	alpha[i][t] *= ct;
			// }
		}

		return alpha;




	}

	public static void print (String tmp) {
		if (prt) {
			System.out.println(tmp);
		}
	}

	

}