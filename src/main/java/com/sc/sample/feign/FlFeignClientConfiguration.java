package com.sc.sample.feign;

import feign.QueryMapEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class FlFeignClientConfiguration {

    /*没法覆盖默认的QueryMapEncoder
    @Bean
    public QueryMapEncoder flQueryMapEncoder() {
        return new FlQueryMapEncoder();
    }*/

}
