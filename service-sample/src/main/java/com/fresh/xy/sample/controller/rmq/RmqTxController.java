package com.fresh.xy.sample.controller.rmq;

import com.fresh.common.result.JsonResult;
import com.fresh.xy.rmq.tx.entity.RmqTx;
import com.fresh.xy.rmq.tx.service.RmqTxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("rmqTx")
public class RmqTxController {

    @Autowired
    private RmqTxService rmqTxService;

    @GetMapping("list")
    public JsonResult list() {
        List<RmqTx> result = rmqTxService.list();
        return JsonResult.buildSuccessResult(result);
    }

}
