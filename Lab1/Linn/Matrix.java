import java.util.Scanner;

public class Matrix {

	public static double[][] multiMatrix(double[][] matA, double[][] matB) {

		double[][] res = new double[matA.length][matB[0].length];

		for (int aRow = 0; aRow < matA.length; aRow++) {
			for (int bCol = 0; bCol < matB[0].length; bCol++) {
				for (int bRow = 0; bRow < matB.length; bRow++) {
					res[aRow][bCol] += matA[aRow][bRow]*matB[bRow][bCol];
				}
			}
		}
		return res;
	}

	public static double[][] createMatrix(int row, int col, Scanner in) {

		double[][] matrix = new double[row][col];

		for (int r = 0; r < row; r++) {
			
	
			for (int c = 0; c < col; c++) {
				matrix[r][c] = Double.parseDouble(in.next());
			}
		}
		return matrix;
	}

	public static void printMatrix(double[][] mtx) {

		for (int i = 0; i < mtx.length; i++) {
			for (int j = 0; j < mtx[0].length; j++) {
				System.out.print(String.format("%.2g%n", mtx[i][j]).replace("\n","") + " ");
			}
			System.out.println("");
		}
	}

}
	
