package com.example.messagebus;

import com.example.commands.AbstractCommand;
import com.example.events.AbstractEvent;
import com.example.events.CmdCompleted;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Component
public class MessageBus {

    @Autowired
    protected KafkaTemplate<String, String> producer;

    @Value("${kafka.commands-topic}")
    protected String commandsTopic;

    @Value("${kafka.events-topic}")
    protected String eventsTopic;

    ObjectMapper mapper = new ObjectMapper(); //.registerModule(new Jdk8Module());

    ConcurrentHashMap<String, Consumer<CmdCompleted>> responses = new ConcurrentHashMap<>();

    /**
     *
     * @param cmd
     * @param <T>
     */
    public <T extends AbstractCommand<T>> void send(final T cmd) {
        try {
            mapper.writeValueAsString(cmd);
            producer.send(commandsTopic, mapper.writeValueAsString(cmd));

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param event
     * @param <T>
     */
    public <T extends AbstractEvent<T>> void send(final T event) {
        try {
            mapper.writeValueAsString(event);
            producer.send(eventsTopic, mapper.writeValueAsString(event));

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param cmd
     * @param callback
     * @param <T>
     */
    public <T extends AbstractCommand<T>> void send(final T cmd, final Consumer<CmdCompleted> callback) {
        try {
            responses.put(cmd.getId(), callback);
            producer.send(commandsTopic, mapper.writeValueAsString(cmd));

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(topics = "${kafka.events-topic}")
    void listen(CmdCompleted event) {
        String eventId = event.getId();
        if(responses.containsKey(eventId)) {
            responses.get(eventId).accept(event);
            responses.remove(eventId);
        }
    }
}
