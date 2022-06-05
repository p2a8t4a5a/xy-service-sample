package com.fresh.xy.sample.service.scan.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fresh.common.exception.BizException;
import com.fresh.common.result.JsonResult;
import com.fresh.xy.common.constants.RmqConstants;
import com.fresh.xy.mbp.utils.MybatisPlusPageUtils;
import com.fresh.xy.redis.config.FlCustomSerializer;
import com.fresh.xy.rmq.bo.RmqScanUpdateBo;
import com.fresh.xy.rmq.config.FlRmqProperties;
import com.fresh.xy.sample.dto.scan.SampleScanAddDto;
import com.fresh.xy.sample.dto.scan.SampleScanSelDto;
import com.fresh.xy.sample.entity.scan.SampleScan;
import com.fresh.xy.sample.mapper.scan.SampleScanMapper;
import com.fresh.xy.sample.rocketmq.tx.model.ScanRmqTxModel;
import com.fresh.xy.sample.service.scan.SampleScanService;
import com.fresh.xy.sample.vo.scan.SampleScanVo;
import com.fresh.xy.sample2.api.Sample2ServiceApi;
import com.fresh.xy.sample2.api.bo.Sample2ScanAddBo;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
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
    private FlRmqProperties flRmqProperties;

    @Autowired
    private FlCustomSerializer flCustomSerializer;

    @Autowired
    private DefaultMQProducer defaultMQProducer; //默认 Producer

    @Lazy
    @Autowired
    private DefaultMQProducer otherMQProducer;  //自定义 Producer

    @Autowired
    private TransactionMQProducer defaultTxMQProducer; //默认 tx Producer

    @Lazy
    @Autowired
    private TransactionMQProducer scanTxMQProducer;  //自定义 tx Producer



    @Override
    public IPage<SampleScanVo> listByPojo(SampleScanSelDto scanSelDto) {
        IPage<SampleScanVo> page = MybatisPlusPageUtils.mybatisPlusPage(scanSelDto);
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
        if (!result.getSuccess())
            throw new BizException(() -> "rpc 保存失败");

    }


    @Transactional
    @Override
    public void send(SampleScanAddDto scanDto) {
        SampleScan scan = SampleScan.builder().build().setName(scanDto.getName()).setScanType(scanDto.getScanType()).setScanTime(scanDto.getScanTime());
        this.save(scan);

        RmqScanUpdateBo bo = RmqScanUpdateBo.builder().id(scan.getId()).name(scan.getName() + "--to update msg by defaultMQProducer").scanType(scan.getScanType().getValue()).scanTime(scan.getScanTime()).build();
        Message msg = new Message(flRmqProperties.getTopics().get("com.fresh.xy.gateway.sample-topic").getName(), flRmqProperties.getTopics().get("sample-topic").getTags().get("scan-update").getName(), scan.getId()+"",
                flCustomSerializer.serializeAsBytes(bo));
        try {
            SendResult sendResult = defaultMQProducer.send(msg);

            //SendResult的处理
            //1、记录SendResult日志
            //2、当前工作模式是SYNC_MASTER,ASYNC_FLUSH,则如果收到SEND_OK,可以认为(99.99%)消息成功存储到broker了，如果是其他状态，则应该手动写代码添加相关重试逻辑(异步)
            //3、如果对发送端数据一致性要求高，可以使用事务性消息
            log.info("sendResult, {}", sendResult);
            if(sendResult.getSendStatus() != SendStatus.SEND_OK) {
                //retry
            }
        } catch (Exception e) { //如果发生异常，此处没有办法判断消息是否发送了，发送成功了，还是发送失败了
            //如果发送抛出异常,则应该手动写代码添加相关重试逻辑(异步)，"发送端不回滚"
            //如果对发送端数据一致性要求高，可以使用事务性消息
            log.error("send error, {}", e.getMessage());
        }
    }

    @Override
    public void sendCus() {
        System.out.println(defaultMQProducer);
        System.out.println(otherMQProducer);
        System.out.println(otherMQProducer.getProducerGroup());

        Message msg = null;
        try {
            msg = new Message(flRmqProperties.getTopics().get("sample-topic").getName(), flRmqProperties.getTopics().get("com.fresh.xy.gateway.sample-topic").getTags().get("other").getName(),
                    "Hello selfDefinedMQProducer".getBytes(RemotingHelper.DEFAULT_CHARSET));
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

    @Override
    public boolean txSend(SampleScanAddDto scanDto) {
        RmqScanUpdateBo bo = RmqScanUpdateBo.builder().name(scanDto.getName() + "--to update msg tx by defaultTxMQProducer").scanType(scanDto.getScanType().getValue()).scanTime(scanDto.getScanTime()).build();

        Message msg = new Message(flRmqProperties.getTopics().get("sample-tx-topic").getName(), flRmqProperties.getTopics().get("com.fresh.xy.gateway.sample-tx-topic").getTags().get("tx-scan-update").getName(),
                flCustomSerializer.serializeAsBytes(bo));

        //使用defaultTxMQProducer，必须实现RmqTxListenerService和ScanRmqTxModel
        msg.putUserProperty(RmqConstants.DEFAULT_RMQ_TX_SERVICE_IMPL, "scanTmqTxService");
        ScanRmqTxModel model = ScanRmqTxModel.builder().name(scanDto.getName()).scanTime(scanDto.getScanTime()).scanType(scanDto.getScanType()).build();
        try {
            //0.可能存在的问题: 返回SEND_OK,但是半消息还是失败了; 当前工作模式是SYNC_MASTER,ASYNC_FLUSH，除非master-slave整体挂掉才会丢失消息
            TransactionSendResult sendResult = defaultTxMQProducer.sendMessageInTransaction(msg, model);
            if(sendResult.getSendStatus() != SendStatus.SEND_OK) { //2、处理SendResult，如果不是SEND_OK,返回false，表示执行失败(因为只有SEND_OK状态，本地事务才会执行, 此时，半消息可能发送成功也可能发送失败(see 0.))
                return false;
            }
            if(sendResult.getLocalTransactionState() != LocalTransactionState.COMMIT_MESSAGE) {//3、本地事务执行的异常不会抛出来，此处用tx-state返回值标记本地事务是否执行成功
                //如果本地事务执行成功，返回COMMIT_MESSAGE
                //如果本地事务执行失败，返回UNKNOW
                return false;
            }
            //4、本地事务执行后，endTransaction执行，异常不会抛出来
            //5、事务回查: 事务回查运行在线程池中，异常不会抛出来
        } catch (MQClientException e) { //1、只有在发送半消息之前或者发送半消息时的异常才会抛出来，此时本地事务还未执行，半消息可能发送成功也可能发送失败，注意本地事务执行、事务执行之后的代码、事务回查的异常不会抛出来
            //如果发生异常，controller调用处捕获异常，返回给前端失败
            log.error(e.getMessage());
            throw new BizException(() -> e.getMessage());
        }
        return true;
    }

    @Override
    public boolean txSendCus(SampleScanAddDto scanDto) {
        RmqScanUpdateBo bo = RmqScanUpdateBo.builder().name(scanDto.getName() + "--to update msg tx by selfDefinedMQProducer").scanType(scanDto.getScanType().getValue()).scanTime(scanDto.getScanTime()).build();

        Message msg = new Message(flRmqProperties.getTopics().get("sample-tx-topic").getName(), flRmqProperties.getTopics().get("com.fresh.xy.gateway.sample-tx-topic").getTags().get("tx-scan-update").getName(),
                flCustomSerializer.serializeAsBytes(bo));

        try {
            TransactionSendResult sendResult = scanTxMQProducer.sendMessageInTransaction(msg, scanDto);
            if(sendResult.getSendStatus() != SendStatus.SEND_OK) {
                return false;
            }
            if(sendResult.getLocalTransactionState() != LocalTransactionState.COMMIT_MESSAGE) {
                return false;
            }
        } catch (MQClientException e) {
            log.error(e.getMessage());
            throw new BizException(() -> e.getMessage());
        }
        return true;
    }


}
