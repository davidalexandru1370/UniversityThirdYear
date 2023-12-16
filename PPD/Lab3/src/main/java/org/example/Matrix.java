package org.example;

import java.util.Arrays;
import java.util.Random;

public class Matrix {
    private final int[][] matrix;
    private final int rows, columns;

    public Matrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.matrix = new int[rows][columns];
    }

    public void setRandomValues() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                this.matrix[row][col] = 1;
            }
        }
    }

    public static int getCell(Matrix matrix1, Matrix matrix2, int row, int col) {
        int product = 0;
        for (int i = 0; i < matrix1.getColumns(); i++) {
            product += matrix1.getElement(row, i) * matrix2.getElement(i, col);
        }

        return product;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public int getElement(int row, int col) {
        return this.matrix[row][col];
    }

    public int getRows() {
        return rows;
    }

    public void setElement(int row, int column, int value) {
        this.matrix[row][column] = value;
    }

    public int getColumns() {
        return columns;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int[] row : matrix) {
            stringBuilder.append(Arrays.toString(row)).append("\n");
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        var matrix = (Matrix) obj;
        if (rows != matrix.getRows() || columns != matrix.getColumns()) {
            return false;
        }

        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.columns; col++) {
                if (this.matrix[row][col] != matrix.getElement(row, col)) {
                    return false;
                }
            }
        }

        return true;
    }
}
