package com.fresh.xy.sample.rocketmq.listener;

import com.fresh.xy.common.enums.ScanTypeEnum;
import com.fresh.xy.redis.config.FlCustomSerializer;
import com.fresh.xy.rmq.bo.RmqScanUpdateBo;
import com.fresh.xy.rmq.config.FlRmqConsumer;
import com.fresh.xy.sample.entity.scan.SampleScan;
import com.fresh.xy.sample.service.scan.SampleScanService;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@FlRmqConsumer(consumerName = "sampleTopicScanUpdateMQConsumer", group = "sample-topic-scan-update-consumer-group",
        topic = "${cus-rocketmq.topics.sample-topic.name}",
        tagExp = "${cus-rocketmq.topics.sample-topic.tags.scan-update.name}")
public class ScanUpdateMessageListener implements MessageListenerConcurrently {
    private Logger log = LoggerFactory.getLogger(ScanUpdateMessageListener.class);

    @Autowired
    private FlCustomSerializer flCustomSerializer;

    @Autowired
    private SampleScanService sampleScanService;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        try {
            list.forEach(msg -> {
                //update是幂等的
                RmqScanUpdateBo bo = flCustomSerializer.deserialize(msg.getBody(), RmqScanUpdateBo.class);
                SampleScan scan = SampleScan.builder().name(bo.getName()).scanType(ScanTypeEnum.getByValue(bo.getScanType())).scanTime(bo.getScanTime()).build();
                scan.setId(bo.getId());
                sampleScanService.updateById(scan);
            });
        } catch (Exception e) {
            log.error("consume message {} ,  error {}", list, e.getMessage());
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        log.info("consume message {}, success", list);
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

}
