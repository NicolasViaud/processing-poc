package com.processing.orchestrator.repositories;

import com.processing.orchestrator.domains.EchoOrchestrator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EchoOrchestratorRepository extends JpaRepository<EchoOrchestrator, Long> {

    Optional<EchoOrchestrator> findByProcessingId(String processingId);

}
