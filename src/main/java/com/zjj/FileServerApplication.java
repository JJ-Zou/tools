package com.zjj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class FileServerApplication {


    public static void main(String[] args) {
        SpringApplication.run(FileServerApplication.class, args);
    }

}
