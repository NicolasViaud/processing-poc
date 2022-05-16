package com.processing.orchestrator.services;

import com.processing.orchestrator.domains.Orchestrator;
import com.processing.orchestrator.domains.Worker;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.function.Consumer;

@Service
@AllArgsConstructor
@Slf4j
public class WorkerService {

    private final RabbitTemplate rabbitTemplate;

//    private WorkerRepository workerRepository;

//    private OrchestratorRepository orchestratorRepository;


    @Transactional
    public <O extends Orchestrator, W extends Worker> void reduce(String processingId,
                                                                  String workerId,
                                                                  Consumer<W> onWorkerDone,
                                                                  Consumer<O> onAllWorkersDone

    ) {
        //TODO remplacer function callbackEchoWorker avec un code modulaire
    }

}
