package com.fresh.xy.sample;

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
@ComponentScan({"com.fresh.xy.sample", "com.fresh.common", "com.fresh.xy.common", "com.fresh.xy.redis", "com.fresh.xy.rmq", "com.fresh.xy.sample2.api"})
@EnableCircuitBreaker
@EnableHystrixDashboard
@EnableFeignClients({"com.fresh.xy.sample2.api"})
@EnableRetry
public class SampleApplication {

    public static void main(String argv[]) {
        SpringApplication.run(SampleApplication.class, argv);
    }
}
