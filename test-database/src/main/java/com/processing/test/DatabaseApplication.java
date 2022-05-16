package com.processing.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class DatabaseApplication implements ApplicationRunner {

    @Autowired
    private ServiceTest serviceTest;

    public static void main(String[] args) {
        SpringApplication.run(DatabaseApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        serviceTest.init();
        for (int i = 0; i < 50; i++) {
            new Thread(() -> serviceTest.updateCounterSafe()).start();
        }
    }
}
