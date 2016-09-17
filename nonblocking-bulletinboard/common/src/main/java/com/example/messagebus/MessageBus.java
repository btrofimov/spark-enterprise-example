package com.example.messagebus;

import com.example.commands.AbstractCommand;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageBus {

    @Autowired
    protected KafkaTemplate<String, String> producer;

    @Value("${kafka.commands-topic}")
    protected String commandsTopic;

    @Value("${kafka.events-topic}")
    protected String eventsTopic;

    ObjectMapper mapper = new ObjectMapper();

    /**
     *
     * @param cmd
     * @param <T>
     */
    public <T extends AbstractCommand<T>> void send(final T cmd) {
        try {
            producer.send(commandsTopic, mapper.writeValueAsString(cmd));

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
