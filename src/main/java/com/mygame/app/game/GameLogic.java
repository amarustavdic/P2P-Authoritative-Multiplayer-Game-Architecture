package com.mygame.app.game;

import java.util.Arrays;
import java.util.Random;

public class GameLogic {

    private static final int COLUMNS = 7;
    private static final int ROWS = 6;
    private static char[][] GAME_MTX = new char[ROWS][COLUMNS];
    private static char P1_COLOR;
    private static char P2_COLOR;
    private static char NEXT_TURN;

    // 'e' - empty
    // 'r' - red
    // 'y' - yellow

    public static void init() {
        for (char[] gameMtx : GAME_MTX) {
            Arrays.fill(gameMtx, 'e');
        }


        Random random = new Random();
        int r = random.nextInt(2);
        if (r == 0) { P1_COLOR = 'r'; P2_COLOR = 'y'; }
        else { P1_COLOR = 'y'; P2_COLOR = 'r'; }
        r = random.nextInt(2);
        if (r == 0) NEXT_TURN = 'r';
        else NEXT_TURN = 'y';
    }

    public static void recordMove(int i, int j, char color) {
        GAME_MTX[i-1][j] = color;
    }

    // naive bot
    public static int computerMove() {
        Random random = new Random();
        int r = random.nextInt(7);;
        while (GAME_MTX[0][r] != 'e') {
            r = random.nextInt(7);
        }
        return r;
    }


    public static boolean hasFourInARow() {
        int rows = GAME_MTX.length;
        int cols = GAME_MTX[0].length;

        // Check rows
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j <= cols - 4; j++) {
                if (GAME_MTX[i][j] != 'e' &&
                        GAME_MTX[i][j] == GAME_MTX[i][j+1] &&
                        GAME_MTX[i][j+1] == GAME_MTX[i][j+2] &&
                        GAME_MTX[i][j+2] == GAME_MTX[i][j+3]) {
                    return true;
                }
            }
        }

        // Check columns
        for (int i = 0; i <= rows - 4; i++) {
            for (int j = 0; j < cols; j++) {
                if (GAME_MTX[i][j] != 'e' &&
                        GAME_MTX[i][j] == GAME_MTX[i+1][j] &&
                        GAME_MTX[i+1][j] == GAME_MTX[i+2][j] &&
                        GAME_MTX[i+2][j] == GAME_MTX[i+3][j]) {
                    return true;
                }
            }
        }

        // Check diagonals (top-left to bottom-right)
        for (int i = 0; i <= rows - 4; i++) {
            for (int j = 0; j <= cols - 4; j++) {
                if (GAME_MTX[i][j] != 'e' &&
                        GAME_MTX[i][j] == GAME_MTX[i+1][j+1] &&
                        GAME_MTX[i+1][j+1] == GAME_MTX[i+2][j+2] &&
                        GAME_MTX[i+2][j+2] == GAME_MTX[i+3][j+3]) {
                    return true;
                }
            }
        }

        // Check diagonals (top-right to bottom-left)
        for (int i = 0; i <= rows - 4; i++) {
            for (int j = 3; j < cols; j++) {
                if (GAME_MTX[i][j] != 'e' &&
                        GAME_MTX[i][j] == GAME_MTX[i+1][j-1] &&
                        GAME_MTX[i+1][j-1] == GAME_MTX[i+2][j-2] &&
                        GAME_MTX[i+2][j-2] == GAME_MTX[i+3][j-3]) {
                    return true;
                }
            }
        }

        return false;
    }





    public static char[][] getGameMtx() {
        return GAME_MTX;
    }

    public static char getP1Color() {
        return P1_COLOR;
    }

    public static char getP2Color() {
        return P2_COLOR;
    }

    public static char getNextTurn() {
        return NEXT_TURN;
    }





    // for testing
    public static void print() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                System.out.print(GAME_MTX[i][j] + " ");
            }
            System.out.println();
        }
    }
}
