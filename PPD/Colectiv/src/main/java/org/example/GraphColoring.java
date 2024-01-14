package org.example;

import mpi.MPI;

import java.util.*;


public class GraphColoring {

    public static void graphColoringMain(Graph graph) throws InterruptedException {

        int noColors = Colors.getNoColors();

        // e.g.: 0  0  0  0  0 is an empty solution, or complete until index -1
        //      -1 -1 -1 -1 -1 is an invalid solution
        //       1  2  1  0  0 is a solution complete until index 2
        int[] codes = graphColoringRec(-1, graph, noColors, new int[graph.getNoNodes()], 0);

        if (codes[0] == -1)
            throw new RuntimeException("No solution found!");
    }

    /*
       solution - The current partial solution
       solutionNode - Until which index the solution is complete
       graph - The whole graph
       noColors - The coloring degree of the problem
       mpiId - The process id the method is executing under
     */
    private static int[] graphColoringRec(int solutionNode, Graph graph, int noColors, int[] solution, int mpiId) throws InterruptedException {

        int noNodes = graph.getNoNodes();

        // If a solution is invalid, we invalidate it.
        if (!isCodeValid(solutionNode, solution, graph)) {
            return getInvalidSolution(noNodes);
        }

        // If a solution is valid and complete until its last index, we return it.
        if (solutionNode + 1 == graph.getNoNodes()) {
            return solution;
        }

        // changeNode - The next index in the solution that needs to be changed
        int changeNode = solutionNode + 1;
        int source, destination;

        // There are n worker processes, where n = the coloring degree of the problem.
        // These processes have an id from 1 to n, same for the colors, therefore we associate each process with a color.
        // The main process will send n partial solutions that are 0-index complete exactly once.
        // The target process id will be dictated by the color id of the changed node.
        //      e.g. 10000 -> p1 ; 20000 -> p2 ; 30000 -> p3
        //
        // The worker processes will send n-1 partial solutions that are changeNode-index complete solutions at a time.
        // The target process id will be dictated by the color id of the changed node.
        //      e.g. 12000 -> p2 ; 13000 -> p3
        for (int currentCode = 1; currentCode <= noColors; currentCode++) {

            destination = currentCode;

            if (currentCode != mpiId) {

                // A buffer containing the partial solution, prefixed with the index of the changed node will be sent.
                int[] nodeBuf = new int[]{changeNode};

                int[] codesBuf = getArrayCopy(solution);
                codesBuf[changeNode] = currentCode;

                int[] buf = new int[nodeBuf.length + codesBuf.length];

                System.arraycopy(nodeBuf, 0, buf, 0, nodeBuf.length);
                System.arraycopy(codesBuf, 0, buf, nodeBuf.length, codesBuf.length);

                MPI.COMM_WORLD.Isend(buf, 0, buf.length, MPI.INT, destination, 0);

                // System.out.println("Process " + mpiId + " sent " + Arrays.toString(codesBuf) + " to process " + currentCode);
            }
        }

        int[] result;

        // 1 new partial solution will be left for the current process, without sending it to itself. e.g. 11000
        if (mpiId != 0) {

            int[] nextCodes = getArrayCopy(solution);
            nextCodes[changeNode] = mpiId;

            result = graphColoringRec(changeNode, graph, noColors, nextCodes, mpiId);

            if (result[0] != -1) {
                return result;
            }
        }

        // The main process will receive the complete solutions.
        // The worker processes will receive partial solutions from other worker processes.
        for (int currentCode = 1; currentCode <= noColors; currentCode++) {

            source = currentCode;

            if (currentCode != mpiId) {

                int[] buf = new int[noNodes + 1];

                MPI.COMM_WORLD.Recv(buf, 0, noNodes + 1, MPI.INT, source, 0);

                int prevNode = buf[0];

                int[] codes = new int[noNodes];

                System.arraycopy(buf, 1, codes, 0, codes.length);

                // System.out.println("Process " + mpiId + " received " + Arrays.toString(codes) + " from process " + currentCode);

                if (mpiId != 0) {

                    // After the worker process has received the partial solution,
                    // a recursive call of this function is made to verify or continue the solution.
                    result = graphColoringRec(prevNode, graph, noColors, codes, mpiId);
                    if (result[0] != -1) {
                        return result;
                    }
                } else {

                    // After the main process has received the final solution, it shows it if it's valid.
                    if (codes[0] != -1) {

                        System.out.println("Worker " + source + " yielded final solution " + Colors.getNodesToColors(codes));
                        // return codes;
                    }
                }
            }
        }

        return getInvalidSolution(noNodes);
    }

    public static void graphColoringWorker(int mpiId, Graph graph) throws InterruptedException {

        int noNodes = graph.getNoNodes();
        int noColors = Colors.getNoColors();

        int[] initialSolution = new int[noNodes + 1];

        // At first, the worker processes receive partial solutions that are 0-index complete.
        MPI.COMM_WORLD.Recv(initialSolution, 0, noNodes + 1, MPI.INT, 0, 0);

        int prevNode = initialSolution[0];

        int[] initialCodes = new int[noNodes];

        System.arraycopy(initialSolution, 1, initialCodes, 0, initialCodes.length);

        int[] newCodes = graphColoringRec(prevNode, graph, noColors, initialCodes, mpiId);

        // The final solution, valid or not, will be sent back to the main process.
        int[] buf = new int[noNodes + 1];

        buf[0] = graph.getNoNodes() - 1;

        System.arraycopy(newCodes, 0, buf, 1, newCodes.length);

        MPI.COMM_WORLD.Isend(buf, 0, noNodes + 1, MPI.INT, 0, 0);

        // System.out.println("Process " + mpiId + " sent " + Arrays.toString(newCodes) + " to process 0");
    }

    /*
       solution - The partial solution
       node - Until which node the solution is complete
       graph - The whole graph
     */
    private static boolean isCodeValid(int node, int[] solution, Graph graph) {

        for (int currentNode = 0; currentNode < node; currentNode++) {

            if (graph.isEdge(node, currentNode) && solution[node] == solution[currentNode])
                return false;
        }

        return true;
    }

    private static int[] getInvalidSolution(int length) {

        int[] array = new int[length];
        Arrays.fill(array, -1);

        return array;
    }

    private static int[] getArrayCopy(int[] array) {

        return Arrays.copyOf(array, array.length);
    }
}