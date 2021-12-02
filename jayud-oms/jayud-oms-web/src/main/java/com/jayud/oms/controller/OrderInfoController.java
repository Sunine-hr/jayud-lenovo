package com.jayud.oms.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.UserOperator;
import com.jayud.common.constant.CommonConstant;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.enums.*;
import com.jayud.common.exception.JayudBizException;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.FileView;
import com.jayud.common.utils.StringUtils;
import com.jayud.oms.feign.StorageClient;
import com.jayud.oms.model.bo.*;
import com.jayud.oms.model.po.AuditInfo;
import com.jayud.oms.model.po.OrderAttachment;
import com.jayud.oms.model.po.OrderInfo;
import com.jayud.oms.model.vo.*;
import com.jayud.oms.model.vo.template.order.*;
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

    @Autowired
    private StorageClient storageClient;

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
        if (form == null || StringUtil.isNullOrEmpty(form.getCmd()) || form.getOrderForm() == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
        }
        //主订单参数校验
        InputMainOrderForm inputMainOrderForm = form.getOrderForm();
        inputMainOrderForm.checkCreateOrder();

        //待处理状态无法操作
        if (inputMainOrderForm.getOrderId() != null) {
            OrderInfo orderInfo = this.orderInfoService.getById(inputMainOrderForm.getOrderId());
            if (OrderStatusEnum.MAIN_8.getCode().equals(orderInfo.getStatus().toString())) {
                return CommonResult.error(400, "待处理状态,无法进行操作");
            }
        }
        //检查提交提单信息
        if (CommonConstant.SUBMIT.equals(form.getCmd())) {
            this.checkSubmitCreateOrder(form);
        }
