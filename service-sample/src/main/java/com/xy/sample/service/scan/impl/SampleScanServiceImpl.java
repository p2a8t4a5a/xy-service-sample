package com.xy.sample.service.scan.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sc.common.exception.BizException;
import com.sc.common.redis.FlCustomSerializer;
import com.sc.common.rmq.bo.RmqScanUpdateBo;
import com.sc.common.rmq.config.FlRmqProperties;
import com.sc.common.vo.JsonResult;
import com.xy.sample.mapper.scan.SampleScanMapper;
import com.xy.sample.dto.scan.SampleScanAddDto;
import com.xy.sample.dto.scan.SampleScanSelDto;
import com.xy.sample.entity.scan.SampleScan;
import com.xy.sample.service.scan.SampleScanService;
import com.xy.sample.vo.scan.SampleScanVo;
import com.xy.sample2.api.Sample2ServiceApi;
import com.xy.sample2.api.bo.Sample2ScanAddBo;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.UnsupportedEncodingException;

@Slf4j
@Service
public class SampleScanServiceImpl extends ServiceImpl<SampleScanMapper, SampleScan> implements SampleScanService {

    @Autowired
    private SampleScanMapper sampleScanMapper;

    @Autowired
    private Sample2ServiceApi sample2ServiceApi;

    @Autowired
    private DefaultMQProducer defaultMQProducer;

    @Lazy
    @Autowired
    private DefaultMQProducer otherMQProducer;

    @Autowired
    private FlCustomSerializer flCustomSerializer;

    @Autowired
    private FlRmqProperties flRmqProperties;

    @Lazy
    @Autowired
    private TransactionMQProducer scanTxMQProducer;



    @Override
    public IPage<SampleScanVo> listByPojo(SampleScanSelDto scanSelDto) {
        IPage<SampleScanVo> page = scanSelDto.buildPage(SampleScanVo.class);
        sampleScanMapper.selectByPojo(page, scanSelDto);
        return page;
    }

    @Transactional
    @Override
    public void saveAndRpc(SampleScanAddDto scanAddDto) {
        SampleScan sampleScan = SampleScan.builder().name(scanAddDto.getName()).scanType(scanAddDto.getScanType()).scanTime(scanAddDto.getScanTime()).build();
        this.save(sampleScan);
        Sample2ScanAddBo scanAddBo = Sample2ScanAddBo.builder().name(scanAddDto.getName()).scanType(scanAddDto.getScanType()).scanTime(scanAddDto.getScanTime()).build();
        JsonResult result = sample2ServiceApi.save(scanAddBo);
        if(!result.getSuccess())
            throw new BizException(() -> "rpc 保存失败");

    }

    @Transactional
    @Override
    public void sampleTopicUpdate(SampleScanAddDto scanDto) {
        SampleScan scan = SampleScan.builder().build().setName(scanDto.getName()).setScanType(scanDto.getScanType()).setScanTime(scanDto.getScanTime());
        this.save(scan);

        RmqScanUpdateBo bo = RmqScanUpdateBo.builder().id(scan.getId()).name(scan.getName() + "--to update msg").scanType(scan.getScanType().getValue()).scanTime(scan.getScanTime()).build();
        Message msg = new Message(flRmqProperties.getTopics().get("sample-topic").getName(), flRmqProperties.getTopics().get("sample-topic").getTags().get("scan-update").getName(), scan.getId()+"",
                flCustomSerializer.serializeAsBytes(bo));
        try {
            SendResult sendResult = defaultMQProducer.send(msg);

            //SendResult的处理
            //1、记录SendResult日志
            //2、当前工作模式时SYNC_MASTER,则如果收到SEND_OK,可以认为(99.99%)消息成功存储到broker了，如果是其他状态，则应该手动写代码添加相关重试逻辑(异步)
            //3、如果对发送端数据一致性要求高，可以使用事务性消息
            log.info("sendResult, {}", sendResult);
            if(sendResult.getSendStatus() != SendStatus.SEND_OK) {
                //retry
            }
        } catch (Exception e) {
            log.error("send error, {}", e.getMessage());
            //如果发送抛出异常,则应该手动写代码添加相关重试逻辑(异步)，"发送端不回滚",考虑两种情况1:消息真的发送失败；2:消息发送假失败
            //如果对发送端数据一致性要求高，可以使用事务性消息
        }
    }

    @Override
    public void txProducer(SampleScanAddDto scanDto) {
        RmqScanUpdateBo bo = RmqScanUpdateBo.builder().name(scanDto.getName() + "--to update msg tx").scanType(scanDto.getScanType().getValue()).scanTime(scanDto.getScanTime()).build();

        Message msg = new Message(flRmqProperties.getTopics().get("sample-tx-topic").getName(), flRmqProperties.getTopics().get("sample-tx-topic").getTags().get("tx-scan-update").getName(), /*scan.getId()+"",*/
                flCustomSerializer.serializeAsBytes(bo));

        try {
            scanTxMQProducer.sendMessageInTransaction(msg, scanDto);
            //事务性消息的sendResult不用处理
        } catch (Exception e) {
            //do nothing
        }
    }



    @Override
    public void other() {
        System.out.println(defaultMQProducer);
        System.out.println(otherMQProducer);
        System.out.println(otherMQProducer.getProducerGroup());

        Message msg = null;
        try {
            msg = new Message(flRmqProperties.getTopics().get("sample-topic").getName(), flRmqProperties.getTopics().get("sample-topic").getTags().get("other").getName(),
                    "Hello otherMQProducer".getBytes(RemotingHelper.DEFAULT_CHARSET));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return ;
        }
        try {
            SendResult sendResult = otherMQProducer.send(msg);
            log.info("sendResult, {}", sendResult);
            if(sendResult.getSendStatus() != SendStatus.SEND_OK) {
                //retry逻辑
            }
        } catch (Exception e) {
            log.error("send error, {}", e.getMessage());
            //retry逻辑
        }
    }


}
