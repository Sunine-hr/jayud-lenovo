package com.jayud.finance.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.enums.ResultEnum;
import com.jayud.finance.bo.*;
import com.jayud.finance.enums.BillEnum;
import com.jayud.finance.enums.FormIDEnum;
import com.jayud.finance.po.OrderPaymentBillDetail;
import com.jayud.finance.po.OrderReceivableBillDetail;
import com.jayud.finance.service.*;
import com.jayud.finance.util.StringUtils;
import com.jayud.finance.vo.*;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
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

    @Autowired
    ICancelAfterVerificationService verificationService;//核销

    @Autowired
    IMakeInvoiceService makeInvoiceService;//开票

    @Autowired
    KingdeeService service;

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
    public CommonResult<Map<String,Object>> findFBillAuditByPage(@RequestBody @Valid QueryFBillAuditForm form) {
        IPage<PaymentNotPaidBillVO> pageList = paymentBillDetailService.findFBillAuditByPage(form);
        CommonPageResult<PaymentNotPaidBillVO> pageVO = new CommonPageResult(pageList);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("pageList",pageVO);//列表
        ViewBillVO viewBillVO = receivableBillDetailService.getViewSBill(form.getBillNo());
        resultMap.put(CommonConstant.WHOLE_DATA,viewBillVO);//全局数据
        return CommonResult.success(resultMap);
    }

    @ApiOperation(value = "应收对账单审核列表,对账单明细")
    @PostMapping("/findSBillAuditByPage")
    public CommonResult<Map<String,Object>> findSBillAuditByPage(@RequestBody @Valid QueryFBillAuditForm form) {
        IPage<PaymentNotPaidBillVO> pageList = receivableBillDetailService.findSBillAuditByPage(form);
        CommonPageResult<PaymentNotPaidBillVO> pageVO = new CommonPageResult(pageList);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("pageList",pageVO);//列表
        ViewBillVO viewBillVO = receivableBillDetailService.getViewSBill(form.getBillNo());
        resultMap.put(CommonConstant.WHOLE_DATA,viewBillVO);//全局数据
        return CommonResult.success(resultMap);
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
        String billNo = MapUtil.getStr(param,"billNo");
        List<HeXiaoListVO> heXiaoList = verificationService.heXiaoList(billNo);
        return CommonResult.success(heXiaoList);
    }

    @ApiOperation(value = "核销确认")
    @PostMapping("/heXiaoConfirm")
    public CommonResult heXiaoConfirm(@RequestBody @Valid HeXiaoConfirmListForm form) {
        return verificationService.heXiaoConfirm(form);
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

    @ApiOperation(value = "付款审核")
    @PostMapping("/auditFInvoice")
    public CommonResult auditFInvoice(@RequestBody @Valid BillAuditForm form) {
        return paymentBillDetailService.auditFInvoice(form);
    }

    @ApiOperation(value = "开票审核")
    @PostMapping("/auditSInvoice")
    public CommonResult auditSInvoice(@RequestBody @Valid BillAuditForm form) {
        return receivableBillDetailService.auditSInvoice(form);
    }

    @ApiOperation(value = "开票核销列表,付款核销列表 billNo=账单编号")
    @PostMapping("/findInvoiceList")
    public CommonResult<List<MakeInvoiceVO>> findInvoiceList(@RequestBody Map<String,Object> param) {
        String billNo = MapUtil.getStr(param,"billNo");
        List<MakeInvoiceVO> invoiceVOS = makeInvoiceService.findInvoiceList(billNo);
        return CommonResult.success(invoiceVOS);
    }

    @ApiOperation(value = "开票核销，付款核销")
    @PostMapping("/makeInvoice")
    public CommonResult makeInvoice(@RequestBody @Valid MakeInvoiceListForm form) {
        return makeInvoiceService.makeInvoice(form);
    }

    @ApiOperation(value = "开票核销作废,付款核销作废 invoiceId开票ID或付款ID")
    @PostMapping("/makeInvoiceDel")
    public CommonResult makeInvoiceDel(@RequestBody Map<String,Object> param) {
        Long inVoiceId = Long.parseLong(MapUtil.getStr(param,"invoiceId"));
        Boolean result = makeInvoiceService.makeInvoiceDel(inVoiceId);
        if (!result) {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        return CommonResult.success();
    }



    /**
     * 推送应收单到金蝶
     *
     * @param form
     * @return
     */
    @PostMapping("/pushReceivable billNos = 编号集合")
    @ApiOperation(value = "推送应收单")
    public CommonResult saveReceivableBill(@RequestBody ListForm form) {
        //校验是否可推送金蝶
        //1.必须财务已审核通过
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("bill_no",form.getBillNos());
        List<OrderReceivableBillDetail> receivableBillDetails = receivableBillDetailService.list(queryWrapper);
        StringBuilder sb = new StringBuilder("账单编号:");
        Boolean flag = false;
        for (OrderReceivableBillDetail receivableBillDetail : receivableBillDetails) {
            if(StringUtil.isNullOrEmpty(receivableBillDetail.getAuditStatus()) || !BillEnum.B_6.getCode().equals(receivableBillDetail.getAuditStatus())){
                flag = true;
                sb.append(receivableBillDetail.getBillNo());
            }
        }
        sb.append("财务未审核通过,不能推送金蝶");
        if(flag) {
            return CommonResult.error(10001, sb.toString());
        }
        //构建数据，推金蝶
        for (OrderReceivableBillDetail receivableBillDetail : receivableBillDetails) {
            ReceivableHeaderForm reqForm = new ReceivableHeaderForm();
            service.saveReceivableBill(FormIDEnum.RECEIVABLE.getFormid(), reqForm);
        }
        return CommonResult.success();
    }

    /**
     * 推送应付单到金蝶
     *
     * @param form
     * @return
     */
    @PostMapping("/pushPayment")
    @ApiOperation(value = "推送应付单 billNos = 编号集合")
    public CommonResult savePayableBill(@RequestBody ListForm form) {
        //校验是否可推送金蝶
        //1.必须财务已审核通过
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("bill_no",form.getBillNos());
        List<OrderPaymentBillDetail> paymentBillDetailList = paymentBillDetailService.list(queryWrapper);
        StringBuilder sb = new StringBuilder("账单编号:");
        Boolean flag = false;
        for (OrderPaymentBillDetail paymentBillDetail : paymentBillDetailList) {
            if(StringUtil.isNullOrEmpty(paymentBillDetail.getAuditStatus()) || !BillEnum.B_6.getCode().equals(paymentBillDetail.getAuditStatus())){
                flag = true;
                sb.append(paymentBillDetail.getBillNo());
            }
        }
        sb.append("财务未审核通过,不能推送金蝶");
        if(flag) {
            return CommonResult.error(10001, sb.toString());
        }
        //构建数据，推金蝶
        for (OrderPaymentBillDetail paymentBillDetail : paymentBillDetailList) {
            PayableHeaderForm reqForm = new PayableHeaderForm();
            service.savePayableBill(FormIDEnum.PAYABLE.getFormid(), reqForm);
        }
        return CommonResult.success();
    }


}
