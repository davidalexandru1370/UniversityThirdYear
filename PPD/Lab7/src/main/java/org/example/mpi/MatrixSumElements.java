package org.example.mpi;

import mpi.MPI;
import mpi.Status;

import javax.xml.transform.Source;
import java.util.ArrayList;
import java.util.List;

public class MatrixSumElements {

    private static void killAll(int numberOfProcesses) {
        for (int i = 1; i < numberOfProcesses; i++) {
            MPI.COMM_WORLD.Send(new int[]{1}, 0, 1, MPI.INT, i, 2);
        }
    }

    private static void master(List<List<Integer>> matrix, int numberOfProcesses) {
        List<Integer> allElements = matrix.stream().flatMap(List::stream).toList();
        System.out.println(allElements);
        int sum = sumBTree(allElements, 0, numberOfProcesses);
        killAll(numberOfProcesses);
        System.out.println(sum);
    }

    private static int sumBTree(List<Integer> elements, int me, int numberOfProcess) {
        if (elements.size() == 1) {
            System.out.println(elements.get(0));
            return elements.get(0);
        }

        int sumLeft, sumRight;
        int child = me + numberOfProcess / 2;

        if (numberOfProcess >= 2 && child < numberOfProcess) {
            List<Integer> left = new ArrayList<>(elements.subList(0, elements.size() / 2));
            //System.out.println(left);
            MPI.COMM_WORLD.Send(new int[]{0}, 0, 1, MPI.INT, child, 2);
            MPI.COMM_WORLD.Send(new Object[]{left}, 0, 1, MPI.OBJECT, child, 0);
            List<Integer> temp = new ArrayList<>(elements.subList(elements.size() / 2, elements.size()));
            sumRight = sumBTree(temp, me,numberOfProcess / 2);
            Object[] result = new Object[1];
            MPI.COMM_WORLD.Recv(result, 0, 1, MPI.OBJECT, child, 1);
            sumLeft = (int) result[0];
        } else {
            sumLeft = sumBTree(elements.subList(0, elements.size() / 2), me, 1);
            sumRight = sumBTree(elements.subList(elements.size() / 2, elements.size()), me, 1);
        }
        return sumLeft + sumRight;
    }

    private static void worker(int id, int numberOfProcesses) {
        while (true) {

            int[] shouldDie = new int[1];

            Status s =  MPI.COMM_WORLD.Recv(shouldDie, 0, 1, MPI.INT, MPI.ANY_SOURCE, 2);

            if (shouldDie[0] == 1) {
                break;
            }

            Object[] receivedList = new Object[1];
            Status status = MPI.COMM_WORLD.Recv(receivedList, 0, 1, MPI.OBJECT, s.source, 0);

            List<Integer> elems = (List<Integer>) receivedList[0];
            int sum = sumBTree(elems, id, numberOfProcesses);
            MPI.COMM_WORLD.Send(new Object[]{sum}, 0, 1, MPI.OBJECT, s.source, 1);
        }
    }

    public static void main(String[] args) {
        MPI.Init(args);

        int selfRank = MPI.COMM_WORLD.Rank();
        int numberOfProcesses = MPI.COMM_WORLD.Size();
        if (selfRank == 0) {
            master(List.of(List.of(1, 2, 3, 4), List.of(5, 6, 7, 8), List.of(9, 10, 11, 12)), numberOfProcesses);
//            master(List.of(List.of(1, 2), List.of(5, 6), List.of(9, 10)), numberOfProcesses);
        } else {
            worker(selfRank, numberOfProcesses);
        }

        MPI.Finalize();
    }
}
