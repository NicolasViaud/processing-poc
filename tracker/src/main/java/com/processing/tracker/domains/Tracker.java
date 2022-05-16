package com.processing.tracker.domains;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter
@Setter
@ToString
@Table(indexes = {
        @Index(columnList = "processingId", unique = true),
})
public class Tracker {

    @Id
    @GeneratedValue
    private Long id;

    private String processingId;

    /**
     * On ten thousand
     */
    private Integer progress;

    private Instant creationDate;
    private Instant lastRefresh;

    /**
     * Used to calculate estimated
     * velocity = (estimated - progress)/(currentTime - lastRefresh (in millisec))
     */
    private Integer velocity;

    /**
     * Used to calculate estimated
     */
    private Integer maxProgress;

}
