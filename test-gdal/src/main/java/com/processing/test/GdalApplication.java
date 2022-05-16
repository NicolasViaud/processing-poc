package com.processing.test;

import lombok.extern.slf4j.Slf4j;
import org.gdal.gdal.*;
import org.gdal.gdalconst.gdalconstConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Vector;
import java.util.stream.Stream;

/**
 * http://webhelp.esri.com/arcgisdesktop/9.3/index.cfm?TopicName=Raster_pyramids
 */
@SpringBootApplication
@Slf4j
public class GdalApplication implements ApplicationRunner {

    /**
     * gdalinfo ~/Projects/processing/images/earthanalyticswk3/BLDR_LeeHill/pre-flood/camera/LeeHill.tif
     * https://epsg.io/map#srs=4326&x=-105.286488&y=40.064831&z=13&layer=streets
     * https://www.google.fr/maps/place/Boulder,+Colorado,+%C3%89tats-Unis/@40.0661381,-105.294925,1238m/data=!3m1!1e3!4m5!3m4!1s0x876b8d4e278dafd3:0xc8393b7ca01b8058!8m2!3d40.0149856!4d-105.2705456
     */
    @Value("classpath:LeeHill.tif")
    private Resource imageLeeHillTif;

    @Value("file:test-gdal/out/preCamera_Comp_warp.tif")
    private Resource imageResultLeeHillWarpTif;
    @Value("file:test-gdal/out/preCamera_Comp_translate.tif")
    private Resource imageResultLeeHillTranslateTif;

    @Value("file:test-gdal/out/processing_echo_1.tif")
    private Resource imageResultTile1Tif;

    @Value("file:test-gdal/out/processing_echo_2.tif")
    private Resource imageResultTile2Tif;

    @Value("file:test-gdal/out/processing_echo_3.tif")
    private Resource imageResultTile3Tif;

    @Value("file:test-gdal/out/processing_echo_4.tif")
    private Resource imageResultTile4Tif;

    @Value("file:test-gdal/out/processing_echo_5.tif")
    private Resource imageResultTile5Tif;

    @Value("file:test-gdal/out/processing_echo_6.tif")
    private Resource imageResultTile6Tif;

    @Value("file:test-gdal/out/processing_echo_7.tif")
    private Resource imageResultTile7Tif;

    @Value("file:test-gdal/out/processing_echo_8.tif")
    private Resource imageResultTile8Tif;

    @Value("file:test-gdal/out/preCamera_Comp_merge.tif")
    private Resource imageResultLeeHillMergeTif;

    @Value("classpath:gcp.json")
    private Resource gcpJson;

