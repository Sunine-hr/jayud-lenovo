package com.jayud.oceanship.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.enums.WriteDirectionEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.entity.InitComboxStrVO;
import com.jayud.common.enums.*;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.StringUtils;
import com.jayud.common.utils.excel.EasyExcelUtils;
import com.jayud.oceanship.bo.*;
import com.jayud.oceanship.feign.FileClient;
import com.jayud.oceanship.feign.OauthClient;
import com.jayud.oceanship.feign.OmsClient;
import com.jayud.oceanship.po.*;
import com.jayud.oceanship.service.*;
import com.jayud.oceanship.utils.DateUtil;
import com.jayud.oceanship.utils.NumUtil;
import com.jayud.oceanship.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.jayud.common.enums.OrderStatusEnum.SEA_S_2;
import static com.jayud.oceanship.utils.PdfUtil.createPdfStream;

/**
 * <p>
 * ??????????????? ???????????????
 * </p>
 *
 * @author LLJ
 * @since 2021-01-28
 */
@RestController
@Slf4j
@Api(tags = "????????????")
@RequestMapping("/seaOrder")
public class SeaOrderController {

    @Autowired
    private ISeaOrderService seaOrderService;

    @Autowired
    private OmsClient omsClient;

    @Autowired
    private OauthClient oauthClient;

    @Autowired
    private FileClient fileClient;

    @Autowired
    private ISeaPortService seaPortService;

    @Autowired
    private ICabinetSizeNumberService cabinetSizeNumberService;

    @Autowired
    private ISeaReplenishmentService seaReplenishmentService;

    @Autowired
    private ISeaContainerInformationService seaContainerInformationService;

    @Autowired
    private ISeaBookshipService seaBookshipService;

    @Autowired
    private ISeaBillService seaBillService;

    @ApiOperation("??????????????????????????????")
    @PostMapping("/findByPage")
    public CommonResult findByPage(@RequestBody QuerySeaOrderForm form) {

        form.setStartTime();
        //????????????????????????
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
        //??????????????????
        Class<SeaOrderFormVO> seaOrderFormVOClass = SeaOrderFormVO.class;
        Field[] declaredFields = seaOrderFormVOClass.getDeclaredFields();
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
        IPage<SeaOrderFormVO> page = this.seaOrderService.findByPage(form);
        if (page.getRecords().size() == 0) {
            map1.put("pageInfo", new CommonPageResult(page));
            return CommonResult.success(map1);
        }

        String prePath = String.valueOf(fileClient.getBaseUrl().getData());

        List<SeaOrderFormVO> records = page.getRecords();
        List<String> seaOrderIds = new ArrayList<>();
        List<String> mainOrder = new ArrayList<>();
        List<Long> entityIds = new ArrayList<>();
        List<Long> supplierIds = new ArrayList<>();
        List<String> unitCodes = new ArrayList<>();
        List<Long> departmentIds = new ArrayList<>();
        List<String> subOrderNos = new ArrayList<>();
        for (SeaOrderFormVO record : records) {
            seaOrderIds.add(record.getOrderNo());
            mainOrder.add(record.getMainOrderNo());
            entityIds.add(record.getLegalEntityId());
            subOrderNos.add(record.getOrderNo());
            unitCodes.add(record.getUnitCode());
            if (record.getSeaBookShipForm().getAgentSupplierId() != null) {
                supplierIds.add(record.getSeaBookShipForm().getAgentSupplierId());
            }

            record.getSeaBookShipForm().getFile(prePath);
            if (record.getDepartmentId() != null) {
                departmentIds.add(record.getDepartmentId());
            }

        }

        //??????????????????
        List<GoodsVO> goods = this.omsClient.getGoodsByBusOrders(seaOrderIds, BusinessTypeEnum.HY.getCode()).getData();

        //??????????????????
        ApiResult legalEntityResult = null;
        if (CollectionUtils.isNotEmpty(entityIds)) {
            legalEntityResult = this.oauthClient.getLegalEntityByLegalIds(entityIds);
        }
        //?????????????????????
        JSONArray supplierInfo = null;
        if (CollectionUtils.isNotEmpty(supplierIds) && supplierIds.size() > 0) {
            supplierInfo = new JSONArray(this.omsClient.getSupplierInfoByIds(supplierIds).getData());
        }

        //??????????????????
        ApiResult departmentResult = null;
        if (CollectionUtils.isNotEmpty(departmentIds) && departmentIds.size() > 0) {
            departmentResult = this.oauthClient.getDepartmentByDepartment(departmentIds);
        }

        //????????????????????????
        ApiResult unitCodeInfo = null;
        if (CollectionUtils.isNotEmpty(unitCodes)) {
            unitCodeInfo = this.omsClient.getCustomerByUnitCode(unitCodes);
        }

        Map<String, Object> data = this.omsClient.isCost(subOrderNos, SubOrderSignEnum.HY.getSignOne()).getData();

        //?????????????????????
        ApiResult<List<OrderAddressVO>> resultOne = this.omsClient.getOrderAddressByBusOrders(seaOrderIds, BusinessTypeEnum.HY.getCode());
        if (resultOne.getCode() != HttpStatus.SC_OK) {
            log.warn("?????????????????????????????? seaOrderId={}", seaOrderIds);
        }

        //?????????????????????
        ApiResult result = omsClient.getMainOrderByOrderNos(mainOrder);
        for (SeaOrderFormVO record : records) {
            //??????????????????
            List<GoodsVO> goodsVOS = new ArrayList<>();
            for (GoodsVO goodsVO : goods) {
                if (record.getId().equals(goodsVO.getBusinessId())
                        && BusinessTypeEnum.HY.getCode().equals(goodsVO.getBusinessType())) {
                    goodsVOS.add(goodsVO);
                }
            }
            record.setOrderId(record.getId());
            record.setGoodsVOS(goodsVOS);
            record.assemblyGoodsInfo(goods);
            //?????????????????????
            record.assemblyMainOrderData(result.getData());
            //??????????????????
            record.assemblyLegalEntity(legalEntityResult);

            record.assemblyDepartment(departmentResult);

            record.setCost(MapUtil.getBool(data, record.getOrderNo()));

            record.assembleCostStatus(record.getOrderNo(),
                    this.omsClient.getCostStatus(null, Collections.singletonList(record.getOrderNo())).getData());

            //???????????????
            record.assemblySupplierInfo(supplierInfo);

            //??????????????????
            record.assemblyUnitCodeInfo(unitCodeInfo);

            record.setSubUnitCode(record.getUnitCode());

            //??????????????????
            for (OrderAddressVO address : resultOne.getData()) {
                address.getFile(prePath);
                if (address.getOrderNo().equals(record.getOrderNo())) {
                    record.processingAddress(address);
                }
            }

            //??????????????????
            if (record.getCabinetType().equals(1)) {
                List<CabinetSizeNumberVO> cabinetSizeNumberVOS = cabinetSizeNumberService.getList(record.getId());
                record.setCabinetSizeNumbers(cabinetSizeNumberVOS);
                record.assemblyCabinetInfo(cabinetSizeNumberVOS);
            }

            //?????????????????????
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.like("sea_order_no", record.getOrderNo());
            List<SeaReplenishment> seaReplenishments = seaReplenishmentService.list(queryWrapper);
            if (CollectionUtils.isNotEmpty(seaReplenishments)) {
                StringBuffer stringBuffer = new StringBuffer();
                List<SeaReplenishmentVO> seaReplenishmentVOS = ConvertUtil.convertList(seaReplenishments, SeaReplenishmentVO.class);
                for (SeaReplenishmentVO seaReplenishmentVO : seaReplenishmentVOS) {
                    seaReplenishmentVO.assemblyAdditionalServices();
                    stringBuffer.append(seaReplenishmentVO.getOrderNo()).append(",");
                    //???????????????????????????????????????????????????
                    List<CabinetSizeNumberVO> list1 = cabinetSizeNumberService.getList(seaReplenishmentVO.getId());
                    seaReplenishmentVO.setCabinetSizeNumbers(list1);
                    List<SeaContainerInformationVO> seaContainerInformations = seaContainerInformationService.getList(seaReplenishmentVO.getOrderNo());
                    seaReplenishmentVO.setSeaContainerInformations(seaContainerInformations);
                }
                record.setSeaReplenishments(seaReplenishmentVOS);
                record.setRepOrderNo(stringBuffer.substring(0, stringBuffer.length() - 1));
            }

        }
        map1.put("pageInfo", new CommonPageResult(page));
        return CommonResult.success(map1);
    }

