package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.*;
import com.jayud.mall.model.vo.OrderClearanceFileVO;
import com.jayud.mall.model.vo.OrderCustomsFileVO;
import com.jayud.mall.model.vo.OrderInfoVO;
import com.jayud.mall.service.IOrderInfoService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orderinfo")
@Api(tags = "S044-后台-订单接口")
@ApiSort(value = 44)
public class OrderInfoController {

    @Autowired
    IOrderInfoService orderInfoService;

    @ApiOperation(value = "分页查询订单")
    @PostMapping("/findOrderInfoByPage")
    @ApiOperationSupport(order = 1)
    public CommonResult<CommonPageResult<OrderInfoVO>> findOrderInfoByPage(@RequestBody QueryOrderInfoForm form) {
        IPage<OrderInfoVO> pageList = orderInfoService.findOrderInfoByPage(form);
        CommonPageResult<OrderInfoVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "订单管理-查看审核文件")
    @PostMapping("/lookOrderInfoFile")
    @ApiOperationSupport(order = 2)
    public CommonResult<OrderInfoVO> lookOrderInfoFile(@RequestBody OrderInfoForm form){
        Long id = form.getId();
        return orderInfoService.lookOrderInfoFile(id);
    }

    //pass
    @ApiOperation(value = "审核通过-订单对应报关文件")
    @PostMapping("/passOrderCustomsFile")
    @ApiOperationSupport(order = 3)
    public CommonResult<OrderCustomsFileVO> passOrderCustomsFile(@RequestBody OrderCustomsFileForm form){
        Long id = form.getId();
        return orderInfoService.passOrderCustomsFile(id);
    }

    @ApiOperation(value = "审核通过-订单对应清关文件")
    @PostMapping("/passOrderClearanceFile")
    @ApiOperationSupport(order = 4)
    public CommonResult<OrderClearanceFileVO> passOrderClearanceFile(@RequestBody OrderClearanceFileForm form){
        Long id = form.getId();
        return orderInfoService.passOrderClearanceFile(id);
    }

    //no pass
    @ApiOperation(value = "审核不通过-订单对应报关文件")
    @PostMapping("/onPassCustomsFile")
    @ApiOperationSupport(order = 5)
    public CommonResult<OrderCustomsFileVO> onPassCustomsFile(@RequestBody OrderCustomsFileForm form){
        Long id = form.getId();
        return orderInfoService.onPassCustomsFile(id);
    }

    @ApiOperation(value = "审核不通过-订单对应清关文件")
    @PostMapping("/onPassOrderClearanceFile")
    @ApiOperationSupport(order = 6)
    public CommonResult<OrderClearanceFileVO> onPassOrderClearanceFile(@RequestBody OrderClearanceFileForm form){
        Long id = form.getId();
        return orderInfoService.onPassOrderClearanceFile(id);
    }

    //订单管理-查看货物信息
    @ApiOperation(value = "订单管理-查看货物信息")
    @PostMapping("/lookOrderInfoGoods")
    @ApiOperationSupport(order = 7)
    public CommonResult<OrderInfoVO> lookOrderInfoGoods(@RequestBody OrderInfoForm form){
        Long id = form.getId();
        return orderInfoService.lookOrderInfoGoods(id);
    }

    @ApiOperation(value = "订单管理-修改订单箱号(长宽高等)")
    @PostMapping("/updateOrderCase")
    @ApiOperationSupport(order = 8)
    public CommonResult updateOrderCase(@RequestBody List<OrderCaseForm> list){
        orderInfoService.updateOrderCase(list);
        return CommonResult.success("保存箱号信息，成功！");
    }

    //订单管理-查看配载信息
    @ApiOperation(value = "订单管理-查看配载信息")
    @PostMapping("/lookOrderInfoConf")
    @ApiOperationSupport(order = 9)
    public CommonResult<OrderInfoVO> lookOrderInfoConf(@RequestBody OrderInfoForm form){
        Long id = form.getId();
        return orderInfoService.lookOrderInfoConf(id);
    }

    //订单管理-修改配载信息
    @ApiOperation(value = "订单管理-修改配载信息(待完成)")
    @PostMapping("/updateOrderCaseConf")
    @ApiOperationSupport(order = 10)
    public CommonResult updateOrderCaseConf(@RequestBody List<OrderCaseForm> list){
        orderInfoService.updateOrderCaseConf(list);
        return CommonResult.success("订单管理-修改配载信息(待完成)");
    }

    //订单管理-查看费用信息
    @ApiOperation(value = "订单管理-查看费用信息")
    @PostMapping("/lookOrderInfoCost")
    @ApiOperationSupport(order = 11)
    public CommonResult<OrderInfoVO> lookOrderInfoCost(@RequestBody OrderInfoForm form){
        Long id = form.getId();
        return orderInfoService.lookOrderInfoCost(id);
    }

    //订单管理-修改费用信息
    @ApiModelProperty(value = "订单管理-修改费用信息")
    @PostMapping("/updateOrderInfoCost")
    @ApiOperationSupport(order = 12)
    public CommonResult updateOrderInfoCost(@RequestBody OrderInfoCostForm form){
        return orderInfoService.updateOrderInfoCost(form);
    }

    //订单管理-查看订单详细
    @ApiOperation(value = "订单管理-查看订单详细")
    @PostMapping("/lookOrderInfoDetails")
    @ApiOperationSupport(order = 13)
    public CommonResult<OrderInfoVO> lookOrderInfoDetails(@RequestBody OrderInfoForm form){
        Long id = form.getId();
        return orderInfoService.lookOrderInfoDetails(id);
    }


}
