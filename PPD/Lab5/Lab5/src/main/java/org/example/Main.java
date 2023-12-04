package org.example;

import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class Multiplication {
    private final int numberOfThreads = 4;
    private final Polynomial p1;
    private final Polynomial p2;

    public Multiplication() {
        this.p1 = new Polynomial(2);
        this.p2 = new Polynomial(2);
    }

    public Polynomial normalMultiply() {
        int degree = p1.getDegree() + p2.getDegree();
        int[] coefficients = new int[degree + 1];
        long start = System.currentTimeMillis();
        for (int i = 0; i <= p1.getDegree(); i++) {
            for (int j = 0; j <= p2.getDegree(); j++) {
                coefficients[i + j] += p1.getCoefficients()[i] * p2.getCoefficients()[j];
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("Normal multiply took: " + (end - start) + " ms");
        Polynomial result = new Polynomial(degree, coefficients);
        System.out.println(result);

        return result;
    }

    public Polynomial normalMultiplyThreaded() throws InterruptedException {
        int degree = p1.getDegree() + p2.getDegree();
        int[] coefficients = new int[degree + 1];
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
        System.out.println("Normal multiply took: " + (end - start) + " ms");

        System.out.println(result);
        return result;

    }

}

public class Main {

    public static void main(String[] args) {
        var mul = new Multiplication();

        mul.normalMultiply();
        try {
            mul.normalMultiplyThreaded();
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
    }
}