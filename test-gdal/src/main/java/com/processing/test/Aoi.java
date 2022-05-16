package com.processing.test;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Aoi {

    String crs;
    Point upperLeft;
    Point upperRight;
    Point lowerLeft;
    Point lowerRight;

}

