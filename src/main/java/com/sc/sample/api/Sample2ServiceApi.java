package com.sc.sample.api;

import com.sc.common.bo.Sample2ScanAddBo;
import com.sc.common.bo.Sample2ScanBo;
import com.sc.common.bo.Sample2ScanPageBo;
import com.sc.common.enums.ScanTypeEnum;
import com.sc.common.vo.BasicJsonResult;
import com.sc.common.vo.JsonResult;
import com.sc.common.vo.PageJsonResultVo;
import com.sc.sample.api.fallback.Sample2ServiceApiFallbackFac;
import com.sc.sample.feign.FlFeignClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

//@FeignClient(name = "service-sample2", fallback = Sample2ServiceApiFallback.class)
@FeignClient(name = "service-sample2", fallbackFactory = Sample2ServiceApiFallbackFac.class, configuration = {FlFeignClientConfiguration.class})
public interface Sample2ServiceApi {

    @GetMapping("/sample2ScanRpc/getById")
    BasicJsonResult<Sample2ScanBo> rpcGetById(@RequestParam("id") Long id);

    @GetMapping("/sample2ScanRpc/getByIds")
    BasicJsonResult<List<Sample2ScanBo>> rpcGetByIds(@RequestParam(name = "ids", required = false) List<Long> ids);

    @GetMapping("/sample2ScanRpc/listByPojo") //pojo参数在Get请求中的参数转化 @see ReflectiveFeign.create(Object[] argv), 支持度不高(declaredField, toString)
    BasicJsonResult<PageJsonResultVo<Sample2ScanBo>> rpcListByPojo(@SpringQueryMap Sample2ScanPageBo scanPageBo);

    @GetMapping("/sample2ScanRpc/listByPojo2")//默认Feign not support Get RequestBody
    BasicJsonResult<PageJsonResultVo<Sample2ScanBo>> rpcListByPojo2(@RequestBody Sample2ScanPageBo scanPageBo);

    @GetMapping("/sample2ScanRpc/listByParams") //@RequestParam标记的参数在Get请求中的参数转化 @see ReflectiveFeign.create(Object[] argv)
    BasicJsonResult<PageJsonResultVo<Sample2ScanBo>> rpcListByParams(@RequestParam("id") Long id, @RequestParam("scanType") ScanTypeEnum scanType, @RequestParam("scanTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime scanTime, @RequestParam("page") Long page, @RequestParam("pageSize") Long pageSize);

    @PostMapping("/sample2ScanRpc/save")
    JsonResult rpcSave(@RequestBody Sample2ScanAddBo scanAddBo);

    //TODO hystrix配置特别是线程池配置使并发容量满足要求，feign配置，ribbon配置
}
