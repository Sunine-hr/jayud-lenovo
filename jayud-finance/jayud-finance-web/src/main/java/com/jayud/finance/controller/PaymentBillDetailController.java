package com.jayud.finance.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.enums.ResultEnum;
import com.jayud.finance.bo.*;
import com.jayud.finance.enums.BillEnum;
import com.jayud.finance.po.OrderPaymentBillDetail;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/paymentBillDetail/")
@Api(tags = "应付对账")
public class PaymentBillDetailController {

    @Autowired
    IOrderPaymentBillDetailService billDetailService;

    @ApiOperation(value = "应付对账单列表,应付对账单审核")
    @PostMapping("/findPaymentBillDetailByPage")
    public CommonResult<CommonPageResult<OrderPaymentBillDetailVO>> findPaymentBillDetailByPage(@RequestBody @Valid QueryPaymentBillDetailForm form) {
        IPage<OrderPaymentBillDetailVO> pageList = billDetailService.findPaymentBillDetailByPage(form);
        CommonPageResult<OrderPaymentBillDetailVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "付款申请")
    @PostMapping("/applyPayment")
    public CommonResult applyPayment(@RequestBody @Valid ApplyPaymentForm form) {
        //1.财务对账审核通过
        OrderPaymentBillDetail orderPaymentBillDetail = billDetailService.getById(form.getBillDetailId());
        if(orderPaymentBillDetail != null && !BillEnum.B_4.getCode().equals(orderPaymentBillDetail.getAuditStatus())){
            return CommonResult.error(10000,"不满足付款申请的条件");
        }
        Boolean result = billDetailService.applyPayment(form);
        if(!result){
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "付款申请作废,billDetailId=列表里面取")
    @PostMapping("/applyPaymentCancel")
    public CommonResult applyPaymentCancel(@RequestBody Map<String,Object> param) {
        Long billDetailId = Long.parseLong(MapUtil.getStr(param, "billDetailId"));
        //1.财务开票申请审核不通过
        OrderPaymentBillDetail orderPaymentBillDetail = billDetailService.getById(billDetailId);
        if(orderPaymentBillDetail != null && !BillEnum.B_6_1.getCode().equals(orderPaymentBillDetail.getAuditStatus())){
            return CommonResult.error(10000,"不满足付款申请作废的条件");
        }
        Boolean result = billDetailService.applyPaymentCancel(billDetailId);
        if(!result){
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "编辑对账单列表")
    @PostMapping("/findEditBillByPage")
    public CommonResult<CommonPageResult<PaymentNotPaidBillVO>> findEditBillByPage(@RequestBody QueryEditBillForm form) {
        IPage<PaymentNotPaidBillVO> pageList = billDetailService.findEditBillByPage(form);
        CommonPageResult<PaymentNotPaidBillVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "编辑对账单保存")
    @PostMapping("/editBill")
    public CommonResult editBill(@RequestBody EditBillForm form) {
        Boolean result = billDetailService.editBill(form);
        if(!result){
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "编辑对账单保存,billDetailId = 账单详情ID")
    @PostMapping("/editBillSubmit")
    public CommonResult editBillSubmit(@RequestBody Map<String,Object> param) {
        Long billDetailId = Long.parseLong(MapUtil.getStr(param,"billDetailId"));
        Boolean result = billDetailService.editBillSubmit(billDetailId);
        if(!result){
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "对账单详情")//出对账单后的
    @PostMapping("/viewBillDetail")
    public CommonResult<Map<String,Object>> viewBillDetail(@RequestBody @Valid ViewBillDetailForm form) {
        Map<String,Object> resultMap = new HashMap<>();
        List<ViewBilToOrderVO> list = billDetailService.viewBillDetail(form.getBillNo());
        resultMap.put(CommonConstant.LIST,list);//分页数据
        List<SheetHeadVO> sheetHeadVOS = billDetailService.findSheetHead(form.getBillNo());
        resultMap.put(CommonConstant.SHEET_HEAD,sheetHeadVOS);//表头
        ViewBillVO viewBillVO = billDetailService.getViewBill(form.getBillNo());
        resultMap.put(CommonConstant.WHOLE_DATA,viewBillVO);//全局数据
        return CommonResult.success(resultMap);
    }


    @ApiOperation(value = "到出对账单详情")//出对账单后的
    @RequestMapping(value = "/exportBillDetail", method = RequestMethod.GET)
    @ResponseBody
    public void exportBillDetail(@RequestParam(value = "billNo",required=true) String billNo,
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
        String name = StringUtils.toUtf8String("客户应付对账单");
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
    @ApiOperation(value = "应付对账单审核")
    @PostMapping("/billAudit")
    public CommonResult billAudit(@RequestBody BillAuditForm form) {
        Boolean result = billDetailService.billAudit(form);
        if(!result){
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        return CommonResult.success();
    }

    //导出对账单审核列表
    @ApiOperation(value = "到出对账单详情")
    @RequestMapping(value = "/exportBillAuditList", method = RequestMethod.GET)
    @ResponseBody
    public void exportBillAuditList(@RequestParam(value = "billNo",required=true) String billNo,
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
        String name = StringUtils.toUtf8String("应付对账单审核列表");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename="+name+".xlsx");

        writer.flush(out);
        writer.close();
        IoUtil.close(out);
    }

    @ApiOperation(value = "反审核,ids=账单详情billDetailId集合")
    @PostMapping("/contraryAudit")
    public CommonResult contraryAudit(@RequestBody ListForm form) {
        Boolean result = billDetailService.contraryAudit(form);
        if(!result){
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        return CommonResult.success();
    }

}
