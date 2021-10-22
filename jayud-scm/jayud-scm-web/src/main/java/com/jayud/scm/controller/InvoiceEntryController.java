package com.jayud.scm.controller;


import cn.hutool.core.map.MapUtil;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.AddInvoiceEntryForm;
import com.jayud.scm.model.bo.QueryForm;
import com.jayud.scm.model.vo.InvoiceDetailVO;
import com.jayud.scm.model.vo.InvoiceEntryVO;
import com.jayud.scm.service.IInvoiceEntryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 结算明细表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-09-07
 */
@RestController
@RequestMapping("/invoiceEntry")
@Api(tags = "应收款明细")
public class InvoiceEntryController {

    @Autowired
    private IInvoiceEntryService iInvoiceEntryService;

    @ApiOperation(value = "根据应收款id和委托单明细id获取应收款明细")
    @PostMapping(value = "/findByPage")
    public CommonResult<List<InvoiceEntryVO>> findInvoiceEntryByInvoiceIdAndOrderEntryId(@RequestBody Map<String,Object> map) {
        Integer invoiceId = MapUtil.getInt(map, "invoiceId");
        Integer orderEntryId = MapUtil.getInt(map, "orderEntryId");
        List<InvoiceEntryVO> invoiceEntryVOList = iInvoiceEntryService.findInvoiceEntryByInvoiceIdAndOrderEntryId(invoiceId,orderEntryId);
        return CommonResult.success(invoiceEntryVOList);
    }

    @ApiOperation(value = "修改应收款明细")
    @PostMapping(value = "/updateInvoiceEntry")
    public CommonResult updateInvoiceEntry(@RequestBody List<AddInvoiceEntryForm> invoiceEntryVOList) {
        boolean result = iInvoiceEntryService.updateInvoiceEntry(invoiceEntryVOList);
        if(!result){
            return CommonResult.error(444,"修改应收款明细失败");
        }
        return CommonResult.success();
    }
}

