package org.example;

import mpi.MPI;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        MPI.Init(args);
        int me = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        Graph graph = new Graph();
        Colors colors2 = new Colors(5);
        GraphColoring graphColoring = new GraphColoring(graph);
        if (me == 0) {
            System.out.println("Main process");

            try {
                long start = System.nanoTime();
                try {
                    System.out.println(graphColoring.colorGraph(size, colors2));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                long stop = System.nanoTime();

                long time = stop - start;
                System.out.println("Time: " + time / 1000000 + " ms");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Process number: " + me);

            int colorsNumber = colors2.getColors().size();

            graphColoring.graphColoringReceive(me, size, colorsNumber);
        }
        MPI.Finalize();
    }
}