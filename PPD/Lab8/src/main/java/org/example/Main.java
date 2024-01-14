package org.example;

import mpi.MPI;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        MPI.Init(args);

        int me = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        DistributedSharedMemory distributedSharedMemory = new DistributedSharedMemory();

        System.out.println("Hello from <" + me + ">" + " size: " + size);

        if (me == 0) {
            Thread thread = new Thread(new Subscriber(distributedSharedMemory));
            thread.start();
            distributedSharedMemory.subscribeTo("1");
            distributedSharedMemory.subscribeTo("2");
            distributedSharedMemory.subscribeTo("3");
            distributedSharedMemory.updateVariable("1", 1);
            distributedSharedMemory.updateVariable("2", 2);
            distributedSharedMemory.updateVariable("3", 3);
            System.out.println(distributedSharedMemory);
            distributedSharedMemory.close();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if (me == 1) {
            Thread thread = new Thread(new Subscriber(distributedSharedMemory));
            thread.start();
            distributedSharedMemory.subscribeTo("1");
            distributedSharedMemory.subscribeTo("3");
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if (me == 2) {
            Thread thread = new Thread(new Subscriber(distributedSharedMemory));
            thread.start();
            distributedSharedMemory.subscribeTo("2");
            distributedSharedMemory.checkAndReplace("2", 2, 5);
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        MPI.Finalize();
    }
}