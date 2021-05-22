package com.jayud.storage.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONArray;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.utils.StringUtils;
import com.jayud.storage.feign.OmsClient;
import com.jayud.storage.model.bo.QueryStockForm;
import com.jayud.storage.model.bo.QueryStorageOrderForm;
import com.jayud.storage.model.vo.*;
import com.jayud.storage.service.IInGoodsOperationRecordService;
import com.jayud.storage.service.IStockService;
import com.jayud.storage.service.IWarehouseGoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>
 * 库存表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-04-29
 */
@RestController
@Api(tags = "库存管理")
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private OmsClient omsClient;

    @Autowired
    private IStockService stockService;

    @Autowired
    private IInGoodsOperationRecordService inGoodsOperationRecordService;

    @Autowired
    private IWarehouseGoodsService warehouseGoodsService;

    @ApiOperation("库存查询")
    @PostMapping("/findByPage")
    public CommonResult findByPage(@RequestBody QueryStockForm form) {

        //模糊查询客户信息
        if (!StringUtils.isEmpty(form.getCustomerName())) {
            ApiResult result = omsClient.getCustomerIdByCustomerName(form.getCustomerName());
            Object data = result.getData();
            if (data != null && ((List) data).size() > 0) {
                JSONArray mainOrders = new JSONArray(data);
                form.assemblyMainOrderNo(mainOrders);
            } else {
                form.setCustomerIds(Collections.singletonList("-1"));
            }
        }

        List list = new ArrayList();

        Class<StockVO> stockVOClass = StockVO.class;
        Field[] declaredFields = stockVOClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            ApiModelProperty annotation = declaredField.getAnnotation(ApiModelProperty.class);
            if (annotation != null) {
                Map map = new HashMap<>();
                map.put("key", declaredField.getName());
                map.put("name", annotation.value());
                list.add(map);
            }
        }


        Map map1 = new HashMap();
        map1.put("header", list);
        IPage<StockVO> page = this.stockService.findByPage(form);
        if (page.getRecords().size() == 0) {
            map1.put("pageInfo", new CommonPageResult(page));
            return CommonResult.success(map1);
        }
        for (StockVO record : page.getRecords()) {
            record.setCustomerName(omsClient.getCustomerNameById(record.getId()).getData());
        }
        map1.put("pageInfo", new CommonPageResult(page));
        return CommonResult.success(page);
    }

    @ApiOperation("查看库存记录")
    @PostMapping("/viewInventoryRecords")
    public CommonResult viewInventoryRecords(@RequestBody Map<String,Object> map) {
        //获取查询的库位以及该库位的商品
        String sku = MapUtil.getStr(map, "sku");
        String locationCode = MapUtil.getStr(map, "locationCode");
        Long customerId = MapUtil.getLong(map, "customer_id");

        //获取该库位该商品的可用库存
        StockLocationNumberVO stockLocationNumberVO = stockService.getListBySkuAndLocationCode(sku,locationCode,customerId);

        //获取入库操作记录
        List<InGoodsOperationRecordFormVO> inGoodsOperationRecordFormVOS = inGoodsOperationRecordService.getListBySkuAndLocationCode(sku,locationCode,customerId);
        List<OutGoodsOperationRecordFormVO> outGoodsOperationRecordFormVOS = warehouseGoodsService.getListBySkuAndLocationCode(sku,locationCode,customerId);
        return CommonResult.success();
    }

    //获取存仓时长
    public String getStorageTime(LocalDateTime time){
        return null;
    }
}


