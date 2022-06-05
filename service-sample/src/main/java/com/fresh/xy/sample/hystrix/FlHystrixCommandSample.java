package com.fresh.xy.sample.hystrix;

import com.fresh.common.result.JsonResult;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;

/**
 * commandKey属性指定HystrixCommand的唯一ID,默认取方法名称
 * threadPoolKey属性表示线程池的标记，默认类名称(一个类创建一个线程池?)
 * fallbackMethod属性指定fallback
 * commandProperties属性指定Hystrix配置
 *  eg: commandProperties = {
 *             @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD")}
 *  the same as application.yml:
 *    hystrix:
 *      command:
 *        HystrixCommandKey:  ##HystrixCommand的CommandKey
 *          execution:
 *            isolation:
 *              strategy: THREAD
 *     hystrix:
 *       command:
 *         default:   ##使用default,对所有HystrixCommand有效
 *           ...
 * threadPoolProperties属性指定Hystrix线程池配置
 */
@Component
@Slf4j
public class FlHystrixCommandSample {

    //sync
    /*
        调用者线程
           |调用HystrixCommand方法，同步wait
           |在调用者线程中执行HystrixCommand
           |调用者线程获得HystrixCommand方法返回

        调用者线程                                线程池
           |调用HystrixCommand方法,同步wait
           |wait                                |在线程池中执行HystrixCommand
           |调用者线程获得HystrixCommand方法返回
     */
    @HystrixCommand(fallbackMethod = "surroundByHystrixFallback",
        commandProperties = {
            //it executes on a separate thread and concurrent requests are limited by the number of threads in the thread-pool
            //使用线程池或者信号量
            //线程池: 每一个HystrixCommand创建一个线程池; 优点: 资源隔离，调用者线程和线程池可以做到并发/并行执行; 缺点: 线程池资源有限
            //信号量: 在调用者线程中执行,信号量定义了一个数值表示最大资源数: execution.isolation.semaphore.maxConcurrentRequests
            //       优点: 最大资源数不受限制; 缺点: HystrixCommand在调用者线程中同步执行
            @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD"),
            //if so, Hystrix marks the HystrixCommand as a TIMEOUT, and performs com.fresh.xy.sample2.api.fallback logic
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000"),

            //设置最小请求数量，当在一个滚动的窗口中失败数量>=该值时，打开断路器开关
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "20"),
            //设置一个百分比，当在一个滚动的窗口中失败率>=该值时，打开断路器开关
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
            //设置时间值，表示断路器开关打开多少时间后，将断路器开关变成半开状态
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000"),
            //circuitBreaker.forceOpen=true: 强制打开断路器开关，并拒绝所有请求
            //circuitBreaker.forceClosed=false: 迫使断路器进入闭合状态，其中它将允许请求，而不考虑错误百分比
            //设置滚动窗口的持续时间
            @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "10000"),
            //?
            //@HystrixProperty(name = "metrics.rollingPercentile.timeInMilliseconds", value = "6000"),

            //设置允许fallback的调用者线程的数量,es: 线程池/信号量=10, 调用者线程=100, com.fresh.xy.sample2.api.fallback limit=20
                //                             10个调用者线程执行, 剩下的90个调用者线程中20个fallback，70个抛异常
            @HystrixProperty(name = "com.fresh.xy.sample2.api.fallback.isolation.semaphore.maxConcurrentRequests", value = "10")
    },  threadPoolProperties = {
            @HystrixProperty(name = "coreSize", value = "10"),
            //@HystrixProperty(name = "allowMaximumSizeToDivergeFromCoreSize", value = "true"),
            //@HystrixProperty(name = "maximumSize", value = "20"),
            @HystrixProperty(name = "maxQueueSize", value = "-1"),
            @HystrixProperty(name = "queueSizeRejectionThreshold", value = "1000"),
            @HystrixProperty(name = "keepAliveTimeMinutes", value = "1")
    }
    )
    public JsonResult surroundByHystrix(Long id) {
        log.info("surroundByHystrix: " + id);
        int i = 1/0;
        return JsonResult.buildSuccessResult("surroundByHystrix");
    }

    public JsonResult surroundByHystrixFallback(Long id, Throwable e) {
        log.info("surroundByHystrixFallback: " + id);
        log.error(e.getMessage());
        return JsonResult.buildFailedResult("surroundByHystrixFallback");
    }

    //Async
    /*
    调用者线程                                  线程池
           |调用HystrixCommand方法,获得Future
           |do other things                   |在线程池中执行HystrixCommand  (调用者线程和线程池中线程并发/并行执行)
           |调用者线程通过Future获得结果
     */
    @HystrixCommand(fallbackMethod = "surroundByHystrixFallbackAsync")
    public Future<JsonResult> surroundByHystrixAsync(Long id) {
        log.info("surroundByHystrixAsync: " + id);
        return new AsyncResult<JsonResult>() {
            @Override
            public JsonResult invoke() {
                int i = 1/0;
                return JsonResult.buildSuccessResult("surroundByHystrixAsync");
            }
        };
    }
    @HystrixCommand
    public Future<JsonResult> surroundByHystrixFallbackAsync(Long id, Throwable e) {
        log.info("surroundByHystrixFallbackAsync: " + id);
        log.error(e.getMessage());
        return new AsyncResult<JsonResult>() {
            @Override
            public JsonResult invoke() {
                return JsonResult.buildSuccessResult("surroundByHystrixFallbackAsync");
            }
        };
    }

}
