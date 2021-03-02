package com.jayud.Inlandtransport.controller;


import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.Inlandtransport.feign.OauthClient;
import com.jayud.Inlandtransport.feign.OmsClient;
import com.jayud.Inlandtransport.model.bo.QueryOrderForm;
import com.jayud.Inlandtransport.model.vo.GoodsVO;
import com.jayud.Inlandtransport.model.vo.OrderInlandTransportFormVO;
import com.jayud.Inlandtransport.service.IOrderInlandTransportService;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.aop.annotations.DynamicHead;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.utils.StringUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 内陆订单 前端控制器
 * </p>
 *
 * @author LDR
 * @since 2021-03-01
 */
@RestController
@RequestMapping("/orderInlandTransport")
public class OrderInlandTransportController {

    @Autowired
    private OmsClient omsClient;
    @Autowired
    private IOrderInlandTransportService orderInlandTransportService;
    @Autowired
    private OauthClient oauthClient;

    @ApiOperation(value = "分页查询空运订单")
    @PostMapping(value = "/findByPage")
    @DynamicHead
    public CommonResult<CommonPageResult<OrderInlandTransportFormVO>> findByPage(@RequestBody QueryOrderForm form) {

        //模糊查询客户信息
        if (!StringUtils.isEmpty(form.getCustomerName())) {
            ApiResult result = omsClient.getByCustomerName(form.getCustomerName());
            Object data = result.getData();
            if (data != null && ((List) data).size() > 0) {
                JSONArray mainOrders = new JSONArray(data);
                form.assemblyMainOrderNo(mainOrders);
            } else {
                form.setMainOrderNos(Collections.singletonList("-1"));
            }
        }

        //获取下个节点状态
//        OrderStatusEnum statusEnum = OrderStatusEnum.getAirOrderPreStatus(form.getStatus());

//        form.setStatus(statusEnum == null ? null : statusEnum.getCode());

        IPage<OrderInlandTransportFormVO> page = this.orderInlandTransportService.findByPage(form);
        if (page.getRecords().size() == 0) {
            return CommonResult.success(new CommonPageResult<>(page));
        }

        List<OrderInlandTransportFormVO> records = page.getRecords();
        List<Long> airOrderIds = new ArrayList<>();
        List<String> mainOrder = new ArrayList<>();
        List<Long> entityIds = new ArrayList<>();
        List<Long> supplierIds = new ArrayList<>();
        for (OrderInlandTransportFormVO record : records) {
            airOrderIds.add(record.getId());
            mainOrder.add(record.getMainOrderNo());
            entityIds.add(record.getLegalEntityId());
            supplierIds.add(record.getSupplierId());
        }
        //查询商品信息
        List<GoodsVO> goods = this.omsClient.getGoodsByBusIds(airOrderIds, BusinessTypeEnum.NL.getCode()).getData();
        //查询法人主体
        ApiResult legalEntityResult = null;
        if (CollectionUtils.isNotEmpty(entityIds)) {
            legalEntityResult = this.oauthClient.getLegalEntityByLegalIds(entityIds);
        }
        //查询供应商信息
        JSONArray supplierInfo = null;
        if (CollectionUtils.isNotEmpty(supplierIds)) {
            supplierInfo = new JSONArray(this.omsClient.getSupplierInfoByIds(supplierIds).getData());
        }
        //查询主订单信息
        ApiResult result = omsClient.getMainOrderByOrderNos(mainOrder);
        for (OrderInlandTransportFormVO record : records) {
            //组装商品信息
            record.assemblyGoodsInfo(goods);
            //拼装主订单信息
            record.assemblyMainOrderData(result.getData());
            //组装法人名称
            record.assemblyLegalEntity(legalEntityResult);
            //拼装供应商
            record.assemblySupplierInfo(supplierInfo);
        }
        return CommonResult.success(new CommonPageResult(page));
    }
}

