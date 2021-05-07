package com.jayud.storage.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONArray;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.jayud.common.ApiResult;
import com.jayud.common.CommonPageResult;
import com.jayud.common.CommonResult;
import com.jayud.common.RedisUtils;
import com.jayud.common.constant.SqlConstant;
import com.jayud.common.enums.OrderStatusEnum;
import com.jayud.common.enums.ProcessStatusEnum;
import com.jayud.common.enums.ResultEnum;
import com.jayud.common.utils.ConvertUtil;
import com.jayud.common.utils.DateUtils;
import com.jayud.common.utils.StringUtils;
import com.jayud.storage.feign.FileClient;
import com.jayud.storage.feign.OauthClient;
import com.jayud.storage.feign.OmsClient;
import com.jayud.storage.model.bo.*;
import com.jayud.storage.model.po.InGoodsOperationRecord;
import com.jayud.storage.model.po.StorageInputOrder;
import com.jayud.storage.model.po.StorageInputOrderDetails;
import com.jayud.storage.model.vo.*;
import com.jayud.storage.service.IInGoodsOperationRecordService;
import com.jayud.storage.service.IStorageInputOrderDetailsService;
import com.jayud.storage.service.IStorageInputOrderService;
import com.jayud.storage.service.IWarehouseGoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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

/**
 * <p>
 * 入库订单表 前端控制器
 * </p>
 *
 * @author LLJ
 * @since 2021-04-19
 */
@RestController
@Slf4j
@Api(tags = "入库订单管理")
@RequestMapping("/storageInputOrder")
public class StorageInputOrderController {

    @Autowired
    private IStorageInputOrderService storageInputOrderService;

    @Autowired
    private OmsClient omsClient;

    @Autowired
    private FileClient fileClient;

    @Autowired
    private OauthClient oauthClient;

    @Autowired
    private IStorageInputOrderDetailsService storageInputOrderDetailsService;

    @Autowired
    private IWarehouseGoodsService warehouseGoodsService;

    @Autowired
    private IInGoodsOperationRecordService inGoodsOperationRecordService;

    @Autowired
    private RedisUtils redisUtils;

    @ApiOperation("分页查询入库订单列表")
    @PostMapping("/findByPage")
    public CommonResult findByPage(@RequestBody QueryStorageOrderForm form) {

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

        Class<StorageInputOrderFormVO> storageInputOrderVOClass = StorageInputOrderFormVO.class;
        Field[] declaredFields = storageInputOrderVOClass.getDeclaredFields();
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
        IPage<StorageInputOrderFormVO> page = this.storageInputOrderService.findByPage(form);
        if (page.getRecords().size() == 0) {
            map1.put("pageInfo", new CommonPageResult(page));
            return CommonResult.success(map1);
        }
        //获取附件地址
        String prePath = String.valueOf(fileClient.getBaseUrl().getData());

        List<StorageInputOrderFormVO> records = page.getRecords();
        List<Long> trailerOrderIds = new ArrayList<>();
        List<String> mainOrder = new ArrayList<>();
        List<Long> entityIds = new ArrayList<>();

        List<String> unitCodes = new ArrayList<>();
        for (StorageInputOrderFormVO record : records) {
            trailerOrderIds.add(record.getId());
            mainOrder.add(record.getMainOrderNo());
            entityIds.add(record.getLegalEntityId());
            unitCodes.add(record.getUnitCode());
        }

        //查询法人主体
        ApiResult legalEntityResult = null;
        if (CollectionUtils.isNotEmpty(entityIds)) {
            legalEntityResult = this.oauthClient.getLegalEntityByLegalIds(entityIds);
        }

        //获取结算单位信息
        ApiResult unitCodeInfo = null;
        if (CollectionUtils.isNotEmpty(unitCodes)) {
            unitCodeInfo = this.omsClient.getCustomerByUnitCode(unitCodes);
        }

        //查询主订单信息
        ApiResult result = omsClient.getMainOrderByOrderNos(mainOrder);
        for (StorageInputOrderFormVO record : records) {
            //拼装主订单信息
            record.assemblyMainOrderData(result.getData());
            //组装法人名称
            record.assemblyLegalEntity(legalEntityResult);

            //拼装结算单位
            record.assemblyUnitCodeInfo(unitCodeInfo);

            List<WarehouseGoodsVO> list1 = warehouseGoodsService.getList(record.getId(), record.getOrderNo());
            record.assemblyGoodsInfo(list1);
            //获取该批次的入库商品信息
            List<InGoodsOperationRecord> inGoodsOperationRecords = inGoodsOperationRecordService.getListByOrderId(record.getId(), record.getOrderNo());
            record.assemblyGoodsInfo1(inGoodsOperationRecords);

        }

        map1.put("pageInfo", new CommonPageResult(page));
        return CommonResult.success(map1);
    }


