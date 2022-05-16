package com.processing.controller.rest;

import com.processing.controller.domains.Processing;
import com.processing.controller.rest.requests.AoiRequest;
import com.processing.controller.rest.requests.EchoRequest;
import com.processing.controller.rest.responses.EchoResponse;
import com.processing.controller.services.ProcessingService;
import com.processing.image.dto.Aoi;
import com.processing.image.services.CrsService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.BasicLinkBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("processing")
public class ProcessingController {

    private final ProcessingService processingService;

    private final CrsService crsService;

    @PostMapping(path = "echo")
    public EntityModel<EchoResponse> echo(
            @Valid @RequestBody EchoRequest body
    ) {
        final Processing processing = processingService.startEchoProcessing(
                body.getImageURL(),
                body.getImageSizeX(),
                body.getImageSizeY(),
                toAoi(body.getAoi())
        );
        return EntityModel.of(
                EchoResponse.builder().processingId(processing.getId()).build(),
                BasicLinkBuilder.linkToCurrentMapping().slash("trackers").slash(processing.getId()).withRel("tracker")
        );
    }

    private Aoi toAoi(AoiRequest aoiRequest) {
        final Aoi aoi = new Aoi();
        aoi.setCrs(crsService.toCrs(aoiRequest.getCrs()));
        aoi.setUpperLeft(aoiRequest.getUpperLeft());
        aoi.setUpperRight(aoiRequest.getUpperRight());
        aoi.setLowerLeft(aoiRequest.getLowerLeft());
        aoi.setLowerRight(aoiRequest.getLowerRight());
        return aoi;
    }


}
