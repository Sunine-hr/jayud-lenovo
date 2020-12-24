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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/finance/")
@Api(tags = "财务管理模块")
public class FinanceController {

    private static Logger logger = LoggerFactory.getLogger(FinanceController.class);

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
        ViewBillVO viewBillVO = paymentBillDetailService.getViewBill(form.getBillNo());
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
        writer.addHeaderAlias("subOrderNo", "子订单编号");
        writer.addHeaderAlias("billNo", "账单编号");
        writer.addHeaderAlias("bizCodeDesc", "业务类型");
        writer.addHeaderAlias("createdTimeStr", "日期");
        writer.addHeaderAlias("supplierChName", "供应商");
        writer.addHeaderAlias("goodsDesc", "货物信息");
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
        writer.addHeaderAlias("subOrderNo", "子订单编号");
        writer.addHeaderAlias("billNo", "账单编号");
        writer.addHeaderAlias("bizCodeDesc", "业务类型");
        writer.addHeaderAlias("createdTimeStr", "日期");
        writer.addHeaderAlias("customerName", "客户");
        writer.addHeaderAlias("goodsDesc", "货物信息");
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
    @PostMapping("/pushReceivable")
    @ApiOperation(value = "推送应收单,billNos = 编号集合")
    public CommonResult saveReceivableBill(@RequestBody ListForm form) {
        //校验是否可推送金蝶
        //1.必须财务已审核通过
        StringBuilder sb = new StringBuilder("账单编号:");
        Boolean flag = false;
        for (String billNo : form.getBillNos()) {
            QueryWrapper queryWrapper1 = new QueryWrapper();
            queryWrapper1.eq("bill_no",billNo);
            List<OrderReceivableBillDetail> tempObjects = receivableBillDetailService.list(queryWrapper1);
            if(tempObjects != null && tempObjects.size()>0){
                OrderReceivableBillDetail tempObject = tempObjects.get(0);
                if(StringUtil.isNullOrEmpty(tempObject.getAuditStatus()) || !BillEnum.B_6.getCode().equals(tempObject.getAuditStatus())){
                    flag = true;
                    sb.append(tempObject.getBillNo()+";");
                }
            }
        }
        sb.append("财务未审核通过,不能推送金蝶");
        if(flag) {
            return CommonResult.error(10001, sb.toString());
        }
        //构建数据，推金蝶
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("bill_no",form.getBillNos());
        List<OrderReceivableBillDetail> receivableBillDetails = receivableBillDetailService.list(queryWrapper);
        for (OrderReceivableBillDetail receivableBillDetail : receivableBillDetails) {
            ReceivableHeaderForm reqForm = receivableBillDetailService.getReceivableHeaderForm(receivableBillDetail.getBillNo());
            /*if(reqForm != null && !StringUtil.isNullOrEmpty(reqForm.getCustomerName())){
                int index = reqForm.getCustomerName().indexOf("(");
                String customerName = reqForm.getCustomerName().substring(0,index);
                reqForm.setCustomerName(customerName);
            }*/
            List<APARDetailForm> entityDetail = receivableBillDetailService.findReceivableHeaderDetail(receivableBillDetail.getBillNo());
            reqForm.setEntityDetail(entityDetail);
            logger.info("推送金蝶传参:" + reqForm);
            CommonResult result = service.saveReceivableBill(FormIDEnum.RECEIVABLE.getFormid(), reqForm);
            if(result.getCode() == 0){//推送成功,则记录推送金蝶次数
                OrderReceivableBillDetail tempObject = new OrderReceivableBillDetail();
                tempObject.setPushKingdeeCount(receivableBillDetail.getPushKingdeeCount() + 1);
                QueryWrapper updateWrapper = new QueryWrapper();
                updateWrapper.eq("bill_no",receivableBillDetail.getBillNo());
                receivableBillDetailService.update(tempObject,updateWrapper);
            }
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
        StringBuilder sb = new StringBuilder("账单编号:");
        Boolean flag = false;
        for (String billNo : form.getBillNos()) {
            QueryWrapper queryWrapper1 = new QueryWrapper();
            queryWrapper1.eq("bill_no",billNo);
            List<OrderPaymentBillDetail> tempObjects = receivableBillDetailService.list(queryWrapper1);
            if(tempObjects != null && tempObjects.size()>0){
                OrderPaymentBillDetail tempObject = tempObjects.get(0);
                if(StringUtil.isNullOrEmpty(tempObject.getAuditStatus()) || !BillEnum.B_6.getCode().equals(tempObject.getAuditStatus())){
                    flag = true;
                    sb.append(tempObject.getBillNo()+";");
                }
            }
        }
        sb.append("财务未审核通过,不能推送金蝶");
        if(flag) {
            return CommonResult.error(10001, sb.toString());
        }
        //构建数据，推金蝶
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.in("bill_no",form.getBillNos());
        List<OrderPaymentBillDetail> paymentBillDetailList = paymentBillDetailService.list(queryWrapper);
        for (OrderPaymentBillDetail paymentBillDetail : paymentBillDetailList) {
            PayableHeaderForm reqForm = paymentBillDetailService.getPayableHeaderForm(paymentBillDetail.getBillNo());
            List<APARDetailForm> entityDetail = paymentBillDetailService.findPayableHeaderDetail(paymentBillDetail.getBillNo());
            reqForm.setEntityDetail(entityDetail);
            logger.info("推送金蝶传参:" + reqForm);
            CommonResult result = service.savePayableBill(FormIDEnum.PAYABLE.getFormid(), reqForm);
            if(result.getCode() == 0){//推送成功,则记录推送金蝶次数
                OrderPaymentBillDetail tempObject = new OrderPaymentBillDetail();
                tempObject.setPushKingdeeCount(paymentBillDetail.getPushKingdeeCount() + 1);
                QueryWrapper updateWrapper = new QueryWrapper();
                updateWrapper.eq("bill_no",paymentBillDetail.getBillNo());
                paymentBillDetailService.update(tempObject,updateWrapper);
            }
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "费用状态下拉框")
    @PostMapping(value = "/initBillStatus")
    public CommonResult<List<InitComboxStrVO>> initBillStatus() {
        List<InitComboxStrVO> comboxStrVOS = new ArrayList<>();
        for (BillEnum billEnum : BillEnum.values()) {
            if(BillEnum.B_1.getCode().equals(billEnum.getCode()) || BillEnum.B_2.getCode().equals(billEnum.getCode()) ||
               BillEnum.B_2_1.getCode().equals(billEnum.getCode()) || BillEnum.B_3.getCode().equals(billEnum.getCode()) ||
               BillEnum.B_4.getCode().equals(billEnum.getCode()) || BillEnum.B_4_1.getCode().equals(billEnum.getCode()) ||
               BillEnum.B_5.getCode().equals(billEnum.getCode()) || BillEnum.B_5_1.getCode().equals(billEnum.getCode()) ||
               BillEnum.B_6.getCode().equals(billEnum.getCode()) || BillEnum.B_6_1.getCode().equals(billEnum.getCode()) ||
               BillEnum.B_7.getCode().equals(billEnum.getCode()) || BillEnum.B_8.getCode().equals(billEnum.getCode()) ||
               BillEnum.B_9.getCode().equals(billEnum.getCode())){
                InitComboxStrVO initComboxStrVO = new InitComboxStrVO();
                initComboxStrVO.setCode(billEnum.getCode());
                initComboxStrVO.setName(billEnum.getDesc());
                comboxStrVOS.add(initComboxStrVO);
            }
        }
        return CommonResult.success(comboxStrVOS);
    }

    @ApiOperation(value = "开票/付款申请,核销,开票/付款核销界面的金额初始化,billNo=账单编号")
    @PostMapping(value = "/getCostAmountView")
    public CommonResult<CostAmountVO> getCostAmountView(@RequestBody Map<String,Object> param) {
        String billNo = MapUtil.getStr(param,"billNo");
        if(StringUtil.isNullOrEmpty(billNo)){
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        CostAmountVO costAmountVO = new CostAmountVO();
        CostAmountVO costFAmountVO = paymentBillDetailService.getFCostAmountView(billNo);
        CostAmountVO costSAmountVO = receivableBillDetailService.getSCostAmountView(billNo);
        costAmountVO.setBillNo(billNo);
        if(costSAmountVO != null) {
            costAmountVO.setYsAmount(costSAmountVO.getYsAmount());
            costAmountVO.setYsCurrency(costSAmountVO.getYsCurrency());
            costAmountVO.setWsAmount(costSAmountVO.getWsAmount());
            costAmountVO.setWsCurrency(costSAmountVO.getWsCurrency());
        }
        if(costFAmountVO != null) {
            costAmountVO.setYfAmount(costFAmountVO.getYfAmount());
            costAmountVO.setYfCurrency(costFAmountVO.getYfCurrency());
            costAmountVO.setDfAmount(costFAmountVO.getDfAmount());
            costAmountVO.setDfCurrency(costFAmountVO.getDfCurrency());
        }
        return CommonResult.success(costAmountVO);
    }


}
