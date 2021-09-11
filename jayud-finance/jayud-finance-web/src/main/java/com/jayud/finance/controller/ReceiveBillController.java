package com.jayud.finance.controller;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.entity.InitComboxStrVO;
import com.jayud.common.enums.BillTypeEnum;
import com.jayud.common.enums.ResultEnum;;
import com.jayud.common.enums.SubOrderSignEnum;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.HttpUtils;
import com.jayud.common.utils.excel.EasyExcelEntity;
import com.jayud.common.utils.excel.EasyExcelUtils;
import com.jayud.finance.bo.*;
import com.jayud.finance.feign.OauthClient;
import com.jayud.finance.feign.OmsClient;
import com.jayud.finance.service.CommonService;
import com.jayud.finance.service.IOrderReceivableBillService;
import com.jayud.finance.util.StringUtils;
import com.jayud.finance.vo.*;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/receiveBill/")
@Api(tags = "应收出账")
public class ReceiveBillController {

    @Autowired
    IOrderReceivableBillService billService;
    @Autowired
    OmsClient omsClient;
    @Autowired
    private CommonService commonService;
    @Autowired
    private OauthClient oauthClient;

    @ApiOperation(value = "应收出账单列表(主订单/子订单)")
    @PostMapping("/findReceiveBillByPage")
    public CommonResult<CommonPageResult<OrderReceiveBillVO>> findReceiveBillByPage(@RequestBody @Valid QueryReceiveBillForm form) {
        IPage<OrderReceiveBillVO> pageList = billService.findReceiveBillByPage(form);
        CommonPageResult<OrderReceiveBillVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "账单数列表(主订单/子订单)")
    @PostMapping("/findReceiveBillNum")
    public CommonResult<Map<String, Object>> findReceiveBillNum(@RequestBody @Valid QueryReceiveBillNumForm form) {
        Map<String, Object> result = billService.findReceiveBillNum(form);
        return CommonResult.success(result);
    }

    @ApiOperation(value = "未出账订单数列表(主订单/子订单)")
    @PostMapping("/findNotPaidBillByPage")
    public CommonResult<CommonPageResult<ReceiveNotPaidBillVO>> findNotPaidBillByPage(@RequestBody @Valid QueryNotPaidBillForm form) {
        IPage<ReceiveNotPaidBillVO> pageList = null;
        if (form.getType() == 1) {
            pageList = billService.findNotPaidBillByPage(form);
        } else {
            pageList = billService.findNotPaidOrderBillByPage(form);
        }
        CommonPageResult<ReceiveNotPaidBillVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "暂存和生成应收账单")
    @PostMapping("/createBill")
    public CommonResult createReceiveBill(@RequestBody @Valid CreateReceiveBillForm form) {
        //参数校验
        if ("create".equals(form.getCmd())) {
            OrderReceiveBillForm receiveBillForm = form.getReceiveBillForm();
            if (receiveBillForm == null || StringUtil.isNullOrEmpty(receiveBillForm.getLegalName()) || StringUtil.isNullOrEmpty(receiveBillForm.getUnitAccount()) ||
                    receiveBillForm.getBillOrderNum() == null || receiveBillForm.getAlreadyPaidAmount() == null || receiveBillForm.getBillNum() == null
//                    || StringUtil.isNullOrEmpty(form.getBillNo())
                    || StringUtil.isNullOrEmpty(form.getAccountTermStr()) || StringUtil.isNullOrEmpty(form.getSettlementCurrency()) ||
                    StringUtil.isNullOrEmpty(form.getSubType())) {
                return CommonResult.error(ResultEnum.PARAM_ERROR);
            }
        }
        form.checkCreateReceiveBill();

        //订单维度补充数据
        if (form.getType() == 2) {
            List<String> orderNos = new ArrayList<>();
            Boolean isMain = form.getSubType().equals(SubOrderSignEnum.MAIN.getSignOne());
            form.getReceiveBillDetailForms().forEach(e -> {
                if (isMain) {
                    orderNos.add(e.getOrderNo());
                } else {
                    orderNos.add(e.getSubOrderNo());
                }
            });
            OrderReceiveBillForm receiveBillForm = form.getReceiveBillForm();
            Object reCost = this.omsClient.getNoBillCost(orderNos, isMain,
                    receiveBillForm.getLegalEntityId(), receiveBillForm.getUnitCode(),
                    BillTypeEnum.RECEIVABLE.getCode()).getData();
            form.assemblyOrderDimensionData(reCost, isMain);
        }
        return billService.createReceiveBill(form);
    }


