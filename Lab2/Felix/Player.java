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

        if (nextStates.size() == 0) {
            // Must play "pass" move if there are no other moves possible.
            return new GameState(gameState, new Move());
        }
        int tmp;                        
        GameState next = nextStates.get(0);          //Going to be the best move
        int res = -Integer.MAX_VALUE;   //Going to be the value for the best move

        for(GameState state : nextStates){
            tmp = alphabeta(Integer.MAX_VALUE, -Integer.MAX_VALUE, state, 20, "A");    //Run alpha-beta on a state
            if(res < tmp){      //If the value is better then det prevous
                res = tmp;      //Save the value
                next = state;   //Save the state
            }
        }
        
        return next;            //Return the state
    }

    public static int alphabeta(int alpha, int beta, GameState currState, int depth, String player){
        //state: the current state we are analyzing
        //alpha: the current best value achievable by A
        //beta: the current best value acheivable by B
        //player: the current player
        //returns the minimax value of the state
        int v = 0;

        Vector<GameState> nextStates = new Vector<GameState>();
                
        currState.findPossibleMoves(nextStates);


        if (depth == 0 || nextStates.isEmpty()) {
            //terminalstate
            v = utility_function(currState, player);
        }


        else if (player == "X"){
            v = -Integer.MAX_VALUE;
            for(GameState child : nextStates){
                v = Math.max(v, alphabeta(alpha, beta, child, depth-1, "O"));
                alpha = Math.max(alpha,v);
            
                if(beta <= alpha){
                    break; //betaprune
                }
            }
        }
        else if (player == "O"){ 
            v = Integer.MAX_VALUE;
            for(GameState child : nextStates){
                v=Math.min(v,alphabeta(alpha, beta, child,depth-1, "X"));
                beta = Math.min(beta,v);

                if (beta <= alpha){
                    break;
                }
            }
        }//alphaprune
        
        return v;
    }

    public static int utility_function(GameState state, String player){
        int sum = 0;

        if (state.isEOG() ) {
            // check who won
            if (state.isXWin()) {
                sum = Integer.MAX_VALUE;
            }
            else if(state.isOWin()) {
                sum = -Integer.MAX_VALUE;
            }
        }

        else {
        
            int x = (player == "X" ? Constants.CELL_X : Constants.CELL_O);

            //Colons
            for(int i = 0; i < GameState.BOARD_SIZE; i++)
                for(int j = 0; j < GameState.BOARD_SIZE; j++) {
                    if(state.at(j,i) == x) {
                        sum++;
                    }
                }

            //Rows
            for(int i = 0; i < GameState.BOARD_SIZE; i = i+4) {
                for(int j = 0; j < GameState.BOARD_SIZE; j++) {
                    if(state.at(i,j) == x) {
                        sum++;
                    }
                }
            }

            for(int i = 0; i < GameState.BOARD_SIZE; i++) {
                if(state.at(i,i) == x) {
                    sum++;
                }
                if(state.at(i, GameState.BOARD_SIZE-(i+1)) == x){
                    sum++;
                }
            }
        }
        return sum;
    }  
}