package org.example;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Producer extends Thread {
    private final Lock lock;
    private final Condition condition;
    private final List<Vector> firstVectors;
    private final List<Vector> secondVectors;
    private final Queue<Integer> queue;
    private final int maximumQueueSize;

    public Producer(Lock lock, Condition condition, List<Vector> firstVectors, List<Vector> secondVectors, Queue<Integer> queue, int maximumQueueSize) {
        this.lock = lock;
        this.condition = condition;
        this.firstVectors = firstVectors;
        this.secondVectors = secondVectors;
        this.queue = queue;
        this.maximumQueueSize = maximumQueueSize;
    }

    @Override
    public void run() {
        for(int index = 0; index < firstVectors.size(); index++) {
            int product = firstVectors.get(index).multiply(secondVectors.get(index));

            this.lock.lock();
            try{
                while(queue.size() >= maximumQueueSize){
                    System.out.println("Queue is full, producer is waiting");
                    this.condition.await();
                }

                System.out.println("Producer produced: " + product);
                this.queue.add(product);
                this.condition.signalAll();
            }
            catch(InterruptedException interruptedException){
                System.out.println("Producer interrupted:" + interruptedException.getMessage());
            }

            this.lock.unlock();
        }
    }
}
