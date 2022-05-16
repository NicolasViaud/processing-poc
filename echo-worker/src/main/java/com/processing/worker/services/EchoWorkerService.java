package com.processing.worker.services;

import com.processing.image.dto.GeoTransform;
import com.processing.image.dto.Point;
import com.processing.image.services.GeoService;
import com.processing.image.services.ImageService;
import com.processing.worker.listener.Context;
import com.processing.worker.messages.EchoWorkerCallbackMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.WarpOptions;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cloud.function.cloudevent.CloudEventMessageBuilder;
import org.springframework.cloud.function.cloudevent.CloudEventMessageUtils;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Vector;

@AllArgsConstructor
@Service
@Slf4j
public class EchoWorkerService {

    private final RabbitTemplate rabbitTemplate;

    private final GeoService geoService;

    private final ImageService imageService;

    @Transactional
    public void start(String processingId, String workerId, String imageURL, int tileSizeX, int tileSizeY, GeoTransform geoTransform) {
        final String imageOutURL = "/bucket-processing/images/out/processing_echo_" + processingId + "_tile_" + workerId + ".tif";

        //check if image exist and is valid
        if (!imageService.existAndIsValid(imageOutURL)) {
            final Dataset dataset = gdal.Open(imageService.getGdalURL(imageURL, true), gdalconstConstants.GA_ReadOnly);
            final Driver driver = gdal.GetDriverByName("GTiff");
            final Dataset datasetTile = driver.Create(imageService.getGdalURL(imageOutURL, false), tileSizeX, tileSizeY, dataset.GetRasterCount());
            datasetTile.SetProjection(geoTransform.getCrs().getCode());
            datasetTile.SetGeoTransform(new double[]{
                    geoTransform.getOriX(),
                    geoTransform.getResX(),
                    geoTransform.getSkewX(),
                    geoTransform.getOriY(),
                    geoTransform.getSkewY(),
                    geoTransform.getResY()});

            final Point lowerRight = geoService.toGeo(tileSizeX, tileSizeY, geoTransform);
            final Vector<String> warpOptions = new Vector<>();
            warpOptions.add("-t_srs");
            warpOptions.add(geoTransform.getCrs().getCode());
            warpOptions.add("-te");
            warpOptions.add(Double.toString(geoTransform.getOriX()));
            warpOptions.add(Double.toString(geoTransform.getOriY()));
            warpOptions.add(Double.toString(lowerRight.getX()));
            warpOptions.add(Double.toString(lowerRight.getY()));
            gdal.Warp(datasetTile, new Dataset[]{dataset}, new WarpOptions(warpOptions));
            datasetTile.delete();
            log.info("Generation of echo tile {} for processing {}", imageOutURL, processingId);
        } else {
            log.debug("Nothing to do for worker {} for processing {}", workerId, processingId);
        }

        final EchoWorkerCallbackMessage payload = new EchoWorkerCallbackMessage();
        payload.setProcessingId(processingId);
        payload.setWorkerId(workerId);
        payload.setImageURL(imageOutURL);
        payload.setTrackerTotalWorkers(Context.getContext().getTotalWorkers());
        final Message<EchoWorkerCallbackMessage> message = CloudEventMessageBuilder.withData(payload).build(CloudEventMessageUtils.AMQP_ATTR_PREFIX);
        rabbitTemplate.convertAndSend("exchange.processing.echo", "processing.echo.worker.callback", payload);
    }

}
