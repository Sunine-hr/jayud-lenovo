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
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/finance/")
@Api(tags = "财务管理模块")
public class FinanceController {

    @Autowired
    IOrderPaymentBillDetailService billDetailService;

    /**财务核算*/
    @ApiOperation(value = "财务核算列表")
    @PostMapping("/findFinanceAccountByPage")
    public CommonResult<CommonPageResult<FinanceAccountVO>> findFinanceAccountByPage(@RequestBody @Valid QueryFinanceAccountForm form) {
        IPage<FinanceAccountVO> pageList = billDetailService.findFinanceAccountByPage(form);
        CommonPageResult<FinanceAccountVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "导出财务核算列表")
    @RequestMapping(value = "/exportCwBill", method = RequestMethod.GET)
    @ResponseBody
    public void exportCwBill(QueryFinanceAccountForm form,
                            HttpServletResponse response) throws IOException {
        //获取数据
        List<FinanceAccountVO> list = billDetailService.findFinanceAccount(form);

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
    //推金碟 TODO


    @ApiOperation(value = "应付对账单审核列表,对账单明细")
    @PostMapping("/findFBillAuditByPage")
    public CommonResult<CommonPageResult<PaymentNotPaidBillVO>> findFBillAuditByPage(@RequestBody @Valid QueryEditBillForm form) {
        IPage<PaymentNotPaidBillVO> pageList = billDetailService.findFBillAuditByPage(form);
        CommonPageResult<PaymentNotPaidBillVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "导出应付对账单详情列表")
    @RequestMapping(value = "/exportBillDetailList", method = RequestMethod.GET)
    @ResponseBody
    public void exportBillDetailList(@RequestParam(value = "billNo",required=true) String billNo,
                                    HttpServletResponse response) throws IOException {
        List<ViewBilToOrderVO> list = billDetailService.viewBillDetail(billNo);
        ExcelWriter writer = ExcelUtil.getWriter(true);

        //自定义标题别名
        writer.addHeaderAlias("createdTimeStr", "建单日期");
        writer.addHeaderAlias("orderNo", "订单编号");
        writer.addHeaderAlias("customerName", "客户");
        writer.addHeaderAlias("startAddress", "启运地");
        writer.addHeaderAlias("endAddress", "目的地");
        writer.addHeaderAlias("licensePlate", "车牌号");
        writer.addHeaderAlias("vehicleSize", "车型");
        writer.addHeaderAlias("pieceNum", "件数");
        writer.addHeaderAlias("weight", "毛重(KGS)");
        writer.addHeaderAlias("yunCustomsNo", "报关单号");
        List<SheetHeadVO> sheetHeadVOS = billDetailService.findSheetHead(billNo);
        for (SheetHeadVO sheetHeadVO : sheetHeadVOS) {
            writer.addHeaderAlias(sheetHeadVO.getName(), sheetHeadVO.getViewName());
        }


        Field[] s = ViewBilToOrderVO.class.getDeclaredFields();
        int lastColumn = s.length-1;

        // 合并单元格后的标题行，使用默认标题样式
        writer.merge(lastColumn, "B类表:存在`敏感品名`的货物表");

        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(list, true);

        //out为OutputStream，需要写出到的目标流

        ServletOutputStream out=response.getOutputStream();
        String name = StringUtils.toUtf8String("应收对账单列表");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename="+name+".xlsx");

        writer.flush(out);
        writer.close();
        IoUtil.close(out);
    }

    @ApiOperation(value = "核销确认显示列表")
    @PostMapping("/heXiaoList")
    public CommonResult<List<HeXiaoListVO>> heXiaoList(@RequestBody Map<String,Object> param) {
        String billNo = MapUtil.getStr(param,"bill_no");
        List<HeXiaoListVO> heXiaoList = billDetailService.heXiaoList(billNo);
        return CommonResult.success(heXiaoList);
    }

    @ApiOperation(value = "核销确认")
    @PostMapping("/heXiaoConfirm")
    public CommonResult heXiaoConfirm(@RequestBody @Valid List<HeXiaoConfirmForm> form) {
        Boolean result = billDetailService.heXiaoConfirm(form);
        if(!result){
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "开票审核列表查询")
    @PostMapping("/findFCostList")
    public CommonResult<List<FCostVO>> findFCostList(@RequestBody Map<String,Object> param) {
        String billNo = MapUtil.getStr(param,"bill_no");
        List<FCostVO> fCostVOS = billDetailService.findFCostList(billNo);
        return CommonResult.success(fCostVOS);
    }

    @ApiOperation(value = "开票审核")
    @PostMapping("/auditInvoice")
    public CommonResult auditInvoice(@RequestBody @Valid BillAuditForm form) {
        Boolean result = billDetailService.auditInvoice(form);
        if (!result) {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "开票核销列表")
    @PostMapping("/findInvoiceList")
    public CommonResult<List<MakeInvoiceVO>> findInvoiceList(@RequestBody Map<String,Object> param) {
        String billNo = MapUtil.getStr(param,"bill_no");
        List<MakeInvoiceVO> invoiceVOS = billDetailService.findInvoiceList(billNo);
        return CommonResult.success(invoiceVOS);
    }

    @ApiOperation(value = "开票核销")
    @PostMapping("/makeInvoice")
    public CommonResult makeInvoice(@RequestBody @Valid MakeInvoiceForm form) {
        Boolean result = billDetailService.makeInvoice(form);
        if (!result) {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "开票作废")
    @PostMapping("/makeInvoiceDel")
    public CommonResult makeInvoiceDel(@RequestBody Map<String,Object> param) {
        Long inVoiceId = Long.parseLong(MapUtil.getStr(param,"inVoiceId"));
        Boolean result = billDetailService.makeInvoiceDel(inVoiceId);
        if (!result) {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        return CommonResult.success();
    }





}
