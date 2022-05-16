package com.processing.tracker.services;

import com.processing.tracker.domains.Tracker;
import com.processing.tracker.repositories.TrackerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Service
@AllArgsConstructor
public class TrackerService {

    private final TrackerRepository trackerRepository;

    @Transactional
    public void startProcessing(String processingId, ZonedDateTime currentDate) {
        final Tracker tracker = new Tracker();
        tracker.setProcessingId(processingId);
        tracker.setProgress(0);
        tracker.setMaxProgress(0);
        tracker.setVelocity(0);
        tracker.setCreationDate(currentDate.toInstant());
        tracker.setLastRefresh(currentDate.toInstant());
        trackerRepository.save(tracker);
    }


    @Transactional
    public void updateProcessingWhenWorkerFinished(String processingId, int totalWorkers, ZonedDateTime currentDate) {
        final int deltaProgress = 9000 / totalWorkers;
        final int deltaMaxProgress = 2 * 9000 / totalWorkers;
        trackerRepository.incrementProgressByProcessingId(processingId, deltaProgress, deltaMaxProgress, currentDate.toInstant());
    }

    @Transactional
    public void finishProcessing(String processingId, ZonedDateTime currentDate) {
        trackerRepository.updateProgressByProcessingId(processingId, 10000, currentDate.toInstant(), 10000, 0);
    }

    public int getProgressPercent(Tracker tracker, boolean estimated, ZonedDateTime currentDate) {
        if (estimated) {
            final int progressEstimated = tracker.getProgress() + (Duration.between(currentDate, tracker.getLastRefresh().atZone(ZoneOffset.UTC)).getNano() / 1000000 * tracker.getVelocity());
            return Math.min(progressEstimated / 100, tracker.getMaxProgress() / 100);
        } else {
            return tracker.getProgress() / 100;
        }
    }


}
