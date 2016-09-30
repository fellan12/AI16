
import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.*;


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
			emissionSeq[i][0] = stdin.nextInt();
		}

	}

	public static double[][] multiplyByMatrix(double[][] m1, double[][] m2) {
		double[][] mRes = null;
		try{
	        int m1ColLen = m1[0].length; 
	        int m2RowLen = m2.length;
	        int m1RowLen = m1.length;    
	        int m2ColLen = m2[0].length; 

	        if(m1ColLen != m2RowLen){
	        	System.out.println("m1ColLen != m2RowLen");
	        	System.out.println(m1ColLen + " != " + m2RowLen);
	        	return null; 
	        } 
	        
	        mRes = new double[m1RowLen][m2ColLen];
	        for(int i = 0; i < m1RowLen; i++) {         
	            for(int j = 0; j < m2ColLen; j++) {     
	                for(int k = 0; k < m1ColLen; k++) { 
	                    mRes[i][j] += m1[i][k] * m2[k][j];
	                }
	            }
	        }

        }catch(NullPointerException e){
        	e.printStackTrace();
    		System.out.println("NullPointerException in multiply");
    	} 
    		return mRes;
        
    }

    public static double[][] scalarByMatrix(double[][] m1, double[][] m2){
    	double[][] mRes = null;
    	try{
	    	int m1ColLen = m1[0].length; 
	        int m2RowLen = m2.length;
	        int m1RowLen = m1.length;    
       		int m2ColLen = m2[0].length;

       		if(m1ColLen != m2ColLen){
        	System.out.println("m1ColLen != m2ColLen");
        	System.out.println(m1ColLen + " != " + m2ColLen);
        	return null; 
	        } 
	        
	        mRes = new double[1][m1ColLen];
	        for(int i = 0; i < m1ColLen; i++) {     
	            mRes[0][i] += m1[0][i] * m2[0][i];
	        }
    	}catch(NullPointerException e){
    		e.printStackTrace();
    		System.out.println("NullPointerException in Scalar");
    	}
        return mRes; 

    }

	public static double[][] getB(int obs){
		double[][] maB = null;
		try{
			maB = new double[1][matrixB.length];
			for(int j = 0; j < matrixB.length; j++){
				maB[0][j] = matrixB[j][obs];		
			}
		}catch(NullPointerException e){
	      	e.printStackTrace();
			System.out.println("NullPointerException in GetB");
		}
		return maB;
	}

	public static void printMatrix(double[][] matrix){
		for(int i = 0; i < matrix.length; i++){
			for(int j = 0; j < matrix[0].length; j++){
				System.out.print(matrix[i][j] + "	");
			}
			System.out.println();
		}
	}

	public static void printDefaultMatrices(){
		//print Matrix A
		System.out.println("Matrix A");
		for(int i = 0; i < matrixA.length; i++){
			for(int j = 0; j < matrixA[0].length; j++){
				System.out.print(matrixA[i][j] + "		" );
			}
			System.out.println();
		}

		//print Matrix B
		System.out.println("Matrix B");
		for(int i = 0; i < matrixB.length; i++){
			for(int j = 0; j < matrixB[0].length; j++){
				System.out.print(matrixB[i][j] + "		" );
			}
			System.out.println();
		}

		//print Matrix init
		System.out.println("Matrix init");
		for(int i = 0; i < matrixInit.length; i++){
			for(int j = 0; j < matrixInit[0].length; j++){
				System.out.print(matrixInit[i][j] + "		" );
			}
			System.out.println();

		}

		//print Emission Sequence
		System.out.println("Emission Sequence");
		for(int i = 0; i < emissionSeq.length; i++){
				System.out.print(emissionSeq[i][0] + " " );
		}
		System.out.println();
	}

	public static void forward(){
		//printDefaultMatrices();
    	double[][] alpha = null;
    	double[][] alphaA = null;
    	double[][] maB = null;
    	//printMatrix(getB((int)emissionSeq[0][0]));
    	alpha = scalarByMatrix(matrixInit, getB((int)emissionSeq[0][0]));	//Alpha 1
    	//printMatrix(alpha);

    	for(int i = 1; i < emissionSeq.length; i++){						//Alpha{2-T}
    	//	System.out.println("Printing AlphaA");
    		alphaA = multiplyByMatrix(alpha, matrixA);
    	//	printMatrix(alphaA);

    	//	System.out.println("Printing maB");
    		maB = getB((int)emissionSeq[i][0]);
    	//	printMatrix(maB);

    	//	System.out.println("Printing Alpha");
    		alpha = scalarByMatrix(maB, alphaA);
    	//	printMatrix(alpha);

    	}
    	printMatrix(alpha);
		System.out.println(DoubleStream.of(alpha[0]).sum());
	}

    public static void main(String[] args) throws IOException {
    	getInput();
    	
    	forward();



    	 
    }
}