package com.jayud.oms.feign;

import com.jayud.common.ApiResult;
import com.jayud.common.entity.SubOrderCloseOpt;
import com.jayud.oms.model.bo.*;
import com.jayud.oms.model.vo.*;
import com.jayud.oms.model.vo.template.order.StorageInputTemplate;
import com.jayud.oms.model.vo.template.order.StorageOutTemplate;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * oms模块消费storage模块的接口
 */
@FeignClient(value = "jayud-storage-web")
public interface StorageClient {

    /**
     * 创建仓储入库订单
     */
    @RequestMapping(value = "/api/storage/createInOrder")
    ApiResult<String> createInOrder(@RequestBody InputStorageInputOrderForm inputStorageInputOrderForm);

    /**
     * 创建仓储出库订单
     */
    @RequestMapping(value = "/api/storage/createOutOrder")
    ApiResult<String> createOutOrder(@RequestBody InputStorageOutOrderForm inputStorageOutOrderForm);

    /**
     * 创建仓储快进快出订单
     */
    @RequestMapping(value = "/api/storage/createFastOrder")
    ApiResult<String> createFastOrder(@RequestBody InputStorageFastOrderForm inputStorageFastOrderForm);

    /**
     * 根据主订单号获取仓储入库单信息
     */
    @RequestMapping(value = "/api/storage/getStorageInOrderDetails")
    ApiResult<InputStorageInputOrderVO> getStorageInOrderDetails(@RequestParam("orderNo") String orderNo);

    /**
     * 根据主订单号获取仓储出库单信息
     */
    @RequestMapping(value = "/api/storage/getStorageOutOrderDetails")
    ApiResult<InputStorageOutOrderVO> getStorageOutOrderDetails(@RequestParam("orderNo") String orderNo);

    /**
     * 根据主订单号获取仓储出库单信息
     */
    @RequestMapping(value = "/api/storage/getStorageFastOrderDetails")
    ApiResult<InputStorageFastOrderVO> getStorageFastOrderDetails(@RequestParam("orderNo") String orderNo);

    @ApiOperation(value = "判断商品是否为商品表维护的数据")
    @PostMapping(value = "/isCommodity")
    public ApiResult isCommodity(@RequestBody List<AddWarehouseGoodsForm> warehouseGoodsForms);

    @ApiOperation(value = "判断出库商品数量是否小于等于该批次商品库存")
    @PostMapping(value = "/isStock")
    public ApiResult isStock(@RequestBody List<AddWarehouseGoodsForm> warehouseGoodsForms);

    @ApiOperation(value = "判断出库商品数量是否小于等于该商品库存")
    @PostMapping(value = "/isEnough")
    public ApiResult isEnough(@RequestBody List<AddWarehouseGoodsForm> goodsFormList);

    /**
     * 获取仓储出库订单号
     */
    @RequestMapping(value = "/api/storage/getStorageOutOrderNo")
    public ApiResult<InitChangeStatusVO> getStorageOutOrderNo(@RequestParam(value = "mainOrderNo") String mainOrderNo);

    /**
     * 获取仓储入库订单号
     */
    @RequestMapping(value = "/api/storage/getStorageInOrderNo")
    public ApiResult<InitChangeStatusVO> getStorageInOrderNo(@RequestParam(value = "mainOrderNo") String mainOrderNo);

    /**
     * 获取仓储快进快出订单号
     */
    @RequestMapping(value = "/api/storage/getStorageFastOrderNo")
    public ApiResult<InitChangeStatusVO> getStorageFastOrderNo(@RequestParam(value = "mainOrderNo") String mainOrderNo);

    /**
     * 关闭订单
     */
    @RequestMapping(value = "/api/storage/closeInOrder")
    public ApiResult closeInOrder(@RequestBody List<SubOrderCloseOpt> form);

    /**
     * 关闭订单
     */
    @RequestMapping(value = "/api/storage/closeOutOrder")
    public ApiResult closeOutOrder(@RequestBody List<SubOrderCloseOpt> form);

    /**
     * 关闭订单
     */
    @RequestMapping(value = "/api/storage/closeFastOrder")
    public ApiResult closeFastOrder(@RequestBody List<SubOrderCloseOpt> form);
}
