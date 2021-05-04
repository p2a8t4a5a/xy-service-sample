package com.sc.sample.controller.scan;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sc.common.dto.PageDto;
import com.sc.common.enums.JsonResultEnum;
import com.sc.common.utils.AssertUtils;
import com.sc.common.vo.JsonResult;
import com.sc.sample.dto.scan.ScanAddDto;
import com.sc.sample.dto.scan.ScanDelDto;
import com.sc.sample.dto.scan.ScanSelDto;
import com.sc.sample.dto.scan.ScanUpdateDto;
import com.sc.sample.entity.scan.Scan;
import com.sc.sample.service.scan.ScanService;
import com.sc.sample.vo.scan.ScanVo;
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

    @GetMapping("getByIds")
    public JsonResult getByIds(@RequestParam(name = "ids", required = false) List<Long> ids) {
        List<Scan> result = null;
        if(ids != null && !ids.isEmpty()) {
            result = scanService.list(new QueryWrapper<Scan>().lambda().select(Scan::getId, Scan::getName, Scan::getScanTime, Scan::getScanType).in(Scan::getId, ids));
        }
        return JsonResult.buildSuccessResult(result);
    }

    /**
     *
     * @param scanSelDto 如果未传PageDto.page或者PageDto.pageSize，取PageDto的默认值
     *                   如果PageDto.page<=0，最终的sql当作0，表示第一页
     *                   如果PageDto.pageSize<0, exception
     * @return
     */
    @GetMapping("listByPojo")
    public JsonResult listByPojo(ScanSelDto scanSelDto) {
        AssertUtils.ifNull(scanSelDto, () -> "查询参数不能为空", () -> JsonResultEnum.FAIL.getCode());
        AssertUtils.ifTrue(scanSelDto.getPageSize()<0, () -> "pageSize不能为负数", () -> JsonResultEnum.FAIL.getCode());

        /*
         * when ScanSelDto.id=null then sql: id = null
         * when ScanSelDto.name=null then sql: name like %null%
         * when ScanSelDto.scanType=null then sql: scan_type = null
         * when ScanSelDto.scanTime=null then sql: scan_time > null
         */
        /*IPage<Scan> result = scanService.page(scanSelDto.buildPage(Scan.class),
                new QueryWrapper<Scan>().lambda().select(Scan::getId, Scan::getName, Scan::getScanTime, Scan::getScanType)
                        .eq(Scan::getId, scanSelDto.getId())
                        .like(Scan::getName, scanSelDto.getName())
                        .eq(Scan::getScanType, scanSelDto.getScanType())
                        .gt(Scan::getScanTime, scanSelDto.getScanTime()));*/

        IPage<ScanVo> result = scanService.listByPojo(scanSelDto);

        return JsonResult.buildSuccessResult(result);
    }

    /**
     *
     * @param pageDto  如果未传PageDto.page或者PageDto.pageSize，取PageDto的默认值
     *                 如果PageDto.page<=0，最终的sql当作0，表示第一页
     *                 如果PageDto.pageSize<0, exception
     * @return
     */
    @GetMapping("list")
    public JsonResult list(PageDto pageDto) {
        AssertUtils.ifNull(pageDto, () -> "分页参数不能为空", () -> JsonResultEnum.FAIL.getCode());
        AssertUtils.ifTrue(pageDto.getPageSize()<0, () -> "pageSize不能为负数", () -> JsonResultEnum.FAIL.getCode());

        IPage<Scan> pageParam = pageDto.buildPage(Scan.class);
        IPage<Scan> result = scanService.page(pageParam);

        return JsonResult.buildSuccessResult(result);
    }

    @GetMapping("listAll")
    public JsonResult listAll() {
        List<Scan> result = scanService.list();

        return JsonResult.buildSuccessResult(result);
    }

}