    @ApiOperation("分页查询仓储入库订单列表")
    @PostMapping("/findWarehousingByPage")
    public CommonResult findWarehousingByPage(@RequestBody QueryStorageOrderForm form) {
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

        Class<StorageInputOrderWarehouseingVO> storageInputOrderVOClass = StorageInputOrderWarehouseingVO.class;
        Field[] declaredFields = storageInputOrderVOClass.getDeclaredFields();
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
        IPage<StorageInputOrderWarehouseingVO> page = this.storageInputOrderService.findWarehousingByPage(form);
        if (page.getRecords().size() == 0) {
            map1.put("pageInfo", new CommonPageResult(page));
            return CommonResult.success(map1);
        }

        List<StorageInputOrderWarehouseingVO> records = page.getRecords();
        List<Long> trailerOrderIds = new ArrayList<>();
        List<String> mainOrder = new ArrayList<>();
        List<Long> entityIds = new ArrayList<>();

        List<String> unitCodes = new ArrayList<>();
        for (StorageInputOrderWarehouseingVO record : records) {
            trailerOrderIds.add(record.getId());
            mainOrder.add(record.getMainOrderNo());
            entityIds.add(record.getLegalEntityId());
            unitCodes.add(record.getUnitCode());
        }

        //查询法人主体
        ApiResult legalEntityResult = null;
        if (CollectionUtils.isNotEmpty(entityIds)) {
            legalEntityResult = this.oauthClient.getLegalEntityByLegalIds(entityIds);
        }

        //获取结算单位信息
        ApiResult unitCodeInfo = null;
        if (CollectionUtils.isNotEmpty(unitCodes)) {
            unitCodeInfo = this.omsClient.getCustomerByUnitCode(unitCodes);
        }

        //查询主订单信息
        ApiResult result = omsClient.getMainOrderByOrderNos(mainOrder);
        for (StorageInputOrderWarehouseingVO record : records) {
            //拼装主订单信息
            record.assemblyMainOrderData(result.getData());
            //组装法人名称
            record.assemblyLegalEntity(legalEntityResult);

            //拼装结算单位
            record.assemblyUnitCodeInfo(unitCodeInfo);
            //获取订单商品信息
            List<WarehouseGoodsVO> list1 = warehouseGoodsService.getList(record.getId(), record.getOrderNo());
            record.assemblyGoodsInfo(list1);
            //获取该批次的入库商品信息
            List<InGoodsOperationRecord> inGoodsOperationRecords = inGoodsOperationRecordService.getListByWarehousingBatchNo(record.getWarehousingBatchNo());
            record.assemblyGoodsInfo1(inGoodsOperationRecords);
        }

        map1.put("pageInfo", new CommonPageResult(page));
        return CommonResult.success(map1);
    }



    @ApiOperation(value = "执行入库流程操作")
    @PostMapping(value = "/doStorageInProcessOpt")
    public CommonResult doTrailerProcessOpt(@RequestBody @Valid StorageInProcessOptForm form, BindingResult result) {

        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {
                return CommonResult.error(444, error.getDefaultMessage());
            }
        }

