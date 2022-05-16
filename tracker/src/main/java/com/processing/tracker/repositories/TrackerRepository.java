package com.processing.tracker.repositories;

import com.processing.tracker.domains.Tracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

public interface TrackerRepository extends JpaRepository<Tracker, Long> {
    @Modifying
    @Query(
            "update Tracker t set " +
                    "t.progress = t.progress + :deltaProgress, " +
                    "t.maxProgress = t.progress + :deltaMaxProgress, " +
                    "t.lastRefresh = :lastRefresh, " +
//                    "t.velocity = :deltaProgress / DATEDIFF('ss', t.lastRefresh , :lastRefresh) " + //For H2
                    "t.velocity = ((DATE_PART('day', :lastRefresh - t.lastRefresh) * 24 + \n" +
                    "                DATE_PART('hour', :lastRefresh - t.lastRefresh)) * 60 +\n" +
                    "                DATE_PART('minute', :lastRefresh - t.lastRefresh)) * 60 +\n" +
                    "                DATE_PART('second', :lastRefresh - t.lastRefresh) * 1000 +\n" +
                    "                DATE_PART('milliseconds', :lastRefresh - t.lastRefresh)" +
                    "where t.processingId = :processingId ")
    void incrementProgressByProcessingId(
            @Param("processingId") String processingId,
            @Param("deltaProgress") Integer deltaProgress,
            @Param("deltaMaxProgress") Integer deltaMaxProgress,
            @Param("lastRefresh") Instant lastRefresh
    );


    Optional<Tracker> findByProcessingId(String processingId);

    @Modifying
    @Query(
            "update Tracker t set " +
                    "t.progress = :deltaProgress, " +
                    "t.lastRefresh = :lastRefresh, " +
                    "t.maxProgress = :maxProgress, " +
                    "t.velocity = :velocity " +
                    "where t.processingId = :processingId ")
    void updateProgressByProcessingId(
            @Param("processingId") String processingId,
            @Param("deltaProgress") Integer deltaProgress,
            @Param("lastRefresh") Instant lastRefresh,
            @Param("maxProgress") Integer maxProgress,
            @Param("velocity") Integer velocity
    );
}
