package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.Objects;


public class ChessBoard {
    private static final String LIGHT_SQUARE = EscapeSequences.SET_BG_COLOR_LIGHT_BROWN;
    private static final String DARK_SQUARE = EscapeSequences.SET_BG_COLOR_DARK_BROWN;
    private static final String BOARD_SURROUND = EscapeSequences.SET_BG_COLOR_DARK_GREY;
    private static final String EMPTY = EscapeSequences.EMPTY;

    private static final String[] WHITE_COL_LETTERS = {" a  ", " b  ", " c ", " d  ", " e ", " f  ", " g  ", " h "};
    private static final String[] ROW_NUMBERS = {"1", "2", "3", "4", "5", "6", "7", "8"};
    private static final String[] BLACK_COL_LETTERS = {" h  ", " g  ", " f ", " e  ", " d ", " c  ", " b  ", " a "};

    public static void createBoard(chess.ChessBoard gameBoard, ChessGame.TeamColor color, String[][] highlightedSquares) {
        String[][] board = new String[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = gameBoard.getPiece(new ChessPosition(row + 1, col + 1));
                if (piece == null) {
                    board[row][col] = EMPTY;
                    continue;
                }
                board = createSwitch(board, piece, row, col);
            }
        }
        if (color == ChessGame.TeamColor.WHITE) {
            printWhitePovBoard(board, highlightedSquares);
        } else {
            printBlackPovBoard(board, highlightedSquares);
        }
    }

    public static String[][] createSwitch(String[][] board, ChessPiece piece, int row, int col) {
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
        return board;
    }

    public static void printWhitePovBoard(String[][] board, String[][] highlightedSquares) {
        System.out.println();
        System.out.print(BOARD_SURROUND + "    " + EscapeSequences.RESET_BG_COLOR);
        for (String letter : WHITE_COL_LETTERS) {
            System.out.print(BOARD_SURROUND + EscapeSequences.SET_TEXT_COLOR_WHITE + letter +
                    EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);
        }
        System.out.print(BOARD_SURROUND + "    " + EscapeSequences.RESET_BG_COLOR);
        System.out.println();
        for (int i = 7; i >= 0; i--) {
            System.out.print(BOARD_SURROUND + EscapeSequences.SET_TEXT_COLOR_WHITE + "\u2003" + ROW_NUMBERS[i] + "\u2003" +
                    EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);
            for (int j = 0; j < 8; j++) {
                printSpot(board, i, j, highlightedSquares);
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
        System.out.println("\n");
    }

    public static void printBlackPovBoard(String[][] board, String[][] highlightedSquares) {
        System.out.println();
        System.out.print(BOARD_SURROUND + "    " + EscapeSequences.RESET_BG_COLOR);
        for (String letter : BLACK_COL_LETTERS) {
            System.out.print(BOARD_SURROUND + EscapeSequences.SET_TEXT_COLOR_WHITE + letter +
                    EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);
        }
        System.out.print(BOARD_SURROUND + "    " + EscapeSequences.RESET_BG_COLOR);
        System.out.println();
        for (int i = 0; i < 8; i++) {
            System.out.print(BOARD_SURROUND + EscapeSequences.SET_TEXT_COLOR_WHITE + "\u2003" + ROW_NUMBERS[i] + "\u2003" +
                    EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);
            for (int j = 7; j >= 0; j--) {
                printSpot(board, i, j, highlightedSquares);
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
        System.out.println("\n");
    }

    private static void printSpot(String[][] board, int i, int j, String[][] highlightedSquares) {
        String squareColor;
        if ((highlightedSquares != null) && (Objects.equals(highlightedSquares[i][j], "highlight"))) {
            squareColor = EscapeSequences.SET_BG_COLOR_YELLOW;
        } else if ((i + j) % 2 == 1) {
            squareColor = LIGHT_SQUARE;
        } else {
            squareColor = DARK_SQUARE;
        }
        System.out.print(squareColor + board[i][j] + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);
    }

    public static void validMovesBoard(ChessGame game, ChessGame.TeamColor color, ChessPosition startPos) {
        Collection<ChessMove> validMoves = new ChessGame(game).validMoves(startPos);
        if (validMoves.isEmpty()) {
            createBoard(game.getBoard(), color, null);
        }
        String[][] highlightedSquares = new String[8][8];
        for (ChessMove move : validMoves) {
            ChessPosition endPos = move.getEndPosition();
            int row = endPos.getRow();
            int tempRow = switch (row) {
                case 0 -> 8;
                case 1 -> 7;
                case 2 -> 6;
                case 3 -> 5;
                case 4 -> 4;
                case 5 -> 3;
                case 6 -> 2;
                case 7 -> 1;
                case 8 -> 0;
                default -> row;
            };
            int col = endPos.getColumn();
            highlightedSquares[tempRow - 1][col] = "highlight";
        }
        createBoard(game.getBoard(), color, highlightedSquares);
    }
}
