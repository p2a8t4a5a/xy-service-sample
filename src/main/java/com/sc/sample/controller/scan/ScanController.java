package com.sc.sample.controller.scan;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sc.common.dto.PageDto;
import com.sc.common.enums.JsonResultEnum;
import com.sc.common.utils.AssertUtils;
import com.sc.common.vo.JsonResult;
import com.sc.sample.dto.scan.ScanAddDto;
import com.sc.sample.dto.scan.ScanDelDto;
import com.sc.sample.dto.scan.ScanUpdateDto;
import com.sc.sample.entity.scan.Scan;
import com.sc.sample.service.scan.ScanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/scan/")
public class ScanController {
    
    @Autowired
    private ScanService scanService;

    @PostMapping("save")
    public JsonResult save(@RequestBody @Valid ScanAddDto scanDto) {
        Scan scan = Scan.builder().build().setName(scanDto.getName()).setScanType(scanDto.getScanType()).setScanTime(scanDto.getScanTime());
        scanService.save(scan);
        return JsonResult.buildSuccessResult("保存成功");
    }

    @PostMapping("del")
    public JsonResult del(@RequestBody @Valid ScanDelDto scan) {
        scanService.removeById(scan.getId());
        return JsonResult.buildSuccessResult("删除成功");
    }

    @PostMapping("update")
    public JsonResult update(@RequestBody @Valid ScanUpdateDto scanDto) {
        Scan scan = Scan.builder().build().setName(scanDto.getName()).setScanType(scanDto.getScanType()).setScanTime(scanDto.getScanTime());
        scan.setId(scanDto.getId());
        scanService.updateById(scan);
        return JsonResult.buildSuccessResult("修改成功");
    }

    @GetMapping("get")
    public JsonResult getById(Long id) {
        //AssertUtils.ifNull(id, () -> "id不能为空", () -> JsonResultEnum.FAIL.getCode());
        Scan scan = scanService.getById(id);
        return JsonResult.buildSuccessResult(scan);
    }

    @GetMapping("list")
    public JsonResult list(PageDto pageDto) {
        AssertUtils.ifNull(pageDto, () -> "分页参数不能为空", () -> JsonResultEnum.FAIL.getCode());
        AssertUtils.ifTrue(pageDto.getPageSize()<0, () -> "pageSize不能为负数", () -> JsonResultEnum.FAIL.getCode());
        Page<Scan> pageParam = new Page<>();
        pageParam.setCurrent(pageDto.getPage());
        pageParam.setSize(pageDto.getPageSize());
        IPage<Scan> result = scanService.page(pageParam);

        return JsonResult.buildSuccessResult(result);
    }

    @GetMapping("listAll")
    public JsonResult listAll() {
        List<Scan> result = scanService.list();

        return JsonResult.buildSuccessResult(result);
    }
    
}
