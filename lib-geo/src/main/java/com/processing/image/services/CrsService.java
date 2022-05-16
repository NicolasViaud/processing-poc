package com.processing.image.services;

import com.processing.image.dto.Crs;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CrsService {
    public Crs toCrs(String code) {
        switch (code) {
            case "epsg:32613":
                return Crs.EPSG32613;
            case "epsg:4326":
                return Crs.EPSG4326;
            case "epsg:6326":
                return Crs.EPSG6326;
            default:
                throw new IllegalArgumentException("Can't convert code " + code + " to CRS");
        }
    }
}