    @ApiOperation("????????????????????????????????????????????????")
    @PostMapping("/findBillByPage")
    public CommonResult findBillByPage(@RequestBody QuerySeaOrderForm form) {
        form.setStartTime();
        //????????????????????????
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
        //??????????????????
        Class<SeaReplenishmentFormVO> replenishmentFormVOClass = SeaReplenishmentFormVO.class;
        Field[] declaredFields = replenishmentFormVOClass.getDeclaredFields();
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

        IPage<SeaReplenishmentFormVO> page = this.seaReplenishmentService.findBillByPage(form);

        //IPage<SeaOrderFormVO> page = this.seaOrderService.findByPage(form);
        if (page.getRecords().size() == 0) {
            map1.put("pageInfo", new CommonPageResult(page));
            return CommonResult.success(map1);
        }

        String prePath = String.valueOf(fileClient.getBaseUrl().getData());
        List<SeaReplenishmentFormVO> records = page.getRecords();
        List<String> seaOrderIds = new ArrayList<>();
        List<String> mainOrder = new ArrayList<>();
        List<String> subOrderNos = new ArrayList<>();
        for (SeaReplenishmentFormVO record : records) {
            seaOrderIds.add(record.getOrderNo());
            //????????????????????????
            SeaOrderVO seaOrderByOrderNO = seaOrderService.getSeaOrderByOrderNO(record.getSeaOrderId());
            mainOrder.add(seaOrderByOrderNO.getMainOrderNo());
            record.setMainOrderNo(seaOrderByOrderNO.getMainOrderNo());
            record.setStatus(seaOrderByOrderNO.getStatus());
            record.setProcessStatus(seaOrderByOrderNO.getProcessStatus());
            subOrderNos.add(seaOrderByOrderNO.getOrderNo());
        }

        //??????????????????
        List<GoodsVO> goods = this.omsClient.getGoodsByBusOrders(seaOrderIds, BusinessTypeEnum.HY.getCode()).getData();
        if (CollectionUtils.isEmpty(goods)) {
            log.warn("?????????????????????????????? seaOrderId={}", seaOrderIds);
        }

        //?????????????????????
        ApiResult<List<OrderAddressVO>> resultOne = this.omsClient.getOrderAddressByBusOrders(seaOrderIds, BusinessTypeEnum.HY.getCode());
        if (resultOne.getCode() != HttpStatus.SC_OK) {
            log.warn("?????????????????????????????? seaOrderId={}", seaOrderIds);
        }

        Map<String, Object> data = this.omsClient.isCost(subOrderNos, SubOrderSignEnum.HY.getSignOne()).getData();

        //?????????????????????
        ApiResult result = omsClient.getMainOrderByOrderNos(mainOrder);
        for (SeaReplenishmentFormVO record : records) {
            //??????????????????
            List<GoodsVO> goodsVOS = new ArrayList<>();
            for (GoodsVO goodsVO : goods) {
                if (record.getId().equals(goodsVO.getBusinessId())
                        && BusinessTypeEnum.HY.getCode().equals(goodsVO.getBusinessType())) {
                    goodsVOS.add(goodsVO);
                }
            }
            record.setOrderId(record.getSeaOrderId());
            record.setGoodsVOS(goodsVOS);
            record.assemblyGoodsInfo(goods);
            record.assemblySeaPort();

            record.setCost(MapUtil.getBool(data, record.getOrderNo()));
            //?????????????????????
            record.assemblyMainOrderData(result.getData());
            //??????????????????
            for (OrderAddressVO address : resultOne.getData()) {
                address.getFile(prePath);
                if (address.getOrderNo().equals(record.getOrderNo())) {
                    record.processingAddress(address);
                }
            }

            //??????????????????
//            if (record.getCabinetType().equals(1)) {
//                List<CabinetSizeNumberVO> cabinetSizeNumberVOS = cabinetSizeNumberService.getList(record.getId());
//                record.setCabinetSizeNumbers(cabinetSizeNumberVOS);
//                record.assemblyCabinetInfo(cabinetSizeNumberVOS);
//            }
            //??????????????????
            if (record.getCabinetType().equals(1)) {
                List<SeaContainerInformationVO> seaContainerInformations = seaContainerInformationService.getList(record.getOrderNo());
                record.setSeaContainerInformations(seaContainerInformations);
            }
        }
        map1.put("pageInfo", new CommonPageResult(page));
        return CommonResult.success(map1);
    }


    //????????????,cmd = S_0?????????,S_1????????????,S_2??????,S_3????????????, S_4????????????,S_5????????????,S_6????????????,S_7????????????,S_8????????????,S_9????????????S_10????????????
    @ApiOperation(value = "????????????????????????")
    @PostMapping(value = "/doSeaProcessOpt")
    public CommonResult doSeaProcessOpt(@RequestBody @Valid SeaProcessOptForm form, BindingResult result) {

        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                return CommonResult.error(444, error.getDefaultMessage());
            }
        }

        if (form.getMainOrderId() == null || form.getOrderId() == null) {
            log.warn("?????????id/????????????id??????");
            return CommonResult.error(ResultEnum.VALIDATE_FAILED);
        }
        //??????????????????
        SeaOrder seaOrder = this.seaOrderService.getById(form.getOrderId());
        if (ProcessStatusEnum.COMPLETE.getCode().equals(seaOrder.getProcessStatus())) {
            return CommonResult.error(400, "?????????????????????");
        }
        if (!ProcessStatusEnum.PROCESSING.getCode().equals(seaOrder.getProcessStatus())) {
            return CommonResult.error(400, "????????????????????????");
        }
        if (!seaOrder.getStatus().equals(form.getStatus())) {
            return CommonResult.error(400, "????????????????????????");
        }
