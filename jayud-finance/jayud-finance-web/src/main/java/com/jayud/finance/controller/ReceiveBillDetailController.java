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
import com.jayud.common.utils.ConvertUtil;
import com.jayud.finance.bo.*;
import com.jayud.finance.enums.BillEnum;
import com.jayud.finance.po.OrderReceivableBillDetail;
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
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/receiveBillDetail/")
@Api(tags = "应收对账")
public class ReceiveBillDetailController {

    @Autowired
    IOrderReceivableBillDetailService billDetailService;

    @ApiOperation(value = "应收对账单列表,应收对账单审核列表,财务应收对账单列表")
    @PostMapping("/findReceiveBillDetailByPage")
    public CommonResult<CommonPageResult<OrderPaymentBillDetailVO>> findReceiveBillDetailByPage(@RequestBody @Valid QueryPaymentBillDetailForm form) {
        IPage<OrderPaymentBillDetailVO> pageList = billDetailService.findReceiveBillDetailByPage(form);
        CommonPageResult<OrderPaymentBillDetailVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "提交财务 billNos=账单编号集合,必须客服主管审核通过,状态为B_2")
    @PostMapping("/submitSCw")
    public CommonResult submitSCw(@RequestBody @Valid ListForm form){
        return billDetailService.submitSCw(form);
    }

    @ApiOperation(value = "导出应收对账单列表")
    @RequestMapping(value = "/exportSBill", method = RequestMethod.GET)
    @ResponseBody
    public void exportSBill(QueryPaymentBillDetailForm form,
                                 HttpServletResponse response) throws IOException {
        //获取数据
        List<OrderPaymentBillDetailVO> initList = billDetailService.findReceiveBillDetail(form);
        List<ExportOrderSBillDetailVO> list = ConvertUtil.convertList(initList,ExportOrderSBillDetailVO.class);

        ExcelWriter writer = ExcelUtil.getWriter(true);

        //自定义标题别名
        writer.addHeaderAlias("billNo", "账单编号");
        writer.addHeaderAlias("legalName", "法人主体");
        writer.addHeaderAlias("customerName", "客服");
        writer.addHeaderAlias("beginAccountTermStr", "开始核算期");
        writer.addHeaderAlias("endAccountTermStr", "结束核算期");
        writer.addHeaderAlias("rmb", "人民币");
        writer.addHeaderAlias("dollar", "美元");
        writer.addHeaderAlias("euro", "欧元");
        writer.addHeaderAlias("hKDollar", "港币");
        writer.addHeaderAlias("heXiaoAmount", "已付金额");
        writer.addHeaderAlias("notHeXiaoAmount", "未付金额");
        writer.addHeaderAlias("settlementCurrency", "结算币种");
        writer.addHeaderAlias("auditStatus", "状态");
        writer.addHeaderAlias("applyStatus", "付款申请");
        writer.addHeaderAlias("makeUser", "制单人");
        writer.addHeaderAlias("makeTimeStr", "制单时间");
        writer.addHeaderAlias("auditUser", "审核人");
        writer.addHeaderAlias("auditTimeStr", "审核时间");
        writer.addHeaderAlias("auditComment", "审核意见");
        writer.addHeaderAlias("heXiaoUser", "核销人");
        writer.addHeaderAlias("heXiaoTimeStr", "核销时间");

        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(list, true);

        //out为OutputStream，需要写出到的目标流
        ServletOutputStream out = response.getOutputStream();
        String name = StringUtils.toUtf8String("应收对账单");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename="+name+".xlsx");

        writer.flush(out);
        writer.close();
        IoUtil.close(out);
    }


