package org.example;

import mpi.MPI;

public class Subscriber implements Runnable {
    private final DistributedSharedMemory distributedSharedMemory;

    public Subscriber(DistributedSharedMemory distributedSharedMemory) {
        this.distributedSharedMemory = distributedSharedMemory;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println(MPI.COMM_WORLD.Rank() + " is waiting for message");
            Object[] message = new Object[1];
            MPI.COMM_WORLD.Recv(message, 0, 1, MPI.OBJECT, MPI.ANY_SOURCE, 0);
            AbstractMessage abstractMessage = (AbstractMessage) message[0];

            if (abstractMessage instanceof CloseMessage) {
                System.out.println(MPI.COMM_WORLD.Rank() + " is closing");
                break;
            } else if (abstractMessage instanceof SubscribeMessage subscribeMessage) {
                System.out.println("Subscribe message in " + MPI.COMM_WORLD.Rank() + ": " + subscribeMessage.getRank() + " subscribes to " + subscribeMessage.getVariable());
                this.distributedSharedMemory.syncSubscription(subscribeMessage.getVariable(), subscribeMessage.getRank());
            } else if (abstractMessage instanceof UpdateMessage updateMessage) {
                System.out.println("Update message in " + MPI.COMM_WORLD.Rank() + ": " + updateMessage.getName() + " is updated to " + updateMessage.getValue());
                this.distributedSharedMemory.setVariable(updateMessage.getName(), updateMessage.getValue());
            }
        }
        System.out.println("Final:" + MPI.COMM_WORLD.Rank() + " done and dsm = " + this.distributedSharedMemory);
    }
}
