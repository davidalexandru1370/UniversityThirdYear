package org.example;

import java.util.ArrayList;
import java.util.List;

public class ThreadStrategy extends AbstractThreadStrategy {
    public Matrix run(Matrix m1, Matrix m2, int numberOfThreads, ThreadApproach threadApproach) {
        Matrix result = new Matrix(m1.getRows(), m2.getColumns());
        List<AbstractStrategyTask> tasks = new ArrayList<>();

        switch (threadApproach) {
            case RowByRow -> tasks = getRowAfterRow(m1, m2, result, numberOfThreads);
            case ColumnByColumn -> tasks = getColAfterCol(m1, m2, result, numberOfThreads);
            case KthCell -> tasks = getKth(m1, m2, result, numberOfThreads);
        }

        tasks.forEach(AbstractStrategyTask::run);

        tasks.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        
        return result;
    }


}
