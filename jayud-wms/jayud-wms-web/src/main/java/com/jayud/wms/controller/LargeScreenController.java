package com.jayud.wms.controller;

import com.jayud.common.BaseResult;
import com.jayud.wms.model.bo.LargeScreen.WarehouseForm;
import com.jayud.wms.model.vo.LargeScreen.OrderCountVO;
import com.jayud.wms.model.vo.LargeScreen.WareLocationUseStatusVO;
import com.jayud.wms.service.LargeScreenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author ciro
 * @date 2022/4/11 10:20
 * @description: 大屏看板
 */
@Api(tags = "大屏看板")
@RestController
@RequestMapping("/largeScreen")
public class LargeScreenController {

    @Autowired
    private LargeScreenService largeScreenService;

    @ApiOperation("获取库位使用情况")
    @PostMapping("/getWarehouseLocationUserStatus")
    public BaseResult<WareLocationUseStatusVO> getWarehouseLocationUserStatus(@RequestBody WarehouseForm warehouseForm){
        return BaseResult.ok(largeScreenService.getWarehouseLocationUserStatus(warehouseForm));
    }

    @ApiOperation("获取完成订单数量")
    @PostMapping("/getFinishOrderCount")
    public BaseResult<OrderCountVO> getFinishOrderCount(@RequestBody WarehouseForm warehouseForm){
        return BaseResult.ok(largeScreenService.getFinishOrderCount(warehouseForm));
    }

    @ApiOperation("获取未完成订单数据")
    @PostMapping("/getUnfinsihOrderMsg")
    public BaseResult<OrderCountVO> getUnfinsihOrderMsg(@RequestBody WarehouseForm warehouseForm){
        return BaseResult.ok(largeScreenService.getUnfinsihOrderMsg(warehouseForm));
    }

    @ApiOperation("获取订单曲线图")
    @PostMapping("/getOrderLineMsg")
    public BaseResult<OrderCountVO> getOrderLineMsg(@RequestBody WarehouseForm warehouseForm){
        return BaseResult.ok(largeScreenService.getOrderLineMsg(warehouseForm));
    }
}
