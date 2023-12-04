package org.example;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MultiplicationThread implements Runnable {
    private final Polynomial p1;
    private final Polynomial p2;
    private final Polynomial result;
    private final int left;
    private final int right;
    private final static Lock lock = new ReentrantLock();

    public MultiplicationThread(Polynomial p1, Polynomial p2, Polynomial result, int left, int right) {
        this.p1 = p1;
        this.p2 = p2;
        this.result = result;
        this.left = left;
        this.right = right;
    }

    @Override
    public void run() {
        for (int i = left; i < right; i++) {
            if (i > result.getDegree()) {
                return;
            }
            for (int j = 0; j <= i; j++) {
                if (j <= p1.getDegree() && (i - j) <= p2.getDegree()) {
                    var value = p1.getCoefficients()[j] * p2.getCoefficients()[i - j];
                    //lock.lock();
                    result.setCoefficient(i, result.getCoefficients()[i] + value);
                    //lock.unlock();
                }
            }
        }
    }
}
