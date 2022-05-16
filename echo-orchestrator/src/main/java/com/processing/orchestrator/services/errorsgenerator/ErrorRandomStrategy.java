package com.processing.orchestrator.services.errorsgenerator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ErrorRandomStrategy implements ErrorStrategy {

    private static final Random RANDOM = new Random();

    @Value("${errors.probability:0.5}")
    public double probability;

    @Override
    public void applyErrorStrategy(ErrorSource errorSource) {
        if (RANDOM.nextDouble() < probability) {
            throwError(errorSource);
        }
    }


}
