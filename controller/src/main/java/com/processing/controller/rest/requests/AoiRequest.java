package com.processing.controller.rest.requests;

import com.processing.image.dto.Point;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AoiRequest {

    private String crs;

    private Point upperLeft;
    private Point upperRight;
    private Point lowerLeft;
    private Point lowerRight;

}
