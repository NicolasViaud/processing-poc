package com.processing.orchestrator.repositories;

import com.processing.orchestrator.domains.EchoWorker;
import com.processing.orchestrator.domains.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EchoWorkerRepository extends JpaRepository<EchoWorker, Long> {

    boolean existsByProcessingId(String processingId);

    List<EchoWorker> findByProcessingId(String processingId);

    Optional<EchoWorker> findByWorkerId(String workerId);

    @Query("select distinct e.state from EchoWorker e where e.processingId = :processingId")
    Set<Worker.State> findDistinctStateByProcessingId(@Param("processingId") String processingId);

}
