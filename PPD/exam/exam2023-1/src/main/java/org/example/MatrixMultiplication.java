package org.example;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MatrixMultiplication {

    public List<List<Integer>> matrix1 = List.of(List.of(1, 2, 3, 4), List.of(5, 6, 7, 8), List.of(9, 10, 11, 12), List.of(13, 14, 15, 16));
    public List<List<Integer>> matrix2 = List.of(List.of(1, 2, 3, 4), List.of(5, 6, 7, 8), List.of(9, 10, 11, 12), List.of(13, 14, 15, 16));

    public List<List<Integer>> result = new ArrayList<>();

    public List<Triple> splitWorkload(int numberOfThreads) {
        //line start, column start, step size
        List<Triple> workload = new ArrayList<>();

        int totalNumberOfElements = result.size() * result.get(0).size();

        int step = totalNumberOfElements / numberOfThreads;

        for (int index = 0; index < numberOfThreads; index++) {
            int rowStart = step * index / result.size();
            int columnStart = step * index % result.size();

            if (index == numberOfThreads - 1) {
                workload.add(new Triple(rowStart, columnStart, step + totalNumberOfElements % numberOfThreads));
            } else {
                workload.add(new Triple(rowStart, columnStart, step));
            }
        }

        return workload;
    }

    public void solve(int numberOfThreads) {
        for (int row = 0; row < matrix1.size(); row++) {
            List<Integer> col = new ArrayList<>();
            for (int column = 0; column < matrix2.get(0).size(); column++) {
                col.add(0);
            }
            result.add(col);
        }
        List<Triple> workload = splitWorkload(numberOfThreads);


        try (ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads)) {
            workload.stream().forEach(
                    triple -> executorService.submit(() -> {
                        var t = triple;
                        int i = t.first;
                        int j = t.second;
                        while (t.third-- > 0) {
                            for (int k = 0; k < matrix1.get(0).size(); k++) {
                                result.get(i).set(j, result.get(i).get(j) + matrix1.get(i).get(k) * matrix2.get(k).get(j));
                            }
                            j++;
                            if (j >= matrix1.size()) {
                                j = 0;
                                i++;
                            }
                        }


                    })
            );

            executorService.awaitTermination(10, TimeUnit.SECONDS);
            executorService.shutdown();
            System.out.println(result);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}
