package com.processing.worker.listener;

import com.processing.worker.messages.EchoWorkerMessage;
import com.processing.worker.services.EchoWorkerService;
import com.rabbitmq.client.Channel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class Listener {

    private final EchoWorkerService echoWorkerService;

    @RabbitListener(queues = "queue.processing.echo.worker"/*, concurrency = "8"*/)
    public void receiveMessage(Message<EchoWorkerMessage> message, Channel channel) {
        log.debug("Received <" + message + ">");

        final Integer deliveryCount = message.getHeaders().get("x-amqp-delivery-count", Integer.class);
        if (deliveryCount != null && deliveryCount > 1) {
            log.warn("Retry message {} for {}", message.getHeaders().getId(), deliveryCount);
        }

        final EchoWorkerMessage payload = message.getPayload();
        Context.getContext().setTotalWorkers(payload.getTrackerTotalWorkers());
        echoWorkerService.start(payload.getProcessingId(), payload.getWorkerId(), payload.getImageURL(), payload.getTileSizeX(), payload.getTileSizeY(), payload.getGeoTransform());
    }

}
