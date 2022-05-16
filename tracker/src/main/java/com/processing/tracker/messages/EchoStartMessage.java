package com.processing.tracker.messages;

import com.processing.image.dto.Aoi;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class EchoStartMessage implements Serializable {

    private String processingId;
    private String imageURL;
    private Aoi aoi;

}
