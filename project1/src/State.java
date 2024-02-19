import java.util.Arrays;

public class State {
    public char[][] board;
    public boolean white_turn;
    static final char BLACK = 'B';
    static final char WHITE = 'W';
    static final char EMPTY = ' ';
    private final int width;
    private final int height;
    
    public State(int width, int height) {
        this.board = new char[width][height];
        this.white_turn = true;
        this.width = width;
        this.height = height;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (i < 2)
                    this.board[i][j] = WHITE;
                else if (i > height - 3)
                    this.board[i][j] = BLACK;
                else
                    this.board[i][j] = EMPTY;
            }
        }
    }

    public State deepCopy(State state) {
        State newState = new State(this.width, state.board[0].length);
        for (int i = 0; i < state.board[0].length; i++) {
            newState.board[i] = Arrays.copyOf(state.board[i], state.board[i].length);
        }
        newState.white_turn = state.white_turn;
        return newState;

    }

    

    public String toString() {
        int dash_count = this.width * 5 - 6;
        String line = "\n" + " " + "-".repeat(dash_count) + "\n";
        String result = line;

        for (int i = 0; i < this.width; i++) {
            result += new String(this.board[i]).replaceAll("", " | ");
            result += line;
        }
        return result;
    }

}