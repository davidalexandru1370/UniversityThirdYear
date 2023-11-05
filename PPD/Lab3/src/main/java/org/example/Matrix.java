package org.example;

import java.util.Random;

public class Matrix {
    private final int[][] matrix;
    private final int rows, columns;

    public Matrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.matrix = new int[rows][columns];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                this.matrix[row][col] = new Random().nextInt(25);
            }
        }
    }

    public int getCell(Matrix matrix1, Matrix matrix2, int row, int col){
        int product = 0;
        for(int i = 0; i < matrix1.getColumns(); i++){
            product += matrix1.getElement(row, i) * matrix2.getElement(i, col);
        }

        return product;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public int getElement(int row, int col){
        return this.matrix[row][col];
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }
}
