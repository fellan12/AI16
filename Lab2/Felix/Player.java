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
        GameState next = null;          //Going to be the best move
        int res = -Integer.MAX_VALUE;   //Going to be the value for the best move

        for(GameState state : nextStates){
            tmp = alphabeta(state, 20, "A");    //Run alpha-beta on a state
            if(res < tmp){      //If the value is better then det prevous
                res = tmp;      //Save the value
                next = state;   //Save the state
            }
        }
        
        return next;            //Return the state
    }

    public static int alphabeta(GameState currState, int depth, String player){
        //state: the current state we are analyzing
        //alpha: the current best value achievable by A
        //beta: the current best value acheivable by B
        //player: the current player
        //returns the minimax value of the state
        int v = 0;
        int alpha = -Integer.MAX_VALUE;
        int beta = Integer.MAX_VALUE;
        Vector<GameState> nextStates = new Vector<GameState>();
        currState.findPossibleMoves(nextStates);


        if (depth == 0 || nextStates == null){
            //terminalstate
            v = utility_function(currState);

        }else if (player == "A"){
            v = -Integer.MAX_VALUE;
            for(GameState child : nextStates)
            v = Math.max(v, alphabeta(child, depth-1, "B"));
            alpha = Math.max(alpha,v);
            if(beta <= alpha){
                return v; //betaprune
            }

        }else{//player=B
            v= Integer.MAX_VALUE;
            for(GameState child : nextStates){
                v=Math.min(v,alphabeta(child,depth-1, "A"));
                beta = Math.min(beta,v);
            }
            if (beta <= alpha){
                return v;
            }
        }//alphaprune
        return v;
    }

    public static int utility_function(GameState state){
        int sum = 0;
        //Colons
        for(int i = 0; i < 4; i++)
            for(int j = 0; j < 4; j++){
                if(state.at(j,i) == 1){
                    sum++;
                }
            }

        //Rows
        for(int i = 0; i < 4; i = i+4){
            for(int j = 0; j < 4; j++){
                if(state.at(i, state.cellToCol(j)) == 1){
                    sum++;
                }
            }
        }

        for(int i = 0; i < 4; i++){
            if(state.at(i,i) == 1){
                sum++;
            }
            if(state.at(12-4*i, 12-5*i) == 1){
                sum++;
            }
        }
        return sum;
    }  
}