package com.jayud.trailer.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.enums.WriteDirectionEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.entity.InitComboxStrVO;
import com.jayud.common.entity.InitComboxVO;
import com.jayud.common.enums.BusinessTypeEnum;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ProcessStatusEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.StringUtils;
import com.jayud.trailer.bo.QueryTrailerOrderForm;
import com.jayud.trailer.bo.TrailerProcessOptForm;
import com.jayud.trailer.feign.FileClient;
import com.jayud.trailer.feign.OauthClient;
import com.jayud.trailer.feign.OmsClient;
import com.jayud.trailer.po.TrailerDispatch;
import com.jayud.trailer.po.TrailerOrder;
import com.jayud.trailer.service.ITrailerDispatchService;
import com.jayud.trailer.service.ITrailerOrderService;
import com.jayud.trailer.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.*;

import static com.jayud.common.enums.OrderStatusEnum.SEA_S_2;

/**
 * <p>
 * 拖车订单表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-03-01
 */
@Api(tags = "拖车管理")
@RestController
@Slf4j
@RequestMapping("/trailerOrder")
public class TrailerOrderController {

    @Autowired
    private OmsClient omsClient;

    @Autowired
    private OauthClient oauthClient;

    @Autowired
    private FileClient fileClient;

    @Autowired
    private ITrailerOrderService trailerOrderService;

    @Autowired
    private ITrailerDispatchService trailerDispatchService;

