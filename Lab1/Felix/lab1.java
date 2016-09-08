
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

class lab1 {

	static double[][] matrixA;
	static double[][] matrixB;
	static double[][] matrixInit;

	public static void getInput() throws IOException{
		Scanner stdin = new Scanner(System.in);
		int ax = stdin.nextInt();
		int ay = stdin.nextInt();

		matrixA = new double[ay][ax];

		for(int i = 0; i < ay; i++){
			for(int j = 0; j < ax; j++){
				matrixA[i][j] = stdin.nextDouble();
			}
		}

		int bx = stdin.nextInt();
		int by = stdin.nextInt();


		matrixB = new double[by][bx];

		for(int i = 0; i < by; i++){
			for(int j = 0; j < bx; j++){
				matrixB[i][j] = stdin.nextDouble();
			}
		}

		int ix = stdin.nextInt();
		int iy = stdin.nextInt();

		matrixInit = new double[iy][ix];

		for(int i = 0; i < iy; i++){
			for(int j = 0; j < ix; j++){
				matrixInit[i][j] = stdin.nextDouble();
			}
		}

		//print Matrix A
		System.out.println("Matrix A");
		for(int i = 0; i < ay; i++){
			for(int j = 0; j < ax; j++){
				System.out.print(matrixA[i][j] + "		" );
			}
			System.out.println();
		}

		//print Matrix B
		System.out.println("Matrix B");
		for(int i = 0; i < by; i++){
			for(int j = 0; j < bx; j++){
				System.out.print(matrixB[i][j] + "		" );
			}
			System.out.println();
		}

		//print Matrix init
		System.out.println("Matrix init");
		for(int i = 0; i < iy; i++){
			for(int j = 0; j < ix; j++){
				System.out.print(matrixInit[i][j] + "		" );
			}
			System.out.println();
		}
	}

	public static void emission(){
		double sum = 0;
		for(int i = 0; i < matrixInit.length; i++){
					System.out.println();
			for (int j = 0; j < matrixInit[0].length; j++){
				System.out.println(matrixInit[i][j]);
				System.out.println(matrixB[j][i]);
				
				sum += matrixInit[i][j]*matrixB[j][i];
			}
		}
		System.out.println(sum);
	}

    public static void main(String[] args) throws IOException {
    	getInput();

    	emission();
    }
}