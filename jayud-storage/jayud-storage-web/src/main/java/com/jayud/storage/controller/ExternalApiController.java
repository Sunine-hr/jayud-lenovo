package com.jayud.storage.controller;


import com.jayud.common.ApiResult;
import com.jayud.common.CommonResult;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.entity.InitChangeStatusVO;
import com.jayud.common.entity.InitComboxStrVO;
import com.jayud.common.entity.SubOrderCloseOpt;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ProcessStatusEnum;
import com.jayud.storage.model.bo.*;
import com.jayud.storage.model.po.*;
import com.jayud.storage.model.vo.StorageFastOrderVO;
import com.jayud.storage.model.vo.StorageInputOrderVO;
import com.jayud.storage.model.vo.StorageOutOrderVO;
import com.jayud.storage.model.vo.WarehouseVO;
import com.jayud.storage.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private IStorageFastOrderService storageFastOrderService;

    @Autowired
    private IGoodService goodService;

    @Autowired
    private IInGoodsOperationRecordService inGoodsOperationRecordService;

    @Autowired
    private IStockService stockService;

    @Autowired
    private IGoodsLocationRecordService goodsLocationRecordService;

    @Autowired
    IWarehouseService warehouseService;

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

    @ApiOperation(value = "判断出库商品数量是否小于等于该商品库存")
    @PostMapping(value = "/isEnough")
    public ApiResult isEnough(@RequestBody List<WarehouseGoodsForm> goodsFormList){
        for (WarehouseGoodsForm warehouseGoodsForm : goodsFormList) {
            Stock stock = stockService.getStockBySku(warehouseGoodsForm.getSku());
            if(stock.getAvailableStock() < warehouseGoodsForm.getNumber()){
                return ApiResult.error(444,warehouseGoodsForm.getSku()+"的库存数量不足，剩余数量为" + stock.getAvailableStock());
            }
        }
        return ApiResult.ok();
    }

    /**
     * 创建仓储快进快出订单
     */
    @RequestMapping(value = "/api/storage/createFastOrder")
    ApiResult<String> createFastOrder(@RequestBody StorageFastOrderForm inputStorageFastOrderForm){
        String orderNo = storageFastOrderService.createOrder(inputStorageFastOrderForm);
        return ApiResult.ok(orderNo);
    }

    /**
     * 根据主订单号获取仓储快进快出单信息
     */
    @RequestMapping(value = "/api/storage/getStorageFastOrderDetails")
    ApiResult<StorageFastOrderVO> getStorageFastOrderDetails(@RequestParam("orderNo") String orderNo){
        StorageFastOrder storageFastOrder = storageFastOrderService.getStorageFastOrderByMainOrderNO(orderNo);
        StorageFastOrderVO storageFastOrderVO = storageFastOrderService.getStorageFastOrderVOById(storageFastOrder.getId());
        return ApiResult.ok(storageFastOrderVO);
    }

    /**
     * 获取仓储出库订单号
     */
    @RequestMapping(value = "/api/storage/getStorageOutOrderNo")
    public ApiResult<InitChangeStatusVO> getStorageOutOrderNo(@RequestParam(value = "mainOrderNo") String mainOrderNo){
        InitChangeStatusVO initChangeStatusVO = new InitChangeStatusVO();
        List<StorageOutOrder> list = this.storageOutOrderService.getByCondition(new StorageOutOrder().setMainOrderNo(mainOrderNo));
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(list)) {
            StorageOutOrder tmp = list.get(0);
            initChangeStatusVO.setId(tmp.getId());
            initChangeStatusVO.setOrderNo(tmp.getOrderNo());
            initChangeStatusVO.setOrderType(CommonConstant.CCE);
            initChangeStatusVO.setOrderTypeDesc(CommonConstant.CCE_DESC);
            initChangeStatusVO.setStatus(tmp.getProcessStatus() + "");
            initChangeStatusVO.setNeedInputCost(tmp.getNeedInputCost());
            return ApiResult.ok(initChangeStatusVO);
        }
        return ApiResult.error();
    }

    /**
     * 获取仓储入库订单号
     */
    @RequestMapping(value = "/api/storage/getStorageInOrderNo")
    public ApiResult<InitChangeStatusVO> getStorageInOrderNo(@RequestParam(value = "mainOrderNo") String mainOrderNo){
        InitChangeStatusVO initChangeStatusVO = new InitChangeStatusVO();
        List<StorageInputOrder> list = this.storageInputOrderService.getByCondition(new StorageInputOrder().setMainOrderNo(mainOrderNo));
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(list)) {
            StorageInputOrder tmp = list.get(0);
            initChangeStatusVO.setId(tmp.getId());
            initChangeStatusVO.setOrderNo(tmp.getOrderNo());
            initChangeStatusVO.setOrderType(CommonConstant.CCI);
            initChangeStatusVO.setOrderTypeDesc(CommonConstant.CCI_DESC);
            initChangeStatusVO.setStatus(tmp.getProcessStatus() + "");
            initChangeStatusVO.setNeedInputCost(tmp.getNeedInputCost());
            return ApiResult.ok(initChangeStatusVO);
        }
        return ApiResult.error();
    }

    /**
     * 获取仓储快进快出订单号
     */
    @RequestMapping(value = "/api/storage/getStorageFastOrderNo")
    public ApiResult<InitChangeStatusVO> getStorageFastOrderNo(@RequestParam(value = "mainOrderNo") String mainOrderNo){
        InitChangeStatusVO initChangeStatusVO = new InitChangeStatusVO();
        List<StorageFastOrder> list = this.storageFastOrderService.getByCondition(new StorageFastOrder().setMainOrderNo(mainOrderNo));
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(list)) {
            StorageFastOrder tmp = list.get(0);
            initChangeStatusVO.setId(tmp.getId());
            initChangeStatusVO.setOrderNo(tmp.getOrderNo());
            initChangeStatusVO.setOrderType(CommonConstant.CCF);
            initChangeStatusVO.setOrderTypeDesc(CommonConstant.CCF_DESC);
            initChangeStatusVO.setStatus( ProcessStatusEnum.CLOSE.getCode().equals(tmp.getProcessStatus())?"CLOSE":tmp.getProcessStatus()+"");
            initChangeStatusVO.setNeedInputCost(tmp.getNeedInputCost());
            return ApiResult.ok(initChangeStatusVO);
        }
        return ApiResult.error();
    }

    /**
     * 关闭订单
     */
    @RequestMapping(value = "/api/storage/closeInOrder")
    public ApiResult closeInOrder(@RequestBody List<SubOrderCloseOpt> form){
        List<String> orderNos = form.stream().map(SubOrderCloseOpt::getOrderNo).collect(Collectors.toList());
        List<StorageInputOrder> list = this.storageInputOrderService.getOrdersByOrderNos(orderNos);
        Map<String, StorageInputOrder> map = list.stream().collect(Collectors.toMap(StorageInputOrder::getOrderNo, e -> e));

        for (SubOrderCloseOpt subOrderCloseOpt : form) {
            StorageInputOrder tmp = map.get(subOrderCloseOpt.getOrderNo());
            StorageInputOrder storageInputOrder = new StorageInputOrder();
            storageInputOrder.setId(tmp.getId());
            storageInputOrder.setProcessStatus(ProcessStatusEnum.CLOSE.getCode());
            storageInputOrder.setNeedInputCost(subOrderCloseOpt.getNeedInputCost());
            storageInputOrder.setUpdateUser(subOrderCloseOpt.getLoginUser());
            storageInputOrder.setUpdateTime(LocalDateTime.now());

            this.storageInputOrderService.updateById(storageInputOrder);

        }
        return ApiResult.ok();
    }

    /**
     * 关闭订单
     */
    @RequestMapping(value = "/api/storage/closeOutOrder")
    public ApiResult closeOutOrder(@RequestBody List<SubOrderCloseOpt> form){
        List<String> orderNos = form.stream().map(SubOrderCloseOpt::getOrderNo).collect(Collectors.toList());
        List<StorageOutOrder> list = this.storageOutOrderService.getOrdersByOrderNos(orderNos);
        Map<String, StorageOutOrder> map = list.stream().collect(Collectors.toMap(StorageOutOrder::getOrderNo, e -> e));

        for (SubOrderCloseOpt subOrderCloseOpt : form) {
            StorageOutOrder tmp = map.get(subOrderCloseOpt.getOrderNo());
            StorageOutOrder storageOutOrder = new StorageOutOrder();
            storageOutOrder.setId(tmp.getId());
            storageOutOrder.setProcessStatus(ProcessStatusEnum.CLOSE.getCode());
            storageOutOrder.setNeedInputCost(subOrderCloseOpt.getNeedInputCost());
            storageOutOrder.setUpdateUser(subOrderCloseOpt.getLoginUser());
            storageOutOrder.setUpdateTime(LocalDateTime.now());

            this.storageOutOrderService.updateById(storageOutOrder);

        }
        return ApiResult.ok();
    }

    /**
     * 关闭订单
     */
    @RequestMapping(value = "/api/storage/closeFastOrder")
    public ApiResult closeFastOrder(@RequestBody List<SubOrderCloseOpt> form){
        List<String> orderNos = form.stream().map(SubOrderCloseOpt::getOrderNo).collect(Collectors.toList());
        List<StorageFastOrder> list = this.storageFastOrderService.getOrdersByOrderNos(orderNos);
        Map<String, StorageFastOrder> map = list.stream().collect(Collectors.toMap(StorageFastOrder::getOrderNo, e -> e));

        for (SubOrderCloseOpt subOrderCloseOpt : form) {
            StorageFastOrder tmp = map.get(subOrderCloseOpt.getOrderNo());
            StorageFastOrder storageFastOrder = new StorageFastOrder();
            storageFastOrder.setId(tmp.getId());
            storageFastOrder.setProcessStatus(ProcessStatusEnum.CLOSE.getCode());
            storageFastOrder.setNeedInputCost(subOrderCloseOpt.getNeedInputCost());
            storageFastOrder.setUpdateUser(subOrderCloseOpt.getLoginUser());
            storageFastOrder.setUpdateTime(LocalDateTime.now());

            this.storageFastOrderService.updateById(storageFastOrder);

        }
        return ApiResult.ok();
    }

    @ApiOperation(value = "下拉启用的仓库")
    @RequestMapping(value = "/api/storage/initEnableWarehouse")
    public CommonResult<List<InitComboxStrVO>> initEnableWarehouse(@RequestParam(value = "name", required = false) String name) {
        List<Warehouse> list = warehouseService.initEnableWarehouse(name);
        List<InitComboxStrVO> comboxStrVOS = new ArrayList<>();
        for (Warehouse warehouse : list) {
            InitComboxStrVO comboxStrVO = new InitComboxStrVO();
            comboxStrVO.setId(warehouse.getId());
            comboxStrVO.setCode(warehouse.getCode());
            comboxStrVO.setName(warehouse.getName());
            comboxStrVOS.add(comboxStrVO);
        }

        return CommonResult.success(comboxStrVOS);
    }

    @ApiOperation(value = "根据id查询仓库名称")
    @RequestMapping("/api/storage/findWarehouseNameById")
    public CommonResult<Warehouse> findWarehouseNameById(@RequestParam(value = "id", required = true) Long id){
        Warehouse warehouse = warehouseService.findWarehouseNameById(id);
        return CommonResult.success(warehouse);
    }
}
