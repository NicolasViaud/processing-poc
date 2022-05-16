package com.processing.controller.rest.requests;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class EchoRequest {

    @NotBlank
    private String imageURL;

    @NotNull
    private Integer imageSizeX;

    @NotNull
    private Integer imageSizeY;

    @NotNull
    private AoiRequest aoi;
}