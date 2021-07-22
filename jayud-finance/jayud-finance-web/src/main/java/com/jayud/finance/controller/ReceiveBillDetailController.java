package com.jayud.finance.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.excel.EasyExcelEntity;
import com.jayud.common.utils.excel.EasyExcelUtils;
import com.jayud.finance.bo.*;
import com.jayud.finance.enums.BillEnum;
import com.jayud.finance.feign.OauthClient;
import com.jayud.finance.feign.OmsClient;
import com.jayud.finance.po.OrderReceivableBillDetail;
import com.jayud.finance.service.CommonService;
import com.jayud.finance.service.IOrderBillCostTotalService;
import com.jayud.finance.service.IOrderReceivableBillDetailService;
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
import java.util.stream.Collectors;


@RestController
@RequestMapping("/receiveBillDetail/")
@Api(tags = "应收对账")
public class ReceiveBillDetailController {

    @Autowired
    IOrderReceivableBillDetailService billDetailService;
    @Autowired
    private OauthClient oauthClient;
    @Autowired
    private IOrderBillCostTotalService orderBillCostTotalService;
    @Autowired
    private OmsClient omsClient;
    @Autowired
    private CommonService commonService;

    @ApiOperation(value = "应收对账单列表,应收对账单审核列表,财务应收对账单列表")
    @PostMapping("/findReceiveBillDetailByPage")
    public CommonResult<CommonPageResult<OrderPaymentBillDetailVO>> findReceiveBillDetailByPage(@RequestBody @Valid QueryPaymentBillDetailForm form) {
        IPage<OrderPaymentBillDetailVO> pageList = billDetailService.findReceiveBillDetailByPage(form);
        List<String> amountStr = pageList.getRecords().stream().map(OrderPaymentBillDetailVO::getAmountStr).collect(Collectors.toList());
        String amount = this.commonService.calculatingCosts(amountStr);
        Map<String, Object> data = new HashMap<>();
        data.put("amountStr", amount == null ? "" : amount);
        CommonPageResult<OrderPaymentBillDetailVO> pageVO = new CommonPageResult(pageList, data);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "提交财务 billNos=账单编号集合,必须客服主管审核通过,状态为B_2")
    @PostMapping("/submitSCw")
    public CommonResult submitSCw(@RequestBody ListForm form) {
        return billDetailService.submitSCw(form);
    }

    @ApiOperation(value = "导出应收对账单列表,导出财务应付对账单列表")
    @RequestMapping(value = "/exportSBill", method = RequestMethod.GET)
    @ResponseBody
    public void exportSBill(QueryPaymentBillDetailForm form,
                            HttpServletResponse response) throws IOException {
        //获取数据
        List<OrderPaymentBillDetailVO> initList = billDetailService.findReceiveBillDetail(form);
        List<ExportOrderSBillDetailVO> list = ConvertUtil.convertList(initList, ExportOrderSBillDetailVO.class);
        list.stream().sorted(Comparator.comparing(ExportOrderSBillDetailVO::getId).reversed());
        ExcelWriter writer = ExcelUtil.getWriter(true);

        //自定义标题别名
        writer.addHeaderAlias("billNo", "账单编号");
        writer.addHeaderAlias("legalName", "法人主体");
        writer.addHeaderAlias("unitAccount", "结算单位");
//        writer.addHeaderAlias("customerName", "客服");
        writer.addHeaderAlias("accountTermStr", "核算期");
        writer.addHeaderAlias("rmb", "人民币");
        writer.addHeaderAlias("dollar", "美元");
        writer.addHeaderAlias("euro", "欧元");
        writer.addHeaderAlias("hKDollar", "港币");
        writer.addHeaderAlias("heXiaoAmount", "已付金额");
        writer.addHeaderAlias("notHeXiaoAmount", "未付金额");
        writer.addHeaderAlias("settlementCurrency", "结算币种");
        writer.addHeaderAlias("auditStatusDesc", "状态");
        writer.addHeaderAlias("applyStatus", "付款申请");
        writer.addHeaderAlias("makeUser", "制单人");
        writer.addHeaderAlias("makeTimeStr", "制单时间");
        writer.addHeaderAlias("auditUser", "审核人");
        writer.addHeaderAlias("auditTimeStr", "审核时间");
        writer.addHeaderAlias("auditComment", "审核意见");
        writer.addHeaderAlias("heXiaoUser", "核销人");
        writer.addHeaderAlias("heXiaoTimeStr", "核销时间");
        writer.addHeaderAlias("pushKingdeeCount", "推金蝶次数");//导出财务应收对账单列表才有

        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(list, true);

        //out为OutputStream，需要写出到的目标流
        ServletOutputStream out = response.getOutputStream();
        String name = StringUtils.toUtf8String("应收对账单");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + name + ".xlsx");

