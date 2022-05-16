package com.processing.image.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Crs {
    EPSG32613("epsg:32613"),
    EPSG4326("epsg:4326"),
    EPSG6326("epsg:6326");

    private final String code;

}