    public static void main(String[] args) {
        SpringApplication.run(GdalApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        gdal.SetConfigOption("CPL_DEBUG", "ON");
        gdal.SetConfigOption("CPL_CURL_VERBOSE", "NO");
//        gdal.SetConfigOption("GS_NO_SIGN_REQUEST", "YES");
        gdal.SetConfigOption("GOOGLE_APPLICATION_CREDENTIALS", gcpJson.getFile().getAbsolutePath());
        gdal.SetConfigOption("CPL_VSIL_USE_TEMP_FILE_FOR_RANDOM_WRITE", "YES");
        gdal.AllRegister();

//        translate();
//        merge();
        storage();
    }

    public void translate() throws IOException {

        final Dataset datasetLeeHill = gdal.Open(imageLeeHillTif.getFile().getAbsolutePath(), gdalconstConstants.GA_ReadOnly);
        final Aoi extent = spatialExtent(datasetLeeHill);
        log.debug("Raster count: {}", datasetLeeHill.getRasterCount());
        log.debug("Layer count: {}", datasetLeeHill.GetLayerCount());
        log.debug("Raster X size: {}", datasetLeeHill.getRasterXSize());
        log.debug("Raster Y size: {}", datasetLeeHill.getRasterYSize());
        log.debug("Projection: {}", datasetLeeHill.GetProjection());
        log.debug("Spatial Ref: {}", datasetLeeHill.GetSpatialRef());
        log.debug("Aoi east: {}", datasetLeeHill.GetSpatialRef().GetAreaOfUse().getEast_lon_degree());
        log.debug("Aoi west: {}", datasetLeeHill.GetSpatialRef().GetAreaOfUse().getWest_lon_degree());
        log.debug("Aoi north: {}", datasetLeeHill.GetSpatialRef().GetAreaOfUse().getNorth_lat_degree());
        log.debug("Aoi south: {}", datasetLeeHill.GetSpatialRef().GetAreaOfUse().getSouth_lat_degree());
        log.debug("AREA_OR_POINT: {}", datasetLeeHill.GetMetadataItem("AREA_OR_POINT"));
        log.debug("Geo transform: {}", datasetLeeHill.GetGeoTransform());
        log.debug("Spatial extent: {}", extent);
//		https://gdal.org/tutorials/osr_api_tut.html

//		Vector<String> warpOptions = new Vector<>();
//		gdal.Warp(imageResultLeeHillWarpTif.getFile().getAbsolutePath(),new Dataset[]{datasetLeeHill},new WarpOptions(warpOptions));

        Vector<String> translateOptions = new Vector<>();
        translateOptions.add("-projwin");
        translateOptions.add(Double.toString(extent.getUpperLeft().getX()));
        translateOptions.add(Double.toString(extent.getUpperLeft().getY()));
        translateOptions.add(Double.toString(extent.getLowerRight().getX() - 1000));
        translateOptions.add(Double.toString(extent.getLowerRight().getY()));
        final Dataset datasetLeeHillTranslateTif = gdal.Translate(imageResultLeeHillTranslateTif.getFile().getAbsolutePath(), datasetLeeHill, new TranslateOptions(translateOptions));
        log.debug("Spatial extent: {}", spatialExtent(datasetLeeHillTranslateTif));
    }

    public void merge() throws IOException {
        final Dataset datasetLeeHill = gdal.Open(imageLeeHillTif.getFile().getAbsolutePath(), gdalconstConstants.GA_ReadOnly);
        final Aoi aoi = spatialExtent(datasetLeeHill);

        final Driver driver = gdal.GetDriverByName("GTiff");


        final Dataset datasetMerge = driver.Create(imageResultLeeHillMergeTif.getFile().getAbsolutePath(), 16000, 8000, 3);
        datasetMerge.SetProjection("epsg:32613");
        datasetMerge.SetGeoTransform(new double[]{472000d, 0.25d, 0d, 4436000d, 0d, -0.25d});

        Vector<String> warpOptions = new Vector<>();
//        warpOptions.add("-t_srs");
//        warpOptions.add("32613");
//        warpOptions.add("-te");
//        warpOptions.add(Double.toString(aoi.getUpperLeft().getX()));
//        warpOptions.add(Double.toString(aoi.getUpperLeft().getY()));
//        warpOptions.add(Double.toString(aoi.getLowerRight().getX()));
//        warpOptions.add(Double.toString(aoi.getLowerRight().getY()));

        gdal.Warp(
                datasetMerge,
                Stream.of(
                        imageResultTile1Tif,
                        imageResultTile2Tif,
                        imageResultTile3Tif,
                        imageResultTile4Tif,
                        imageResultTile5Tif,
                        imageResultTile6Tif,
                        imageResultTile7Tif,
                        imageResultTile8Tif)
                        .map(datasetTile -> {
                            try {
                                return gdal.Open(datasetTile.getFile().getAbsolutePath(), gdalconstConstants.GA_ReadOnly);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }).toArray(Dataset[]::new),
                new WarpOptions(warpOptions)
        );

        datasetMerge.FlushCache();
    }

    public void storage() {

        final Dataset datasetLeeHill = gdal.Open("/vsigs/bucket-processing/images/in/LeeHill.tif", gdalconstConstants.GA_ReadOnly);
        final Aoi aoi = spatialExtent(datasetLeeHill);


        final Driver driver = gdal.GetDriverByName("GTiff");
        final Dataset datasetMerge = driver.Create("/vsigs/bucket-processing/images/out/test.tif", 200, 200, 1);
        datasetMerge.SetProjection("epsg:32613");
        datasetMerge.SetGeoTransform(new double[]{472000d, 0.25d, 0d, 4436000d, 0d, -0.25d});
        final Vector<String> warpOptions = new Vector<>();
//        warpOptions.add("-te");
//        warpOptions.add(Double.toString(aoi.getUpperLeft().getX()));
//        warpOptions.add(Double.toString(aoi.getUpperLeft().getY()));
//        warpOptions.add(Double.toString(aoi.getLowerRight().getX()));
//        warpOptions.add(Double.toString(aoi.getLowerRight().getY()));
        gdal.Warp(datasetMerge, new Dataset[]{datasetLeeHill}, new WarpOptions(warpOptions));
        datasetMerge.delete();

    }

    /**
     * https://gis4programmers.wordpress.com/2017/01/06/using-gdal-to-get-raster-extent/
     *
     * @param dataset
     * @return
     */
    public Aoi spatialExtent(Dataset dataset) {
        double cols = dataset.getRasterXSize();
        double rows = dataset.getRasterYSize();
        double[] geoTransform = dataset.GetGeoTransform();
        double upx = geoTransform[0];
        double xres = geoTransform[1];
        double xskew = geoTransform[2];
        double upy = geoTransform[3];
        double yskew = geoTransform[4];
        double yres = geoTransform[5];
        double ulx = upx + 0 * xres + 0 * xskew;
        double uly = upy + 0 * yskew + 0 * yres;
        double llx = upx + 0 * xres + rows * xskew;
        double lly = upy + 0 * yskew + rows * yres;
        double lrx = upx + cols * xres + rows * xskew;
        double lry = upy + cols * yskew + rows * yres;
        double urx = upx + cols * xres + 0 * xskew;
        double ury = upy + cols * yskew + 0 * yres;
        return Aoi.builder()
                .crs(dataset.GetSpatialRef().GetName())
                .upperLeft(new Point(ulx, uly))
                .lowerLeft(new Point(llx, lly))
                .upperRight(new Point(urx, ury))
                .lowerRight(new Point(lrx, lry))
                .build();
    }
}