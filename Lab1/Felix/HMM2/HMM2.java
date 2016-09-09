
import java.util.Scanner;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

class HMM2 {

	static double[][] matrixA;
	static double[][] matrixB;
	static double[][] matrixInit;
	static double[][] emissionSeq;

	public static void getInput() throws IOException{
		Scanner stdin = new Scanner(System.in);
		int ax = stdin.nextInt();
		int ay = stdin.nextInt();

		matrixA = new double[ax][ay];

		for(int i = 0; i < ax; i++){
			for(int j = 0; j < ay; j++){
				matrixA[i][j] = stdin.nextDouble();
			}
		}

		int bx = stdin.nextInt();
		int by = stdin.nextInt();


		matrixB = new double[bx][by];

		for(int i = 0; i < bx; i++){
			for(int j = 0; j < by; j++){
				matrixB[i][j] = stdin.nextDouble();
			}
		}

		int ix = stdin.nextInt();
		int iy = stdin.nextInt();

		matrixInit = new double[ix][iy];
		for(int i = 0; i < ix; i++){
			for(int j = 0; j < iy; j++){
				matrixInit[i][j] = stdin.nextDouble();
			}
		}

		int eSeq = stdin.nextInt();
		emissionSeq = new double[eSeq][1];
		for(int i = 0; i < eSeq; i++){
			emissionSeq[i][1] = stdin.nextInt();
		}

		
		//print Matrix A
		System.out.println("Matrix A");
		for(int i = 0; i < ax; i++){
			for(int j = 0; j < ay; j++){
				System.out.print(matrixA[i][j] + "		" );
			}
			System.out.println();
		}

		//print Matrix B
		System.out.println("Matrix B");
		for(int i = 0; i < bx; i++){
			for(int j = 0; j < by; j++){
				System.out.print(matrixB[i][j] + "		" );
			}
			System.out.println();
		}

		//print Matrix init
		System.out.println("Matrix init");
		for(int i = 0; i < ix; i++){
			for(int j = 0; j < iy; j++){
				System.out.print(matrixInit[i][j] + "		" );
			}
			System.out.println();

		}

		//print Emission Sequence
		System.out.println("Emission Sequence");
		for(int i = 0; i < ix; i++){
				System.out.print(emissionSeq[i][0] + "		" );
		}
	}

	public static double[][] multiplyByMatrix(double[][] m1, double[][] m2) {
        int m1ColLen = m1[0].length; 
        int m2RowLen = m2.length;    
        if(m1ColLen != m2RowLen){
        	System.out.println("m1ColLen != m2RowLen");
        	return null; 
        } 
        int mRowLen = m1.length;    
        int mColLen = m2[0].length; 
        double[][] mRes = new double[mRowLen][mColLen];
        for(int i = 0; i < mRowLen; i++) {         
            for(int j = 0; j < mColLen; j++) {     
                for(int k = 0; k < m1ColLen; k++) { 
                    mRes[i][j] += m1[i][k] * m2[k][j];
                }
            }
        }
        return mRes;
    }

	public static double[][] emission(){
		//(Init * MatrixA) * MatrixB = MatrixRes
		//( 1x4  *   4x4 ) * 4x3	 =  1x3
		return multiplyByMatrix(multiplyByMatrix(matrixInit, matrixA), matrixB);
	}

    public static void main(String[] args) throws IOException {
    	getInput();    
    	double[][] out = new double[1][matrixB.length];
    	double[][] maB2 = new double[1][matrixB.length];
    	for(int i = 0; i < emissionSeq.length; i++){
    		for(int j = 0; j < matrixB.length; j++){
    			int tmp = (int)emissionSeq[j][0];
    			maB2[0][j] = matrixB[tmp][j];
    		}
    		out = multiplyByMatrix(matrixInit,maB2);
    	}

    }
}