package com.processing.merger.messages;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class EchoMergeCallbackMessage implements Serializable {

    private String processingId;
    private String imageURL;

}
