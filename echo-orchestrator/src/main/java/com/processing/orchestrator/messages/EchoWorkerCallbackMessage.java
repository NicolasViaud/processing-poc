package com.processing.orchestrator.messages;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class EchoWorkerCallbackMessage implements Serializable {

    private String processingId;
    private String workerId;
    private String imageURL;
    private Integer trackerTotalWorkers;

}
