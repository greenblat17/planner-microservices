package com.greenblat.micro.plannerusers.mq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

// создание канала
public interface TodoBinding {

    String OUTPUT_CHANNEL = "todoOutputChannel";

    @Output(OUTPUT_CHANNEL)
    MessageChannel todoOutputChannel();
}
