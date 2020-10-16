package com.jayud.oms.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.model.bo.*;
import com.jayud.oms.model.vo.InputOrderVO;
import com.jayud.oms.model.vo.OrderInfoVO;
import com.jayud.oms.model.vo.OrderStatusVO;
import com.jayud.oms.service.IOrderInfoService;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/orderInfo")
@Api(tags = "主订单接口")
public class OrderInfoController {

    @Autowired
    IOrderInfoService orderInfoService;

    @ApiOperation(value = "未提交/提交订单列表/费用审核")
    @PostMapping("/findOrderInfoByPage")
    public CommonResult<CommonPageResult<OrderInfoVO>> findOrderInfoByPage(@RequestBody QueryOrderInfoForm form) {
        IPage<OrderInfoVO> pageList = orderInfoService.findOrderInfoByPage(form);
        CommonPageResult<OrderInfoVO> pageVO = new CommonPageResult(pageList);
        return CommonResult.success(pageVO);
    }

    @ApiOperation(value = "主订单流程获取")
    @PostMapping("/handleProcess")
    public CommonResult<List<OrderStatusVO>> handleProcess(@RequestBody QueryOrderStatusForm form) {
        List<OrderStatusVO> orderStatusVOS = orderInfoService.handleProcess(form);
        return CommonResult.success(orderStatusVOS);
    }

    //订单详情接口
    @ApiOperation(value = "订单详情")
    @PostMapping("/getOrderDetail")
    public CommonResult<InputOrderVO> getOrderDetail(@RequestBody @Valid GetOrderDetailForm form) {
        InputOrderVO inputOrderVO = orderInfoService.getOrderDetail(form);
        return CommonResult.success(inputOrderVO);
    }

    @ApiOperation(value = "客户-创建订单暂存和提交")
    @PostMapping("/createOrder")
    public CommonResult createOrder(@RequestBody InputOrderForm form){
        //通用参数校验
        if(form == null || StringUtil.isNullOrEmpty(form.getCmd()) ||
           form.getOrderForm() == null || "".equals(form.getOrderForm())){
            return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMessage());
        }
        if(CommonConstant.SUBMIT.equals(form.getCmd())){
            //主订单参数校验
            InputMainOrderForm inputMainOrderForm = form.getOrderForm();
            if(inputMainOrderForm == null || StringUtil.isNullOrEmpty(inputMainOrderForm.getCustomerCode())
              || StringUtil.isNullOrEmpty(inputMainOrderForm.getCustomerName())
              || inputMainOrderForm.getBizUid() == null
              || StringUtil.isNullOrEmpty(inputMainOrderForm.getBizUname())
              || StringUtil.isNullOrEmpty(inputMainOrderForm.getLegalName())
              || inputMainOrderForm.getBizBelongDepart() == null
              || StringUtil.isNullOrEmpty(inputMainOrderForm.getBizCode())
              || StringUtil.isNullOrEmpty(inputMainOrderForm.getClassCode())
              || StringUtil.isNullOrEmpty(inputMainOrderForm.getSelectedServer())
              || StringUtil.isNullOrEmpty(inputMainOrderForm.getUnitCode())
              || StringUtil.isNullOrEmpty(inputMainOrderForm.getUnitAccount())
              || StringUtil.isNullOrEmpty(inputMainOrderForm.getIsDataAll())){
                return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMessage());
            }
            //报关订单参数校验
            InputOrderCustomsForm inputOrderCustomsForm = form.getOrderCustomsForm();
            if(inputOrderCustomsForm == null ||
               StringUtil.isNullOrEmpty(inputOrderCustomsForm.getPortCode()) ||
               StringUtil.isNullOrEmpty(inputOrderCustomsForm.getPortName()) ||
               inputOrderCustomsForm.getGoodsType() == null ||
               StringUtil.isNullOrEmpty(inputOrderCustomsForm.getBizModel()) ||
               StringUtil.isNullOrEmpty(inputOrderCustomsForm.getLegalName()) ||
               inputOrderCustomsForm.getSubOrders() == null){
                return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMessage());
            }
            //附件处理
            inputOrderCustomsForm.setCntrPic(StringUtils.getFileStr(inputOrderCustomsForm.getCntrPics()));
            inputOrderCustomsForm.setCntrPicName(StringUtils.getFileNameStr(inputOrderCustomsForm.getCntrPics()));
            inputOrderCustomsForm.setEncodePic(StringUtils.getFileStr(inputOrderCustomsForm.getEncodePics()));
            inputOrderCustomsForm.setEncodePicName(StringUtils.getFileNameStr(inputOrderCustomsForm.getEncodePics()));
            inputOrderCustomsForm.setAirTransportPic(StringUtils.getFileStr(inputOrderCustomsForm.getAirTransportPics()));
            inputOrderCustomsForm.setAirTransPicName(StringUtils.getFileNameStr(inputOrderCustomsForm.getAirTransportPics()));
            inputOrderCustomsForm.setSeaTransportPic(StringUtils.getFileStr(inputOrderCustomsForm.getAirTransportPics()));
            inputOrderCustomsForm.setSeaTransPicName(StringUtils.getFileNameStr(inputOrderCustomsForm.getAirTransportPics()));
            //报关订单中的子订单
            List<InputSubOrderCustomsForm> subOrders = inputOrderCustomsForm.getSubOrders();
            for (InputSubOrderCustomsForm subOrderCustomsForm : subOrders) {
                if(StringUtil.isNullOrEmpty(subOrderCustomsForm.getOrderNo())
                        || StringUtil.isNullOrEmpty(subOrderCustomsForm.getTitle())
                        || StringUtil.isNullOrEmpty(subOrderCustomsForm.getUnitCode())
                        || StringUtil.isNullOrEmpty(subOrderCustomsForm.getIsTitle())) {
                    return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMessage());
                }
            }
            //中港订单参数校验
            InputOrderTransportForm inputOrderTransportForm = form.getOrderTransportForm();
            if(inputOrderTransportForm == null ||
            StringUtil.isNullOrEmpty(inputOrderTransportForm.getPortCode()) ||
            inputOrderTransportForm.getGoodsType() == null ||
            StringUtil.isNullOrEmpty(inputOrderTransportForm.getVehicleType()) ||
            StringUtil.isNullOrEmpty(inputOrderTransportForm.getVehicleSize()) ||
            inputOrderTransportForm.getWarehouseInfoId() == null ||
            StringUtil.isNullOrEmpty(inputOrderTransportForm.getLegalName()) ||
            StringUtil.isNullOrEmpty(inputOrderTransportForm.getUnitCode())){
                return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(),ResultEnum.PARAM_ERROR.getMessage());
            }
        }
        boolean result = orderInfoService.createOrder(form);
        if(!result) {
            return CommonResult.error(ResultEnum.OPR_FAIL.getCode(),ResultEnum.OPR_FAIL.getMessage());
        }
        return CommonResult.success();
    }



}

