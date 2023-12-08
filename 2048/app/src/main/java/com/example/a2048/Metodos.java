package com.example.a2048;

import android.graphics.Color;
import android.view.GestureDetector;
import android.widget.TextView;


import java.util.Random;

public class Metodos extends GestureDetector.SimpleOnGestureListener{
    // Object to generate random numbers
    private Random rand = new Random();
    int score=0;
    // Directions mapped to a number to easy acces
    // Indexes
    // 0 -> Down, 1 -> Right, 2 -> UP, 3 -> LEFT
    int[] direccionLinea = new int[]{1,0,-1,0};
    int[] direccionColumna = new int[]{0,1,0,-1};

    // Method created to generate the logical board to control the GUI
    public int[][] newGame(TextView[][] GUI){
        int[][] tablero = new int[8][8];
        for (int i = 0; i <= 7; i++){
            for (int j = 0; j <= 7; j++){
                tablero[i][j] = 0;
            }
        }
        // Generate the board and the first 2 fields with a random number and positions
        tablero = generaPiezasAleatorias(2, tablero,GUI);
        return  tablero;
    }

    // Generate random coordinates in an empty field
    public int[][] generaPiezasAleatorias(int piezas, int[][] board, TextView[][] GUI){
        int[] cordenadas = new int[2];
        for (int i = 0; i < piezas; i++){
            boolean busy = false;
            while(!busy){
                // X -> 0 Y -> 1
                cordenadas[0] = rand.nextInt(8);
                cordenadas[1] = rand.nextInt(8);
                if (board[cordenadas[0]][cordenadas[1]] == 0){
                    int value = (rand.nextInt(2)+1) * 2;
                    board[cordenadas[0]][cordenadas[1]] = value;
                    GUI[cordenadas[0]][cordenadas[1]].setText(String.valueOf(value));
                    colores(value,cordenadas[0],cordenadas[1],GUI);
                    busy = true;
                }
            }
        }
        return board;
    }

    public boolean movimientoValido(int line, int column , int nextLine, int nextColumn, int[][] board){
        if (nextLine < 0 || nextColumn < 0 || nextLine > 7 || nextColumn > 7
                || board[line][column] != board[nextLine][nextColumn] && board[nextLine][nextColumn] != 0 ){
            return false;
        }
        return true;
    }

    // Method to apply the moves in the selected direction
    public int[][] movimientos(int direction, int[][] board, TextView[][] GUI) {
        int startLine = 0, startColumn = 0, lineStep = 1, columnStep = 1;

        // Moves from down to up
        if (direction == 0) {
            startLine = 7;
            lineStep = -1;
        }
        if (direction == 1) {
            startColumn = 7;
            columnStep = -1;
        }
        int result = 0;
        boolean agregarPieza = false;
        boolean movimientoPosible = true;
        int combinaPieza = 0;
        while (movimientoPosible) {
            movimientoPosible = false;
            for (int i = startLine; i >= 0 && i <= 7; i += lineStep) {
                for (int j = startColumn; j >= 0 && j <= 7; j += columnStep) {
                    int nextI = i + direccionLinea[direction], nextJ = j + direccionColumna[direction];
                    if (movimientoValido(i, j, nextI, nextJ, board) && board[i][j] != 0) {
                        result = board[nextI][nextJ] + board[i][j];
                        if (result != board[nextI][nextJ] && result != board[i][j]) {
                            combinaPieza++;
                        }
                        board[nextI][nextJ] = result;
                        GUI[nextI][nextJ].setText(String.valueOf(result));
                        board[i][j] = 0;
                        GUI[i][j].setText("");
                        agregarPieza = true;
                        movimientoPosible = true;
                    }
                }
            }
        }
        // Actualiza los colores
        for (int i = 0; i <= 7; i ++) {
            for (int j = 0; j <= 7; j ++) {
                colores(board[i][j],i,j,GUI);
            }
        }
        if (agregarPieza && tableroLLeno(board)) {
            board = generaPiezasAleatorias(1, board, GUI);
        }
        if (combinaPieza > 0){
            score += result;
        }
        return board;
    }
    public boolean tableroLLeno(int[][] board){
        for (int i = 0; i < board.length; i ++) {
            for (int j = 0; j < board[i].length; j ++) {
                if (board[i][j] == 0){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean validMoveExists(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != 0) {
                    // Check for valid moves in all directions
                    for (int k = 0; k < direccionLinea.length; k++) {
                        int nextI = i + direccionLinea[k], nextJ = j + direccionColumna[k];
                        if (nextI >= 0 && nextJ >= 0 && nextI < 8 && nextJ < 8 && movimientoValido(i, j, nextI, nextJ, board)) {
                            // A valid move exists
                            return true;
                        }
                    }
                }
            }
        }
        // No valid move exists
        return false;
    }

    public void colores(int value,int i, int j, TextView[][] GUI){
        if (value == 0){
            GUI[i][j].setBackgroundColor(Color.parseColor("#B6F7F1F1"));
        } else if (value >= 2 && value <= 16) {
            GUI[i][j].setBackgroundColor(Color.parseColor("#FFD133"));
        }else if (value > 16 && value <= 128){
            GUI[i][j].setBackgroundColor(Color.parseColor("#D0A820"));
        }else{
            GUI[i][j].setBackgroundColor(Color.parseColor("#EE292F"));
        }
    }
}
