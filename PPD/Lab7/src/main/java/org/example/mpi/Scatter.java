package org.example.mpi;

import mpi.MPI;

import java.util.Arrays;

public class Scatter {

    public static void main(String[] args) {
        MPI.Init(args);
        int me = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        int[] arr = new int[2];
        int[] sendbuf = new int[]{1, 2, 3, 4, 5};
        int chunksize = sendbuf.length / size;
        MPI.COMM_WORLD.Scatter(sendbuf, 0, chunksize, MPI.INT, arr, 0, chunksize, MPI.INT, 0);
        System.out.println("Process " + me + " received: " + Arrays.toString(arr));

        MPI.Finalize();
    }
}
