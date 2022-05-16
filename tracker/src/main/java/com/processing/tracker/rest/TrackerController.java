package com.processing.tracker.rest;

import com.processing.tracker.domains.Tracker;
import com.processing.tracker.repositories.TrackerRepository;
import com.processing.tracker.rest.responses.TrackerResponse;
import com.processing.tracker.services.TrackerService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

@AllArgsConstructor
@RestController
@RequestMapping("trackers")
public class TrackerController {

    private final TrackerRepository trackerRepository;

    private final TrackerService trackerService;

    @GetMapping
    private ResponseEntity<PagedModel<EntityModel<TrackerResponse>>> getAll(
            @RequestParam(name = "estimated", defaultValue = "false") Boolean estimated,
            @ParameterObject Pageable pageRequest,
            @Parameter(hidden = true) PagedResourcesAssembler<TrackerResponse> pagedResourcesAssembler
    ) {
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(
                trackerRepository.findAll(pageRequest)
                        .map(tracker -> toResponse(tracker, estimated, ZonedDateTime.now()))
        ));
    }

    @GetMapping(path = "/{processingId}")
    private ResponseEntity<TrackerResponse> get(
            @PathVariable(name = "processingId") String processingId,
            @RequestParam(name = "estimated", defaultValue = "false") Boolean estimated
    ) {
        return trackerRepository.findByProcessingId(processingId)
                .map(tracker -> toResponse(tracker, estimated, ZonedDateTime.now()))
                .map(ResponseEntity::ok)
                .orElseGet(ResponseEntity.notFound()::build);
    }

    private TrackerResponse toResponse(Tracker tracker, boolean estimated, ZonedDateTime currentDate) {
        return TrackerResponse.builder()
                .processingId(tracker.getProcessingId())
                .progress(trackerService.getProgressPercent(tracker, estimated, currentDate))
                .lastRefresh(tracker.getLastRefresh())
                .creationDate(tracker.getCreationDate())
                .build();
    }

}
