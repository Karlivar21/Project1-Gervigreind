public class State {
    public int[][] board;
    public boolean white_turn;
    public int white = 1;
    public int black = 2;
    public int empty = 0;
    private int width;
    private int height;

    public State(int width, int height) {
        this.board = new int[width][height];
        this.white_turn = true;
        this.width = width;
        this.height = height;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i < 2) {
                    this.board[i][j] = white;
                } else if (i > height - 3) {
                    this.board[i][j] = black;
                } else {
                    this.board[i][j] = empty;
                }
            }
        }
    }
    public String toString() {
        String str = "";
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                str += this.board[i][j] + " ";
            }
            str += "\n";
        }
        return str;
    }


}