        if (form.getMainOrderId() == null || form.getOrderId() == null) {
            log.warn("主订单id/入库订单id必填");
            return CommonResult.error(ResultEnum.VALIDATE_FAILED);
        }
        //入库订单信息
        StorageInputOrder storageInputOrder = this.storageInputOrderService.getById(form.getOrderId());
        if (ProcessStatusEnum.COMPLETE.getCode().equals(storageInputOrder.getProcessStatus())) {
            return CommonResult.error(400, "该订单已经完成");
        }
        if (!ProcessStatusEnum.PROCESSING.getCode().equals(storageInputOrder.getProcessStatus())) {
            return CommonResult.error(400, "当前订单无法操作");
        }
        String orderProcessNode = (String) omsClient.getOrderProcessNode(storageInputOrder.getMainOrderNo(), storageInputOrder.getOrderNo(), storageInputOrder.getStatus()).getData();

        OrderStatusEnum statusEnum = OrderStatusEnum.getStorageInOrderNextStatus(orderProcessNode);
        if (statusEnum == null) {
            log.error("执行入库流程操作失败,超出流程之外 data={}", storageInputOrder);
            return CommonResult.error(ResultEnum.OPR_FAIL);
        }

        //校验参数
        form.checkProcessOpt(statusEnum);
        form.setStatus(statusEnum.getCode());

        //指令操作
        switch (statusEnum) {
            case CCI_1: //入库接单
                storageInputOrderService.warehouseReceipt(form);
                break;
            case CCI_2: //确认入仓
                boolean b = storageInputOrderService.confirmEntry(form);
                if(!b){
                    CommonResult.error(444,"确认入仓失败");
                }
                break;
            case CCI_3: //仓储入库
                boolean a = storageInputOrderService.warehousingEntry(form);
                if(!a){
                    CommonResult.error(444,"仓储入库失败");
                }
                break;
        }

