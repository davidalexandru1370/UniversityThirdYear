package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Graph {
    private int numberOfNodes;
    private final List<List<Integer>> edges = new ArrayList<>();

    public Graph() throws FileNotFoundException {
        readGraphFromFile("1.txt");
    }

    public void addEdge(int from, int to) {
        edges.get(from).add(to);
    }

    public int getNodesNumber() {
        return numberOfNodes;
    }

    public List<Integer> getOutgoingNeighbours(int node) {
        return edges.get(node);
    }

    public void readGraphFromFile(String filename) throws FileNotFoundException {
        int vertices, edges;

        Scanner scanner = new Scanner(new File(filename));
        vertices = scanner.nextInt();
        edges = scanner.nextInt();

        for (int i = 0; i < vertices; i++) {
            this.edges.add(new ArrayList<>());
        }

        for (int i = 0; i < edges; i++) {
            int node1 = scanner.nextInt();
            int node2 = scanner.nextInt();
            this.edges.get(node1).add(node2);
        }

    }


}
