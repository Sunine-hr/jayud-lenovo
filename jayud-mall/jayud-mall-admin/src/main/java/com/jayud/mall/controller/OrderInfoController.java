package com.jayud.mall.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.mall.model.bo.*;
import com.jayud.mall.model.vo.OrderClearanceFileVO;
import com.jayud.mall.model.vo.OrderCustomsFileVO;
import com.jayud.mall.model.vo.OrderInfoVO;
import com.jayud.mall.service.IOrderInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orderinfo")
@Api(tags = "订单接口")
public class OrderInfoController {

    @Autowired
    IOrderInfoService orderInfoService;

    @ApiOperation(value = "分页查询订单")
    @PostMapping("/findOrderInfoByPage")
    public CommonResult<CommonPageResult<OrderInfoVO>> findOrderInfoByPage(@RequestBody QueryOrderInfoForm form) {
        IPage<OrderInfoVO> pageList = orderInfoService.findOrderInfoByPage(form);
        CommonPageResult<OrderInfoVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "订单管理-查看审核文件")
    @PostMapping("/lookOrderInfoFile")
    public CommonResult<OrderInfoVO> lookOrderInfoFile(@RequestBody OrderInfoForm form){
        Long id = form.getId();
        return orderInfoService.lookOrderInfoFile(id);
    }

    //pass
    @ApiOperation(value = "审核通过-订单对应报关文件")
    @PostMapping("/passOrderCustomsFile")
    public CommonResult<OrderCustomsFileVO> passOrderCustomsFile(@RequestBody OrderCustomsFileForm form){
        Long id = form.getId();
        return orderInfoService.passOrderCustomsFile(id);
    }

    @ApiOperation(value = "审核通过-订单对应清关文件")
    @PostMapping("/passOrderClearanceFile")
    public CommonResult<OrderClearanceFileVO> passOrderClearanceFile(@RequestBody OrderClearanceFileForm form){
        Long id = form.getId();
        return orderInfoService.passOrderClearanceFile(id);
    }

    //no pass
    @ApiOperation(value = "审核不通过-订单对应报关文件")
    @PostMapping("/onPassCustomsFile")
    public CommonResult<OrderCustomsFileVO> onPassCustomsFile(@RequestBody OrderCustomsFileForm form){
        Long id = form.getId();
        return orderInfoService.onPassCustomsFile(id);
    }

    @ApiOperation(value = "审核不通过-订单对应清关文件")
    @PostMapping("/onPassOrderClearanceFile")
    public CommonResult<OrderClearanceFileVO> onPassOrderClearanceFile(@RequestBody OrderClearanceFileForm form){
        Long id = form.getId();
        return orderInfoService.onPassOrderClearanceFile(id);
    }

    //订单管理-查看货物信息
    @ApiOperation(value = "订单管理-查看货物信息")
    @PostMapping("/lookOrderInfoGoods")
    public CommonResult<OrderInfoVO> lookOrderInfoGoods(@RequestBody OrderInfoForm form){
        Long id = form.getId();
        return orderInfoService.lookOrderInfoGoods(id);
    }

    @ApiOperation(value = "订单管理-修改订单箱号(长宽高等)")
    @PostMapping("/updateOrderCase")
    public CommonResult updateOrderCase(@RequestBody List<OrderCaseForm> list){
        orderInfoService.updateOrderCase(list);
        return CommonResult.success("保存箱号信息，成功！");
    }

    //订单管理-查看配载信息
    @ApiOperation(value = "订单管理-查看配载信息")
    @PostMapping("/lookOrderInfoConf")
    public CommonResult<OrderInfoVO> lookOrderInfoConf(@RequestBody OrderInfoForm form){
        Long id = form.getId();
        return orderInfoService.lookOrderInfoConf(id);
    }


}
