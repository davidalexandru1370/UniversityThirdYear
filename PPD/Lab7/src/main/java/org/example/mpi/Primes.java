package org.example.mpi;

import mpi.MPI;

import java.util.ArrayList;
import java.util.List;

public class Primes {

    static List<Integer> primesInInterval(int me, int numberOfProcesses, int n, List<Integer> primesToSqrtN) {
        List<Integer> primes = new ArrayList<>();
        int left = me * (n - primesToSqrtN.getLast() + 1) / numberOfProcesses;
        int end = (me + 1) * (n - primesToSqrtN.getLast() + 1) / numberOfProcesses;

        for (int index = left; index < end; index++) {
            int div = 0;
            while (div < primesToSqrtN.size() && index % primesToSqrtN.get(div) != 0) {
                div++;
            }

            if (div == primesToSqrtN.size()) {
                primes.add(index);
            }
        }

        return primes;
    }

    private static void master(int n, int numberOfProcesses, List<Integer> primesToSqrtN) {
        int[] sizes = new int[2];
        sizes[0] = n;
        sizes[1] = primesToSqrtN.size();
        for (int i = 1; i < numberOfProcesses; i++) {
            MPI.COMM_WORLD.Send(sizes, 0, 2, MPI.INT, i, 0);
            MPI.COMM_WORLD.Send(new Object[]{primesToSqrtN}, 0, 1, MPI.OBJECT, i, 0);
        }

        List<Integer> result = primesInInterval(0, numberOfProcesses, n, primesToSqrtN);
        for (int i = 1; i < numberOfProcesses; i++) {
            Object[] obj = new Object[1];
            MPI.COMM_WORLD.Recv(obj, 0, 1, MPI.OBJECT, i, 0);
            result.addAll((List<Integer>) obj[0]);
        }
        System.out.println(result);
    }

    private static void worker(int me, int numberOfProcesses) {
        int[] metadata = new int[2];
        MPI.COMM_WORLD.Recv(metadata, 0, 2, MPI.INT, 0, 0);
        int maxN = metadata[0];
        int size = metadata[1];
        System.out.println(maxN);
        System.out.println(size);
        Object[] primesToSqrt = new Object[1];
        MPI.COMM_WORLD.Recv(primesToSqrt, 0, 1, MPI.OBJECT, 0, 0);
        System.out.println(primesToSqrt[0].toString());
        List<Integer> result = primesInInterval(me, numberOfProcesses, maxN, (List<Integer>) primesToSqrt[0]);
        MPI.COMM_WORLD.Send(new Object[]{result}, 0, 1, MPI.OBJECT, 0, 0);
    }

    public static void main(String[] args) {
        MPI.Init(args);
        int me = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        int n = 100;
        List<Integer> primesToSqrtN = List.of(2, 3, 5, 7);

        if (me == 0) {
            master(n, size, primesToSqrtN);
        } else {
            worker(me, size);
        }

        MPI.Finalize();
    }
}
