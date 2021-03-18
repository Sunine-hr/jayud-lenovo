package com.jayud.oms.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.enums.OrderAttachmentTypeEnum;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.enums.StatusEnum;
import com.jayud.common.utils.FileView;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.model.bo.*;
import com.jayud.oms.model.po.AuditInfo;
import com.jayud.oms.model.po.OrderAttachment;
import com.jayud.oms.model.po.OrderInfo;
import com.jayud.oms.model.vo.*;
import com.jayud.oms.service.IAuditInfoService;
import com.jayud.oms.service.IOrderAttachmentService;
import com.jayud.oms.service.IOrderInfoService;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.*;


@RestController
@RequestMapping("/orderInfo")
@Api(tags = "主订单接口")
@Slf4j
public class OrderInfoController {

    @Autowired
    IOrderInfoService orderInfoService;
    @Autowired
    IAuditInfoService auditInfoService;
    @Autowired
    private IOrderAttachmentService orderAttachmentService;

    //获取订单列表，要判断账号所属法人主体，根据法人主体分页查询订单

    @ApiOperation(value = "外部报关放行/通过前审核/订单列表/费用审核")
    @PostMapping("/findOrderInfoByPage")
    public CommonResult<Map<String, Object>> findOrderInfoByPage(@RequestBody QueryOrderInfoForm form) {
        IPage<OrderInfoVO> pageList = orderInfoService.findOrderInfoByPage(form);
        CommonPageResult<OrderInfoVO> pageVO = new CommonPageResult(pageList);
        OrderDataCountVO orderDataCountVO = orderInfoService.countOrderData(form);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(CommonConstant.PAGE_LIST, pageVO);//分页数据
        resultMap.put(CommonConstant.ALL_COUNT, orderDataCountVO.getAllCount());//所有订单数量
        resultMap.put(CommonConstant.PRE_SUBMIT_COUNT, orderDataCountVO.getPreSubmitCount());//暂存数量
        resultMap.put(CommonConstant.DATA_NOT_ALL_COUNT, orderDataCountVO.getDataNotAllCount());//待补全数据量
        resultMap.put(CommonConstant.CANCELLED_COUNT, orderDataCountVO.getCancelledCount());//待取消数据量
        resultMap.put(CommonConstant.REJECTED_COUNT, orderDataCountVO.getRejectedCount());//待驳回数据量
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
    public CommonResult createOrder(@RequestBody InputOrderForm form) {
        //通用参数校验
        if (form == null || StringUtil.isNullOrEmpty(form.getCmd()) ||
                form.getOrderForm() == null || "".equals(form.getOrderForm())) {
            return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
        }
        //主订单参数校验
        InputMainOrderForm inputMainOrderForm = form.getOrderForm();
        //待处理状态无法操作
        if (inputMainOrderForm.getOrderId() != null) {
            OrderInfo orderInfo = this.orderInfoService.getById(inputMainOrderForm.getOrderId());
            if (OrderStatusEnum.MAIN_8.getCode().equals(orderInfo.getStatus().toString())) {
                return CommonResult.error(400, "待处理状态,无法进行操作");
            }
        }

        if (inputMainOrderForm == null || StringUtil.isNullOrEmpty(inputMainOrderForm.getCustomerCode())
                || StringUtil.isNullOrEmpty(inputMainOrderForm.getCustomerName())
                || inputMainOrderForm.getBizUid() == null
                || StringUtil.isNullOrEmpty(inputMainOrderForm.getBizUname())
                || StringUtil.isNullOrEmpty(inputMainOrderForm.getLegalName())
                || inputMainOrderForm.getLegalEntityId() == null
                || inputMainOrderForm.getBizBelongDepart() == null
                || StringUtil.isNullOrEmpty(inputMainOrderForm.getBizCode())
                || StringUtil.isNullOrEmpty(inputMainOrderForm.getClassCode())
                || StringUtil.isNullOrEmpty(inputMainOrderForm.getSelectedServer())
                || StringUtil.isNullOrEmpty(inputMainOrderForm.getUnitCode())
                || StringUtil.isNullOrEmpty(inputMainOrderForm.getUnitAccount())
                || (StringUtil.isNullOrEmpty(inputMainOrderForm.getIsDataAll())
                && inputMainOrderForm.getSelectedServer().contains(OrderStatusEnum.CKBG.getCode()))) {
            return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
        }
        if (CommonConstant.SUBMIT.equals(form.getCmd())) {
            //1.报关资料是否齐全 1-齐全 0-不齐全 齐全时校验报关数据
            //2.纯报关时校验数据
            if (CommonConstant.VALUE_1.equals(inputMainOrderForm.getIsDataAll()) ||
                    OrderStatusEnum.CBG.getCode().equals(form.getOrderForm().getClassCode())) {
                //报关订单参数校验
                InputOrderCustomsForm inputOrderCustomsForm = form.getOrderCustomsForm();
                form.checkCustomsParam();
//                if (inputOrderCustomsForm == null ||
//                        StringUtil.isNullOrEmpty(inputOrderCustomsForm.getPortCode()) ||
//                        StringUtil.isNullOrEmpty(inputOrderCustomsForm.getPortName()) ||
//                        inputOrderCustomsForm.getGoodsType() == null ||
//                        StringUtil.isNullOrEmpty(inputOrderCustomsForm.getBizModel()) ||
//                        StringUtil.isNullOrEmpty(inputOrderCustomsForm.getLegalName()) ||
//                        inputOrderCustomsForm.getLegalEntityId() == null ||
//                        StringUtil.isNullOrEmpty(inputOrderCustomsForm.getEncode()) ||//六联单号
//                        inputOrderCustomsForm.getSubOrders() == null) {
//                    return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
//                }
//                //六联单号必须为13位的纯数字
//                String encode = inputOrderCustomsForm.getEncode();
//                if (!(encode.matches("[0-9]{1,}") && encode.length() == 13)) {
//                    return CommonResult.error(ResultEnum.ENCODE_PURE_NUMBERS);
//                }
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
//                List<InputSubOrderCustomsForm> subOrders = inputOrderCustomsForm.getSubOrders();
//                if (subOrders.size() == 0) {
//                    return CommonResult.error(ResultEnum.PARAM_ERROR);
//                }
//                for (InputSubOrderCustomsForm subOrderCustomsForm : subOrders) {
//                    if (StringUtil.isNullOrEmpty(subOrderCustomsForm.getOrderNo())
//                            || StringUtil.isNullOrEmpty(subOrderCustomsForm.getTitle())
//                            || StringUtil.isNullOrEmpty(subOrderCustomsForm.getUnitCode())
//                            || StringUtil.isNullOrEmpty(subOrderCustomsForm.getIsTitle())) {
//                        return CommonResult.error(ResultEnum.PARAM_ERROR);
//                    }
//                }
            }
            //中港订单参数校验
            if (OrderStatusEnum.ZGYS.getCode().equals(inputMainOrderForm.getClassCode())) {
                //中港订单参数校验
                InputOrderTransportForm inputOrderTransportForm = form.getOrderTransportForm();
                if (inputOrderTransportForm == null ||
                        StringUtil.isNullOrEmpty(inputOrderTransportForm.getPortCode()) ||
                        inputOrderTransportForm.getGoodsType() == null ||
                        inputOrderTransportForm.getVehicleType() == null ||
                        inputOrderTransportForm.getVehicleSize() == null ||
                        inputOrderTransportForm.getWarehouseInfoId() == null ||
                        StringUtil.isNullOrEmpty(inputOrderTransportForm.getLegalName()) ||
                        inputOrderTransportForm.getLegalEntityId() == null ||
                        StringUtil.isNullOrEmpty(inputOrderTransportForm.getUnitCode())) {
                    return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
                }
                //中港订单提货收货信息参数校验
                List<InputOrderTakeAdrForm> takeAdrForms1 = inputOrderTransportForm.getTakeAdrForms1();//必填
                List<InputOrderTakeAdrForm> takeAdrForms2 = inputOrderTransportForm.getTakeAdrForms2();
                //提货地址必填
                if (takeAdrForms1 == null || takeAdrForms1.size() == 0) {
                    return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
                }
                List<InputOrderTakeAdrForm> takeAdrForms = new ArrayList<>();
                takeAdrForms.addAll(takeAdrForms1);
                takeAdrForms.addAll(takeAdrForms2);
                for (InputOrderTakeAdrForm inputOrderTakeAdr : takeAdrForms) {
                    if (inputOrderTakeAdr.getAddress() == null
                            || inputOrderTakeAdr.getTakeTimeStr() == null || inputOrderTakeAdr.getPieceAmount() == null
                            || inputOrderTakeAdr.getWeight() == null || StringUtil.isNullOrEmpty(inputOrderTakeAdr.getGoodsDesc())) {
                        return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
                    }
                }
                //清关参数校验
                if (inputMainOrderForm.getSelectedServer().contains(OrderStatusEnum.XGQG.getCode())) {
                    if (StringUtil.isNullOrEmpty(inputOrderTransportForm.getHkLegalName()) ||
                            inputOrderTransportForm.getHkLegalId() == null ||
                            StringUtil.isNullOrEmpty(inputOrderTransportForm.getHkUnitCode()) ||
                            StringUtil.isNullOrEmpty(inputOrderTransportForm.getIsHkClear())) {
                        return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
                    }
                }
            }
            //空运校验参数
            if (OrderStatusEnum.KY.getCode().equals(inputMainOrderForm.getClassCode())) {
                InputAirOrderForm airOrderForm = form.getAirOrderForm();
                if (!airOrderForm.checkCreateOrder()) {
                    return CommonResult.error(ResultEnum.PARAM_ERROR);
                }
            }
            //服务单参数校验
            if (OrderStatusEnum.FWD.getCode().equals(inputMainOrderForm.getClassCode())) {
                InputOrderServiceForm orderServiceForm = form.getOrderServiceForm();
                if (orderServiceForm.getType() == null) {
                    return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
                }
            }
            //海运校验参数
            if (OrderStatusEnum.HY.getCode().equals(inputMainOrderForm.getClassCode())) {
                InputSeaOrderForm seaOrderForm = form.getSeaOrderForm();
                if (!seaOrderForm.checkCreateOrder()) {
                    return CommonResult.error(ResultEnum.PARAM_ERROR);
                }
            }
            //校验参数
            form.checkCreateParam();
        }

        boolean result = orderInfoService.createOrder(form);
        if (!result) {
            return CommonResult.error(ResultEnum.OPR_FAIL.getCode(), ResultEnum.OPR_FAIL.getMessage());
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
        if (!result) {
            return CommonResult.error(ResultEnum.OPR_FAIL.getCode(), ResultEnum.OPR_FAIL.getMessage());
        }
        return CommonResult.success();
    }


    @ApiOperation(value = "外部报关放行")
    @PostMapping(value = "/outCustomsRelease")
    public CommonResult outCustomsRelease(@RequestBody OprStatusForm form) {
        form.checkExternalCustomsDeclarationParam();
        //外部报关放行:1.对主订单放行  2.随时可操作  3.没有出口报关的中港运输的单才可进行外部报关放行,有出口报关的就进行报关模块的报关放行
        //外部报关放行不体现在流程节点中
        AuditInfo auditInfo = new AuditInfo();
        auditInfo.setExtId(form.getMainOrderId());
        auditInfo.setAuditTypeDesc(CommonConstant.OUT_CUSTOMS_RELEASE_DESC);
        auditInfo.setAuditStatus(OrderStatusEnum.EXT_CUSTOMS_RELEASE.getCode());
        auditInfo.setAuditComment(form.getDescription());
        auditInfo.setStatusFile(StringUtils.getFileStr(form.getFileViewList()));
        auditInfo.setStatusFileName(StringUtils.getFileNameStr(form.getFileViewList()));
        auditInfo.setAuditUser(UserOperator.getToken());
        auditInfo.setCreatedUser(UserOperator.getToken());
        auditInfo.setAuditTime(LocalDateTime.now());
        auditInfo.setExtDesc(SqlConstant.ORDER_INFO);
        auditInfoService.save(auditInfo);//保存操作记录
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setId(form.getMainOrderId());
        orderInfo.setCustomsRelease(true);
        orderInfo.setEncode(form.getEncode());
        orderInfo.setUpUser(UserOperator.getToken());
        orderInfo.setUpTime(LocalDateTime.now());
        boolean result = orderInfoService.updateById(orderInfo);

        //TODO 上传仓单文件/六联单号文件/上传舱单文件/上传报关单文件
        //查询主单信息
        OrderInfo tmp = this.orderInfoService.getById(form.getMainOrderId());
        //先把已有文件修改为禁用状态
        this.orderAttachmentService.update(tmp.getOrderNo(),
                Arrays.asList(OrderAttachmentTypeEnum.CUSTOMS_ATTACHMENT.getDesc(),
                        OrderAttachmentTypeEnum.MANIFEST_ATTACHMENT.getDesc(),
                        OrderAttachmentTypeEnum.SIX_SHEET_ATTACHMENT.getDesc())
                , new OrderAttachment().setStatus(StatusEnum.DISABLE.getCode()));

        Map<String, List<FileView>> attachment = form.assemblyAttachment();
        List<OrderAttachment> list = new ArrayList<>();
        attachment.forEach((k, v) -> {
            if (CollectionUtils.isNotEmpty(v)) {
                OrderAttachment orderAttachment = new OrderAttachment().setFileName(StringUtils.getFileNameStr(v))
                        .setFilePath(StringUtils.getFileStr(v))
                        .setMainOrderNo(tmp.getOrderNo()).setStatus(StatusEnum.ENABLE.getCode())
                        .setUploadTime(LocalDateTime.now())
                        .setRemarks(k);
                list.add(orderAttachment);
            }
        });
        if (CollectionUtils.isNotEmpty(list)) {
            this.orderAttachmentService.saveBatch(list);
        }

        if (!result) {
            return CommonResult.error(ResultEnum.OPR_FAIL.getCode(), ResultEnum.OPR_FAIL.getMessage());
        }
        return CommonResult.success();
    }


    @ApiOperation(value = "二期优化1：通关前审核，通关前复核")
    @PostMapping(value = "/initGoCustomsAudit")
    public CommonResult<InitGoCustomsAuditVO> initGoCustomsAudit(@RequestBody @Valid InitGoCustomsAuditForm form) {
        InitGoCustomsAuditVO initGoCustomsAuditVO = orderInfoService.initGoCustomsAudit(form);
        initGoCustomsAuditVO.setGoodsInfo(form.getGoodsInfo());
        return CommonResult.success(initGoCustomsAuditVO);
    }


    @ApiOperation(value = "取消待处理 mainOrderId=主订单id")
    @PostMapping("/cancelPending")
    public CommonResult cancelPending(@RequestBody @Valid Map<String, Long> map) {
        Long mainOrderId = MapUtil.getLong(map, "mainOrderId");
        if (mainOrderId == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        OrderInfo orderInfo = this.orderInfoService.getById(mainOrderId);
        if (!Integer.valueOf(OrderStatusEnum.MAIN_6.getCode()).equals(orderInfo.getStatus())) {
            log.warn("该订单不是处于待处理状态 status={}", orderInfo.getStatus());
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        this.orderInfoService.updateById(new OrderInfo().setId(mainOrderId)
                .setStatus(Integer.valueOf(OrderStatusEnum.MAIN_1.getCode())));

        return CommonResult.success();
    }


    @ApiOperation(value = "修改主订单描述 mainOrderId=主订单id,remarks=描述")
    @PostMapping("/updateRemarks")
    public CommonResult updateRemarks(@RequestBody @Valid Map<String, Object> map) {
        Long mainOrderId = MapUtil.getLong(map, "mainOrderId");
        if (mainOrderId == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        String remarks = MapUtil.getStr(map, "remarks");
        if (remarks == null) {
            return CommonResult.success();
        }
        this.orderInfoService.updateById(new OrderInfo().setId(mainOrderId).setRemarks(remarks));

        return CommonResult.success();
    }



    //订单详情接口
    @ApiOperation(value = "获取子订单")
    @PostMapping("/getSubOrderDetail")
    public CommonResult<InputOrderVO> getSubOrderDetail(@RequestBody @Valid GetOrderDetailForm form) {
        InputOrderVO inputOrderVO = orderInfoService.getOrderDetail(form);
        return CommonResult.success(inputOrderVO);
    }
}

