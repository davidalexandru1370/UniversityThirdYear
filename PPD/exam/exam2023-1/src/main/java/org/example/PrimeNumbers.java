package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Math.min;

public class PrimeNumbers {

    List<Pair<Integer, Integer>> splitWorkload(int n, int numberOfThreads) {

        int chunkSize = n / numberOfThreads;
        int mod = n % numberOfThreads;

        List<Pair<Integer, Integer>> workload = new ArrayList<>();

        for (int i = 0; i < numberOfThreads; i++) {
            int start = i * chunkSize + 2;
            int end = start + chunkSize;
            if (mod > 0) {
                end += 1;
                mod--;
            }
            workload.add(new Pair<>(start, min(end, n)));
        }

        return workload;
    }

    public void execute(int numberOfThreads) throws InterruptedException {

        List<Integer> givenPrimeNumbers = List.of(2, 3, 5, 7);
        int n = 100;
        List<Integer> primeNumbers = new ArrayList<>();
        ReentrantLock lock = new ReentrantLock();
        ExecutorService threadPool = Executors.newFixedThreadPool(numberOfThreads);
        List<Pair<Integer, Integer>> workload = splitWorkload(n, numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            int start = workload.get(i).first;
            int end = workload.get(i).second;
            threadPool.submit(() -> {
                List<Integer> primes = new ArrayList<>();
                for (int j = start; j < end; j++) {
                    boolean isPrime = true;
                    for (int primeNumber : givenPrimeNumbers) {
                        if (j % primeNumber == 0 && j != primeNumber) {
                            isPrime = false;
                            break;
                        }
                    }
                    if (isPrime) {
                        primes.add(j);
                    }
                }
                lock.lock();
                primeNumbers.addAll(primes);
                lock.unlock();
            });
        }

        threadPool.awaitTermination(1, TimeUnit.SECONDS);
        threadPool.shutdown();
        System.out.println(primeNumbers.stream().sorted().toList());


    }
}
