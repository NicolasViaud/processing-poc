package com.processing.image.services;

import com.processing.image.dto.Cell;
import com.processing.image.dto.GeoTransform;
import com.processing.image.dto.Grid;
import com.processing.image.dto.Point;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class GridService {

    private final GeoService geoService;

    public Grid split(int cellSizeX, int cellSizeY, int sizeX, int sizeY, GeoTransform geoTransform) {
        final List<Cell> cells = new ArrayList<>();
        for (double x = 0; x < sizeX; x += cellSizeX) {
            for (double y = 0; y < sizeY; y += cellSizeY) {
                final Cell cell = new Cell();
                cell.setSizeX(cellSizeX);
                cell.setSizeY(cellSizeY);
                final Point geoPoint = geoService.toGeo(new Point(x, y), geoTransform);
                final GeoTransform geoTransformCell = new GeoTransform();
                geoTransformCell.setCrs(geoTransform.getCrs());
                geoTransformCell.setOriX(geoPoint.getX());
                geoTransformCell.setOriY(geoPoint.getY());
                geoTransformCell.setResX(geoTransform.getResX());
                geoTransformCell.setResY(geoTransform.getResY());
                geoTransformCell.setSkewX(geoTransform.getSkewX());
                geoTransformCell.setSkewY(geoTransform.getSkewY());
                cell.setGeoTransform(geoTransformCell);
                cells.add(cell);
            }
        }

        if (cells.isEmpty()) {
            throw new IllegalStateException("Empty grid");
        }

        final Grid grid = new Grid();
        grid.setCells(cells);
        return grid;
    }


}
