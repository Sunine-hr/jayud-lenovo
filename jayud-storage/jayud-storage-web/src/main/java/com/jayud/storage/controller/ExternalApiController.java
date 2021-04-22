package com.jayud.storage.controller;


import com.jayud.common.ApiResult;
import com.jayud.storage.model.bo.StorageInputOrderForm;
import com.jayud.storage.model.bo.StorageOutOrderForm;
import com.jayud.storage.model.vo.StorageInputOrderVO;
import com.jayud.storage.model.vo.StorageOutOrderVO;
import com.jayud.storage.service.IStorageInputOrderService;
import com.jayud.storage.service.IStorageOutOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 被外部模块调用的处理接口
 *
 * @author
 * @description
 * @Date:
 */
@RestController
@Api(tags = "仓储被外部调用的接口")
@Slf4j
public class ExternalApiController {

    @Autowired
    private IStorageInputOrderService storageInputOrderService;

    @Autowired
    private IStorageOutOrderService storageOutOrderService;

    @ApiOperation("创建入库订单")
    @RequestMapping(value = "/api/storage/createInOrder")
    ApiResult<String> createInOrder(@RequestBody StorageInputOrderForm storageInputOrderForm){
        String orderNo = storageInputOrderService.createOrder(storageInputOrderForm);
        return ApiResult.ok(orderNo);
    }


    @ApiOperation("创建出库订单")
    @RequestMapping(value = "/api/storage/createOutOrder")
    ApiResult<String> createOutOrder(@RequestBody StorageOutOrderForm storageOutOrderForm){
        String orderNo = storageOutOrderService.createOrder(storageOutOrderForm);
        return ApiResult.ok(orderNo);
    }

    /**
     * 根据主订单号获取仓储入库单信息
     */
    @RequestMapping(value = "/api/storage/getStorageInOrderDetails")
    ApiResult<StorageInputOrderVO> getStorageInOrderDetails(@RequestParam("orderNo") String orderNo){

        return ApiResult.ok(orderNo);
    }

    /**
     * 根据主订单号获取仓储出库单信息
     */
    @RequestMapping(value = "/api/storage/getStorageOutOrderDetails")
    ApiResult<StorageOutOrderVO> getStorageOutOrderDetails(@RequestParam("orderNo") String orderNo){

        return ApiResult.ok(orderNo);
    }

}
