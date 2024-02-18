import java.util.ArrayList;

import javax.swing.border.EmptyBorder;

public class Environment {
    public State current_state;
    public int width, height;
    static final char BLACK = 'B';
    static final char WHITE = 'W';
    static final char EMPTY = ' ';

    public Environment(int width, int height) {
        this.width = width;
        this.height = height;
        this.current_state = new State(width, height);
    }

    public boolean is_terminal(State state) {
        ArrayList<Move> legalMoves = get_legal_moves(state);
        return legalMoves.size() == 0;
    }


    private boolean can_move_n_steps_forward(State state, int y, int max_height_black, int max_height_white) {
        if (state.white_turn && y <= max_height_white) return true;
        if (!state.white_turn && y >= max_height_black) return true;
        return false;
    }

    private void get_moves(State state, ArrayList<Move> moves, int y, int x) {
        char opponent = state.white_turn ? BLACK : WHITE;
        int one_step = state.white_turn ? 1 : -1;
        int two_step = state.white_turn ? 2 : -2;

        // two steps forward and one steo left/right
        if (can_move_n_steps_forward(state, y, 2, this.height - 3)) {
            if (x > 0 && state.board[y + two_step][x - 1] == EMPTY) 
                moves.add(new Move(x, y, x - 1, y + two_step));
            
            if (x < this.width - 1 && state.board[y + two_step][x + 1] == EMPTY)
                moves.add(new Move(x, y, x + 1, y + two_step));
        }

        // one step forward and two steps left/right
        if (can_move_n_steps_forward(state, y, 1, this.height - 2)){
            if (x > 1 && state.board[y + one_step][x - 2] == 0) {
                moves.add(new Move(x, y, x - 2, y + one_step));
            }
            if (x < this.width - 2 && state.board[y + one_step][x + 2] == 0) {
                moves.add(new Move(x, y, x + 2, y + one_step));
            }
        }

        // diagonal (capture)
        if (x > 0 && y > 0 && state.board[y + one_step][x - 1] == opponent) {
            moves.add(new Move(x, y, x - 1, y + one_step));
        }
        if (x < this.width - 1 && y > 0 && state.board[y + one_step][x + 1] == opponent) {
            moves.add(new Move(x, y, x + 1, y + one_step));
        }
    }

    public ArrayList<Move> get_legal_moves(State state) {
        ArrayList<Move> moves = new ArrayList<>();
        char friendly = state.white_turn ? WHITE : BLACK;

        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                if (state.board[y][x] == friendly) {
                    get_moves(state, moves, y, x);
                }
            }
        }
        return moves;
    }

    public void move(State state, Move move) {
        state.board[move.y2][move.x2] = state.board[move.y1][move.x1];
        state.board[move.y1][move.x1] = EMPTY;
        state.white_turn = !state.white_turn;
    }

    private boolean was_diagonal_move(Move move) {
        if (move.y2 - 1 == move.y1 && move.x2 - 1 == move.x1)
            return true;
        if (move.y2 + 1 == move.y1 && move.x2 - 1 == move.x1)
            return true;
        if (move.y2 - 1 == move.y1 && move.x2 + 1 == move.x1)
            return true;
        if (move.y2 + 1 == move.y1 && move.x2 + 1 == move.x1)
            return true;
        return false;
    }

    public void undo_move(State state, Move move) {
        if (was_diagonal_move(move)) {
            state.board[move.y1][move.x1] = state.board[move.y2][move.x2];
            state.board[move.y2][move.x2] = state.white_turn ? WHITE : BLACK;
        } else {
            char tmp = state.board[move.y1][move.x1];
            state.board[move.y1][move.x1] = state.board[move.y2][move.x2];
            state.board[move.y2][move.x2] = tmp;
        }
        state.white_turn = !state.white_turn;
    }

    public int evaluate(State state) {
        int white_count = 0;
        int black_count = 0;
        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                if (state.board[y][x] == WHITE) white_count++;
                if (state.board[y][x] == BLACK) black_count++;
            }
        }
        return white_count - black_count;
        
    }

    public State result(State state, Move move) {
        State new_state = state.deepCopy(state);
        move(new_state, move);
        return new_state;
    }

    public int utility(State state) {
        if (is_terminal(state)) {
            return evaluate(state);
        }
        return 0;
    }
}
