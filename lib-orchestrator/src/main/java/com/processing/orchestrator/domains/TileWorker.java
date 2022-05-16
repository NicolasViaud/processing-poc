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
public abstract class TileWorker extends Worker {

    private Integer tileSizeX;
    private Integer tileSizeY;

    @Embedded
    private GeoTransform geoTransform;

    private String tileURL;

}
