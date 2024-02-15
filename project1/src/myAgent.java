import java.util.Random;

public class myAgent implements Agent
{
	private Random random = new Random();

	private String role; // the name of this agent's role (white or black)
	private int playclock; // this is how much time (in seconds) we have before nextAction needs to return a move
	private boolean myTurn; // whether it is this agent's turn or not
	private int width, height; // dimensions of the board
    private int[][] board;
    private int alpha = Integer.MIN_VALUE;
    private int beta = Integer.MAX_VALUE;
	
	/*
		init(String role, int playclock) is called once before you have to select the first action. Use it to initialize the agent. role is either "white" or "black" and playclock is the number of seconds after which nextAction must return.
	*/
    public void init(String role, int width, int height, int playclock) {
		this.role = role;
		this.playclock = playclock;
		myTurn = !role.equals("white");
		this.width = width;
		this.height = height;
        board = new int[width][height];
		// TODO: add your own initialization code here
    }

    public int[][] initializeBoard(int[][] board){
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                if (i == 0 && j == 0){
                    board[i][j] = 1;
                } else if (i == width - 1 && j == height - 1){
                    board[i][j] = 2;
                } else {
                board[i][j] = 0;
                }   
            }
        }
        return board;
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
            board[x2][y2] = board[x1][y1];
            board[x1][y1] = 0;
            System.out.println("Board: ");
    	}
    	// update turn (above that line it myTurn is still for the previous state)
		myTurn = !myTurn;
		if (myTurn) {
			// TODO: 2. run alpha-beta search to determine the best move
            int[][] legalmoves = findLegalMove(board, role);
            System.out.println(legalmoves);
			return "(move)";
		} else {
            int[][] legalmoves = findLegalMove(board, role);
            System.out.println(legalmoves);
			return "noop";
		}
	}

    private void print(int[][] legalmoves) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'print'");
    }

    //function to find legal move
    public int[][] findLegalMove(int[][] board, String role){
        int[][] legalMove = new int[width][height];
        for(int i = 0; i < width; i++){
            for(int j = 0; j < height; j++){
                if(board[i][j] == 0){
                    legalMove[i][j] = 1;
                }
            }
        }
        return legalMove;
    }


	// is called when the game is over or the match is aborted
	@Override
	public void cleanup() {
		// TODO: cleanup so that the agent is ready for the next match
	}

}


    


    

    