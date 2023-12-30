package org.example;

import mpi.MPI;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class DistributedSharedMemory {
    private final Map<String, Set<Integer>> subscribers = new HashMap<>();
    private static final ReentrantLock lock = new ReentrantLock();
    private final Map<String, Integer> variables = new HashMap<>();

    public DistributedSharedMemory() {
        variables.put("1", 1);
        variables.put("2", 2);
        variables.put("3", 3);

        subscribers.put("1", new LinkedHashSet<>());
        subscribers.put("2", new LinkedHashSet<>());
        subscribers.put("3", new LinkedHashSet<>());
    }

    public void setVariable(String name, int value) {
        lock.lock();
        try {
            variables.put(name, value);
            subscribers.put(name, new LinkedHashSet<>());
        } finally {
            lock.unlock();
        }
    }

    public void updateVariable(String name, int value) {
        lock.lock();
        try {
            variables.put(name, value);
            sendMessageToSubscribers(name, new UpdateMessage(name, value));
        } finally {
            lock.unlock();
        }
    }

    public void checkAndReplace(String name, int oldValue, int newValue) {
        lock.lock();
        if (variables.get(name) == oldValue) {
            variables.put(name, newValue);
            sendMessageToSubscribers(name, new UpdateMessage(name, newValue));
        }
        lock.unlock();
    }

    public void sendMessageToSubscribers(String name, AbstractMessage message) {
        for (int index = 0; index < MPI.COMM_WORLD.Size(); index++) {
            if (!subscribers.get(name).contains(index) || MPI.COMM_WORLD.Rank() == index) {
                continue;
            }
            MPI.COMM_WORLD.Send(new Object[]{message}, 0, 1, MPI.OBJECT, index, 0);

        }
    }

    public void broadcastMessage(AbstractMessage message) {
        for (int index = 0; index < MPI.COMM_WORLD.Size(); index++) {
            if (MPI.COMM_WORLD.Rank() == index || !(message instanceof CloseMessage)) {
                continue;
            }
            MPI.COMM_WORLD.Send(new Object[]{message}, 0, 1, MPI.OBJECT, index, 0);

        }
    }

    public void syncSubscription(String name, int rank) {
        Set<Integer> subscribers = this.subscribers.get(name);
        subscribers.add(rank);
        this.subscribers.put(name, subscribers);
    }

    public void subscribeTo(String name) {
        Set<Integer> subscribers = this.subscribers.get(name);
        subscribers.add(MPI.COMM_WORLD.Rank());
        this.subscribers.put(name, subscribers);
        this.broadcastMessage(new SubscribeMessage(name, MPI.COMM_WORLD.Rank()));
    }

    public void close() {
        broadcastMessage(new CloseMessage());
    }

    @Override
    public String toString() {
        return "DistributedSharedMemory{" +
                "subscribers=" + subscribers +
                ", variables=" + variables +
                '}';
    }
}
