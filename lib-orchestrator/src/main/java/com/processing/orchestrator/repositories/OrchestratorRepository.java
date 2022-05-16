package com.processing.orchestrator.repositories;

import com.processing.orchestrator.domains.Orchestrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface OrchestratorRepository<O extends Orchestrator> extends JpaRepository<O, Long> {

    Optional<O> findByProcessingId(String processingId);

}
