package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.*;

import static org.example.Polynomial.*;

class Multiplication {
    private final int numberOfThreads = 32;

    public Multiplication() {

    }

    public Polynomial normalMultiply(Polynomial p1, Polynomial p2) {
        int degree = p1.getDegree() + p2.getDegree();
        ArrayList<Integer> coefficients = new ArrayList<>(Collections.nCopies(degree + 1, 0));

        long start = System.currentTimeMillis();
        for (int i = 0; i <= p1.getDegree(); i++) {
            for (int j = 0; j <= p2.getDegree(); j++) {
                var value = p1.getCoefficients().get(i) * p2.getCoefficients().get(j);
                coefficients.set(i + j, coefficients.get(i + j) + value);
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("Normal multiply took: " + (end - start) + " ms");
        Polynomial result = new Polynomial(degree, coefficients);
        //System.out.println(result);

        return result;
    }

    public Polynomial normalMultiplyThreaded(Polynomial p1, Polynomial p2) throws InterruptedException {
        int degree = p1.getDegree() + p2.getDegree();
        ArrayList<Integer> coefficients = new ArrayList<>(Collections.nCopies(degree + 1, 0));
        Polynomial result = new Polynomial(degree, coefficients);

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(this.numberOfThreads);

        int step = Math.max(1, (degree + 1) / this.numberOfThreads);
        long start = System.currentTimeMillis();

        for (int index = 0; index <= degree; index += step) {
            int right = index + step;
            executor.execute(new MultiplicationThread(p1, p2, result, index, right));
            //new MultiplicationThread(p1, p2, result, index, right).run();
        }

        executor.shutdown();
        executor.awaitTermination(100, TimeUnit.MILLISECONDS);
        executor.close();
        long end = System.currentTimeMillis();
        System.out.println("MultiThread multiply took: " + (end - start) + " ms");

        //System.out.println(result);
        return result;

    }

    public Polynomial karatsubaSequential(Polynomial p1, Polynomial p2) {
        int maxDegree = Math.max(p1.getDegree(), p2.getDegree());
        int length = maxDegree / 2 + maxDegree % 2;
        long start = System.currentTimeMillis();
        Polynomial p1low = new Polynomial(new ArrayList<>(p1.getCoefficients().subList(0, length)));
        Polynomial p1high = new Polynomial(new ArrayList<>(p1.getCoefficients().subList(length, p1.getDegree() + 1)));
        Polynomial p2low = new Polynomial(new ArrayList<>(p2.getCoefficients().subList(0, length)));
        Polynomial p2high = new Polynomial(new ArrayList<>(p2.getCoefficients().subList(length, p2.getDegree() + 1)));

        Polynomial z0 = this.normalMultiply(p1low, p2low);
        Polynomial z1 = this.normalMultiply(add(p1low, p1high), add(p2low, p2high));
        Polynomial z2 = this.normalMultiply(p1high, p2high);

        Polynomial result1 = extendLeft(z2, 2 * length);
        Polynomial result2 = extendLeft(subtract(subtract(z1, z2), z0), length);

        Polynomial result = add(add(result1, result2), z0);
        long end = System.currentTimeMillis();
        System.out.println("Karatsuba sequential took: " + (end - start) + " ms");
        return result;
    }

    public Polynomial karatsubaParallel(Polynomial p1, Polynomial p2) throws ExecutionException, InterruptedException {
        int maxDegree = Math.max(p1.getDegree(), p2.getDegree());
        int length = maxDegree / 2 + maxDegree % 2;

        long start = System.currentTimeMillis();
        Polynomial p1low = new Polynomial(new ArrayList<>(p1.getCoefficients().subList(0, length)));
        Polynomial p1high = new Polynomial(new ArrayList<>(p1.getCoefficients().subList(length, p1.getDegree() + 1)));
        Polynomial p2low = new Polynomial(new ArrayList<>(p2.getCoefficients().subList(0, length)));
        Polynomial p2high = new Polynomial(new ArrayList<>(p2.getCoefficients().subList(length, p2.getDegree() + 1)));

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(this.numberOfThreads);

        Future<Polynomial> futureLow = executor.submit(() -> this.normalMultiply(p1low, p2low));
        Future<Polynomial> futureCommon = executor.submit(() -> this.normalMultiply(add(p1low, p1high), add(p2low, p2high)));
        Future<Polynomial> futureHigh = executor.submit(() -> this.normalMultiply(p1high, p2high));

        long end = System.currentTimeMillis();
        executor.shutdown();
        executor.close();
        executor.awaitTermination(100, TimeUnit.MILLISECONDS);
        var z0 = futureLow.get();
        var z1 = futureCommon.get();
        var z2 = futureHigh.get();
        Polynomial result1 = extendLeft(z2, 2 * length);
        Polynomial result2 = extendLeft(subtract(subtract(z1, z2), z0), length);
        Polynomial result = add(add(result1, result2), z0);


        System.out.println("Karatsuba threaded took: " + (end - start) + " ms");
        return result;
    }
}

public class Main {
    public static void main(String[] args) {
        var mul = new Multiplication();
        final Polynomial p1 = new Polynomial(100_000);
        final Polynomial p2 = new Polynomial(100_000);
        //var normalMultiplyResult = mul.normalMultiply(p1, p2);
        try {
            mul.normalMultiplyThreaded(p1, p2);
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }

        //var karatsubaResult = mul.karatsubaSequential(p1, p2);
        try {
            mul.karatsubaParallel(p1, p2);
        } catch (ExecutionException | InterruptedException e) {
            System.out.println("Interrupted");
        }
    }
}