    @ApiOperation(value = "预览应收账单")
    @PostMapping("/viewReceiveBill")
    public CommonResult<Map<String, Object>> viewReceiveBill(@RequestBody @Valid ViewSBillForm form) {
        List<OrderReceiveBillDetailForm> billDetailForms = form.getBillDetailForms();
        List<Long> costIds = new ArrayList<>();

        for (OrderReceiveBillDetailForm billDetailForm : billDetailForms) {
            if (form.getType() == 2) {
                costIds.addAll(billDetailForm.getCostIds());
            } else {
                costIds.add(billDetailForm.getCostId());
            }
        }

        //订单维度补充数据

        //订单维度补充数据
//        if (form.getType() == 2) {
//            List<String> orderNos = new ArrayList<>();
//            Boolean isMain = form.getCmd().equals(SubOrderSignEnum.MAIN.getSignOne());
//            form.getBillDetailForms().forEach(e -> {
//                if (isMain) {
//                    orderNos.add(e.getOrderNo());
//                } else {
//                    orderNos.add(e.getSubOrderNo());
//                }
//            });
//            Object reCost = this.omsClient.getNoBillCost(orderNos, isMain, 0).getData();
//            if (reCost != null) {
//                costIds = new JSONArray(reCost).stream().map(e -> ((JSONObject) e).getLong("id")).collect(Collectors.toList());
//            }
//        }
        form.setTemplateCmd(form.getCmd());
        Map<String, Object> resultMap = new HashMap<>();
//        JSONArray list = billService.viewReceiveBill(form, costIds);
        JSONArray jsonArray = billService.viewReceiveBillInfo(form, costIds);
        resultMap.put(CommonConstant.LIST, jsonArray);//分页数据
//        List<SheetHeadVO> sheetHeadVOS = billService.findSheetHead(costIds);
        Map<String, Object> callbackArg = new HashMap<>();
        List<SheetHeadVO> sheetHeadVOS = billService.findSheetHeadInfo(costIds, callbackArg, form.getCmd());
        int index = Integer.parseInt(callbackArg.get("fixHeadIndex").toString()) - 1;
        //合计费用
        Map<String, Map<String, BigDecimal>> cost = this.commonService.totalDynamicHeadCost(index, sheetHeadVOS, jsonArray);

        resultMap.put(CommonConstant.SHEET_HEAD, sheetHeadVOS);//表头
        ViewBillVO viewBillVO = billService.getViewBillByCostIds(costIds, form.getCmd());
        viewBillVO.assembleTotalCost(cost).setOrderNum(jsonArray.size());
        resultMap.put(CommonConstant.WHOLE_DATA, viewBillVO);//全局数据
        return CommonResult.success(resultMap);
    }


    @ApiOperation(value = "查询应收费用币种信息")
    @PostMapping("/getReceivableCost")
    public CommonResult getReceivableCostCurrencyInfo(@RequestBody Map<String, Object> map) {
        List<Long> costIds = new ArrayList<>();
        for (Object costId : MapUtil.get(map, "costIds", List.class)) {
            if (costId == null) {
                return CommonResult.error(ResultEnum.PARAM_ERROR);
            }
            costIds.add(Long.parseLong(costId.toString()));
        }
        ApiResult result = this.omsClient.getCostCurrencyInfo(costIds, 0);
        if (result.getCode() != HttpStatus.HTTP_OK) {
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        //组装数据
        JSONArray datas = new JSONArray(result.getData());
        List<InitComboxStrVO> initComboxStrVOS = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            JSONObject data = datas.getJSONObject(i);
            InitComboxStrVO initComboxStrVO = new InitComboxStrVO();
            initComboxStrVO.setCode(data.getStr("currencyCode"));
            initComboxStrVO.setName(data.getStr("currencyName"));
            initComboxStrVO.setNote("");
            initComboxStrVOS.add(initComboxStrVO);
        }
        return CommonResult.success(initComboxStrVOS);
    }


    @ApiOperation(value = "导出预览应收账单")
    @PostMapping("/exportViewReceiveBill")
    public CommonResult<Map<String, Object>> exportViewReceiveBill(@RequestBody @Valid ViewSBillForm form) throws IOException {

        List<OrderReceiveBillDetailForm> billDetailForms = form.getBillDetailForms();
        List<Long> costIds = new ArrayList<>();
        for (OrderReceiveBillDetailForm billDetailForm : billDetailForms) {
            if (form.getType() == 2) {
                costIds.addAll(billDetailForm.getCostIds());
            } else {
                costIds.add(billDetailForm.getCostId());
            }
        }

        JSONArray datas = billService.viewReceiveBillInfo(form, costIds);
        Map<String, Object> callbackArg = new HashMap<>();
        List<SheetHeadVO> sheetHeadVOS = billService.findSheetHeadInfo(costIds, callbackArg, form.getCmd());
        sheetHeadVOS = sheetHeadVOS.stream().filter(e -> !"customerName".equals(e.getName()) || !"unitAccount".equals(e.getName())).collect(Collectors.toList());
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
        ViewBillVO viewBillVO = billService.getViewBillByCostIds(costIds, form.getCmd());
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
                .append("账单编号:").append(viewBillVO.getBillNo() == null ? "" : viewBillVO.getBillNo()).toString());
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
                .append(viewBillVO.getMakeUser() == null ? "" : viewBillVO.getMakeUser())
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
                .append(viewBillVO.getMakeUser() == null ? "" : viewBillVO.getMakeUser())
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

        HttpServletResponse response = HttpUtils.getHttpResponseServletContext();

        ServletOutputStream out = response.getOutputStream();
        String name = StringUtils.toUtf8String("客户应收对账单");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + name + ".xlsx");

        workbook.write(out);
        workbook.close();
        IoUtil.close(out);


        return CommonResult.success();
    }


    private Object orderDimensionOpt(CreateReceiveBillForm form) {
        return null;
    }

}
