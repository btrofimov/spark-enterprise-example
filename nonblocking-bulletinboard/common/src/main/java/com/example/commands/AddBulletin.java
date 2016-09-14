package com.example.commands;

import lombok.Builder;
import lombok.Value;

@Value
public class AddBulletin extends AbstractCommand<AddBulletin> {

    public static final String TYPE = "AddBulletin";

    private String author;

    private String message;

    public String getType() {
        return TYPE;
    }

}
