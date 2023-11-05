package org.example;

import java.util.ArrayList;
import java.util.List;

public class RowTask extends AbstractStrategyTask {
    public RowTask(Matrix matrix1, Matrix matrix2, Matrix result, int rowStart, int columnStart, int numberOfCells) {
        super(matrix1, matrix2, result, rowStart, columnStart, numberOfCells);
    }

    @Override
    public List<Pair<Integer, Integer>> getCells() {
        List<Pair<Integer, Integer>> cells = new ArrayList<>();

        int count = 0;
        int row = this.rowStart;
        int col = this.columnStart;
        while (count < numberOfCells && row < result.getRows() && col < result.getColumns()) {
            count++;
            cells.add(new Pair<>(row, col));
            col++;
            if (col == result.getColumns()) {
                col = 0;
                row++;
            }
        }
        return cells;
    }
}
