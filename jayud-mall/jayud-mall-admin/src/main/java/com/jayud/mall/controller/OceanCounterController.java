package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.CounterDocumentInfoCounterIdForm;
import com.jayud.mall.model.bo.CounterDocumentInfoForm;
import com.jayud.mall.model.bo.CounterDocumentInfoIdForm;
import com.jayud.mall.model.bo.OceanCounterIdForm;
import com.jayud.mall.model.vo.CounterDocumentInfoVO;
import com.jayud.mall.model.vo.OceanCounterVO;
import com.jayud.mall.service.ICounterDocumentInfoService;
import com.jayud.mall.service.IOceanCounterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/oceancounter")
@Api(tags = "A039-admin-提单对应货柜信息接口")
@ApiSort(value = 39)
public class OceanCounterController {

    @Autowired
    IOceanCounterService oceanCounterService;
    @Autowired
    ICounterDocumentInfoService counterDocumentInfoService;

    //查询-(提单)柜子信息-包括费用
    @ApiOperation(value = "查询(提单)柜子信息-包括费用")
    @PostMapping(value = "/findOceanCounterById")
    @ApiOperationSupport(order = 1)
    public CommonResult<OceanCounterVO> findOceanCounterById(@Valid @RequestBody OceanCounterIdForm form){
        Long id = form.getId();
        OceanCounterVO oceanCounterVO = oceanCounterService.findOceanCounterById(id);
        return CommonResult.success(oceanCounterVO);
    }

    //添加-(提单)柜子对应文件信息
    @ApiOperation(value = "添加-(提单)柜子对应文件信息")
    @PostMapping(value = "/addCounterDocumentInfo")
    @ApiOperationSupport(order = 2)
    public CommonResult addCounterDocumentInfo(@Valid @RequestBody CounterDocumentInfoForm form){
        counterDocumentInfoService.addCounterDocumentInfo(form);
        return CommonResult.success("操作成功");
    }

    //删除-(提单)柜子对应文件信息
    @ApiOperation(value = "删除-(提单)柜子对应文件信息")
    @PostMapping(value = "/delCounterDocumentInfo")
    @ApiOperationSupport(order = 3)
    public CommonResult delCounterDocumentInfo(@Valid @RequestBody CounterDocumentInfoIdForm from){
        Long id = from.getId();
        counterDocumentInfoService.delCounterDocumentInfo(id);
        return CommonResult.success("操作成功");
    }

    //查询-(提单)柜子对应文件信息
    @ApiOperation(value = "查询-(提单)柜子对应文件信息")
    @PostMapping(value = "/findCounterDocumentInfoByCounterId")
    @ApiOperationSupport(order = 4)
    public CommonResult<List<CounterDocumentInfoVO>> findCounterDocumentInfoByCounterId(@Valid @RequestBody CounterDocumentInfoCounterIdForm form){
        Long counterId = form.getCounterId();
        List<CounterDocumentInfoVO> counterDocumentInfoList = counterDocumentInfoService.findCounterDocumentInfoByCounterId(counterId);
        return CommonResult.success(counterDocumentInfoList);
    }

    //根据id查询-(提单)柜子对应文件信息
    @ApiOperation(value = "根据id查询-(提单)柜子对应文件信息")
    @PostMapping(value = "/findCounterDocumentInfoById")
    @ApiOperationSupport(order = 5)
    public CommonResult<CounterDocumentInfoVO> findCounterDocumentInfoById(@Valid @RequestBody CounterDocumentInfoIdForm from){
        Long id = from.getId();
        CounterDocumentInfoVO counterDocumentInfoVO = counterDocumentInfoService.findCounterDocumentInfoById(id);
        return CommonResult.success(counterDocumentInfoVO);
    }



}
