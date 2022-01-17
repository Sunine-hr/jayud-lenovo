package com.jayud.scm.controller;


import cn.hutool.core.map.MapUtil;
import com.jayud.common.CommonResult;
import com.jayud.scm.model.bo.AddAcctReceiptForm;
import com.jayud.scm.model.bo.AddExportTaxInvoiceForm;
import com.jayud.scm.model.bo.QueryCommonForm;
import com.jayud.scm.model.vo.AcctReceiptVO;
import com.jayud.scm.model.vo.ExportTaxInvoiceVO;
import com.jayud.scm.service.IExportTaxInvoiceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * 进项票主表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-09-09
 */
@RestController
@RequestMapping("/exportTaxInvoice")
@Api(tags = "进项票管理")
public class ExportTaxInvoiceController {

    @Autowired
    private IExportTaxInvoiceService exportTaxInvoiceService;

    @ApiOperation(value = "根据id查询进项票信息")
    @PostMapping(value = "/getExportTaxInvoiceById")
    public CommonResult<ExportTaxInvoiceVO> getExportTaxInvoiceById(@RequestBody Map<String,Object> map) {
        Integer id = MapUtil.getInt(map, "id");
        ExportTaxInvoiceVO exportTaxInvoiceVO = exportTaxInvoiceService.getExportTaxInvoiceById(id);
        return CommonResult.success(exportTaxInvoiceVO);
    }

    @ApiOperation(value = "新增或修改进项票信息")
    @PostMapping(value = "/saveOrUpdateExportTaxInvoice")
    public CommonResult saveOrUpdateExportTaxInvoice(@RequestBody AddExportTaxInvoiceForm form) {
        boolean result = exportTaxInvoiceService.saveOrUpdateExportTaxInvoice(form);
        if(!result){
            return CommonResult.error(444,"添加或修改进项票失败");
        }
        return CommonResult.success();
    }


    @ApiOperation(value = "发函时间")
    @PostMapping(value = "/sendingTime")
    public CommonResult sendingTime(@RequestBody QueryCommonForm form) {
        ExportTaxInvoiceVO taxInvoiceVO = this.exportTaxInvoiceService.getExportTaxInvoiceById(form.getId());
        if(!taxInvoiceVO.getLetterFlag().equals("未发函")){
            return CommonResult.error(444,"请选择未发函的数据");
        }
        boolean result = this.exportTaxInvoiceService.sendingTime(form);
        if(result){
            return CommonResult.success();
        }
        return CommonResult.error(444,"发函时间录入失败");
    }

    @ApiOperation(value = "回函时间")
    @PostMapping(value = "/replyTime")
    public CommonResult replyTime(@RequestBody QueryCommonForm form) {
        ExportTaxInvoiceVO taxInvoiceVO = this.exportTaxInvoiceService.getExportTaxInvoiceById(form.getId());
        if(!taxInvoiceVO.getLetterFlag().equals("函调中")){
            return CommonResult.error(444,"请选择未回函的数据");
        }
        boolean result = this.exportTaxInvoiceService.replyTime(form);
        if(result){
            return CommonResult.success();
        }
        return CommonResult.error(444,"回函时间录入失败");
    }

    @ApiOperation(value = "认证时间")
    @PostMapping(value = "/certificationTime")
    public CommonResult certificationTime(@RequestBody QueryCommonForm form) {
        boolean result = this.exportTaxInvoiceService.certificationTime(form);
        if(result){
            return CommonResult.success();
        }
        return CommonResult.error(444,"认证时间录入失败");
    }

    @ApiOperation(value = "退税款录入")
    @PostMapping(value = "/taxRefundEntry")
    public CommonResult taxRefundEntry(@RequestBody QueryCommonForm form) {
        boolean result = this.exportTaxInvoiceService.taxRefundEntry(form);
        if(result){
            return CommonResult.success();
        }
        return CommonResult.error(444,"退税款录入失败");
    }
}

