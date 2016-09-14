package com.example.commands;

import lombok.*;
import lombok.experimental.NonFinal;
import lombok.experimental.Tolerate;

import java.beans.Transient;
import java.util.UUID;

@Data
public abstract class AbstractCommand <T extends AbstractCommand> {

    @Getter
    private String id;

    // Instant eventTime;

    abstract public String getType();

    public AbstractCommand(String id) {
        this.id = id;
    }

    public AbstractCommand() {
        id = UUID.randomUUID().toString();
    }

    public void setId(String id) {
        this.id = id;
    }
}
