package com.jayud.finance.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.enums.SubOrderSignEnum;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.Utilities;
import com.jayud.common.utils.excel.EasyExcelEntity;
import com.jayud.common.utils.excel.EasyExcelUtils;
import com.jayud.finance.bo.*;
import com.jayud.finance.enums.BillEnum;
import com.jayud.finance.enums.BillTemplateEnum;
import com.jayud.finance.enums.FormIDEnum;
import com.jayud.finance.po.CurrencyRate;
import com.jayud.finance.po.OrderPaymentBillDetail;
import com.jayud.finance.po.OrderReceivableBillDetail;
import com.jayud.finance.service.*;
import com.jayud.finance.util.StringUtils;
import com.jayud.finance.vo.*;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


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
    private KingdeeService kingdeeService;
    @Autowired
    private CommonService commonService;
    @Autowired
    KingdeeService service;
    @Autowired
    private ICurrencyRateService currencyRateService;
    @Autowired
    private FinanceService financeService;

    /**
     * 财务核算
     */
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
        response.setHeader("Content-Disposition", "attachment;filename=" + name + ".xlsx");

        writer.flush(out);
        writer.close();
        IoUtil.close(out);
    }

    /**
     * 对账单管理
     */
    @ApiOperation(value = "应付对账单审核列表,对账单明细")
    @PostMapping("/findFBillAuditByPage")
    public CommonResult<Map<String, Object>> findFBillAuditByPage(@RequestBody @Valid QueryFBillAuditForm form) {
        IPage<PaymentNotPaidBillVO> pageList = paymentBillDetailService.findFBillAuditByPage(form);
        CommonPageResult<PaymentNotPaidBillVO> pageVO = new CommonPageResult(pageList);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("pageList", pageVO);//列表
        ViewBillVO viewBillVO = paymentBillDetailService.getViewBill(form.getBillNo());
        resultMap.put(CommonConstant.WHOLE_DATA, viewBillVO);//全局数据
        return CommonResult.success(resultMap);
    }

    @ApiOperation(value = "应收对账单审核列表,对账单明细")
    @PostMapping("/findSBillAuditByPage")
    public CommonResult<Map<String, Object>> findSBillAuditByPage(@RequestBody @Valid QueryFBillAuditForm form) {
        IPage<LinkedHashMap> pageList = receivableBillDetailService.findSBillAuditByPage(form);
        CommonPageResult<PaymentNotPaidBillVO> pageVO = new CommonPageResult(pageList);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("pageList", pageVO);//列表
        ViewBillVO viewBillVO = receivableBillDetailService.getViewSBill(form.getBillNo());
        resultMap.put(CommonConstant.WHOLE_DATA, viewBillVO);//全局数据
        return CommonResult.success(resultMap);
    }

    @ApiOperation(value = "导出应付对账单明细列表")
    @RequestMapping(value = "/exportFBillDetailList", method = RequestMethod.GET)
    @ResponseBody
    public void exportFBillDetailList(QueryFBillAuditForm form,
                                      HttpServletResponse response) throws IOException {
        List<PaymentNotPaidBillVO> list = paymentBillDetailService.findFBillAudit(form);
//        ExcelWriter writer = ExcelUtil.getWriter(true);
//
//        //自定义标题别名
//        writer.addHeaderAlias("orderNo", "订单编号");
//        writer.addHeaderAlias("subOrderNo", "子订单编号");
//        writer.addHeaderAlias("billNo", "账单编号");
//        writer.addHeaderAlias("bizCodeDesc", "业务类型");
//        writer.addHeaderAlias("createdTimeStr", "日期");
//        writer.addHeaderAlias("supplierChName", "供应商");
//        writer.addHeaderAlias("goodsDesc", "货物信息");
//        writer.addHeaderAlias("startAddress", "起运地");
//        writer.addHeaderAlias("endAddress", "目的地");
//        writer.addHeaderAlias("licensePlate", "车牌号");
//        writer.addHeaderAlias("yunCustomsNo", "报关单号");
//        writer.addHeaderAlias("costTypeName", "费用类别");
//        writer.addHeaderAlias("costGenreName", "费用类型");
//        writer.addHeaderAlias("costName", "费用名称");
//        writer.addHeaderAlias("rmb", "人民币");
//        writer.addHeaderAlias("dollar", "美元");
//        writer.addHeaderAlias("euro", "欧元");
//        writer.addHeaderAlias("hKDollar", "港币");
//        writer.addHeaderAlias("taxRate", "税率");
//        writer.addHeaderAlias("remarks", "费用备注");
//        writer.addHeaderAlias("settlementCurrency", "结算币种");
//        writer.addHeaderAlias("settlementAmount", "结算金额");
//        writer.addHeaderAlias("exchangeRate", "汇率");
//
//
//        // 一次性写出内容，使用默认样式，强制输出标题
//        writer.write(list, true);
//
//        //out为OutputStream，需要写出到的目标流
//        ServletOutputStream out = response.getOutputStream();
//        String name = StringUtils.toUtf8String("客户应付对账单");
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
//        response.setHeader("Content-Disposition", "attachment;filename=" + name + ".xlsx");
//
//        writer.flush(out);
//        writer.close();
//        IoUtil.close(out);
        JSONArray datas = new JSONArray(list);
        LinkedHashMap<String, String> headMap = new LinkedHashMap<>();
        headMap.put("orderNo", "订单编号");
        headMap.put("subOrderNo", "子订单编号");
        headMap.put("billNo", "账单编号");
        headMap.put("bizCodeDesc", "业务类型");
        headMap.put("createdTimeStr", "日期");
//        headMap.put("supplierChName", "供应商");

        if (list.size() != 0 && SubOrderSignEnum.KY.getSignOne().equals(list.get(0).getSubType())) {
            String cmd = list.get(0).getSubType();
            List<String> mainOrderNos = list.stream().map(PaymentNotPaidBillVO::getOrderNo).collect(Collectors.toList());
            datas = this.commonService.templateDataProcessing(cmd, cmd, datas, mainOrderNos, 1);
            List<Map<String, Object>> maps = Utilities.assembleEntityHead(BillTemplateEnum.getTemplate(cmd), false);
            for (Map<String, Object> map : maps) {
                headMap.put(String.valueOf(map.get("name")), String.valueOf(map.get("viewName")));
            }
        } else {
            headMap.put("goodsDesc", "货物信息");
            headMap.put("startAddress", "起运地");
            headMap.put("endAddress", "目的地");
            headMap.put("licensePlate", "车牌号");
        }
        headMap.put("yunCustomsNo", "报关单号");
        headMap.put("costTypeName", "费用类别");
        headMap.put("costGenreName", "费用类型");
        headMap.put("costName", "费用名称");
        headMap.put("rmb", "人民币");
        headMap.put("dollar", "美元");
        headMap.put("euro", "欧元");
        headMap.put("hKDollar", "港币");
        headMap.put("taxRate", "税率");
        headMap.put("remarks", "费用备注");
        headMap.put("settlementCurrency", "结算币种");
        headMap.put("settlementAmount", "结算金额");
        headMap.put("exchangeRate", "汇率");
        EasyExcelEntity entity = new EasyExcelEntity();
        entity.setTableHead(headMap);
        entity.setTableData(datas);
        Workbook workbook = EasyExcelUtils.autoGeneration("", entity);

        ServletOutputStream out = response.getOutputStream();
        String name = StringUtils.toUtf8String("客户应付对账单");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + name + ".xlsx");

        workbook.write(out);
        workbook.close();
        IoUtil.close(out);
    }

    @ApiOperation(value = "导出应收对账单明细列表")
    @RequestMapping(value = "/exportSBillDetailList", method = RequestMethod.GET)
    @ResponseBody
    public void exportSBillDetailList(QueryFBillAuditForm form,
                                      HttpServletResponse response) throws IOException {
        List<PaymentNotPaidBillVO> list = receivableBillDetailService.findSBillAudit(form);
//        ExcelWriter writer = ExcelUtil.getWriter(true);
//
//        //自定义标题别名
//        writer.addHeaderAlias("orderNo", "订单编号");
//        writer.addHeaderAlias("subOrderNo", "子订单编号");
//        writer.addHeaderAlias("billNo", "账单编号");
//        writer.addHeaderAlias("bizCodeDesc", "业务类型");
//        writer.addHeaderAlias("createdTimeStr", "日期");
//        writer.addHeaderAlias("customerName", "客户");
//        writer.addHeaderAlias("goodsDesc", "货物信息");
//        writer.addHeaderAlias("startAddress", "起运地");
//        writer.addHeaderAlias("endAddress", "目的地");
//        writer.addHeaderAlias("licensePlate", "车牌号");
//        writer.addHeaderAlias("yunCustomsNo", "报关单号");
//        writer.addHeaderAlias("costTypeName", "费用类别");
//        writer.addHeaderAlias("costGenreName", "费用类型");
//        writer.addHeaderAlias("costName", "费用名称");
//        writer.addHeaderAlias("rmb", "人民币");
//        writer.addHeaderAlias("dollar", "美元");
//        writer.addHeaderAlias("euro", "欧元");
//        writer.addHeaderAlias("hKDollar", "港币");
//        writer.addHeaderAlias("taxRate", "税率");
//        writer.addHeaderAlias("remarks", "费用备注");
//        writer.addHeaderAlias("settlementCurrency", "结算币种");
//        writer.addHeaderAlias("settlementAmount", "结算金额");
//        writer.addHeaderAlias("exchangeRate", "汇率");
//
//
//        // 一次性写出内容，使用默认样式，强制输出标题
//        writer.write(list, true);
//
//        //out为OutputStream，需要写出到的目标流
//        ServletOutputStream out = response.getOutputStream();
//        String name = StringUtils.toUtf8String("客户应收对账单");
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
//        response.setHeader("Content-Disposition", "attachment;filename=" + name + ".xlsx");
//
//        writer.flush(out);
//        writer.close();
//        IoUtil.close(out);
        JSONArray datas = new JSONArray(list);
        LinkedHashMap<String, String> headMap = new LinkedHashMap<>();
        headMap.put("orderNo", "订单编号");
        headMap.put("subOrderNo", "子订单编号");
        headMap.put("billNo", "账单编号");
        headMap.put("bizCodeDesc", "业务类型");
        headMap.put("createdTimeStr", "日期");
        if (list.size() != 0 && SubOrderSignEnum.KY.getSignOne().equals(list.get(0).getSubType())) {
            String cmd = list.get(0).getSubType();
            List<String> mainOrderNos = list.stream().map(PaymentNotPaidBillVO::getOrderNo).collect(Collectors.toList());
            datas = this.commonService.templateDataProcessing(cmd, cmd, datas, mainOrderNos, 0);
            List<Map<String, Object>> maps = Utilities.assembleEntityHead(BillTemplateEnum.getTemplate(cmd), false);
            for (Map<String, Object> map : maps) {
                headMap.put(String.valueOf(map.get("name")), String.valueOf(map.get("viewName")));
            }
        } else {
            headMap.put("unitAccount", "结算单位");
            headMap.put("goodsDesc", "货物信息");
            headMap.put("startAddress", "起运地");
            headMap.put("endAddress", "目的地");
            headMap.put("licensePlate", "车牌号");
            headMap.put("yunCustomsNo", "报关单号");
        }
        headMap.put("costTypeName", "费用类别");
        headMap.put("costGenreName", "费用类型");
        headMap.put("costName", "费用名称");
        headMap.put("rmb", "人民币");
        headMap.put("dollar", "美元");
        headMap.put("euro", "欧元");
        headMap.put("hKDollar", "港币");
        headMap.put("taxRate", "税率");
        headMap.put("remarks", "费用备注");
        headMap.put("settlementCurrency", "结算币种");
        headMap.put("settlementAmount", "结算金额");
        headMap.put("exchangeRate", "汇率");
        EasyExcelEntity entity = new EasyExcelEntity();
        entity.setTableHead(headMap);
        entity.setTableData(datas);
        Workbook workbook = EasyExcelUtils.autoGeneration("", entity);

        ServletOutputStream out = response.getOutputStream();
        String name = StringUtils.toUtf8String("客户应收对账单");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + name + ".xlsx");

        workbook.write(out);
        workbook.close();
        IoUtil.close(out);
    }

    @ApiOperation(value = "核销列表")
    @PostMapping("/heXiaoList")
    public CommonResult<List<HeXiaoListVO>> heXiaoList(@RequestBody Map<String, Object> param) {
        String billNo = MapUtil.getStr(param, "billNo");
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
    public CommonResult<List<FCostVO>> findFCostList(@RequestBody Map<String, Object> param) {
        String billNo = MapUtil.getStr(param, "billNo");
        List<FCostVO> fCostVOS = paymentBillDetailService.findFCostList(billNo);
        return CommonResult.success(fCostVOS);
    }

    @ApiOperation(value = "开票审核列表 billNo = 账单编号")
    @PostMapping("/findSCostList")
    public CommonResult<List<FCostVO>> findSCostList(@RequestBody Map<String, Object> param) {
        String billNo = MapUtil.getStr(param, "billNo");
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
    public CommonResult<List<MakeInvoiceVO>> findInvoiceList(@RequestBody Map<String, Object> param) {
        String billNo = MapUtil.getStr(param, "billNo");
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
    public CommonResult makeInvoiceDel(@RequestBody Map<String, Object> param) {
        Long inVoiceId = Long.parseLong(MapUtil.getStr(param, "invoiceId"));
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
//        StringBuilder sb = new StringBuilder("账单编号:");
//        Boolean flag = false;
//        for (String billNo : form.getBillNos()) {
//            QueryWrapper queryWrapper1 = new QueryWrapper();
//            queryWrapper1.eq("bill_no", billNo);
//            List<OrderReceivableBillDetail> tempObjects = receivableBillDetailService.list(queryWrapper1);
//            if (tempObjects != null && tempObjects.size() > 0) {
//                OrderReceivableBillDetail tempObject = tempObjects.get(0);
//                if (StringUtil.isNullOrEmpty(tempObject.getAuditStatus())
//                        || (!BillEnum.B_6.getCode().equals(tempObject.getAuditStatus())
//                        && !BillEnum.B_4.getCode().equals(tempObject.getAuditStatus()))) {
//                    flag = true;
//                    sb.append(tempObject.getBillNo() + ";");
//                }
//            }
//        }
//        sb.append("财务未审核通过,不能推送金蝶");
//        if (flag) {
//            return CommonResult.error(10001, sb.toString());
//        }

        //构建数据，推金蝶
        QueryWrapper<OrderReceivableBillDetail> queryWrapper = new QueryWrapper();
        queryWrapper.in("bill_no", form.getBillNos());
        queryWrapper.lambda().groupBy(OrderReceivableBillDetail::getBillNo);
        List<OrderReceivableBillDetail> receivableBillDetails = receivableBillDetailService.list(queryWrapper);
        //检查是否能推金蝶
        financeService.isPushKingdee(receivableBillDetails, 2);

        //删除返回信息
        Map<String, String> deleteErr = new HashMap<>();

        for (OrderReceivableBillDetail receivableBillDetail : receivableBillDetails) {
            //查询结算币种的币种管理汇率(结算币种兑换人民币汇率)
            Map<String, BigDecimal> exchangeRate = this.currencyRateService.getExchangeRates("CNY", receivableBillDetail.getAccountTerm());
            //如果本次推送没有应付数据，需要查看是否存在本单号的应收，如有，要删去
            Map<String, String> error = this.kingdeeService.deleteOrder(receivableBillDetail.getBillNo(), 0);
            deleteErr.putAll(error);
            if (error.size() > 0) {
                continue;
            }

            List<ReceivableHeaderForm> reqForm = receivableBillDetailService.getReceivableHeaderForm(receivableBillDetail.getBillNo());
            CommonResult result = new CommonResult();
            for (ReceivableHeaderForm tempReqForm : reqForm) {
                //设置汇率
                tempReqForm.setExchangeRate(exchangeRate.get(tempReqForm.getCurrency()));
                List<APARDetailForm> entityDetail = receivableBillDetailService.findReceivableHeaderDetail(tempReqForm.getBillNo(), tempReqForm.getBusinessNo());
                entityDetail.forEach(e -> e.assemblyPriceQty(e.getPriceQty()));
                tempReqForm.setEntityDetail(entityDetail);
                logger.info("推送金蝶传参:" + reqForm);
                result = service.saveReceivableBill(FormIDEnum.RECEIVABLE.getFormid(), tempReqForm);
            }

            if (result.getCode() == null) continue;

            if (result.getCode() == 0) {
                OrderReceivableBillDetail tempObject = new OrderReceivableBillDetail();
                Integer num = receivableBillDetail.getPushKingdeeCount() == null ? 0 : receivableBillDetail.getPushKingdeeCount();
                tempObject.setPushKingdeeCount(num + 1);
                QueryWrapper updateWrapper = new QueryWrapper();
                updateWrapper.eq("bill_no", receivableBillDetail.getBillNo());
                receivableBillDetailService.update(tempObject, updateWrapper);
            } else {
                return CommonResult.error(400, result.getMsg());
            }
        }

        if (deleteErr.size() > 0) {
            StringBuilder msg = new StringBuilder();
            deleteErr.forEach((k, v) -> msg.append(v).append("\n"));
            System.out.println(msg.toString());
            return CommonResult.error(400, msg.toString());
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
//        StringBuilder sb = new StringBuilder("账单编号:");
//        Boolean flag = false;
//        for (String billNo : form.getBillNos()) {
//            QueryWrapper queryWrapper1 = new QueryWrapper();
//            queryWrapper1.eq("bill_no", billNo);
//            List<OrderPaymentBillDetail> tempObjects = receivableBillDetailService.list(queryWrapper1);
//            if (tempObjects != null && tempObjects.size() > 0) {
//                OrderPaymentBillDetail tempObject = tempObjects.get(0);
//                if (StringUtil.isNullOrEmpty(tempObject.getAuditStatus())
//                        || !BillEnum.B_6.getCode().equals(tempObject.getAuditStatus())) {
//                    flag = true;
//                    sb.append(tempObject.getBillNo() + ";");
//                }
//            }
//        }
//        sb.append("财务未审核通过,不能推送金蝶");
//        if (flag) {
//            return CommonResult.error(10001, sb.toString());
//        }


        //构建数据，推金蝶
        QueryWrapper<OrderPaymentBillDetail> queryWrapper = new QueryWrapper();
        queryWrapper.in("bill_no", form.getBillNos());
        queryWrapper.lambda().groupBy(OrderPaymentBillDetail::getBillNo);
        List<OrderPaymentBillDetail> paymentBillDetailList = paymentBillDetailService.list(queryWrapper);
        financeService.isPushKingdee(paymentBillDetailList, 1);

        //删除返回信息
        Map<String, String> deleteErr = new HashMap<>();

        for (OrderPaymentBillDetail paymentBillDetail : paymentBillDetailList) {
            //查询结算币种的币种管理汇率
            Map<String, BigDecimal> exchangeRate = this.currencyRateService.getExchangeRates("CNY", paymentBillDetail.getAccountTerm());
            //如果本次推送没有应付数据，需要查看是否存在本单号的应付，如有，要删去
            Map<String, String> error = this.kingdeeService.deleteOrder(paymentBillDetail.getBillNo(), 1);
            deleteErr.putAll(error);
            if (error.size() > 0) {
                continue;
            }

            List<PayableHeaderForm> reqForm = paymentBillDetailService.getPayableHeaderForm(paymentBillDetail.getBillNo());
            CommonResult result = new CommonResult();
            for (PayableHeaderForm tempReqForm : reqForm) {
                //设置汇率
                tempReqForm.setExchangeRate(exchangeRate.get(tempReqForm.getCurrency()));
                List<APARDetailForm> entityDetail = paymentBillDetailService.findPayableHeaderDetail(tempReqForm.getBillNo(), tempReqForm.getBusinessNo());
                entityDetail.forEach(e -> e.assemblyPriceQty(e.getPriceQty()));
                tempReqForm.setEntityDetail(entityDetail);
                logger.info("推送金蝶传参:" + reqForm);

                result = service.savePayableBill(FormIDEnum.PAYABLE.getFormid(), tempReqForm);
            }

            if (result.getCode() == null) continue;

            if (result.getCode() == 0) {
                OrderPaymentBillDetail tempObject = new OrderPaymentBillDetail();
                tempObject.setPushKingdeeCount(paymentBillDetail.getPushKingdeeCount() + 1);
                QueryWrapper updateWrapper = new QueryWrapper();
                updateWrapper.eq("bill_no", paymentBillDetail.getBillNo());
                paymentBillDetailService.update(tempObject, updateWrapper);
            } else {
                return CommonResult.error(400, result.getMsg());
            }
        }

        if (deleteErr.size() > 0) {
            StringBuilder msg = new StringBuilder();
            deleteErr.forEach((k, v) -> msg.append(v).append("\n"));
            return CommonResult.error(400, msg.toString());
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "费用状态下拉框")
    @PostMapping(value = "/initBillStatus")
    public CommonResult<List<InitComboxStrVO>> initBillStatus() {
        List<InitComboxStrVO> comboxStrVOS = new ArrayList<>();
        for (BillEnum billEnum : BillEnum.values()) {
            if (BillEnum.B_1.getCode().equals(billEnum.getCode()) || BillEnum.B_2.getCode().equals(billEnum.getCode()) ||
                    BillEnum.B_2_1.getCode().equals(billEnum.getCode()) || BillEnum.B_3.getCode().equals(billEnum.getCode()) ||
                    BillEnum.B_4.getCode().equals(billEnum.getCode()) || BillEnum.B_4_1.getCode().equals(billEnum.getCode()) ||
                    BillEnum.B_5.getCode().equals(billEnum.getCode()) || BillEnum.B_5_1.getCode().equals(billEnum.getCode()) ||
                    BillEnum.B_6.getCode().equals(billEnum.getCode()) || BillEnum.B_6_1.getCode().equals(billEnum.getCode()) ||
                    BillEnum.B_7.getCode().equals(billEnum.getCode()) || BillEnum.B_8.getCode().equals(billEnum.getCode()) ||
                    BillEnum.B_9.getCode().equals(billEnum.getCode())) {
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
    public CommonResult<CostAmountVO> getCostAmountView(@RequestBody Map<String, Object> param) {
        String billNo = MapUtil.getStr(param, "billNo");
        if (StringUtil.isNullOrEmpty(billNo)) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        CostAmountVO costAmountVO = new CostAmountVO();
        CostAmountVO costFAmountVO = paymentBillDetailService.getFCostAmountView(billNo);
        CostAmountVO costSAmountVO = receivableBillDetailService.getSCostAmountView(billNo);
        costAmountVO.setBillNo(billNo);
        if (costSAmountVO != null) {
            costAmountVO.setYsAmount(costSAmountVO.getYsAmount());
            costAmountVO.setYsCurrency(costSAmountVO.getYsCurrency());
            costAmountVO.setWsAmount(costSAmountVO.getWsAmount());
            costAmountVO.setWsCurrency(costSAmountVO.getWsCurrency());
        }
        if (costFAmountVO != null) {
            costAmountVO.setYfAmount(costFAmountVO.getYfAmount());
            costAmountVO.setYfCurrency(costFAmountVO.getYfCurrency());
            costAmountVO.setDfAmount(costFAmountVO.getDfAmount());
            costAmountVO.setDfCurrency(costFAmountVO.getDfCurrency());
        }
        return CommonResult.success(costAmountVO);
    }


}
