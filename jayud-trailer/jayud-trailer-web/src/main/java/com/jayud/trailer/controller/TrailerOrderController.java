package com.jayud.trailer.controller;


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
import com.jayud.common.utils.Query;
import com.jayud.common.utils.StringUtils;
import com.jayud.trailer.bo.*;
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
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.jayud.common.enums.OrderStatusEnum.TT_2;
import static com.jayud.common.enums.OrderStatusEnum.TT_3;


/**
 * <p>
 * ??????????????? ???????????????
 * </p>
 *
 * @author LLJ
 * @since 2021-03-01
 */
@Api(tags = "????????????")
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

    @ApiOperation("??????????????????????????????")
    @PostMapping("/findByPage")
    public CommonResult findByPage(@RequestBody QueryTrailerOrderForm form) {

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
        //??????????????????
        if (form.getTakeTimeStr() != null && form.getTakeTimeStr().length > 0) {
            Set<String> orderNos = omsClient.getOrderNosByTakeTime(form.getTakeTimeStr(), BusinessTypeEnum.TC.getCode()).getData();
            if (org.apache.commons.collections4.CollectionUtils.isEmpty(orderNos)) {
                orderNos.add("-1");
            }
            form.setSubOrderNos(orderNos);
        }

        List list = new ArrayList();
        //??????????????????
//        Field[] declaredFields = new Field[100];

        if (form.getImpAndExpType().equals(0)) {
            Class<TrailerOrderFormVO> seaOrderFormVOClass = TrailerOrderFormVO.class;
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
        }
        if (form.getImpAndExpType().equals(1)) {
            Class<TrailerOrderImportVO> seaOrderFormVOClass = TrailerOrderImportVO.class;
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
        }
        if (form.getImpAndExpType().equals(2)) {
            Class<TrailerOrderExportVO> seaOrderFormVOClass = TrailerOrderExportVO.class;
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
        }

        Map map1 = new HashMap();
        map1.put("header", list);
        IPage<TrailerOrderFormVO> page = this.trailerOrderService.findByPage(form);
        if (page.getRecords().size() == 0) {
            map1.put("pageInfo", new CommonPageResult(page));
            return CommonResult.success(map1);
        }
        //??????????????????
        String prePath = String.valueOf(fileClient.getBaseUrl().getData());

        List<TrailerOrderFormVO> records = page.getRecords();
        List<Long> trailerOrderIds = new ArrayList<>();
        List<String> mainOrder = new ArrayList<>();
        List<Long> entityIds = new ArrayList<>();
//        List<Long> supplierIds = new ArrayList<>();
        List<String> unitCodes = new ArrayList<>();
        List<String> subOrderNos = new ArrayList<>();
        for (TrailerOrderFormVO record : records) {
            trailerOrderIds.add(record.getOrderId());
            mainOrder.add(record.getMainOrderNo());
            entityIds.add(record.getLegalEntityId());
            unitCodes.add(record.getUnitCode());
            subOrderNos.add(record.getOrderNo());
//            if(record.getTrailerDispatchVO().getSupplierId()!=null){
//                supplierIds.add(record.getTrailerDispatchVO().getSupplierId());
//            }
        }

        //??????????????????
        Map<String, Object> data1 = this.omsClient.isCost(subOrderNos, SubOrderSignEnum.TC.getSignOne()).getData();
        Map<String, Object> costStatus = omsClient.getCostStatus(null, subOrderNos).getData();

        //??????????????????
        ApiResult legalEntityResult = null;
        if (CollectionUtils.isNotEmpty(entityIds)) {
            legalEntityResult = this.oauthClient.getLegalEntityByLegalIds(entityIds);
        }
        //?????????????????????
//        JSONArray supplierInfo = null;
//        if (CollectionUtils.isNotEmpty(supplierIds) && supplierIds.size()>0) {
//            supplierInfo = new JSONArray(this.omsClient.getSupplierInfoByIds(supplierIds).getData());
//        }

        //????????????????????????
        ApiResult unitCodeInfo = null;
        if (CollectionUtils.isNotEmpty(unitCodes)) {
            unitCodeInfo = this.omsClient.getCustomerByUnitCode(unitCodes);
        }

        //?????????????????????
        ApiResult<List<OrderAddressVO>> resultOne = this.omsClient.getOrderAddressByBusIds(trailerOrderIds, BusinessTypeEnum.TC.getCode());
        if (resultOne.getCode() != HttpStatus.SC_OK) {
            log.warn("?????????????????????????????? trailerOrderId={}", trailerOrderIds);
        }

        //??????????????????
        List<InitComboxStrVO> portCodeInfo = (List<InitComboxStrVO>) this.omsClient.initDictByDictTypeCode("Port").getData();

        //??????????????????
        ApiResult cabinetSizeInfo = this.omsClient.getVehicleSizeInfo();
        //???????????????code
        Map<String, String> supplierCodes = this.omsClient.getSupplierCodesByName().getData();

        //?????????????????????
        ApiResult result = omsClient.getMainOrderByOrderNos(mainOrder);
        for (TrailerOrderFormVO record : records) {
            //??????????????????
//            List<GoodsVO> goodsVOS = new ArrayList<>();
//            for (GoodsVO goodsVO : goods) {
//                if (record.getId().equals(goodsVO.getBusinessId())
//                        && BusinessTypeEnum.TC.getCode().equals(goodsVO.getBusinessType())) {
//                    goodsVOS.add(goodsVO);
//                }
//            }
//            record.setGoodsForms(goodsVOS);
            record.setCost(MapUtil.getBool(data1, record.getOrderNo()));
            record.assemblyCostStatus(costStatus);

            //?????????????????????
            record.assemblyMainOrderData(result.getData());
            //??????????????????
            record.assemblyLegalEntity(legalEntityResult);
            record.setDefaultSupplierCode(supplierCodes.get(record.getSupplierName()));
            //???????????????
//            record.assemblySupplierInfo(supplierInfo);
            //??????????????????
            record.getFile(prePath);

            //??????????????????
            record.assemblyUnitCodeInfo(unitCodeInfo);

            //??????????????????
            record.assemblyCabinetSize(cabinetSizeInfo);

            record.setSubUnitCode(record.getUnitCode());

            //??????????????????
            if (resultOne.getData() != null && resultOne.getData().size() > 0) {
                List<TrailerOrderAddressVO> trailerOrderAddressVOS = new ArrayList<>();
                List<GoodsVO> goodsVOS = new ArrayList<>();
                for (OrderAddressVO address : resultOne.getData()) {
                    address.getFile(prePath);
                    if (address.getOrderNo().equals(record.getOrderNo())) {
                        TrailerOrderAddressVO convert = ConvertUtil.convert(address, TrailerOrderAddressVO.class);
                        ApiResult goodResult = omsClient.getGoodById(address.getBindGoodsId());
                        JSONObject goodById = new JSONObject(goodResult.getData());
                        GoodsVO convert1 = ConvertUtil.convert(goodById, GoodsVO.class);
                        goodsVOS.add(convert1);
                        convert.setName(convert1.getName());
                        convert.setBulkCargoAmount(convert1.getBulkCargoAmount());
                        convert.setBulkCargoUnit(convert1.getBulkCargoUnit());
                        convert.setSize(convert1.getSize());
                        convert.setTotalWeight(convert1.getTotalWeight());
                        convert.setVolume(convert1.getVolume());
                        trailerOrderAddressVOS.add(convert);
                    }

                }
                record.assemblyGoodsInfo(goodsVOS);
                record.setOrderAddressForms(trailerOrderAddressVOS);
                if (CollectionUtils.isNotEmpty(record.getOrderAddressForms())) {
                    record.assemblyDateStr();
                    record.setDeliveryDate(record.getOrderAddressForms().get(0).getDeliveryDate());
                }
            }

            //??????????????????
            for (InitComboxStrVO initComboxStrVO : portCodeInfo) {
                if (initComboxStrVO.getCode().equals(record.getPortCode())) {
                    record.setPortCodeName(initComboxStrVO.getName());
                }
            }

            //??????????????????
            TrailerDispatch enableByTrailerOrderId = trailerDispatchService.getEnableByTrailerOrderId(record.getOrderNo());
            TrailerDispatchVO trailerDispatchVO = ConvertUtil.convert(enableByTrailerOrderId, TrailerDispatchVO.class);
//            System.out.println("trailerDispatchVO=================================="+trailerDispatchVO);
            record.setTrailerDispatchVO(trailerDispatchVO);
            if (trailerDispatchVO.getPlateNumber() != null) {
                VehicleInfoLinkVO data = omsClient.initVehicleInfo(trailerDispatchVO.getPlateNumber()).getData();
                trailerDispatchVO.setPlateNumberName(data.getPlateNumber());
                for (DriverInfoVO driverInfo : data.getDriverInfos()) {
                    if (trailerDispatchVO.getName().equals(driverInfo.getId())) {
                        trailerDispatchVO.setDriverName(driverInfo.getName());
                    }
                }
                record.setPlateNumber(trailerDispatchVO.getPlateNumberName());
            }
            if (record.getImpAndExpType().equals(2) && record.getStatus().equals(OrderStatusEnum.TT_4.getCode())) {
                if (record.getProcessDescription() != null) {
                    record.setStatus(record.getProcessDescription());
                }
            }
            if (record.getImpAndExpType().equals(1) && record.getStatus().equals(OrderStatusEnum.TT_7.getCode())) {
                if (record.getProcessDescription() != null) {
                    record.setStatus(record.getProcessDescription());
                }
            }
            if (record.getImpAndExpType().equals(2) && record.getStatus().equals(OrderStatusEnum.TT_7.getCode()) && record.getIsWeighed()) {
                if (record.getProcessDescription() != null) {
                    record.setStatus(record.getProcessDescription());
                }
            }
        }

        map1.put("pageInfo", new CommonPageResult(page));
        return CommonResult.success(map1);
    }

    //????????????,cmd = ??????(TT_0?????????,TT_1????????????,TT_2????????????,TT_3????????????,TT_4????????????,TT_5????????????,TT_6????????????,TT_7????????????,TT_8????????????)
    @ApiOperation(value = "????????????????????????")
    @PostMapping(value = "/doTrailerProcessOpt")
    public CommonResult doTrailerProcessOpt(@RequestBody @Valid TrailerProcessOptForm form, BindingResult result) {

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
        TrailerOrder trailerOrder = this.trailerOrderService.getById(form.getOrderId());
        if (ProcessStatusEnum.COMPLETE.getCode().equals(trailerOrder.getProcessStatus())) {
            return CommonResult.error(400, "?????????????????????");
        }
        if (!ProcessStatusEnum.PROCESSING.getCode().equals(trailerOrder.getProcessStatus())) {
            return CommonResult.error(400, "????????????????????????");
        }
//        if(!trailerOrder.getStatus().equals(form.getStatus())){
//            return CommonResult.error(400, "????????????????????????");
//        }
//
        String orderProcessNode = (String) omsClient.getOrderProcessNode(trailerOrder.getMainOrderNo(), trailerOrder.getOrderNo(), trailerOrder.getStatus()).getData();

        OrderStatusEnum statusEnum = OrderStatusEnum.getTrailerOrderNextStatus(orderProcessNode);
        if (statusEnum == null) {
            log.error("??????????????????????????????,?????????????????? data={}", trailerOrder);
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }

        //????????????
        form.checkProcessOpt(statusEnum);
        form.setStatus(statusEnum.getCode());

        //????????????
        switch (statusEnum) {
            case TT_1: //????????????
                TrailerOrder trailerOrder1 = new TrailerOrder();
                trailerOrder1.setOrderTaker(form.getOperatorUser());
                trailerOrder1.setReceivingOrdersDate(DateUtils.str2LocalDateTime(form.getOperatorTime(), DateUtils.DATE_TIME_PATTERN));
                this.trailerOrderService.updateProcessStatus(trailerOrder1, form);
                break;
            case TT_2: //??????
            case TT_3: //????????????
                this.trailerOrderService.doTrailerDispatchOpt(form);
                break;
            case TT_4: //????????????
            case TT_5: //????????????
            case TT_6: //????????????
            case TT_8: //????????????
                this.trailerOrderService.updateProcessStatus(new TrailerOrder(), form);
                break;
            case TT_7: //????????????
                this.trailerOrderService.doTrailerDispatchOpt(form);
                break;
        }

        return CommonResult.success();
    }

    @ApiOperation(value = "??????????????????")
    @PostMapping(value = "/getDispatchNO")
    public CommonResult<TrailerDispatch> getDispatchNO(@RequestBody Map<String, Object> map) {
        String trailerOrderNo = MapUtil.getStr(map, "orderNo");
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("trailer_order_no", trailerOrderNo);
        queryWrapper.eq("status", 1);
        TrailerDispatch one = trailerDispatchService.getOne(queryWrapper);
        if (one != null) {
            return CommonResult.success(one);
        } else {
            one = new TrailerDispatch();
        }
        String substring = trailerOrderNo.substring(0, trailerOrderNo.length() - 8);
        String preOrderNo = OrderTypeEnum.P.getCode() + substring;
        String classCode = OrderTypeEnum.P.getCode();
        String orderNo = (String) omsClient.getOrderNo(preOrderNo, classCode).getData();
        one.setOrderNo(orderNo);
        one.setTrailerOrderNo(trailerOrderNo);
        one.setStatus(1);
        trailerDispatchService.saveOrUpdateTrailerDispatch(one);
        return CommonResult.success(one);
    }

    @ApiOperation(value = "?????????????????? trailerOrderId=????????????id")
    @PostMapping(value = "/getTrailerOrderDetails")
    public CommonResult<TrailerOrderVO> getTrailerOrderDetails(@RequestBody Map<String, Object> map) {
        Long trailerOrderId = MapUtil.getLong(map, "id");
        if (trailerOrderId == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        TrailerOrderVO trailerOrderDetails = this.trailerOrderService.getTrailerOrderByOrderNO(trailerOrderId);

        return CommonResult.success(trailerOrderDetails);
    }


    @ApiOperation(value = "?????????????????? id=????????????id")
    @PostMapping(value = "/DispatchRejectionEdit")
    public CommonResult DispatchRejectionEdit(@RequestBody TrailerProcessOptForm form) {
        if (form.getMainOrderId() == null
                || form.getOrderId() == null
                || form.getTrailerDispatchVO().getId() == null) {
            log.warn("??????????????????/????????????id??????/????????????id?????? data={}", JSONUtil.toJsonStr(form));
            return CommonResult.error(ResultEnum.VALIDATE_FAILED);
        }

        TrailerOrder trailerOrder = this.trailerOrderService.getById(form.getOrderId());
        if (!OrderStatusEnum.TT_3_2.getCode().equals(trailerOrder.getStatus())) {
            log.warn("???????????????????????????????????? status={}", OrderStatusEnum.getDesc(trailerOrder.getStatus()));
            return CommonResult.error(400, "????????????????????????????????????");
        }
        form.setStatus(TT_2.getCode());
        //????????????
        form.checkProcessOpt(TT_2);
        //????????????????????????
        this.trailerOrderService.doTrailerDispatchOpt(form);
        return CommonResult.success();
    }


    @ApiOperation(value = "??????????????????")
    @PostMapping(value = "/rejectOrder")
    public CommonResult rejectOrder(@RequestBody TrailerCargoRejected trailerCargoRejected) {
        //??????????????????
        TrailerOrder tmp = this.trailerOrderService.getById(trailerCargoRejected.getTrailerOrderId());
        //????????????????????????
        OrderStatusEnum orderStatusEnum = OrderStatusEnum.getTrailerOrderRejection(tmp.getStatus());
        if (orderStatusEnum == null) {
            return CommonResult.error(400, "??????????????????,????????????????????????");
        }
        //??????????????????
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setExtId(trailerCargoRejected.getTrailerOrderId());
        auditInfoForm.setExtDesc(SqlConstant.TRAILER_ORDER);
        auditInfoForm.setAuditStatus(orderStatusEnum.getCode());
        auditInfoForm.setAuditTypeDesc(orderStatusEnum.getDesc());

        auditInfoForm.setAuditComment(trailerCargoRejected.getCause());

        Integer rejectOptions = trailerCargoRejected.getRejectOptions() == null ? 1 : trailerCargoRejected.getRejectOptions();
        trailerCargoRejected.setRejectOptions(rejectOptions);
        if (trailerCargoRejected.getRejectOptions().equals(2)) {
            if (trailerCargoRejected.getCause() == null) {
                return CommonResult.error(400, "???????????????????????????????????????");
            }
        }
        switch (orderStatusEnum) {
            case TT_1_1:
                //????????????
                this.trailerOrderService.orderReceiving(tmp, auditInfoForm, trailerCargoRejected);
                break;
            case TT_2_1:
            case TT_3_1:
            case TT_4_1:
                this.trailerOrderService.rejectedOpt(tmp, auditInfoForm, trailerCargoRejected);
                break;
        }

        return CommonResult.success();
    }

    /**
     * ???????????????
     */
    @Value("${address.trailerAddr}")
    private String filePath;

    @ApiOperation(value = "???????????????")
    @GetMapping(value = "/uploadExcel")
    public void uploadExcel(@RequestParam("orderId") Long orderId, HttpServletResponse response) {

        TrailerOrderVO trailerOrderDetails = trailerOrderService.getTrailerOrderByOrderNO(orderId);

//        ClassPathResource classPathResource = new ClassPathResource("/static/?????????.xls");
//        String filename1 = classPathResource.getFilename();

//        File file = new File("D:\\CodeRepository1\\jayud-platform\\jayud-trailer\\jayud-trailer-web\\src\\main\\resources\\static\\trailer.xls");
        File file = new File(filePath);
        String name = file.getName();

        try {
            InputStream inputStream = new FileInputStream(file);
            Workbook templateWorkbook = null;
            String fileType = name.substring(name.lastIndexOf("."));
            if (".xls".equals(fileType)) {
                templateWorkbook = new HSSFWorkbook(inputStream); // 2003-
            } else if (".xlsx".equals(fileType)) {
                templateWorkbook = new XSSFWorkbook(inputStream); // 2007+
            } else {

            }
            //HSSFWorkbook templateWorkbook = new HSSFWorkbook(inputStream);

            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            templateWorkbook.write(outStream);
            ByteArrayInputStream templateInputStream = new ByteArrayInputStream(outStream.toByteArray());

            String fileName = "?????????";

            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // ??????URLEncoder.encode???????????????????????? ?????????easyexcel????????????
            String filename = URLEncoder.encode(fileName, "utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");

            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(templateInputStream).build();

            WriteSheet writeSheet = EasyExcel.writerSheet().build();

//            FillConfig fillConfig = FillConfig.builder().direction(WriteDirectionEnum.HORIZONTAL).build();
            //?????????????????????
            excelWriter.fill(new FillWrapper("orderAddress", trailerOrderDetails.getOrderAddressForms()), writeSheet);
//            excelWriter.fill(new FillWrapper("good",trailerOrderDetails.getGoodsForms()),fillConfig,writeSheet);

            //?????????????????????
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("orderNo", trailerOrderDetails.getTrailerDispatchVO().getOrderNo());
            map.put("cabinetNumber", trailerOrderDetails.getCabinetNumber());
            map.put("paperStripSeal", trailerOrderDetails.getPaperStripSeal());
            map.put("plateNumber", trailerOrderDetails.getTrailerDispatchVO().getPlateNumberName());
            map.put("phone", trailerOrderDetails.getTrailerDispatchVO().getPhone());
            map.put("cabinetSizeName", trailerOrderDetails.getCabinetSizeName());
            map.put("cuttingReplenishingTime", trailerOrderDetails.getCuttingReplenishingTime());
            map.put("portCodeName", trailerOrderDetails.getPortCodeName());
            map.put("closingTime", trailerOrderDetails.getClosingTime());
            map.put("remark", trailerOrderDetails.getTrailerDispatchVO().getRemark());
            map.put("createTime", trailerOrderDetails.getCreateTime().toString().substring(0, 10));
            map.put("so", trailerOrderDetails.getSo());
            map.put("timeCounterRent", trailerOrderDetails.getTimeCounterRent());
            map.put("totalWeightName", trailerOrderDetails.getTotalWeightName());
            map.put("totalAmountName", trailerOrderDetails.getTotalAmountName());
            map.put("totalXAmountName", trailerOrderDetails.getTotalXAmountName());
            excelWriter.fill(map, writeSheet);

            excelWriter.finish();
            outStream.close();
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @PostMapping(value = "/createOrUpdateOrder")
    @ApiOperation(value = "???????????????")
    public CommonResult<String> createOrUpdateOrder(@RequestBody AddTrailerOrderFrom addTrailerOrderFrom) {
        if (addTrailerOrderFrom.getIsInfoComplete()) {
            addTrailerOrderFrom.checkPickUpInfo();
        }
        String order = trailerOrderService.createOrder(addTrailerOrderFrom);
        return CommonResult.success(order);
    }


    @PostMapping(value = "/getOrderDetails")
    @ApiOperation(value = "?????????????????????????????????????????? ")
    public CommonResult<TrailerOrderVO> getOrderDetails(@RequestBody Map<String, Object> map) {
        String orderNo = MapUtil.getStr(map, "orderNo");
        if (StringUtils.isEmpty(orderNo)) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        TrailerOrder trailerOrder = trailerOrderService.getByOrderNO(orderNo);
        TrailerOrderVO trailerOrderVO = trailerOrderService.getTrailerOrderByOrderNO(trailerOrder.getId());
        return CommonResult.success(trailerOrderVO);
    }


    @Value("${address.driverAddress}")
    private String driverPath;

    @ApiOperation(value = "????????????-????????????")
    @GetMapping(value = "/uploadTrailerExcel")
    public void uploadTrailerExcel(@RequestParam("orderId") Long orderId, HttpServletResponse response) {
        TrailerOrderVO trailerOrderByOrderNO = trailerOrderService.getTrailerOrderByOrderNO(orderId);
        TrailerDispatchVO trailerDispatchVO = trailerOrderByOrderNO.getTrailerDispatchVO();

        File file = new File(driverPath);
        String name = file.getName();

        try {
            InputStream inputStream = new FileInputStream(file);
//            Workbook templateWorkbook = WorkbookFactory.create(inputStream);
//            Workbook templateWorkbook = new XSSFWorkbook(inputStream);

            Workbook templateWorkbook = null;
            String fileType = name.substring(name.lastIndexOf("."));
            if (".xls".equals(fileType)) {
                templateWorkbook = new HSSFWorkbook(inputStream); // 2003-
            } else if (".xlsx".equals(fileType)) {
                templateWorkbook = new XSSFWorkbook(inputStream); // 2007+
            } else {

            }
            //HSSFWorkbook templateWorkbook = new HSSFWorkbook(inputStream);

            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            templateWorkbook.write(outStream);
            ByteArrayInputStream templateInputStream = new ByteArrayInputStream(outStream.toByteArray());

            String fileName = "??????????????????";

            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // ??????URLEncoder.encode???????????????????????? ?????????easyexcel????????????
            String filename = URLEncoder.encode(fileName, "utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");

            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(templateInputStream).build();

            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            //?????????????????????
            Map<String, Object> map = new HashMap<String, Object>();

            ApiResult mainOrderByOrderNos = omsClient.getMainOrderByOrderNos(Collections.singletonList(trailerOrderByOrderNO.getMainOrderNo()));
            JSONArray mainOrders = new JSONArray(JSON.toJSONString(mainOrderByOrderNos.getData()));
            JSONObject json = mainOrders.getJSONObject(0);
            String customerName = json.getStr("customerName");
            map.put("customerName", customerName);
            if (trailerOrderByOrderNO.getSo() != null) {
                map.put("so", trailerOrderByOrderNO.getSo());
            }
            if (trailerOrderByOrderNO.getCabinetSizeName() != null) {
                map.put("cabinetSizeName", trailerOrderByOrderNO.getCabinetSizeName());
            }
            if (trailerOrderByOrderNO.getCabinetNumber() != null) {
                map.put("cabinetNumber", trailerOrderByOrderNO.getCabinetNumber());
            }
            if (trailerOrderByOrderNO.getPaperStripSeal() != null) {
                map.put("paperStripSeal", trailerOrderByOrderNO.getPaperStripSeal());
            }
            if (trailerDispatchVO.getPlateNumber() != null) {
                VehicleInfoLinkVO data = omsClient.initVehicleInfo(trailerDispatchVO.getPlateNumber()).getData();
                trailerDispatchVO.setPlateNumberName(data.getPlateNumber());
                for (DriverInfoVO driverInfo : data.getDriverInfos()) {
                    if (trailerDispatchVO.getName().equals(driverInfo.getId())) {
                        map.put("driverName", driverInfo.getName());
                        map.put("phone", driverInfo.getPhone());
                        map.put("idNo", driverInfo.getIdNo());
                    }
                }
                map.put("plateNumberName", trailerDispatchVO.getPlateNumberName());
            }
            if (trailerDispatchVO.getCabinetWeight() != null) {
                map.put("cabinetWeight", trailerDispatchVO.getCabinetWeight());
            }
            if (trailerDispatchVO.getCabinetWeight() != null) {
                map.put("weighing", trailerDispatchVO.getWeighing());
            }
            List<TrailerOrderAddressVO> orderAddressForms = trailerOrderByOrderNO.getOrderAddressForms();
            if (CollectionUtils.isNotEmpty(orderAddressForms)) {
                StringBuffer stringBuffer = new StringBuffer();
                for (TrailerOrderAddressVO orderAddressForm : orderAddressForms) {
                    if (orderAddressForm.getAddress() != null) {
                        stringBuffer.append(orderAddressForm.getAddress()).append("/");
                    }
                }
                map.put("address", stringBuffer.toString().substring(0, stringBuffer.length() - 1));
            }
            if (trailerDispatchVO.getCreateTime() != null) {
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy???MM???dd???");
                map.put("createTime", df.format(trailerDispatchVO.getCreateTime()).substring(5, 11));
            }
            map.put("legalName", trailerOrderByOrderNO.getLegalName());

            excelWriter.fill(map, writeSheet);

            excelWriter.finish();
            outStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

//    public static void main(String[] args) {
//        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy???MM???dd??? " );
//        String str = sdf.format(new Date());
//        System.out.println(str);
//        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy???MM???dd???");
//        System.out.println(df.format(LocalDateTime.now()).substring(5,11));
//    }
}

