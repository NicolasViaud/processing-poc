package com.processing.image.services;

import com.processing.image.dto.Aoi;
import com.processing.image.dto.GeoTransform;
import com.processing.image.dto.Point;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GeoService {

    public GeoTransform getGeoTransform(double sizeX, double sizeY, Aoi aoi) {
        final double resX = (aoi.getUpperRight().getX() - aoi.getUpperLeft().getX()) / sizeX;
        final double resY = (aoi.getLowerLeft().getY() - aoi.getUpperLeft().getY()) / sizeY;
        final GeoTransform geoTransform = new GeoTransform();
        geoTransform.setCrs(aoi.getCrs());
        geoTransform.setOriX(aoi.getUpperLeft().getX());
        geoTransform.setOriY(aoi.getUpperLeft().getY());
        geoTransform.setResX(resX);
        geoTransform.setResY(resY);
        geoTransform.setSkewX(0d);
        geoTransform.setSkewY(0d);
        return geoTransform;
    }

    public Point toGeo(Point pixel, GeoTransform transform) {
        return toGeo(pixel.getX(), pixel.getY(), transform);
    }

    public Point toGeo(double x, double y, GeoTransform transform) {
        final double geoX = transform.getOriX() + x * transform.getResX() + y * transform.getSkewX();
        final double geoY = transform.getOriY() + x * transform.getSkewY() + y * transform.getResY();
        return new Point(geoX, geoY);
    }

}
