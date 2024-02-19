import java.lang.reflect.Array;
import java.util.ArrayList;

public class myAgent implements Agent {
    private String role; // the name of this agent's role (white or black)
	private int playclock; // this is how much time (in seconds) we have before nextAction needs to return a move
	private boolean myTurn; // whether it is this agent's turn or not
	private int width, height; // dimensions of the board
    private Environment env;
    private int stateExpansions;
    private long startTime;
    private int totalExpansions;
	private double totalTime;
	private int totalDepthLimit;
    private static final int MAX_VALUE = Integer.MAX_VALUE;
	private static final int MIN_VALUE = Integer.MIN_VALUE;
	
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

    
    public int alphaBetaRoot(State state, int depth, int alpha, int beta, boolean maxingPlayer) {
        totalDepthLimit = Math.max(totalDepthLimit, depth);
        totalExpansions++;
        
        if (depth == 0 || env.is_terminal(state)) {
            return env.evaluate(state);
        }

        return maxingPlayer ? alphaBetaMax(state, depth, alpha, beta) : alphaBetaMin(state, depth, alpha, beta);
    }

    public int alphaBetaMax(State state, int depth, int alpha, int beta) {
        int value = MIN_VALUE;
        ArrayList<Move> next_moves = env.get_legal_moves(state);
        for (Move move : next_moves) {
            value = Math.max(value, alphaBetaRoot(env.result(state, move), depth - 1, alpha, beta, false));
            alpha = Math.max(alpha, value);
            if (alpha >= beta) {
                break;
            }
        }
        return value;
    }

    public int alphaBetaMin(State state, int depth, int alpha, int beta) {
        int value = MAX_VALUE;
        ArrayList<Move> next_moves = env.get_legal_moves(state);
        for (Move move : next_moves) {
            value = Math.min(value, alphaBetaRoot(env.result(state, move), depth - 1, alpha, beta, true));
            beta = Math.min(beta, value);
            if (alpha >= beta) {
                break;
            }
        }
        return value;
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
    		this.env.move(this.env.current_state, new Move(x1-1, y1-1, x2-1, y2-1));
    	}
		
    	// update turn (above that line it myTurn is still for the previous state)
		myTurn = !myTurn;
		if (myTurn) {
			// TODO: 2. run alpha-beta search to determine the best move
            // Move move = env.get_legal_moves(env.current_state).get(0);
            long startTime = System.currentTimeMillis();
            ArrayList<Move> next_moves = this.env.get_legal_moves(this.env.current_state);
            int bestValue = MIN_VALUE;
            Move bestMove = null;
            int depth = 1;
            boolean maxingPlayer = false;
            Move currentBestMove = null;
            while (true) {
                for (Move move : next_moves) {
                    int newValue = alphaBetaRoot(env.result(env.current_state, move), depth, MIN_VALUE, MAX_VALUE, maxingPlayer);
                    if (newValue > bestValue) {
                        bestValue = newValue;
                        currentBestMove = move;
                    }
                    
                }
                bestMove = currentBestMove;
                long endTime = System.currentTimeMillis();
                double duration = (endTime - startTime) / 1000.0;
                System.out.println("Depth: " + depth + " Time: " + duration);
                // if the time is up or the depth limit is reached
                if (duration >= playclock - 1 || depth >= 5) {
                    System.out.println(playclock);
                    System.out.println("Time is up");
                    break;
                }
                // System.out.println("Best Move111: " + bestMove + " Value: " + bestValue);
                depth++;
            }

            if (bestMove != null) {
                return "(move " + (bestMove.x1 + 1) + " " + (bestMove.y1 + 1) + " " + (bestMove.x2 + 1) + " " + (bestMove.y2 + 1) + ")";
            } else {
                System.out.println(env.current_state);
                return "noop";
            }
        }
        else {
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

