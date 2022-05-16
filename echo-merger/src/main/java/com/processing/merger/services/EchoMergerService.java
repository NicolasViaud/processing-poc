package com.processing.merger.services;

import com.processing.image.dto.GeoTransform;
import com.processing.image.services.ImageService;
import com.processing.merger.messages.EchoMergeCallbackMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gdal.gdal.*;
import org.gdal.gdalconst.gdalconstConstants;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cloud.function.cloudevent.CloudEventMessageBuilder;
import org.springframework.cloud.function.cloudevent.CloudEventMessageUtils;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

@AllArgsConstructor
@Service
@Slf4j
public class EchoMergerService {

    private final RabbitTemplate rabbitTemplate;

    private final ImageService imageService;

    /**
     * https://www.gislite.com/tutorial/k8024
     *
     * @param processingId
     * @param tileURLs
     * @throws IOException
     */
    public void start(String processingId, List<String> tileURLs, int imageSizeX, int imageSizeY, GeoTransform geoTransform) {
        final String imageOutURL = "/bucket-processing/images/out/processing_echo_" + processingId + "_result.vrt";

        if (!imageService.existAndIsValid(imageOutURL)) {
            createVRT(imageOutURL, tileURLs);
//            createTif(imageOutURL, tileURLs, imageSizeX, imageSizeY, geoTransform);
        } else {
            log.debug("Nothing to do for merging processing {}", processingId);
        }

        final EchoMergeCallbackMessage payload = new EchoMergeCallbackMessage();
        payload.setProcessingId(processingId);
        payload.setImageURL(imageOutURL);
        final Message<EchoMergeCallbackMessage> message = CloudEventMessageBuilder.withData(payload).build(CloudEventMessageUtils.AMQP_ATTR_PREFIX);
        rabbitTemplate.convertAndSend("exchange.processing.echo", "processing.echo.merge.callback", payload);
    }

    public void createVRT(String imageOutURL, List<String> tileURLs) {
        final Vector<String> vrtOptions = new Vector<>();
        final Dataset datasetMerge = gdal.BuildVRT(
                imageService.getGdalURL(imageOutURL, false),
                tileURLs.stream().map(tileUrl -> gdal.Open(imageService.getGdalURL(tileUrl, true), gdalconstConstants.GA_ReadOnly)).toArray(Dataset[]::new),
                new BuildVRTOptions(vrtOptions)
        );

        datasetMerge.delete();
    }

    public void createTif(String imageOutURL, List<String> tileURLs, int imageSizeX, int imageSizeY, GeoTransform geoTransform) {
        final Driver driver = gdal.GetDriverByName("GTiff");
        final int bands = 3;
        final Dataset datasetMerge = driver.Create(imageService.getGdalURL(imageOutURL, false), imageSizeX, imageSizeY, bands);
        datasetMerge.SetProjection(geoTransform.getCrs().getCode());
        datasetMerge.SetGeoTransform(toGdalGeoTransform(geoTransform));

        final Vector<String> warpOptions = new Vector<>();
        gdal.Warp(
                datasetMerge,
                tileURLs.stream().map(tileUrl -> gdal.Open(imageService.getGdalURL(tileUrl, true), gdalconstConstants.GA_ReadOnly)).toArray(Dataset[]::new),
                new WarpOptions(warpOptions)
        );

        datasetMerge.delete();
    }

    private double[] toGdalGeoTransform(GeoTransform geoTransform) {
        return new double[]{
                geoTransform.getOriX(),
                geoTransform.getResX(),
                geoTransform.getSkewX(),
                geoTransform.getOriY(),
                geoTransform.getSkewY(),
                geoTransform.getResY()
        };
    }

}
