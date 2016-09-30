class GammaDigamma{

    static double[][] gamma;
    static double[][][] digamma;
       
    static int numOfStates;
    static int obsSequenceLength;


    public GammaDigamma(int numOfStates, int obsSequenceLength){
        this.numOfStates = numOfStates;
        this.obsSequenceLength = obsSequenceLength;
    }

    public static void gammaDigamma(double[][] transMatrix, double[][] emissionMatrix, int[] obsSequence, double[][] alpha, double[][] beta){
    	double denom = 0;

        gamma = new double[numOfStates][obsSequenceLength];
        digamma = new double[numOfStates][numOfStates][obsSequenceLength];

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

    public static double[][] getGamma(){
        return gamma;
    }

    public static double[][][] getDiGamma(){
        return digamma;
    }

     public static void printMatrix(double[][] matrix){
        for(int i = 0; i < matrix.length; i++){
            for(int j = 0; j < matrix[0].length; j++){
                System.out.print(matrix[i][j] + "   ");
            }
            System.out.println();
        }
        System.out.println();
    }

}