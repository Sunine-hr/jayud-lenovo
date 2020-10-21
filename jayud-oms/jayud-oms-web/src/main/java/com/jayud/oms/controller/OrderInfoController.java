package com.jayud.oms.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.model.bo.*;
import com.jayud.oms.model.vo.*;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/orderInfo")
@Api(tags = "主订单接口")
public class OrderInfoController {

    @Autowired
    IOrderInfoService orderInfoService;

    @ApiOperation(value = "外部报关放行/通过前审核/订单列表/费用审核")
    @PostMapping("/findOrderInfoByPage")
    public CommonResult<Map<String,Object>> findOrderInfoByPage(@RequestBody QueryOrderInfoForm form) {
        IPage<OrderInfoVO> pageList = orderInfoService.findOrderInfoByPage(form);
        CommonPageResult<OrderInfoVO> pageVO = new CommonPageResult(pageList);
        OrderDataCountVO orderDataCountVO = orderInfoService.countOrderData();
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put(CommonConstant.PAGE_LIST,pageVO);//分页数据
        resultMap.put(CommonConstant.ALL_COUNT,orderDataCountVO.getAllCount());//所有订单数量
        resultMap.put(CommonConstant.PRE_SUBMIT_COUNT,orderDataCountVO.getPreSubmitCount());//暂存数量
        resultMap.put(CommonConstant.DATA_NOT_ALL_COUNT,orderDataCountVO.getDataNotAllCount());//待补全数据量
        return CommonResult.success(resultMap);
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
            //报关资料是否齐全 1-齐全 0-不齐全 齐全时校验报关数据
            if(CommonConstant.VALUE_1.equals(inputMainOrderForm.getIsDataAll())) {
                //报关订单参数校验
                InputOrderCustomsForm inputOrderCustomsForm = form.getOrderCustomsForm();
                if (inputOrderCustomsForm == null ||
                        StringUtil.isNullOrEmpty(inputOrderCustomsForm.getPortCode()) ||
                        StringUtil.isNullOrEmpty(inputOrderCustomsForm.getPortName()) ||
                        inputOrderCustomsForm.getGoodsType() == null ||
                        StringUtil.isNullOrEmpty(inputOrderCustomsForm.getBizModel()) ||
                        StringUtil.isNullOrEmpty(inputOrderCustomsForm.getLegalName()) ||
                        inputOrderCustomsForm.getSubOrders() == null) {
                    return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
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
                    if (StringUtil.isNullOrEmpty(subOrderCustomsForm.getOrderNo())
                            || StringUtil.isNullOrEmpty(subOrderCustomsForm.getTitle())
                            || StringUtil.isNullOrEmpty(subOrderCustomsForm.getUnitCode())
                            || StringUtil.isNullOrEmpty(subOrderCustomsForm.getIsTitle())) {
                        return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
                    }
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

    @ApiOperation(value = "更改状态初始化订单下的子订单号")
    @PostMapping("/findSubOrderNo")
    public CommonResult<List<InitChangeStatusVO>> findSubOrderNo(@RequestBody @Valid GetOrderDetailForm form) {
        List<InitChangeStatusVO> changeStatusVOS = orderInfoService.findSubOrderNo(form);
        return CommonResult.success(changeStatusVOS);
    }

    @ApiOperation(value = "确认更改状态")
    @PostMapping("/changeStatus")
    public CommonResult changeStatus(@RequestBody @Valid ChangeStatusListForm form) {
        Boolean result = orderInfoService.changeStatus(form);
        if(!result){
            return CommonResult.error(ResultEnum.OPR_FAIL.getCode(),ResultEnum.OPR_FAIL.getMessage());
        }
        return CommonResult.success();
    }




}

