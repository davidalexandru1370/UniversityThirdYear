package org.example.mpi;

import mpi.MPI;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;

public class Convolution {
    private static void master(List<Integer> a, List<Integer> b, int size) {
        int length = a.size();
        int toShare = size - 1;
        int step = length / toShare;
        int remainder = length % toShare;

        int start = 0, stop = 0;

        List<Integer> result = new ArrayList<>(length);

        for (int i = 0; i < length; i++) {
            result.add(0);
        }

        for (int i = 1; i <= toShare; i++) {
            stop = start + step;

            if (remainder > 0) {
                stop += 1;
                remainder--;
            }

            int[] sizes = new int[]{start, stop};
            MPI.COMM_WORLD.Send(sizes, 0, 2, MPI.INT, i, 0);
            MPI.COMM_WORLD.Send(new Object[]{a}, 0, 1, MPI.OBJECT, i, 1);
            MPI.COMM_WORLD.Send(new Object[]{b}, 0, 1, MPI.OBJECT, i, 2);

            start = stop;
        }

        for (int slave = 1; slave <= toShare; slave++) {
            Object[] receivedResult = new Object[1];

            MPI.COMM_WORLD.Recv(receivedResult, 0, 1, MPI.OBJECT, slave, 0);

            List<Integer> elements = (List<Integer>) receivedResult[0];

            for (int i = 0; i < length; i++) {
                result.set(i, result.get(i) + elements.get(i));
            }
        }

        System.out.println(result.stream().map(Object::toString).reduce("", (a1, b1) -> a1 + " " + b1));

    }

    private static void worker(int id, int numberOfProcesses) {
        int[] sizes = new int[2];
        Object[] lists = new Object[2];

        MPI.COMM_WORLD.Recv(sizes, 0, 2, MPI.INT, 0, 0);
        MPI.COMM_WORLD.Recv(lists, 0, 1, MPI.OBJECT, 0, 1);
        MPI.COMM_WORLD.Recv(lists, 1, 1, MPI.OBJECT, 0, 2);

        int start = sizes[0], stop = sizes[1];
        List<Integer> a = (List<Integer>) lists[0];
        List<Integer> b = (List<Integer>) lists[1];

        List<Integer> result = new ArrayList<>(a.size());

        for (int index = 0; index < a.size(); index++) {
            result.add(0);
        }

        for (int i = start; i < min(stop, a.size()); i++) {
            for (int j = 0; j < a.size(); j++) {
                result.set(i, result.get(i) + a.get(j) * b.get(mod(i - j, a.size())));
            }
        }

        MPI.COMM_WORLD.Send(new Object[]{result}, 0, 1, MPI.OBJECT, 0, 0);
    }

    private static int mod(int a, int b) {
        if (a < 0) {
            return (a + b) % b;
        }

        return a % b;
    }

    public static void main(String[] args) {
        MPI.Init(args);
        int me = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        List<Integer> a = List.of(1, 2, 3, 5, 6, 7);
        List<Integer> b = List.of(1, 2, 3, 5, 6, 7);
        if (me == 0) {
            master(a, b, size);
        } else {
            worker(me, size);
        }

        MPI.Finalize();
    }
}
