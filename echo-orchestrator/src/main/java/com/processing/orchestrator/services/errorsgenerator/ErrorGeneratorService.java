package com.processing.orchestrator.services.errorsgenerator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * TODO pattern delegate and decorator
 */
@Service
@AllArgsConstructor
public class ErrorGeneratorService {


    private final ErrorStrategy errorStrategy;

    /**
     * Should check that the message in queue 'queue.processing.echo.start' is not acknowledged
     *
     * @throws IllegalArgumentException
     */
    public void onReceiveStartMessage() throws IllegalArgumentException {
        errorStrategy.applyErrorStrategy(ErrorStrategy.ErrorSource.ON_START_MESSAGE);
    }

    /**
     * Should check that the message in queue 'queue.processing.echo.start' is not acknowledged and the database insertion rollback
     *
     * @throws IllegalArgumentException
     */
    public void afterInitWorkersInDatabase() throws IllegalArgumentException {
        errorStrategy.applyErrorStrategy(ErrorStrategy.ErrorSource.AFTER_INIT_WORKERS_IN_DATABASE);
    }

    /**
     * Should check that the message in queue 'queue.processing.echo.start' is not acknowledged and the database update rollback and not new insert
     *
     * @throws IllegalArgumentException
     */
    public void afterProcessingWorkerInDatabase() throws IllegalArgumentException {
        errorStrategy.applyErrorStrategy(ErrorStrategy.ErrorSource.AFTER_PROCESSING_WORKER_IN_DATABASE);
    }

    /**
     * Should check that
     *
     * @throws IllegalArgumentException
     */
    public void onWorkerCallbackMessage() throws IllegalArgumentException {
        errorStrategy.applyErrorStrategy(ErrorStrategy.ErrorSource.ON_WORKER_CALLBACK_MESSAGE);
    }

}
