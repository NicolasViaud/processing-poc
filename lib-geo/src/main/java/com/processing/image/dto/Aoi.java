package com.processing.image.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Aoi {

    private Crs crs;

    private Point upperLeft;
    private Point upperRight;
    private Point lowerLeft;
    private Point lowerRight;

}

