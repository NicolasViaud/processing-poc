package com.processing.tracker.rest.responses;

import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.server.core.Relation;

import java.time.Instant;

@Getter
@Builder
@Relation(collectionRelation = "trackers", itemRelation = "tracker")
public class TrackerResponse {

    private final String processingId;
    private final Integer progress;
    private final Instant lastRefresh;
    private final Instant creationDate;

}
