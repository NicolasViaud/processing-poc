package com.processing.orchestrator.services;

import com.processing.image.dto.GeoTransform;
import com.processing.image.dto.Grid;
import com.processing.orchestrator.domains.EchoOrchestrator;
import com.processing.orchestrator.domains.EchoWorker;
import com.processing.orchestrator.domains.Worker;
import com.processing.orchestrator.messages.EchoMergeMessage;
import com.processing.orchestrator.messages.EchoWorkerMessage;
import com.processing.orchestrator.repositories.EchoOrchestratorRepository;
import com.processing.orchestrator.repositories.EchoWorkerRepository;
import com.processing.orchestrator.services.errorsgenerator.ErrorGeneratorService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class EchoWorkerService {

    private final RabbitTemplate rabbitTemplate;

    private final EchoWorkerRepository echoWorkerRepository;

    private final EchoOrchestratorRepository echoOrchestratorRepository;

    private final ErrorGeneratorService errorGeneratorService;

    /**
     * Create tiles workers
     *
     * @param imageURL
     * @param grid
     * @return
     */
    @Transactional
    public List<EchoWorker> createWorkers(String processingId, String imageURL, Grid grid, GeoTransform geoTransform) {
        if (!echoWorkerRepository.existsByProcessingId(processingId)) {//for idempotent purpose
            final List<EchoWorker> workers = grid.getCells().stream()
                    .map(cell -> EchoWorker.builder()
                            .state(Worker.State.INIT)
                            .processingId(processingId)
                            .workerId(UUID.randomUUID().toString())
                            .tileSizeX(cell.getSizeX())
                            .tileSizeY(cell.getSizeY())
                            .geoTransform(cell.getGeoTransform())
                            .imageURL(imageURL)
                            .build())
                    .collect(Collectors.toList());
            final List<EchoWorker> workersSaved = echoWorkerRepository.saveAll(workers);
            errorGeneratorService.afterInitWorkersInDatabase();
            return workersSaved;
        } else {
            return echoWorkerRepository.findByProcessingId(processingId);
        }
    }

    @Transactional
    public void startEchoWorker(EchoWorker echoWorker, int totalWorkers) {
        if (echoWorker.getState() == Worker.State.INIT) {
            echoWorker.setState(Worker.State.PROCESSING);
            echoWorkerRepository.save(echoWorker);
            final EchoWorkerMessage message = new EchoWorkerMessage();
            message.setProcessingId(echoWorker.getProcessingId());
            message.setWorkerId(echoWorker.getWorkerId());
            message.setImageURL(echoWorker.getImageURL());
            message.setTileSizeX(echoWorker.getTileSizeX());
            message.setTileSizeY(echoWorker.getTileSizeY());
            message.setGeoTransform(echoWorker.getGeoTransform());
            message.setTrackerTotalWorkers(totalWorkers);
            errorGeneratorService.afterProcessingWorkerInDatabase();
            rabbitTemplate.convertAndSend("exchange.processing.echo", "processing.echo.worker", message);
        }
    }

    /**
     * TODO reflechir à la synchronisation entre plusieurs thread. Possibilité de ne jamais lancer de merge
     *
     * @param processingId
     * @param workerId
     * @param tileURL
     */
    @Transactional
    public void callbackEchoWorker(String processingId, String workerId, String tileURL) {
        echoWorkerRepository.findByWorkerId(workerId).ifPresentOrElse(
                echoWorker -> {
                    echoWorker.setState(Worker.State.DONE);
                    echoWorker.setTileURL(tileURL);
                    final Set<Worker.State> states = echoWorkerRepository.findDistinctStateByProcessingId(processingId);
                    if (states.size() == 1 && states.contains(Worker.State.DONE)) {
                        log.debug("All workers are in state {}. Sending merge message", states);
                        final EchoOrchestrator echoOrchestrator = echoOrchestratorRepository.findByProcessingId(processingId).orElseThrow(IllegalStateException::new);
                        final List<EchoWorker> echoWorkers = echoWorkerRepository.findByProcessingId(processingId);
                        final EchoMergeMessage message = new EchoMergeMessage();
                        message.setProcessingId(echoWorker.getProcessingId());
                        message.setTileURLs(echoWorkers.stream().map(EchoWorker::getTileURL).collect(Collectors.toList()));
                        message.setImageSizeX(echoOrchestrator.getImageSizeX());
                        message.setImageSizeY(echoOrchestrator.getImageSizeY());
                        message.setGeoTransform(echoOrchestrator.getGeoTransform());
                        rabbitTemplate.convertAndSend("exchange.processing.echo", "processing.echo.merge", message);
                    }
                },
                () -> log.error("Can't find worker with id {} for callback processing. Skipping message", workerId)
        );
    }

}
