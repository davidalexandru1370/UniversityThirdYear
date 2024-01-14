package org.example;

import mpi.*;

public class Main {

    static int nodeCount = 5;
    static int coloringDegree = 3;

    public static void main(String[] args) throws InterruptedException {

        MPI.Init(args);

        final String[] c = {"color 1", "color 2", "color 3", "color 4", "color 5", "color 6", "color 7"};

        int id = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        //graph.generateRandomGraph(nodeCount, 10);
        var graph = new Graph(5);
        graph.setEdge(0,1);
        graph.setEdge(1,2);
        graph.setEdge(1,4);
        graph.setEdge(2,0);
        graph.setEdge(2,3);
        graph.setEdge(3,1);
        graph.setEdge(3,4);
        graph.setEdge(4,0);

//        var graph = new Graph(4);
//        graph.setEdge(0, 1);
//        graph.setEdge(1, 2);
//        graph.setEdge(2, 3);
//        graph.setEdge(0, 2);
//        graph.setEdge(1, 3);
//        graph.setEdge(0, 3);

        Colors.setNoColors(coloringDegree);

        for (int i = 1; i <= coloringDegree; i++) Colors.setColorName(i, c[i - 1]);

        // n = the coloring degree
        // Rationale: Each of the n processes (excluding the main process) will handle
        // the partial solutions ending in color k (1 <= k <= n)
        assert size - 1 == Colors.getNoColors();

        if (id == 0) {

            try {

                GraphColoring.graphColoringMain(graph);
            } catch (RuntimeException gce) {
                System.out.println(gce.getMessage());
            }
        } else {
            try {
                GraphColoring.graphColoringWorker(id, graph);

            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }

        MPI.Finalize();
    }
}