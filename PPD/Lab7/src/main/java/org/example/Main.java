package org.example;

import mpi.MPI;

import java.util.ArrayList;
import java.util.Collections;

import static org.example.Polynomial.*;

public class Main {
    private static Polynomial combineResultsFromProcesses(Object[] polynomials) {
        int size = ((Polynomial) polynomials[0]).getDegree();

        Polynomial result = new Polynomial(size, 0);
        for (Object polynomial : polynomials) {
            result = add(result, (Polynomial) polynomial);
        }

        return result;
    }

    private static void initializeMPI(Polynomial p1, Polynomial p2, int size) {
        long startTime = System.nanoTime();

        int length = p1.getDegree() / size;

        int left = 0;
        int right = 0;

        for (int process = 1; process < size; process++) {
            left = right;
            right = left + length;
            if (process == size - 1) {
                right = p1.getDegree() + 1;
            }

            MPI.COMM_WORLD.Send(new Object[]{p1}, 0, 1, MPI.OBJECT, process, 0);
            MPI.COMM_WORLD.Send(new Object[]{p2}, 0, 1, MPI.OBJECT, process, 0);
            MPI.COMM_WORLD.Send(new int[]{left}, 0, 1, MPI.INT, process, 0);
            MPI.COMM_WORLD.Send(new int[]{right}, 0, 1, MPI.INT, process, 0);
        }

        Object[] result = new Object[size - 1];

        for (int process = 1; process < size; process++) {
            MPI.COMM_WORLD.Recv(result, process - 1, 1, MPI.OBJECT, process, 0);
        }
        long endTime = System.nanoTime();

        Polynomial finalResult = combineResultsFromProcesses(result);
        System.out.println("Time: " + (endTime - startTime) / 1_000_000 + " ms");
        System.out.println(finalResult);
    }

    private static Polynomial normalMultiply(Polynomial p1, Polynomial p2) {
        int degree = p1.getDegree() + p2.getDegree();
        ArrayList<Integer> coefficients = new ArrayList<>(Collections.nCopies(degree + 1, 0));

        long startTime = System.currentTimeMillis();
        for (int i = 0; i <= p1.getDegree(); i++) {
            for (int j = 0; j <= p2.getDegree(); j++) {
                var value = p1.getCoefficients().get(i) * p2.getCoefficients().get(j);
                coefficients.set(i + j, coefficients.get(i + j) + value);
            }
        }

        long endTime = System.currentTimeMillis();
        Polynomial result = new Polynomial(degree, coefficients);
        return result;
    }

    private static Polynomial normalMultiply(Polynomial p1, Polynomial p2, int start, int end) {
        int degree = p1.getDegree() + p2.getDegree();
        ArrayList<Integer> coefficients = new ArrayList<>(Collections.nCopies(degree + 1, 0));

        long startTime = System.currentTimeMillis();
        for (int i = start; i < end; i++) {
            for (int j = 0; j <= p2.getDegree(); j++) {
                var value = p1.getCoefficients().get(i) * p2.getCoefficients().get(j);
                coefficients.set(i + j, coefficients.get(i + j) + value);
            }
        }

        long endTime = System.currentTimeMillis();
        Polynomial result = new Polynomial(degree, coefficients);
        return result;
    }

    public static void normalMultiplication(int me) {
        Object[] p1 = new Object[2];
        Object[] p2 = new Object[2];
        int[] start = new int[1];
        int[] end = new int[1];

        MPI.COMM_WORLD.Recv(p1, 0, 1, MPI.OBJECT, 0, 0);
        MPI.COMM_WORLD.Recv(p2, 0, 1, MPI.OBJECT, 0, 0);
        MPI.COMM_WORLD.Recv(start, 0, 1, MPI.INT, 0, 0);
        MPI.COMM_WORLD.Recv(end, 0, 1, MPI.INT, 0, 0);

        Polynomial polynomial1 = (Polynomial) p1[0];
        Polynomial polynomial2 = (Polynomial) p2[0];

        Polynomial result = normalMultiply(polynomial1, polynomial2, start[0], end[0]);

        MPI.COMM_WORLD.Send(new Object[]{result}, 0, 1, MPI.OBJECT, 0, 0);
    }

    private static Polynomial karatsubaMultiply(Polynomial p1, Polynomial p2) {
        int maxDegree = Math.max(p1.getDegree(), p2.getDegree());
        int length = maxDegree / 2 + maxDegree % 2;
        long start = System.currentTimeMillis();
        Polynomial p1low = new Polynomial(new ArrayList<>(p1.getCoefficients().subList(0, length)));
        Polynomial p1high = new Polynomial(new ArrayList<>(p1.getCoefficients().subList(length, p1.getDegree() + 1)));
        Polynomial p2low = new Polynomial(new ArrayList<>(p2.getCoefficients().subList(0, length)));
        Polynomial p2high = new Polynomial(new ArrayList<>(p2.getCoefficients().subList(length, p2.getDegree() + 1)));

        Polynomial z0 = normalMultiply(p1low, p2low);
        Polynomial z1 = normalMultiply(add(p1low, p1high), add(p2low, p2high));
        Polynomial z2 = normalMultiply(p1high, p2high);

        Polynomial result1 = extendLeft(z2, 2 * length);
        Polynomial result2 = extendLeft(subtract(subtract(z1, z2), z0), length);

        Polynomial result = add(add(result1, result2), z0);
        long end = System.currentTimeMillis();
        //System.out.println("Karatsuba sequential took: " + (end - start) + " ms");
        return result;
    }

    public static void karatsubaMultiplication(int me) {
        Object[] p1 = new Object[2];
        Object[] p2 = new Object[2];
        int[] start = new int[1];
        int[] end = new int[1];

        MPI.COMM_WORLD.Recv(p1, 0, 1, MPI.OBJECT, 0, 0);
        MPI.COMM_WORLD.Recv(p2, 0, 1, MPI.OBJECT, 0, 0);
        MPI.COMM_WORLD.Recv(start, 0, 1, MPI.INT, 0, 0);
        MPI.COMM_WORLD.Recv(end, 0, 1, MPI.INT, 0, 0);

        Polynomial pol1 = (Polynomial) p1[0];
        Polynomial pol2 = (Polynomial) p2[0];

        for (int i = 0; i < start[0]; i++) {
            pol1.getCoefficients().set(i, 0);
        }
        for (int j = end[0]; j < pol1.getCoefficients().size(); j++) {
            pol1.getCoefficients().set(j, 0);
        }

        Polynomial result = karatsubaMultiply(pol1, pol2);

        MPI.COMM_WORLD.Send(new Object[]{result}, 0, 1, MPI.OBJECT, 0, 0);
    }

    public static void main(String[] args) {
        MPI.Init(args);

        int me = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        boolean shouldExecuteNormalMultiplication = false;

//        if (me == 0) {
//            Polynomial p1 = new Polynomial(100);
//            Polynomial p2 = new Polynomial(100);
//
//            initializeMPI(p1, p2, size);
//        } else {
//            if (shouldExecuteNormalMultiplication) {
//                normalMultiplication(me);
//            } else {
//                karatsubaMultiplication(me);
//            }
//        }

        MPI.Finalize();
    }
}