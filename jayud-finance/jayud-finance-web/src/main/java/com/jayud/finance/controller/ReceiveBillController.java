package com.jayud.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.enums.ResultEnum;
import com.jayud.finance.bo.*;
import com.jayud.finance.service.IOrderReceivableBillService;
import com.jayud.finance.vo.*;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/receiveBill/")
@Api(tags = "应收出账")
public class ReceiveBillController {

    @Autowired
    IOrderReceivableBillService billService;

    @ApiOperation(value = "应收出账单列表(主订单/子订单)")
    @PostMapping("/findReceiveBillByPage")
    public CommonResult<CommonPageResult<OrderReceiveBillVO>> findReceiveBillByPage(@RequestBody @Valid QueryReceiveBillForm form) {
        IPage<OrderReceiveBillVO> pageList = billService.findReceiveBillByPage(form);
        CommonPageResult<OrderReceiveBillVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "账单数列表(主订单/子订单)")
    @PostMapping("/findReceiveBillNum")
    public CommonResult<Map<String,Object>> findReceiveBillNum(@RequestBody @Valid QueryReceiveBillNumForm form) {
        Map<String,Object> result = billService.findReceiveBillNum(form);
        return CommonResult.success(result);
    }

    @ApiOperation(value = "未出账订单数列表(主订单/子订单)")
    @PostMapping("/findNotPaidBillByPage")
    public CommonResult<CommonPageResult<ReceiveNotPaidBillVO>> findNotPaidBillByPage(@RequestBody @Valid QueryNotPaidBillForm form) {
        IPage<ReceiveNotPaidBillVO> pageList = billService.findNotPaidBillByPage(form);
        CommonPageResult<ReceiveNotPaidBillVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "暂存和生成应收账单")
    @PostMapping("/createBill")
    public CommonResult createReceiveBill(@RequestBody @Valid CreateReceiveBillForm form) {
        //参数校验
        if("create".equals(form.getCmd())){
            OrderReceiveBillForm receiveBillForm = form.getReceiveBillForm();
            if(receiveBillForm == null || StringUtil.isNullOrEmpty(receiveBillForm.getLegalName()) || StringUtil.isNullOrEmpty(receiveBillForm.getUnitAccount()) ||
               receiveBillForm.getBillOrderNum() == null || receiveBillForm.getAlreadyPaidAmount() == null ||  receiveBillForm.getBillNum() == null ||
               StringUtil.isNullOrEmpty(form.getBillNo()) || StringUtil.isNullOrEmpty(form.getBeginAccountTermStr()) ||
               StringUtil.isNullOrEmpty(form.getEndAccountTermStr()) || StringUtil.isNullOrEmpty(form.getSettlementCurrency()) ||
               StringUtil.isNullOrEmpty(form.getSubType()) || (!("main".equals(form.getSubType()) || "zgys".equals(form.getSubType()) ||
               "bg".equals(form.getSubType())))){
                return CommonResult.error(ResultEnum.PARAM_ERROR);
            }
        }
        Boolean result = billService.createReceiveBill(form);
        if(result){
            return CommonResult.success();
        }
        return CommonResult.error(ResultEnum.OPR_FAIL);
    }

    @ApiOperation(value = "预览应收账单")
    @PostMapping("/viewReceiveBill")
    public CommonResult<Map<String,Object>> viewReceiveBill(@RequestBody @Valid ViewSBillForm form) {
        List<OrderReceiveBillDetailForm> billDetailForms = form.getBillDetailForms();
        List<Long> costIds = new ArrayList<>();
        for (OrderReceiveBillDetailForm billDetailForm : billDetailForms) {
            costIds.add(billDetailForm.getCostId());
        }
        Map<String,Object> resultMap = new HashMap<>();
        List<ViewBilToOrderVO> list = billService.viewReceiveBill(costIds);
        resultMap.put(CommonConstant.LIST,list);//分页数据
        List<SheetHeadVO> sheetHeadVOS = billService.findSheetHead(costIds);
        resultMap.put(CommonConstant.SHEET_HEAD,sheetHeadVOS);//表头
        ViewBillVO viewBillVO = billService.getViewBillByCostIds(costIds);
        resultMap.put(CommonConstant.WHOLE_DATA,viewBillVO);//全局数据
        return CommonResult.success(resultMap);
    }


}
