package com.processing.worker.messages;

import com.processing.image.dto.GeoTransform;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EchoWorkerMessage {

    private String processingId;
    private String workerId;
    private String imageURL;
    private Integer tileSizeX;
    private Integer tileSizeY;

    private GeoTransform geoTransform;

    private Integer trackerTotalWorkers;

}
