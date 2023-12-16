package org.example;

import java.util.ArrayList;
import java.util.List;

public class KthTask extends AbstractStrategyTask {
    private final int k;

    public KthTask(Matrix matrix1, Matrix matrix2, Matrix result, int rowStart, int columnStart, int numberOfCells, int numberOfThreads) {
        super(matrix1, matrix2, result, rowStart, columnStart, numberOfCells);
        k = numberOfThreads;
    }

    @Override
    public List<Pair<Integer, Integer>> getCells() {
        List<Pair<Integer, Integer>> cells = new ArrayList<>();

        int row = rowStart;
        int column = columnStart;
        int count = 0;

        while (count < numberOfCells && row < result.getRows() && column < result.getColumns()) {
            count++;
            cells.add(new Pair<>(row, column));
            row += (column + k) / result.getColumns();
            column = (column + k) % result.getColumns();
        }

        return cells;
    }
}
