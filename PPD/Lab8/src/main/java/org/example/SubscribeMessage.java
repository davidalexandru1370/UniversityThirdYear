package org.example;

import java.io.Serializable;

public class SubscribeMessage extends AbstractMessage implements Serializable {
    private String variable;
    private int rank;

    public SubscribeMessage(String variable, int rank) {
        this.variable = variable;
        this.rank = rank;
    }

    public String getVariable() {
        return variable;
    }

    public int getRank() {
        return rank;
    }
}
