package org.example;

import java.util.ArrayList;
import java.util.List;

public class AbstractThreadStrategy {

    protected static List<AbstractStrategyTask> getRowAfterRow(Matrix matrix1, Matrix matrix2, Matrix result, int nrThreads) {
        List<AbstractStrategyTask> abstractStrategyTasks = new ArrayList<>();
        int totalResultSize = result.getRows() * result.getColumns();
        int stepSize = totalResultSize / nrThreads;

        for (int index = 0; index < nrThreads; index++) {
            int rowStart = stepSize * index / result.getRows();
            int columnStart = stepSize * index % result.getRows();

            if (index == nrThreads - 1) {
                stepSize += totalResultSize % nrThreads;
            }

            abstractStrategyTasks.add(new RowTask(matrix1, matrix2, result, rowStart, columnStart, stepSize));
        }

        return abstractStrategyTasks;
    }

    protected static List<AbstractStrategyTask> getColAfterCol(Matrix matrix1, Matrix matrix2, Matrix result, int nrThreads) {
        List<AbstractStrategyTask> abstractStrategyTasks = new ArrayList<>();
        int totalResultSize = result.getRows() * result.getColumns();
        int stepSize = totalResultSize / nrThreads;

        for (int index = 0; index < nrThreads; index++) {
            int rowStart = stepSize * index / result.getRows();
            int columnStart = stepSize * index % result.getRows();

            if (index == nrThreads - 1) {
                stepSize += totalResultSize % nrThreads;
            }

            abstractStrategyTasks.add(new ColumnTask(matrix1, matrix2, result, rowStart, columnStart, stepSize));
        }

        return abstractStrategyTasks;
    }

    protected static List<AbstractStrategyTask> getKth(Matrix matrix1, Matrix matrix2, Matrix result, int nrThreads) {
        List<AbstractStrategyTask> abstractStrategyTasks = new ArrayList<>();
        int totalResultSize = result.getRows() * result.getColumns();
        int stepSize = totalResultSize / nrThreads;

        for (int index = 0; index < nrThreads; index++) {
            if (index < totalResultSize % nrThreads) {
                stepSize++;
            }

            int rowStart = index / result.getColumns();
            int columnStart = index % result.getColumns();

            abstractStrategyTasks.add(new KthTask(matrix1, matrix2, result, rowStart, columnStart, stepSize, nrThreads));
        }

        return abstractStrategyTasks;
    }
}
