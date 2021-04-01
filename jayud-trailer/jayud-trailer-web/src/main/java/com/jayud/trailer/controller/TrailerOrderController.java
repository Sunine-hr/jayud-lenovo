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
import java.util.*;

import static com.jayud.common.enums.OrderStatusEnum.TT_2;
import static com.jayud.common.enums.OrderStatusEnum.TT_3;


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
//        List<Long> supplierIds = new ArrayList<>();
        List<String> unitCodes = new ArrayList<>();
        for (TrailerOrderFormVO record : records) {
            trailerOrderIds.add(record.getOrderId());
            mainOrder.add(record.getMainOrderNo());
            entityIds.add(record.getLegalEntityId());
            unitCodes.add(record.getUnitCode());
//            if(record.getTrailerDispatchVO().getSupplierId()!=null){
//                supplierIds.add(record.getTrailerDispatchVO().getSupplierId());
//            }
        }

        //查询法人主体
        ApiResult legalEntityResult = null;
        if (CollectionUtils.isNotEmpty(entityIds)) {
            legalEntityResult = this.oauthClient.getLegalEntityByLegalIds(entityIds);
        }
        //查询供应商信息
//        JSONArray supplierInfo = null;
//        if (CollectionUtils.isNotEmpty(supplierIds) && supplierIds.size()>0) {
//            supplierInfo = new JSONArray(this.omsClient.getSupplierInfoByIds(supplierIds).getData());
//        }

        //获取结算单位信息
        ApiResult unitCodeInfo = null;
        if (CollectionUtils.isNotEmpty(unitCodes)) {
             unitCodeInfo = this.omsClient.getCustomerByUnitCode(unitCodes);
        }

        //获取发货人信息
        ApiResult<List<OrderAddressVO>> resultOne = this.omsClient.getOrderAddressByBusIds(trailerOrderIds, BusinessTypeEnum.TC.getCode() );
        if (resultOne.getCode() != HttpStatus.SC_OK) {
            log.warn("查询订单地址信息失败 trailerOrderId={}", trailerOrderIds);
        }

        //获取港口信息
        List<InitComboxStrVO> portCodeInfo = (List<InitComboxStrVO>)this.omsClient.initDictByDictTypeCode("Port").getData();

        //获取车型信息
        ApiResult cabinetSizeInfo = this.omsClient.getVehicleSizeInfo();

        //查询主订单信息
        ApiResult result = omsClient.getMainOrderByOrderNos(mainOrder);
        for (TrailerOrderFormVO record : records) {
            //组装商品信息
//            List<GoodsVO> goodsVOS = new ArrayList<>();
//            for (GoodsVO goodsVO : goods) {
//                if (record.getId().equals(goodsVO.getBusinessId())
//                        && BusinessTypeEnum.TC.getCode().equals(goodsVO.getBusinessType())) {
//                    goodsVOS.add(goodsVO);
//                }
//            }
//            record.setGoodsForms(goodsVOS);

            //拼装主订单信息
            record.assemblyMainOrderData(result.getData());
            //组装法人名称
            record.assemblyLegalEntity(legalEntityResult);
            //拼装供应商
            //record.assemblySupplierInfo(supplierInfo);
            //拼接附件信息
            record.getFile(prePath);

            //拼装结算单位
            record.assemblyUnitCodeInfo(unitCodeInfo);

            //获取车型大小
            record.assemblyCabinetSize(cabinetSizeInfo);

            //处理地址信息
            if(resultOne.getData()!=null && resultOne.getData().size()>0){
                List<TrailerOrderAddressVO> trailerOrderAddressVOS = new ArrayList<>();
                List<GoodsVO> goodsVOS = new ArrayList<>();
                for (OrderAddressVO address : resultOne.getData()) {
                    address.getFile(prePath);
                    if(address.getOrderNo().equals(record.getOrderNo())){
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
            }

            //获取港口信息
            for (InitComboxStrVO initComboxStrVO : portCodeInfo) {
                if(initComboxStrVO.getCode().equals(record.getPortCode())){
                    record.setPortCodeName(initComboxStrVO.getName());
                }
            }

            //获取派车信息
            TrailerDispatch enableByTrailerOrderId = trailerDispatchService.getEnableByTrailerOrderId(record.getOrderNo());
            TrailerDispatchVO trailerDispatchVO = ConvertUtil.convert(enableByTrailerOrderId,TrailerDispatchVO.class);
//            System.out.println("trailerDispatchVO=================================="+trailerDispatchVO);
            record.setTrailerDispatchVO(trailerDispatchVO);
            if(trailerDispatchVO.getPlateNumber()!=null){
                VehicleInfoLinkVO data = omsClient.initVehicleInfo(trailerDispatchVO.getPlateNumber()).getData();
                trailerDispatchVO.setPlateNumberName(data.getPlateNumber());
                for (DriverInfoVO driverInfo : data.getDriverInfos()) {
                    if(trailerDispatchVO.getName().equals(driverInfo.getId())){
                        trailerDispatchVO.setDriverName(driverInfo.getName());
                    }
                }
                record.setPlateNumber(trailerDispatchVO.getPlateNumberName());
            }
            if(record.getImpAndExpType().equals(2)&&record.getStatus().equals(OrderStatusEnum.TT_4.getCode())){
                if(record.getProcessDescription()!=null){
                    record.setStatus(record.getProcessDescription());
                }
            }
            if(record.getImpAndExpType().equals(1)&&record.getStatus().equals(OrderStatusEnum.TT_7.getCode())){
                if(record.getProcessDescription()!=null){
                    record.setStatus(record.getProcessDescription());
                }
            }
            if(record.getImpAndExpType().equals(2)&&record.getStatus().equals(OrderStatusEnum.TT_7.getCode())&&record.getIsWeighed()){
                if(record.getProcessDescription()!=null){
                    record.setStatus(record.getProcessDescription());
                }
            }
        }

        map1.put("pageInfo",new CommonPageResult(page));
        return CommonResult.success(map1);
    }

    //操作指令,cmd = 状态(TT_0待接单,TT_1拖车接单,TT_2拖车派车,TT_3派车审核,TT_4拖车提柜,TT_5拖车到仓,TT_6拖车离仓,TT_7拖车过磅,TT_8确认还柜)
    @ApiOperation(value = "执行拖车流程操作")
    @PostMapping(value = "/doTrailerProcessOpt")
    public CommonResult doTrailerProcessOpt(@RequestBody @Valid TrailerProcessOptForm form , BindingResult result) {

        if(result.hasErrors()){
            for (ObjectError error : result.getAllErrors()) {
                return CommonResult.error(444,error.getDefaultMessage());
            }
        }

        if (form.getMainOrderId() == null || form.getOrderId() == null) {
            log.warn("主订单id/拖车订单id必填");
            return CommonResult.error(ResultEnum.VALIDATE_FAILED);
        }
        //拖车订单信息
        TrailerOrder trailerOrder = this.trailerOrderService.getById(form.getOrderId());
        if (ProcessStatusEnum.COMPLETE.getCode().equals(trailerOrder.getProcessStatus())) {
            return CommonResult.error(400, "该订单已经完成");
        }
        if (!ProcessStatusEnum.PROCESSING.getCode().equals(trailerOrder.getProcessStatus())) {
            return CommonResult.error(400, "当前订单无法操作");
        }
//        if(!trailerOrder.getStatus().equals(form.getStatus())){
//            return CommonResult.error(400, "当前订单正在操作");
//        }
//
        String orderProcessNode = (String)omsClient.getOrderProcessNode(trailerOrder.getMainOrderNo(),trailerOrder.getOrderNo(),trailerOrder.getStatus()).getData();

        OrderStatusEnum statusEnum = OrderStatusEnum.getTrailerOrderNextStatus(orderProcessNode);
        if (statusEnum == null) {
            log.error("执行拖车流程操作失败,超出流程之外 data={}", trailerOrder);
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
            case TT_2: //派车
            case TT_3: //派车审核
                this.trailerOrderService.doTrailerDispatchOpt(form);
                break;
            case TT_4: //拖车提柜
            case TT_5: //拖车到仓
            case TT_6: //拖车离仓
            case TT_8: //确认还柜
                this.trailerOrderService.updateProcessStatus(new TrailerOrder(), form);
                break;
            case TT_7: //拖车过磅
                this.trailerOrderService.doTrailerDispatchOpt(form);
                break;
        }

        return CommonResult.success();
    }

    @ApiOperation(value = "获取派车单号")
    @PostMapping(value = "/getDispatchNO")
    public CommonResult<TrailerDispatch> getDispatchNO(@RequestBody Map<String, Object> map){
        String trailerOrderNo = MapUtil.getStr(map, "orderNo");
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("trailer_order_no",trailerOrderNo);
        TrailerDispatch one = trailerDispatchService.getOne(queryWrapper);
        if(one!=null){
            return CommonResult.success(one);
        }else{
            one = new TrailerDispatch();
        }
        String substring = trailerOrderNo.substring(0, trailerOrderNo.length() - 8);
        String preOrderNo = OrderTypeEnum.P.getCode()+substring;
        String classCode = OrderTypeEnum.P.getCode();
        String orderNo = (String)omsClient.getOrderNo(preOrderNo,classCode).getData();
        one.setOrderNo(orderNo);
        one.setTrailerOrderNo(trailerOrderNo);
        one.setStatus(1);
        trailerDispatchService.saveOrUpdateTrailerDispatch(one);
        return CommonResult.success(one);
    }

    @ApiOperation(value = "查询订单详情 trailerOrderId=拖车订单id")
    @PostMapping(value = "/getTrailerOrderDetails")
    public CommonResult<TrailerOrderVO> getTrailerOrderDetails(@RequestBody Map<String, Object> map) {
        Long trailerOrderId = MapUtil.getLong(map, "id");
        if (trailerOrderId == null) {
            return CommonResult.error(ResultEnum.PARAM_ERROR);
        }
        TrailerOrderVO trailerOrderDetails = this.trailerOrderService.getTrailerOrderByOrderNO(trailerOrderId);

        return CommonResult.success(trailerOrderDetails);
    }


    @ApiOperation(value = "拖车驳回编辑 id=拖车订单id")
    @PostMapping(value = "/DispatchRejectionEdit")
    public CommonResult DispatchRejectionEdit(@RequestBody TrailerProcessOptForm form) {
        if (form.getMainOrderId() == null
                || form.getOrderId() == null
                || form.getTrailerDispatchVO().getId() == null) {
            log.warn("拖车订单编号/拖车订单id必填/拖车派车id必填 data={}", JSONUtil.toJsonStr(form));
            return CommonResult.error(ResultEnum.VALIDATE_FAILED);
        }

        TrailerOrder trailerOrder = this.trailerOrderService.getById(form.getOrderId());
        if (!OrderStatusEnum.TT_3_2.getCode().equals(trailerOrder.getStatus())) {
            log.warn("当前订单状态无法进行操作 status={}", OrderStatusEnum.getDesc(trailerOrder.getStatus()));
            return CommonResult.error(400, "当前订单状态无法进行操作");
        }
        form.setStatus(TT_2.getCode());
        //校验参数
        form.checkProcessOpt(TT_2);
        //执行拖车驳回编辑
        this.trailerOrderService.doTrailerDispatchOpt(form);
        return CommonResult.success();
    }



    @ApiOperation(value = "拖车订单驳回")
    @PostMapping(value = "/rejectOrder")
    public CommonResult rejectOrder(@RequestBody TrailerCargoRejected trailerCargoRejected) {
        //查询拖车订单
        TrailerOrder tmp = this.trailerOrderService.getById(trailerCargoRejected.getTrailerOrderId());
        //获取相应驳回操作
        OrderStatusEnum orderStatusEnum = OrderStatusEnum.getTrailerOrderRejection(tmp.getStatus());
        if (orderStatusEnum == null) {
            return CommonResult.error(400, "驳回操作失败,没有相对应的操作");
        }
        //记录审核信息
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setExtId(trailerCargoRejected.getTrailerOrderId());
        auditInfoForm.setExtDesc(SqlConstant.TRAILER_ORDER);
        auditInfoForm.setAuditStatus(orderStatusEnum.getCode());
        auditInfoForm.setAuditTypeDesc(orderStatusEnum.getDesc());

        auditInfoForm.setAuditComment(trailerCargoRejected.getCause());

        Integer rejectOptions = trailerCargoRejected.getRejectOptions() == null ? 1 : trailerCargoRejected.getRejectOptions();
        trailerCargoRejected.setRejectOptions(rejectOptions);
        if(trailerCargoRejected.getRejectOptions().equals(2)){
            if(trailerCargoRejected.getCause()==null){
                return CommonResult.error(400, "参数错误，审核意见需要填写");
            }
        }
        switch (orderStatusEnum) {
            case TT_1_1:
                //订单驳回
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
     * 导出补料单
     */
    @Value("${address.trailerAddr}")
    private String filePath;
    @ApiOperation(value = "下载派车单")
    @GetMapping(value = "/uploadExcel")
    public void uploadExcel(@RequestParam("orderId") Long orderId, HttpServletResponse response) {

        TrailerOrderVO trailerOrderDetails = trailerOrderService.getTrailerOrderByOrderNO(orderId);

//        ClassPathResource classPathResource = new ClassPathResource("/static/派车单.xls");
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
            }else{

            }
            //HSSFWorkbook templateWorkbook = new HSSFWorkbook(inputStream);

            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            templateWorkbook.write(outStream);
            ByteArrayInputStream templateInputStream = new ByteArrayInputStream(outStream.toByteArray());

            String fileName = "派车单";

            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String filename = URLEncoder.encode(fileName, "utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + filename+".xlsx");

            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(templateInputStream).build();

            WriteSheet writeSheet = EasyExcel.writerSheet().build();

//            FillConfig fillConfig = FillConfig.builder().direction(WriteDirectionEnum.HORIZONTAL).build();
            //将集合数据填充
            excelWriter.fill(new FillWrapper("orderAddress",trailerOrderDetails.getOrderAddressForms()),writeSheet);
//            excelWriter.fill(new FillWrapper("good",trailerOrderDetails.getGoodsForms()),fillConfig,writeSheet);

            //将指定数据填充
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
            map.put("createTime", trailerOrderDetails.getCreateTime().toString().substring(0,10));
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

    /**
     * 创建拖车单
     * @param addTrailerOrderFrom
     * @return
     */
    @RequestMapping(value = "/trailer/createOrder")
    public ApiResult<String> createOrder(@RequestBody AddTrailerOrderFrom addTrailerOrderFrom) {
        String order = trailerOrderService.createOrder(addTrailerOrderFrom);
        return ApiResult.ok(order);
    }

    /**
     * 根据主订单号获取拖车订单信息
     */
    @RequestMapping(value = "/trailer/getTrailerOrderDetails")
    public ApiResult<TrailerOrderVO> getSeaOrderDetails(@RequestParam("orderNo")String orderNo){
        TrailerOrder trailerOrder = trailerOrderService.getByMainOrderNO(orderNo);
        TrailerOrderVO trailerOrderVO = trailerOrderService.getTrailerOrderByOrderNO(trailerOrder.getId());
        return ApiResult.ok(trailerOrderVO);
    }
}

