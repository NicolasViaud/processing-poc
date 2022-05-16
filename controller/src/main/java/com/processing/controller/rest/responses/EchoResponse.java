package com.processing.controller.rest.responses;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EchoResponse {

    private final String processingId;

}
