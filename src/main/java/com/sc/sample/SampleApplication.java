package com.sc.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ComponentScan;

@RefreshScope
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan("com.sc")
public class SampleApplication {
    public static void main(String argv[]) {
        SpringApplication.run(SampleApplication.class, argv);
    }
}
