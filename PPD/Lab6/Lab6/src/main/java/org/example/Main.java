package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class Main {
    private static final int THREAD_COUNT = 32;
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_COUNT);

    private static List<Boolean> visited;
    private static List<Integer> path;
    private static AtomicBoolean cycleFound = new AtomicBoolean(false);
    private static List<Integer> finalPath = new ArrayList<>();

    private static List<List<Integer>> generateRandomGraph(int nodesCount, int edgeCount) {
        List<List<Integer>> graph = new ArrayList<>();

        for (int i = 0; i < nodesCount; i++) {
            graph.add(new ArrayList<>());
        }

        int edges = 1;

        while (edges < edgeCount) {
            int node1 = new Random().nextInt(nodesCount);
            int node2 = new Random().nextInt(nodesCount);

            if (node1 != node2 && !graph.get(node1).contains(node2)) {
                graph.get(node1).add(node2);
                edges++;
            }
        }

        return graph;
    }

    private static List<List<Integer>> loadGraph() throws FileNotFoundException {
        List<List<Integer>> graph = new ArrayList<>();
        int vertices, edges;

        Scanner scanner = new Scanner(new File("src/main/java/org/example/3.txt"));
        vertices = scanner.nextInt();
        edges = scanner.nextInt();

        for (int i = 0; i < vertices; i++) {
            graph.add(new ArrayList<>());
        }

        for (int i = 0; i < edges; i++) {
            int node1 = scanner.nextInt();
            int node2 = scanner.nextInt();
            graph.get(node1).add(node2);
        }

        return graph;
    }

    private static void findHamiltonianCycleSequential(List<List<Integer>> graph, List<Integer> path, int node) throws ExecutionException, InterruptedException {
        int vertices = graph.size();

        if (cycleFound.get()) {
            return;
        }

        if (path.size() > vertices) {
            if (path.getLast() == path.getFirst()) {
                cycleFound.set(true);
            }
            return;
        }

        for (int i = 0; i < graph.get(node).size(); i++) {
            int nextNode = graph.get(node).get(i);
            List<Integer> path2 = new ArrayList<>(path);
            if (!visited.get(nextNode)) {
                path2.add(nextNode);
                visited.set(nextNode, true);
                findHamiltonianCycleSequential(graph, path2, nextNode);
                if (path2.size() > 0) {
                    path2.remove(path2.size() - 1);
                }
                visited.set(nextNode, false);
            }
        }
    }


    private static void findHamiltonianCycleParallel(List<List<Integer>> graph, List<Integer> path, int node) throws ExecutionException, InterruptedException {
        int vertices = graph.size();

        if (cycleFound.get()) {
            return;
        }

        if (path.size() > vertices) {
            if (path.getLast().intValue() == path.getFirst().intValue()) {
                cycleFound.set(true);
                finalPath.addAll(path);
            }
            return;
        }

        for (int i = 0; i < graph.get(node).size(); i++) {
            int nextNode = graph.get(node).get(i);
            List<Integer> path2 = new ArrayList<>(path);
            if (!visited.get(nextNode)) {
                if (((ThreadPoolExecutor) threadPool).getPoolSize() <= THREAD_COUNT - 1) {
                    var result = threadPool.submit(() -> {
                                //System.out.println("threaded");
                                visited.set(nextNode, true);
                                path2.add(nextNode);
                                try {
                                    findHamiltonianCycleParallel(graph, path2, nextNode);
                                } catch (ExecutionException e) {
                                    throw new RuntimeException(e);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                    );
                    if (result.get() == null) {
                        if (!path2.isEmpty()) {
                            path2.remove(path2.size() - 1);
                        }
                        visited.set(nextNode, false);
                    }
                } else {
                    visited.set(nextNode, true);
                    path2.add(nextNode);
                    findHamiltonianCycleParallel(graph, path2, nextNode);
                    if (!path2.isEmpty()) {
                        path2.remove(path2.size() - 1);
                    }
                    visited.set(nextNode, false);
                }

            }
        }


    }


    public static void main(String[] args) throws FileNotFoundException, InterruptedException, ExecutionException {
        List<List<Integer>> graph = loadGraph();
        int vertices = graph.size();
        visited = new ArrayList<>(vertices);
        path = new ArrayList<>(vertices);

        for (int i = 0; i < vertices; i++) {
            visited.add(false);
        }
        var startNode = 0;
        path.add(startNode);
        float startTime = System.nanoTime();
        findHamiltonianCycleSequential(graph, path, startNode);
        if (cycleFound.get()) {
            System.out.println("Hamiltonian cycle found");
        } else {
            System.out.println("Hamiltonian cycle not found");
        }
        float endTime = System.nanoTime();



        System.out.println("Time taken: " + (endTime - startTime) / 1000000 + " ms");


        cycleFound.set(false);
        path.clear();
        path.add(startNode);
        for (int i = 0; i < vertices; i++) {
            visited.set(i, false);
        }

        startTime = System.nanoTime();

        var result = threadPool.submit(() -> {
            try {
                findHamiltonianCycleParallel(graph, path, startNode);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });



        if (result.get() == null) {
            endTime = System.nanoTime();
            if (cycleFound.get()) {
                System.out.println("Time taken Parallel: " + (endTime - startTime) / 1000000 + " ms");
                System.out.println("Hamiltonian cycle found");
                String path = finalPath
                        .stream()
                        .map(i -> i.toString())
                        .collect(Collectors.joining(" -> "));
                System.out.println(path);
            } else {
                System.out.println("Hamiltonian cycle not found");
            }
            threadPool.awaitTermination(1, java.util.concurrent.TimeUnit.SECONDS);
            threadPool.shutdown();
        }


    }
}