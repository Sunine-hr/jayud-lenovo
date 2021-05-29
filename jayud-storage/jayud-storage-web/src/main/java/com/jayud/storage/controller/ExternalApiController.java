package com.jayud.storage.controller;


import com.jayud.common.ApiResult;
import com.jayud.common.CommonResult;
import com.jayud.storage.model.bo.StorageInputOrderForm;
import com.jayud.storage.model.bo.StorageOutOrderForm;
import com.jayud.storage.model.bo.WarehouseGoodsForm;
import com.jayud.storage.model.po.GoodsLocationRecord;
import com.jayud.storage.model.po.InGoodsOperationRecord;
import com.jayud.storage.model.po.StorageInputOrder;
import com.jayud.storage.model.po.StorageOutOrder;
import com.jayud.storage.model.vo.StorageInputOrderVO;
import com.jayud.storage.model.vo.StorageOutOrderVO;
import com.jayud.storage.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private IGoodService goodService;

    @Autowired
    private IInGoodsOperationRecordService inGoodsOperationRecordService;

    @Autowired
    private IStockService stockService;

    @Autowired
    private IGoodsLocationRecordService goodsLocationRecordService;

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
        StorageInputOrder storageInputOrder = storageInputOrderService.getStorageInOrderByMainOrderNO(orderNo);
        System.out.println("storageInputOrder=========================================="+storageInputOrder);
        StorageInputOrderVO storageInputOrderVO = storageInputOrderService.getStorageInputOrderVOById(storageInputOrder.getId());
        return ApiResult.ok(storageInputOrderVO);
    }

    /**
     * 根据主订单号获取仓储出库单信息
     *
     */
    @RequestMapping(value = "/api/storage/getStorageOutOrderDetails")
    ApiResult<StorageOutOrderVO> getStorageOutOrderDetails(@RequestParam("orderNo") String orderNo){
        StorageOutOrder storageOutOrder = storageOutOrderService.getStorageOutOrderByMainOrderNO(orderNo);
        StorageOutOrderVO storageOutOrderVO = storageOutOrderService.getStorageOutOrderVOById(storageOutOrder.getId());
        return ApiResult.ok(storageOutOrderVO);
    }

    @ApiOperation(value = "判断商品是否为商品表维护的数据")
    @PostMapping(value = "/isCommodity")
    public ApiResult isCommodity(@RequestBody List<WarehouseGoodsForm> warehouseGoodsForms){
        for (WarehouseGoodsForm warehouseGoodsForm : warehouseGoodsForms) {
            boolean flag = goodService.isCommodity(warehouseGoodsForm.getSku());
            if(!flag){
                return ApiResult.error(444,"商品未建档，请前往建档");
            }
        }
        return ApiResult.ok();
    }

    @ApiOperation(value = "判断出库商品数量是否小于等于该商品库存")
    @PostMapping(value = "/isStock")
    public ApiResult isStock(@RequestBody List<WarehouseGoodsForm> warehouseGoodsForms){
        for (WarehouseGoodsForm warehouseGoodsForm : warehouseGoodsForms) {
            InGoodsOperationRecord listByWarehousingBatchNoAndSku = inGoodsOperationRecordService.getListByWarehousingBatchNoAndSku(warehouseGoodsForm.getWarehousingBatchNo(),warehouseGoodsForm.getSku());

            if(listByWarehousingBatchNoAndSku == null){
                return ApiResult.error(444,"该商品没有库存");
            }

            Integer number = 0;
            List<GoodsLocationRecord> goodsLocationRecordByGoodId = goodsLocationRecordService.getGoodsLocationRecordByGoodId(listByWarehousingBatchNoAndSku.getId());
            for (GoodsLocationRecord goodsLocationRecord : goodsLocationRecordByGoodId) {
                number = number + goodsLocationRecord.getUnDeliveredQuantity();
            }


            if(number < warehouseGoodsForm.getNumber()){
                return ApiResult.error(444,listByWarehousingBatchNoAndSku.getWarehousingBatchNo()+"的"+warehouseGoodsForm.getName()+"数量为"+number);
            }
        }
        return ApiResult.ok();
    }

}