        return CommonResult.success();
    }

    @ApiOperation(value = "获取入仓时的数据")
    @PostMapping(value = "/getDataInConfirmEntry")
    public CommonResult getDataInConfirmEntry(@RequestBody Map<String,Object> map){
        Long orderId = MapUtil.getLong(map, "orderId");
        StorageInputOrder storageInputOrder = storageInputOrderService.getById(orderId);
        StorageInputOrderDetails storageInputOrderDetails = storageInputOrderDetailsService.getStorageInputOrderDetails(orderId);
        if(storageInputOrderDetails == null){
            storageInputOrderDetails = new StorageInputOrderDetails();
        }

        StorageInProcessOptFormVO storageInProcessOptFormVO = ConvertUtil.convert(storageInputOrderDetails, StorageInProcessOptFormVO.class);
        List<WarehouseGoodsVO> list = warehouseGoodsService.getList(storageInputOrder.getId(), storageInputOrder.getOrderNo());
        storageInProcessOptFormVO.setWarehouseGoodsForms(list);

        for (WarehouseGoodsVO warehouseGoodsVO : list) {
            List<InGoodsOperationRecord> inGoodsOperationRecords = inGoodsOperationRecordService.getList(storageInputOrder.getId(), storageInputOrder.getOrderNo(),warehouseGoodsVO.getName());
            Double totalWeight = 0.0;
            Integer totalAmount = 0;
            Integer totalJAmount = 0;
            Integer totalPCS = 0;

            for (InGoodsOperationRecord inGoodsOperationRecord : inGoodsOperationRecords) {
                totalAmount = totalAmount + inGoodsOperationRecord.getNumber();
                totalJAmount = totalJAmount + inGoodsOperationRecord.getBoardNumber();
                totalPCS = totalPCS + inGoodsOperationRecord.getPcs();
                totalWeight = totalWeight + inGoodsOperationRecord.getWeight();
            }
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(totalJAmount).append("板").append("/")
                    .append(totalAmount).append("件").append("/")
                    .append(totalPCS).append("PCS").append("/")
                    .append(totalWeight).append("KG");
            warehouseGoodsVO.setWarehousingInformation(stringBuffer.toString());
        }
        storageInProcessOptFormVO.setWarehouseGoodsForms(list);
        ApiResult result = omsClient.getMainOrderByOrderNos(Collections.singletonList(storageInProcessOptFormVO.getMainOrderNo()));
        storageInProcessOptFormVO.assemblyMainOrderData(result);
        storageInProcessOptFormVO.setMainOrderNo(storageInputOrder.getMainOrderNo());
        storageInProcessOptFormVO.setOrderId(storageInputOrder.getId());
        storageInProcessOptFormVO.setOrderNo(storageInputOrder.getOrderNo());
        storageInProcessOptFormVO.setPlateNumber(storageInputOrder.getPlateNumber());
        storageInProcessOptFormVO.setWarehouseNumber(storageInputOrder.getWarehouseNumber());
        storageInProcessOptFormVO.setCreateTime(storageInputOrder.getCreateTime());

        return CommonResult.success(storageInProcessOptFormVO);
    }

    @ApiOperation(value = "获取仓储入库时的数据")
    @PostMapping(value = "/getDataInWarehousing")
    public CommonResult getDataInWarehousing(@RequestBody Map<String,Object> map){
        Long orderId = MapUtil.getLong(map, "orderId");
        String warehousingBatchNo = MapUtil.getStr(map, "warehousingBatchNo");
        StorageInputOrder storageInputOrder = storageInputOrderService.getById(orderId);
        StorageInputOrderDetails storageInputOrderDetails = storageInputOrderDetailsService.getStorageInputOrderDetails(orderId);
        if(storageInputOrderDetails == null){
            storageInputOrderDetails = new StorageInputOrderDetails();
        }

        StorageInProcessOptFormVO storageInProcessOptFormVO = ConvertUtil.convert(storageInputOrderDetails, StorageInProcessOptFormVO.class);
        storageInProcessOptFormVO.setWarehousingBatchNo(warehousingBatchNo);
        List<InGoodsOperationRecord> inGoodsOperationRecords = inGoodsOperationRecordService.getListByWarehousingBatchNo(warehousingBatchNo);
        List<InGoodsOperationRecordVO> inGoodsOperationRecordVOS = ConvertUtil.convertList(inGoodsOperationRecords, InGoodsOperationRecordVO.class);
        storageInProcessOptFormVO.setInGoodsOperationRecords(inGoodsOperationRecordVOS);

        ApiResult result = omsClient.getMainOrderByOrderNos(Collections.singletonList(storageInProcessOptFormVO.getMainOrderNo()));
        storageInProcessOptFormVO.assemblyMainOrderData(result);
        storageInProcessOptFormVO.setMainOrderNo(storageInputOrder.getMainOrderNo());
        storageInProcessOptFormVO.setOrderId(storageInputOrder.getId());
        storageInProcessOptFormVO.setOrderNo(storageInputOrder.getOrderNo());
        storageInProcessOptFormVO.setPlateNumber(storageInputOrder.getPlateNumber());
        storageInProcessOptFormVO.setWarehouseNumber(storageInputOrder.getWarehouseNumber());
        storageInProcessOptFormVO.setCreateTime(storageInputOrder.getCreateTime());

        return CommonResult.success(storageInProcessOptFormVO);
    }

    /**
     * 获取入库批次号
     * @param map
     * @return
     */
    @ApiOperation(value = "获取入库批次号")
    @PostMapping(value = "/getWarehousingBatchNumber")
    public CommonResult getWarehousingBatchNumber(@RequestBody Map<String,Object> map){
        String orderNo = MapUtil.getStr(map, "orderNo");
        //从redis中获取数据，没有在生成
        String s = redisUtils.get(orderNo);
        if(s!=null){
            return CommonResult.success(s);
        }
        //获取入库批次号
        String batchNumber = (String)omsClient.getWarehouseNumber("A").getData();
        redisUtils.set(orderNo,batchNumber);
        return CommonResult.success(batchNumber);
    }

    @ApiOperation(value = "判断商品信息是否能编辑和删除")
    @PostMapping(value = "/isSubmit")
    public CommonResult isSubmit(@RequestBody Map<String,Object> map) {
        String orderId = MapUtil.getStr(map, "orderId");
        StorageInputOrder storageInputOrder = this.storageInputOrderService.getById(orderId);
        if(storageInputOrder.getIsSubmit().equals(1)){
            return CommonResult.success(false);
        }
        return CommonResult.success(true);
    }

    @ApiOperation(value = "判断入库信息是否准确")
    @PostMapping(value = "/isWarehousing")
    public CommonResult isWarehousing(@RequestBody StorageInProcessOptForm form) {
        //获取该批次所有的入仓商品
        List<InGoodsOperationRecord> listByWarehousingBatchNo = inGoodsOperationRecordService.getListByWarehousingBatchNo(form.getWarehousingBatchNo());
        Integer totalNumber = 0;
        for (InGoodsOperationRecord inGoodsOperationRecord : listByWarehousingBatchNo) {
            totalNumber = totalNumber + inGoodsOperationRecord.getNumber();
        }
        Integer totalNumber1 = 0;
        for (InGoodsOperationRecordForm inGoodsOperationRecord : form.getInGoodsOperationRecords()) {
            List<GoodsLocationRecordForm> goodsLocationRecordForms = inGoodsOperationRecord.getGoodsLocationRecordForms();
            for (GoodsLocationRecordForm goodsLocationRecordForm : goodsLocationRecordForms) {
                if(goodsLocationRecordForm.getNumber() != null && goodsLocationRecordForm.getKuId() != null){
                    totalNumber1 = totalNumber1 + goodsLocationRecordForm.getNumber();
                }
            }
        }
        if(totalNumber == totalNumber1){
            return CommonResult.success();
        }
        return CommonResult.error(444,"入库件数不准确");
    }


    @ApiOperation(value = "入库订单驳回")
    @PostMapping(value = "/rejectOrder")
    public CommonResult rejectOrder(@RequestBody StorageInCargoRejected storageInCargoRejected) {
        //查询拖车订单
        StorageInputOrder tmp = this.storageInputOrderService.getById(storageInCargoRejected.getStorageInOrderId());
        //获取相应驳回操作
        OrderStatusEnum orderStatusEnum = OrderStatusEnum.getInStorageOrderRejection(tmp.getStatus());
        if (orderStatusEnum == null) {
            return CommonResult.error(400, "驳回操作失败,没有相对应的操作");
        }
        //记录审核信息
        AuditInfoForm auditInfoForm = new AuditInfoForm();
        auditInfoForm.setExtId(storageInCargoRejected.getStorageInOrderId());
        auditInfoForm.setExtDesc(SqlConstant.STORAGE_INPUT_ORDER);
        auditInfoForm.setAuditStatus(orderStatusEnum.getCode());
        auditInfoForm.setAuditTypeDesc(orderStatusEnum.getDesc());

        auditInfoForm.setAuditComment(storageInCargoRejected.getCause());

        switch (orderStatusEnum) {
            case CCI_1_1:
                //订单驳回
                this.storageInputOrderService.orderReceiving(tmp, auditInfoForm, storageInCargoRejected);
                break;
        }

        return CommonResult.success();
    }

    @ApiOperation(value = "获取二维码信息")
    @PostMapping(value = "/getQRCodeInformation")
    public CommonResult getQRCodeInformation(@RequestBody Map<String,Object> map) {
        String warehousingBatchNo = MapUtil.getStr(map, "warehousingBatchNo");

        return CommonResult.success();
    }

    @ApiOperation(value = "获取版头纸信息")
    @PostMapping(value = "/getHeaderInformation")
    public CommonResult getHeaderInformation(@RequestBody Map<String,Object> map) {
        String warehousingBatchNo = MapUtil.getStr(map, "warehousingBatchNo");
        return CommonResult.success();
    }

    //入仓单模板
    @Value("")
    private String path;

    @ApiOperation(value = "导出入仓单")
    @PostMapping(value = "/exportInWarehouseReceipt")
    public void exportInWarehouseReceipt(@RequestBody Map<String,Object> map, HttpServletResponse response){
        StorageInProcessOptFormVO storageInProcessOptFormVO = (StorageInProcessOptFormVO)this.getDataInConfirmEntry(map).getData();

        File file = new File(path);
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

            String fileName = "入仓单";

            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String filename = URLEncoder.encode(fileName, "utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + filename + ".xlsx");

            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(templateInputStream).build();

            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            excelWriter.finish();
            outStream.close();
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}

