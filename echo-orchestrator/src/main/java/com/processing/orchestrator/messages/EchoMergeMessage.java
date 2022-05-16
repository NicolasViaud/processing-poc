package com.processing.orchestrator.messages;

import com.processing.image.dto.GeoTransform;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
public class EchoMergeMessage implements Serializable {

    private String processingId;
    private List<String> tileURLs;
    private Integer imageSizeX;
    private Integer imageSizeY;
    private GeoTransform geoTransform;

}
