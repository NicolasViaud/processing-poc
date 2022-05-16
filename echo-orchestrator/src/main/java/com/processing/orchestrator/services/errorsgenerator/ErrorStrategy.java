package com.processing.orchestrator.services.errorsgenerator;

public interface ErrorStrategy {

    void applyErrorStrategy(ErrorSource errorSource);

    default void throwError(ErrorSource errorSource) {
        throw new IllegalArgumentException("Manual error on source " + errorSource);
    }

    enum ErrorSource {
        ON_START_MESSAGE,
        AFTER_INIT_WORKERS_IN_DATABASE,
        AFTER_PROCESSING_WORKER_IN_DATABASE,
        ON_WORKER_CALLBACK_MESSAGE
    }

}
