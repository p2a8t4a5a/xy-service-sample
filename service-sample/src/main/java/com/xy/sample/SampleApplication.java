package com.xy.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan({"com.xy", "com.sc"})
@EnableCircuitBreaker
@EnableHystrixDashboard
@EnableFeignClients("com.xy")
@EnableRetry
public class SampleApplication {

    public static void main(String argv[]) {
        SpringApplication.run(SampleApplication.class, argv);
    }
}
