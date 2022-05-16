package com.processing.orchestrator.services;

import com.processing.image.dto.Aoi;
import com.processing.image.dto.GeoTransform;
import com.processing.image.dto.Grid;
import com.processing.image.services.GeoService;
import com.processing.image.services.GridService;
import com.processing.orchestrator.domains.EchoWorker;
import com.processing.orchestrator.services.errorsgenerator.ErrorGeneratorService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EchoService {

    private static final int CELL_WIDTH = 2000;
    private static final int CELL_HEIGHT = 2000;

    private final GridService gridService;

    private final GeoService geoService;

    private final EchoWorkerService echoWorkerService;

    private final EchoOrchestratorService echoOrchestratorService;

    private final ErrorGeneratorService errorGeneratorService;

    public void start(String processingId, String imageURL, int imageSizeX, int imageSizeY, Aoi aoi) {
        final GeoTransform geoTransform = geoService.getGeoTransform(imageSizeX, imageSizeY, aoi);

        //create orchestrator TODO technical code. To move from here
        echoOrchestratorService.createOrchestrator(processingId, imageURL, imageSizeX, imageSizeY, geoTransform);

        //create workers
        final Grid grid = gridService.split(CELL_WIDTH, CELL_HEIGHT, imageSizeX, imageSizeY, geoTransform);
        final List<EchoWorker> workers = echoWorkerService.createWorkers(processingId, imageURL, grid, geoTransform);

        //start workers
        workers.forEach(worker -> echoWorkerService.startEchoWorker(worker, workers.size()));
    }


    public void workerCallback(String processingId, String workerId, String tileUrl) {
        echoWorkerService.callbackEchoWorker(processingId, workerId, tileUrl);
    }

    public void mergeCallback(String processingId, String imageURL) {
    }
}
