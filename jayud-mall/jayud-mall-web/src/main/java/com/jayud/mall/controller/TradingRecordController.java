package com.jayud.mall.controller;

import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.TradingRecordCZForm;
import com.jayud.mall.model.bo.TradingRecordForm;
import com.jayud.mall.model.bo.TradingRecordQueryForm;
import com.jayud.mall.model.vo.TradingRecordVO;
import com.jayud.mall.model.vo.domain.CustomerUser;
import com.jayud.mall.service.BaseService;
import com.jayud.mall.service.ITradingRecordService;
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
@RequestMapping("/tradingrecord")
@Api(tags = "C015-client-交易记录接口")
@ApiSort(value = 15)
public class TradingRecordController {

    @Autowired
    ITradingRecordService tradingRecordService;
    @Autowired
    BaseService baseService;

    //账户余额-立即充值
    @ApiOperation(value = "账户余额-立即充值(客户充值)")
    @PostMapping("/customerPay")
    @ApiOperationSupport(order = 1)
    public CommonResult customerPay(@Valid @RequestBody TradingRecordForm form) {
        CustomerUser customerUser = baseService.getCustomerUser();
        form.setCustomerId(Long.valueOf(customerUser.getId()));//当前登录客户
        tradingRecordService.customerPay(form);
        return CommonResult.success("操作成功");
    }

    //账户余额-充值记录（充值）
    @ApiOperation(value = "账户余额-充值记录（查询充值记录）")
    @PostMapping("/findTradingRecordByCz")
    @ApiOperationSupport(order = 2)
    public CommonResult<List<TradingRecordVO>> findTradingRecordByCz(@RequestBody TradingRecordCZForm form){
        CustomerUser customerUser = baseService.getCustomerUser();
        form.setCustomerId(Long.valueOf(customerUser.getId()));//当前登录客户
        List<TradingRecordVO> tradingRecordVOS = tradingRecordService.findTradingRecordByCz(form);
        return CommonResult.success(tradingRecordVOS);
    }

    //账户余额-交易记录（充值、支付）
    @ApiOperation(value = "账户余额-交易记录（查询充值、支付记录）")
    @PostMapping("/findTradingRecord")
    @ApiOperationSupport(order = 3)
    public CommonResult<List<TradingRecordVO>> findTradingRecord(@RequestBody TradingRecordQueryForm form){
        CustomerUser customerUser = baseService.getCustomerUser();
        form.setCustomerId(Long.valueOf(customerUser.getId()));//当前登录客户
        List<TradingRecordVO> tradingRecordVOS = tradingRecordService.findTradingRecord(form);
        return CommonResult.success(tradingRecordVOS);
    }


}
