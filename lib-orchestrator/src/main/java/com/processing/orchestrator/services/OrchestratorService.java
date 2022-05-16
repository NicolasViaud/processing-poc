package com.processing.orchestrator.services;

import com.processing.orchestrator.domains.Orchestrator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class OrchestratorService<O extends Orchestrator> {

//    private OrchestratorRepository orchestratorRepository;

//    @Transactional
//    public O createOrchestrator(String processingId, String imageUrl, Consumer<O> setter) {
//        return orchestratorRepository.findByProcessingId(processingId).orElseGet(() -> {
//            final O orchestrator = O.;
//            orchestrator.setProcessingId(processingId);
//            setter.accept(orchestrator);
//            return orchestratorRepository.save(orchestrator);
//        });
//    }

}
