package com.jayud.finance.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.enums.ResultEnum;
import com.jayud.finance.bo.*;
import com.jayud.finance.service.IOrderReceivableBillService;
import com.jayud.finance.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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
        Boolean result = billService.createReceiveBill(form);
        if(result){
            return CommonResult.success();
        }
        return CommonResult.error(ResultEnum.OPR_FAIL);
    }

    @ApiOperation(value = "预览应付账单")
    @PostMapping("/viewReceiveBill")
    public CommonResult<Map<String,Object>> viewReceiveBill(@RequestBody @Valid ViewBillForm form) {
        Map<String,Object> resultMap = new HashMap<>();
        List<ViewBilToOrderVO> list = null;//billService.viewReceiveBill(form);
        resultMap.put(CommonConstant.LIST,list);//分页数据
        List<SheetHeadVO> sheetHeadVOS = null;//billService.viewReceiveBill(form);
        resultMap.put(CommonConstant.SHEET_HEAD,sheetHeadVOS);//表头
        return CommonResult.success(resultMap);
    }


}
