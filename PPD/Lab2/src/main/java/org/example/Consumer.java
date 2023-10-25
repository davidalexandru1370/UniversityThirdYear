package org.example;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Consumer extends Thread {
    private final Lock lock;
    private final Condition condition;
    private final Queue<Integer> queue;
    private final int maximumQueueSize;
    private int sum = 0;
    private int length;

    public Consumer(Lock lock, Condition condition, Queue<Integer> queue, int maximumQueueSize, int length) {
        this.lock = lock;
        this.condition = condition;
        this.queue = queue;
        this.maximumQueueSize = maximumQueueSize;
        this.length = length;
    }

    @Override
    public void run() {
        for (int index = 0; index < length; index++) {
            this.lock.lock();
            try {
                while (queue.isEmpty()) {
                    System.out.println("Queue is empty, consumer is waiting");
                    this.condition.await();
                }

                int product = this.queue.remove();
                sum += product;
                System.out.println("Consumer consumed: " + product);
                this.condition.signalAll();
            } catch (InterruptedException interruptedException) {
                System.out.println("Consumer interrupted:" + interruptedException.getMessage());
            }
            this.lock.unlock();
        }
    }

    public int getSum() {
        return sum;
    }
}
