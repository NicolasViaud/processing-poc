package com.processing.orchestrator.services.errorsgenerator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class ErrorPropertiesStrategy implements ErrorStrategy {
    @Value("${errors.on_receive_start_message:false}")
    public boolean errorOnReceiveStartMessage;
    @Value("${errors.after_init_workers_in_database:false}")
    public boolean afterInitWorkersInDatabase;

    @Value("${errors.after_processing_worker_in_database:false}")
    public boolean afterProcessingWorkerInDatabase;

    @Value("${errors.on_receive_worker_callback_message:false}")
    public boolean errorOnReceiveWorkerCallbackMessage;

    @Override
    public void applyErrorStrategy(ErrorSource errorSource) {
        switch (errorSource) {
            case ON_START_MESSAGE:
                if (errorOnReceiveStartMessage) {
                    throwError(errorSource);
                }
                break;
            case AFTER_INIT_WORKERS_IN_DATABASE:
                if (afterInitWorkersInDatabase) {
                    throwError(errorSource);
                }
                break;
            case AFTER_PROCESSING_WORKER_IN_DATABASE:
                if (afterProcessingWorkerInDatabase) {
                    throwError(errorSource);
                }
                break;
            case ON_WORKER_CALLBACK_MESSAGE:
                if (errorOnReceiveWorkerCallbackMessage) {
                    throwError(errorSource);
                }
                break;
        }
    }


}
