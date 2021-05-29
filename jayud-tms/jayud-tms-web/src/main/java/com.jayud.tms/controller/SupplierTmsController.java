package com.jayud.tms.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.entity.DataControl;
import com.jayud.common.entity.InitComboxStrVO;
import com.jayud.common.enums.UserTypeEnum;
import com.jayud.common.utils.BeanUtils;
import com.jayud.tms.feign.OauthClient;
import com.jayud.tms.feign.OmsClient;
import com.jayud.tms.model.bo.QueryOrderTmsForm;
import com.jayud.tms.model.vo.OrderTransportInfoVO;
import com.jayud.tms.model.vo.OrderTransportVO;
import com.jayud.tms.model.vo.supplier.QuerySupplierBill;
import com.jayud.tms.model.vo.supplier.QuerySupplierBillInfo;
import com.jayud.tms.model.vo.supplier.SupplierBill;
import com.jayud.tms.model.vo.supplier.SupplierBillInfo;
import com.jayud.tms.service.IOrderTransportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/supplier")
@Api(tags = "web中港供应商")
public class SupplierTmsController {

    @Autowired
    private IOrderTransportService orderTransportService;
    @Autowired
    private OmsClient omsClient;
    @Autowired
    private OauthClient oauthClient;

    @ApiOperation(value = "分页查询供应商订单")
    @PostMapping("/findSupplierTmsByPage")
    public CommonResult<CommonPageResult<OrderTransportVO>> findSupplierTmsByPage(@RequestBody QueryOrderTmsForm form) {
        form.setAccountType(UserTypeEnum.SUPPLIER_TYPE.getCode());
        IPage<OrderTransportVO> pageList = orderTransportService.findTransportOrderByPage(form);
        CommonPageResult<OrderTransportVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "查询供应商订单详情")
    @PostMapping("/getSupplierTmsById")
    public CommonResult<OrderTransportInfoVO> getSupplierTmsById(@RequestBody Map<String, Object> map) {
        Long id = MapUtil.getLong(map, BeanUtils.convertToFieldName(OrderTransportVO::getId));
        OrderTransportInfoVO details = this.orderTransportService.getDetailsById(id);
        details.doFilterData(UserTypeEnum.SUPPLIER_TYPE.getCode());
        return CommonResult.success(details);
    }

    @ApiOperation(value = "根据中港费用类型查询费用名称")
    @PostMapping(value = "/getCostInfoByCostType")
    public CommonResult<List<InitComboxStrVO>> getCostInfoByCostType() {
        Map<String, String> map = new HashMap<>();
        map.put("code", "FYLB2002");
        return CommonResult.success(this.omsClient.getCostInfoByCostType(map).getData());
    }

    @ApiOperation(value = "查询供应商费待处理数量")
    @PostMapping(value = "/getSupplyPendingOpt")
    public CommonResult<Map<String, Object>> getSupplyPendingOpt() {
        Map<String, Object> map = this.orderTransportService.getPendingOpt(UserTypeEnum.SUPPLIER_TYPE.getCode());
        return CommonResult.success(map);
    }

    @ApiOperation(value = "查询供应商账单列表")
    @PostMapping(value = "/findSupplierBillByPage")
    public CommonResult<Map<String, Object>> findSupplierBillByPage(@RequestBody QuerySupplierBill form) {
        Map<String, Object> callbackParam = new HashMap<>();
        IPage<SupplierBill> list = this.orderTransportService.findSupplierBillByPage(form, callbackParam);
        CommonPageResult<SupplierBill> pageVO = new CommonPageResult(list);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(CommonConstant.PAGE_LIST, pageVO);//分页数据
        //数据组装
        Object total = callbackParam.get("total");

        if (total != null) {
            StringBuilder sb = new StringBuilder();
            ((Map<String, BigDecimal>) total).forEach((k, v) -> {
                sb.append(v).append(k).append("</br>");
            });
            resultMap.put("total", sb.toString());
        }
        resultMap.put("orderTotalNum", callbackParam.get("orderTotalNum"));

        return CommonResult.success(resultMap);
    }


    @ApiOperation(value = "分页查询供应商订单")
    @PostMapping("/findSupplierBillInfoByPage")
    public CommonResult<List<SupplierBillInfo>> findSupplierBillInfoByPage(@RequestBody QuerySupplierBillInfo form) {
        IPage<SupplierBillInfo> pageList = orderTransportService.findSupplierBillInfoByPage(form);
        return CommonResult.success(pageList.getRecords());
    }
}
