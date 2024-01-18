package org.example;


import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static final int threads = 16;
    private static final int rows = 4;
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(threads);
    static AtomicInteger sum = new AtomicInteger(0);
    static AtomicInteger isDone = new AtomicInteger(0);

    static void solveSequential(List<Integer> line) {
        if (line.size() == 1) {
            System.out.println(line.get(0));
            sum.accumulateAndGet(line.get(0), Integer::sum);
            return;
        }

        if (line.isEmpty()) {
            return;
        }

        solveSequential(line.subList(0, line.size() / 2));
        solveSequential(line.subList(line.size() / 2, line.size()));
    }

    static void solve(List<Integer> line) {
        if (line.size() == 1) {
            System.out.println(line.get(0));
            sum.accumulateAndGet(line.get(0), Integer::sum);
            return;
        }
        if (line.isEmpty()) {
            return;
        }

        if (((ThreadPoolExecutor) threadPool).getPoolSize() <= threads - 1) {
            threadPool.submit(() -> {
                solve(line.subList(0, line.size() / 2));
            });
            threadPool.submit(() -> {
                solve(line.subList(line.size() / 2, line.size()));
            });
        } else {
            solve(line.subList(0, line.size() / 2));
            solve(line.subList(line.size() / 2, line.size()));
        }
    }

    public static void main(String[] args) throws InterruptedException {
        List<List<Integer>> matrix = List.of(List.of(1, 2, 3, 4), List.of(5, 6, 7, 8), List.of(9, 10, 11, 12), List.of(13, 14, 15, 16));


//        for (var line : matrix) {
//            solveSequential(line); //
//        }
//
//        System.out.println("Sum: " + sum.get());
//        for (var line : matrix) {
//            threadPool.submit(() -> {
//                solve(line);
//            });
//        }
//
//        while (true) {
//            if (((ThreadPoolExecutor) threadPool).getActiveCount() == 0) {
//                System.out.println("Sum: " + sum);
//                threadPool.shutdown();
//                break;
//            }
//        }
//        var vectorProductProblem = new ScalarProductTree();
//
//        var result = vectorProductProblem.scalarProduct(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), 4);
//
//        System.out.println(result);

//        var matrixMultiplication = new MatrixMultiplication();
//
//        matrixMultiplication.solve(4);

        var primeNumbers = new PrimeNumbers();
        primeNumbers.execute(7);

    }
}