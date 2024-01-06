package org.example;

import org.example.Node;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Graph {

    private final Set<Node> nodes;

    private final int threadCount = 16;

    public Graph(int size) {

        nodes = new HashSet<>(size);

        for (int id = 0; id < size; id++) {

            nodes.add(new Node(id));
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