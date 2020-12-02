package com.jayud.airfreight.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.airfreight.feign.OmsClient;
import com.jayud.airfreight.model.bo.QueryAirOrderForm;
import com.jayud.airfreight.model.po.Goods;
import com.jayud.airfreight.model.vo.AirOrderFormVO;
import com.jayud.airfreight.service.IAirOrderService;
import com.jayud.airfreight.service.IGoodsService;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ResultEnum;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 空运订单表 前端控制器
 * </p>
 *
 * @author LDR
 * @since 2020-11-30
 */
@RestController
@Slf4j
@RequestMapping("/airOrder")
public class AirOrderController {
    @Autowired
    private OmsClient omsClient;
    @Autowired
    private IAirOrderService airOrderService;
    @Autowired
    private IGoodsService goodsService;

    @ApiOperation(value = "分页查询空运订单")
    @PostMapping(value = "/findByPage")
    public CommonResult findByPage(@RequestBody QueryAirOrderForm form) {

        //模糊查询客户信息
        if (form.getCustomerName() != null) {
            ApiResult result = omsClient.getByCustomerName(form.getCustomerName());
            Object data = result.getData();
            if (data != null) {
                JSONArray mainOrders = JSONArray.parseArray(JSON.toJSONString(data));
                form.assemblyMainOrderNo(mainOrders);
            }
        }

        //获取下个节点状态
        OrderStatusEnum statusEnum = OrderStatusEnum.getAirOrderPreStatus(form.getStatus());
        if (statusEnum == null) {
            log.warn("不存在这个状态");
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        form.setStatus(statusEnum.getCode());

        IPage<AirOrderFormVO> page = this.airOrderService.findByPage(form);
        if (page.getRecords().size() == 0) {
            return CommonResult.success(new CommonPageResult<>(page));
        }

        List<AirOrderFormVO> records = page.getRecords();
        List<Long> airOrderIds = new ArrayList<>();
        List<String> mainOrder = new ArrayList<>();
        for (AirOrderFormVO record : records) {
            airOrderIds.add(record.getId());
            mainOrder.add(record.getMainOrderNo());
        }
        //查询商品信息
        List<Goods> goods = goodsService.getGoodsByBusIds(airOrderIds, BusinessTypeEnum.KY.getDesc());
        //查询客户信息
        ApiResult result = omsClient.getMainOrderByOrderNos(mainOrder);
        for (AirOrderFormVO record : records) {
            //组装商品信息
            record.assemblyGoodsInfo(goods);
            //客户名称
            record.assemblyCustomerName(result.getData());
        }
        return CommonResult.success(new CommonPageResult(page));
    }

    /**
     * 空运流程操作
     */

}

