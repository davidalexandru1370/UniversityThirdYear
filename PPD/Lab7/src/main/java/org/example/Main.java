package org.example;

import mpi.MPI;

import java.util.ArrayList;
import java.util.Collections;

public class Main {
    private static Polynomial combineResultsFromProcesses(Object[] polynomials){
        int size = ((Polynomial) polynomials[0]).getDegree();

        Polynomial result = new Polynomial(size, 0);
        for(Object polynomial: polynomials){
            result = Polynomial.add(result, (Polynomial) polynomial);
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

            MPI.COMM_WORLD. Send(new Object[]{p1}, 0, 1, MPI.OBJECT, process, 0);
            MPI.COMM_WORLD.Send(new Object[]{p2}, 0, 1, MPI.OBJECT, process, 0);
            MPI.COMM_WORLD.Send(new int[]{left}, 0, 1, MPI.INT, process, 0);
            MPI.COMM_WORLD.Send(new int[]{right}, 0, 1, MPI.INT, process, 0);
        }

        Object[] result = new Object[size - 1];

        for (int process = 1; process < size; process++) {
            MPI.COMM_WORLD.Recv(result, process - 1, 1, MPI.OBJECT, process, 0);
        }

        Polynomial finalResult = combineResultsFromProcesses(result);
        long endTime = System.nanoTime();
        System.out.println("Time: " + (endTime - startTime) / 1_000_000 + " ms");
        System.out.println(finalResult);
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

    public static void main(String[] args) {
        MPI.Init(args);

        int me = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        boolean shouldExecuteNormalMultiplication = true;

        if (me == 0) {
            Polynomial p1 = new Polynomial(100);
            Polynomial p2 = new Polynomial(100);

            initializeMPI(p1, p2, size);
        } else {
            if (shouldExecuteNormalMultiplication) {
                normalMultiplication(me);
            } else {

            }
        }

        MPI.Finalize();
    }
}