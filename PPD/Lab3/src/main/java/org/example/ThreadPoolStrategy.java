package org.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolStrategy extends AbstractThreadStrategy {

    public Matrix run(Matrix m1, Matrix m2, int numberOfThreads, ThreadApproach threadApproach) {
        Matrix result = new Matrix(m1.getRows(), m2.getColumns());
        try (ExecutorService threadPool = Executors.newFixedThreadPool(numberOfThreads)) {

            switch (threadApproach) {
                case RowByRow -> getRowAfterRow(m1, m2, result, numberOfThreads).forEach(threadPool::execute);
                case ColumnByColumn -> getColAfterCol(m1, m2, result, numberOfThreads).forEach(threadPool::execute);
                case KthCell -> getKth(m1, m2, result, numberOfThreads).forEach(threadPool::execute);
            }
            threadPool.shutdown();
            try {
                if (!threadPool.awaitTermination(Long.MAX_VALUE, java.util.concurrent.TimeUnit.NANOSECONDS)) {
                    threadPool.shutdownNow();
                }
            } catch (InterruptedException e) {
                threadPool.shutdownNow();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
