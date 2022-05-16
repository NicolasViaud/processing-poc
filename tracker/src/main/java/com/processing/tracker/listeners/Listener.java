package com.processing.tracker.listeners;

import com.processing.tracker.messages.EchoMergeCallbackMessage;
import com.processing.tracker.messages.EchoStartMessage;
import com.processing.tracker.messages.EchoWorkerCallbackMessage;
import com.processing.tracker.services.TrackerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Slf4j
@Component
@AllArgsConstructor
public class Listener {

    private final TrackerService trackerService;

    @RabbitListener(queues = "queue.tracker.echo.start")
    public void receiveStartMessage(Message<EchoStartMessage> message) {
        log.debug("Received start {}", message);
        //TODO for chain workers, identify the number with the request param. For example, when geom=true => worker chain = 2 else 1
        final EchoStartMessage payload = message.getPayload();
        trackerService.startProcessing(payload.getProcessingId(), ZonedDateTime.now());
    }

    @RabbitListener(queues = "queue.tracker.echo.worker.callback")
    public void receiveWorkerCallbackMessage(Message<EchoWorkerCallbackMessage> message) {
        log.debug("Received worker callback {}", message);
        final EchoWorkerCallbackMessage payload = message.getPayload();
        trackerService.updateProcessingWhenWorkerFinished(payload.getProcessingId(), payload.getTrackerTotalWorkers(), ZonedDateTime.now());
    }

    @RabbitListener(queues = "queue.tracker.echo.merge.callback")
    public void receiveMergeCallbackMessage(Message<EchoMergeCallbackMessage> message) {
        log.debug("Received merge callback {}", message);
        final EchoMergeCallbackMessage payload = message.getPayload();
        trackerService.finishProcessing(payload.getProcessingId(), ZonedDateTime.now());
    }

}
