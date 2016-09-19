package com.example.commands;

import lombok.Data;
import lombok.Getter;

import java.util.UUID;

@Data
public abstract class AbstractCommand <T extends AbstractCommand> {

    @Getter
    private String id;

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
