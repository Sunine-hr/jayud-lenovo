package com.jayud.finance.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.excel.EasyExcelEntity;
import com.jayud.common.utils.excel.EasyExcelUtils;
import com.jayud.finance.bo.*;
import com.jayud.finance.enums.BillEnum;
import com.jayud.finance.po.OrderPaymentBillDetail;
import com.jayud.finance.service.IOrderPaymentBillDetailService;
import com.jayud.finance.util.StringUtils;
import com.jayud.finance.vo.*;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;


@RestController
@RequestMapping("/paymentBillDetail/")
@Api(tags = "应付对账")
public class PaymentBillDetailController {

    @Autowired
    IOrderPaymentBillDetailService billDetailService;

    @ApiOperation(value = "应付对账单列表,应付对账单审核列表,财务应付对账单列表")
    @PostMapping("/findPaymentBillDetailByPage")
    public CommonResult<CommonPageResult<OrderPaymentBillDetailVO>> findPaymentBillDetailByPage(@RequestBody @Valid QueryPaymentBillDetailForm form) {
        IPage<OrderPaymentBillDetailVO> pageList = billDetailService.findPaymentBillDetailByPage(form);
        CommonPageResult<OrderPaymentBillDetailVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "提交财务 billNos=账单编号集合,必须客服主管审核通过,状态为B_2")
    @PostMapping("/submitFCw")
    public CommonResult submitFCw(@RequestBody ListForm form){
        return billDetailService.submitFCw(form);
    }

    @ApiOperation(value = "导出应付对账单列表,导出财务应付对账单列表")
    @RequestMapping(value = "/exportFBill", method = RequestMethod.GET)
    @ResponseBody
    public void exportFBill(QueryPaymentBillDetailForm form,
                                 HttpServletResponse response) throws IOException {
        //获取数据
        List<OrderPaymentBillDetailVO> initList = billDetailService.findPaymentBillDetail(form);
        List<ExportOrderFBillDetailVO> list = ConvertUtil.convertList(initList,ExportOrderFBillDetailVO.class);

        ExcelWriter writer = ExcelUtil.getWriter(true);

        //自定义标题别名
        writer.addHeaderAlias("billNo", "账单编号");
        writer.addHeaderAlias("legalName", "法人主体");
        writer.addHeaderAlias("supplierChName", "供应商");
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
        writer.addHeaderAlias("pushKingdeeCount", "推金蝶次数");//导出财务应付对账单列表才有

        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(list, true);

        //out为OutputStream，需要写出到的目标流
        ServletOutputStream out = response.getOutputStream();
        String name = StringUtils.toUtf8String("应付对账单");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename="+name+".xlsx");

        writer.flush(out);
        writer.close();
        IoUtil.close(out);
    }


