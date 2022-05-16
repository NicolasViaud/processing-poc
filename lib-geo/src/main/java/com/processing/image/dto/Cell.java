package com.processing.image.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cell {

    private Integer oriX;
    private Integer oriY;
    private Integer sizeX;
    private Integer sizeY;

    private GeoTransform geoTransform;

}
