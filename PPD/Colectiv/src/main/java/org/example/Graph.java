package org.example;


import java.util.*;


public class Graph {

    private Set<Node> nodes;

    private final int threadCount = 16;

    public Graph(int size) {

        nodes = new HashSet<>(size);

        for (int id = 0; id < size; id++) {

            nodes.add(new Node(id));
        }
    }

    public void generateRandomGraph(int nodesCount, int edgeCount) {
        Random random = new Random();

        nodes = new HashSet<>(nodesCount);

        for (int id = 0; id < nodesCount; id++) {

            nodes.add(new Node(id));
        }

//        for (int i = 0; i < nodesCount - 1; i++) {
//            setEdge(i, i + 1);
//        }

        for (int i = nodesCount; i < edgeCount; i++) {

            int n1 = random.nextInt(nodesCount);
            int n2 = random.nextInt(nodesCount);

            while (n1 == n2 || isEdge(n1, n2)) {
                n1 = random.nextInt(nodesCount);
                n2 = random.nextInt(nodesCount);
            }

            setEdge(n1, n2);
        }
    }

    public void setEdge(Integer n1, Integer n2) {

        Node node1 = nodes.stream().filter((n) -> Objects.equals(n.getId(), n1)).findFirst().get();
        Node node2 = nodes.stream().filter((n) -> Objects.equals(n.getId(), n2)).findFirst().get();

        node1.setAdjacentNode(node2);
        node2.setAdjacentNode(node1);
    }

    public boolean isEdge(Integer n1, Integer n2) {

        Node node1 = nodes.stream().filter((n) -> Objects.equals(n.getId(), n1)).findFirst().get();
        Node node2 = nodes.stream().filter((n) -> Objects.equals(n.getId(), n2)).findFirst().get();

        return node1.isAdjacent(node2);
    }


    public List<Node> getNodes() {

        return nodes.stream().toList().stream().sorted().toList();
    }

    public Node getNodeById(int id) {

        return nodes.stream().filter((n) -> Objects.equals(n.getId(), id)).findFirst().get();
    }

    public int getNoNodes() {

        return nodes.size();
    }
}