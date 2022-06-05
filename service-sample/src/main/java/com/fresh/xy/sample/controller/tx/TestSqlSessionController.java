package com.fresh.xy.sample.controller.tx;


import com.fresh.common.result.JsonResult;
import com.fresh.xy.sample.service.tx.SscService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("sscTest")
public class TestSqlSessionController {

    @Autowired
    private SscService sscService;

    @GetMapping("nonTransactional")
    public JsonResult nonTransactional() {
        sscService.nonTransactional();
        return JsonResult.buildSuccessResult("true");
    }

    @GetMapping("transactional")
    public JsonResult transactional() {
        sscService.transactional();
        return JsonResult.buildSuccessResult("true");
    }

}