    @ApiOperation("分页查询海运订单列表")
    @PostMapping("/findByPage")
    public CommonResult findByPage(@RequestBody QueryTrailerOrderForm form) {

        form.setStartTime();
        //模糊查询客户信息
        if (!StringUtils.isEmpty(form.getCustomerName())) {
            ApiResult result = omsClient.getByCustomerName(form.getCustomerName());
            Object data = result.getData();
            if (data != null && ((List) data).size() > 0) {
                JSONArray mainOrders = new JSONArray(data);
                form.assemblyMainOrderNo(mainOrders);
            } else {
                form.setMainOrderNos(Collections.singletonList("-1"));
            }
        }

        List list = new ArrayList();
        //获取表头信息
//        Field[] declaredFields = new Field[100];

        if(form.getImpAndExpType().equals(0)){
            Class<TrailerOrderFormVO> seaOrderFormVOClass = TrailerOrderFormVO.class;
            Field[] declaredFields = seaOrderFormVOClass.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                ApiModelProperty annotation = declaredField.getAnnotation(ApiModelProperty.class);
                if(annotation!=null){
                    Map map = new HashMap<>();
                    map.put("key",declaredField.getName());
                    map.put("name",annotation.value());
                    list.add(map);
                }
            }
        }
        if(form.getImpAndExpType().equals(1)){
            Class<TrailerOrderImportVO> seaOrderFormVOClass = TrailerOrderImportVO.class;
            Field[] declaredFields = seaOrderFormVOClass.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                ApiModelProperty annotation = declaredField.getAnnotation(ApiModelProperty.class);
                if(annotation!=null){
                    Map map = new HashMap<>();
                    map.put("key",declaredField.getName());
                    map.put("name",annotation.value());
                    list.add(map);
                }
            }
        }
        if(form.getImpAndExpType().equals(2)){
            Class<TrailerOrderExportVO> seaOrderFormVOClass = TrailerOrderExportVO.class;
            Field[] declaredFields = seaOrderFormVOClass.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                ApiModelProperty annotation = declaredField.getAnnotation(ApiModelProperty.class);
                if(annotation!=null){
                    Map map = new HashMap<>();
                    map.put("key",declaredField.getName());
                    map.put("name",annotation.value());
                    list.add(map);
                }
            }
        }

        Map map1 = new HashMap();
        map1.put("header",list);
        IPage<TrailerOrderFormVO> page = this.trailerOrderService.findByPage(form);
        if (page.getRecords().size() == 0) {
            map1.put("pageInfo",new CommonPageResult(page));
            return CommonResult.success(map1);
        }
        //获取附件地址
        String prePath = String.valueOf(fileClient.getBaseUrl().getData());

        List<TrailerOrderFormVO> records = page.getRecords();
        List<Long> trailerOrderIds = new ArrayList<>();
        List<String> mainOrder = new ArrayList<>();
        List<Long> entityIds = new ArrayList<>();
        List<Long> supplierIds = new ArrayList<>();
        List<String> unitCodes = new ArrayList<>();
        for (TrailerOrderFormVO record : records) {
            trailerOrderIds.add(record.getId());
            mainOrder.add(record.getMainOrderNo());
            entityIds.add(record.getLegalEntityId());
            unitCodes.add(record.getUnitCode());
            if(record.getTrailerDispatchVO().getSupplierId()!=null){
                supplierIds.add(record.getTrailerDispatchVO().getSupplierId());
            }
        }

        //查询商品信息
        List<GoodsVO> goods = this.omsClient.getGoodsByBusIds(trailerOrderIds, BusinessTypeEnum.HY.getCode()).getData();

        //查询法人主体
        ApiResult legalEntityResult = null;
        if (CollectionUtils.isNotEmpty(entityIds)) {
            legalEntityResult = this.oauthClient.getLegalEntityByLegalIds(entityIds);
        }
        //查询供应商信息
        JSONArray supplierInfo = null;
        if (CollectionUtils.isNotEmpty(supplierIds) && supplierIds.size()>0) {
            supplierInfo = new JSONArray(this.omsClient.getSupplierInfoByIds(supplierIds).getData());
        }

        //获取结算单位信息
        ApiResult unitCodeInfo = null;
        if (CollectionUtils.isNotEmpty(unitCodes)) {
            unitCodeInfo = this.omsClient.getCustomerByUnitCode(unitCodes);
        }

        //获取发货人信息
        ApiResult<List<OrderAddressVO>> resultOne = this.omsClient.getOrderAddressByBusIds(trailerOrderIds, BusinessTypeEnum.TC.getCode() );
        if (resultOne.getCode() != HttpStatus.SC_OK) {
            log.warn("查询订单地址信息失败 airOrderId={}", trailerOrderIds);
        }

        //获取港口信息
        List<InitComboxStrVO> portCodeInfo = (List<InitComboxStrVO>)this.omsClient.initDictByDictTypeCode("Port").getData();

        //获取车型信息
        List<InitComboxVO> cabinetSizeInfo = (List<InitComboxVO>)this.omsClient.getVehicleSizeInfo().getData();


        //查询主订单信息
        ApiResult result = omsClient.getMainOrderByOrderNos(mainOrder);
        for (TrailerOrderFormVO record : records) {
            //组装商品信息
            List<GoodsVO> goodsVOS = new ArrayList<>();
            for (GoodsVO goodsVO : goods) {
                if (record.getId().equals(goodsVO.getBusinessId())
                        && BusinessTypeEnum.TC.getCode().equals(goodsVO.getBusinessType())) {
                    goodsVOS.add(goodsVO);
                }
            }
            record.setGoodsForms(goodsVOS);
            record.assemblyGoodsInfo(goods);
            //拼装主订单信息
            record.assemblyMainOrderData(result.getData());
            //组装法人名称
            record.assemblyLegalEntity(legalEntityResult);
            //拼装供应商
            record.assemblySupplierInfo(supplierInfo);

            //拼装结算单位
            record.assemblyUnitCodeInfo(unitCodeInfo);

            //处理地址信息
            for (OrderAddressVO address : resultOne.getData()) {
                address.getFile(prePath);
            }
            //获取港口信息
            for (InitComboxStrVO initComboxStrVO : portCodeInfo) {
                if(initComboxStrVO.getCode().equals(record.getPortCode())){
                    record.setPortCodeName(initComboxStrVO.getName());
                }
            }
            //获取车型大小
            for (InitComboxVO comboxVO : cabinetSizeInfo) {
                if(comboxVO.getId().equals(record.getCabinetSize())){
                    record.setCabinetSizeName(comboxVO.getName());
                }
            }
            //获取派车信息
            TrailerDispatch enableByTrailerOrderId = trailerDispatchService.getEnableByTrailerOrderId(record.getId());
            TrailerDispatchVO trailerDispatchVO = ConvertUtil.convert(enableByTrailerOrderId,TrailerDispatchVO.class);
            record.setTrailerDispatchVO(trailerDispatchVO);
        }

        map1.put("pageInfo",new CommonPageResult(page));
        return CommonResult.success(map1);
    }

    //操作指令,cmd = S_0待接单,S_1海运接单,S_2订船,S_3订单入仓, S_4提交补料,S_5草稿提单,S_6放单确认,S_7确认离港,S_8确认到港,S_9海外代理S_10确认签收
    @ApiOperation(value = "执行拖车流程操作")
    @PostMapping(value = "/doTrailerProcessOpt")
    public CommonResult doTrailerProcessOpt(@RequestBody @Valid TrailerProcessOptForm form , BindingResult result) {

        if(result.hasErrors()){
            for (ObjectError error : result.getAllErrors()) {
                return CommonResult.error(444,error.getDefaultMessage());
            }
        }

        if (form.getMainOrderId() == null || form.getId() == null) {
            log.warn("主订单id/拖车订单id必填");
            return CommonResult.error(ResultEnum.VALIDATE_FAILED);
        }
        //拖车订单信息
        TrailerOrder trailerOrder = this.trailerOrderService.getById(form.getId());
        if (ProcessStatusEnum.COMPLETE.getCode().equals(trailerOrder.getProcessStatus())) {
            return CommonResult.error(400, "该订单已经完成");
        }
        if (!ProcessStatusEnum.PROCESSING.getCode().equals(trailerOrder.getProcessStatus())) {
            return CommonResult.error(400, "当前订单无法操作");
        }
        if(!trailerOrder.getStatus().equals(form.getStatus())){
            return CommonResult.error(400, "当前订单正在操作");
        }
//
        String orderProcessNode = (String)omsClient.getOrderProcessNode(trailerOrder.getMainOrderNo(),trailerOrder.getOrderNo(),trailerOrder.getStatus()).getData();

        OrderStatusEnum statusEnum = OrderStatusEnum.getTrailerOrderNextStatus(orderProcessNode);
        if (statusEnum == null) {
            log.error("执行拖车流程操作失败,超出流程之外 data={}", trailerOrder.toString());
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        //校验参数
        form.checkProcessOpt(statusEnum);
        form.setStatus(statusEnum.getCode());

        //指令操作
        switch (statusEnum) {
            case TT_1: //拖车接单
                TrailerOrder trailerOrder1 = new TrailerOrder();
                trailerOrder1.setOrderTaker(form.getOperatorUser());
                trailerOrder1.setReceivingOrdersDate(DateUtils.str2LocalDateTime(form.getOperatorTime(), DateUtils.DATE_TIME_PATTERN));
                this.trailerOrderService.updateProcessStatus(trailerOrder1 , form);
                break;
            case SEA_S_2: //派车
                this.trailerOrderService.doTrailerDispatchOpt(form);
                break;
            case SEA_S_3: //确认订单入仓
                this.trailerOrderService.updateProcessStatus(new TrailerOrder(), form);
                break;
            case SEA_S_5: //确认草稿提单
            case SEA_S_6: //确认装船
            case SEA_S_7: //确认放单
            case SEA_S_8: //确认到港
                this.trailerOrderService.doTrailerDispatchOpt(form);
                break;
//            case SEA_S_9: //海外代理
//                StringBuilder sb = new StringBuilder();
//                form.getProxyServiceType().forEach(e -> sb.append(e).append(","));
//                String proxyServiceType = sb.length() == 0 ? null : sb.substring(0, sb.length() - 1);
//                SeaOrder seaOrder2 = new SeaOrder();
//                seaOrder2.setOverseasSuppliersId(form.getAgentSupplierId());
//                seaOrder2.setProxyServiceType(proxyServiceType);
//                this.seaOrderService.updateProcessStatus(seaOrder2, form);
//                break;
//            case SEA_S_10: //确认签收
//                this.seaOrderService.updateProcessStatus(new SeaOrder(), form);
//                break;
        }

        return CommonResult.success();
    }


    @ApiOperation(value = "查询订单详情 trailerOrderId=海运订单id")
    @PostMapping(value = "/getTrailerOrderDetails")
    public CommonResult<TrailerOrderVO> getAirOrderDetails(@RequestBody Map<String, Object> map) {
        Long trailerOrderId = MapUtil.getLong(map, "id");
        if (trailerOrderId == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        TrailerOrderVO trailerOrderDetails = this.trailerOrderService.getTrailerOrderByOrderNO(trailerOrderId);

        return CommonResult.success(trailerOrderDetails);
    }

//
//    @ApiOperation(value = "订船驳回编辑 id=海运订单id")
//    @PostMapping(value = "/bookShipRejectionEdit")
//    public CommonResult bookingRejectionEdit(@RequestBody SeaProcessOptForm form) {
//        if (form.getMainOrderId() == null
//                || form.getOrderId() == null
//                || form.getSeaBookShipForm().getId() == null) {
//            log.warn("海运订单编号/海运订单id必填/海运订船id必填 data={}", JSONUtil.toJsonStr(form));
//            return CommonResult.error(ResultEnum.VALIDATE_FAILED);
//        }
//
//        SeaOrder seaOrder = this.seaOrderService.getById(form.getOrderId());
//        if (!OrderStatusEnum.SEA_S_3_2.getCode().equals(seaOrder.getStatus())) {
//            log.warn("当前订单状态无法进行操作 status={}", OrderStatusEnum.getDesc(seaOrder.getStatus()));
//            return CommonResult.error(400, "当前订单状态无法进行操作");
//        }
//        form.setStatus(SEA_S_2.getCode());
//        //校验参数
//        form.checkProcessOpt(SEA_S_2);
//        //执行订船驳回编辑
//        this.seaOrderService.doSeaBookShipOpt(form);
//        return CommonResult.success();
//    }
//
//
//
//    @ApiOperation(value = "海运订单驳回")
//    @PostMapping(value = "/rejectOrder")
//    public CommonResult rejectOrder(@RequestBody SeaCargoRejected seaCargoRejected) {
//        //查询空运订单
//        SeaOrder tmp = this.seaOrderService.getById(seaCargoRejected.getSeaOrderId());
//        //获取相应驳回操作
//        OrderStatusEnum orderStatusEnum = OrderStatusEnum.getSeaOrderRejection(tmp.getStatus());
//        if (orderStatusEnum == null) {
//            return CommonResult.error(400, "驳回操作失败,没有相对应的操作");
//        }
//        //记录审核信息
//        AuditInfoForm auditInfoForm = new AuditInfoForm();
//        auditInfoForm.setExtId(seaCargoRejected.getSeaOrderId());
//        auditInfoForm.setExtDesc(SqlConstant.SEA_ORDER);
//        auditInfoForm.setAuditStatus(orderStatusEnum.getCode());
//        auditInfoForm.setAuditTypeDesc(orderStatusEnum.getDesc());
//
//        auditInfoForm.setAuditComment(seaCargoRejected.getCause());
//
//        Integer rejectOptions = seaCargoRejected.getRejectOptions() == null ? 1 : seaCargoRejected.getRejectOptions();
//        seaCargoRejected.setRejectOptions(rejectOptions);
//        switch (orderStatusEnum) {
//            case SEA_S_1_1:
//                //订单驳回
//                this.seaOrderService.orderReceiving(tmp, auditInfoForm, seaCargoRejected);
//                break;
//            case SEA_S_2_1:
//            case SEA_S_3_1:
//                this.seaOrderService.rejectedOpt(tmp, auditInfoForm, seaCargoRejected);
//                break;
//        }
//
//        return CommonResult.success();
//    }

}

