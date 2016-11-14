class Backward{

    int numOfStates;
    int obsSequenceLength;

    public Backward(int numOfStates, int obsSequenceLength){
        this.numOfStates = numOfStates;
        this.obsSequenceLength = obsSequenceLength;
    }

    public double[][] backward(double[][] transMatrix, double[][] emissionMatrix, int[] obsSequence, double[] c) {
        double[][] beta = new double[numOfStates][obsSequenceLength];

        // compute b(t-1)(i)
        for (int i = 0; i < numOfStates; i++) {
            beta[i][obsSequenceLength-1] = c[obsSequenceLength-1];
        }

        // compute bt(i)
        for (int t = obsSequenceLength-2; t >= 0; t--) {
            for (int i = 0; i < numOfStates; i++) {
                beta[i][t] = 0;
                for (int j = 0; j < numOfStates; j++) {
                    beta[i][t] += transMatrix[i][j]*emissionMatrix[j][obsSequence[t+1]]*beta[j][t+1];
                } 
                beta[i][t] *= c[t];
            }
        }

        return beta;
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