    @ApiOperation(value = "付款申请")
    @PostMapping("/applyPayment")
    public CommonResult applyPayment(@RequestBody @Valid ApplyPaymentForm form) {
        //1.财务对账审核通过
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("bill_no",form.getBillNo());
        List<OrderPaymentBillDetail> orderPaymentBillDetails = billDetailService.list(queryWrapper);
        OrderPaymentBillDetail orderPaymentBillDetail = orderPaymentBillDetails.get(0);
        if(orderPaymentBillDetail != null && !BillEnum.B_4.getCode().equals(orderPaymentBillDetail.getAuditStatus())){
            return CommonResult.error(10000,"不满足付款申请的条件");
        }
        Boolean result = billDetailService.applyPayment(form);
        if(!result){
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "付款申请作废,billNo=列表里面取")
    @PostMapping("/applyPaymentCancel")
    public CommonResult applyPaymentCancel(@RequestBody Map<String,Object> param) {
        String billNo = MapUtil.getStr(param, "billNo");
        //1.财务付款申请审核不通过
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("bill_no",billNo);
        List<OrderPaymentBillDetail> billDetails = billDetailService.list(queryWrapper);
        OrderPaymentBillDetail billDetail = billDetails.get(0);
        if(billDetail != null && !BillEnum.B_6_1.getCode().equals(billDetail.getAuditStatus())){
            return CommonResult.error(10000,"不满足付款申请作废的条件");
        }
        Boolean result = billDetailService.applyPaymentCancel(billNo);
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

    @ApiOperation(value = "编辑保存确定")
    @PostMapping("/editFSaveConfirm")
    public CommonResult editFSaveConfirm(@RequestBody EditBillForm form) {
        //参数校验
        List<OrderPaymentBillDetailForm> paymentBillDetailForms = form.getPaymentBillDetailForms();
        if(paymentBillDetailForms == null || paymentBillDetailForms.size() == 0){
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        List<Long> costIds = new ArrayList<>();
        for (OrderPaymentBillDetailForm formObject : paymentBillDetailForms) {
            costIds.add(formObject.getCostId());
        }
        Boolean result = billDetailService.editFSaveConfirm(costIds);
        if(!result){
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "编辑删除")
    @PostMapping("/editFDel")
    public CommonResult editFDel(@RequestBody EditBillForm form) {
        //参数校验
        List<OrderPaymentBillDetailForm> delCosts = form.getDelCosts();
        if(delCosts == null || delCosts.size() == 0){
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        List<Long> costIds = new ArrayList<>();
        for (OrderPaymentBillDetailForm formObject : delCosts) {
            costIds.add(formObject.getCostId());
        }
        Boolean result = billDetailService.editFDel(costIds);
        if(!result){
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "客服编辑对账单保存或提交,财务编辑对账单保存或提交")
    @PostMapping("/editBill")
    public CommonResult editBill(@RequestBody EditBillForm form) {
        //参数校验
        if(StringUtil.isNullOrEmpty(form.getBillNo()) || StringUtil.isNullOrEmpty(form.getCmd())
                || StringUtil.isNullOrEmpty(form.getLoginUserName()) ){
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        return billDetailService.editBill(form);
    }

    @ApiOperation(value = "对账单详情，对账单审核详情")
    @PostMapping("/viewBillDetail")
    public CommonResult<Map<String,Object>> viewBillDetail(@RequestBody @Valid ViewBillDetailForm form) {
        Map<String,Object> resultMap = new HashMap<>();
        List<ViewFBilToOrderVO> list = billDetailService.viewBillDetail(form.getBillNo());
        resultMap.put(CommonConstant.LIST,list);//分页数据
        List<SheetHeadVO> sheetHeadVOS = billDetailService.findSheetHead(form.getBillNo());
        resultMap.put(CommonConstant.SHEET_HEAD,sheetHeadVOS);//表头
        ViewBillVO viewBillVO = billDetailService.getViewBill(form.getBillNo());
        resultMap.put(CommonConstant.WHOLE_DATA,viewBillVO);//全局数据
        return CommonResult.success(resultMap);
    }


    @ApiOperation(value = "导出对账单详情,待开发")
    @RequestMapping(value = "/exportBillDetail", method = RequestMethod.GET)
    @ResponseBody
    public void exportBillDetail(@RequestParam(value = "billNo", required = true) String billNo,
                                 HttpServletResponse response) throws IOException {
        List<ViewFBilToOrderVO> list = billDetailService.viewBillDetail(billNo);

        TypeUtils.compatibleWithJavaBean = true;

        JSONArray datas = JSONArray.parseArray(JSON.toJSONString(list));

        ViewBillVO viewBillVO = billDetailService.getViewBill(billNo);

        //头部数据重组
        List<SheetHeadVO> sheetHeadVOS = billDetailService.findSheetHead(billNo);
        LinkedHashMap<String, String> headMap = new LinkedHashMap<>();
        LinkedHashMap<String, String> dynamicHead = new LinkedHashMap<>();
        for (int i = 0; i < sheetHeadVOS.size(); i++) {
            SheetHeadVO sheetHeadVO = sheetHeadVOS.get(i);
            headMap.put(sheetHeadVO.getName(), sheetHeadVO.getViewName());
            if (i > 9) {
                dynamicHead.put(sheetHeadVO.getName(), sheetHeadVO.getViewName());
            }
        }

        EasyExcelEntity entity = new EasyExcelEntity();
        entity.setSheetName("客户应付对账单");
        //组装标题
        List<String> titles = new ArrayList<>();
        titles.add(viewBillVO.getLegalName());
        titles.add("客户应收款对帐单");
        StringBuilder sb = new StringBuilder();
        titles.add(sb.append("对账日期:")
                .append(viewBillVO.getBeginAccountTermStr()).append("到")
                .append(viewBillVO.getEndAccountTermStr()).toString());
        entity.setTitle(titles);
        //组装台头
        List<String> stageHeads = new ArrayList<>();
        stageHeads.add("TO:" + viewBillVO.getCustomerName());
        sb = new StringBuilder();
        stageHeads.add(sb.append("FR:").append(viewBillVO.getLegalName()).append(EasyExcelUtils.SPLIT_SYMBOL)
                .append("账单编号:").append(viewBillVO.getBillNo()).toString());
        entity.setStageHead(stageHeads);
        //组装表头信息
        entity.setTableHead(headMap);
        //组装数据
        entity.setTableData(datas);
        //合计
        LinkedHashMap<String, BigDecimal> costTotal = new LinkedHashMap<>();

        for (int i = 0; i < datas.size(); i++) {
            JSONObject jsonObject = datas.getJSONObject(i);
            dynamicHead.forEach((k, v) -> {
                BigDecimal cost = jsonObject.getBigDecimal(k);
                if (costTotal.get(k) == null) {
                    costTotal.put(k, cost);
                } else {
                    costTotal.put(k, costTotal.get(k).add(cost == null ? new BigDecimal(0) : cost));
                }
            });

        }
        entity.setTotalData(costTotal);
        entity.setTotalIndex(9);


        //尾部
        List<String> bottomData = new ArrayList<>();
        bottomData.add(" " + EasyExcelUtils.SPLIT_SYMBOL + "制 单 人:" + viewBillVO.getMakeUser());
        bottomData.add(" " + EasyExcelUtils.SPLIT_SYMBOL + "制单时间:" + viewBillVO.getMakeTimeStr());
        bottomData.add(" " + EasyExcelUtils.SPLIT_SYMBOL + "审 单 人:" + viewBillVO.getMakeUser());
        bottomData.add(" " + EasyExcelUtils.SPLIT_SYMBOL + "审单时间:" + viewBillVO.getMakeTimeStr());
        entity.setBottomData(bottomData);

        Workbook workbook = EasyExcelUtils.autoGeneration("", entity);

//        Field[] s = ViewBilToOrderVO.class.getDeclaredFields();
//        int lastColumn = s.length-1;

//        // 合并单元格后的标题行，使用默认标题样式
//        writer.merge(lastColumn, "B类表:存在`敏感品名`的货物表");
//
//        // 一次性写出内容，使用默认样式，强制输出标题
//        writer.write(list, true);

        //out为OutputStream，需要写出到的目标流


        ServletOutputStream out = response.getOutputStream();
        String name = StringUtils.toUtf8String("客户应付对账单");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + name + ".xlsx");

        workbook.write(out);
        workbook.close();
        IoUtil.close(out);
    }


    /***
     * 对账单审核模块
     * @param form
     * @return
     */
    @ApiOperation(value = "应付对账单审核,财务对账单审核")
    @PostMapping("/billAudit")
    public CommonResult billAudit(@RequestBody BillAuditForm form) {
        return billDetailService.billAudit(form);
    }

    @ApiOperation(value = "导出应付对账单审核列表")
    @RequestMapping(value = "/exportAuditBill", method = RequestMethod.GET)
    @ResponseBody
    public void exportAuditBill(QueryPaymentBillDetailForm form,
                           HttpServletResponse response) throws IOException {
        //获取数据
        List<OrderPaymentBillDetailVO> initList = billDetailService.findPaymentBillDetail(form);
        List<ExportOrderFBillDetailVO> list = ConvertUtil.convertList(initList,ExportOrderFBillDetailVO.class);

        ExcelWriter writer = ExcelUtil.getWriter(true);

        //自定义标题别名
        writer.addHeaderAlias("billNo", "账单编号");
        writer.addHeaderAlias("legalName", "法人主体");
        writer.addHeaderAlias("supplierChName", "供应商");
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
        String name = StringUtils.toUtf8String("应付对账单审核列表");
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
    @PostMapping("/contraryAudit")
    public CommonResult contraryAudit(@RequestBody ListForm form) {
        if(form.getBillNos() == null || form.getBillNos().size() == 0){
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        return billDetailService.contraryAudit(form);
    }

}
