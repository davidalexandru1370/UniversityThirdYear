package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) {
        int length = 100;
        List<Vector> firstVectors = generateVector(length);
        List<Vector> secondVectors = generateVector(length);

        int maximumQueueSize = 20;

        Queue<Integer> queue = new java.util.LinkedList<>();
        Lock mutex = new ReentrantLock();
        Condition condition = mutex.newCondition();
        Thread producer = new Producer(mutex, condition, firstVectors, secondVectors,queue, maximumQueueSize);
        Consumer consumer = new Consumer(mutex, condition, queue, maximumQueueSize, length);

        producer.start();
        consumer.start();

        try{
            producer.join();
            consumer.join();
        }
        catch (InterruptedException interruptedException){
            System.out.println("Main thread interrupted:" + interruptedException.getMessage());
        }


        System.out.println("Threaded sum = " +  consumer.getSum());

        int sum = 0;
        for(int index = 0; index < length; index++){
            sum += firstVectors.get(index).multiply(secondVectors.get(index));
        }
        System.out.println("Non-threaded sum = " + sum);
    }

    public static List<Vector> generateVector(int length){
        List<Vector> vectors = new ArrayList<>();
        for(int index = 0; index < length; index++){
            int x = (int) (Math.random() * 100);
            int y = (int) (Math.random() * 100);
            int z = (int) (Math.random() * 100);

            vectors.add(new Vector(x, y, z));
        }

        return vectors;
    }
}