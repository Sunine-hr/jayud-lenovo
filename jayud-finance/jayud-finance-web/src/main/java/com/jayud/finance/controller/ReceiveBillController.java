package com.jayud.finance.controller;

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
import com.jayud.finance.bo.*;
import com.jayud.finance.feign.OmsClient;
import com.jayud.finance.service.CommonService;
import com.jayud.finance.service.IOrderReceivableBillService;
import com.jayud.finance.vo.*;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
            Object reCost = this.omsClient.getNoBillCost(orderNos, isMain, BillTypeEnum.RECEIVABLE.getCode()).getData();
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

        Map<String, Object> resultMap = new HashMap<>();
//        JSONArray list = billService.viewReceiveBill(form, costIds);
        JSONArray jsonArray = billService.viewReceiveBillInfo(form, costIds);
        resultMap.put(CommonConstant.LIST, jsonArray);//分页数据
//        List<SheetHeadVO> sheetHeadVOS = billService.findSheetHead(costIds);
        Map<String, Object> callbackArg = new HashMap<>();
        List<SheetHeadVO> sheetHeadVOS = billService.findSheetHeadInfo(costIds,callbackArg, form.getCmd());
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


    private Object orderDimensionOpt(CreateReceiveBillForm form) {
        return null;
    }

}
