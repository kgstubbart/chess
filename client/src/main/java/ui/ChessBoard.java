package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

public class ChessBoard {
    private static final String LIGHT_SQUARE = EscapeSequences.SET_BG_COLOR_LIGHT_BROWN;
    private static final String DARK_SQUARE = EscapeSequences.SET_BG_COLOR_DARK_BROWN;
    private static final String BOARD_SURROUND = EscapeSequences.SET_BG_COLOR_DARK_GREY;
    private static final String EMPTY = EscapeSequences.EMPTY;

    private static final String[] WHITE_COL_LETTERS = {" a  ", " b  ", " c ", " d  ", " e ", " f  ", " g  ", " h "};
    private static final String[] ROW_NUMBERS = {"8", "7", "6", "5", "4", "3", "2", "1"};
    private static final String[] BLACK_COL_LETTERS = {" h  ", " g  ", " f ", " e  ", " d ", " c  ", " b  ", " a "};

    public static void createBoard(chess.ChessBoard gameBoard, ChessGame.TeamColor color) {
        String[][] board = new String[8][8];
        for (int row = 0; row <= 7; row++) {
            for (int col = 0; col <= 7; col++) {
                ChessPiece piece = gameBoard.getPiece(new ChessPosition(row, col));
                if (piece == null) {
                    board[row][col] = EMPTY;
                    continue;
                }
                switch (piece.getPieceType()) {
                    case KING -> {
                        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                            board[row][col] = EscapeSequences.WHITE_KING;
                        } else {
                            board[row][col] = EscapeSequences.BLACK_KING;
                        }
                    }
                    case QUEEN -> {
                        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                            board[row][col] = EscapeSequences.WHITE_QUEEN;
                        } else {
                            board[row][col] = EscapeSequences.BLACK_QUEEN;
                        }
                    }
                    case ROOK -> {
                        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                            board[row][col] = EscapeSequences.WHITE_ROOK;
                        } else {
                            board[row][col] = EscapeSequences.BLACK_ROOK;
                        }
                    }
                    case BISHOP -> {
                        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                            board[row][col] = EscapeSequences.WHITE_BISHOP;
                        } else {
                            board[row][col] = EscapeSequences.BLACK_BISHOP;
                        }
                    }
                    case KNIGHT -> {
                        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                            board[row][col] = EscapeSequences.WHITE_KNIGHT;
                        } else {
                            board[row][col] = EscapeSequences.BLACK_KNIGHT;
                        }
                    }
                    case PAWN -> {
                        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                            board[row][col] = EscapeSequences.WHITE_PAWN;
                        } else {
                            board[row][col] = EscapeSequences.BLACK_PAWN;
                        }
                    }
                    default -> board[row][col] = EMPTY;
                }
            }
        }
        if (color == ChessGame.TeamColor.WHITE) {
            printWhitePovBoard(board);
        } else {
            printBlackPovBoard(board);
        }
    }

    public static void printWhitePovBoard(String[][] board) {
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
                printSpot(board, i, j);
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

    public static void printBlackPovBoard(String[][] board) {
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
                printSpot(board, i, j);
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

    private static void printSpot(String[][] board, int i, int j) {
        String squareColor;
        if ((i + j) % 2 == 0) {
            squareColor = LIGHT_SQUARE;
        } else {
            squareColor = DARK_SQUARE;
        }
        System.out.print(squareColor + board[i][j] + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);
    }
}
