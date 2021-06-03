package com.jayud.storage.controller;


import cn.hutool.core.map.MapUtil;
import com.jayud.common.CommonResult;
import com.jayud.common.entity.InitComboxStrVO;
import com.jayud.common.entity.InitComboxVO;
import com.jayud.storage.feign.OmsClient;
import com.jayud.storage.model.bo.RelocationRecordForm;
import com.jayud.storage.model.po.GoodsLocationRecord;
import com.jayud.storage.model.po.InGoodsOperationRecord;
import com.jayud.storage.model.vo.InitComboxNumberVO;
import com.jayud.storage.service.IGoodsLocationRecordService;
import com.jayud.storage.service.IInGoodsOperationRecordService;
import com.jayud.storage.service.IRelocationRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 移库信息表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-05-19
 */
@RestController
@Api(tags = "移库订单管理")
@RequestMapping("/relocationRecord")
public class RelocationRecordController {

    @Autowired
    private IRelocationRecordService relocationRecordService;

    @Autowired
    private OmsClient omsClient;

    @Autowired
    private IInGoodsOperationRecordService inGoodsOperationRecordService;

    @Autowired
    private IGoodsLocationRecordService goodsLocationRecordService;

    @ApiOperation(value = "创建移库订单")
    @PostMapping("/createRelocationOrder")
    public CommonResult createRelocationOrder(@RequestBody RelocationRecordForm form){
        boolean result = relocationRecordService.createRelocationOrder(form);
        if(!result){
            return CommonResult.error(444,"创建移库订单失败");
        }
        return CommonResult.success();
    }

    @ApiOperation(value = "获取移库订单号")
    @PostMapping("/getOrderNo")
    public CommonResult getOrderNo(){
        String orderNo = (String)omsClient.getWarehouseNumber("YK").getData();
        if(orderNo != null){
            return CommonResult.success(orderNo);
        }
        return CommonResult.error(444,"获取移库订单失败");
    }

    @ApiOperation(value = "根据库位和sku获取商品入库批次号的下拉列表")
    @PostMapping("/getWarehousingBatchNoComBox")
    public CommonResult getWarehousingBatchNoComBox(@RequestBody Map<String,Object> map){
        String kuCode = MapUtil.getStr(map, "kuCode");
        String sku = MapUtil.getStr(map, "sku");

        //获取所有该商品的入库批次号，且在该库位的
        List<String> batchNoComBox = inGoodsOperationRecordService.getWarehousingBatchNoComBox(kuCode,sku);
        List<InitComboxNumberVO> initComboxNumberVOS = new ArrayList<>();
        for (String noComBox : batchNoComBox) {
            GoodsLocationRecord goodsLocationRecordBySkuAndKuCode = goodsLocationRecordService.getGoodsLocationRecordBySkuAndKuCode(kuCode, noComBox, sku);
            InitComboxNumberVO initComboxNumberVO = new InitComboxNumberVO();
            initComboxNumberVO.setName(noComBox);
            initComboxNumberVO.setNumber(goodsLocationRecordBySkuAndKuCode.getUnDeliveredQuantity());
            initComboxNumberVOS.add(initComboxNumberVO);
        }
        return CommonResult.success(initComboxNumberVOS);
    }

}