//        if (CommonConstant.SUBMIT.equals(form.getCmd())) {
//            //1.报关资料是否齐全 1-齐全 0-不齐全 齐全时校验报关数据
//            //2.纯报关时校验数据
//            if (CommonConstant.VALUE_1.equals(inputMainOrderForm.getIsDataAll())) {
//                //报关订单参数校验
//                InputOrderCustomsForm inputOrderCustomsForm = form.getOrderCustomsForm();
//                form.checkCustomsParam();
//                inputOrderCustomsForm.handleAttachmentInfo();
//            }
//            //中港订单参数校验
//            if (OrderStatusEnum.ZGYS.getCode().equals(inputMainOrderForm.getClassCode())
//                    || inputMainOrderForm.getSelectedServer().contains(OrderStatusEnum.KYDD.getCode())) {
//                //中港订单参数校验
//                InputOrderTransportForm inputOrderTransportForm = form.getOrderTransportForm();
//                inputOrderTransportForm.cheackAddParam();
//
//                //中港订单提货收货信息参数校验
//                List<InputOrderTakeAdrForm> takeAdrForms1 = inputOrderTransportForm.getTakeAdrForms1();//必填
//                List<InputOrderTakeAdrForm> takeAdrForms2 = inputOrderTransportForm.getTakeAdrForms2();
//                //提货地址必填
//                if (takeAdrForms1 == null || takeAdrForms1.size() == 0) {
//                    return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
//                }
//                List<InputOrderTakeAdrForm> takeAdrForms = new ArrayList<>();
//                takeAdrForms.addAll(takeAdrForms1);
//                takeAdrForms.addAll(takeAdrForms2);
//                for (InputOrderTakeAdrForm inputOrderTakeAdr : takeAdrForms) {
//                    if (inputOrderTakeAdr.getAddress() == null
//                            || inputOrderTakeAdr.getTakeTimeStr() == null || inputOrderTakeAdr.getPieceAmount() == null
//                            || inputOrderTakeAdr.getWeight() == null || StringUtil.isNullOrEmpty(inputOrderTakeAdr.getGoodsDesc())) {
//                        return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
//                    }
//                }
//                //清关参数校验
//                if (inputMainOrderForm.getSelectedServer().contains(OrderStatusEnum.XGQG.getCode())) {
//                    if (StringUtil.isNullOrEmpty(inputOrderTransportForm.getHkLegalName()) ||
//                            inputOrderTransportForm.getHkLegalId() == null ||
//                            StringUtil.isNullOrEmpty(inputOrderTransportForm.getHkUnitCode()) ||
//                            StringUtil.isNullOrEmpty(inputOrderTransportForm.getIsHkClear())) {
//                        return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
//                    }
//                }
//            }
//            //空运校验参数
//            if (OrderStatusEnum.KY.getCode().equals(inputMainOrderForm.getClassCode())) {
//                InputAirOrderForm airOrderForm = form.getAirOrderForm();
//                if (!airOrderForm.checkCreateOrder()) {
//                    return CommonResult.error(ResultEnum.PARAM_ERROR);
//                }
//            }
//            //服务单参数校验
//            if (OrderStatusEnum.FWD.getCode().equals(inputMainOrderForm.getClassCode())) {
//                InputOrderServiceForm orderServiceForm = form.getOrderServiceForm();
//                if (orderServiceForm.getType() == null) {
//                    return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
//                }
//            }
//            //海运校验参数
//            if (OrderStatusEnum.HY.getCode().equals(inputMainOrderForm.getClassCode())) {
//                InputSeaOrderForm seaOrderForm = form.getSeaOrderForm();
//                String s = seaOrderForm.checkCreateOrder();
//                if (s != null) {
//                    return CommonResult.error(1, s);
//                }
//            }
//            //拖车校验参数
//            if (OrderStatusEnum.TC.getCode().equals(inputMainOrderForm.getClassCode())
//                    || inputMainOrderForm.getSelectedServer().contains(OrderStatusEnum.TCEDD.getCode())
//                    || inputMainOrderForm.getSelectedServer().contains(OrderStatusEnum.TCIDD.getCode())) {
//                List<InputTrailerOrderFrom> trailerOrderFrom = form.getTrailerOrderFrom();
//                for (InputTrailerOrderFrom inputTrailerOrderFrom : trailerOrderFrom) {
//                    if (!inputTrailerOrderFrom.checkCreateOrder()) {
//                        return CommonResult.error(ResultEnum.PARAM_ERROR);
//                    }
//                }
//            }
//            //仓储校验参数
//            if (OrderStatusEnum.CC.getCode().equals(inputMainOrderForm.getClassCode()) ||
//                    inputMainOrderForm.getSelectedServer().contains(OrderStatusEnum.CCEDD.getCode()) ||
//                    inputMainOrderForm.getSelectedServer().contains(OrderStatusEnum.CCIDD.getCode()) ||
//                    inputMainOrderForm.getSelectedServer().contains(OrderStatusEnum.CCFDD.getCode())) {
////                if (inputMainOrderForm.getSelectedServer().contains(OrderStatusEnum.CCEDD.getCode())) {
////                    InputStorageOutOrderForm storageOutOrderForm = form.getStorageOutOrderForm();
////                    if (!storageOutOrderForm.checkCreateOrder().equals("pass")) {
////                        return CommonResult.error(1, storageOutOrderForm.checkCreateOrder());
////                    }
////                    ApiResult result = storageClient.isEnough(storageOutOrderForm.getGoodsFormList());
////                    if(!result.isOk()){
////                        return CommonResult.error(result.getCode(),result.getMsg());
////                    }
////                    ApiResult stock = storageClient.isStock(storageOutOrderForm.getGoodsFormList());
////                    if(!stock.isOk()){
////                        return CommonResult.error(stock.getCode(),stock.getMsg());
////                    }
////
////                }
////                if (inputMainOrderForm.getSelectedServer().contains(OrderStatusEnum.CCIDD.getCode())) {
////                    InputStorageInputOrderForm storageInputOrderForm = form.getStorageInputOrderForm();
////                    ApiResult commodity = storageClient.isCommodity(storageInputOrderForm.getGoodsFormList());
////                    if(!commodity.isOk()){
////                        return CommonResult.error(commodity.getCode(),commodity.getMsg());
////                    }
////                }
//            }
//
//            //校验参数
//            form.checkCreateParam();
//        }

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
        orderInfoService.pushMessageNumbers(form.getMainOrderId(), form.getEncode());
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


    @ApiOperation(value = "修改主订单信息 mainOrderId=主订单id")
    @PostMapping("/updateOrderInfo")
    public CommonResult updateOrderInfo(@RequestBody InputMainOrderForm form) {
        if (form.getOrderId() == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        OrderInfo orderInfo = new OrderInfo().setId(form.getOrderId());
        orderInfo.setOperationTime(DateUtils.str2LocalDateTime(form.getOperationTime(), DateUtils.DATE_TIME_PATTERN));
        this.orderInfoService.updateById(orderInfo);
        return CommonResult.success();
    }


    @ApiOperation(value = "获取主订单页面子订单信息")
    @PostMapping("/getSubOrderDetail")
    public CommonResult<OrderInfoTemplate> getSubOrderDetail(@RequestBody @Valid GetOrderDetailForm form) {
        InputOrderVO inputOrderVO = orderInfoService.getOrderDetail(form);

        InputMainOrderVO orderForm = inputOrderVO.getOrderForm();

        OrderInfoTemplate orderInfoTemplate = new OrderInfoTemplate();
        //中港模板
        InputOrderTransportVO orderTransportForm = inputOrderVO.getOrderTransportForm();
        if (orderTransportForm != null) {
            TmsOrderTemplate tmsOrderTemplate = ConvertUtil.convert(orderTransportForm, TmsOrderTemplate.class);
//            tmsOrderTemplate.setCost(this.orderInfoService.isCost(tmsOrderTemplate.getOrderNo(), 1));
            tmsOrderTemplate.setMainOrderId(form.getMainOrderId());
            tmsOrderTemplate.setClassCode(form.getClassCode());
            tmsOrderTemplate.isRejected();
            tmsOrderTemplate.setMainOrderStatus(inputOrderVO.getOrderForm().getStatus());
            //费用状态
            tmsOrderTemplate.assembleCostStatus(tmsOrderTemplate.getOrderNo(),
                    this.orderInfoService.getCostStatus(null, Collections.singletonList(tmsOrderTemplate.getOrderNo())));

            Template<TmsOrderTemplate> template = new Template<TmsOrderTemplate>() {
            }.setList(Collections.singletonList(tmsOrderTemplate));
            orderInfoTemplate.setTmsOrderTemplates(template);
        }
        //内陆模板
        InputOrderInlandTPVO inlandTransportForm = inputOrderVO.getOrderInlandTransportForm();
        if (inlandTransportForm != null) {
            InlandTPTemplate inlandTPTemplate = ConvertUtil.convert(inlandTransportForm, InlandTPTemplate.class);
//            inlandTPTemplate.setCost(this.orderInfoService.isCost(inlandTPTemplate.getOrderNo(), 1));
            //费用状态
            inlandTPTemplate.assembleCostStatus(inlandTPTemplate.getOrderNo(),
                    this.orderInfoService.getCostStatus(null, Collections.singletonList(inlandTPTemplate.getOrderNo())));

            Template<InlandTPTemplate> template = new Template<InlandTPTemplate>() {
            }.setList(Collections.singletonList(inlandTPTemplate));
            orderInfoTemplate.setInlandTPTemplates(template);
        }

        //报关模板
        InputOrderCustomsVO orderCustomsForm = inputOrderVO.getOrderCustomsForm();
        if (orderCustomsForm != null) {
            if (CollectionUtils.isNotEmpty(orderCustomsForm.getSubOrders())) {
                List<OrderCustomsTemplate> list = new ArrayList<>();
                List<String> subOrderNos = new ArrayList<>();
                for (InputSubOrderCustomsVO subOrder : orderCustomsForm.getSubOrders()) {
                    OrderCustomsTemplate customsTemplate = ConvertUtil.convert(subOrder, OrderCustomsTemplate.class);
                    customsTemplate.assemblyData(orderCustomsForm);
                    customsTemplate.setId(subOrder.getSubOrderId());
//                    customsTemplate.setCost(this.orderInfoService.isCost(customsTemplate.getOrderNo(), 1));
                    subOrderNos.add(subOrder.getOrderNo());
                    list.add(customsTemplate);
                }

                Map<String, Object> costStatus = this.orderInfoService.getCostStatus(null, subOrderNos);
                for (OrderCustomsTemplate template : list) {
                    template.assembleCostStatus(template.getOrderNo(), costStatus);
                }

                Template<OrderCustomsTemplate> template = new Template<OrderCustomsTemplate>() {
                }.setList(list);
                orderInfoTemplate.setOrderCustomsTemplates(template);
            }
        }

        //空运模板
        InputAirOrderVO airOrderForm = inputOrderVO.getAirOrderForm();
        if (airOrderForm != null) {
            AirOrderTemplate airOrderTemplate = ConvertUtil.convert(airOrderForm, AirOrderTemplate.class);
//            airOrderTemplate.setCost(this.orderInfoService.isCost(airOrderTemplate.getOrderNo(), 1));
            Template<AirOrderTemplate> template = new Template<AirOrderTemplate>() {
            }.setList(Collections.singletonList(airOrderTemplate));
            airOrderTemplate.assemblyData(airOrderForm);
            airOrderTemplate.assembleCostStatus(airOrderTemplate.getOrderNo(),
                    this.orderInfoService.getCostStatus(null, Collections.singletonList(airOrderTemplate.getOrderNo())));
            orderInfoTemplate.setAirOrderTemplates(template);
        }

        //海运模板
        InputSeaOrderVO seaOrderForm = inputOrderVO.getSeaOrderForm();
        if (seaOrderForm != null) {
            SeaOrderTemplate seaOrderTemplate = ConvertUtil.convert(seaOrderForm, SeaOrderTemplate.class);
//            seaOrderTemplate.setCost(this.orderInfoService.isCost(seaOrderTemplate.getOrderNo(), 1));
            seaOrderTemplate.assembleCostStatus(seaOrderTemplate.getOrderNo(),
                    this.orderInfoService.getCostStatus(null, Collections.singletonList(seaOrderTemplate.getOrderNo())));

            Template<SeaOrderTemplate> template = new Template<SeaOrderTemplate>() {
            }.setList(Collections.singletonList(seaOrderTemplate));

            orderInfoTemplate.setSeaOrderTemplates(template);
        }

        //拖车模板
        List<InputTrailerOrderVO> trailerOrderForms = inputOrderVO.getTrailerOrderForm();
        if (CollectionUtils.isNotEmpty(trailerOrderForms)) {
            List<TrailerOrderTemplate> templates = new ArrayList<>();
            List<String> subOrderNos = new ArrayList<>();
            for (InputTrailerOrderVO trailerOrderForm : trailerOrderForms) {
                if (trailerOrderForm != null) {
                    TrailerOrderTemplate trailerOrderTemplate = ConvertUtil.convert(trailerOrderForm, TrailerOrderTemplate.class);
//                    trailerOrderTemplate.setCost(this.orderInfoService.isCost(trailerOrderTemplate.getOrderNo(), 1));
                    templates.add(trailerOrderTemplate);
                    subOrderNos.add(trailerOrderForm.getOrderNo());
                }
            }
            Map<String, Object> costStatus = this.orderInfoService.getCostStatus(null, subOrderNos);
            templates.forEach(e -> e.setCustomerCode(orderForm.getCustomerCode()).assembleCostStatus(e.getOrderNo(), costStatus));

            Template<TrailerOrderTemplate> template = new Template<TrailerOrderTemplate>() {
            }.setList(templates);
            orderInfoTemplate.setTrailerOrderTemplates(template);
        }

        //仓储模板
        //入库
        InputStorageInputOrderVO storageInputOrderForm = inputOrderVO.getStorageInputOrderForm();
        if (storageInputOrderForm != null) {
            StorageInputTemplate storageInputTemplate = ConvertUtil.convert(storageInputOrderForm, StorageInputTemplate.class);
            storageInputTemplate.setCost(this.orderInfoService.isCost(storageInputTemplate.getOrderNo(), 1));

            Template<StorageInputTemplate> template = new Template<StorageInputTemplate>() {
            }.setList(Collections.singletonList(storageInputTemplate));

            orderInfoTemplate.setStorageInputTemplateTemplate(template);
        }
        //出库
        InputStorageOutOrderVO storageOutOrderForm = inputOrderVO.getStorageOutOrderForm();
        if (storageOutOrderForm != null) {
            StorageOutTemplate storageOutTemplate = ConvertUtil.convert(storageOutOrderForm, StorageOutTemplate.class);
            storageOutTemplate.setCost(this.orderInfoService.isCost(storageOutTemplate.getOrderNo(), 1));

            Template<StorageOutTemplate> template = new Template<StorageOutTemplate>() {
            }.setList(Collections.singletonList(storageOutTemplate));

            orderInfoTemplate.setStorageOutTemplateTemplate(template);
        }
        //快进快出
        InputStorageFastOrderVO storageFastOrderForm = inputOrderVO.getStorageFastOrderForm();
        if (storageFastOrderForm != null) {
            StorageFastTemplate storageFastTemplate = ConvertUtil.convert(storageFastOrderForm, StorageFastTemplate.class);
            storageFastTemplate.setCost(this.orderInfoService.isCost(storageFastTemplate.getOrderNo(), 1));

            Template<StorageFastTemplate> template = new Template<StorageFastTemplate>() {
            }.setList(Collections.singletonList(storageFastTemplate));

            orderInfoTemplate.setStorageFastTemplateTemplate(template);
        }


        return CommonResult.success(orderInfoTemplate);
    }


    @ApiOperation(value = "获取复制信息")
    @PostMapping("/copyInformation")
    public CommonResult<InputOrderVO> copyInformation(@RequestBody @Valid GetOrderDetailForm form) {
        InputOrderVO inputOrderVO = orderInfoService.getOrderDetail(form);
        //主订单的 设置 来源   只要是复制的订单 默认为本系统创建
        //创建类型
        inputOrderVO.getOrderForm().setCreateUserType(0);
        //客户参考号
        inputOrderVO.getOrderForm().setReferenceNo("");
        inputOrderVO.getOrderForm().setCreateUserTypeName(CreateUserTypeEnum.getDesc(Integer.parseInt("0")));

        inputOrderVO.copyOperationInfo();

        //中港
        InputOrderTransportVO orderTransportForm = inputOrderVO.getOrderTransportForm();
        if (orderTransportForm != null) {
            orderTransportForm.copyOperationInfo();
        }
        //内陆模板
        InputOrderInlandTPVO inlandTransportForm = inputOrderVO.getOrderInlandTransportForm();
        if (inlandTransportForm != null) {
            inlandTransportForm.copyOperationInfo();
        }
        //报关模板
        InputOrderCustomsVO orderCustomsForm = inputOrderVO.getOrderCustomsForm();
        if (orderCustomsForm != null) {
            orderCustomsForm.copyOperationInfo();
        }
        //空运模板
        InputAirOrderVO airOrderForm = inputOrderVO.getAirOrderForm();
        if (airOrderForm != null) {
            airOrderForm.copyOperationInfo();
        }
        //海运模板
        InputSeaOrderVO seaOrderForm = inputOrderVO.getSeaOrderForm();
        if (seaOrderForm != null) {
            seaOrderForm.copyOperationInfo();
        }
        //拖车模板
        List<InputTrailerOrderVO> trailerOrderForm = inputOrderVO.getTrailerOrderForm();
        if (CollectionUtils.isNotEmpty(trailerOrderForm)) {
            trailerOrderForm.forEach(InputTrailerOrderVO::copyOperationInfo);
        }
        //服务模板
        InputOrderServiceVO orderServiceForm = inputOrderVO.getOrderServiceForm();
        if (orderServiceForm != null) {
            orderServiceForm.copyOperationInfo();
        }
        InputStorageInputOrderVO storageInputOrderForm = inputOrderVO.getStorageInputOrderForm();
        if (storageInputOrderForm != null) {
            storageInputOrderForm.copyOperationInfo();
        }
        InputStorageOutOrderVO storageOutOrderForm = inputOrderVO.getStorageOutOrderForm();
        if (storageOutOrderForm != null) {
            storageOutOrderForm.copyOperationInfo();
        }
        InputStorageFastOrderVO storageFastOrderForm = inputOrderVO.getStorageFastOrderForm();
        if (storageFastOrderForm != null) {
            storageFastOrderForm.copyOperationInfo();
        }
        return CommonResult.success(inputOrderVO);
    }


    @ApiOperation(value = "获取订单模块节点")
    @PostMapping("/getOrderModuleNode")
    public CommonResult viewAddNewModule(@RequestBody @Valid Map<String, Object> map) {
        Long mainOrderId = MapUtil.getLong(map, "mainOrderId");
        if (mainOrderId == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        List<ProductClassifyVO> list = this.orderInfoService.getOrderModuleNode(mainOrderId);

        return CommonResult.success(list);
    }

    @ApiOperation(value = "追加订单模块节点")
    @PostMapping("/addOrderModule")
    public CommonResult addOrderModule(@RequestBody InputOrderForm form) {
        //通用参数校验
        if (form == null || StringUtil.isNullOrEmpty(form.getCmd()) || form.getOrderForm() == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR.getCode(), ResultEnum.PARAM_ERROR.getMessage());
        }
        if (StringUtils.isEmpty(form.getOrderForm().getSelectedServer())) {
            return CommonResult.error(400, "没有追加的新模块");
        }
        //主订单参数校验
        InputMainOrderForm inputMainOrderForm = form.getOrderForm();
        inputMainOrderForm.checkCreateOrder();
        this.checkSubmitCreateOrder(form);
        //特殊处理选择服务
        inputMainOrderForm.specialTreatmentSelectedServer();
        inputMainOrderForm.setCmd("submit");
        //追加订单模块节点
        this.orderInfoService.addOrderModule(form);

        return CommonResult.success();
    }

    @ApiOperation(value = "关闭服务单")
    @PostMapping("/closeServiceOrder")
    public CommonResult closeServiceOrder(@RequestBody Map<String, Object> map) {
        Long mainOrderId = MapUtil.getLong(map, "mainOrderId");
        if (mainOrderId == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setStatus(Integer.valueOf(OrderStatusEnum.MAIN_3.getCode()));
        orderInfo.setId(mainOrderId);
        orderInfo.setUpTime(LocalDateTime.now());
        orderInfo.setUpUser(UserOperator.getToken());
        orderInfo.setNeedInputCost(false);
        this.orderInfoService.updateById(orderInfo);
        return CommonResult.success();
    }

    @ApiOperation(value = "删除订单")
    @PostMapping("/deleteOrder")
    public CommonResult deleteOrder(@RequestBody Map<String, Object> map) {
        Long mainOrderId = MapUtil.getLong(map, "mainOrderId");
        if (mainOrderId == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        OrderInfo tmp = this.orderInfoService.getById(mainOrderId);
        if (!OrderStatusEnum.MAIN_2.getCode().equals(tmp.getStatus() + "")) {
            return CommonResult.error(400, "只有草稿状态才能删除");
        }
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setStatus(Integer.valueOf(OrderStatusEnum.MAIN_9.getCode()));
        orderInfo.setId(mainOrderId);
        orderInfo.setUpTime(LocalDateTime.now());
        orderInfo.setUpUser(UserOperator.getToken());
        orderInfo.setNeedInputCost(true);
        this.orderInfoService.updateById(orderInfo);
        return CommonResult.success();
    }

    /**
     * 检查提交提单信息
     *
     * @param form
     */
    private void checkSubmitCreateOrder(InputOrderForm form) {
        InputMainOrderForm inputMainOrderForm = form.getOrderForm();

        //1.报关资料是否齐全 1-齐全 0-不齐全 齐全时校验报关数据
        //2.纯报关时校验数据
        if (CommonConstant.VALUE_1.equals(inputMainOrderForm.getIsDataAll())) {
            //报关订单参数校验
            InputOrderCustomsForm inputOrderCustomsForm = form.getOrderCustomsForm();
            form.checkCustomsParam();
            //附件处理
            inputOrderCustomsForm.handleAttachmentInfo();
        }
        //中港订单参数校验
        if (OrderStatusEnum.ZGYS.getCode().equals(inputMainOrderForm.getClassCode())
                || inputMainOrderForm.getSelectedServer().contains(OrderStatusEnum.ZGYSDD.getCode())) {
            //中港订单参数校验
            InputOrderTransportForm inputOrderTransportForm = form.getOrderTransportForm();
            inputOrderTransportForm.checkAddParam();

            //中港订单提货收货信息参数校验
            List<InputOrderTakeAdrForm> takeAdrForms1 = inputOrderTransportForm.getTakeAdrForms1();//必填
            List<InputOrderTakeAdrForm> takeAdrForms2 = inputOrderTransportForm.getTakeAdrForms2();
            //提货地址必填
            if (takeAdrForms1 == null || takeAdrForms1.size() == 0) {
                throw new JayudBizException(ResultEnum.PARAM_ERROR);
            }
            List<InputOrderTakeAdrForm> takeAdrForms = new ArrayList<>();
            takeAdrForms.addAll(takeAdrForms1);
            takeAdrForms.addAll(takeAdrForms2);
            for (InputOrderTakeAdrForm inputOrderTakeAdr : takeAdrForms) {
                if (inputOrderTakeAdr.getAddress() == null
                        || inputOrderTakeAdr.getTakeTimeStr() == null || inputOrderTakeAdr.getPieceAmount() == null
                        || inputOrderTakeAdr.getWeight() == null || StringUtil.isNullOrEmpty(inputOrderTakeAdr.getGoodsDesc())) {
                    throw new JayudBizException(ResultEnum.PARAM_ERROR);
                }
            }
            //清关参数校验
            if (inputMainOrderForm.getSelectedServer().contains(OrderStatusEnum.XGQG.getCode())) {
                if (StringUtil.isNullOrEmpty(inputOrderTransportForm.getHkLegalName()) ||
                        inputOrderTransportForm.getHkLegalId() == null ||
                        StringUtil.isNullOrEmpty(inputOrderTransportForm.getHkUnitCode()) ||
                        StringUtil.isNullOrEmpty(inputOrderTransportForm.getIsHkClear())) {
                    throw new JayudBizException(ResultEnum.PARAM_ERROR);
                }
            }
        }
        //空运校验参数
        if (OrderStatusEnum.KY.getCode().equals(inputMainOrderForm.getClassCode())) {
            InputAirOrderForm airOrderForm = form.getAirOrderForm();
            if (!airOrderForm.checkCreateOrder()) {
                throw new JayudBizException(ResultEnum.PARAM_ERROR);
            }
        }
        //服务单参数校验
        if (OrderStatusEnum.FWD.getCode().equals(inputMainOrderForm.getClassCode())) {
            InputOrderServiceForm orderServiceForm = form.getOrderServiceForm();
            if (orderServiceForm.getType() == null) {
                throw new JayudBizException(ResultEnum.PARAM_ERROR);
            }
        }
        //海运校验参数
        if (OrderStatusEnum.HY.getCode().equals(inputMainOrderForm.getClassCode())) {
            InputSeaOrderForm seaOrderForm = form.getSeaOrderForm();
            String s = seaOrderForm.checkCreateOrder();
            if (s != null) {
                throw new JayudBizException(1, s);
            }
        }
        //拖车校验参数
        if (OrderStatusEnum.TC.getCode().equals(inputMainOrderForm.getClassCode())
                || inputMainOrderForm.getSelectedServer().contains(OrderStatusEnum.TCEDD.getCode())
                || inputMainOrderForm.getSelectedServer().contains(OrderStatusEnum.TCIDD.getCode())) {
            List<InputTrailerOrderFrom> trailerOrderFrom = form.getTrailerOrderFrom();
            for (InputTrailerOrderFrom inputTrailerOrderFrom : trailerOrderFrom) {
                if (!inputTrailerOrderFrom.checkCreateOrder()) {
                    throw new JayudBizException(ResultEnum.PARAM_ERROR);
                }
            }
        }
        //仓储校验参数
        if (OrderStatusEnum.CC.getCode().equals(inputMainOrderForm.getClassCode()) ||
                inputMainOrderForm.getSelectedServer().contains(OrderStatusEnum.CCEDD.getCode()) ||
                inputMainOrderForm.getSelectedServer().contains(OrderStatusEnum.CCIDD.getCode()) ||
                inputMainOrderForm.getSelectedServer().contains(OrderStatusEnum.CCFDD.getCode())) {
//                if (inputMainOrderForm.getSelectedServer().contains(OrderStatusEnum.CCEDD.getCode())) {
//                    InputStorageOutOrderForm storageOutOrderForm = form.getStorageOutOrderForm();
//                    if (!storageOutOrderForm.checkCreateOrder().equals("pass")) {
//                        return CommonResult.error(1, storageOutOrderForm.checkCreateOrder());
//                    }
//                    ApiResult result = storageClient.isEnough(storageOutOrderForm.getGoodsFormList());
//                    if(!result.isOk()){
//                        return CommonResult.error(result.getCode(),result.getMsg());
//                    }
//                    ApiResult stock = storageClient.isStock(storageOutOrderForm.getGoodsFormList());
//                    if(!stock.isOk()){
//                        return CommonResult.error(stock.getCode(),stock.getMsg());
//                    }
//
//                }
//                if (inputMainOrderForm.getSelectedServer().contains(OrderStatusEnum.CCIDD.getCode())) {
//                    InputStorageInputOrderForm storageInputOrderForm = form.getStorageInputOrderForm();
//                    ApiResult commodity = storageClient.isCommodity(storageInputOrderForm.getGoodsFormList());
//                    if(!commodity.isOk()){
//                        return CommonResult.error(commodity.getCode(),commodity.getMsg());
//                    }
//                }
        }
        //校验参数
        form.checkCreateParam();
    }

}

