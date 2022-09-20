package com.greenblat.micro.plannerusers.mq;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(TodoBinding.class)
public class MessageProducer {

    private final TodoBinding todoBinding;

    public MessageProducer(TodoBinding todoBinding) {
        this.todoBinding = todoBinding;
    }

    public void newUserAction(Long userId) {
        Message<Long> message = MessageBuilder.withPayload(userId).build();

        todoBinding.todoOutputChannel().send(message);
    }
}
