package com.xy.sample.ribbon.controller;

import com.sc.common.vo.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ribbon")
public class RibbonController {

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @GetMapping("client")
    public JsonResult ribbonClient(Long id) {
        ServiceInstance serverInstance = loadBalancerClient.choose("flRibbonClientSample");
        return JsonResult.buildSuccessResult(serverInstance);
    }


}
