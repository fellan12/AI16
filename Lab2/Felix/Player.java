import java.util.*;
import java.lang.Math.*;

public class Player {
    /**
     * Performs a move
     *
     * @param gameState
     *            the current state of the board
     * @param deadline
     *            time before which we must have returned
     * @return the next state the board is in after our move
     */
    public GameState play(final GameState gameState, final Deadline deadline) {
       
        Vector<GameState> nextStates = new Vector<GameState>();
        gameState.findPossibleMoves(nextStates);

        if (nextStates.isEmpty()) {
            // Must play "pass" move if there are no other moves possible.
            return new GameState(gameState, new Move());
        }

        GameState next = nextStates.firstElement();        //Going to be the best move
        int res = Integer.MIN_VALUE;                      //Going to be the value for the best move
        int depth = 5;

        for(GameState state : nextStates){
            int tmp = alphabeta(state, Integer.MIN_VALUE, Integer.MAX_VALUE, depth, gameState.getNextPerson());    //Start alphabeta with the next person (O), because you (X) starts
            if(res < tmp){      //If the value is better then det prevous
                res = tmp;      //Save the value
                next = state;   //Save the state
            }
        }
        return next;            //Return the state
        
    }

    private int alphabeta(GameState currState, int alpha, int beta, int depth, int player){
        //state: the current state we are analyzing
        //alpha: the current best value achievable by A
        //beta: the current best value acheivable by B
        //player: the current player
        //returns the minimax value of the state
        if(currState.isEOG()){
            return gamma(currState, player);
        }
        else if (depth == 0) {
            //terminalstate
            return eval(currState);
        }

        int v = 0;
        Vector<GameState> nextStates = new Vector<GameState>();
        currState.findPossibleMoves(nextStates);
        if (!nextStates.isEmpty() && player == Constants.CELL_X){
            v = Integer.MIN_VALUE;
            for(GameState child : nextStates){
                v = Math.max(v, alphabeta(child, alpha, beta, depth-1, Constants.CELL_O));
                alpha = Math.max(alpha,v);
            
                if(beta <= alpha){
                    break; //betaprune
                }
            }
        }
        else if (!nextStates.isEmpty() && player == Constants.CELL_O){ 
            v = Integer.MAX_VALUE;
            for(GameState child : nextStates){
                v=Math.min(v,alphabeta(child, alpha, beta, depth-1, Constants.CELL_X));
                beta = Math.min(beta,v);

                if (beta <= alpha){
                    break;
                }
            }
        }//alphaprune
        return v;
    }

    private int gamma(GameState state, int player){
        int sum = 0;
        if (player == Constants.CELL_O) {
            if (state.isOWin()) {
                sum = Integer.MAX_VALUE;
            } else if (state.isXWin()) {
                sum = Integer.MIN_VALUE;
            }
        } else if (player == Constants.CELL_X) {
            if (state.isXWin()) {
                sum = Integer.MAX_VALUE;
            } else if (state.isOWin()) {
                sum = Integer.MIN_VALUE;
            }
       
        }else{
                sum = 0;       //Tie
        }
        return sum;
    }

    private int eval(GameState state){
        int sum = 0;
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                //Colon
                if(state.at(j,i) == Constants.CELL_X){
                    sum++;
                }
                //Row
                if(state.at(i, j) == Constants.CELL_X){
                    sum++;
                }
            }

            //Diagonals
            if(state.at(i,i) == 1){
                sum++;
            }
            if(state.at(12-4*i, 12-5*i) == Constants.CELL_X){
                sum++;
            }
        }
       return sum;
    }
}