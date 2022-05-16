package com.processing.worker.messages;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EchoWorkerCallbackMessage {

    private String processingId;
    private String workerId;
    private String imageURL;
    private Integer trackerTotalWorkers;

}
