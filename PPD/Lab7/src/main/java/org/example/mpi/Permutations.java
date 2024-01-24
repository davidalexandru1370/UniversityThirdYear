package org.example.mpi;

import mpi.MPI;
import mpi.Status;

import java.util.ArrayList;
import java.util.List;

public class Permutations {

    private static void killAll(int numberOfProcesses) {
        for (int i = 1; i < numberOfProcesses; i++) {
            MPI.COMM_WORLD.Send(new int[]{1}, 0, 1, MPI.INT, i, 2);
        }
    }

    private static void master(int n, int numberOfProcesses) {
        List<Integer> sol = new ArrayList<>();
        int count = solve(sol, n, 0, numberOfProcesses);
        killAll(numberOfProcesses);
        System.out.println("count = " + count);
    }

    private static int solve(List<Integer> sol, int n, int me, int numberOfProcesses) {
        if (sol.size() == n) {
            //condition
            if (sol.get(0) == 0) {
                System.out.println(sol);
                return 1;
            }
            return 0;
        }

        int solutions = 0;
        int child = me + numberOfProcesses / 2;
        if (numberOfProcesses >= 2 && child < numberOfProcesses) {
            List<Integer> toSend = new ArrayList<>(sol);
            System.out.printf("Sending candidate solution %s from %d to %d%n", toSend, me, child);
            MPI.COMM_WORLD.Ssend(new int[]{0}, 0, 1, MPI.INT, child, 2);
            MPI.COMM_WORLD.Ssend(new Object[]{toSend}, 0, 1, MPI.OBJECT, child, 0);
            List<Integer> temp = new ArrayList<>(sol);
            for (int i = 0; i < n; i++) {
                if (temp.contains(i)) {
                    continue;
                }
                temp.add(i);
                var copyTemp = new ArrayList<>(temp);
                solutions += solve(copyTemp, n, me, numberOfProcesses / 2);
                temp.remove(temp.size() - 1);
            }
            Object[] receivedData = new Object[1];
            MPI.COMM_WORLD.Recv(receivedData, 0, 1, MPI.OBJECT, child, 1);
            solutions += (int) receivedData[0];
        } else {
            for (int i = 0; i < n; i++) {
                if (sol.contains(i)) {
                    continue;
                }
                sol.add(i);
                solutions += solve(sol, n, me, 1);
                sol.remove(sol.size() - 1);
            }
        }
        return solutions;
    }

    private static void worker(int n, int me, int numberOfProcesses) {
        while (true) {
            int[] alive = new int[1];
            MPI.COMM_WORLD.Recv(alive, 0, 1, MPI.INT, MPI.ANY_SOURCE, 2);
            if (alive[0] == 1) {
                break;
            }
            Object[] receivedData = new Object[1];
            Status recv = MPI.COMM_WORLD.Recv(receivedData, 0, 1, MPI.OBJECT, MPI.ANY_SOURCE, 0);
            int parent = recv.source;
            List<Integer> array = (List<Integer>) receivedData[0];
            System.out.printf("Received candidate solution %s from %d to %d%n", array, parent, me);
            int sum = 0;
            for (int i = 1; i < n; i++) {
                if (array.contains(i)) continue;
                array.add(i);
                sum += solve(array, n, me, numberOfProcesses);
                array.remove(array.size() - 1);
            }
            MPI.COMM_WORLD.Ssend(new Object[]{sum}, 0, 1, MPI.OBJECT, parent, 1);
            System.out.printf("Send sum from %d t0 %d%n", me, parent); // Rete
        }
    }

    public static void main(String[] args) {
        MPI.Init(args);
        int selfRank = MPI.COMM_WORLD.Rank();
        int numberOfProcesses = MPI.COMM_WORLD.Size();
        int n = 4;
        if (selfRank == 0) {
            master(n, numberOfProcesses);
        } else {
            worker(n, selfRank, numberOfProcesses);
        }

        MPI.Finalize();
    }
}
