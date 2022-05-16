package com.processing.orchestrator.domains;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Table(indexes = {
        @Index(columnList = "processingId"),
        @Index(columnList = "workerId", unique = true),
})
public class EchoWorker extends TileWorker {

    @Id
    @GeneratedValue
    private Long id;

}
