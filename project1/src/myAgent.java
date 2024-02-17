import java.util.Random;
import java.util.ArrayList;

public class myAgent implements Agent
{
	private String role; // the name of this agent's role (white or black)
	private int playclock; // this is how much time (in seconds) we have before nextAction needs to return a move
	private boolean myTurn; // whether it is this agent's turn or not
	private int width, height; // dimensions of the board
	private Environment env;
	/*
		init(String role, int playclock) is called once before you have to select the first action. Use it to initialize the agent. role is either "white" or "black" and playclock is the number of seconds after which nextAction must return.
	*/
    public void init(String role, int width, int height, int playclock) {
		this.role = role;
		this.playclock = playclock;
		this.width = width;
		this.height = height;
		env = new Environment(width, height);
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
    	}
    	// update turn (above that line it myTurn is still for the previous state)
		myTurn = !myTurn;
		if (myTurn) { 
            System.out.println(env.curr_state);
            ArrayList<Move> moves = env.get_legal_moves(env.curr_state);
            Move move = moves.get(0);
            env.move(env.curr_state, move);
            return "(move " + (move.x1+1) + " " + (move.y1+1) + " " + (move.x2+1) + " " + (move.y2+1) + ")";
		} else {
            System.out.println(env.curr_state);
            ArrayList<Move> moves = env.get_legal_moves(env.curr_state);
            Move move = moves.get(0);
            env.move(env.curr_state, move);
			return "noop";
		}
	}
	// is called when the game is over or the match is aborted
	@Override
	public void cleanup() {
		// TODO: cleanup so that the agent is ready for the next match

	}

}


    


    

    