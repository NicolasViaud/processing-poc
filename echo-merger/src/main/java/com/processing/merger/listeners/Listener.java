package com.processing.merger.listeners;

import com.processing.merger.messages.EchoMergeMessage;
import com.processing.merger.services.EchoMergerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class Listener {

    private final EchoMergerService echoMergerService;

    @RabbitListener(queues = "queue.processing.echo.merge")
    public void receiveMessage(Message<EchoMergeMessage> message) {
        log.debug("Received {}", message);
        final EchoMergeMessage payload = message.getPayload();
        echoMergerService.start(
                payload.getProcessingId(),
                payload.getTileURLs(),
                payload.getImageSizeX(),
                payload.getImageSizeY(),
                payload.getGeoTransform());
    }

}
