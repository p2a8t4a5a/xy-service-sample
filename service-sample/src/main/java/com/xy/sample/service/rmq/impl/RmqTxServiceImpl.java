package com.xy.sample.service.rmq.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sc.common.entity.rmq.RmqTx;
import com.xy.sample.mapper.rmq.RmqTxMapper;
import com.xy.sample.service.rmq.RmqTxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RmqTxServiceImpl extends ServiceImpl<RmqTxMapper, RmqTx> implements RmqTxService {

}
