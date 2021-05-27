package com.sc.sample.controller.scan;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sc.common.bo.scan.Sample2ScanBo;
import com.sc.common.bo.scan.Sample2ScanPageBo;
import com.sc.common.dto.PageDto;
import com.sc.common.enums.JsonResultEnum;
import com.sc.common.enums.ScanTypeEnum;
import com.sc.common.utils.AssertUtils;
import com.sc.common.vo.JsonResult;
import com.sc.common.vo.PageJsonResultVo;
import com.sc.sample.api.Sample2ServiceApi;
import com.sc.sample.dto.scan.SampleScanAddDto;
import com.sc.sample.dto.scan.SampleScanDelDto;
import com.sc.sample.dto.scan.SampleScanSelDto;
import com.sc.sample.dto.scan.SampleScanUpdateDto;
import com.sc.sample.entity.scan.SampleScan;
import com.sc.sample.service.scan.SampleScanService;
import com.sc.sample.vo.scan.SampleScanVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/sampleScan/")
public class SampleScanController {
    
    @Autowired
    private SampleScanService sampleScanService;
    @Autowired
    private Sample2ServiceApi sample2ServiceApi;

    @PostMapping("save")
    public JsonResult save(@RequestBody @Valid SampleScanAddDto scanDto) {
        SampleScan scan = SampleScan.builder().build().setName(scanDto.getName()).setScanType(scanDto.getScanType()).setScanTime(scanDto.getScanTime());
        sampleScanService.save(scan);
        return JsonResult.buildSuccessResult("保存成功");
    }

    @PostMapping("del")
    public JsonResult del(@RequestBody @Valid SampleScanDelDto scan) {
        sampleScanService.removeById(scan.getId());
        return JsonResult.buildSuccessResult("删除成功");
    }

    @PostMapping("update")
    public JsonResult update(@RequestBody @Valid SampleScanUpdateDto scanDto) {
        SampleScan scan = SampleScan.builder().build().setName(scanDto.getName()).setScanType(scanDto.getScanType()).setScanTime(scanDto.getScanTime());
        scan.setId(scanDto.getId());
        sampleScanService.updateById(scan);
        return JsonResult.buildSuccessResult("修改成功");
    }

    @GetMapping("getById")
    public JsonResult getById(Long id) {
        //AssertUtils.ifNull(id, () -> "id不能为空", () -> JsonResultEnum.FAIL.getCode());
        SampleScan scan = sampleScanService.getById(id);
        SampleScanVo scanVo = null;
        if(scan != null) {
            scanVo = SampleScanVo.builder().id(scan.getId()).name(scan.getName()).scanType(scan.getScanType()).scanTime(scan.getScanTime()).createTime(scan.getCreateTime()).modifyTime(scan.getModifyTime()).build();
        }
        JsonResult<SampleScanVo> result = JsonResult.buildSuccessResult(scanVo);

        return result;
    }

    @GetMapping("getByIds")
    public JsonResult getByIds(@RequestParam(name = "ids", required = false) List<Long> ids) {
        List<SampleScanVo> result = null;
        if(ids != null && !ids.isEmpty()) {
            List<SampleScan> scanResult = sampleScanService.list(new QueryWrapper<SampleScan>().lambda().select(SampleScan::getId, SampleScan::getName, SampleScan::getScanTime, SampleScan::getScanType).in(SampleScan::getId, ids));
            result = scanResult.stream().map(scan -> SampleScanVo.builder().id(scan.getId()).name(scan.getName()).scanType(scan.getScanType()).scanTime(scan.getScanTime()).createTime(scan.getCreateTime()).modifyTime(scan.getModifyTime()).build()).collect(Collectors.toList());
        }
        return JsonResult.buildSuccessResult(result);
    }

