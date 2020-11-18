package com.jayud.finance.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.ResultEnum;
import com.jayud.finance.bo.*;
import com.jayud.finance.service.IOrderPaymentBillDetailService;
import com.jayud.finance.service.IOrderReceivableBillDetailService;
import com.jayud.finance.util.StringUtils;
import com.jayud.finance.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/finance/")
@Api(tags = "财务管理模块")
public class FinanceController {

    @Autowired
    IOrderPaymentBillDetailService paymentBillDetailService;

    @Autowired
    IOrderReceivableBillDetailService receivableBillDetailService;

    /**财务核算*/
    @ApiOperation(value = "财务核算列表")
    @PostMapping("/findFinanceAccountByPage")
    public CommonResult<CommonPageResult<FinanceAccountVO>> findFinanceAccountByPage(@RequestBody @Valid QueryFinanceAccountForm form) {
        IPage<FinanceAccountVO> pageList = paymentBillDetailService.findFinanceAccountByPage(form);
        CommonPageResult<FinanceAccountVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "导出财务核算列表")
    @RequestMapping(value = "/exportCwBill", method = RequestMethod.GET)
    @ResponseBody
    public void exportCwBill(QueryFinanceAccountForm form,
                            HttpServletResponse response) throws IOException {
        //获取数据
        List<FinanceAccountVO> list = paymentBillDetailService.findFinanceAccount(form);

        ExcelWriter writer = ExcelUtil.getWriter(true);

        //自定义标题别名
        writer.addHeaderAlias("orderNo", "订单号");
        writer.addHeaderAlias("createTimeStr", "创建时间");
        writer.addHeaderAlias("legalName", "法人主体");
        writer.addHeaderAlias("customerName", "客户");
        writer.addHeaderAlias("customerCode", "客户代码");
        writer.addHeaderAlias("ywName", "业务员");
        writer.addHeaderAlias("sBillNo", "应收账单号");
        writer.addHeaderAlias("sAccountTermStr", "应收核算期");
        writer.addHeaderAlias("sRmb", "应收人民币");
        writer.addHeaderAlias("sDollar", "应收美元");
        writer.addHeaderAlias("sEuro", "应收欧元");
        writer.addHeaderAlias("sHkDollar", "应收港币");
        writer.addHeaderAlias("sLocalAmount", "应收本币金额");
        writer.addHeaderAlias("sStatus", "应收对账单状态");
        writer.addHeaderAlias("sCostStatus", "应收费用状态");
        writer.addHeaderAlias("fBillNo", "应付账单号");
        writer.addHeaderAlias("fAccountTermStr", "应付核算期");
        writer.addHeaderAlias("fRmb", "应付人民币");
        writer.addHeaderAlias("fDollar", "应付美元");
        writer.addHeaderAlias("fEuro", "应付欧元");
        writer.addHeaderAlias("fHkDollar", "应付港币");
        writer.addHeaderAlias("fLocalAmount", "应付本币金额");
        writer.addHeaderAlias("fCostStatus", "应付费用状态");
        writer.addHeaderAlias("fStatus", "应付对账单状态");
        writer.addHeaderAlias("profit", "利润(人民币)");

        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(list, true);

        //out为OutputStream，需要写出到的目标流
        ServletOutputStream out = response.getOutputStream();
        String name = StringUtils.toUtf8String("财务核算列表");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename="+name+".xlsx");

        writer.flush(out);
        writer.close();
        IoUtil.close(out);
    }

    /**对账单管理*/
    @ApiOperation(value = "应付对账单审核列表,对账单明细")
    @PostMapping("/findFBillAuditByPage")
    public CommonResult<CommonPageResult<PaymentNotPaidBillVO>> findFBillAuditByPage(@RequestBody @Valid QueryFBillAuditForm form) {
        IPage<PaymentNotPaidBillVO> pageList = paymentBillDetailService.findFBillAuditByPage(form);
        CommonPageResult<PaymentNotPaidBillVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "应收对账单审核列表,对账单明细")
    @PostMapping("/findSBillAuditByPage")
    public CommonResult<CommonPageResult<PaymentNotPaidBillVO>> findSBillAuditByPage(@RequestBody @Valid QueryFBillAuditForm form) {
        IPage<PaymentNotPaidBillVO> pageList = receivableBillDetailService.findSBillAuditByPage(form);
        CommonPageResult<PaymentNotPaidBillVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "导出应付对账单明细列表")
    @RequestMapping(value = "/exportFBillDetailList", method = RequestMethod.GET)
    @ResponseBody
    public void exportFBillDetailList(QueryFBillAuditForm form,
                                    HttpServletResponse response) throws IOException {
        List<PaymentNotPaidBillVO> list = paymentBillDetailService.findFBillAudit(form);
        ExcelWriter writer = ExcelUtil.getWriter(true);

        //自定义标题别名
        writer.addHeaderAlias("orderNo", "订单编号");
        writer.addHeaderAlias("bizCodeDesc", "业务类型");
        writer.addHeaderAlias("createdTimeStr", "日期");
        writer.addHeaderAlias("supplierChName", "供应商");
        writer.addHeaderAlias("startAddress", "起运地");
        writer.addHeaderAlias("endAddress", "目的地");
        writer.addHeaderAlias("licensePlate", "车牌号");
        writer.addHeaderAlias("yunCustomsNo", "报关单号");
        writer.addHeaderAlias("costTypeName", "费用类别");
        writer.addHeaderAlias("costGenreName", "费用类型");
        writer.addHeaderAlias("costName", "费用名称");
        writer.addHeaderAlias("rmb", "人民币");
        writer.addHeaderAlias("dollar", "美元");
        writer.addHeaderAlias("euro", "欧元");
        writer.addHeaderAlias("hKDollar", "港币");
        writer.addHeaderAlias("taxRate", "税率");
        writer.addHeaderAlias("remarks", "费用备注");

        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(list, true);

        //out为OutputStream，需要写出到的目标流
        ServletOutputStream out=response.getOutputStream();
        String name = StringUtils.toUtf8String("客户应付对账单");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename="+name+".xlsx");

        writer.flush(out);
        writer.close();
        IoUtil.close(out);
    }

