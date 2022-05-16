package com.processing.orchestrator.domains;

import com.processing.image.dto.GeoTransform;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Embedded;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@MappedSuperclass
public class TileOrchestrator extends Orchestrator {

    private Integer imageSizeX;
    private Integer imageSizeY;

    @Embedded
    private GeoTransform geoTransform;

    private String imageURL;
}
