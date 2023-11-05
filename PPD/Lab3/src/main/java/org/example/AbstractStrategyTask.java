package org.example;

import java.util.List;

public abstract class AbstractStrategyTask extends Thread{
    protected final Matrix matrix1;
    protected final Matrix matrix2;
    protected final Matrix result;
    protected final int start;
    protected final int end;

    public AbstractStrategyTask(Matrix matrix1, Matrix matrix2, Matrix result, int start, int end) {
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
        this.result = result;
        this.start = start;
        this.end = end;
    }

    public abstract List<Pair<Integer, Integer>> getCells();
    
    @Override
    public void run(){

    }
}
