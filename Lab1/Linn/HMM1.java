import java.util.Scanner;


public class HMM1 {

	static Scanner in = new Scanner(System.in);
	static double[][] transMatrix;
	static double[][] emissionMatrix;
	static double[][] initState;


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

		double[][] nextState = Matrix.multiMatrix(initState, transMatrix);
		double[][] nextObs = Matrix.multiMatrix(nextState, emissionMatrix);

		System.out.print(nextObs.length + " " + nextObs[0].length + " ");
		Matrix.printMatrix(nextObs);
	}
}