    @ApiOperation(value = "导出应收对账单明细列表")
    @RequestMapping(value = "/exportSBillDetailList", method = RequestMethod.GET)
    @ResponseBody
    public void exportSBillDetailList(QueryFBillAuditForm form,
                                      HttpServletResponse response) throws IOException {
        List<PaymentNotPaidBillVO> list = receivableBillDetailService.findSBillAudit(form);
        ExcelWriter writer = ExcelUtil.getWriter(true);

        //自定义标题别名
        writer.addHeaderAlias("orderNo", "订单编号");
        writer.addHeaderAlias("bizCodeDesc", "业务类型");
        writer.addHeaderAlias("createdTimeStr", "日期");
        writer.addHeaderAlias("customerName", "客服");
        writer.addHeaderAlias("startAddress", "起运地");
        writer.addHeaderAlias("endAddress", "目的地");
        writer.addHeaderAlias("licensePlate", "车牌号");
        writer.addHeaderAlias("yunCustomsNo", "报关单号");
        writer.addHeaderAlias("costTypeName", "费用类别");
        writer.addHeaderAlias("costGenreName", "费用类型");
        writer.addHeaderAlias("costName", "费用名称");
        writer.addHeaderAlias("rmb", "人民币");
        writer.addHeaderAlias("dollar", "美元");
        writer.addHeaderAlias("euro", "欧元");
        writer.addHeaderAlias("hKDollar", "港币");
        writer.addHeaderAlias("taxRate", "税率");
        writer.addHeaderAlias("remarks", "费用备注");

        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(list, true);

        //out为OutputStream，需要写出到的目标流
        ServletOutputStream out=response.getOutputStream();
        String name = StringUtils.toUtf8String("客户应收对账单");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename="+name+".xlsx");

        writer.flush(out);
        writer.close();
        IoUtil.close(out);
    }

    @ApiOperation(value = "核销列表")
    @PostMapping("/heXiaoList")
    public CommonResult<List<HeXiaoListVO>> heXiaoList(@RequestBody Map<String,Object> param) {
        String billNo = MapUtil.getStr(param,"bill_no");
        List<HeXiaoListVO> heXiaoList = paymentBillDetailService.heXiaoList(billNo);
        return CommonResult.success(heXiaoList);
    }

    @ApiOperation(value = "核销确认")
    @PostMapping("/heXiaoConfirm")
    public CommonResult heXiaoConfirm(@RequestBody @Valid List<HeXiaoConfirmForm> form) {
        Boolean result = paymentBillDetailService.heXiaoConfirm(form);
        if(!result){
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "付款审核列表 billNo = 账单编号")
    @PostMapping("/findFCostList")
    public CommonResult<List<FCostVO>> findFCostList(@RequestBody Map<String,Object> param) {
        String billNo = MapUtil.getStr(param,"billNo");
        List<FCostVO> fCostVOS = paymentBillDetailService.findFCostList(billNo);
        return CommonResult.success(fCostVOS);
    }

    @ApiOperation(value = "开票审核列表 billNo = 账单编号")
    @PostMapping("/findSCostList")
    public CommonResult<List<FCostVO>> findSCostList(@RequestBody Map<String,Object> param) {
        String billNo = MapUtil.getStr(param,"billNo");
        List<FCostVO> fCostVOS = receivableBillDetailService.findSCostList(billNo);
        return CommonResult.success(fCostVOS);
    }

    @ApiOperation(value = "开票审核")
    @PostMapping("/auditInvoice")
    public CommonResult auditInvoice(@RequestBody @Valid BillAuditForm form) {
        Boolean result = paymentBillDetailService.auditInvoice(form);
        if (!result) {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "开票核销列表,付款核销列表 bill_no=账单编号")
    @PostMapping("/findInvoiceList")
    public CommonResult<List<MakeInvoiceVO>> findInvoiceList(@RequestBody Map<String,Object> param) {
        String billNo = MapUtil.getStr(param,"bill_no");
        List<MakeInvoiceVO> invoiceVOS = paymentBillDetailService.findInvoiceList(billNo);
        return CommonResult.success(invoiceVOS);
    }

    @ApiOperation(value = "开票核销，付款核销")
    @PostMapping("/makeInvoice")
    public CommonResult makeInvoice(@RequestBody @Valid MakeInvoiceForm form) {
        Boolean result = paymentBillDetailService.makeInvoice(form);
        if (!result) {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "开票核销作废,付款核销作废 invoiceId开票ID或付款ID")
    @PostMapping("/makeInvoiceDel")
    public CommonResult makeInvoiceDel(@RequestBody Map<String,Object> param) {
        Long inVoiceId = Long.parseLong(MapUtil.getStr(param,"invoiceId"));
        Boolean result = paymentBillDetailService.makeInvoiceDel(inVoiceId);
        if (!result) {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        return CommonResult.success();
    }





}
