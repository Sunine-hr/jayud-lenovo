package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.*;
import com.jayud.mall.model.vo.*;
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


    //TODO 提单的接口

    @ApiOperation(value = "保存提单信息2(在配载添加提单，默认关联配载)")
    @PostMapping("/saveOceanBillByConf")
    @ApiOperationSupport(order = 10)
    public CommonResult<OceanBillVO> saveOceanBillByConf(@Valid @RequestBody OceanBillForm form){
        return oceanBillService.saveOceanBillByConf(form);
    }

    // 查询
    @ApiOperation(value = "配载，提单（4个窗口），查看-清关")
    @PostMapping("/findBillClearanceInfoByBillId")
    @ApiOperationSupport(order = 11)
    public CommonResult<List<BillClearanceInfoVO>> findBillClearanceInfoByBillId(@Valid @RequestBody OceanBillParaForm form){
        Long billId = form.getId();
        List<BillClearanceInfoVO> billClearanceInfoVOS = oceanBillService.findBillClearanceInfoByBillId(billId);
        return CommonResult.success(billClearanceInfoVOS);
    }

    @ApiOperation(value = "配载，提单（4个窗口），查看-报关")
    @PostMapping("/findBillCustomsInfoByBillId")
    @ApiOperationSupport(order = 12)
    public CommonResult<List<BillCustomsInfoVO>> findBillCustomsInfoByBillId(@Valid @RequestBody OceanBillParaForm form){
        Long billId = form.getId();
        List<BillCustomsInfoVO> billCustomsInfoVOS = oceanBillService.findBillCustomsInfoByBillId(billId);
        return CommonResult.success(billCustomsInfoVOS);
    }

    @ApiOperation(value = "配载，提单（4个窗口），查看-柜子")
    @PostMapping("/findOceanCounterByObId")
    @ApiOperationSupport(order = 13)
    public CommonResult<List<OceanCounterVO>> findOceanCounterByObId(@Valid @RequestBody OceanBillParaForm form){
        Long obId = form.getId();
        List<OceanCounterVO> oceanCounterVOS = oceanBillService.findOceanCounterByObId(obId);
        return CommonResult.success(oceanCounterVOS);
    }

    @ApiOperation(value = "配载，提单（4个窗口），查看-详情")
    @PostMapping("/findOceanBillById")
    @ApiOperationSupport(order = 14)
    public CommonResult<OceanBillVO> findOceanBillById(@Valid @RequestBody OceanBillParaForm form){
        Long id = form.getId();
        return oceanBillService.findOceanBillById(id);
    }

    //添加修改

    @ApiOperation(value = "添加修改-提单清关信息")
    @PostMapping("/saveBillClearanceInfo")
    @ApiOperationSupport(order = 15)
    public CommonResult<BillClearanceInfoVO> saveBillClearanceInfo(@Valid @RequestBody BillClearanceInfoForm form){
        BillClearanceInfoVO billClearanceInfoVO = oceanBillService.saveBillClearanceInfo(form);
        return CommonResult.success(billClearanceInfoVO);
    }

    @ApiOperation(value = "添加修改-提单报关信息")
    @PostMapping("/saveBillCustomsInfo")
    @ApiOperationSupport(order = 16)
    public CommonResult<BillCustomsInfoVO> saveBillCustomsInfo(@Valid @RequestBody BillCustomsInfoForm form){
        BillCustomsInfoVO billCustomsInfoVO = oceanBillService.saveBillCustomsInfo(form);
        return CommonResult.success(billCustomsInfoVO);
    }

    @ApiOperation(value = "添加修改-提单柜子信息")
    @PostMapping("/saveOceanCounter")
    @ApiOperationSupport(order = 17)
    public CommonResult<OceanCounterVO> saveOceanCounter(@Valid @RequestBody OceanCounterForm form){
        OceanCounterVO oceanCounterVO = oceanBillService.saveOceanCounter(form);
        return CommonResult.success(oceanCounterVO);
    }

    //删除

    @ApiOperation(value = "删除-提单清关信息")
    @PostMapping("/delBillClearanceInfo")
    @ApiOperationSupport(order = 18)
    public CommonResult delBillClearanceInfo(@Valid @RequestBody BillClearanceInfoIdForm form){
        oceanBillService.delBillClearanceInfo(form);
        return CommonResult.success("删除成功");
    }

    @ApiOperation(value = "删除-提单报关信息")
    @PostMapping("/delBillCustomsInfo")
    @ApiOperationSupport(order = 19)
    public CommonResult delBillCustomsInfo(@Valid @RequestBody BillCustomsInfoIdForm form){
        oceanBillService.delBillCustomsInfo(form);
        return CommonResult.success("删除成功");
    }

    @ApiOperation(value = "删除-提单柜子信息")
    @PostMapping("/delOceanCounter")
    @ApiOperationSupport(order = 20)
    public CommonResult delOceanCounter(@Valid @RequestBody OceanCounterIdForm form){
        oceanBillService.delOceanCounter(form);
        return CommonResult.success("删除成功");
    }

    @ApiOperation(value = "提单，选择配载的箱子(配载，报价，订单，箱号 -> 展示 箱号) 分页")
    @PostMapping("/findConfCaseByPage")
    @ApiOperationSupport(order = 21)
    public CommonResult<CommonPageResult<ConfCaseVO>> findConfCaseByPage(@Valid @RequestBody ConfCaseForm form){
        IPage<ConfCaseVO> pageList = oceanBillService.findConfCaseByPage(form);
        CommonPageResult<ConfCaseVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }


}
