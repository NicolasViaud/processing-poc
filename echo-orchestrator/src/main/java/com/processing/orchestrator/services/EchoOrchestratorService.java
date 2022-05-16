package com.processing.orchestrator.services;

import com.processing.image.dto.GeoTransform;
import com.processing.orchestrator.domains.EchoOrchestrator;
import com.processing.orchestrator.repositories.EchoOrchestratorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@AllArgsConstructor
@Service
public class EchoOrchestratorService {

    private final EchoOrchestratorRepository echoOrchestratorRepository;

    @Transactional
    public EchoOrchestrator createOrchestrator(String processingId, String imageUrl, int imageSizeX, int imageSizeY, GeoTransform geoTransform) {
        return echoOrchestratorRepository.findByProcessingId(processingId).orElseGet(() -> {
            final EchoOrchestrator echoOrchestrator = new EchoOrchestrator();
            echoOrchestrator.setProcessingId(processingId);
            echoOrchestrator.setImageURL(imageUrl);
            echoOrchestrator.setImageSizeX(imageSizeX);
            echoOrchestrator.setImageSizeY(imageSizeY);
            echoOrchestrator.setGeoTransform(geoTransform);
            return echoOrchestratorRepository.save(echoOrchestrator);
        });
    }

}
