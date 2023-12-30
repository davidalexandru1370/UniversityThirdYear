package org.example;

import mpi.MPI;

import java.util.*;
import java.util.stream.Collectors;

public class GraphColoring {

    private Graph graph;

    public GraphColoring(Graph graph) {
        this.graph = graph;
    }

    public Map<Integer, String> colorGraph(int mpiSize, Colors colors) throws Exception {
        int colorsNumber = colors.getColors().size();

        int[] codes = nColoringGraph(0, colorsNumber, new int[graph.getNodesNumber()], 0, mpiSize, 0);

        if (codes[0] == -1) {
            throw new Exception("No solution for this graph");
        }

        return colors.getColorsForNodes(codes);
    }

    private int[] nColoringGraph(int nodeId, int colorsNumber, int[] colorCodes, int mpiRank, int mpiSize, int power) {
        int nodesNumber = graph.getNodesNumber();

        if (!isColorValid(nodeId, colorCodes)) {
            return Collections.nCopies(nodesNumber, -1).stream().mapToInt(i -> i).toArray();
        }

        if (nodeId + 1 == graph.getNodesNumber()) {
            return colorCodes;
        }

        int coefficient = (int) Math.pow(colorsNumber, power);
        int colorCode = 0;
        int destination = mpiRank + coefficient * (colorCode + 1);

        while (colorCode + 1 < colorsNumber && destination < mpiSize) {
            colorCode += 1;
            destination = mpiRank + coefficient * (colorCode + 1);
        }

        int nextNode = nodeId + 1;
        int nextPower = power + 1;

        for (int currentColorCode = 1; currentColorCode < colorsNumber; currentColorCode++) {
            destination = mpiRank + coefficient * currentColorCode;
            int[] message = new int[]{mpiRank, nextNode, nextPower};
            MPI.COMM_WORLD.Send(message, 0, message.length, MPI.INT, destination, 0);

            int[] nextColorCodes = Arrays.copyOf(colorCodes, colorCodes.length);

            nextColorCodes[nextNode] = currentColorCode;

            MPI.COMM_WORLD.Send(nextColorCodes, 0, nodesNumber, MPI.INT, destination, 0);
        }

        int[] nextColorCodes = Arrays.copyOf(colorCodes, colorCodes.length);

        nextColorCodes[nextNode] = 0;

        int[] result = nColoringGraph(nextNode, colorsNumber, nextColorCodes, mpiRank, mpiSize, nextPower);

        if (result[0] != -1) {
            return result;
        }

        for (int currentColorCode = 1; currentColorCode < colorCode; currentColorCode++) {
            destination = mpiRank + coefficient * currentColorCode;
            result = new int[nodesNumber];

            MPI.COMM_WORLD.Recv(result, 0, nodesNumber, MPI.INT, destination, MPI.ANY_TAG);
            if (result[0] != -1) {
                return result;
            }
        }

        //try remaining colors for next node (on this process)
        for (int currentColorCode = colorCode; currentColorCode < colorsNumber; currentColorCode++) {
            nextColorCodes = Arrays.copyOf(colorCodes, colorCodes.length);
            nextColorCodes[nextNode] = currentColorCode;

            result = nColoringGraph(nextNode, colorsNumber, nextColorCodes, mpiRank, mpiSize, nextPower);

            if (result[0] != -1) {
                return result;
            }
        }

        //invalid solution (result is an array of -1)
        return result;
    }

    public void graphColoringReceive(int mpiRank, int mpiSize, int colorsNumber) {
        int nodesNumber = graph.getNodesNumber();

        //receive data
        int[] data = new int[3];
        MPI.COMM_WORLD.Recv(data, 0, data.length, MPI.INT, MPI.ANY_SOURCE, MPI.ANY_TAG);

        int parent = data[0];
        int nodeId = data[1];
        int power = data[2];

        int[] codes = new int[nodesNumber];
        MPI.COMM_WORLD.Recv(codes, 0, nodesNumber, MPI.INT, MPI.ANY_SOURCE, MPI.ANY_TAG);

        //recursive call
        int[] result = this.nColoringGraph(nodeId, colorsNumber, codes, mpiRank, mpiSize, power);

        //send data to parent
        MPI.COMM_WORLD.Send(result, 0, nodesNumber, MPI.INT, parent, 0);
    }


    private boolean isColorValid(int node, int[] colors) {
        for (int neighbour : graph.getNeighbours(node)) {
            if (colors[neighbour] == colors[node]) {
                return false;
            }
        }

        return true;
    }


}
