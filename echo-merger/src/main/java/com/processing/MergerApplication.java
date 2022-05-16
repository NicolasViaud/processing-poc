package com.processing;

import org.gdal.gdal.gdal;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MergerApplication {

    public static void main(String[] args) {
        gdal.AllRegister();
        SpringApplication.run(MergerApplication.class, args);
    }


}