    //compare with listByPojo
    @GetMapping("listByParams")
    public JsonResult listByParams(@RequestParam(name = "id", required = false) Long id,
                                   @RequestParam(name = "scanType", required = false) ScanTypeEnum scanType,  //String convert to Enum, 使用默认的StringCovertToEnum
                                   @RequestParam(name = "scanTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime scanTime, //String covert to LocalDateTime，String转化为LocalDateTime，默认的String格式是iso格式
                                   @RequestParam(name = "page", required = false) Long page,
                                   @RequestParam(name = "pageSize", required = false) Long pageSize) {

        SampleScanSelDto scanSelDto = SampleScanSelDto.builder().id(id).scanType(scanType).scanTime(scanTime).build();
        scanSelDto.setPage(page);
        scanSelDto.setPageSize(pageSize);
        IPage<SampleScanVo> result = sampleScanService.listByPojo(scanSelDto);

        return JsonResult.buildSuccessResult(result);
    }

    /**
     * Get请求的Enum中@JsonCreator
     * @param scanSelDto 如果未传PageDto.page或者PageDto.pageSize，取PageDto的默认值
     *                   如果PageDto.page<=0，最终的sql当作0，表示第一页
     *                   如果PageDto.pageSize<0, exception
     * @return
     */
    @GetMapping("listByPojo")
    public JsonResult listByPojo(SampleScanSelDto scanSelDto) {
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

        IPage<SampleScanVo> result = sampleScanService.listByPojo(scanSelDto);

        return JsonResult.buildSuccessResult(result);
    }

    /**
     * compare with listByPojo, Get请求将参数放在请求体，使用@RequestBody解析请求体参数
     * @param scanSelDto
     * @return
     */
    @GetMapping("listByPojo2")
    public JsonResult listByPojo2(@RequestBody SampleScanSelDto scanSelDto) {
        AssertUtils.ifNull(scanSelDto, () -> "查询参数不能为空", () -> JsonResultEnum.FAIL.getCode());
        AssertUtils.ifTrue(scanSelDto.getPageSize()<0, () -> "pageSize不能为负数", () -> JsonResultEnum.FAIL.getCode());
        IPage<SampleScanVo> result = sampleScanService.listByPojo(scanSelDto);

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

        IPage<SampleScan> pageParam = pageDto.buildPage(SampleScan.class);
        IPage<SampleScan> result = sampleScanService.page(pageParam);

        return JsonResult.buildSuccessResult(result);
    }

    @GetMapping("listAll")
    public JsonResult listAll() {
        List<SampleScan> scanResult = sampleScanService.list();
        List<SampleScanVo> result = scanResult.stream().map(scan -> SampleScanVo.builder().id(scan.getId()).name(scan.getName()).scanType(scan.getScanType()).scanTime(scan.getScanTime()).createTime(scan.getCreateTime()).modifyTime(scan.getModifyTime()).build()).collect(Collectors.toList());

        return JsonResult.buildSuccessResult(result);
    }

    //rpc from service-sample2
    @GetMapping("getByIdRpc")
    public JsonResult getByIdRpc(Long id) {
        JsonResult<Sample2ScanBo> result = sample2ServiceApi.rpcGetById(id);
        return result;
    }
    @GetMapping("getByIdsRpc")
    public JsonResult getByIdsRpc(@RequestParam(name = "ids", required = false) List<Long> ids) {
        JsonResult<List<Sample2ScanBo>> result = sample2ServiceApi.rpcGetByIds(ids);
        return result;
    }

    //Get请求的Enum中@JsonCreator
    @GetMapping("listByPojoRpc")
    public JsonResult listByPojoRpc(SampleScanSelDto scanSelDto) {
        AssertUtils.ifNull(scanSelDto, () -> "查询参数不能为空", () -> JsonResultEnum.FAIL.getCode());
        AssertUtils.ifTrue(scanSelDto.getPageSize()<0, () -> "pageSize不能为负数", () -> JsonResultEnum.FAIL.getCode());

        Sample2ScanPageBo scanPageBo = Sample2ScanPageBo.builder().id(scanSelDto.getId()).name(scanSelDto.getName()).scanType(scanSelDto.getScanType()).scanTime(scanSelDto.getScanTime()).build();
        JsonResult<PageJsonResultVo<Sample2ScanBo>> result = sample2ServiceApi.rpcListByPojo(scanPageBo);
        return result;
    }

    //Get请求的Enum中@JsonCreator
    @GetMapping("listByParamsRpc")
    public JsonResult listByParamsRpc(@RequestParam(name = "id", required = false) Long id,
                                   @RequestParam(name = "scanType", required = false) ScanTypeEnum scanType,  //String convert to Enum, 使用默认的StringCovertToEnum
                                   @RequestParam(name = "scanTime", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime scanTime, //String covert to LocalDateTime，String转化为LocalDateTime，默认的String格式是iso格式
                                   @RequestParam(name = "page", required = false) Long page,
                                   @RequestParam(name = "pageSize", required = false) Long pageSize) {

        JsonResult<PageJsonResultVo<Sample2ScanBo>> result = sample2ServiceApi.rpcListByParams(id, scanType, scanTime, page, pageSize);
        return result;
    }


    //compare with listByPojo, Get请求将参数放在请求体，使用@RequestBody解析请求体参数
    @GetMapping("listByPojo2Rpc")
    public JsonResult listByPojo2Rpc(@RequestBody Sample2ScanPageBo scanPageBo) {
        AssertUtils.ifNull(scanPageBo, () -> "查询参数不能为空", () -> JsonResultEnum.FAIL.getCode());
        AssertUtils.ifTrue(scanPageBo.getPageSize()<0, () -> "pageSize不能为负数", () -> JsonResultEnum.FAIL.getCode());

        JsonResult<PageJsonResultVo<Sample2ScanBo>> result = sample2ServiceApi.rpcListByPojo2(scanPageBo);
        return result;
    }

    @PostMapping("saveRpc")
    public JsonResult saveRpc(@RequestBody @Valid SampleScanAddDto scanAddDto) {
        try {
            sampleScanService.saveAndRpc(scanAddDto);
        } catch(Exception e) {
            log.error(e.getMessage());
            return JsonResult.buildFailedResult("保存失败");
        }
        return JsonResult.buildSuccessResult("保存成功");
    }

}