    @ApiOperation(value = "开票申请")
    @PostMapping("/applyInvoice")
    public CommonResult applyInvoice(@RequestBody @Valid ApplyInvoiceForm form) {
        //1.财务对账审核通过
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("bill_no",form.getBillNo());
        List<OrderReceivableBillDetail> orderReceivableBillDetails = billDetailService.list(queryWrapper);
        OrderReceivableBillDetail orderReceivableBillDetail = orderReceivableBillDetails.get(0);
        if(orderReceivableBillDetail != null && !BillEnum.B_4.getCode().equals(orderReceivableBillDetail.getAuditStatus())){
            return CommonResult.error(10000,"不满足付款申请的条件");
        }
        Boolean result = billDetailService.applyInvoice(form);
        if(!result){
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "开票申请作废,billNo=列表里面取")
    @PostMapping("/applyInvoiceCancel")
    public CommonResult applyInvoiceCancel(@RequestBody Map<String,Object> param) {
        String billNo = MapUtil.getStr(param, "billNo");
        //1.财务开票申请审核不通过
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("bill_no",billNo);
        List<OrderReceivableBillDetail> orderReceivableBillDetails = billDetailService.list(queryWrapper);
        OrderReceivableBillDetail orderReceivableBillDetail = orderReceivableBillDetails.get(0);
        if(orderReceivableBillDetail != null && !BillEnum.B_6_1.getCode().equals(orderReceivableBillDetail.getAuditStatus())){
            return CommonResult.error(10000,"不满足付款申请作废的条件");
        }
        Boolean result = billDetailService.applyInvoiceCancel(billNo);
        if(!result){
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "编辑对账单列表,费用维度的")
    @PostMapping("/findEditSBillByPage")
    public CommonResult<CommonPageResult<PaymentNotPaidBillVO>> findEditSBillByPage(@RequestBody QueryEditBillForm form) {
        IPage<PaymentNotPaidBillVO> pageList = billDetailService.findEditSBillByPage(form);
        CommonPageResult<PaymentNotPaidBillVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "客服编辑对账单保存,财务编辑对账单")
    @PostMapping("/editSBill")
    public CommonResult editSBill(@RequestBody EditSBillForm form) {
        Boolean result = billDetailService.editSBill(form);
        if(!result){
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "对账单详情，对账单审核详情")
    @PostMapping("/viewSBillDetail")
    public CommonResult<Map<String,Object>> viewSBillDetail(@RequestBody @Valid ViewBillDetailForm form) {
        Map<String,Object> resultMap = new HashMap<>();
        List<ViewBilToOrderVO> list = billDetailService.viewSBillDetail(form.getBillNo());
        resultMap.put(CommonConstant.LIST,list);//分页数据
        List<SheetHeadVO> sheetHeadVOS = billDetailService.findSSheetHead(form.getBillNo());
        resultMap.put(CommonConstant.SHEET_HEAD,sheetHeadVOS);//表头
        ViewBillVO viewBillVO = billDetailService.getViewSBill(form.getBillNo());
        resultMap.put(CommonConstant.WHOLE_DATA,viewBillVO);//全局数据
        return CommonResult.success(resultMap);
    }


    @ApiOperation(value = "导出对账单详情,待开发")
    @RequestMapping(value = "/exportSBillDetail", method = RequestMethod.GET)
    @ResponseBody
    public void exportSBillDetail(@RequestParam(value = "billNo",required=true) String billNo,
                                   HttpServletResponse response) throws IOException {
        List<ViewBilToOrderVO> list = billDetailService.viewSBillDetail(billNo);
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
        List<SheetHeadVO> sheetHeadVOS = billDetailService.findSSheetHead(billNo);
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
        String name = StringUtils.toUtf8String("客户应收对账单");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename="+name+".xlsx");

        writer.flush(out);
        writer.close();
        IoUtil.close(out);
    }

    /***
     * 对账单审核模块
     * @param form
     * @return
     */
    @ApiOperation(value = "应收对账单审核,财务对账单审核")
    @PostMapping("/billSAudit")
    public CommonResult billSAudit(@RequestBody BillAuditForm form) {
        return billDetailService.billSAudit(form);
    }

    @ApiOperation(value = "导出应收对账单审核列表")
    @RequestMapping(value = "/exportAuditSBill", method = RequestMethod.GET)
    @ResponseBody
    public void exportAuditSBill(QueryPaymentBillDetailForm form,
                           HttpServletResponse response) throws IOException {
        //获取数据
        List<OrderPaymentBillDetailVO> initList = billDetailService.findReceiveBillDetail(form);
        List<ExportOrderSBillDetailVO> list = ConvertUtil.convertList(initList,ExportOrderSBillDetailVO.class);

        ExcelWriter writer = ExcelUtil.getWriter(true);

        //自定义标题别名
        writer.addHeaderAlias("billNo", "账单编号");
        writer.addHeaderAlias("legalName", "法人主体");
        writer.addHeaderAlias("customerName", "客服");
        writer.addHeaderAlias("beginAccountTermStr", "开始核算期");
        writer.addHeaderAlias("endAccountTermStr", "结束核算期");
        writer.addHeaderAlias("rmb", "人民币");
        writer.addHeaderAlias("dollar", "美元");
        writer.addHeaderAlias("euro", "欧元");
        writer.addHeaderAlias("hKDollar", "港币");
        writer.addHeaderAlias("heXiaoAmount", "已付金额");
        writer.addHeaderAlias("notHeXiaoAmount", "未付金额");
        writer.addHeaderAlias("settlementCurrency", "结算币种");
        writer.addHeaderAlias("auditStatus", "状态");
        writer.addHeaderAlias("applyStatus", "付款申请");
        writer.addHeaderAlias("makeUser", "制单人");
        writer.addHeaderAlias("makeTimeStr", "制单时间");
        writer.addHeaderAlias("auditUser", "审核人");
        writer.addHeaderAlias("auditTimeStr", "审核时间");
        writer.addHeaderAlias("auditComment", "审核意见");
        writer.addHeaderAlias("heXiaoUser", "核销人");
        writer.addHeaderAlias("heXiaoTimeStr", "核销时间");

        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(list, true);

        //out为OutputStream，需要写出到的目标流
        ServletOutputStream out = response.getOutputStream();
        String name = StringUtils.toUtf8String("应收对账单审核列表");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename="+name+".xlsx");

        writer.flush(out);
        writer.close();
        IoUtil.close(out);
    }

    /**
     * 1.客服主管-应收反审核 kf_s_reject
     * 2.客服主管-应付反审核 kf_f_reject
     * 3.财务-应收反审核 cw_s_reject
     * 4.财务-应付反审核 cw_f_reject
     * @param form
     * @return
     */
    @ApiOperation(value = "反审核,billNos=账单编号集合")
    @PostMapping("/contrarySAudit")
    public CommonResult contrarySAudit(@RequestBody ListForm form) {
        if(form.getBillNos() == null || form.getBillNos().size() == 0){
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
       return billDetailService.contrarySAudit(form);
    }

}
