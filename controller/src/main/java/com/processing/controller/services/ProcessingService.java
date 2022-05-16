package com.processing.controller.services;

import com.processing.controller.domains.Processing;
import com.processing.controller.messages.EchoStartMessage;
import com.processing.image.dto.Aoi;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cloud.function.cloudevent.CloudEventMessageBuilder;
import org.springframework.cloud.function.cloudevent.CloudEventMessageUtils;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ProcessingService {

    private final RabbitTemplate rabbitTemplate;

    public Processing startEchoProcessing(String imageURL, int imageSizeX, int imageSizeY, Aoi aoi) {
        final EchoStartMessage payload = new EchoStartMessage();
        final String processingId = UUID.randomUUID().toString();
        payload.setProcessingId(processingId);
        payload.setImageURL(imageURL);
        payload.setImageSizeX(imageSizeX);
        payload.setImageSizeY(imageSizeY);
        payload.setAoi(aoi);
        final Message<EchoStartMessage> message = CloudEventMessageBuilder.withData(payload).build(CloudEventMessageUtils.AMQP_ATTR_PREFIX);
        rabbitTemplate.convertAndSend("exchange.processing.echo", "processing.echo.start", payload);
//        OR (equivalent)
//        rabbitTemplate.convertAndSend(  "queue.processing.echo.start", message);
        return Processing.builder().id(processingId).build();
    }

}
