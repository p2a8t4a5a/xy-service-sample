package com.fresh.xy.sample.retry;

import com.fresh.common.exception.BizException;
import com.fresh.xy.sample.entity.scan.SampleScan;
import com.fresh.xy.sample.dto.scan.SampleScanAddDto;
import com.fresh.xy.sample.service.scan.SampleScanService;
import com.fresh.xy.sample.dto.scan.SampleScanUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class FlRetrySample {
    private static ThreadLocal<Long> retryThreadLocal = new ThreadLocal<>();

    @Autowired
    private SampleScanService sampleScanService;

    @Retryable(include = {BizException.class},
            maxAttempts = 4,
            backoff = @Backoff(random = true, multiplier = 1.3),
            stateful = false   //stateful=false==>stateless(无状态的retry): retry, recover 发生在相同线程中，recover执行一次
    )
    public String basicRetryWhenBizException() {
        logRetry();
        throw new BizException(() -> "retry test");
        //return "retry";
    }

    @Recover
    public String basicRecoverWhenBizException(Throwable e) {
        logRecover();
        return "recover";
    }

    private void logRetry() {
        Long currentValue = retryThreadLocal.get();
        if(currentValue == null) currentValue = 0L;
        retryThreadLocal.set(++currentValue);
        log.info("retry thread: {}, retry times: {}", Thread.currentThread().getId(), currentValue);
    }
    private void logRecover() {
        Long currentValue = retryThreadLocal.get();
        log.info("recover thread: {}, recover时的times: {}", Thread.currentThread().getId(), currentValue);
    }


    @Retryable(include = {BizException.class},
            maxAttempts = 4,
            backoff = @Backoff(random = true, multiplier = 1.3),
            stateful = false //stateful=false==>stateless(无状态的retry): retry, recover 发生在相同线程中，recover执行一次
    )
    @Transactional
    public boolean retrySave(SampleScanAddDto scanAddDto) {
        logRetry();
        SampleScan scan = SampleScan.builder().name(scanAddDto.getName()).scanTime(scanAddDto.getScanTime()).scanType(scanAddDto.getScanType()).build();
        boolean result = sampleScanService.save(scan);
        //1.如果没有@Retryable, 此处抛出BizException，事务将回滚
        //2.加上@Retryable{stateful=false}, 每次retry，开启事务，并回滚，即retry在不同事务里面(通过事务日志可以看到)
        throw new BizException(() -> "retrySave exception");
        //return result;
    }

    @Recover
    public boolean retrySaveRecover(Throwable e) {
        logRecover();
        return false;
    }


    @Retryable(include = {BizException.class},
            maxAttempts = 4,
            backoff = @Backoff(random = true, multiplier = 1.3),
            stateful = false //stateful=false==>stateless(无状态的retry): retry, recover 发生在相同线程中，recover执行一次
    )
    @Transactional
    public boolean retryUpdate(SampleScanUpdateDto scanUpdateDto) {
        logRetry();
        SampleScan scan = SampleScan.builder().name(scanUpdateDto.getName()).scanTime(scanUpdateDto.getScanTime()).scanType(scanUpdateDto.getScanType()).build();
        scan.setId(scanUpdateDto.getId());
        boolean result = sampleScanService.updateById(scan);
        //1.如果没有@Retryable, 此处抛出BizException，事务将回滚
        //2.加上@Retryable{stateful=false}, 每次retry，开启事务，并回滚，即retry在不同事务里面(通过事务日志可以看到)
        throw new BizException(() -> "retryUpdate exception");

        //return result;
    }

    //TODO stateful=true is what mean


}
