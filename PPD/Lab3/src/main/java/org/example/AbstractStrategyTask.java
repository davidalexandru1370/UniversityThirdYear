package org.example;

import java.util.List;

public abstract class AbstractStrategyTask extends Thread {
    protected final Matrix matrix1;
    protected final Matrix matrix2;
    protected final Matrix result;
    protected final int rowStart;
    protected final int columnStart;
    protected final int numberOfCells;

    public AbstractStrategyTask(Matrix matrix1, Matrix matrix2, Matrix result, int rowStart, int columnStart, int numberOfCells) {
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
        this.result = result;
        this.rowStart = rowStart;
        this.columnStart = columnStart;
        this.numberOfCells = numberOfCells;
    }

    public abstract List<Pair<Integer, Integer>> getCells();

    @Override
    public void run() {
        List<Pair<Integer, Integer>> cells = getCells();
        for (var cell : cells) {
            int row = cell.getFirst();
            int column = cell.getSecond();
            result.setElement(row, column, Matrix.getCell(matrix1, matrix2, row, column));
        }
    }
}
