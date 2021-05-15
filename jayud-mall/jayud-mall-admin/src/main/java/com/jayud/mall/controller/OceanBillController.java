package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.*;
import com.jayud.mall.model.vo.BillCostInfoVO;
import com.jayud.mall.model.vo.BillTaskRelevanceVO;
import com.jayud.mall.model.vo.OceanBillVO;
import com.jayud.mall.service.IOceanBillService;
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
@RequestMapping("/oceanbill")
@Api(tags = "A038-admin-提单接口")
@ApiSort(value = 38)
public class OceanBillController {

    @Autowired
    IOceanBillService oceanBillService;

    @ApiOperation(value = "分页查询提单信息")
    @PostMapping("/findOceanBillByPage")
    @ApiOperationSupport(order = 1)
    public CommonResult<CommonPageResult<OceanBillVO>> findOceanBillByPage(@RequestBody QueryOceanBillForm form) {
        IPage<OceanBillVO> pageList = oceanBillService.findOceanBillByPage(form);
        CommonPageResult<OceanBillVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "保存提单信息")
    @PostMapping("/saveOceanBill")
    @ApiOperationSupport(order = 2)
    public CommonResult<OceanBillVO> saveOceanBill(@Valid @RequestBody OceanBillForm form){
        return oceanBillService.saveOceanBill(form);
    }

    @ApiOperation(value = "查看提单详情")
    @PostMapping(value = "lookOceanBill")
    @ApiOperationSupport(order = 3)
    public CommonResult<OceanBillVO> lookOceanBill(@Valid @RequestBody OceanBillParaForm form){
        Long id = form.getId();
        return oceanBillService.lookOceanBill(id);
    }

    //提单-录入费用(根据 提单id 查询)
    @ApiOperation(value = "提单-录入费用(根据 提单id 查询)")
    @PostMapping(value = "billLadingCost")
    @ApiOperationSupport(order = 4)
    public CommonResult<OceanBillVO> billLadingCost(@Valid @RequestBody OceanBillParaForm form){
        Long id = form.getId();
        return oceanBillService.billLadingCost(id);
    }

    //保存提单费用信息(录入提单费用保存)
    @ApiOperation(value = "保存提单费用信息(录入提单费用保存)")
    @PostMapping(value = "saveBillCostInfo")
    @ApiOperationSupport(order = 5)
    public CommonResult<BillCostInfoVO> saveBillCostInfo(@Valid @RequestBody BillCostInfoForm form){
        return oceanBillService.saveBillCostInfo(form);
    }

    //根据计费重,一键均摊
    @ApiOperation(value = "一键均摊提单费用到订单(根据订单计费重,均摊)")
    @PostMapping(value = "shareEqually")
    @ApiOperationSupport(order = 6)
    public CommonResult<OceanBillVO> shareEqually(@Valid @RequestBody BillCostInfoForm form){
        return oceanBillService.shareEqually(form);
    }


    //提单任务-反馈状态(根据 提单id 查询)
    @ApiOperation(value = "提单任务-反馈状态(根据 提单id 查询)")
    @PostMapping("/lookOceanBillTask")
    @ApiOperationSupport(order = 7)
    public CommonResult<OceanBillVO> lookOceanBillTask(@Valid @RequestBody OceanBillParaForm form){
        Long obId = form.getId();
        return oceanBillService.lookOceanBillTask(obId);
    }

    //提单任务-反馈状态(点击已完成)
    @ApiOperation(value = "提单任务-反馈状态(点击已完成)")
    @PostMapping("/confirmCompleted")
    @ApiOperationSupport(order = 8)
    public CommonResult<BillTaskRelevanceVO> confirmCompleted(@Valid @RequestBody BillTaskRelevanceParaForm form){
        Long id = form.getId();
        return oceanBillService.confirmCompleted(id);
    }

    //提单任务-订单操作日志（根据订单id查看）
    @ApiOperation(value = "运单任务-订单操作日志（根据订单id查看）")
    @PostMapping("/lookOperateLog")
    @ApiOperationSupport(order = 9)
    public CommonResult<List<BillTaskRelevanceVO>> lookOperateLog(@Valid @RequestBody OrderInfoParaForm form){
        Long id = form.getId();
        return oceanBillService.lookOperateLog(id);
    }


}