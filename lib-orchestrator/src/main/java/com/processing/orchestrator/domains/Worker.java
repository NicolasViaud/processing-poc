package com.processing.orchestrator.domains;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@MappedSuperclass
public class Worker {

    private String processingId;
    private String workerId;

    @Enumerated(EnumType.STRING)
    private State state;
    private String imageURL;

    public enum State {INIT, PROCESSING, DONE}

}
