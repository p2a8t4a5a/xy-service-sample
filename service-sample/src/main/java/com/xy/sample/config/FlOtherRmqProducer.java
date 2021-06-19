package com.xy.sample.config;

import com.sc.common.rmq.config.FlRmqProducer;

@FlRmqProducer(producerName = "otherMQProducer", group = "sample-other-producer-group")
public class FlOtherRmqProducer {
    //do nothing
}
