package org.example;

import java.io.Serializable;

public class UpdateMessage extends AbstractMessage implements Serializable {
    private final String name;

    private final int value;

    public UpdateMessage(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