//        OrderStatusEnum statusEnum = OrderStatusEnum.getSeaOrderNextStatus(seaOrder.getStatus());
//        QueryWrapper queryWrapper = new QueryWrapper();
//        queryWrapper.eq("f_status",seaOrder.getStatus());
//        queryWrapper.eq("order_no",seaOrder.getOrderNo());
//        OrderFlowSheet orderFlowSheet = orderFlowSheetService.getOne(queryWrapper);
        String orderProcessNode = (String) omsClient.getOrderProcessNode(seaOrder.getMainOrderNo(), seaOrder.getOrderNo(), seaOrder.getStatus()).getData();
        OrderStatusEnum statusEnum = OrderStatusEnum.getSeaOrderNextStatus(orderProcessNode);
        System.out.println("statusEnum=====================================================" + statusEnum);
        if (statusEnum == null) {
            log.error("??????????????????????????????,?????????????????? data={}", seaOrder.toString());
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }
        //????????????
        form.checkProcessOpt(statusEnum);
        form.setStatus(statusEnum.getCode());

        //????????????
        switch (statusEnum) {
            case SEA_S_1: //????????????
                SeaOrder seaOrder1 = new SeaOrder();
                seaOrder1.setOrderTaker(form.getOperatorUser());
                seaOrder1.setReceivingOrdersDate(DateUtils.str2LocalDateTime(form.getOperatorTime(), DateUtils.DATE_TIME_PATTERN));
                this.seaOrderService.updateProcessStatus(seaOrder1, form);
                break;
            case SEA_S_2: //??????
            case SEA_S_7: //????????????
            case SEA_S_9: //????????????
                this.seaOrderService.doSeaBookShipOpt(form);
                break;
            case SEA_S_3: //??????????????????
                this.seaOrderService.updateProcessStatus(new SeaOrder(), form);
                break;
            case SEA_S_4: //????????????
                this.seaOrderService.updateOrSaveProcessStatus(form);
                break;
            case SEA_S_5: //????????????
                this.seaOrderService.updateOrSaveReplenishmentAudit(form);
                break;
            case SEA_S_6: //??????????????????
            case SEA_S_8: //????????????
                this.seaOrderService.updateOrSaveConfirmationAudit(form);
                break;
            case SEA_S_10: //????????????
                StringBuilder sb = new StringBuilder();
                form.getProxyServiceType().forEach(e -> sb.append(e).append(","));
                String proxyServiceType = sb.length() == 0 ? null : sb.substring(0, sb.length() - 1);
                SeaOrder seaOrder2 = new SeaOrder();
                seaOrder2.setOverseasSuppliersId(form.getAgentSupplierId());
                seaOrder2.setProxyServiceType(proxyServiceType);
                this.seaOrderService.updateProcessStatus(seaOrder2, form);
                break;
            case SEA_S_11: //????????????
                this.seaOrderService.updateProcessStatus(new SeaOrder(), form);
                break;
        }

        return CommonResult.success();
    }


    @ApiOperation(value = "?????????????????? seaOrderId=????????????id")
    @PostMapping(value = "/getSeaOrderDetails")
    public CommonResult<SeaOrderVO> getSeaOrderDetails(@RequestBody Map<String, Object> map) {
        Long seaOrderId = MapUtil.getLong(map, "seaOrderId");
        if (seaOrderId == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        SeaOrderVO seaOrderDetails = this.seaOrderService.getSeaOrderDetails(seaOrderId);

        return CommonResult.success(seaOrderDetails);
    }

    @ApiOperation(value = "?????????????????? seaOrderId=????????????id")
    @PostMapping(value = "/getSeaRepOrderDetails")
    public CommonResult<SeaOrderFormVO> getSeaRepOrderDetails(@RequestBody Map<String, Object> map) {
        Long orderId = MapUtil.getLong(map, "id");
        if (orderId == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        SeaReplenishmentVO seaReplenishmentVO = this.seaReplenishmentService.getSeaRepOrderDetails(orderId);
        SeaOrder byId = seaOrderService.getById(seaReplenishmentVO.getSeaOrderId());
        if (byId.getCabinetType().equals(1)) {
            List<CabinetSizeNumberVO> cabinetSizeNumberVOS = cabinetSizeNumberService.getList(byId.getId());
            seaReplenishmentVO.setCabinetSizeNumbers(cabinetSizeNumberVOS);
            if (CollectionUtils.isNotEmpty(cabinetSizeNumberVOS)) {
                seaReplenishmentVO.assemblyCabinetInfo(cabinetSizeNumberVOS);
            }

        }
        if (seaReplenishmentVO.getSeaContainerInformations() == null || seaReplenishmentVO.getSeaContainerInformations().size() < 0) {
            List<SeaContainerInformationVO> seaContainerInformations = new ArrayList<>();
            seaContainerInformations.add(new SeaContainerInformationVO());
            seaReplenishmentVO.setSeaContainerInformations(seaContainerInformations);
        }
        if (seaReplenishmentVO.getNotificationAddress() == null || seaReplenishmentVO.getNotificationAddress().size() < 0) {
            List<OrderAddressVO> notificationAddress = new ArrayList<>();
            OrderAddressVO orderAddressVO = new OrderAddressVO();
            orderAddressVO.setType(2);
            notificationAddress.add(orderAddressVO);
            seaReplenishmentVO.setNotificationAddress(notificationAddress);
        }
        List<SeaReplenishmentVO> seaReplenishmentVOS = new ArrayList<>();
        seaReplenishmentVOS.add(seaReplenishmentVO);
        String orderNo = seaReplenishmentVO.getSeaOrderNo();
        String[] orderNoes = orderNo.split(",");
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("order_no", orderNoes[0]);
        SeaOrder seaOrder1 = this.seaOrderService.getOne(queryWrapper);
        this.seaOrderService.getSeaOrderDetails(seaOrder1.getId());
        SeaOrderFormVO convert = ConvertUtil.convert(seaOrder1, SeaOrderFormVO.class);
        convert.setOrderId(convert.getId());
        //?????????????????????
        List<String> mainOrder = new ArrayList<>();
        mainOrder.add(convert.getMainOrderNo());
        ApiResult result = omsClient.getMainOrderByOrderNos(mainOrder);
        convert.assemblyMainOrderData(result.getData());
        convert.setSeaReplenishments(seaReplenishmentVOS);
        return CommonResult.success(convert);
    }


    @ApiOperation(value = "?????????????????? seaOrderId=????????????id")
    @PostMapping(value = "/getSeaReplenishmentOrderDetails")
    public CommonResult<SeaOrderVO> getSeaReplenishmentOrderDetails(@RequestBody Map<String, Object> map) {
        Long seaOrderId = MapUtil.getLong(map, "id");
        if (seaOrderId == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        SeaOrderVO seaOrderDetails = this.seaOrderService.getSeaOrderDetails(seaOrderId);
        if (seaOrderDetails.getSeaReplenishments() == null || seaOrderDetails.getSeaReplenishments().size() <= 0) {
            List<SeaReplenishmentVO> seaReplenishmentVOS = new ArrayList<>();
            SeaReplenishmentVO convert = ConvertUtil.convert(seaOrderDetails, SeaReplenishmentVO.class);
            convert.setOrderNo(null);
            convert.setSeaOrderId(seaOrderDetails.getOrderId());
            convert.setSeaOrderNo(seaOrderDetails.getOrderNo());
            if (CollectionUtils.isNotEmpty(seaOrderDetails.getCabinetSizeNumbers())) {
                convert.assemblyCabinetInfo(seaOrderDetails.getCabinetSizeNumbers());
            }

            if (convert.getSeaContainerInformations() == null || convert.getSeaContainerInformations().size() < 0) {
                List<SeaContainerInformationVO> seaContainerInformations = new ArrayList<>();
                seaContainerInformations.add(new SeaContainerInformationVO());
                convert.setSeaContainerInformations(seaContainerInformations);
            }
            if (convert.getNotificationAddress() == null || convert.getNotificationAddress().size() < 0) {
                List<OrderAddressVO> notificationAddress = new ArrayList<>();
                OrderAddressVO orderAddressVO = new OrderAddressVO();
                orderAddressVO.setType(2);
                notificationAddress.add(orderAddressVO);
                convert.setNotificationAddress(notificationAddress);
            }
            //??????????????????
            SeaBookship enableBySeaOrderId = seaBookshipService.getEnableBySeaOrderId(seaOrderDetails.getOrderId());
            if (enableBySeaOrderId.getShipName() != null) {
                convert.setShipName(enableBySeaOrderId.getShipName());
            }
            if (enableBySeaOrderId.getShipNumber() != null) {
                convert.setShipNumber(enableBySeaOrderId.getShipNumber());
            }
            if (enableBySeaOrderId.getEtd() != null) {
                convert.setSailingTime(enableBySeaOrderId.getEtd());
            }
            seaReplenishmentVOS.add(convert);
            seaOrderDetails.setSeaReplenishments(seaReplenishmentVOS);
        } else {
            List<SeaReplenishmentVO> seaReplenishments = seaOrderDetails.getSeaReplenishments();
            List<SeaReplenishmentVO> seaReplenishmentVOS = new ArrayList<>();
            for (SeaReplenishmentVO seaReplenishment : seaReplenishments) {
                seaReplenishment.setCabinetSizeNumbers(seaOrderDetails.getCabinetSizeNumbers());
                if (CollectionUtils.isNotEmpty(seaOrderDetails.getCabinetSizeNumbers())) {
                    seaReplenishment.assemblyCabinetInfo(seaOrderDetails.getCabinetSizeNumbers());
                }

                if (seaReplenishment.getSeaContainerInformations() == null || seaReplenishment.getSeaContainerInformations().size() < 0 || seaReplenishment.getCabinetTypeName().equals("LCL")) {
                    List<SeaContainerInformationVO> seaContainerInformations = new ArrayList<>();
                    seaContainerInformations.add(new SeaContainerInformationVO());
                    seaReplenishment.setSeaContainerInformations(seaContainerInformations);
                }
                if (seaReplenishment.getNotificationAddress() == null || seaReplenishment.getNotificationAddress().size() < 0) {
                    List<OrderAddressVO> notificationAddress = new ArrayList<>();
                    OrderAddressVO orderAddressVO = new OrderAddressVO();
                    orderAddressVO.setType(2);
                    notificationAddress.add(orderAddressVO);
                    seaReplenishment.setNotificationAddress(notificationAddress);
                }
                //??????????????????
                SeaBookship enableBySeaOrderId = seaBookshipService.getEnableBySeaOrderId(seaOrderDetails.getOrderId());
                if (enableBySeaOrderId.getShipName() != null) {
                    seaReplenishment.setShipName(enableBySeaOrderId.getShipName());
                }
                if (enableBySeaOrderId.getShipNumber() != null) {
                    seaReplenishment.setShipNumber(enableBySeaOrderId.getShipNumber());
                }
                seaReplenishmentVOS.add(seaReplenishment);
            }
            seaOrderDetails.setSeaReplenishments(seaReplenishmentVOS);
        }

        return CommonResult.success(seaOrderDetails);
    }


    @ApiOperation(value = "?????????????????? id=????????????id")
    @PostMapping(value = "/bookShipRejectionEdit")
    public CommonResult bookingRejectionEdit(@RequestBody SeaProcessOptForm form) {
        if (form.getMainOrderId() == null
                || form.getOrderId() == null
                || form.getSeaBookShipForm().getId() == null) {
            log.warn("??????????????????/????????????id??????/????????????id?????? data={}", JSONUtil.toJsonStr(form));
            return CommonResult.error(ResultEnum.VALIDATE_FAILED);
        }

        SeaOrder seaOrder = this.seaOrderService.getById(form.getOrderId());
        if (!OrderStatusEnum.SEA_S_3_2.getCode().equals(seaOrder.getStatus())) {
            log.warn("???????????????????????????????????? status={}", OrderStatusEnum.getDesc(seaOrder.getStatus()));
            return CommonResult.error(400, "????????????????????????????????????");
        }
        form.setStatus(SEA_S_2.getCode());
        //????????????
        form.checkProcessOpt(SEA_S_2);
        //????????????????????????
        this.seaOrderService.doSeaBookShipOpt(form);
        return CommonResult.success();
    }


    @ApiOperation(value = "??????????????????")
    @PostMapping(value = "/rejectOrder")
    public CommonResult rejectOrder(@RequestBody SeaCargoRejected seaCargoRejected) {
        //??????????????????
        SeaOrder tmp = this.seaOrderService.getById(seaCargoRejected.getSeaOrderId());
        //????????????????????????
        OrderStatusEnum orderStatusEnum = OrderStatusEnum.getSeaOrderRejection(tmp.getStatus());
        if (orderStatusEnum == null) {
            return CommonResult.error(400, "??????????????????,????????????????????????");
        }
        //??????????????????
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setExtId(seaCargoRejected.getSeaOrderId());
        auditInfoForm.setExtDesc(SqlConstant.SEA_ORDER);
        auditInfoForm.setAuditStatus(orderStatusEnum.getCode());
        auditInfoForm.setAuditTypeDesc(orderStatusEnum.getDesc());

        auditInfoForm.setAuditComment(seaCargoRejected.getCause());

        Integer rejectOptions = seaCargoRejected.getRejectOptions() == null ? 1 : seaCargoRejected.getRejectOptions();
        seaCargoRejected.setRejectOptions(rejectOptions);
        switch (orderStatusEnum) {
            case SEA_S_1_1:
                //????????????
                this.seaOrderService.orderReceiving(tmp, auditInfoForm, seaCargoRejected);
                break;
            case SEA_S_2_1:
            case SEA_S_3_1:
                this.seaOrderService.rejectedOpt(tmp, auditInfoForm, seaCargoRejected);
                break;
        }

        return CommonResult.success();
    }

    /**
     * ????????????????????????
     */
    @ApiOperation(value = "????????????????????????")
    @PostMapping(value = "/isReleaseOrder")
    public CommonResult isReleaseOrder(@RequestBody Map<String, Object> map) {
        Long orderId = MapUtil.getLong(map, "orderId");
        //???????????????id?????????????????????
        SeaReplenishment seaReplenishment = seaReplenishmentService.getById(orderId);
        String seaOrderNo = seaReplenishment.getSeaOrderNo();
        String[] split = seaOrderNo.split(",");
        for (String s : split) {
            SeaOrder byMainOrderNO = seaOrderService.getByOrderNO(s);
            if (!byMainOrderNO.getStatus().equals("S_7")) {
                return CommonResult.error(444, "????????????????????????????????????");
            }
        }
        return CommonResult.success();
    }

    /**
     * ???????????????
     */
    @Value("${address.seaAddr}")
    private String filePath;

//    @ApiOperation(value = "???????????????")
//    @GetMapping(value = "/uploadExcel")
//    public void uploadExcel(@RequestParam("orderId") Long orderId, HttpServletResponse response) throws IOException {
////        Long orderId = MapUtil.getLong(map1, "OrderId");
//        SeaOrderVO seaOrderDetails = seaOrderService.getSeaOrderDetails(orderId);
//        List<SeaReplenishmentVO> seaReplenishments = seaOrderDetails.getSeaReplenishments();
//
//
//
//
//        File file = new File(filePath);
//        String filename1 = file.getName();
//
//        InputStream inputStream = new FileInputStream(file);
////        String fileType = filename1.substring(filename1.lastIndexOf("."));
//        XSSFWorkbook templateWorkbook = new XSSFWorkbook(inputStream); // 2007+
//
//        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//        templateWorkbook.write(outStream);
//        ByteArrayInputStream templateInputStream = new ByteArrayInputStream(outStream.toByteArray());
//
//        EasyExcelUtils.copyFirstSheet(templateWorkbook, seaReplenishments.size() - 1);
//        // update sheet name
//        String sheetNamePrefix = "Sheet-";
//        for (int i = 0; i < templateWorkbook.getNumberOfSheets(); i++) {
//            String sheetName = sheetNamePrefix + (i + 1);
//            templateWorkbook.setSheetName(i, sheetName);
//        }
//
//
//        String fileName = "????????????";
//        ExcelWriter excelWriter = null;
//        if (response != null) {
//            response.setContentType("application/vnd.ms-excel");
//            response.setContentType("application/msexcel;charset=UTF-8");
//            response.setCharacterEncoding("utf-8");
//            // ??????URLEncoder.encode???????????????????????? ?????????easyexcel????????????
//            String filename = URLEncoder.encode(fileName, "utf-8");
//            response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
//            excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(templateInputStream).build();
//        } else {
//            excelWriter = EasyExcel.write(fileName).withTemplate(templateInputStream).build();
//        }
//
//        FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
//
//        for (int i = 0; i < seaReplenishments.size(); i++) {
//                SeaReplenishmentVO seaReplenishment = seaReplenishments.get(i);
//                WriteSheet writeSheet = EasyExcel.writerSheet(i).build();
//
//                //?????????????????????
//                excelWriter.fill(new FillWrapper("delivery", seaReplenishment.getDeliveryAddress()), fillConfig, writeSheet);
//                excelWriter.fill(new FillWrapper("shipping", seaReplenishment.getShippingAddress()), fillConfig, writeSheet);
//                if (seaOrderDetails.getNotificationAddress() != null && seaReplenishment.getNotificationAddress().size() > 0) {
//                    excelWriter.fill(new FillWrapper("notification", seaReplenishment.getNotificationAddress()), fillConfig, writeSheet);
//                }
//                excelWriter.fill(new FillWrapper("goodone", seaReplenishment.getGoodsForms()), fillConfig, writeSheet);
//                excelWriter.fill(new FillWrapper("seaContainerInformation", seaReplenishment.getSeaContainerInformations()), fillConfig, writeSheet);
//
//                //?????????????????????
//                Map<String, Object> map = new HashMap<String, Object>();
//                map.put("shipCompany", seaOrderDetails.getSeaBookshipVO().getShipCompany());
//                map.put("shipNumber", seaOrderDetails.getSeaBookshipVO().getShipNumber());
//                map.put("portDeparture", seaOrderDetails.getPortDeparture());
//                map.put("portDestination", seaOrderDetails.getPortDestination());
//                map.put("cabinetType", seaOrderDetails.getCabinetTypeName());
//                if (seaOrderDetails.getCabinetTypeName().equals("FCL")) {
//                    map.put("whether", "???");
//                } else {
//                    map.put("whether2", "???");
//                }
//
//                List<SeaContainerInformationVO> seaContainerInformations = seaReplenishment.getSeaContainerInformations();
//                Integer totalBulkCargoAmount = 0;
//                Double totalWeights = 0.0;
//                Double totalvolume = 0.0;
//                for (SeaContainerInformationVO seaContainerInformation : seaContainerInformations) {
//                    totalBulkCargoAmount = totalBulkCargoAmount + seaContainerInformation.getPlatNumber();
//                    totalWeights = totalWeights + seaContainerInformation.getWeight();
//                    if (seaContainerInformation.getVolume() != null) {
//                        totalvolume = totalvolume + seaContainerInformation.getVolume();
//                    }
//
//                }
//                map.put("totalBulkCargoAmount", totalBulkCargoAmount);
//                map.put("totalWeights", totalWeights);
//                map.put("totalvolume", totalvolume);
//                excelWriter.fill(map, writeSheet);
//
//        }
//        excelWriter.finish();
//        inputStream.close();
//        outStream.close();
//
//
//    }


    @ApiOperation(value = "???????????????")
    @GetMapping(value = "/uploadExcel")
    public void uploadExcel(@RequestParam("orderId") Long orderId, HttpServletResponse response) throws IOException {
//        Long orderId = MapUtil.getLong(map1, "OrderId");
        SeaOrderVO seaOrderDetails = seaOrderService.getSeaOrderDetails(orderId);
        List<SeaReplenishmentVO> seaReplenishments = seaOrderDetails.getSeaReplenishments();

        File file = new File(filePath);
        String filename1 = file.getName();

        InputStream inputStream = new FileInputStream(file);
        String fileType = filename1.substring(filename1.lastIndexOf("."));
        XSSFWorkbook templateWorkbook = new XSSFWorkbook(inputStream); // 2007+
        if ((seaReplenishments.size() - 1) != 0) {
            EasyExcelUtils.copyFirstSheet(templateWorkbook, seaReplenishments.size() - 1);
            // update sheet name
            String sheetNamePrefix = "Sheet-";
            for (int i = 0; i < templateWorkbook.getNumberOfSheets(); i++) {
                String sheetName = sheetNamePrefix + (i + 1);
                templateWorkbook.setSheetName(i, sheetName);
            }
        }


        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        templateWorkbook.write(outStream);
        ByteArrayInputStream templateInputStream = new ByteArrayInputStream(outStream.toByteArray());

        String fileName = "????????????";

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // ??????URLEncoder.encode???????????????????????? ?????????easyexcel????????????
        String filename = URLEncoder.encode(fileName, "utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");

        ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(templateInputStream).build();

//                WriteSheet writeSheet = EasyExcel.writerSheet().build();

        FillConfig fillConfig = FillConfig.builder().direction(WriteDirectionEnum.HORIZONTAL).build();

        int count = 0;
        for (SeaReplenishmentVO seaReplenishment : seaReplenishments) {
            WriteSheet writeSheet = EasyExcel.writerSheet(count).build();
//
            //HSSFWorkbook templateWorkbook = new HSSFWorkbook(inputStream);


            //?????????????????????
            excelWriter.fill(new FillWrapper("delivery", seaReplenishment.getDeliveryAddress()), fillConfig, writeSheet);
            excelWriter.fill(new FillWrapper("shipping", seaReplenishment.getShippingAddress()), fillConfig, writeSheet);
            if (seaReplenishment.getNotificationAddress() != null && seaReplenishment.getNotificationAddress().size() > 0) {
                excelWriter.fill(new FillWrapper("notification", seaReplenishment.getNotificationAddress()), fillConfig, writeSheet);
            }
            excelWriter.fill(new FillWrapper("goodone", seaReplenishment.getGoodsForms()), fillConfig, writeSheet);
            excelWriter.fill(new FillWrapper("seaContainerInformation", seaReplenishment.getSeaContainerInformations()), writeSheet);

            //?????????????????????
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("shipCompany", seaOrderDetails.getSeaBookshipVO().getShipCompany());
            map.put("shipNumber", seaOrderDetails.getSeaBookshipVO().getShipNumber());
            map.put("portDeparture", seaReplenishment.getPortDepartureName());
            map.put("portDestination", seaReplenishment.getPortDestinationName());
            map.put("cabinetType", seaReplenishment.getCabinetTypeName());
            if (seaReplenishment.getCabinetTypeName().equals("FCL")) {
                map.put("whether", "???");
            } else {
                map.put("whether2", "???");
            }

            if (seaReplenishment.getCabinetTypeName().equals("FCL")) {
                List<SeaContainerInformationVO> seaContainerInformations = seaReplenishment.getSeaContainerInformations();
                Integer totalBulkCargoAmount = 0;
                Double totalWeights = 0.0;
                Double totalvolume = 0.0;
                if (seaContainerInformations != null || seaContainerInformations.size() > 0) {
                    for (SeaContainerInformationVO seaContainerInformation : seaContainerInformations) {
                        if (seaContainerInformation.getPlatNumber() != null) {
                            totalBulkCargoAmount = totalBulkCargoAmount + seaContainerInformation.getPlatNumber();
                        }
                        if (seaContainerInformation.getWeight() != null) {
                            totalWeights = totalWeights + seaContainerInformation.getWeight();
                        }
                        if (seaContainerInformation.getVolume() != null) {
                            totalvolume = totalvolume + seaContainerInformation.getVolume();
                        }

                    }
                }

                map.put("totalBulkCargoAmount", totalBulkCargoAmount);
                map.put("totalWeights", totalWeights);
                map.put("totalvolume", totalvolume);
            }

            excelWriter.fill(map, writeSheet);

            ++count;


        }
        excelWriter.finish();
        outStream.close();
        inputStream.close();
    }


    @Value("${address.manifestAddress}")
    private String path;

    @Value("${address.imgAddress}")
    private String imgPath;

    @ApiOperation(value = "????????????pdf??????excel")
    @GetMapping(value = "/uploadExcelManifest")
    public void uploadExcelManifest(@RequestParam("id") Long id, HttpServletResponse response) {
        //??????id??????????????????
        SeaOrderVO seaOrder = seaOrderService.getSeaOrderDetails(id);

        List<OrderAddressVO> deliveryAddress = seaOrder.getDeliveryAddress();
        List<OrderAddressVO> shippingAddress = seaOrder.getShippingAddress();
        List<OrderAddressVO> notificationAddress = seaOrder.getNotificationAddress();
        List<GoodsVO> goodsForms = seaOrder.getGoodsForms();

        Map<String, String> resultMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(deliveryAddress)) {
            if (deliveryAddress.get(0).getAddress() != null) {
                resultMap.put("shipperInformation", deliveryAddress.get(0).getAddress());
            }
        }
        if (CollectionUtils.isNotEmpty(shippingAddress)) {
            if (shippingAddress.get(0).getAddress() != null) {
                resultMap.put("consigneeInformation", shippingAddress.get(0).getAddress());
            }
        }
        if (CollectionUtils.isNotEmpty(notificationAddress)) {
            if (notificationAddress.get(0).getAddress() != null) {
                resultMap.put("notifierInformation", notificationAddress.get(0).getAddress());
            }
        }

        if (seaOrder.getDestination() != null) {
            resultMap.put("destination", seaOrder.getDestination());
        }
        if (seaOrder.getPortDepartureCode() != null) {
            resultMap.put("portDepartureCode", seaPortService.getPortName(seaOrder.getPortDepartureCode()));
        }
        if (seaOrder.getPortDestinationCode() != null) {
            //????????????????????????????????????
            resultMap.put("portDestinationCode", seaPortService.getPortName(seaOrder.getPortDestinationCode()));
        }
        if (CollectionUtils.isNotEmpty(goodsForms)) {
            if (goodsForms.get(0).getLabel() != null) {
                resultMap.put("shippingMark", goodsForms.get(0).getLabel());
            }
            if (goodsForms.get(0).getName() != null) {
                resultMap.put("goodName", goodsForms.get(0).getName());
            }
            if (goodsForms.get(0).getBulkCargoAmount() != null) {
                resultMap.put("number", goodsForms.get(0).getBulkCargoAmount() + (goodsForms.get(0).getBulkCargoUnit() == null ? "" : goodsForms.get(0).getBulkCargoUnit()));
            }
            if (goodsForms.get(0).getTotalWeight() != null) {
                resultMap.put("weight", goodsForms.get(0).getTotalWeight() + "KGS");
            }
            if (goodsForms.get(0).getVolume() != null) {
                resultMap.put("volume", goodsForms.get(0).getVolume() + "CBM");
            }
        }
        if (seaOrder.getCreateTime() != null) {
            resultMap.put("createTime", seaOrder.getCreateTime().toString().replaceAll("T", " ").substring(0, 10));
        }
        if (seaOrder.getCreateUser() != null) {
            resultMap.put("createUser", seaOrder.getCreateUser());
        }
        //??????????????????????????????????????????
        ApiResult systemUserByName = oauthClient.getSystemUserByName(seaOrder.getCreateUser());
        if (systemUserByName != null && systemUserByName.getCode() == HttpStatus.SC_OK) {
            JSONObject jsonObject = new JSONObject(systemUserByName.getData());
            resultMap.put("phone", jsonObject.getStr("phone"));
            resultMap.put("email", jsonObject.getStr("email"));
        }
        //?????????????????????
        ApiResult mainOrderByOrderNos = omsClient.getMainOrderByOrderNos(Collections.singletonList(seaOrder.getMainOrderNo()));
        if (mainOrderByOrderNos != null && mainOrderByOrderNos.getCode() == HttpStatus.SC_OK) {
            JSONArray legalEntitys = new JSONArray(mainOrderByOrderNos.getData());

            JSONObject json = legalEntitys.getJSONObject(0);
            resultMap.put("remarks", json.getStr("remarks"));


        }
        //??????????????????
        SeaBookship enableBySeaOrderId = seaBookshipService.getEnableBySeaOrderId(seaOrder.getOrderId());
        if (enableBySeaOrderId != null) {
            resultMap.put("ship", enableBySeaOrderId.getShipName() == null ? "" : enableBySeaOrderId.getShipName() + "/" + enableBySeaOrderId.getShipNumber() == null ? "" : enableBySeaOrderId.getShipNumber());
        }
        if (seaOrder.getCabinetType().equals(1)) {
            List<CabinetSizeNumberVO> list = cabinetSizeNumberService.getList(seaOrder.getOrderId());
            if (CollectionUtils.isNotEmpty(list)) {
                StringBuffer stringBuffer = new StringBuffer();
                for (CabinetSizeNumberVO cabinetSizeNumberVO : list) {
                    stringBuffer.append(cabinetSizeNumberVO.getCabinetTypeSize()).append("??").append(cabinetSizeNumberVO.getNumber()).append("  ");
                }
                resultMap.put("cabinet", stringBuffer.toString());
            }

        } else {
            resultMap.put("cabinet", "CFS ( Y )");
        }
        Map<String, String> imgMap = new HashMap<>();
        imgMap.put("img", imgPath);


        //2.???????????????????????????
        ByteArrayOutputStream pdf = createPdfStream(path, resultMap, imgMap, null);


        //3.????????????????????????
        String fileName = "??????.pdf";
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            response.setContentType("application/vnd.ms-pdf");
            response.setCharacterEncoding("utf-8");
            // ??????URLEncoder.encode???????????????????????? ?????????easyexcel????????????
            String filename = URLEncoder.encode(fileName, "utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + filename);

            out.write(pdf.toByteArray());
            out.flush();
            out.close();
            pdf.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    @Value("${address.costAddress}")
    private String costPath;

    @ApiOperation(value = "??????????????????")
    @GetMapping(value = "/uploadCostExcel")
    public void uploadCostExcel(@RequestParam("orderNo") String orderNo, HttpServletResponse response) {
        SeaOrder byMainOrderNO = seaOrderService.getByMainOrderNO(orderNo);
        SeaOrderVO seaOrderDetails = seaOrderService.getSeaOrderDetails(byMainOrderNO.getId());

        File file = new File(costPath);
        String name = file.getName();
//
        try {
//            InputStream inputStream = new FileInputStream(file);
//
//            Workbook templateWorkbook = null;
//            String fileType = name.substring(name.lastIndexOf("."));
//            if (".xls".equals(fileType)) {
//                templateWorkbook = new HSSFWorkbook(inputStream); // 2003-
//            } else if (".xlsx".equals(fileType)) {
//                templateWorkbook = new XSSFWorkbook(inputStream); // 2007+
//            } else {
//
//            }
//            //HSSFWorkbook templateWorkbook = new HSSFWorkbook(inputStream);
//
//            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//            templateWorkbook.write(outStream);
//            ByteArrayInputStream templateInputStream = new ByteArrayInputStream(outStream.toByteArray());
//
            String fileName = seaOrderDetails.getMainOrderNo();
//
//            response.setContentType("application/vnd.ms-excel");
//            response.setCharacterEncoding("utf-8");
//            // ??????URLEncoder.encode???????????????????????? ?????????easyexcel????????????
//            String filename = URLEncoder.encode(fileName, "utf-8");
//            response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
//
//            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(templateInputStream).build();
//
//            WriteSheet writeSheet = EasyExcel.writerSheet().build();
//
//            FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();

//            FillConfig fillConfig = FillConfig.builder().direction(WriteDirectionEnum.VERTICAL).build();
            //?????????????????????
//            Map<String, Object> map = new HashMap<String, Object>();
            JSONObject map = new JSONObject();

            //??????????????????????????????????????????
            List<OrderReceivableCostVO> data = omsClient.getOrderReceivableCostByMainOrderNo(seaOrderDetails.getMainOrderNo()).getData();
//            JSONArray mainOrders = new JSONArray(JSON.toJSONString(data));
//            System.out.println(mainOrders);
            if (CollectionUtils.isNotEmpty(data)) {
                map.put("unitCodeName", data.get(0).getCustomerName());
                BigDecimal totalMoney = new BigDecimal(0.00);
                BigDecimal changeUSDMoney = new BigDecimal(0.00);
                BigDecimal changeRMBMoney = new BigDecimal(0.00);
                int count = 0;
                Set set = new HashSet();

                for (OrderReceivableCostVO datum : data) {
                    if (datum.getAmount() != null) {
                        totalMoney = totalMoney.add(datum.getAmount());

                    }
                    if (datum.getCurrencyCode().equals("USD")) {
                        datum.setChangeUSDAmount(datum.getAmount());
                    } else {
                        //???????????????????????????????????????
                        BigDecimal rate = omsClient.getExchangeRateByCurrency(datum.getCurrencyCode(), "USD", DateUtil.dateToYearMonth(seaOrderDetails.getCreateTime())).getData();
                        datum.setChangeUSDAmount(datum.getAmount().multiply(rate));
                    }

                    if (datum.getChangeAmount() != null) {
                        changeRMBMoney = changeRMBMoney.add(datum.getChangeAmount());
                    }
                    if (datum.getChangeUSDAmount() != null) {
                        changeUSDMoney = changeUSDMoney.add(datum.getChangeUSDAmount());
//                        System.out.println(datum.getChangeUSDAmount());
                        if (datum.getChangeUSDAmount().equals(new BigDecimal(0).multiply(datum.getChangeUSDAmount()))) {
                            count++;
                        }
                    }
                    set.add(datum.getCurrencyCode());
                }
                StringBuffer stringBuffer = new StringBuffer();
                for (Object o : set) {
                    BigDecimal bigDecimal = new BigDecimal(0.00);
                    for (OrderReceivableCostVO datum : data) {
                        if(o.equals(datum.getCurrencyCode())){
                            bigDecimal = bigDecimal.add(datum.getAmount());
                        }
                    }
                    stringBuffer.append(o).append(" ").append(bigDecimal).append("  ");
                }
                map.put("totalMoney", stringBuffer.toString());
                if (count > 0) {
                    map.put("changeUSDMoney", "USD "+0);
                } else {
                    map.put("changeUSDMoney", "USD "+changeUSDMoney.setScale(4, BigDecimal.ROUND_HALF_UP));
                }
                map.put("changeRMBMoney", "RMB "+changeRMBMoney.setScale(2, BigDecimal.ROUND_HALF_UP));


            }
            map.put("mainOrderNo", seaOrderDetails.getMainOrderNo());

            //??????????????????
            List<SeaBill> seaBills = seaBillService.getSeaBillBySeaOrderId(seaOrderDetails.getOrderId());
            if (CollectionUtils.isNotEmpty(seaBills)) {
                SeaBill seaBill = seaBills.get(0);
                if (seaBill.getSo() != null) {
                    map.put("so", seaBill.getSo());
                }
                if (seaBill.getBillNo() != null) {
                    map.put("billNo", seaBill.getBillNo());
                }

                map.put("mb", "");

                map.put("ship", seaBill.getShipName() == null ? "" : seaBill.getShipName() + "/" + (seaBill.getShipNumber() == null ? "" : seaBill.getShipNumber()));

                map.put("portDepartureCode", seaBill.getPortDepartureCode() == null ? "" : seaBill.getPortDepartureCode());
                map.put("portDestinationCode", seaBill.getPortDestinationCode() == null ? "" : seaBill.getPortDestinationCode());
                map.put("sailingTime", seaBill.getSailingTime() == null ? "" : seaBill.getSailingTime().toString().replaceAll("T", " ").substring(0, 10));
                List<SeaContainerInformationVO> list = seaContainerInformationService.getList(seaBill.getOrderNo());
                if (CollectionUtils.isNotEmpty(list)) {
                    StringBuffer stringBuffer = new StringBuffer();
                    for (SeaContainerInformationVO seaContainerInformationVO : list) {
                        if (seaContainerInformationVO.getCabinetNumber() != null) {
                            stringBuffer.append(seaContainerInformationVO.getCabinetNumber()).append("/");
                        }
                        if (seaContainerInformationVO.getCabinetName() != null) {
                            stringBuffer.append(seaContainerInformationVO.getCabinetName()).append("  ");
                        }
                    }
                    map.put("cabinet", stringBuffer.toString());
                }
            }
            SeaBookship enableBySeaOrderId = seaBookshipService.getEnableBySeaOrderId(seaOrderDetails.getOrderId());
            if (enableBySeaOrderId != null) {
                if (enableBySeaOrderId.getShipCompany() != null) {
                    map.put("shipCompany", enableBySeaOrderId.getShipCompany());
                }
            }
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy???MM???dd???");
            map.put("createTime", df.format(LocalDateTime.now()));

            JSONArray jsonArray = new JSONArray(data);
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOnce("re", jsonArray);
            Map<String, List<Object>> map1 = new HashMap();
            map1.put("orderReceivableCost", jsonObject.get("re", List.class));

//            excelWriter.fill(map, writeSheet);
//            excelWriter.fill(new FillWrapper("orderReceivableCost", data), fillConfig ,writeSheet);
//            excelWriter.finish();
//            outStream.close();
//            inputStream.close();
            EasyExcelUtils.fillTemplate2(map, map1, costPath,null ,fileName, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Value("${address.retAddress}")
    private String retAddress;

    @ApiOperation(value = "?????????????????????????????????")
    @GetMapping(value = "/uploadRetailersExcel")
    public void uploadRetailersExcel( HttpServletResponse response) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("legal_name","??????????????????????????????????????????");
//        queryWrapper.eq("main_order_no","JYD81221070243");
        List<SeaOrder> list = seaOrderService.list(queryWrapper);
        List<String> mainOrderNos = new ArrayList<>();
        for (SeaOrder seaOrder : list) {
            mainOrderNos.add(seaOrder.getMainOrderNo());
        }
        List<SeaOrderProfitVO> seaOrderProfitVOS = omsClient.getOrderCostByMainOrderNos(mainOrderNos).getData();
        for (SeaOrderProfitVO seaOrderProfitVO : seaOrderProfitVOS) {
            SeaOrder byMainOrderNO = seaOrderService.getByMainOrderNO(seaOrderProfitVO.getMainOrderNo());
            SeaOrderVO seaOrderDetails = seaOrderService.getSeaOrderDetails(byMainOrderNO.getId());
            seaOrderProfitVO.setBizName(seaOrderDetails.getBizUname());
            if(seaOrderDetails.getSeaBookshipVO().getClosingTime() != null){
                seaOrderProfitVO.setCreateTime(seaOrderDetails.getSeaBookshipVO().getCreateTime().toString().replaceAll("T"," "));
                seaOrderProfitVO.setEta(seaOrderDetails.getSeaBookshipVO().getEta().toString().replaceAll("T"," "));
                seaOrderProfitVO.setEtd(seaOrderDetails.getSeaBookshipVO().getEtd().toString().replaceAll("T"," "));
            }
            seaOrderProfitVO.setCustomerName(seaOrderDetails.getCustomerName());
            seaOrderProfitVO.setPortDeparture(seaOrderDetails.getPortDeparture());
            seaOrderProfitVO.setPortDestination(seaOrderDetails.getPortDestination());
            seaOrderProfitVO.setCabinetTypeName(seaOrderDetails.getCabinetTypeName());
            if(CollectionUtils.isNotEmpty(seaOrderDetails.getGoodsForms())){
                List<GoodsVO> goodsForms = seaOrderDetails.getGoodsForms();
                GoodsVO goodsVO = goodsForms.get(0);
                seaOrderProfitVO.setBulkCargoAmount((goodsVO.getBulkCargoAmount()==null ? 0:goodsVO.getBulkCargoAmount())+(goodsVO.getBulkCargoUnit()==null?"":goodsVO.getBulkCargoUnit()));
                seaOrderProfitVO.setTotalWeight(goodsVO.getTotalWeight());
                seaOrderProfitVO.setVolume(goodsVO.getVolume());
            }

        }
        try {
//            File file = new File(retAddress);
//            String name = file.getName();
//            InputStream inputStream = new FileInputStream(file);
//
//            Workbook templateWorkbook = null;
//            String fileType = name.substring(name.lastIndexOf("."));
//            if (".xls".equals(fileType)) {
//                templateWorkbook = new HSSFWorkbook(inputStream); // 2003-
//            } else if (".xlsx".equals(fileType)) {
//                templateWorkbook = new XSSFWorkbook(inputStream); // 2007+
//            } else {
//
//            }
//            //HSSFWorkbook templateWorkbook = new HSSFWorkbook(inputStream);
//
//            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//            templateWorkbook.write(outStream);
//            ByteArrayInputStream templateInputStream = new ByteArrayInputStream(outStream.toByteArray());
//
//            String fileName = "?????????????????????????????????";
//
//            response.setContentType("application/vnd.ms-excel");
//            response.setCharacterEncoding("utf-8");
//            // ??????URLEncoder.encode???????????????????????? ?????????easyexcel????????????
//            String filename = URLEncoder.encode(fileName, "utf-8");
//            response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");
//
//            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(templateInputStream).build();
//
//            WriteSheet writeSheet = EasyExcel.writerSheet().build();
//
//            FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();

//            FillConfig fillConfig = FillConfig.builder().direction(WriteDirectionEnum.VERTICAL).build();
//            ?????????????????????
            Map<String, Object> map = new HashMap<String, Object>();
            JSONArray jsonArray = new JSONArray(seaOrderProfitVOS);
            JSONObject jsonObject = new JSONObject();
            jsonObject.putOnce("re", jsonArray);
            Map<String, List<Object>> map1 = new HashMap();
            map1.put("seaOrderProfitVO", jsonObject.get("re", List.class));
//
            EasyExcelUtils.fillTemplate2(new JSONObject(), map1, retAddress,null ,"?????????????????????????????????", response);
//            excelWriter.fill(new FillWrapper("orderReceivableCost", seaOrderProfitVOS), fillConfig ,writeSheet);
//            excelWriter.finish();
//            outStream.close();
//            inputStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}

