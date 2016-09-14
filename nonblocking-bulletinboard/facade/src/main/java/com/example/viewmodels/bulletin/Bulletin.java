package com.example.viewmodels.bulletin;

import lombok.Value;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.time.LocalDateTime;

@Value
public class Bulletin {

    @Id
    String id;

    String date;

    String author;

    String message;
}
