package com.processing.orchestrator.listener;

import com.processing.orchestrator.messages.EchoMergeCallbackMessage;
import com.processing.orchestrator.messages.EchoStartMessage;
import com.processing.orchestrator.messages.EchoWorkerCallbackMessage;
import com.processing.orchestrator.services.EchoService;
import com.processing.orchestrator.services.errorsgenerator.ErrorGeneratorService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@AllArgsConstructor
public class Listener {

    private final EchoService echoService;

    private final ErrorGeneratorService errorGeneratorService;

    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "queue.processing.echo.start")
    public void receiveStartMessage(Message<EchoStartMessage> message) throws IOException {
        log.debug("Received start {}", message);
        errorGeneratorService.onReceiveStartMessage();
        final EchoStartMessage payload = message.getPayload();
        echoService.start(
                payload.getProcessingId(),
                payload.getImageURL(),
                payload.getImageSizeX(),
                payload.getImageSizeY(),
                payload.getAoi());
    }

    @RabbitListener(queues = "queue.processing.echo.worker.callback", concurrency = "${concurrent.worker.callback}")
    public void receiveWorkerCallbackMessage(Message<EchoWorkerCallbackMessage> message) {
        log.debug("Received callback {}", message);
        errorGeneratorService.onWorkerCallbackMessage();
        final EchoWorkerCallbackMessage payload = message.getPayload();
        echoService.workerCallback(payload.getProcessingId(), payload.getWorkerId(), payload.getImageURL());
    }

    @RabbitListener(queues = "queue.processing.echo.merge.callback")
    public void receiveMergeCallbackMessage(Message<EchoMergeCallbackMessage> message) {
        log.debug("Received merge {}", message);
        final EchoMergeCallbackMessage payload = message.getPayload();
        echoService.mergeCallback(payload.getProcessingId(), payload.getImageURL());
    }

}
