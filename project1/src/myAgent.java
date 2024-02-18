import java.util.ArrayList;

public class myAgent implements Agent {
    private String role; // the name of this agent's role (white or black)
	private int playclock; // this is how much time (in seconds) we have before nextAction needs to return a move
	private boolean myTurn; // whether it is this agent's turn or not
	private int width, height; // dimensions of the board
    private Environment env;
    private int stateExpansions;
    private long startTime;
	
	/*
		init(String role, int playclock) is called once before you have to select the first action. Use it to initialize the agent. role is either "white" or "black" and playclock is the number of seconds after which nextAction must return.
	*/
    public void init(String role, int width, int height, int playclock) {
		this.role = role;
		this.playclock = playclock;
		myTurn = !role.equals("white");
		this.width = width;
		this.height = height;
		// TODO: add your own initialization code here
        this.env = new Environment(width, height);
        this.startTime = System.currentTimeMillis();
    }


    public Move alpha_beta_root(State state, ArrayList<Move> moves) {
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        int v = Integer.MIN_VALUE;
        Move best_move = null;
        for (Move move : moves) {
            env.move(state, move);
            int new_v = alpha_beta_search(state, alpha, beta);
            if (new_v > v) {
                v = new_v;
                best_move = move;
            }
            env.undo_move(state, move);
        }
        return best_move;
        
    }

    public int alpha_beta_search(State state, int alpha, int beta) {
        if (state.white_turn) {
            return max_value(state, alpha, beta);
        } else {
            return min_value(state, alpha, beta);
        }
    }

    public int max_value(State state, int alpha, int beta) {
        int v = Integer.MIN_VALUE;
        ArrayList<Move> moves = env.get_legal_moves(state);
        for (Move move : moves) {
            env.move(state, move);
            v = Math.max(v, alpha_beta_search(state, alpha, beta));
            env.undo_move(state, move);
            if (v >= beta) {
                return v;
            }
            alpha = Math.max(alpha, v);
        }
        return v;
    }

    public int min_value(State state, int alpha, int beta) {
        int v = Integer.MAX_VALUE;
        ArrayList<Move> moves = env.get_legal_moves(state);
        for (Move move : moves) {
            env.move(state, move);
            v = Math.min(v, alpha_beta_search(state, alpha, beta));
            env.undo_move(state, move);
            if (v <= alpha) {
                return v;
            }
            beta = Math.min(beta, v);
        }
        return v;
    }

    
    public Move getBestMove(ArrayList<Move> legalMoves) {
        this.stateExpansions = 0; // Reset state expansions counter
        Move bestMove = null;
        int depth = 0;
        long searchStart = System.currentTimeMillis();
    
        // Iterative deepening loop
        while (true) {
            depth++;
            long iterationStart = System.currentTimeMillis();
            Move currentBestMove = alpha_beta_root(env.current_state, legalMoves);
            long iterationEnd = System.currentTimeMillis();
    
            // Check if time is up or if a winning move is found
            if (playclock - (iterationEnd - searchStart) / 1000 <= 1) break; // Adjust 1 to your safety buffer
    
            bestMove = currentBestMove; // Update best move
    
            // Outputting information
            System.out.println("Depth: " + depth + ", State Expansions: " + this.stateExpansions +
                    ", Time for last depth: " + (iterationEnd - iterationStart) + "ms");
        }
    
        long searchEnd = System.currentTimeMillis();
        System.out.println("Total search time: " + (searchEnd - searchStart) + "ms");
        System.out.println("Total state expansions: " + this.stateExpansions);
    
        return bestMove;
    }
    


	// lastMove is null the first time nextAction gets called (in the initial state)
    // otherwise it contains the coordinates x1,y1,x2,y2 of the move that the last player did
    public String nextAction(int[] lastMove) {
    	if (lastMove != null) {
    		int x1 = lastMove[0], y1 = lastMove[1], x2 = lastMove[2], y2 = lastMove[3];
    		String roleOfLastPlayer;
    		if (myTurn && role.equals("white") || !myTurn && role.equals("black")) {
    			roleOfLastPlayer = "white";
    		} else {
    			roleOfLastPlayer = "black";
    		}
   			System.out.println(roleOfLastPlayer + " moved from " + x1 + "," + y1 + " to " + x2 + "," + y2);
    		// TODO: 1. update your internal world model according to the action that was just executed
    		this.env.move(this.env.current_state, new Move(x1-1, y1-1, x2-1, y2-1));
    	}
		
    	// update turn (above that line it myTurn is still for the previous state)
		myTurn = !myTurn;
		if (myTurn) {
			// TODO: 2. run alpha-beta search to determine the best move
            // Move move = env.get_legal_moves(env.current_state).get(0);
            ArrayList<Move> moves = env.get_legal_moves(env.current_state);
            Move best_move = getBestMove(moves);

            System.out.println(env.current_state);
            return "(move " + (best_move.x1 + 1) + " " + (best_move.y1 + 1) + " " + (best_move.x2 + 1) + " " + (best_move.y2 + 1) + ")";
            // return "(move " + (move.x1 + 1) + " " + (move.y1 + 1) + " " + (move.x2 + 1) + " " + (move.y2 + 1) + ")";
		} else {
            System.out.println(env.current_state);
			return "noop";
		}
	}

	// is called when the game is over or the match is aborted
	@Override
	public void cleanup() {
		// TODO: cleanup so that the agent is ready for the next match
        this.env = null;
        this.playclock = 0;
        this.role = null;
        this.width = 0;
        this.height = 0;
	}
}

