package com.jayud.storage.controller;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.storage.model.bo.*;
import com.jayud.storage.model.po.*;
import com.jayud.storage.model.vo.*;
import com.jayud.storage.service.*;
import com.jayud.storage.utils.DateDayUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * PDA拣货订单管理 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-04-19
 */
@RestController
@Slf4j
@Api(tags = "PDA拣货订单管理")
@RequestMapping("/pickUpOrder")
public class PickUpOrderController {

    @Autowired
    private IStorageOutOrderService storageOutOrderService;

    @Autowired
    private IWarehouseGoodsService warehouseGoodsService;

    @Autowired
    private IGoodsLocationRecordService goodsLocationRecordService;

    @Autowired
    private IInGoodsOperationRecordService inGoodsOperationRecordService;

    @ApiOperation("通过订单号查询出库商品信息")
    @PostMapping("/findByOrderNo")
    public CommonResult findByOrderNo(@RequestBody QueryPickUpGoodForm form) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_no",form.getOrderNo());
        StorageOutOrder one = storageOutOrderService.getOne(queryWrapper);
        if(one == null){
            return CommonResult.error(444,"该订单不存在");
        }
        List<WarehouseGoodsLocationCodeVO> warehouseGoodsLocationVOS = storageOutOrderService.findByOrderNo(form);
        StorageOutOrderGoodVO storageOutOrderGoodVO = new StorageOutOrderGoodVO();
        storageOutOrderGoodVO.setWarehouseGoodsLocationCodeVOS(warehouseGoodsLocationVOS);
        if(CollectionUtils.isNotEmpty(warehouseGoodsLocationVOS)){
            if(warehouseGoodsLocationVOS.get(0).getExpectedDeliveryTime() != null){
                storageOutOrderGoodVO.setDefaultOutTime(warehouseGoodsLocationVOS.get(0).getExpectedDeliveryTime().toString());
            }
        }
        return CommonResult.success(storageOutOrderGoodVO);
    }

    @ApiOperation("通过订单号和sku查询出库商品信息")
    @PostMapping("/findByWarehousingBatchNo")
    public CommonResult findByWarehousingBatchNo(@RequestBody Map<String,Object> map) {
        String orderNo = MapUtil.getStr(map, "orderNo");
        String sku = MapUtil.getStr(map, "sku");

        if(orderNo != null && sku != null){
            List<WarehouseGoods> warehouseGoods = warehouseGoodsService.getListBySkuAndOrderNo(sku,orderNo);
            if(CollectionUtils.isEmpty(warehouseGoods)){
                return CommonResult.error(444,"该订单或者该sku不存在数据");
            }
            List<WarehouseGoodsLocationVO> warehouseGoodsLocationVOS = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(warehouseGoods)){
                for (WarehouseGoods warehouseGood : warehouseGoods) {
                    List<GoodsLocationRecordFormVO> outGoodsLocationRecordByGoodId = goodsLocationRecordService.getOutGoodsLocationRecordByGoodIdAndPicked(warehouseGood.getId());
                    WarehouseGoodsLocationVO convert = ConvertUtil.convert(warehouseGood, WarehouseGoodsLocationVO.class);

                    if(CollectionUtils.isNotEmpty(outGoodsLocationRecordByGoodId)){
                        convert.setDefaultLocation(outGoodsLocationRecordByGoodId.get(0).getKuCode());
                    }

                    convert.setGoodsLocationRecordForms(outGoodsLocationRecordByGoodId);

                    warehouseGoodsLocationVOS.add(convert);
                }
            }

            StorageOutOrderGoodVO storageOutOrderGoodVO = new StorageOutOrderGoodVO();

            //根据订单号获取所有未出库的数据
            List<WarehouseGoods> warehouseGoods1 = warehouseGoodsService.getOutListByOrderNo(orderNo);
            if(CollectionUtils.isEmpty(warehouseGoods1)){
                storageOutOrderGoodVO.setFinishPicking(true);
            }else{
                storageOutOrderGoodVO.setFinishPicking(false);
            }

            storageOutOrderGoodVO.setWarehouseGoodsLocationVOS(warehouseGoodsLocationVOS);
            storageOutOrderGoodVO.setDefaultBatchNo(warehouseGoods.get(0).getWarehousingBatchNo());
            return CommonResult.success(storageOutOrderGoodVO);
        }
        return CommonResult.success();
    }

    @ApiOperation("通过批次号和sku以及库位查询未出库的商品数量")
    @PostMapping("/findByWarehousingBatchNoAndSku")
    public CommonResult findByWarehousingBatchNoAndSku(@RequestBody Map<String,Object> map) {
        String warehousingBatchNo = MapUtil.getStr(map, "warehousingBatchNo");
        String sku = MapUtil.getStr(map, "sku");
        String kuCode = MapUtil.getStr(map, "kuCode");
        //获取该库位未出库的该商品数量
        GoodsLocationRecord goodsLocationRecordBySkuAndKuCode = goodsLocationRecordService.getGoodsLocationRecordBySkuAndKuCode(kuCode, warehousingBatchNo, sku);
        return CommonResult.success(goodsLocationRecordBySkuAndKuCode.getUnDeliveredQuantity());
    }

    @ApiOperation("商品拣货")
    @PostMapping("/goodsPicking")
    public CommonResult goodsPicking(@RequestBody WarehousePickingForm form , BindingResult result) {
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                return CommonResult.error(444, error.getDefaultMessage());
            }
        }
        if(form.getKuCode().equals(form.getScanningKuCode())){
            return CommonResult.error(444, "选择库位与扫描库位不一致");
        }
        if(form.getNumber().equals(form.getPickedNumber())){
            return CommonResult.error(444, "已拣数量和待拣数量不一致");
        }
        boolean b = storageOutOrderService.PDAWarehousePicking(form);
        if(!b){
            return CommonResult.error(444, "仓储拣货失败");
        }
        return CommonResult.success();
    }

    @ApiOperation("根据条件获取拣货订单记录")
    @PostMapping("/getListByQueryForm")
    public CommonResult getListByQueryForm(@RequestBody QueryPutGoodForm form) {
        if(form.getCreateTime().length <= 0){
            String[] strings = new String[]{DateDayUtils.getFirst(),DateDayUtils.getLast()};
            form.setCreateTime(strings);
        }
        form.setStartTime();
        List<OnShelfOrderVO> onShelfOrderVOS = storageOutOrderService.getListByQueryForm(form);
        return CommonResult.success(onShelfOrderVOS);
    }

    @ApiOperation("根据条件获取拣货订单记录详情")
    @PostMapping("/getOrderDetails")
    public CommonResult getOrderDetails(@RequestBody Map<String,Object> map) {
        String warehousingBatchNo = MapUtil.getStr(map, "warehousingBatchNo");
        String orderNo = MapUtil.getStr(map, "orderNo");
        List<WarehouseGoodsVO> warehouseGoodsVOS = warehouseGoodsService.getListByWarehousingBatchNoAndOrderNo(warehousingBatchNo,orderNo);
        return CommonResult.success(warehouseGoodsVOS);
    }


}
