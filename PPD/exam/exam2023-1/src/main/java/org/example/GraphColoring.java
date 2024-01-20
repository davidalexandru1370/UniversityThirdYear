package org.example;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class GraphColoring {
    private final int threadsNumber;
    private final Graph graph;
    private final Colors colors;
    private final Lock lock = new ReentrantLock();
    private final Vector<Integer> codes = new Vector<>();

    public GraphColoring(int threadsNumber, Graph graph, Colors colors) {
        this.threadsNumber = threadsNumber;
        this.graph = graph;
        this.colors = colors;
    }

    public Map<Integer, String> getColoredGraph() throws Exception {
        Vector<Integer> partialCodes = new Vector<>(Collections.nCopies(graph.getNodesNumber(), 0));
        getColoredGraphRecursive(new AtomicInteger(threadsNumber), 0, colors.getColorsNumber(), partialCodes, 0);

        // No solution
        if (codes.isEmpty()) {
            throw new Exception("No solution found!");
        }

        // Solution found
        return colors.getColorsForCodes(codes);
    }

    private void getColoredGraphRecursive(AtomicInteger threadsNumber, int nodeId, int codesNumber, Vector<Integer> partialCodes, int depth) {
        // Solution already found
        if (!codes.isEmpty()) {
            return;
        }

        // Solution
        if (nodeId + 1 == graph.getNodesNumber()) {
            if (isColorValid(nodeId, partialCodes)) {
                lock.lock();
                try {
                    if (codes.isEmpty()) {
                        codes.addAll(partialCodes);
                    }
                } finally {
                    lock.unlock();
                }
            }
            return;
        }

        // Find possible colors for the next node
        int nextNode = nodeId + 1;
        List<Thread> threads = new ArrayList<>();

        if (depth < 3) {
            for (int code = 0; code < codesNumber; code++) {
                partialCodes.set(nextNode, code);
                if (isColorValid(nextNode, partialCodes)) {
                    if (threadsNumber.getAndDecrement() > 0) {
                        Vector<Integer> nextPartialCodes = new Vector<>(partialCodes);
                        Thread thread = new Thread(() -> getColoredGraphRecursive(threadsNumber, nextNode, codesNumber, nextPartialCodes, depth + 1));
                        thread.start();
                        threads.add(thread);
                    } else {
                        getColoredGraphRecursive(threadsNumber, nextNode, codesNumber, partialCodes, depth + 1);
                    }
                }
            }

            // Join threads
            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } else {
            for (int code = 0; code < codesNumber; code++) {
                partialCodes.set(nextNode, code);
                if (isColorValid(nextNode, partialCodes)) {
                    if (threadsNumber.getAndDecrement() > 0) {
                        Vector<Integer> nextPartialCodes = new Vector<>(partialCodes);
                        getColoredGraphRecursive(threadsNumber, nextNode, codesNumber, nextPartialCodes, depth + 1);
                    }
                }
            }
        }
    }

    private boolean isColorValid(int node, Vector<Integer> codes) {
        for (int current = 0; current < node; current++) {
            if ((graph.existsEdge(node, current) || graph.existsEdge(current, node)) && codes.get(node).equals(codes.get(current))) {
                return false;
            }
        }
        return true;
    }
}
