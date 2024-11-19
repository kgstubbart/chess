package ui;

public class ChessBoard {
    private static final String LIGHT_SQUARE = EscapeSequences.SET_BG_COLOR_LIGHT_BROWN;
    private static final String DARK_SQUARE = EscapeSequences.SET_BG_COLOR_DARK_BROWN;
    private static final String BOARD_SURROUND = EscapeSequences.SET_BG_COLOR_DARK_GREY;
    private static final String EMPTY = EscapeSequences.EMPTY;

    private static final String[] WHITE_COL_LETTERS = {" a  ", " b  ", " c ", " d  ", " e ", " f  ", " g  ", " h "};
    private static final String[] ROW_NUMBERS = {"8", "7", "6", "5", "4", "3", "2", "1"};
    private static final String[] BLACK_COL_LETTERS = {" h  ", " g  ", " f ", " e  ", " d ", " c  ", " b  ", " a "};

    private static final String[][] BOARD = {
            {EscapeSequences.BLACK_ROOK, EscapeSequences.BLACK_KNIGHT, EscapeSequences.BLACK_BISHOP, EscapeSequences.BLACK_QUEEN,
                    EscapeSequences.BLACK_KING, EscapeSequences.BLACK_BISHOP, EscapeSequences.BLACK_KNIGHT, EscapeSequences.BLACK_ROOK},
            {EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN,
                    EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN, EscapeSequences.BLACK_PAWN},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY},
            {EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN,
                    EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN, EscapeSequences.WHITE_PAWN},
            {EscapeSequences.WHITE_ROOK, EscapeSequences.WHITE_KNIGHT, EscapeSequences.WHITE_BISHOP, EscapeSequences.WHITE_QUEEN,
                    EscapeSequences.WHITE_KING, EscapeSequences.WHITE_BISHOP, EscapeSequences.WHITE_KNIGHT, EscapeSequences.WHITE_ROOK}
    };

    public static void printWhitePovBoard() {
        System.out.println();
        System.out.print(BOARD_SURROUND + "    " + EscapeSequences.RESET_BG_COLOR);
        for (String letter : WHITE_COL_LETTERS) {
            System.out.print(BOARD_SURROUND + EscapeSequences.SET_TEXT_COLOR_WHITE + letter +
                    EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);
        }
        System.out.print(BOARD_SURROUND + "    " + EscapeSequences.RESET_BG_COLOR);
        System.out.println();
        for (int i = 0; i < 8; i++) {
            System.out.print(BOARD_SURROUND + EscapeSequences.SET_TEXT_COLOR_WHITE + "\u2003" + ROW_NUMBERS[i] + "\u2003" +
                    EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);
            for (int j = 0; j < 8; j++) {
                String squareColor;
                if ((i + j) % 2 == 0) {
                    squareColor = LIGHT_SQUARE;
                }
                else {
                    squareColor = DARK_SQUARE;
                }
                System.out.print(squareColor + BOARD[i][j] + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);
            }
            System.out.print(BOARD_SURROUND + EscapeSequences.SET_TEXT_COLOR_WHITE + "\u2003" + ROW_NUMBERS[i] + "\u2003" +
                    EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);
            System.out.println();
        }
        System.out.print(BOARD_SURROUND + "    " + EscapeSequences.RESET_BG_COLOR);
        for (String letter : WHITE_COL_LETTERS) {
            System.out.print(BOARD_SURROUND + EscapeSequences.SET_TEXT_COLOR_WHITE + letter +
                    EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);
        }
        System.out.print(BOARD_SURROUND + "    " + EscapeSequences.RESET_BG_COLOR);
        System.out.println();
    }

    public static void printBlackPovBoard() {
        System.out.println();
        System.out.print(BOARD_SURROUND + "    " + EscapeSequences.RESET_BG_COLOR);
        for (String letter : BLACK_COL_LETTERS) {
            System.out.print(BOARD_SURROUND + EscapeSequences.SET_TEXT_COLOR_WHITE + letter +
                    EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);
        }
        System.out.print(BOARD_SURROUND + "    " + EscapeSequences.RESET_BG_COLOR);
        System.out.println();
        for (int i = 7; i >= 0; i--) {
            System.out.print(BOARD_SURROUND + EscapeSequences.SET_TEXT_COLOR_WHITE + "\u2003" + ROW_NUMBERS[i] + "\u2003" +
                    EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);
            for (int j = 7; j >= 0; j--) {
                String squareColor;
                if ((i + j) % 2 == 0) {
                    squareColor = LIGHT_SQUARE;
                }
                else {
                    squareColor = DARK_SQUARE;
                }
                System.out.print(squareColor + BOARD[i][j] + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);
            }
            System.out.print(BOARD_SURROUND + EscapeSequences.SET_TEXT_COLOR_WHITE + "\u2003" + ROW_NUMBERS[i] + "\u2003" +
                    EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);
            System.out.println();
        }
        System.out.print(BOARD_SURROUND + "    " + EscapeSequences.RESET_BG_COLOR);
        for (String letter : BLACK_COL_LETTERS) {
            System.out.print(BOARD_SURROUND + EscapeSequences.SET_TEXT_COLOR_WHITE + letter +
                    EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);
        }
        System.out.print(BOARD_SURROUND + "    " + EscapeSequences.RESET_BG_COLOR);
        System.out.println();
    }
}
