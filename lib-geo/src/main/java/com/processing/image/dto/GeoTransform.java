package com.processing.image.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GeoTransform {

    private Crs crs;
    private Double oriX;
    private Double oriY;
    private Double resX;
    private Double resY;
    private Double skewX;
    private Double skewY;

}