        writer.flush(out);
        writer.close();
        IoUtil.close(out);
    }


    @ApiOperation(value = "开票申请")
    @PostMapping("/applyInvoice")
    public CommonResult applyInvoice(@RequestBody @Valid ApplyInvoiceForm form) {
        //1.财务对账审核通过
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("bill_no", form.getBillNo());
        List<OrderReceivableBillDetail> orderReceivableBillDetails = billDetailService.list(queryWrapper);
        OrderReceivableBillDetail orderReceivableBillDetail = orderReceivableBillDetails.get(0);
        if (orderReceivableBillDetail != null && !(BillEnum.B_4.getCode().equals(orderReceivableBillDetail.getAuditStatus()) ||
                BillEnum.B_5_1.getCode().equals(orderReceivableBillDetail.getAuditStatus()))) {
            return CommonResult.error(10000, "不满足付款申请的条件");
        }
        Boolean result = billDetailService.applyInvoice(form);
        if (!result) {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "开票申请作废,billNo=列表里面取")
    @PostMapping("/applyInvoiceCancel")
    public CommonResult applyInvoiceCancel(@RequestBody Map<String, Object> param) {
        String billNo = MapUtil.getStr(param, "billNo");
        //1.财务开票申请审核不通过
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("bill_no", billNo);
        List<OrderReceivableBillDetail> orderReceivableBillDetails = billDetailService.list(queryWrapper);
        OrderReceivableBillDetail orderReceivableBillDetail = orderReceivableBillDetails.get(0);
        if (orderReceivableBillDetail != null && !BillEnum.B_6_1.getCode().equals(orderReceivableBillDetail.getAuditStatus())) {
            return CommonResult.error(10000, "不满足付款申请作废的条件");
        }
        if (orderReceivableBillDetail != null && BillEnum.F_4.getCode().equals(orderReceivableBillDetail.getApplyStatus())) {
            return CommonResult.error(10000, "开票申请已作废");
        }
        Boolean result = billDetailService.applyInvoiceCancel(billNo);
        if (!result) {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "编辑对账单列表,费用维度的")
    @PostMapping("/findEditSBillByPage")
    public CommonResult<CommonPageResult<PaymentNotPaidBillVO>> findEditSBillByPage(@RequestBody @Valid QueryEditBillForm form) {
        IPage<PaymentNotPaidBillVO> pageList = billDetailService.findEditSBillByPage(form);
        CommonPageResult<PaymentNotPaidBillVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "编辑保存确定")
    @PostMapping("/editSSaveConfirm")
    public CommonResult editSSaveConfirm(@RequestBody EditSBillForm form) {
        //参数校验
        List<OrderReceiveBillDetailForm> receiveBillDetailForms = form.getReceiveBillDetailForms();
        if (receiveBillDetailForms == null || receiveBillDetailForms.size() == 0) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        List<Long> costIds = new ArrayList<>();
        for (OrderReceiveBillDetailForm formObject : receiveBillDetailForms) {
            costIds.add(formObject.getCostId());
        }
        Boolean result = billDetailService.editSSaveConfirm(costIds, form);
        if (!result) {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "编辑删除")
    @PostMapping("/editSDel")
    public CommonResult editSDel(@RequestBody EditSBillForm form) {
        //参数校验
        List<OrderReceiveBillDetailForm> delCosts = form.getDelCosts();
        if (delCosts == null || delCosts.size() == 0) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        //查询账单详情id查询账单信息
        Long billDetailId = delCosts.get(0).getBillDetailId();
        if (billDetailId != null) {
            OrderReceivableBillDetail orderReceivableBillDetail = this.billDetailService.getById(billDetailId);
            //查询编辑账单数量
            if (!BillEnum.EDIT_NO_COMMIT.getCode().equals(orderReceivableBillDetail.getAuditStatus()) && this.billDetailService.getEditBillNum(orderReceivableBillDetail.getBillNo()) <= 1) {
                return CommonResult.error(400, "需要保留一条已出的账单费用");
            }
        }

        List<Long> costIds = new ArrayList<>();
        for (OrderReceiveBillDetailForm formObject : delCosts) {
            costIds.add(formObject.getCostId());
        }
        Boolean result = billDetailService.editSDel(costIds);
        if (!result) {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "客服编辑对账单保存,财务编辑对账单")
    @PostMapping("/editSBill")
    public CommonResult editSBill(@RequestBody EditSBillForm form) {
        //参数校验
        form.checkEditSBill();
        return billDetailService.editSBill(form);
    }

    @ApiOperation(value = "对账单详情，对账单审核详情")
    @PostMapping("/viewSBillDetail")
    public CommonResult<Map<String, Object>> viewSBillDetail(@RequestBody @Valid ViewBillDetailForm form) {
        Map<String, Object> resultMap = new HashMap<>();
//        List<ViewBilToOrderVO> list = billDetailService.viewSBillDetail(form.getBillNo());
        JSONArray jsonArray = billDetailService.viewSBillDetailInfo(form.getBillNo(), form.getCmd(), form.getCmd());
        resultMap.put(CommonConstant.LIST, jsonArray);//分页数据
//        List<SheetHeadVO> sheetHeadVOS = billDetailService.findSSheetHead(form.getBillNo(), new HashMap<>());
        List<SheetHeadVO> sheetHeadVOS = billDetailService.findSSheetHeadInfo(form.getBillNo(), new HashMap<>(), form.getCmd(), form.getCmd());
        resultMap.put(CommonConstant.SHEET_HEAD, sheetHeadVOS);//表头
        ViewBillVO viewBillVO = billDetailService.getViewSBill(form.getBillNo());
        resultMap.put(CommonConstant.WHOLE_DATA, viewBillVO);//全局数据
        return CommonResult.success(resultMap);
    }


    @ApiOperation(value = "导出对账单详情 cmd=ky,zgys,nl ..等指令和录用费用类型对应")
    @RequestMapping(value = "/exportSBillDetail", method = RequestMethod.GET)
    @ResponseBody
    public void exportSBillDetail(@RequestParam(value = "billNo") String billNo,
                                  @RequestParam(value = "cmd", required = false) String cmd,
                                  @RequestParam(value = "templateCmd", required = false) String templateCmd,
                                  HttpServletResponse response) throws IOException {

//        List<ViewBilToOrderVO> list = billDetailService.viewSBillDetail(billNo);
//        //地址只展示6个字符
//        list.stream().forEach(e -> {
//            if (e.getStartAddress() != null && e.getStartAddress().length() > 6) {
//                e.setStartAddress(e.getStartAddress().substring(0, 6));
//            }
//            if (e.getEndAddress() != null && e.getEndAddress().length() > 6) {
//                e.setEndAddress(e.getEndAddress().substring(0, 6));
//            }
//        });
//
//        TypeUtils.compatibleWithJavaBean = true;
//        JSONArray datas = JSONArray.parseArray(JSON.toJSONString(list));

        JSONArray datas = this.billDetailService.viewSBillDetailInfo(billNo, cmd, templateCmd);
        ViewBillVO viewBillVO = billDetailService.getViewSBill(billNo);

        Map<String, Object> callbackArg = new HashMap<>();
        //头部数据重组
        List<SheetHeadVO> sheetHeadVOS = billDetailService.findSSheetHeadInfo(billNo, callbackArg, cmd, templateCmd);
        int index = Integer.parseInt(callbackArg.get("fixHeadIndex").toString()) - 1;
        LinkedHashMap<String, String> headMap = new LinkedHashMap<>();
        LinkedHashMap<String, String> dynamicHead = new LinkedHashMap<>();
        for (int i = 0; i < sheetHeadVOS.size(); i++) {
            SheetHeadVO sheetHeadVO = sheetHeadVOS.get(i);
            headMap.put(sheetHeadVO.getName(), sheetHeadVO.getViewName());
            if (i > index) {
                dynamicHead.put(sheetHeadVO.getName(), sheetHeadVO.getViewName());
            }
        }

        //计算结算币种
        this.orderBillCostTotalService.exportSettlementCurrency(cmd, headMap, dynamicHead, datas, "2");

        //查询法人人主体信息
        cn.hutool.json.JSONArray tmp = new cn.hutool.json.JSONArray(this.oauthClient
                .getLegalEntityByLegalIds(Collections.singletonList(viewBillVO.getLegalEntityId())).getData());
        cn.hutool.json.JSONObject legalEntityJson = tmp.getJSONObject(0);

        //内部往来关系
        List<String> settlementRelationship = this.commonService.getInternalSettlementRelationship(datas);

        EasyExcelEntity entity = new EasyExcelEntity();
        entity.setSheetName("客户应收对账单");
        //组装标题
        List<String> titles = new ArrayList<>();
        titles.add(viewBillVO.getLegalName());
        titles.add("客户应收款对帐单");
        StringBuilder sb = new StringBuilder();
        titles.add(sb.append("对账日期:").append(viewBillVO.getAccountTermStr()).toString());
        entity.setTitle(titles);
        //组装台头
        List<String> stageHeads = new ArrayList<>();
        //内部往来
        stageHeads.add("TO:" + viewBillVO.getCustomerName() + settlementRelationship.get(1));
        sb = new StringBuilder();
        stageHeads.add(sb.append("FR:").append(viewBillVO.getLegalName()).append(settlementRelationship.get(0))
                .append(EasyExcelUtils.SPLIT_SYMBOL)
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
        entity.setTotalIndex(index);

        //尾部
        List<String> bottomData = new ArrayList<>();
        bottomData.add(new StringBuilder()
                .append("公司名称:")
                .append(legalEntityJson.getStr("legalName", ""))
                .append(EasyExcelUtils.SPLIT_SYMBOL)
                .append("制 单 人:")
                .append(viewBillVO.getMakeUser())
                .toString());
        bottomData.add(new
                StringBuilder()
                .append("开户银行:")
                .append(legalEntityJson.getStr("bank", ""))
                .append(EasyExcelUtils.SPLIT_SYMBOL)
                .append("制单时间:")
                .append(DateUtils.format(viewBillVO.getMakeTimeStr(), DateUtils.DATE_PATTERN)).toString());

        bottomData.add(new StringBuilder()
                .append("开户账号:")
                .append(legalEntityJson.getStr("accountOpen", ""))
                .append(EasyExcelUtils.SPLIT_SYMBOL)
                .append("审 单 人:")
                .append(viewBillVO.getMakeUser())
                .toString());
        bottomData.add(new StringBuilder()
                .append("纳税人识别号:")
                .append(legalEntityJson.getStr("taxIdentificationNum", ""))
                .append(EasyExcelUtils.SPLIT_SYMBOL)
                .append("审单时间:")
                .append(DateUtils.format(viewBillVO.getAuditTimeStr(), DateUtils.DATE_PATTERN)).toString());
        bottomData.add(new
                StringBuilder()
                .append("公司地址:")
                .append(legalEntityJson.getStr("rigisAddress", ""))
                .append(EasyExcelUtils.SPLIT_SYMBOL)
                .toString());
        bottomData.add(new StringBuilder()
                .append("联系电话:")
                .append(legalEntityJson.getStr("phone", ""))
                .append(EasyExcelUtils.SPLIT_SYMBOL)
                .toString());
        entity.setBottomData(bottomData);
        Workbook workbook = EasyExcelUtils.autoGeneration("", entity);

        ServletOutputStream out = response.getOutputStream();
        String name = StringUtils.toUtf8String("客户应收对账单");
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
        List<ExportOrderSBillDetailVO> list = ConvertUtil.convertList(initList, ExportOrderSBillDetailVO.class);

        ExcelWriter writer = ExcelUtil.getWriter(true);

        //自定义标题别名
        writer.addHeaderAlias("billNo", "账单编号");
        writer.addHeaderAlias("legalName", "法人主体");
        writer.addHeaderAlias("customerName", "客服");
        writer.addHeaderAlias("accountTermStr", "核算期");
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
        response.setHeader("Content-Disposition", "attachment;filename=" + name + ".xlsx");

        writer.flush(out);
        writer.close();
        IoUtil.close(out);
    }

    /**
     * 1.客服主管-应收反审核 kf_s_reject
     * 2.客服主管-应付反审核 kf_f_reject
     * 3.财务-应收反审核 cw_s_reject
     * 4.财务-应付反审核 cw_f_reject
     *
     * @param form
     * @return
     */
    @ApiOperation(value = "反审核,billNos=账单编号集合")
    @PostMapping("/contrarySAudit")
    public CommonResult contrarySAudit(@RequestBody ListForm form) {
        if (form.getBillNos() == null || form.getBillNos().size() == 0 || StringUtil.isNullOrEmpty(form.getCmd())) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        return billDetailService.contrarySAudit(form);
    }

    @ApiOperation(value = "获取修改应收对账单信息,billNo=账单编号")
    @PostMapping("/getUpdateBillDetail")
    public CommonResult getUpdateBillDetail(@RequestBody Map<String, Object> map) {
        String billNo = MapUtil.getStr(map, "billNo");
        if (com.jayud.common.utils.StringUtils.isEmpty(billNo)) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        return CommonResult.success(orderBillCostTotalService.getEditBillByBillNo(billNo, 0));
    }


}
