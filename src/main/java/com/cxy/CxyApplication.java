package com.cxy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.cxy.dao")
public class CxyApplication {

    public static void main(String[] args) {
        SpringApplication.run(CxyApplication.class, args);
    }
}
