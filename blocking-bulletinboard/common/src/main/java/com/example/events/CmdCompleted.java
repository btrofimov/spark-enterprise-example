package com.example.events;

import lombok.Value;

import java.util.Optional;

@Value
public class CmdCompleted extends AbstractEvent<CmdCompleted> {

    String id;

    Boolean succeeded;

    String errorMessage;
}
