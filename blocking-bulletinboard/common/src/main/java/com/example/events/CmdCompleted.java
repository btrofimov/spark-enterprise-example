package com.example.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Optional;

@Value
@Builder
@AllArgsConstructor
public class CmdCompleted extends AbstractEvent<CmdCompleted> {

    String id;

    Boolean succeeded;

    String errorMessage;
}
