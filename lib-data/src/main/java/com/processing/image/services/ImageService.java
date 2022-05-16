package com.processing.image.services;

import lombok.extern.slf4j.Slf4j;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ImageService {

    @Value("${data.local:true}")
    private boolean dataLocal;

    /**
     * @param imageURL
     * @return
     */
    public String getGdalURL(String imageURL, boolean read) {
        final String gdalURL;
        if (dataLocal) {
            gdalURL = "/data" + imageURL;
        } else if (read) {
            gdalURL = "/vsigs_streaming" + imageURL;
        } else {
            gdalURL = "/vsigs" + imageURL;
        }
        log.debug("Gdal URL: {}", gdalURL);
        return gdalURL;
    }

    /**
     * TODO check if is valid
     *
     * @param imageURL
     * @return
     */
    public boolean existAndIsValid(String imageURL) {
        final Dataset datasetTileCheck = gdal.Open(getGdalURL(imageURL, true), gdalconstConstants.GA_ReadOnly);
        if (datasetTileCheck != null) {
            datasetTileCheck.delete();
            return true;
        } else {
            return false;
        }
    }

}
