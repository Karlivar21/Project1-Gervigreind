import java.util.ArrayList;

public class Environment {
    public int width;
    public int height;
    public State curr_state;
    
    public Environment(int width, int height) {
        this.width = width;
        this.height = height;
        this.curr_state = new State(width, height);
    }

    private boolean can_move_n_step_forward(State state, int y, int max_heightblack, int max_heightwhite) {
        if (state.white_turn && y <= max_heightwhite) return true;
        if (!state.white_turn && y >= max_heightblack) return true;
        return false;
    }

    private void get_moves(State state, ArrayList<Move> moves, int x, int y) {
        int opp = state.white_turn ? state.black : state.white;
        int one_step = state.white_turn ? 1 : -1;
        int two_step = state.white_turn ? 2 : -2;

        //two steps forward and one left or right
        if (can_move_n_step_forward(state, y, 2, this.height - 3)){
            if (x > 0 && state.board[y + two_step][x - 1] == 0) {
                moves.add(new Move(x, y, x - 1, y + two_step));
            }
            if (x < this.width - 1 && state.board[y + two_step][x + 1] == 0) {
                moves.add(new Move(x, y, x + 1, y + two_step));
            }
        }
        // one step forward and two left or right
        if (can_move_n_step_forward(state, y, 1, this.height - 2)){
            if (x > 1 && state.board[y + one_step][x - 2] == 0) {
                moves.add(new Move(x, y, x - 2, y + one_step));
            }
            if (x < this.width - 2 && state.board[y + one_step][x + 2] == 0) {
                moves.add(new Move(x, y, x + 2, y + one_step));
            }
        }
        
        // diagonal moves if there is an opponent piece
        if (x > 0 && y > 0 && state.board[y + one_step][x - 1] == opp) {
            moves.add(new Move(x, y, x - 1, y + one_step));
        }
        if (x < this.width - 1 && y > 0 && state.board[y + one_step][x + 1] == opp) {
            moves.add(new Move(x, y, x + 1, y + one_step));
        }
    }


    public ArrayList<Move> get_legal_moves(State state) {
        ArrayList<Move> moves = new ArrayList<>();
        int friendly = state.white_turn ? state.white : state.black;
        
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                if (state.board[y][x] == friendly) {
                    get_moves(state, moves, x, y);
                }
            }
        }
        return moves;
        
    }

    public void move(State state, Move move) {
        state.board[move.y2][move.x2] = state.board[move.y1][move.x1];
        state.board[move.y1][move.x1] = 0;
        state.white_turn = !state.white_turn;
        curr_state.toString();

    }

    public boolean was_diagonal_move (Move move) {
        if (move.y2 - 1 == move.y1 && move.x2 - 1 == move.y1) {
            return true;
        }
        if (move.y2 - 1 == move.y1 && move.x2 + 1 == move.y1) {
            return true;
        }
        if (move.y2 + 1 == move.y1 && move.x2 - 1 == move.y1) {
            return true;
        }
        if (move.y2 + 1 == move.y1 && move.x2 + 1 == move.y1) {
            return true;
        }
        return false;
    }



    public void undo_move (State state, Move move) {
        if (was_diagonal_move(move)) {
            state.board[move.y1][move.x1] = state.board[move.y2][move.x2];
            state.board[move.y2][move.x2] = state.white_turn ? 2 : 1;
        } else {
            int temp = state.board[move.y1][move.x1];
            state.board[move.y1][move.x1] = state.board[move.y2][move.x2];
            state.board[move.y2][move.x2] = temp;
        }

        state.white_turn = !state.white_turn;

    }

    public void evaluate (int[][] board) {

    }

}
