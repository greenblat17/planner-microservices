package com.greenblat.micro.plannertodo.mq;

import com.greenblat.micro.plannertodo.service.TestDataService;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(TodoBinding.class)
public class MessageConsumer {

    private final TestDataService testDataService;

    public MessageConsumer(TestDataService testDataService) {
        this.testDataService = testDataService;
    }

    @StreamListener(target = TodoBinding.INPUT_CHANNEL)
    public void initTestData(Long userId) {
        testDataService.initTestData(userId);
    }
}
