import java.util.stream.*;

class Forward{
    
    static int numOfStates;
    static int obsSequenceLength;

    public Forward(int numOfStates, int obsSequenceLength){
        this.numOfStates = numOfStates;
        this.obsSequenceLength = obsSequenceLength;
    }

    public static double[][] forward(double[][] transMatrix, double[][] emissionMatrix, double[] initState, int[] obsSequence, double[] c){
    double[][] alpha = new double[numOfStates][obsSequenceLength];

        // compute a0(i)
        c[0] = 0;
        for (int i = 0; i < numOfStates; i++) {
            alpha[i][0] = initState[i]*emissionMatrix[i][obsSequence[0]];
            c[0] += alpha[i][0];
        }

        // c a0(i)
        c[0] = 1/c[0];
        for (int i = 0; i < numOfStates; i++) {
            alpha[i][0] *= c[0];
        }

        // compute at(i)
        for (int t = 1; t < obsSequenceLength; t++) {
            c[t] = 0;
            for (int i = 0; i < numOfStates; i++) {
                alpha[i][t] = 0;
                for (int j = 0; j < numOfStates; j++) {
                    alpha[i][t] += alpha[j][t-1]*transMatrix[j][i];
                }
                alpha[i][t] *= emissionMatrix[i][obsSequence[t]];
                c[t] += alpha[i][t];
            }

            // c at(i)
            c[t] = 1/c[t];
            for (int i = 0; i < numOfStates; i++) {
                alpha[i][t] *= c[t];
            }
        }
    
    return alpha;
    }
}